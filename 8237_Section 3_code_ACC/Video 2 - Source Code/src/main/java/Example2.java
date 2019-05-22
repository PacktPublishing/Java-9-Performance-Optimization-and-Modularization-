import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Example2 {
    public static void main(String[] args) {
        Object lock = new Object();

        Runnable thread = () -> {
            //One thread at a time can access lock
            synchronized (lock) {
                try {
                    System.out.println("Thread " + Thread.currentThread().getName() + " is sleeping 1 second...");
                    Thread.sleep(1000);
                    System.out.println("Hello from thread: " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
