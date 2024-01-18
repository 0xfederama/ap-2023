import MultiSet

debug :: Bool
debug = True

assert :: (Eq a, Show a) => a -> a -> IO ()
assert x y =
  if x == y then do
    putStr "[ OK ] "
    if debug then putStrLn $ show y else putStrLn ""
  else do
    putStr "[FAIL] "
    putStr (show x)
    putStr " should be "
    putStrLn (show y)

main :: IO ()
main = do
  -- test empty and add
  let ms = empty
  let ms1 = add ms 1
  let ms2 = add ms1 1
  let ms3 = add ms2 3
  assert ms3 (MS [(1,2), (3,1)])

  -- test occs
  let res = occs ms3 1
  assert res 2
  let res = occs ms3 2
  assert res 0

  -- test elems
  let res = elems ms3
  assert res [1, 3]

  -- test subeq
  let ms4 = MS [(1,1), (2,1), (3,1)]
  let res = subeq ms3 ms4
  assert res False
  let ms5 = add ms4 1
  let res = subeq ms3 ms5
  assert res True

  -- test union
  let msunion = union ms3 ms4
  assert msunion (MS [(1,3), (3,2), (2,1)])

  -- test foldr
  let sumelems = foldr (+) 0 msunion
  assert sumelems 6

  -- test mapmset
  let ms6 = mapMSet id msunion
  assert ms6 msunion
  let ms7 = MS [(1,2), (4,5)]
  let ms8 = mapMSet (\x -> x*0) ms7
  assert ms8 (MS [(0,7)])