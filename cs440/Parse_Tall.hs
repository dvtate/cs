-- CS 440 Lecture 11 Spring 2020
-- Parse an expression (recursive descent)
-- (version - Tall parse trees - include skinny trees)
--

module Parse_Tall where
import Data.Char
import Data.List

-- Language
--
-- E -> T  Ttail
-- Ttail -> + T Ttail | ε 
-- T -> F Ftail
-- Ftail -> * F Ftail | ε
-- F -> x | ( E ) 

-- The parse tree structure follows the grammar structure
--
data Ptree =
    Empty                           -- The empty parse tree
    | Id String                     -- Identifiers
    | Exp Ptree Ptree               -- For Term/Term_tail
    | Term Ptree Ptree              -- For Factor/Factor_Tail
    | Ttail Symbol Ptree Ptree      -- for Symbol Term Ttail
    | Ftail Symbol Ptree Ptree      -- Symbol Factor Ftail
    | Factor Ptree                  -- Factor id
    | Paren Ptree                   -- Factor (Expr)
    deriving (Eq, Show, Read)

-- As before, symbols are just characters
--
type Symbol = Char
type Input  = [Symbol]

-- Instead of Recognizer, we have a Parser.  Most of the parsers
-- are Parser Ptree -- they return a Ptree and leftover input.
--
type Parser t = Input -> Maybe (t, Input)


-- Parsing the empty string always succeeds and doesn't change
-- the input
--
parse_Empty :: Parser Ptree
parse_Empty input = Just (Empty, input)


-- For an expression, we look for a term and term_tail.
--
-- Grammar rule: E -> T  Ttail
--
parse_E :: Parser Ptree
parse_E input =
    case parse_T input of
        Nothing -> Nothing
        Just (term, input1) ->
            case parse_Ttail input1 of
                Nothing -> Nothing
                Just (ttail, input2) ->
                    Just (Exp term ttail, input2)

-- A term is a factor and factor tail.
--
-- Grammar rule: T -> F Ftail
--
parse_T :: Parser Ptree
parse_T input =
    case parse_F input of
        Nothing -> Nothing
        Just (factor, input1) ->
            case parse_Ftail input1 of
                Nothing -> Nothing
                Just (ftail, input2) ->
                    Just (Term factor ftail, input2)

-- A Ttail is either '+' with a term and term_tail
--
-- Grammar rule: Ttail -> + T Ttail | ε 
--
parse_Ttail :: Parser Ptree
parse_Ttail input =
    case next_symbol '+' input of
        Nothing -> parse_Empty input
        Just (symbol, input1) ->
            case parse_T input1 of
                Nothing -> Nothing
                Just (term, input2) ->
                    case parse_Ttail input2 of
                        Nothing -> Nothing
                        Just (ttail, left3) ->
                            Just (Ttail symbol term ttail, left3)


-- A factor is an identifier or parenthesized expression
--
-- Grammar rule: F -> x | paren_E
--
parse_F :: Parser Ptree
parse_F input =
    case parse_id input of
        Just (idtree, input1) -> Just(Factor idtree, input1)
        Nothing ->
            case parse_paren_E input of
                Nothing -> Nothing
                Just (paren_tree, input') -> Just(Factor paren_tree, input')

-- Parenthesized expression
--
-- Grammar rule: paren_E -> ( E )
--
parse_paren_E input = 
    case next_symbol '(' input of 
      Nothing -> Nothing
      Just (_, input1) ->
          case parse_E input1 of
              Nothing -> Nothing
              Just (expr, input2) ->
                  case next_symbol ')' input2 of
                      Nothing -> Nothing
                      Just (_, left3) ->
                          Just (Paren expr, left3)

-- An Ftail is either '*' with a factor and factor_tail or empty

--
-- Grammar rule: Ftail -> * F Ftail | ε
--
parse_Ftail :: Parser Ptree
parse_Ftail input =
    case next_symbol '*' input of
        Nothing -> parse_Empty input
        Just (symbol, input1) ->
            case parse_F input1 of
                Nothing -> Nothing
                Just (factor, input2) ->
                    case parse_Ftail input2 of
                        Nothing -> Nothing
                        Just (ftail, left3) ->
                            Just (Ftail symbol factor ftail, left3)



-- Identifier: Remove whitespace and look for an identifier string
-- return an id ptree if found.
--
parse_id :: Parser Ptree
parse_id input =
    case getId (dropSpaces input) of
        Nothing -> Nothing
        Just(idstring, input1) -> Just(Id idstring, input1)


-- Remove initial whitespace and look for an identifier strin
--
getId [] = Nothing
getId (h:input1)
    | Data.Char.isLetter h =
        let (idtail, input2) = span isAlphaNum input1
        in Just (h:idtail, input2)
    | otherwise = Nothing

-- drop initial whitespace
--
dropSpaces x = Data.List.dropWhile Data.Char.isSpace x


-- next_symbol symbol input -- Remove initial whitespace from input
-- then look for the symbol; if found, return Just(symbol, leftover)
-- 
next_symbol :: Symbol -> Parser Symbol
next_symbol symbol input =
    case dropSpaces input of
        [] -> Nothing
        (h:input1) | h == symbol -> Just(symbol, input1)
        _ -> Nothing
