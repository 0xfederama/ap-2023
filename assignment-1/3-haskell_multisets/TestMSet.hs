import MultiSet
import Data.List (sort)
import Data.Char (toLower)

ciao :: [Char] -> [Char]
ciao str = sort (map toLower str)

readMSet :: FilePath -> IO (MSet [Char])
readMSet filename = do
  content <- readFile filename
  -- get the list of words from the content
  let wlist = words content
  -- fold on the wlist in order to create an mset
  return $ foldr (\w acc -> add acc (ciao w)) empty wlist

writeMSet :: FilePath -> MSet [Char] -> IO ()
writeMSet filename (MS mset) =
  -- map on the mset and compose the strings to write to file
  writeFile filename (unlines $ map (\(x, n) -> x ++ " - " ++ show n) mset)

main :: IO()
main = do
  -- read files to msets
  m1 <- readMSet "aux_files/anagram.txt"
  m2 <- readMSet "aux_files/anagram-s1.txt"
  m3 <- readMSet "aux_files/anagram-s2.txt"
  m4 <- readMSet "aux_files/margana2.txt"

  -- check first claim
  if elems m1 == elems m4 && m1 /= m4 then putStrLn "[ OK ] elems m1 == elems m4 && m1 != m4"
  else putStrLn "[FAIL] elems m1 != elems m4 || m1 == m4"

  -- check second claim
  if m1 == (union m2 m3) then putStrLn "[ OK ] m1 == m2 U m3"
  else putStrLn "[FAIL] m1 != m2 U m3"

  -- write m1 and m4 to file
  writeMSet "anag-out.txt" m1
  writeMSet "gana-out.txt" m4
