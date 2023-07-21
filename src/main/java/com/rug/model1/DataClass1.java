package com.rug.model1;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DataClass1 {
    @JsonProperty("original")
    private Original original;

    @JsonProperty("reactions")
    private List<Reaction> reactionList;

    // Getter and Setter methods

    public Original getOriginal() {
        return original;
    }

    public void setOriginal(Original original) {
        this.original = original;
    }

    public List<Reaction> getReactionList() {
        return reactionList;
    }

    public void setReactionList(List<Reaction> reactionList) {
        this.reactionList = reactionList;
    }
    

    public static class Original {
        @JsonProperty("post_id")
        private String postId;
    
        @JsonProperty("original_author_id")
        private String originalAuthorId;
    
        @JsonProperty("ag_o_name")
        private String agOName;
    
        @JsonProperty("author_props_id")
        private String authorPropsId;
    
        @JsonProperty("credible")
        private String credible;
    
        @JsonProperty("username")
        private String username;
    
        @JsonProperty("verified")
        private String verified;
    
        @JsonProperty("followers_count")
        private String followersCount;
    
        @JsonProperty("following_count")
        private String followingCount;
    
        @JsonProperty("original_tweet_id")
        private String originalTweetId;
    
        @JsonProperty("original_text")
        private String originalText;
    
        @JsonProperty("original_tweet_props_id")
        private String originalTweetPropsId;

        @JsonProperty("ORIGINAL_properties")
        private String originalProperties;
    
        @JsonProperty("ORIGINAL_created_at")
        private String originalCreatedAt;
    
        @JsonProperty("ORIGINAL_location")
        private String originalLocation;
    
        @JsonProperty("ORIGINAL_like_count")
        private String originalLikeCount;
    
        @JsonProperty("ORIGINAL_quote_count")
        private String originalQuoteCount;
    
        @JsonProperty("ORIGINAL_reply_count")
        private String originalReplyCount;
    
        @JsonProperty("ORIGINAL_retweet_count")
        private String originalRetweetCount;
    
        // Getter and Setter methods
    
        public String getPostId() {
            return postId;
        }
    
        public void setPostId(String postId) {
            this.postId = postId;
        }
    
        public String getOriginalAuthorId() {
            return originalAuthorId;
        }
    
        public void setOriginalAuthorId(String originalAuthorId) {
            this.originalAuthorId = originalAuthorId;
        }
    
        public String getAgOName() {
            return agOName;
        }
    
        public void setAgOName(String agOName) {
            this.agOName = agOName;
        }
    
        public String getAuthorPropsId() {
            return authorPropsId;
        }
    
        public void setAuthorPropsId(String authorPropsId) {
            this.authorPropsId = authorPropsId;
        }
    
        public String getCredible() {
            return credible;
        }
    
        public void setCredible(String credible) {
            this.credible = credible;
        }
    
    
        public String getUsername() {
            return username;
        }
    
        public void setUsername(String username) {
            this.username = username;
        }
    
        public String getVerified() {
            return verified;
        }
    
        public void setVerified(String verified) {
            this.verified = verified;
        }
    
        public String getFollowersCount() {
            return followersCount;
        }
    
        public void setFollowersCount(String followersCount) {
            this.followersCount = followersCount;
        }
    
        public String getFollowingCount() {
            return followingCount;
        }
    
        public void setFollowingCount(String followingCount) {
            this.followingCount = followingCount;
        }
    
        public String getOriginalTweetId() {
            return originalTweetId;
        }
    
        public void setOriginalTweetId(String originalTweetId) {
            this.originalTweetId = originalTweetId;
        }
    
        public String getOriginalText() {
            return originalText;
        }
    
        public void setOriginalText(String originalText) {
            this.originalText = originalText;
        }
    
        public String getOriginalTweetPropsId() {
            return originalTweetPropsId;
        }
    
        public void setOriginalTweetPropsId(String originalTweetPropsId) {
            this.originalTweetPropsId = originalTweetPropsId;
        }
    
        public String getOriginalCreatedAt() {
            return originalCreatedAt;
        }
    
        public void setOriginalCreatedAt(String originalCreatedAt) {
            this.originalCreatedAt = originalCreatedAt;
        }

        public String getOriginalProperties() {
            return originalProperties;
        }
    
        public void setOriginalProperties(String originalProperties) {
            this.originalProperties = originalProperties;
        }
    
        public String getOriginalLocation() {
            return originalLocation;
        }
    
        public void setOriginalLocation(String originalLocation) {
            this.originalLocation = originalLocation;
        }
    
        public String getOriginalLikeCount() {
            return originalLikeCount;
        }
    
        public void setOriginalLikeCount(String originalLikeCount) {
            this.originalLikeCount = originalLikeCount;
        }
    
        public String getOriginalQuoteCount() {
            return originalQuoteCount;
        }
    
        public void setOriginalQuoteCount(String originalQuoteCount) {
            this.originalQuoteCount = originalQuoteCount;
        }
    
        public String getOriginalReplyCount() {
            return originalReplyCount;
        }
    
        public void setOriginalReplyCount(String originalReplyCount) {
            this.originalReplyCount = originalReplyCount;
        }
    
        public String getOriginalRetweetCount() {
            return originalRetweetCount;
        }
    
        public void setOriginalRetweetCount(String originalRetweetCount) {
            this.originalRetweetCount = originalRetweetCount;
        }
    }
    
    public static class Reaction {
        @JsonProperty("react_id")
        private String reactId;
    
        @JsonProperty("reaction_author_id")
        private String reactionAuthorId;
    
        @JsonProperty("ag_r_name")
        private String agRName;
    
        @JsonProperty("author_props_id")
        private String authorPropsId;
    
        @JsonProperty("credible")
        private String credible;
    
        @JsonProperty("username")
        private String username;
    
        @JsonProperty("verified")
        private String verified;
    
        @JsonProperty("followers_count")
        private String followersCount;
    
        @JsonProperty("following_count")
        private String followingCount;
    
        @JsonProperty("reaction_tweet_id")
        private String reactionTweetId;
    
        @JsonProperty("reply_retweet_quote")
        private String replyRetweetQuote;
    
        @JsonProperty("reaction_text")
        private String reactionText;
    
        @JsonProperty("reaction_tweet_props_id")
        private String reactionTweetPropsId;

        @JsonProperty("REACTION_properties")
        private String reactionProperties;
    
        @JsonProperty("REACTION_created_at")
        private String reactionCreatedAt;
    
        @JsonProperty("REACTION_location")
        private String reactionLocation;
    
        @JsonProperty("REACTION_like_count")
        private String reactionLikeCount;

        @JsonProperty("REACTION_retweet_count")
        private String reactionRetweetCount;
    
        @JsonProperty("REACTION_reference_id")
        private String reactionReferenceId;
    
        // Getter and Setter methods
    
        public String getReactId() {
            return reactId;
        }
    
        public void setReactId(String reactId) {
            this.reactId = reactId;
        }
    
        public String getReactionAuthorId() {
            return reactionAuthorId;
        }
    
        public void setReactionAuthorId(String reactionAuthorId) {
            this.reactionAuthorId = reactionAuthorId;
        }
    
        public String getAgRName() {
            return agRName;
        }
    
        public void setAgRName(String agRName) {
            this.agRName = agRName;
        }
    
        public String getAuthorPropsId() {
            return authorPropsId;
        }
    
        public void setAuthorPropsId(String authorPropsId) {
            this.authorPropsId = authorPropsId;
        }
    
        public String getCredible() {
            return credible;
        }
    
        public void setCredible(String credible) {
            this.credible = credible;
        }
    
        public String getUsername() {
            return username;
        }
    
        public void setUsername(String username) {
            this.username = username;
        }
    
        public String getVerified() {
            return verified;
        }
    
        public void setVerified(String verified) {
            this.verified = verified;
        }
    
        public String getFollowersCount() {
            return followersCount;
        }
    
        public void setFollowersCount(String followersCount) {
            this.followersCount = followersCount;
        }
    
        public String getFollowingCount() {
            return followingCount;
        }
    
        public void setFollowingCount(String followingCount) {
            this.followingCount = followingCount;
        }
    
        public String getReactionTweetId() {
            return reactionTweetId;
        }
    
        public void setReactionTweetId(String reactionTweetId) {
            this.reactionTweetId = reactionTweetId;
        }
    
        public String getReplyRetweetQuote() {
            return replyRetweetQuote;
        }
    
        public void setReplyRetweetQuote(String replyRetweetQuote) {
            this.replyRetweetQuote = replyRetweetQuote;
        }
    
        public String getReactionText() {
            return reactionText;
        }
    
        public void setReactionText(String reactionText) {
            this.reactionText = reactionText;
        }
    
        public String getReactionTweetPropsId() {
            return reactionTweetPropsId;
        }
    
        public void setReactionTweetPropsId(String reactionTweetPropsId) {
            this.reactionTweetPropsId = reactionTweetPropsId;
        }
    
        public String getReactionCreatedAt() {
            return reactionCreatedAt;
        }
    
        public void setReactionCreatedAt(String reactionCreatedAt) {
            this.reactionCreatedAt = reactionCreatedAt;
        }

        public String getReactionProperties() {
            return reactionProperties;
        }
    
        public void setReactionProperties(String reactionProperties) {
            this.reactionProperties = reactionProperties;
        }
    
        public String getReactionLocation() {
            return reactionLocation;
        }
    
        public void setReactionLocation(String reactionLocation) {
            this.reactionLocation = reactionLocation;
        }
    
        public String getReactionLikeCount() {
            return reactionLikeCount;
        }
    
        public void setReactionLikeCount(String reactionLikeCount) {
            this.reactionLikeCount = reactionLikeCount;
        }

        public String getReactionRetweetCount() {
            return reactionRetweetCount;
        }
    
        public void setReactionRetweetCount(String reactionRetweetCount) {
            this.reactionRetweetCount = reactionRetweetCount;
        }
    
        public String getReactionReferenceId() {
            return reactionReferenceId;
        }
    
        public void setReactionReferenceId(String reactionReferenceId) {
            this.reactionReferenceId = reactionReferenceId;
        }
    }
    
}
