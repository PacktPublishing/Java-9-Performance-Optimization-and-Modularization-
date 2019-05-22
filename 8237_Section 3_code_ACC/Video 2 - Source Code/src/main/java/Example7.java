import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Example7 {
    //Bank manager
    public static void main(String[] args) {
        Account account1 = new Account(200);
        Account account2 = new Account(100);

        Runnable firstTransfer = () -> account1.transferMoney(50, account2);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(new BalanceMonitor(account1));
        executorService.submit(firstTransfer);
        executorService.shutdown(); //After execution
    }

    static class BalanceMonitor implements Runnable {
        private Account account; //Which account to monitor the balance of

        public BalanceMonitor(Account account) {
            this.account = account;
        }

        @Override
        public void run() {
            while(true) {
                if (account.readLock().tryLock()) { //Balance monitor only needs to read
                    if (account.balance < 0) {
                        System.out.println("Account has negative balance.");
                    }
                    account.readLock().unlock();
                    try {
                        Thread.sleep(1000L); //Sleep 0.1 sec
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static class Account extends ReentrantReadWriteLock {
        private double balance = 0;

        public Account(double balance) {
            this.balance = balance;
        }

        public void transferMoney(double amount, Account destinationAccount) {
            boolean transferred = false;
            while(!transferred) {
                if(this.writeLock().tryLock()) { //Needs to change the values
                    if(destinationAccount.writeLock().tryLock()) { //Needs to change the values
                        this.balance -= amount;
                        destinationAccount.balance += amount;
                        System.out.println(Thread.currentThread().getName()
                                + " Transfer done. Balance on source account: " + this.balance
                                + ". Balance on destination account: " + destinationAccount.balance);
                        this.writeLock().unlock();
                        destinationAccount.writeLock().unlock();
                        break; //exit the while(true) loop
                    } else {
                        System.out.println(Thread.currentThread().getName() + " Failed to lock destination account to transfer " + amount + ". Retrying.");
                        this.writeLock().unlock();
                    }
                } else {
                    System.out.println(Thread.currentThread().getName() + " Failed to lock source account to transfer " + amount + ". Retrying.");
                }
                try {
                    Thread.sleep(1000L); //Sleep 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
