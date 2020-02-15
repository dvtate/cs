-- CS 440 Spring 2020
-- HW 4 skeleton  (Note this file doesn't compile)

-- Capture string matched by regular expression.
-- Extends match for regular expressions (in Lecture 7)

:{
-- Regular expressions (same as lecture 7's)
data RegExpr a
    = RE_const a
    | RE_or [RegExpr a]
    | RE_and [RegExpr a]
    | RE_star (RegExpr a) 
    | RE_any                  -- dot   - any one symbol
    | RE_in_set [a]           -- [...] - any symbol in list 
    | RE_end                  -- $  - at end of input
    | RE_empty                -- epsilon - empty (no symbols)
        deriving (Eq, Read, Show)


-- for problem # 3
match :: Eq a => RegExpr a -> [a] -> Maybe [a]

match (RE_const _) [] = Nothing
match (RE_const symbol) (head_inp : input')
    | head_inp == symbol = Just input'
    | otherwise = Nothing

match (RE_or []) _ = Nothing            -- the OR of no clauses fails
match (RE_or (rexp : regexprs')) input =
    case match rexp input of
        Nothing -> match (RE_or regexprs') input
        ok @ (Just _) -> ok

match (RE_and []) input = Just input    -- the AND of no clauses succeeds
match (RE_and (rexp : regexprs)) input =
    case match rexp input of
        Nothing -> Nothing
        ok @ (Just input') -> match (RE_and regexprs) input'


match RE_any (_ : input') = Just input'
match RE_any _ = Nothing


match RE_end [] = Just [] -- different because "" is type [Char] and [] is ambiguous but assumed [RegExpr]
match RE_end _ = Nothing

match RE_empty input = Just input

match (RE_in_set chars) (h : t)
    | h `elem` chars = Just t
    | otherwise = Nothing

match (RE_star rxp) input =
    case match rxp input of
        Nothing -> Just input
        ok @ (Just input') -> (match (RE_star rxp) input')


type Token a = [a]
type RevToken a = [a]
type Input a = [a]

capture :: Eq a => RegExpr a -> Input a -> Maybe(Token a, Input a)
capture' :: Eq a => RegExpr a -> (RevToken a, Input a) -> Maybe(RevToken a, Input a)


-- could remove need for capture' if I just used ++ operator instead of colon
-- but wanted to do was assignment said

capture rexp input = case capture' rexp ([], input) of
    Nothing -> Nothing
    ok @ (Just(revtoken, input')) -> Just(reverse revtoken, input')


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

:}


-- CS 440 Lecture 7, Spring 2020

-- Assumption: The code file has been loaded

-- Lots of tests for regular expressions

re = RE_or [ RE_and [RE_const 'a', RE_const 'b'], RE_const 'c' ]

match re "abcd" == Just "cd" -- the ab in ab|c matched, leaving cd
match re "cxyz" == Just "xyz" -- the c in ab|c matched, leaving xyz
match re "qrst" == Nothing    -- both ab and c failed to match
match re "acde" == Nothing    -- the a matched but b didn't match cde
                              -- and c didn't match acde

match (RE_const "hi") ["hi", "there"] == Just ["there"]

or1 = RE_or [RE_const 1, RE_const 2]
match or1 [1,2,3] == Just [2,3] -- (1 or 2) matched the leading 1
match or1 [2,3] == Just [3]     -- (1 or 2) matched the leading 2
match or1 [3,2] == Nothing      -- (1 or 2) doesn't match leading 3

or2 = RE_or (map RE_const "abc")
match or2 "axy" == Just "xy"
match or2 "bcd" == Just "cd"
match or2 "ccd" == Just "cd"
match or2 "dba" == Nothing

or3 = RE_or (map RE_const ["hello","goodbye"])
match or3 ["hello", "and", "goodbye"] == Just ["and", "goodbye"]
match or3 ["goodbye", "and", "hello"] == Just ["and", "hello"]
match or3 ["aloha"] == Nothing

abc = RE_and $ map RE_const "abc"   -- look for "a" then "b" then "c"
match abc "abcd" == Just "d"  -- "d" left after dropping "a", "b", "c"
match abc "ab"   == Nothing   -- "a" and "b" ok but matching "c" fails

match (RE_and [abc,abc]) "abcabcz" == Just "z"
        -- match "abc", get Just "abcz", match "abc" and get Just "z"
match (RE_and [or3,or3]) ["hello","goodbye","okay?"] == Just ["okay?"]
        -- 1st or2 matches "hello", 2nd matches "goodbye"
match (RE_and [or3,or3]) ["hello","nope"] == Nothing
        -- 1st or3 matches "hello" but 2nd or3 doesn't match "nope"



stre = RE_star (RE_and [ RE_const 'a', RE_any ])
match stre "a1a2a3a4"


-- CS 440 Spring 2020
-- Capture string matched by regular expression.
-- Some tests
-- Assumption: The code file has been loaded

-- Lots of tests for capturing match of regular expressions

-- ab|c
re = RE_or [ RE_and [RE_const 'a', RE_const 'b'], RE_const 'c' ]

capture re "abcd" == Just ("ab", "cd") -- the ab in ab|c matched, leaving cd
capture re "cxyz" == Just ("c", "xyz") -- the c in ab|c matched, leaving xyz
capture re "qrst" == Nothing    -- both ab and c failed to match
capture re "acde" == Nothing    -- the a matched but b didn't match cde
                              -- and c didn't match acde

capture (RE_const "hi") ["hi", "there"] == Just (["hi"], ["there"])

or1 = RE_or [RE_const 1, RE_const 2]
capture or1 [1,2,3] == Just ([1], [2,3]) -- (1 or 2) matched the leading 1
capture or1 [2,3] == Just ([2], [3])     -- (1 or 2) matched the leading 2
capture or1 [3,2] == Nothing      -- (1 or 2) doesn't match leading 3

or2 = RE_or (map RE_const "abc")
capture or2 "axy" == Just ("a", "xy")
capture or2 "bcd" == Just ("b", "cd")
capture or2 "ccd" == Just ("c", "cd")
capture or2 "dba" == Nothing

or3 = RE_or (map RE_const ["hello","goodbye"])
capture or3 ["hello", "and", "goodbye"] == Just (["hello"], ["and", "goodbye"])
capture or3 ["goodbye", "and", "hello"] == Just (["goodbye"], ["and", "hello"])
capture or3 ["aloha"] == Nothing

abc = RE_and $ map RE_const "abc"   -- look for "a" then "b" then "c"
capture abc "abcd" == Just ("abc", "d")  -- "d" left after dropping "a", "b", "c"
capture abc "ab"   == Nothing   -- "a" and "b" ok but matching "c" fails

capture (RE_and [abc,abc]) "abcabcz" == Just ("abcabc", "z")
        -- match "abc", get Just (xxx, "abcz"), match "abc" and get Just (xxx, "z")
capture (RE_and [or3,or3]) ["hello","goodbye","okay?"] == Just (["hello","goodbye"], ["okay?"])
        -- 1st or2 matches "hello", 2nd matches "goodbye"
capture (RE_and [or3,or3]) ["hello","nope"] == Nothing
        -- 1st or3 matches "hello" but 2nd or3 doesn't match "nope"
        
        
