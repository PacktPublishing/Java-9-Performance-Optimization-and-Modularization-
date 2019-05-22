import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class TwitterApp {
    public static void main(String[] args) throws TwitterException {
        Twitter twitter = new TwitterFactory().getInstance();

        String status = "My first Tweet";
        Status statusObject = twitter.updateStatus(status);

        System.out.println("Updated Twitter status to [" + statusObject.getText() + "]");
    }

}
