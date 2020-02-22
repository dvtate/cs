-- CS 440 Lecture 12 Spring 2020
--
-- We look at ways to get rid of the case … of Nothing -> Nothing
-- code by using a function (bind) that does that.  We call the
-- bind function with a Maybe value and a function that takes
-- an actual value and returns a Maybe result.  Using bind
-- lets us concentrate on the actionable code.
--

import Parse_Short

bind :: Maybe a -> (a -> Maybe b) -> Maybe b
bind Nothing f = Nothing
bind (Just val) f = f val

-- Use bind on named functions; fails to compile because level3 wants to use expr_tree,
-- which is local to level2
-- parse_paren_E input        = bind (next_symbol '(' input) level1
-- level1 (lparen, input1)    = bind (parse_E input1) level2
-- level2 (expr_tree, input2) = bind (next_symbol ')' input2) level3
-- level3 (rparen, input3)    = Just (expr_tree, input3) *** ERR ***

-- This embeds level3 inside level2, which makes expr_tree available to level3.
-- Since level2 doesn't use any nonlocal variables, we don't need to embed it
-- within level1.
--
parse_paren_E2 input =
    let level1 (lparen, input1) = 
            bind (parse_E input1) level2
        level2 (expr_tree, input2) =
            let level3 (rparen, input3) = Just (expr_tree, input3)
            in bind (next_symbol ')' input2) level3
    in bind (next_symbol '(' input) level1

-- For completeness, here each level is completely embedded in the one above it.
--
parse_paren_E3 input = 
    let level1 (lparen, input1) =
          let level2 (expr_tree, input2) =
                let level3 (rparen, input3) =
                      Just (expr_tree, input3)
                in bind (next_symbol ')' input2) level3
          in bind (parse_E input1) level2 
    in bind (next_symbol '(' input) level1

-- Use unnamed lambdas with bind in prefix.  Just for an alternate style,
-- each closing right paren appears in the same column as its left paren
-- (if it doesn't go on the same line as the left paren).
--
parse_paren_E4 input =
    bind (next_symbol '(' input)
        (\(lparen, input1) ->
            bind (parse_E input1)
                (\(expr_tree, input2) ->
                    bind (next_symbol ')' input2)
                        (\ (rparen, input3) -> 
                                Just (expr_tree, input3)
                        )
                )
        )

-- Again use unnamed lambdas but with bind in infix.  The lambda headers
-- (i.e., function parameters) are on the right, actions are on the left.
--
parse_paren_E5 input =
    (next_symbol '(' input)     `bind ` (\ (lparen, input1) ->
    (parse_E input1)            `bind ` (\ (expr_tree, input2) ->
    (next_symbol ')' input2)    `bind ` (\ (rparen, input3) -> 
    Just (expr_tree, input3) )))


--------------------------------------------------
-- Output
-- 
-- Prelude> :l using_bind.hs
-- [1 of 2] Compiling Parse_Short      ( Parse_Short.hs, interpreted )
-- [2 of 2] Compiling Main             ( using_bind.hs, interpreted )
-- Ok, two modules loaded.
-- *Main> parse_paren_E "(x+y*z)"
-- Just (Exp (Id "x") (Ttail '+' (Term (Id "y") (Ftail '*' (Id "z") Empty)) Empty),"")
-- *Main> parse_paren_E2 "(x+y*z)"
-- Just (Exp (Id "x") (Ttail '+' (Term (Id "y") (Ftail '*' (Id "z") Empty)) Empty),"")
-- *Main> parse_paren_E3 "(x+y*z)"
-- Just (Exp (Id "x") (Ttail '+' (Term (Id "y") (Ftail '*' (Id "z") Empty)) Empty),"")
-- *Main> parse_paren_E4 "(x+y*z)"
-- Just (Exp (Id "x") (Ttail '+' (Term (Id "y") (Ftail '*' (Id "z") Empty)) Empty),"")
-- *Main> parse_paren_E5 "(x+y*z)"
-- Just (Exp (Id "x") (Ttail '+' (Term (Id "y") (Ftail '*' (Id "z") Empty)) Empty),"")
