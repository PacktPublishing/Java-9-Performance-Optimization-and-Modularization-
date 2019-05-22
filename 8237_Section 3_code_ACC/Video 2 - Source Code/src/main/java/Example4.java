import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Example4 {
    public static void main(String[] args) {
        //Better solution: Common lock
        Object commonLockFor1And2 = new Object();

        Runnable thread1 = () -> {
                System.out.println(Thread.currentThread().getName() + " waiting to get commonLockFor1And2");
                synchronized (commonLockFor1And2) {
                    System.out.println(Thread.currentThread().getName() + " has commonLockFor1And2");
                }
        };

        Runnable thread2 = () -> {
                System.out.println(Thread.currentThread().getName() + " waiting to get commonLockFor1And2");
                synchronized (commonLockFor1And2) {
                    System.out.println(Thread.currentThread().getName() + " has commonLockFor1And2");
                }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(thread1);
        executorService.submit(thread2);
        executorService.shutdown(); //After execution

    }
}
