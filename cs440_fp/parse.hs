

-- Problem → { Equations }
-- Equations → Equation | Equation , Equations
-- Equation → Expr = Expr
-- Expr → … as before, with +, * ...

-- Expr   -> Term \+ Ttail
-- Ttail  -> \+ Term Ttail | empty
-- Term   -> Factor Ftail
-- Ftail  -> \* Factor Ftail | empty
-- Factor -> Id_or_Call | Paren | Negative
-- Paren  -> \( Expr \)
-- Negative -> \- Factor
--
-- Id_or_Call -> Id (Arguments | empty)
-- Arguments  -> \( Expr Argtail \)
-- Argtail    -> \, Expr Argtail | empty

-- Factor → identifier | variable
-- Factor → constant | \( Expr \)| Function_call
-- Function_call → identifier \( (Arguments|ε) \) // includes calls like f()
-- Arguments → Term | Term , Arguments


-- recursive descent parser...



-- The parse tree structure follows the grammar structure
--
data Ptree =
    Empty                           -- The empty parse tree
    | Id String                     -- Identifiers
    | Call String [Ptree]           -- Function call, list of arguments
    | Exp Ptree Ptree               -- For Term/Term_tail
    | Term Ptree Ptree              -- For Factor/Factor_Tail
    | Ttail Symbol Ptree Ptree      -- for Symbol Term Ttail
    | Ftail Symbol Ptree Ptree      -- Symbol Factor Ftail
    | Unary Ptree                -- For - factor
    deriving (Eq, Show, Read)

