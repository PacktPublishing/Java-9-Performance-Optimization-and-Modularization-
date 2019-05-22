import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Example5 {
    public static void main(String[] args) {
        Lock lock1 = new ReentrantLock();
        Lock lock2 = new ReentrantLock();

        Runnable thread1 = () -> {
            while(true) {
                if(lock1.tryLock()) {
                    System.out.println(Thread.currentThread().getName() + " got lock 1.");
                    System.out.println(Thread.currentThread().getName() + " waiting to get lock 2.");
                    if(lock2.tryLock()) {
                        System.out.println(Thread.currentThread().getName() + " has both lock 1 and lock 2");
                        //Unlock both before breaking
                        lock1.unlock();
                        lock2.unlock();
                        break; //exit the while(true) loop
                    } //lock2 failed. Lock1 is still locked.
                    lock1.unlock();
                    System.out.println("Failed to gain both locks. Reverting.");
                    //Wait 1 second
                    try {
                        Thread.sleep(1000L); //1000 milliseconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Runnable thread2 = () -> {
            while(true) {
                if(lock2.tryLock()) {
                    System.out.println(Thread.currentThread().getName() + " got lock 2.");
                    System.out.println(Thread.currentThread().getName() + " waiting to get lock 1.");
                    if(lock1.tryLock()) {
                        System.out.println(Thread.currentThread().getName() + " has both lock 1 and lock 2");
                        //Unlock both before breaking
                        lock1.unlock();
                        lock2.unlock();
                        break; //exit the while(true) loop
                    } //lock1 failed. lock2 is still locked
                    lock2.unlock();
                    System.out.println("Failed to gain both locks. Reverting.");
                    //Wait 1 second
                    try {
                        Thread.sleep(1000L); //1000 milliseconds
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(thread1);
        executorService.submit(thread2);
        executorService.shutdown(); //After execution
    }
}
