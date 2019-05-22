import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class TweetPublisher {
    private Twitter twitterHandler;

    public TweetPublisher(Twitter twitterHandler) {
        this.twitterHandler = twitterHandler;
    }

    public Status publish(String text) throws TwitterException, IOException {
        Status status = twitterHandler.updateStatus(text);
        //Write new tweet to file
        Files.write(Paths.get("myTweets.txt"),
                status.getText().getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        System.out.println("Published tweet [" + status.getText() + "]");
        return status;
    }
}
