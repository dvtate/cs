:{
    twice (h1 : h2 : t) = (h1 == h2 || twice (h1 : t) || twice (h2 : t))
    twice a = False
:}
filter twice [[],[1],[1,2],[2,2],[1,2,3],[1,2,1],[1,1,2],[1,2,2]] == [[2,2],[1,2,1],[1,1,2],[1,2,2]]

 :{
    twice arr = case arr of
        (h1 : h2 : t) -> (h1 == h2 || twice (h1 : t) || twice (h2 : t))
        otherwise -> False
:}

filter twice [[],[1],[1,2],[2,2],[1,2,3],[1,2,1],[1,1,2],[1,2,2]] == [[2,2],[1,2,1],[1,1,2],[1,2,2]]


:{
    twice (arr)
        | length arr >= 2 = (\(h1 : h2 : t) -> (h1 == h2 || twice (h1 : t) || twice (h2 : t)) arr)
        | otherwise = False
:}

:{
    twice (arr)
        | length arr < 2 = False
        | length arr == 2 = head arr == last arr
        | otherwise = if elem (head arr) (tail arr) then True else twice (tail arr)
:}
:{
twice x = case () of 
    ()  | length arr < 2 = False
               | length arr >= 2 = if elem (head arr) (tail arr) then True else twice (tail arr)
:}
:{

:{
	

:}
