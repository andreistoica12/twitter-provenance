package com.rug;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.expander.Bindings;
import org.openprovenance.prov.interop.InteropFramework;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Date;


import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.rdf.*;
import org.openprovenance.prov.template.expander.Bindings;
import org.openprovenance.prov.template.expander.BindingsBean;
import org.openprovenance.prov.template.expander.BindingsJson;


public class Binding {

    private final Template template;

    
    public Binding(Template template) {
        this.template = template;
    }

    public enum TweetType {
        ORIGINAL,
        REACTION
    }

    public void addReactActivity(String react_id, Bindings bindings) {
        bindings.addVariable(template.qn_var("react_id"), template.qn_tw(react_id));
    }

    public void addReactionAuthor(String reaction_author_id, String ag_r_name, Bindings bindings) {
        bindings.addVariable(template.qn_var("reaction_author_id"), template.qn_tw(reaction_author_id));
        bindings.addAttribute("ag_r_name", ag_r_name);
    }

    // public void addReactionAuthorProps(String reaction_author_props_id, String REACTION_credible,
    //                                   String REACTION_name, String REACTION_username, String REACTION_verified,
    //                                   String REACTION_followers_count, String REACTION_following_count,
    //                                   Bindings bindings) {
    //     bindings.addVariable(template.qn_var("reaction_author_props_id"), template.qn_tw(reaction_author_props_id));
    //     bindings.addAttribute("REACTION_credible", REACTION_credible);
    //     bindings.addAttribute("REACTION_name", REACTION_name);
    //     bindings.addAttribute("REACTION_username", REACTION_username);
    //     bindings.addAttribute("REACTION_verified", REACTION_verified);
    //     bindings.addAttribute("REACTION_followers_count", REACTION_followers_count);
    //     bindings.addAttribute("REACTION_following_count", REACTION_following_count);       
    // }

    public void addReactionTweet(String reaction_tweet_id, String reply_retweet_quote, String reaction_text, Bindings bindings) {
        bindings.addVariable(template.qn_var("reaction_tweet_id"), template.qn_tw(reaction_tweet_id));
        bindings.addAttribute("reply_retweet_quote", reply_retweet_quote);
        bindings.addAttribute("reaction_text", reaction_text);
    }

    public void addReactionTweetProps(String reaction_tweet_props_id, String REACTION_created_at,
                                      String REACTION_location, String REACTION_reference_type, String REACTION_reference_id,
                                      Bindings bindings) {
        bindings.addVariable(template.qn_var("reaction_tweet_props_id"), template.qn_tw(reaction_tweet_props_id));
        bindings.addAttribute("REACTION_created_at", REACTION_created_at);
        bindings.addAttribute("REACTION_location", REACTION_location);
        bindings.addAttribute("REACTION_reference_type", REACTION_reference_type);
        bindings.addAttribute("REACTION_reference_id", REACTION_reference_id);
    }

