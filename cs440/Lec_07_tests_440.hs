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
