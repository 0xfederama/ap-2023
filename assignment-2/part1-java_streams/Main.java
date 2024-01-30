import framework.JobSchedulerContext;

public class Main {
    public static void main(String[] args) {
        CiaoStrategy strategy = new CiaoStrategy("books");
        JobSchedulerContext<String, String> context = new JobSchedulerContext<>(strategy);
        context.main();
    }
}
