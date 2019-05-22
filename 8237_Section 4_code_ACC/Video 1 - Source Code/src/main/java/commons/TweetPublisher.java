package commons;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Callable;

public class TweetPublisher implements Callable<Status> {
    private Twitter twitterHandler;
    private final String text;

    public TweetPublisher(Twitter twitterHandler, String text) {
        this.twitterHandler = twitterHandler;
        this.text = text;
    }

    @Override
    public Status call() {
        try {
            Status status = twitterHandler.updateStatus(text);
            //Write new tweet to file
            Files.write(Paths.get("myTweets.txt"),
                    status.getText().getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Published tweet [" + status.getText() + "]");
            return status;
        } catch (TwitterException | IOException e) {
            throw new IllegalStateException("Could not publish Tweet", e);
        }
    }
}
