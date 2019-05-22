import twitter4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TwitterApp {
    public static void main(String[] args) throws TwitterException, InterruptedException {
        Twitter twitter = new TwitterFactory().getInstance();
        List<Long> tweetIds = new ArrayList<>();

        Query query = new Query();
        query.setQuery("#java");

        while(true) {
            List<Status> tweets = twitter.search(query).getTweets();
            List<Status> newTweets = tweets.stream()
                    .filter(tweet -> !tweetIds.contains(tweet.getId()))
                    .collect(Collectors.toList());

            newTweets.forEach(x -> tweetIds.add(x.getId())); //Memorize displayed tweets

            for (Status tweet : newTweets) {
                System.out.println(tweet.getUser().getScreenName() + ": "
                        + tweet.getText());
            }
            Thread.sleep(5000); //Sleep 5 seconds between queries
        }
    }

}
