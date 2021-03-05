module CS440.FinalProject where

-- Dustin Van Tate Testa
-- A20422091


-- import CS440.RE


--------------------------------------------------------------------------------------------------
-- Regular Expressions
--------------------------------------------------------------------------------------------------

data RegExpr a
    = RE_const a
    | RE_or [RegExpr a]
    | RE_and [RegExpr a]
    | RE_star (RegExpr a)
    | RE_in_set [a]           -- [...] - any symbol in list
    | RE_empty                -- epsilon - empty (no symbols)
        deriving (Eq, Read, Show)

-- Regular expressions have their own types for token, reversed token,
-- and input.  (They're used to make type annotations more understandable.)
--
type RE_Token a = [a]
type RE_RevToken a = [a]
type RE_Input a = [a]

-- capture matches a regular expression against some input; on success,
-- it returns the matching token (= list of input symbols) and the
-- remaining input.  E.g. capture abcd abcdef = Just(abcd, ef)
--
capture  :: Eq a => RegExpr a -> RE_Input a -> Maybe(RE_Token a, RE_Input a)

-- top-level capture routine calls assistant with empty built-up token to start
--
capture rexp input = case capture' rexp ([], input) of
    Nothing -> Nothing
    Just (revtoken, input') -> Just (reverse revtoken, input')

-- capture' rexp (partial_token input) matches the expression against
-- the input given a reversed partial token; on success, it returns
-- the completed token and remaining input. The token is in reverse
-- order. E.g., capture' cd (ba, cdef) = (dcba, ef)
--
capture' :: Eq a => RegExpr a -> (RE_RevToken a, RE_Input a) -> Maybe(RE_RevToken a, RE_Input a)

-- *** STUB *** add code for RE_const, RE_or, RE_and, RE_in_set, RE_star, RE_empty
-- *** and for any other kinds of regular expressions you need.
--

capture' (RE_const _) (_, []) = Nothing
capture' (RE_const symbol) (revtoken, head_inp : input')
    | head_inp == symbol = Just((head_inp : revtoken), input')
    | otherwise = Nothing

-- the OR of no clauses fails
capture' (RE_or []) _ = Nothing
capture' (RE_or (rexp : regexprs')) (revtoken, input) =
    case capture' rexp (revtoken, input) of
        Nothing -> capture' (RE_or regexprs') (revtoken, input)
        ok @ (Just (revtoken', input')) -> ok

-- the AND of no clauses succeeds
capture' (RE_and []) (revtoken, input) = Just (revtoken, input)
capture' (RE_and (rexp : regexprs)) (revtoken, input) =
    case capture' rexp (revtoken, input) of
        Nothing -> Nothing 
        ok @ (Just (revtoken', input')) -> capture' (RE_and regexprs) (revtoken', input')

