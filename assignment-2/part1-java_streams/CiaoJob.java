import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import framework.AJob;
import framework.Pair;

public class CiaoJob extends AJob<String, String> {
    private final String filename;

    public CiaoJob(String filename) {
        this.filename = filename;
    }

    /**
     * Compute the ciao of a word.
     *
     * @param word
     * @return a string formed by sorting the chars of word
     */
    private String ciao(String word) {
        return new String(word.toLowerCase().chars().sorted().toArray(), 0, word.length());
    }

    /**
     * Read the filename in the class, then split it in words, filter words with special character and
     * words shorter than 4 characters, then create pairs.
     *
     * @return a stream of pairs < ciao(word), word >
     */
    @Override
    public Stream<Pair<String, String>> execute() {
        try {
            return Files.
                    // get lines of file
                    lines(Path.of(filename))
                    // split line in words
                    .flatMap(line -> Arrays.stream(line.split("\\s+")))
                    // filter words, keep only alphabetic and longer than 4
                    .filter(word -> word.matches("^[a-zA-Z]{4,}$"))
                    // build pairs of ciao
                    .map(word -> new Pair<>(ciao(word), word.toLowerCase()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
