package framework;

import java.util.List;
import java.util.stream.Stream;

public interface JobSchedulerStrategy<K, V> {

	/**
	 * Hot stop: must be overridden.
	 *
	 * @return a stream of jobs to be executed by compute
	 */
	public Stream<AJob<K, V>> emit();

	/**
	 * Hot spot: must be overridden.
	 *
	 * @param result
	 */
	public void output(Stream<Pair<K, List<V>>> result);

}
