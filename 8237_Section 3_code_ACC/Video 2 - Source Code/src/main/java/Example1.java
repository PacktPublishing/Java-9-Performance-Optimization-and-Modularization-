import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Example1 {
    public static void main(String[] args) {
        Runnable thread = () -> {
            try {
                System.out.println("Thread " + Thread.currentThread().getName() + " is sleeping 1 second...");
                Thread.sleep(1000);
                System.out.println("Hello from thread: " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.submit(thread);
        executorService.submit(thread);
        executorService.submit(thread);
        executorService.submit(thread);
        executorService.submit(thread);
        executorService.shutdown();
    }
}
