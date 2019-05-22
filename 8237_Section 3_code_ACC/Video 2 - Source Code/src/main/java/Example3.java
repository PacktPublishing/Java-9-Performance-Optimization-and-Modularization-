import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Example3 {
    public static void main(String[] args) {
        Object lock = new Object();

        Runnable thread = () -> {
                try {
                    //Repeat forever
                    while (true) {
                        System.out.println("Thread " + Thread.currentThread().getName() + " is sleeping 1 second...");
                        Thread.sleep(1000); //This should not be inside a synchronized() block
                        synchronized (lock) { //One thread at a time can access lock
                            System.out.println("Hello from thread: " + Thread.currentThread().getName());
                        }
                    }
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
