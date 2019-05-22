import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Example6 {
    //Bank manager
    public static void main(String[] args) {
        Account account1 = new Account(200);
        Account account2 = new Account(100);

        Runnable firstTransfer = () -> account1.transferMoney(50, account2);
        Runnable secondTransfer = () -> account2.transferMoney(30, account1);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(firstTransfer);
        executorService.submit(secondTransfer);
        executorService.shutdown(); //After execution
    }

    static class Account extends ReentrantLock {
        private double balance = 0;

        public Account(double balance) {
            this.balance = balance;
        }

        public void transferMoney(double amount, Account destinationAccount) {
            boolean transferred = false;
            while(!transferred) {
                System.out.println(Thread.currentThread().getName() + " is trying to lock source account.");
                if(this.tryLock()) {
                    System.out.println(Thread.currentThread().getName() + " is trying to lock destination account.");
                    if(destinationAccount.tryLock()) {
                        this.balance -= amount;
                        destinationAccount.balance += amount;
                        System.out.println(Thread.currentThread().getName()
                                + " Transfer done. Balance on source account: " + this.balance
                                + ". Balance on destination account: " + destinationAccount.balance);
                        break; //exit the while(true) loop
                    } else {
                        System.out.println(Thread.currentThread().getName() + " Failed to lock destination account to transfer " + amount + ". Retrying.");
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " Failed to lock source account to transfer " + amount + ". Retrying.");
                }
                try {
                    Thread.sleep(1000); //Sleep 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
