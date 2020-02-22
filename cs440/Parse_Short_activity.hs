-- CS 440 Lecture 11 Spring 2020
-- Parse an expression (recursive descent)
-- (version - Shorter parse trees - shorten paths if possible)
--

-- Different from class discussion: Using a make_tail function
-- to check for Empty term/factor tail trees and just return the
-- term/factor instead.

module Parse_Short where
import Data.Char
import Data.List

-- Language
--
-- E -> T  Ttail
-- Ttail -> + T Ttail | empty
-- T -> F Ftail
-- Ftail -> * F Ftail | empty
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
--    | Factor Ptree                  -- Factor id -- no longer used
--    | Paren Ptree                   -- Factor (Expr) -- no longer used
    deriving (Eq, Show, Read)

-- As before, symbols are just characters
--
type Symbol = Char
type Input  = [Symbol]

-- Instead of Recognizer, we have a Parser.  Most of the parsers
-- are Parser Ptree -- they return a Ptree and leftover input.
--
type Parser t = Input -> Maybe (t, Input)


---------- PARSERS --------------------------------------------------
--
-- For an expression, we look for a term and term_tail.
-- If the term tail is empty, we just return the term parse tree.
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
                    Just (make_tail Exp term ttail, input2) -- might build short tree


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
                    Just (Term factor ftail, input2) -- might build short tree

-- A Ttail is either '+' with a term and term_tail
--
-- Grammar rule: Ttail -> + T Ttail | empty
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
-- Grammar rule: F -> id | ( E )
--
parse_F :: Parser Ptree
parse_F input =
    case parse_id input of
        Just (idtree, input1) -> Just(idtree, input1) -- was Factor idtree for tall trees
        Nothing ->
            case parse_paren_E input of
                Nothing -> Nothing
                Just (paren_tree, input') ->
                      Just(paren_tree, input')  -- was Factor paren_tree for tall trees

-- Parenthesized expression
--
-- Grammar rule: Paren_E -> ( E )
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
                          Just (expr, left3) -- was Paren expr for tall trees

-- An Ftail is either '*' with a factor and factor_tail or empty
--
-- Grammar rule: Ftail -> * F Ftail | empty
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


-- Parse the empty string (always succeeds and uses no input).
--
parse_Empty :: Parser Ptree
parse_Empty input = Just (Empty, input)


-- Identifier: Remove whitespace and look for an identifier string
-- return an id ptree if found.
--
parse_id :: Parser Ptree
parse_id input =
    case getId (dropSpaces input) of
        Nothing -> Nothing
        Just(idstring, input1) -> Just(Id idstring, input1)


-- next_symbol symbol input -- Remove initial whitespace from input
-- then look for the symbol; if found, return Just(symbol, leftover)
--
next_symbol :: Symbol -> Parser Symbol
next_symbol symbol input =
    case dropSpaces input of
        [] -> Nothing
        (h:input1) | h == symbol -> Just(symbol, input1)
        _ -> Nothing


---------- UTILITIES --------------------------------------------------
--
-- make_tail builds an expr using a term and term tail; it builds a term
-- using a factor and factor tail.  If the tail is empty, make_tail just
-- returns the given term or factor.  This optimization reduces the
-- number of skinny paths through the parse tree, which becomes shorter.
--
make_tail _ _ _ = Nothing -- *** STUB ***


-- Remove initial whitespace and look for an identifier string
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