capture' (RE_in_set []) _ = Nothing
capture' (RE_in_set _) (_, []) = Nothing
capture' (RE_in_set (symbol : symbols')) (revtoken, (head_inp : input'))
    | head_inp == symbol     = Just((head_inp : revtoken), input')
    | (length symbols') == 0 = Nothing
    | otherwise              = capture' (RE_in_set symbols') (revtoken, (head_inp : input'))


capture' (RE_star rexp) (revtoken, input) = 
    case cstar rexp input [] of
        ([], input') -> Just(revtoken, input')
        (tok, input') -> Just ((reverse tok) ++ revtoken, input')
    where 
        cstar :: Eq a => RegExpr a -> [a] -> [a] -> ([a], [a])
        cstar rxp input ret = 
            case capture rxp input of
                Just (cv, input') -> cstar rxp input' (ret ++ cv)
                Nothing -> (ret, input)

capture' (RE_empty) (revtoken, input) = Just(revtoken, input)





--------------------------------------------------------------------------------------------------
-- Lexical Scanner
--------------------------------------------------------------------------------------------------


-- paying for 'purity' w/ performance...
digit1_9        = RE_in_set "123456789"
digit           = RE_in_set "0123456789"
int_literal     = RE_and [digit1_9, RE_star digit]

operator        = RE_in_set "+-*/,(){}="

lc_letter       = RE_in_set "abcdefghijklmnopqrstuvwxyz"
uc_letter       = RE_in_set "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
letter          = RE_or [ lc_letter, uc_letter]

alphanum        = RE_or [ lc_letter, uc_letter, RE_const '_', digit ]
identifier      = RE_and [ lc_letter, RE_star alphanum ]
variable        = RE_and [ uc_letter, RE_star alphanum ]

spaces          = RE_in_set " \n\t"

-- data Token  = Const String    -- integer constant
--                 | Id String    -- labelled constant identifier
--                 | Var String   -- variable identifier
--                 | Op Char      -- operator
--                 | Space        -- whitespace to ignore
--     deriving (Show, Read, Eq)

-- list of token regexps and how to convert to a token pair
-- I used strings instead of Datatypes because its easier and performance doesnt matter with haskell
scanners :: [ (RegExpr Char, a -> (String, a) ) ]
scanners = 
    [ (int_literal, \t -> ("const", t))
    , (identifier,  \t -> ("id", t))
    , (variable,    \t -> ("var", t))
    , (operator,    \t -> ("op", t))
    , (spaces,      \t -> ("space", t)) ]

-- resulting tokens (if any).  It calls a helper routine with an empty list
-- of result tokens.
scan :: String -> [(String, String)]
scan input = case scan' scanners [] input of
    Just(tokens, remaining) -> tokens
    Nothing -> []

-- scan' rexps revtokens input applies the first regular expression of
-- the list rexps to the input.  If the match succeeds, the captured
-- string is passed to a token-making routine associated with the reg
-- expr, and we add the new token to the head of our reversed list of
-- tokens found so far.  Exception: We don't add a Space token.
-- procedural equivalent:
    -- runs all the token regexpressions on the input
    --  finds longest token
    --  if longest token is `Nothing` throw syntax error
    --  push longest onto return
    -- scan input produces Just (list of tokens, remaining input)
-- scan' :: [ (RegExpr Char, a -> (String, a) ) ] -> [(String, String)] -> [Char] -> Maybe ([(String, String)], [Char])
scan' _ revtokens [] = Just(filter (\t -> (fst t) /= "space") (reverse revtokens), [])
scan' patterns revt input
    | snd ret == input = error ("Lexical scan failed on `" ++ input ++ "`\n") --Just(filter (\t -> t /= Space) (reverse revt), snd ret)
    | otherwise = scan' patterns ((fst ret) : revt) (snd ret)
    where
        -- find index of maximum element in list
        maxIndex xs = head $ filter ((== maximum xs) . (xs !!)) [0..]

        -- invoke scanner on input string
        -- scan_p :: (RegExpr Char, a -> (String, a)) -> [Char] -> (Int, ((String, a), RE_Input Char))
        scan_p scanner inp = 
            case capture (fst scanner) inp of
                Just(tok, input') -> (length tok, ((snd scanner) tok, input'))
                Nothing -> (0, (("space", ""), input))

        -- invoke all the scanners on the input
        candToks = map (\s -> scan_p s input) patterns

        -- find longest token
        ret = snd (candToks !! (maxIndex (map (\r -> fst r) candToks)))


--------------------------------------------------------------------------------------------------
-- Parsing
--------------------------------------------------------------------------------------------------

---------- UTILITIES --------------------------------------------------
-- maybe next token is same value specified
parse_token_v :: String -> [(String, String)] -> Maybe ((String, String), [(String, String)])
parse_token_v val (h:rem)
    | val == (snd h) = Just(h, rem)
    | otherwise = Nothing
parse_token_v _ [] = Nothing

-- maybe next token is same type specified
parse_token_t :: String -> [(String, String)] -> Maybe ((String, String), [(String, String)])
parse_token_t t (h:rem)
    | t == (fst h) = Just(h, rem)
    | otherwise = Nothing
parse_token_t _ [] = Nothing

--
-- The bind routine lets us take a Just val and run a function on the val.
-- If given Nothing instead, bind also yields Nothing.
--
bind :: Maybe a -> (a -> Maybe b) -> Maybe b
bind Nothing f = Nothing
bind (Just val) f = f val

--
-- The fails routine lets you call a function() if given Nothing; if
-- given Just val, the fails routine just yields that.
--
fails :: Maybe a -> (() -> Maybe a) -> Maybe a
fails Nothing f = f()
fails ok _ = ok


-- AST
data Expr = 
    Empty
    | Binary Char Expr Expr     -- +, -, etc.
    | Negative Expr           -- !, -, etc.?
    | FnCall String [Expr]      -- f(a,b,c)
    | Id String                 -- xY2_z
    | Var String                -- XY2_z
    | Num String                -- 123
    deriving (Eq, Show, Read)

type Equation = (Expr, Expr)
    -- deriving (Eq, Show, Read)

type Problem = [Equation]
    -- deriving (Eq, Show, Read)

-- Grammar:
-- Problem → { Equations }
-- Equations → Equation EquationsTail
-- EquationsTail -> , Equations | empty
-- Equation → Expr = Expr

-- Expr   -> Term Ttail
-- Ttail  -> \+ Term Ttail | empty
-- Term   -> Factor Ftail
-- Ftail  -> \* Factor Ftail | empty
-- Paren  -> \( Expr \)
-- Negative -> \- Factor
-- Factor → identifier | variable | constant | Paren | Function_call | Negative
-- Function_call → identifier (Arguments)
-- Arguments  -> Expr Argtail | empty
-- Argtail    -> \, Expr Argtail | empty




parse :: String -> (Maybe Problem)
parse input = case parse_problem (scan input) of 
    Just(prob, rem) -> Just(prob)
    Nothing -> Nothing

-- Problem -> { Equations }
parse_problem::[(String, String)]->(Maybe ([Equation], [(String, String)]))
parse_problem input = 
    (parse_token_v "{" input)    `bind` (\ (_, in1) ->
        (parse_equations in1)       `bind` (\ (ret, in2) ->
            (parse_token_v "}" in2)      `bind` (\ (_, in3) ->
                Just(ret, in3)
            )
        )
    )

-- Equations -> Equation EquationsTail
parse_equations input =
    (parse_equation input)      `bind` (\ (eq, in1) ->
        (parse_equations_tail in1)  `bind` (\ (eqs, in2) ->
            Just(eq : eqs, in2)
        )
    )

-- EquationsTail -> , Equations | empty
parse_equations_tail input =
    (parse_token_v "," input)   `bind` (\ (_, in1) -> 
        (parse_equations in1)   `bind` (\ (eqs, in2) ->
            Just(eqs, in2)
        )
    ) `fails` (\() -> Just([], input))

-- Equation -> Expr = Expr
parse_equation input = 
    (parse_expr input) `bind` (\ (e1, in1) ->
        (parse_token_v "=" in1) `bind` (\ (_, in2) ->
            (parse_expr in2) `bind` (\ (e2, in3) ->
                Just((e1, e2), in3)
            )
        )
    )

-- Expr -> Term \+ Ttail
parse_expr input =
    (parse_term input) `bind` (\ (t1, in1) ->
        (parse_term_tail in1 t1) `bind` (\ (tt, in2) ->
            Just(tt, in2)
        ) `fails` (\ () -> 
            Just(t1, in1)
        )
    )


-- Term   -> Factor Ftail
parse_term input = 
    (parse_factor input) `bind` (\ (f, in1) -> 
        (parse_factor_tail in1) `bind` (\ (ft, in2) ->
            Just(Binary '*' f ft, in2)
        ) `fails` (\ () -> 
            Just(f, in1)
        )
    )

-- Ttail  -> \+ Term | empty
parse_term_tail input term = 
    (parse_token_v "+" input) `bind` (\ (_, in1) -> 
        (parse_term in1) `bind` (\ (t, in2) -> 
            Just(Binary '+' term t, in2)
        )
    ) `fails` (\ () -> Just(term, input))
    

-- Factor →  Function_call | identifier | variable | constant | Paren | Negative
parse_factor input = 
    (parse_function_call input) 
    `fails` (\() -> parse_id input)
    `fails` (\() -> parse_variable input)
    `fails` (\() -> parse_const input)
    `fails` (\() -> parse_paren input)
    `fails` (\() -> parse_negative input)
    
-- Ftail  -> \* Factor Ftail | empty
parse_factor_tail input =
    (parse_token_v "*" input) `bind` (\ (_, in1) ->
        (parse_term in1) -- AKA: Ftail -> \* Term | empty
    )

-- parse identifier
parse_id input = 
    (parse_token_t "id" input) `bind` (\ (token, in1) ->
        Just(Id (snd token), in1)
    )

parse_const input = 
    (parse_token_t "const" input) `bind` (\ (token, in1) -> 
        Just (Num (snd token), in1)
    )

parse_variable input =
    (parse_token_t "var" input) `bind` (\ (token, in1) -> 
        Just (Var (snd token), in1)
    )

-- Function_call → identifier (Arguments)
parse_function_call input =
    (parse_id input) `bind` (\ (Id id, in1) -> 
        (parse_token_v "(" in1) `bind` (\ (_, in2) ->
            (parse_arguments in2) `bind` (\ (args, in3) -> 
                (parse_token_v ")" in3) `bind` (\ (_, in4) -> 
                    Just(FnCall id args, in4)
                )
            )
        )
    )

-- Arguments  -> Expr Argtail | empty
parse_arguments input =
    (parse_expr input) `bind` (\ (e, in1) ->
        (parse_arguments_tail in1) `bind` (\ (es, in2) -> 
            Just(e : es, in2)    
        )
    )

-- Argtail    -> \, Expr Argtail | empty
parse_arguments_tail input =
    (parse_token_v "," input) `bind` (\ (_, in1) ->
        (parse_arguments in1)     
    ) `fails` (\ () -> 
        Just([], input)
    )

-- \( expr \)
parse_paren input = 
    (parse_token_v "(" input) `bind` (\ (_, in1) ->
        (parse_expr in1) `bind` (\ (e, in2) -> 
            (parse_token_v ")" in2) `bind` (\ (_, in3) ->
                Just(e, in3)
            )
        )
    )

-- - Factor
parse_negative input = 
    (parse_token_v "-" input) `bind` (\ (_, in1) ->
        (parse_factor in1) `bind` (\ (f, in2) -> 
            Just (Negative f, in2)
        )    
    )


--------------------------------------------------------------------------------------------------
-- Substitution
--------------------------------------------------------------------------------------------------


-- wrapper for substitute'' because required
substitute :: Expr -> Expr -> Expr -> Expr
substitute expr v_from e_to = substitute'' expr (v_from, e_to)

-- apply substituion to each equation
substitute' res [] sub = res
substitute' res ((e0, e1) : eqs) sub = 
    substitute' ((substitute'' e0 sub, substitute'' e1 sub) : res) eqs sub

-- apply substitution to each branch/expr
substitute'' expr sub@(Var sv, se) = 
    case expr of
        v@(Var vn) ->
            if vn == sv then se else v
        (Binary op e0 e1) ->
            Binary op (substitute'' e0 sub) (substitute'' e1 sub)
        (Negative e0) ->
            Negative (substitute'' e0 sub)
        (FnCall n args) ->
            FnCall n (map (\arg -> substitute'' arg sub) args)
        -- Num/Id/Empty
        any -> any

--------------------------------------------------------------------------------------------------
-- Unification
--------------------------------------------------------------------------------------------------

type Substitution = (Expr, Expr) -- fst is always Var
data UnifSolution = 
    NoSolution String [(Expr, Expr)] [(Expr, Expr)]
    | Solution [(Expr, Expr)]
    deriving (Eq, Read)


-- solve unification problem by iterating over AST
unify::Problem->UnifSolution
unify problem = unify' [] problem


unify' s [] = Solution s
unify' s (eq : eqs) = 
    case eq of
        (Num n0, Num n1) ->
            if n0 == n1 then
                unify' s eqs
            else
                NoSolution ("can't unify " ++ (show n0) ++ " and " ++ (show n1)) s (eq : eqs)
                
        ((FnCall fn0 args0), (FnCall fn1 args1)) ->
            if fn0 == fn1 && (length args0) == (length args1) then
                unify' s ((zip args0 args1) ++ eqs)
            else if fn0 == fn1 && (length args0) /= (length args1) then
                NoSolution ("function " ++ fn0 ++ " called with differing number of arguments") s (eq : eqs)
            else -- TODO: replace this with substiution of fn names
                NoSolution ("differing function names " ++ (show fn0) ++ " and " ++ (show fn1)) s (eq : eqs)
                
        ((Binary op0 e00 e01), (Binary op1 e10 e11)) ->
            if op0 == op1 then
                unify' s ((e00, e10) : (e01, e11) : eqs)
            else
                NoSolution ("differing operators " ++ (show op0) ++ " and " ++ (show op1)) s (eq : eqs)

        (Negative e0, Negative e1) ->
            unify' s ((e0, e1) : eqs)

        (Id a, Id b) -> 
            if a == b then
                unify' s eqs
            else
                NoSolution ("cannot unify unique identifiers " ++ (show a) ++ " and " ++ (show b)) s (eq : eqs)
        
        -- probably should have been a parse error...
        (e, Empty) ->
            NoSolution ("failed on " ++ (show e) ++ ", maybe parse error?") s (eq : eqs)
        (Empty, e) ->
            NoSolution ("failed on " ++ (show e) ++ ", maybe parse error?") s (eq : eqs)

        (Var a, Var b) ->
            if a == b then
                unify' s eqs
            else
                case (unify' ((Var a, Var b) : (substitute' [] s (Var a, Var b))) (substitute' [] eqs (Var a, Var b)) ) of 
                    ok@(Solution sol) -> ok
                    bad ->
                        -- try other way around...
                        case (unify' (substitute' [] s (Var b, Var a)) (substitute' [] eqs (Var b, Var a)) ) of 
                            ok@(Solution sol) -> ok
                            _ -> bad

        sub@(Var v, expr) ->
            unify' (sub : (substitute' [] s sub)) (substitute' [] eqs sub)
        (expr, Var v) ->
            unify' ((Var v, expr) : (substitute' [] s (Var v, expr))) (substitute' [] eqs (Var v, expr))

        -- disparate types
        (_, _) ->
            NoSolution "cannot unify disparate expression types" s (eq : eqs)


--------------------------------------------------------------------------------------------------
-- Pretty print
--------------------------------------------------------------------------------------------------

-- equivalent to javascript String(str).join(token)
str_join str tok = foldl (++) [] (ins tok str)
    where -- put i in between elements of list
        ins i = foldr (\x ys -> x : if null ys then [] else i : ys) []


pprint::UnifSolution -> String
pprint (NoSolution m s eqs) = "Unification Failed: " ++ m ++ (
    if length s > 0 then
        "\nRemaining Equations:\n" ++ (pprint (Solution eqs)) ++
        "\nPartial Solution:\n" ++ pprint (Solution s)
    else "") ++ "\n"

pprint (Solution eqs) = if length eqs > 0 then (
        if length eqs < 3 then
            "{ " ++ (str_join (map pp_eq eqs) ", ") ++ " }\n"
        else 
            "{\n" ++ (str_join (map pp_eq eqs) ",\n") ++ "\n}\n" 
    ) else "{}\n"

pp_eq (l, r) = "  " ++ (ppexpr l) ++ " = " ++ (ppexpr r)

ppexpr (Var v) = v
ppexpr (Id c) = c
ppexpr (Num n) = n
ppexpr (Negative e) = "-" ++ (ppexpr e)
ppexpr (FnCall name args) = name ++ "(" ++ (str_join (map ppexpr args) ", ") ++ ")"
ppexpr (Binary op a b) = (ppop op a) ++ " " ++ [op] ++ " " ++ (ppop op b)
    where
        ppop op v = if op == '+' then (ppexpr v) else
            case v of
                (Binary op2 _ _) -> 
                    if op2 == '+' then
                        "(" ++ (ppexpr v) ++ ")"
                    else (ppexpr v)
                _ -> ppexpr v


instance Show UnifSolution where 
    show = pprint

--------------------------------------------------------------------------------------------------
-- Testing
--------------------------------------------------------------------------------------------------


-- try to solve problem
solve::String -> String
solve problem = "Solving: " ++ problem ++ "...\n" ++ (
    case parse problem of 
        Nothing -> "Syntax Error: failed to parse problem"
        Just prob -> 
            -- "Solving: " ++ pprint (Solution prob) ++ 
            show (unify prob))

-- unit tests
tests = [
    "{X=Y,X=3}", -- Solution: Y=3, X=3 (not Y=3, X=Y)
    "{X=1,X=3}", -- Unification fails (tries to unify 1 and 3)
    "{f(a,Y) = f(X,b), c = Z}", -- Solution: Z=c, Y=b, X=a
    "{f(X) = g(Y)}", -- Unification fails (different function names)
    "{f(X,Y) = f(X)}", -- Unification fails (different # of arguments)
    "{ab+t = ae*-2}",
    "{X=Y, X=Y+Y}",
    "{f(X, Y, Z) = f(X, X,Z), f(X, Y, Z) = f(Y, Z,Z), f(A, B, B) = f(1, 2, B)}",
    "{f(X, Y, Z) = f(X, X,Z), f(X, Y, Z) = f(Y, Z,Z), f(A, B, B) = f(x, x, x)}",
    "{T0 = n(a, T1, T2), n(a, T1, T2) = n(a, n(b, c, Z), n(d, T3, T4)),n(d, T3, n(g, h, Z)) = n(d, n(e, Z, f), T4) }",
    "{X = d(Y, Z), X = d(Z, Y)}",
    "{d(a, b) = d(A, B), f(A) = f(B)}",
    "{p(X, Y) = p(X, p(X, Z)), Y = p(A, y), X = p(x, Z)}",
    "{t(A, B, d(a, c)) = t(p(a, E), B, C), p(d(E, c), d(a, F)) = p(d(b, F), C)}",
    "{f(f(f(f(a, Z),Y),X),W) = f(W,f(X,f(Y,f(Z,a))))}" ] -- (found on Wikipedia)


-- to run tests:
-- putStrLn (foldl (++) [] (map (\t -> (solve t) ) tests))


-- [tate@archbook cs440_fp]$ ghci
-- GHCi, version 8.8.3: https://www.haskell.org/ghc/  :? for help
-- Prelude> :l fp.hs 
-- [1 of 1] Compiling CS440.FinalProject ( fp.hs, interpreted )
-- Ok, one module loaded.
-- *CS440.FinalProject> putStrLn (foldl (++) [] (map (\t -> (solve t) ) tests))
-- Solving: {X=Y,X=3}...
-- {
--   Y = 3,
--   X = 3
-- }
-- Solving: {X=1,X=3}...
-- Unification Failed: can't unify "1" and "3"
-- Partial Solution:
-- {
--   X = 1
-- }

-- Solving: {f(a,Y) = f(X,b), c = Z}...
-- {
--   Y = b,
--   X = a,
--   Z = c
-- }
-- Solving: {f(X) = g(Y)}...
-- Unification Failed: differing function names "f" and "g"
-- Solving: {f(X,Y) = f(X)}...
-- Unification Failed: function f called with differing number of arguments
-- Solving: {f(f(f(f(a, Z),Y),X),W) = f(W,f(X,f(Y,f(Z,a))))}...
-- {
--   Z = a,
--   X = f(f(a, a), f(a, a)),
--   W = f(f(f(a, a), f(a, a)), f(f(a, a), f(a, a))),
--   Y = f(a, a)
-- }
-- ...
