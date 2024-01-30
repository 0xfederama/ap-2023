package framework;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JobSchedulerContext<K, V> {

    private JobSchedulerStrategy<K, V> schedulerStrategy;

    public JobSchedulerContext(JobSchedulerStrategy<K, V> schedulerStrategy) {
        this.schedulerStrategy = schedulerStrategy;
    }

    /**
     * Frozen spot: executes the jobs.
     *
     * @param jobs
     * @return a stream of pairs, output of the job execution
     */
    private Stream<Pair<K, V>> compute(Stream<AJob<K, V>> jobs) {
        return jobs.flatMap(AJob::execute);
    }

    /**
     * Frozen spots: collects the results from the computation of the jobs.
     *
     * @param output
     * @return a stream of pairs, grouped by key of pair
     */
    private Stream<Pair<K, List<V>>> collect(Stream<Pair<K, V>> output) {
        return output.collect(
                // group by key
                Collectors.groupingBy(
                        Pair::getKey,
                        // accumulate to list
                        Collectors.mapping(Pair::getValue, Collectors.toList())))
                .entrySet().stream()
                .map(entry -> new Pair<>(entry.getKey(), entry.getValue()));
    }

    /**
     * Execute, in order: emit, compute, collect, output.
     */
    public void main() {
        schedulerStrategy.output(collect(compute(schedulerStrategy.emit())));
    }

}
