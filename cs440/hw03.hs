-- 1:

-- tail-recursive helper function:
common_ind x y i = if (x !! i) == (y !! i) then (common_ind x y (i + 1)) else (i - 1)

common x y = ( [x !! i | i <- [0..(common_ind x y 0)]] , [x !! i | i <- [(common_ind x y 0)..(length x - 1)]], [y !! i | i <- [(common_ind x y 0)..(length y - 1)]] )

-- 2: 

data List a = Node a (List a) | Nil deriving (Show, Eq)
:{
  listShow' Nil = ""
  listShow' (Node v l) =  "," ++ (show v) ++ (listShow' l)
  listShow Nil = "[]";
  listShow (Node v l) = "[" ++ (show v) ++ (listShow' l) ++ "]"
:}


-- 3:

data Tree a b = Leaf b | Node a (Tree a b) (Tree a b) deriving (Read, Show, Eq)
:{ -- Note: always True
  isFull (Leaf a) = True
  isFull (Node a l r) = (isFull l) && (isFull r)
:}


:{
parse (Leaf v) = v 
parse (Node a b c)
   | a == "+" = (parse b) + (parse c)
   | a == "-" = (parse b) - (parse c)
   | a == "*" = (parse b) * (parse c)
   | a == "/" = (parse b) / (parse c)
:}



-- 5. `^0$|^[1-9][0-9]?[0-9]?(,[0-9][0-9][0-9])*$`
-- 6. `^[^\t]+$|^([^\t]\t?)+[^\t]$`
--   - more generic: `^\S+$|^(\S\s?)+\S$`
-- 7. `^([a-vx-z]){0,4}$`
