import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Example4 {
    public static void main(String[] args) {
        Object lock1 = new Object();
        Object lock2 = new Object();

        Runnable thread1 = () -> {
            synchronized (lock1) {
                System.out.println(Thread.currentThread().getName() + " got lock 1");
                System.out.println(Thread.currentThread().getName() + " waiting to get lock 2");
                synchronized (lock2) {
                    System.out.println(Thread.currentThread().getName() + " has both lock 1 and lock 2");
                }
            }
        };

        Runnable thread2 = () -> {
            synchronized (lock2) {
                System.out.println(Thread.currentThread().getName() + " got lock 2");
                System.out.println(Thread.currentThread().getName() + " waiting to get lock 1");
                synchronized (lock1) {
                    System.out.println(Thread.currentThread().getName() + " has both lock 1 and lock 2");
                }
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(thread1);
        executorService.submit(thread2);
        executorService.shutdown(); //After execution

    }
}
