module MultiSet (
  MSet(..), -- export the type and its data constructors for the simple tests
  empty,
  add,
  occs,
  elems,
  subeq,
  union,
  mapMSet
) where

data MSet a = MS [(a, Int)] deriving (Show)

empty :: MSet a
empty = MS []

add :: Eq a => MSet a -> a -> MSet a
add (MS []) v = MS [(v, 1)]
add (MS ((x, n):xs)) v
  | x == v    = MS ((x, n + 1):xs)
  | otherwise = let MS rest = add (MS xs) v in MS ((x, n):rest)

occs :: Eq t => MSet t -> t -> Int
occs (MS []) _ = 0
occs (MS ((x, n):xs)) v
  | x == v    = n
  | otherwise = occs (MS xs) v

elems :: MSet a -> [a]
elems (MS xs) = [x | (x, _) <- xs]

subeq :: Eq a => MSet a -> MSet a -> Bool
subeq (MS []) _ = True
subeq (MS ((x, n):xs)) mset2 = n <= occs mset2 x && subeq (MS xs) mset2

union :: Eq a => MSet a -> MSet a -> MSet a
union mset1 (MS []) = mset1
-- iterating on mset2, add every pair with its cardinality to mset1
union mset1 (MS ((x, n):xs)) = union (addtimes mset1 x n) (MS xs)
  where
    addtimes (MS []) v t = MS [(v, t)]
    addtimes (MS ((y, m):ys)) v t
      | y == v    = MS ((y, m + t):ys)
      | otherwise = let MS rest = addtimes (MS ys) v t in MS ((y, m):rest)

instance Eq a => Eq (MSet a) where
  mset1 == mset2 = subeq mset1 mset2 && subeq mset2 mset1

instance Foldable MSet where
  foldr f acc (MS xs) = foldr (\(x, _) acc' -> f x acc') acc xs

mapMSet :: Eq a => (t -> a) -> MSet t -> MSet a
-- apply the function and check if well formed (e.g. f could be x*0)
mapMSet f (MS xs) = MS (groupSame [(f x, n) | (x, n) <- xs])
  where
    -- groupSame groups all the pairs with the same x, to make mset well-formed
    groupSame [] = []
    groupSame ((y, n):ys) =
      let same = filter (\(z, _) -> z == y) ys
          rest = filter (\(z, _) -> z /= y) ys
          total = n + sum [m | (_, m) <- same]
      in (y, total) : groupSame rest

{-
It's not possible to define an instance of Functor for MSet by providing mapMSet as an
implementation of fmap because the type signature of fmap is fmap :: (a -> b) -> f a -> f b
while the signature of mapMSet also requires that a is an instance of Eq.
-}