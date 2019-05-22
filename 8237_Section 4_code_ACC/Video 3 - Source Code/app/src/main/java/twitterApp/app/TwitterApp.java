package twitterApp.app;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.PropertyConfiguration;
import twitterApp.commons.TweetPublisher;
import twitterApp.commons.TweetRetriever;

import java.io.File;
import java.io.FileInputStream;
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
import java.util.logging.Logger;

public class TwitterApp {
    public static void main(String[] args) throws TwitterException, InterruptedException, IOException, ExecutionException {
        Logger logger = Logger.getLogger(TwitterApp.class.getName());
        FileInputStream configFile = new FileInputStream("src/main/resources/twitter4j.properties");
        PropertyConfiguration config = new PropertyConfiguration(configFile);
        Twitter twitter = new TwitterFactory(config).getInstance();

        Lock outputLock = new ReentrantLock();

        //new commons.TweetRetriever that should print to the console and file
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
                logger.info("statusFuture.get() = " + statusFuture.get());

            } else if (operation.equalsIgnoreCase("Search for")) {
                String hashtag = scanner.nextLine();
                TweetRetriever tweetsRetriever = new TweetRetriever(twitter, Arrays.asList(consoleOutput, fileOutput), hashtag, outputLock);
                executorService.submit(tweetsRetriever);
            }
        }
    }
}