    public void addReaction(Bindings bindings) {
        // bindings.addVariable(template.qn_var("react_id"), template.qn_tw("reactId2"));
        addReactActivity("reactId2", bindings);
        // bindings.addVariable(template.qn_var("reaction_author_id"), template.qn_tw("reactionAuthorId2"));
        // bindings.addAttribute("ag_r_name", "Some second reaction author name");
        addReactionAuthor("reactionAuthorId2", "Some second reaction author name", bindings);
        // bindings.addVariable(template.qn_var("reaction_author_props_id"), template.qn_tw("ReactionAuthorPropsId2"));
        // bindings.addAttribute("REACTION_credible", "1");
        // bindings.addAttribute("REACTION_name", "Some second reaction author name");
        // bindings.addAttribute("REACTION_username", "Some second reaction author username");
        // bindings.addAttribute("REACTION_verified", "True");
        // bindings.addAttribute("REACTION_followers_count", "100");
        // bindings.addAttribute("REACTION_following_count", "200");
        addAuthorProps(TweetType.REACTION, "ReactionAuthorPropsId2", "1",
                      "Some second reaction author name", "Some second reaction author username", "True", 
                      "100", "200", 
                      bindings);
        // bindings.addVariable(template.qn_var("reaction_tweet_id"), template.qn_tw("ReactionTweetId2"));
        // bindings.addAttribute("reply_retweet_quote", "quote");
        // bindings.addAttribute("reaction_text", "This is the text of the quote.");
        addReactionTweet("ReactionTweetId2", "quote", "This is the text of the quote.", bindings);
        // bindings.addVariable(template.qn_var("reaction_tweet_props_id"), template.qn_tw("ReactionTweetPropsId2"));
        // bindings.addAttribute("REACTION_created_at", "Some other timestamp");
        // bindings.addAttribute("REACTION_location", "Some other place");
        // bindings.addAttribute("REACTION_reference_type", "quoted");
        // bindings.addAttribute("REACTION_reference_id", "985929573745");
        addReactionTweetProps("ReactionTweetPropsId2", "Some other timestamp", 
                              "Some other place", "quoted", "985929573745", 
                              bindings);
    }

    public void addOriginalTweet(String original_tweet_id, String original_text, Bindings bindings) {
        bindings.addVariable(template.qn_var("original_tweet_id"), template.qn_tw(original_tweet_id));
        bindings.addAttribute("original_text", original_text);
    }

    public void addPostActivity(String post_id, Bindings bindings) {
        bindings.addVariable(template.qn_var("post_id"), template.qn_tw(post_id));        
    }

    public void addOriginalAuthor(String original_author_id, String ag_o_name, Bindings bindings) {
        bindings.addVariable(template.qn_var("original_author_id"),template.qn_tw(original_author_id));
        bindings.addAttribute("ag_o_name", ag_o_name);
    }

    public void addOriginalTweetProps(String original_tweet_props_id, String ORIGINAL_created_at,
                                      String ORIGINAL_location, String ORIGINAL_like_count, String ORIGINAL_quote_count,
                                      String ORIGINAL_reply_count, String ORIGINAL_retweet_count,
                                      Bindings bindings) {
        bindings.addVariable(template.qn_var("original_tweet_props_id"), template.qn_tw(original_tweet_props_id));
        bindings.addAttribute("ORIGINAL_created_at", ORIGINAL_created_at);
        bindings.addAttribute("ORIGINAL_location", ORIGINAL_location);
        bindings.addAttribute("ORIGINAL_like_count", ORIGINAL_like_count);
        bindings.addAttribute("ORIGINAL_quote_count", ORIGINAL_quote_count);
        bindings.addAttribute("ORIGINAL_reply_count", ORIGINAL_reply_count);
        bindings.addAttribute("ORIGINAL_retweet_count", ORIGINAL_retweet_count);
    }

    public void addEditedTweet(String edited_tweet_id, String edited_text, Bindings bindings) {
        bindings.addVariable(template.qn_var("edited_tweet_id"), template.qn_tw(edited_tweet_id));
        bindings.addAttribute("edited_text", edited_text);
    }

    public void addEditActivity(String edit_id, Bindings bindings) {
        bindings.addVariable(template.qn_var("edit_id"), template.qn_tw(edit_id));
    }

    // public void addOriginalAuthorProps(String original_author_props_id, String ORIGINAL_credible,
    //                                    String ORIGINAL_name, String ORIGINAL_username, String ORIGINAL_verified,
    //                                    String ORIGINAL_followers_count, String ORIGINAL_following_count,
    //                                    Bindings bindings) {
    //     bindings.addVariable(template.qn_var("original_author_props_id"), template.qn_tw(original_author_props_id));
    //     bindings.addAttribute("ORIGINAL_credible", ORIGINAL_credible);
    //     bindings.addAttribute("ORIGINAL_name", ORIGINAL_name);
    //     bindings.addAttribute("ORIGINAL_username", ORIGINAL_username);
    //     bindings.addAttribute("ORIGINAL_verified", ORIGINAL_verified);
    //     bindings.addAttribute("ORIGINAL_followers_count", ORIGINAL_followers_count);
    //     bindings.addAttribute("ORIGINAL_following_count", ORIGINAL_following_count);       
    // }

