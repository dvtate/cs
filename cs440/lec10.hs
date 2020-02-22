-- CS 440 Lecture 10 Spring 2020
-- Recognize an expression (don't return parse trees)
--

-- Language
--
-- E -> T  Ttail
-- Ttail -> + T Ttail | ε 
-- T -> F Ftail
-- Ftail -> * F Ftail | ε
-- F -> x | ( E ) 


-- We don't need a parse tree definition but it's handy to have
-- a token type and a parser type (just as synonyms).
--
type Token = Char                   -- from lec 9 rec expr capture file
type Input = [Token]                -- from lec 9 rec expr capture file
type Parser = Input -> Maybe Input

-- We're using characters for tokens
--
plus = '+'          :: Token
times = '*'         :: Token
left_paren = '('    :: Token
right_paren = ')'   :: Token


-- Parse an expression
-- Syntax: E -> T  Ttail
--
parse_E :: Parser
parse_E input =
    case parse_T input of
        Nothing -> Nothing
        Just leftover -> parse_Ttail leftover


-- Parse a term tail
-- Syntax: Ttail -> + T Ttail | ε -- is just (+ T Ttail)*
--
parse_Ttail :: Parser
parse_Ttail input =
    case next_token plus input of
        Nothing -> parse_empty input
        Just left1 ->               -- input leftover after removing '+'
            case parse_T left1 of
                Nothing -> Nothing
                Just left2 -> parse_Ttail left2


-- Parse a term
-- Syntax: T -> F Ftail
--
parse_T :: Parser
parse_T input =
    case parse_F input of
        Nothing -> Nothing
        Just left -> parse_Ftail left


-- Parse a factor tail (* F Ftail)*
-- Syntax: Ftail -> * F Ftail | ε -- is just (* F Ftail)*
--
parse_Ftail :: Parser
parse_Ftail input =
    case next_token times input of
        Nothing -> parse_empty input
        Just left1 ->
            case parse_F left1 of
                Nothing -> Nothing
                Just left2 -> parse_Ftail left2


-- Parse a factor (an identifier or parenthesized expression)
-- Syntax: F -> x | ( E ) 
--
parse_F :: Parser
parse_F [] = Nothing
parse_F input =
    case parse_identifier input of
        Just left -> Just left                  -- saw an identifier ?
        Nothing ->                              -- if not, try parsing ( E )
            case next_token left_paren input of -- want left paren of ( E )
                Nothing -> Nothing              -- didn't see '('?  fail
                Just left2 ->
                    case parse_E left2 of       -- look for an E
                        Nothing -> Nothing      -- no E? fail
                        Just left3 ->
                            next_token right_paren left3   -- closing ')'?


-- Parsing an empty string -- always succeeds
--
parse_empty :: Parser
parse_empty input = Just input


-- Parse an identifier (very simple one: the letter 'x')
--
parse_identifier :: Parser
parse_identifier input =
    next_token 'x' input


-- See if the next input token matches the one we want
--
next_token :: Token -> Parser
next_token want_tok (token:input') | want_tok == token = Just input'
next_token _ _ = Nothing


