-- CS 440 Lecture 12 Spring 2020
-- Parse an expression (recursive descent)
-- (version using bind/fails)
--
-- Activity version

module Parse_Bind_Fail where
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
    (parse_T input)         `bind` (\ (term, input1) ->
    (parse_Ttail input1)    `bind` (\ (ttail, input2) ->
    Just (make_tail Exp term ttail, input2) ))
        -- make_tail tries to build a short tree


-- A term is a factor and factor tail.
--
-- Grammar rule: T -> F Ftail
--
parse_T :: Parser Ptree
parse_T input =
	Nothing -- *** STUB ***


-- A Ttail is either '+' with a term and term_tail
--
-- Grammar rule : Ttail -> + T Ttail | empty
--
parse_Ttail :: Parser Ptree
parse_Ttail input =
    next_symbol '+' input       `bind` (\ (symbol, input1) ->
    parse_T input1              `bind` (\ (term, input2) ->
    parse_Ttail input2          `bind` (\ (ttail, left3) ->
    Just (Ttail symbol term ttail, left3) )))
                                `fails` (\() ->
    parse_Empty input )


-- A factor is an identifier or parenthesized expression
--
-- Grammar rule: F -> id | ( E )
--
parse_F :: Parser Ptree
parse_F input =
    parse_id input          `fails` (\() ->
    parse_paren_E input )

-- Parenthesized expression
--
-- Grammar rule: Paren_E -> ( E )
--
parse_paren_E :: Parser Ptree
parse_paren_E input =
    next_symbol '(' input   `bind` (\ (_, left1) ->
    parse_E left1           `bind` (\ (etree, left2) ->
    next_symbol ')' left2   `bind` (\ (_, left3) ->
    Just (etree, left3) )))


-- An Ftail is either '*' with a factor and factor_tail or empty
--
-- Grammar rule: Ftail -> * F Ftail | empty
--
parse_Ftail :: Parser Ptree
parse_Ftail input =
    Nothing -- *** STUB ***


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
-- The bind routine lets you take a Just val and run a function on the val.
-- If given Nothing instead, bind also yields Nothing.
--
bind :: Maybe a -> (a -> Maybe b) -> Maybe b
bind Nothing f = Nothing
bind (Just val) f = f val

-- The fails routine lets you call a function() if given Nothing; if
-- given Just val, fails simply yields that.
--
fails :: Maybe a -> (() -> Maybe a) -> Maybe a
fails Nothing f = f()
fails ok _ = ok


-- make_tail builds an expr using a term and term tail; it builds a term
-- using a factor and factor tail.  If the tail is empty, make_tail just
-- returns the given term or factor.  This optimization reduces the
-- number of skinny paths through the parse tree, which becomes shorter.
--
make_tail :: (Ptree -> Ptree -> Ptree) -> Ptree -> Ptree -> Ptree
make_tail _ ptree Empty = ptree
make_tail build ptree tailtree = build ptree tailtree


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
