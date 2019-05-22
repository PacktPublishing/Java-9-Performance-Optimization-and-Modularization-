import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TweetRetriever implements Runnable {
    private Twitter twitterHandler;
    private List<PrintStream> outputStreams = new ArrayList<>();
    private final String searchQuery;

    public TweetRetriever(Twitter twitterHandler, List<PrintStream> outputStreams, String searchQuery) {
        this.twitterHandler = twitterHandler;
        this.outputStreams = outputStreams;
        this.searchQuery = searchQuery;
    }

    @Override
    public void run() {
        List<Long> tweetIds = new ArrayList<>();
        Query query = new Query();
        query.setQuery(searchQuery);

        while (true) {
            List<Status> tweets = null;
            try {
                tweets = twitterHandler.search(query).getTweets();
            } catch (TwitterException e) {
                System.err.println("Could not perform hashtag search" + e);
                break; //Will exit the while loop
            }
            List<Status> newTweets = tweets.stream()
                    .filter(tweet -> !tweetIds.contains(tweet.getId()))
                    .collect(Collectors.toList());

            newTweets.forEach(x -> tweetIds.add(x.getId())); //Memorize displayed tweets

            //For each new Tweet
            for (Status tweet : newTweets) {
                //Print to every output stream
                for (PrintStream outputStream : this.outputStreams) {
                    outputStream.println(searchQuery
                            + tweet.getUser().getScreenName()
                            + ": "
                            + tweet.getText()
                            + System.lineSeparator()
                            + "----------------------");
                }
            }
            try {
                Thread.sleep(5000); //Sleep 5 seconds between queries
            } catch (InterruptedException e) {
                System.err.println("Interrupted Exception " + e);
            }
        }
    }
}