    public void addAuthorProps(TweetType type, String author_props_id, String credible,
                                String name, String username, String verified,
                                String followers_count, String following_count,
                                Bindings bindings) {
        bindings.addVariable(template.qn_var(type.toString().toLowerCase() + "_author_props_id"), template.qn_tw(author_props_id));
        bindings.addAttribute(type.toString() + "_credible", credible);
        bindings.addAttribute(type.toString() + "_name", name);
        bindings.addAttribute(type.toString() + "_username", username);
        bindings.addAttribute(type.toString() + "_verified", verified);
        bindings.addAttribute(type.toString() + "_followers_count", followers_count);
        bindings.addAttribute(type.toString() + "_following_count", following_count);  
    }


                


    public void addOriginal(Bindings bindings) {
        // bindings.addVariable(template.qn_var("original_tweet_id"), template.qn_tw("OriginalTweetId"));
        // bindings.addAttribute("original_text", "This is the text of the tweet.");
        addOriginalTweet("OriginalTweetId", "This is the text of the tweet.", bindings);
        // bindings.addVariable(template.qn_var("post_id"), template.qn_tw("postId"));  
        addPostActivity("postId", bindings);      
        // bindings.addVariable(template.qn_var("original_author_id"),template.qn_tw("originalAuthorId"));
        // bindings.addAttribute("ag_o_name", "Some original author name");
        addOriginalAuthor("originalAuthorId", "Some original author name", bindings);
        // bindings.addVariable(template.qn_var("original_tweet_props_id"), template.qn_tw("OriginalTweetPropsId"));
        // bindings.addAttribute("ORIGINAL_created_at", "Some timestamp");
        // bindings.addAttribute("ORIGINAL_location", "Some place");
        // bindings.addAttribute("ORIGINAL_like_count", "20");
        // bindings.addAttribute("ORIGINAL_quote_count", "20");
        // bindings.addAttribute("ORIGINAL_reply_count", "20");
        // bindings.addAttribute("ORIGINAL_retweet_count", "20");
        addOriginalTweetProps("OriginalTweetPropsId", "Some timestamp", 
                              "Some place", "20", "20", 
                              "20", "20", 
                              bindings);
        // bindings.addVariable(template.qn_var("edited_tweet_id"), template.qn_tw("EditedTweetId"));
        // bindings.addAttribute("edited_text", "EDIT: This is the new text of the tweet.");
        addEditedTweet("EditedTweetId", "EDIT: This is the new text of the tweet.", bindings);
        // bindings.addVariable(template.qn_var("edit_id"), template.qn_tw("editId"));
        addEditActivity("editId", bindings);
        // bindings.addVariable(template.qn_var("original_author_props_id"), template.qn_tw("OriginalAuthorPropsId"));
        // bindings.addAttribute("ORIGINAL_credible", "1");
        // bindings.addAttribute("ORIGINAL_name", "Some orignial author name");
        // bindings.addAttribute("ORIGINAL_username", "Some original author username");
        // bindings.addAttribute("ORIGINAL_verified", "True");
        // bindings.addAttribute("ORIGINAL_followers_count", "20");
        // bindings.addAttribute("ORIGINAL_following_count", "20");
        addAuthorProps(TweetType.ORIGINAL, "OriginalAuthorPropsId", "1", 
                       "Some original author name", "Some original author username", 
                       "True", "20", "20", 
                       bindings);
    }


