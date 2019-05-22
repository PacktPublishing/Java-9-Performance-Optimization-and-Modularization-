import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TweetRetriever {
    private Twitter twitterHandler;
    private List<PrintStream> outputStreams = new ArrayList<>();

    public TweetRetriever(Twitter twitterHandler, List<PrintStream> outputStreams) {
        this.twitterHandler = twitterHandler;
        this.outputStreams = outputStreams;
    }

    public Status retrieveLiveTweets(String searchQuery) throws TwitterException, InterruptedException {
        List<Long> tweetIds = new ArrayList<>();
        Query query = new Query();
        query.setQuery(searchQuery);

        while (true) {
            List<Status> tweets = twitterHandler.search(query).getTweets();
            List<Status> newTweets = tweets.stream()
                    .filter(tweet -> !tweetIds.contains(tweet.getId()))
                    .collect(Collectors.toList());

            newTweets.forEach(x -> tweetIds.add(x.getId())); //Memorize displayed tweets

            //For each new Tweet
            for (Status tweet : newTweets) {
                //Print to every output stream
                for (PrintStream outputStream : this.outputStreams) {
                    outputStream.println(tweet.getUser().getScreenName()
                            + ": "
                            + tweet.getText());
                }
                Thread.sleep(5000); //Sleep 5 seconds between queries
            }
        }
    }
}
