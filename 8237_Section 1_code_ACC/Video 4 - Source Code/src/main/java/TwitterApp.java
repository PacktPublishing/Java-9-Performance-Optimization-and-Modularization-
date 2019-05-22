import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;

public class TwitterApp {
    public static void main(String[] args) throws TwitterException, InterruptedException, IOException {
        Twitter twitter = new TwitterFactory().getInstance();

        TweetPublisher tweetPublisher = new TweetPublisher(twitter);
        tweetPublisher.publish("Test tweet 1");
        Thread.sleep(1000);
        tweetPublisher.publish("Test tweet 2");
        Thread.sleep(1000);
        tweetPublisher.publish("Test tweet 3");
        Thread.sleep(1000);

        //new TweetRetriever that should print to the console and a file
        PrintStream fileOutput = new PrintStream(
                new FileOutputStream("outputFile.txt"));
        PrintStream consoleOutput = System.out;

        TweetRetriever tweetRetriever = new TweetRetriever(twitter,
                Arrays.asList(consoleOutput, fileOutput));
        tweetRetriever.retrieveLiveTweets("#java");
        fileOutput.close();
    }

}