    public void bind(String file_json) {
        ProvFactory pFactory = template.getpFactory();

        Bindings bindings = new Bindings(pFactory);
        // bindings.addVariable(template.qn_var("original_tweet_id"), template.qn_tw("OriginalTweetId"));
        // bindings.addAttribute("original_text", "This is the text of the tweet.");
        // bindings.addVariable(template.qn_var("post_id"), template.qn_tw("postId"));        
        // bindings.addVariable(template.qn_var("original_author_id"),template.qn_tw("originalAuthorId"));
        // bindings.addAttribute("ag_o_name", "Some original author name");
        // bindings.addVariable(template.qn_var("original_tweet_props_id"), template.qn_tw("OriginalTweetPropsId"));
        // bindings.addAttribute("ORIGINAL_created_at", "Some timestamp");
        // bindings.addAttribute("ORIGINAL_location", "Some place");
        // bindings.addAttribute("ORIGINAL_like_count", "20");
        // bindings.addAttribute("ORIGINAL_quote_count", "20");
        // bindings.addAttribute("ORIGINAL_reply_count", "20");
        // bindings.addAttribute("ORIGINAL_retweet_count", "20");
        // bindings.addVariable(template.qn_var("edited_tweet_id"), template.qn_tw("EditedTweetId"));
        // bindings.addAttribute("edited_text", "EDIT: This is the new text of the tweet.");
        // bindings.addVariable(template.qn_var("edit_id"), template.qn_tw("editId"));
        // bindings.addVariable(template.qn_var("original_author_props_id"), template.qn_tw("OriginalAuthorPropsId"));
        // bindings.addAttribute("ORIGINAL_credible", "1");
        // bindings.addAttribute("ORIGINAL_name", "Some orignial author name");
        // bindings.addAttribute("ORIGINAL_username", "Some original author username");
        // bindings.addAttribute("ORIGINAL_verified", "True");
        // bindings.addAttribute("ORIGINAL_followers_count", "20");
        // bindings.addAttribute("ORIGINAL_following_count", "20");

        addOriginal(bindings);

        // bindings.addVariable(template.qn_var("react_id"), template.qn_tw("reactId"));
        // bindings.addVariable(template.qn_var("reaction_author_id"), template.qn_tw("reactionAuthorId"));
        // bindings.addAttribute("ag_r_name", "Some reaction author name");
        // bindings.addVariable(template.qn_var("reaction_author_props_id"), template.qn_tw("ReactionAuthorPropsId"));
        // bindings.addAttribute("REACTION_credible", "0");
        // bindings.addAttribute("REACTION_name", "Some reaction author name");
        // bindings.addAttribute("REACTION_username", "Some reaction author username");
        // bindings.addAttribute("REACTION_verified", "False");
        // bindings.addAttribute("REACTION_followers_count", "40");
        // bindings.addAttribute("REACTION_following_count", "60");
        // bindings.addVariable(template.qn_var("reaction_tweet_id"), template.qn_tw("ReactionTweetId"));
        // bindings.addAttribute("reply/retweet/quote", "reply");
        // bindings.addAttribute("reaction_text", "This is the text of the reaction.");
        // bindings.addVariable(template.qn_var("reaction_tweet_props_id"), template.qn_tw("ReactionTweetPropsId"));
        // bindings.addAttribute("REACTION_created_at", "Some timestamp");
        // bindings.addAttribute("REACTION_location", "Some place");
        // bindings.addAttribute("REACTION_reference_type", "replied_to");
        // bindings.addAttribute("REACTION_reference_id", "123456");

        addReaction(bindings);


        bindings.addVariableBindingsAsAttributeBindings();
        bindings.exportToJson(file_json);
    }

    
    public static void main(String[] args) {
        if (args.length!=1) throw new UnsupportedOperationException("main to be called with filename");
        String file_json=args[0];
        
        Template template=new Template(InteropFramework.getDefaultFactory());
        Binding binding = new Binding(template);
        binding.bind(file_json);
    }
}
