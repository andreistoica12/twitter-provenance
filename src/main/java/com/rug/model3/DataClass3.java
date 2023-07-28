package com.rug.model3;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataClass3 {
    @JsonProperty("all_textual_tweets_at_timepoint_id")
    private String allTextualTweetsAtTimepointId;

    @JsonProperty("date")
    private String date;

    @JsonProperty("time_interval")
    private String timeInterval;

    @JsonProperty("percentage_out_of_day_tweets")
    private String percentageOutOfDayTweets;

    @JsonProperty("nr_of_original_tweets")
    private String nrOfOriginalTweets;

    @JsonProperty("nr_of_replies")
    private String nrOfReplies;

    @JsonProperty("nr_of_quotes")
    private String nrOfQuotes;

    // @JsonProperty("nr_of_retweets")
    // private String nrOfRetweets;

    @JsonProperty("post_or_react_id")
    private String postOrReactId;

    @JsonProperty("group_of_authors_id")
    private String groupOfAuthorsId;

    @JsonProperty("nr_of_distinct_authors")
    private String nrOfDistinctAuthors;

    @JsonProperty("avg_nr_of_followers_top_10_influencers")
    private String avgNrOfFollowersTop10Influencers;

    @JsonProperty("avg_nr_of_followers_all_users")
    private String avgNrOfFollowersAllUsers;

    // Getters and Setters

    public String getAllTextualTweetsAtTimepointId() {
        return allTextualTweetsAtTimepointId;
    }

    public void setAllTextualTweetsAtTimepointId(String allTextualTweetsAtTimepointId) {
        this.allTextualTweetsAtTimepointId = allTextualTweetsAtTimepointId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(String timeInterval) {
        this.timeInterval = timeInterval;
    }

    public String getPercentageOutOfDayTweets() {
        return percentageOutOfDayTweets;
    }

    public void setPercentageOutOfDayTweets(String percentageOutOfDayTweets) {
        this.percentageOutOfDayTweets = percentageOutOfDayTweets;
    }

    public String getNrOfOriginalTweets() {
        return nrOfOriginalTweets;
    }

    public void setNrOfOriginalTweets(String nrOfOriginalTweets) {
        this.nrOfOriginalTweets = nrOfOriginalTweets;
    }

    public String getNrOfReplies() {
        return nrOfReplies;
    }

    public void setNrOfReplies(String nrOfReplies) {
        this.nrOfReplies = nrOfReplies;
    }

    public String getNrOfQuotes() {
        return nrOfQuotes;
    }

    public void setNrOfQuotes(String nrOfQuotes) {
        this.nrOfQuotes = nrOfQuotes;
    }

    // public String getNrOfRetweets() {
    //     return nrOfRetweets;
    // }

    // public void setNrOfRetweets(String nrOfRetweets) {
    //     this.nrOfRetweets = nrOfRetweets;
    // }

    public String getPostOrReactId() {
        return postOrReactId;
    }

    public void setPostOrReactId(String postOrReactId) {
        this.postOrReactId = postOrReactId;
    }

    public String getGroupOfAuthorsId() {
        return groupOfAuthorsId;
    }

    public void setGroupOfAuthorsId(String groupOfAuthorsId) {
        this.groupOfAuthorsId = groupOfAuthorsId;
    }

    public String getNrOfDistinctAuthors() {
        return nrOfDistinctAuthors;
    }

    public void setNrOfDistinctAuthors(String nrOfDistinctAuthors) {
        this.nrOfDistinctAuthors = nrOfDistinctAuthors;
    }

    public String getAvgNrOfFollowersTop10Influencers() {
        return avgNrOfFollowersTop10Influencers;
    }

    public void setAvgNrOfFollowersTop10Influencers(String avgNrOfFollowersTop10Influencers) {
        this.avgNrOfFollowersTop10Influencers = avgNrOfFollowersTop10Influencers;
    }

    public String getAvgNrOfFollowersAllUsers() {
        return avgNrOfFollowersAllUsers;
    }

    public void setAvgNrOfFollowersAllUsers(String avgNrOfFollowersAllUsers) {
        this.avgNrOfFollowersAllUsers = avgNrOfFollowersAllUsers;
    }
}
