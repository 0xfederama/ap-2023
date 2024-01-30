import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import framework.AJob;
import framework.JobSchedulerStrategy;
import framework.Pair;

public class CiaoStrategy implements JobSchedulerStrategy<String, String> {

    private final String dirname;

    public CiaoStrategy(String dirname) {
        this.dirname = dirname;
    }

    /**
     * Read the files in the directory, and for each file create a ciao job.
     *
     * @return a stream of jobs
     */
    @Override
    public Stream<AJob<String, String>> emit() {
        File dir = new File(dirname);

        List<AJob<String, String>> jobs = new ArrayList<>();
        for (File file : dir.listFiles()) {
            // if file ends with txt, add to jobs list
            if (file.getName().endsWith(".txt")) {
                jobs.add(new CiaoJob(file.getAbsolutePath()));
            }
        }
        return jobs.stream();
    }

    /**
     * Writes the result of the jobs execution to "count_anagrams.txt".
     * For each pair, print to file the ciao of the word and the number of words
     * that have that ciao.
     *
     * @param the stream of pairs to write to file
     */
    @Override
    public void output(Stream<Pair<String, List<String>>> result) {
        // open the writer to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File("count_anagrams.txt")))) {
            result.forEach(pair -> {
                try {
                    // print to file
                    writer.write(pair.getKey() + " - " + pair.getValue().size() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Wrote stream to file");
        }
    }
}
