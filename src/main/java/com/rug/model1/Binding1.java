package com.rug.model1;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.expander.Bindings;
import org.openprovenance.prov.interop.InteropFramework;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rug.model1.DataClass1.Reaction;
import java.net.URISyntaxException;


public class Binding1 {

    private final Template1 template;
    private final DataClass1 data;

    public enum TweetType {
        ORIGINAL,
        REACTION
    }
    
    public Binding1(Template1 template, DataClass1 data) {
        this.template = template;
        this.data = data;
    }

    public void addAuthorProps(TweetType type, String author_props_id, String credible,
                                String username, String verified,
                                String followers_count, String following_count,
                                Bindings bindings) {
        bindings.addVariable(template.qn_var(type.toString().toLowerCase() + "_author_props_id"), template.qn_tw(author_props_id));
        bindings.addAttribute(type.toString() + "_credible", credible);
        bindings.addAttribute(type.toString() + "_username", username);
        bindings.addAttribute(type.toString() + "_verified", verified);
        bindings.addAttribute(type.toString() + "_followers_count", followers_count);
        bindings.addAttribute(type.toString() + "_following_count", following_count);  
    }


    public void addPostActivity(String post_id, Bindings bindings) {
        bindings.addVariable(template.qn_var("post_id"), template.qn_tw(post_id));        
    }
    
    public void addOriginalAuthor(String original_author_id, String ag_o_name, Bindings bindings) {
        bindings.addVariable(template.qn_var("original_author_id"),template.qn_tw(original_author_id));
        bindings.addAttribute("ag_o_name", ag_o_name);
    }

    public void addOriginalTweet(String original_tweet_id, String original_text, Bindings bindings) {
        bindings.addVariable(template.qn_var("original_tweet_id"), template.qn_tw(original_tweet_id));
        bindings.addAttribute("original_text", original_text);
    }

    public void addOriginalTweetProps(String original_tweet_props_id, String ORIGINAL_properties, String ORIGINAL_created_at,
                                      String ORIGINAL_location, String ORIGINAL_like_count, String ORIGINAL_quote_count,
                                      String ORIGINAL_reply_count, String ORIGINAL_retweet_count,
                                      Bindings bindings) {
        bindings.addVariable(template.qn_var("original_tweet_props_id"), template.qn_tw(original_tweet_props_id));
        bindings.addAttribute("ORIGINAL_properties", ORIGINAL_properties);
        bindings.addAttribute("ORIGINAL_created_at", ORIGINAL_created_at);
        bindings.addAttribute("ORIGINAL_location", ORIGINAL_location);
        bindings.addAttribute("ORIGINAL_like_count", ORIGINAL_like_count);
        bindings.addAttribute("ORIGINAL_quote_count", ORIGINAL_quote_count);
        bindings.addAttribute("ORIGINAL_reply_count", ORIGINAL_reply_count);
        bindings.addAttribute("ORIGINAL_retweet_count", ORIGINAL_retweet_count);
    }

    public void addEditActivity(String edit_id, Bindings bindings) {
        bindings.addVariable(template.qn_var("edit_id"), template.qn_tw(edit_id));
    }

    public void addEditedTweet(String edited_tweet_id, String edited_text, Bindings bindings) {
        bindings.addVariable(template.qn_var("edited_tweet_id"), template.qn_tw(edited_tweet_id));
        bindings.addAttribute("edited_text", edited_text);
    }

    public void addReactActivity(String react_id, Bindings bindings) {
        bindings.addVariable(template.qn_var("react_id"), template.qn_tw(react_id));
    }

    public void addReactionAuthor(String reaction_author_id, String ag_r_name, Bindings bindings) {
        bindings.addVariable(template.qn_var("reaction_author_id"), template.qn_tw(reaction_author_id));
        bindings.addAttribute("ag_r_name", ag_r_name);
    }

    public void addReactionTweet(String reaction_tweet_id, String reply_retweet_quote, String reaction_text, Bindings bindings) {
        bindings.addVariable(template.qn_var("reaction_tweet_id"), template.qn_tw(reaction_tweet_id));
        bindings.addAttribute("reply_retweet_quote", reply_retweet_quote);
        bindings.addAttribute("reaction_text", reaction_text);
    }

