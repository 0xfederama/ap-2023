import functools
import time
import threading
import statistics
import json


def bench(n_threads=1, seq_iter=1, iter=1):
    def decorator_bench(func):
        @functools.wraps(func)
        def wrapper_bench(*args, **kwargs):
            # function to run func for seq_iter times
            def run_times(*args, **kwargs):
                for _ in range(seq_iter):
                    func(*args, *kwargs)

            results = []

            # do everything iter times
            for _ in range(iter):
                start_time = time.perf_counter()

                # launch n threads
                threads = []
                for _ in range(n_threads):
                    t = threading.Thread(target=run_times, args=args, kwargs=kwargs)
                    threads.append(t)
                    t.start()
                # join threads
                for t in threads:
                    t.join()

                end_time = time.perf_counter()
                exec_time = end_time - start_time
                results.append(exec_time)

            # compute stats and results
            mean = statistics.mean(results)
            variance = statistics.variance(results) if len(results) > 1 else 0

            return {
                "fun": func.__name__,
                "args": args,
                "n_threads": n_threads,
                "seq_iter": seq_iter,
                "iter": iter,
                "mean": mean,
                "variance": variance,
            }

        return wrapper_bench

    return decorator_bench


def just_wait(n):
    time.sleep(n * 0.01)


def grezzo(n):
    for i in range(2**n):
        pass


@bench(2, 2, 2)
def do_nothing():
    print("nothing")


def test(iter, fun, *args):
    for n_threads in [1, 2, 4, 8]:
        seq_iter = int(16 / n_threads)
        res = bench(n_threads, seq_iter, iter)(fun)(*args)
        print(res)

        filename = f"{fun.__name__}_{args[0]}_{n_threads}_{seq_iter}.json"
        with open(filename, "w") as file:
            s = json.dumps(res, indent=4)
            file.write(s)


if __name__ == "__main__":
    do_nothing()
    print()

    # start of the exercise
    test(16, grezzo, 10)
    print()
    test(16, just_wait, 1)


""" Results

Claim: "Two threads calling a function may take twice as
 much time as a single thread calling the function twice"

Starting with just_wait, we see that the mean execution time is inversely propotional to
 the number of threads: doubling n_threads halves the mean time.
Considering grezzo, we see that the behavior of the test is the opposite: doubling the
 number of threads, the mean execution time increases.

This difference is caused by the Global Interpreter Lock in Python: for I/O-bound tasks
 the GIL is released, while for CPU-intensive tasks the GIL is hold by the thread.
 Thus just_wait executes in parallel (because the GIL is released every time sleep is
 called), while grezzo executes sequentially (because every threads holds the GIL).
 Furthermore, the mean time for grezzo increases with the number of threads due to the
 overhead for launching and joining the threads.

In conclusion, the claim is partially true: two threads MAY take twice as much, but it
 depends on the type of the task to be executed.

"""
