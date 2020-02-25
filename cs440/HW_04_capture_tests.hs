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