    public void addReactionTweetProps(String reaction_tweet_props_id, String REACTION_properties, String REACTION_created_at,
                                      String REACTION_location, String REACTION_like_count, String REACTION_retweet_count,
                                      String REACTION_reference_id,
                                      Bindings bindings) {
        bindings.addVariable(template.qn_var("reaction_tweet_props_id"), template.qn_tw(reaction_tweet_props_id));
        bindings.addAttribute("REACTION_properties", REACTION_properties);
        bindings.addAttribute("REACTION_created_at", REACTION_created_at);
        bindings.addAttribute("REACTION_location", REACTION_location);
        bindings.addAttribute("REACTION_like_count", REACTION_like_count);
        bindings.addAttribute("REACTION_retweet_count", REACTION_retweet_count);
        bindings.addAttribute("REACTION_reference_id", REACTION_reference_id);
    }


    public void addEdited(Bindings bindings) {
        addEditedTweet("EditedTweetId", "EDIT: This is the new text of the tweet.", bindings);
        addEditActivity("editId", bindings);
    }


    public void addOriginal(Bindings bindings) {
        addPostActivity(data.getOriginal().getPostId(), bindings);      
        addOriginalAuthor(data.getOriginal().getOriginalAuthorId(), data.getOriginal().getAgOName(), bindings);
        addAuthorProps(TweetType.ORIGINAL, data.getOriginal().getAuthorPropsId(), data.getOriginal().getCredible(), 
                       data.getOriginal().getUsername(), data.getOriginal().getVerified(), 
                       data.getOriginal().getFollowersCount(), data.getOriginal().getFollowingCount(), 
                       bindings);
        addOriginalTweet(data.getOriginal().getOriginalTweetId(), data.getOriginal().getOriginalText(), bindings);
        addOriginalTweetProps(data.getOriginal().getOriginalTweetPropsId(), data.getOriginal().getOriginalProperties(), data.getOriginal().getOriginalCreatedAt(), 
                              data.getOriginal().getOriginalLocation(), data.getOriginal().getOriginalLikeCount(), data.getOriginal().getOriginalQuoteCount(), 
                              data.getOriginal().getOriginalReplyCount(), data.getOriginal().getOriginalRetweetCount(), 
                              bindings);
    }


    public void addReaction(Reaction reaction, Bindings bindings) {
        addReactActivity(reaction.getReactId(), bindings);
        addReactionAuthor(reaction.getReactionAuthorId(), reaction.getAgRName(), bindings);
        addAuthorProps(TweetType.REACTION, reaction.getAuthorPropsId(), reaction.getCredible(),
                      reaction.getUsername(), reaction.getVerified(), 
                      reaction.getFollowersCount(), reaction.getFollowingCount(), 
                      bindings);
        addReactionTweet(reaction.getReactionTweetId(), reaction.getReplyRetweetQuote(), reaction.getReactionText(), bindings);
        addReactionTweetProps(reaction.getReactionTweetPropsId(), reaction.getReactionProperties(), reaction.getReactionCreatedAt(), 
                              reaction.getReactionLocation(), reaction.getReactionLikeCount(), 
                              reaction.getReactionRetweetCount(), reaction.getReactionReferenceId(), 
                              bindings);
    }

    public void addReactionList(Bindings bindings) {
        List<Reaction> reactionList = data.getReactionList();
        for (Reaction reaction : reactionList) {
            addReaction(reaction, bindings);
        }
    }



    public void bind(String file_json) {
        ProvFactory pFactory = template.getpFactory();

        Bindings bindings = new Bindings(pFactory);

        addOriginal(bindings);

        if(this.template.getoriginalIsEdited()) {
            addEdited(bindings);
        }

        addReactionList(bindings);


        bindings.addVariableBindingsAsAttributeBindings();
        bindings.exportToJson(file_json);
    }

    
    public static void main(String[] args) throws URISyntaxException {
        if (args.length!=1) throw new UnsupportedOperationException("main to be called with filename");
        String file_json=args[0];
        
        Template1 template = new Template1(InteropFramework.getDefaultFactory());


        String currentWorkingDirectory = System.getProperty("user.dir");
        String dataPath = currentWorkingDirectory + "/src/main/python/model1/output/data1.json";

        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read the JSON file and map it to the DataClass
            DataClass1 data = objectMapper.readValue(new File(dataPath), DataClass1.class);

            Binding1 binding = new Binding1(template, data);
            binding.bind(file_json);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
