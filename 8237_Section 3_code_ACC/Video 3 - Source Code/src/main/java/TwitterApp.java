import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TwitterApp {
    public static void main(String[] args) throws TwitterException, InterruptedException, IOException, ExecutionException {
        Twitter twitter = new TwitterFactory().getInstance();
        Lock outputLock = new ReentrantLock();

        //new TweetRetriever that should print to the console and file
        PrintStream consoleOutput = System.out;
        PrintStream fileOutput = new PrintStream(new File("tweet-monitor.txt"));

        ExecutorService executorService = Executors.newCachedThreadPool();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Please type the operation (Tweet / Search for): ");
        while(scanner.hasNextLine()) {
            String operation = scanner.nextLine();

            if(operation.equalsIgnoreCase("Tweet")) {
                String text = scanner.nextLine(); //After "Tweet"
                TweetPublisher tweetPublisher = new TweetPublisher(twitter, text);
                Future<Status> statusFuture = executorService.submit(tweetPublisher);
                System.out.println("statusFuture.get() = " + statusFuture.get());

            } else if (operation.equalsIgnoreCase("Search for")) {
                String hashtag = scanner.nextLine();
                TweetRetriever tweetsRetriever = new TweetRetriever(twitter, Arrays.asList(consoleOutput, fileOutput), hashtag, outputLock);
                executorService.submit(tweetsRetriever);
            }
        }


    }
}
