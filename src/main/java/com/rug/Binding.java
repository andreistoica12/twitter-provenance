package com.rug;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.expander.Bindings;
import org.openprovenance.prov.interop.InteropFramework;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Date;
import java.util.List;



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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.net.URISyntaxException;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.net.URL;



public class Binding {

    private final Template template;

    
    public Binding(Template template) {
        this.template = template;
    }

    public enum TweetType {
        ORIGINAL,
        REACTION
    }

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

    public void addReactionTweetProps(String reaction_tweet_props_id, String REACTION_created_at,
                                      String REACTION_location, String REACTION_reference_type, String REACTION_reference_id,
                                      Bindings bindings) {
        bindings.addVariable(template.qn_var("reaction_tweet_props_id"), template.qn_tw(reaction_tweet_props_id));
        bindings.addAttribute("REACTION_created_at", REACTION_created_at);
        bindings.addAttribute("REACTION_location", REACTION_location);
        bindings.addAttribute("REACTION_reference_type", REACTION_reference_type);
        bindings.addAttribute("REACTION_reference_id", REACTION_reference_id);
    }


    public void addEdited(Bindings bindings) {
        addEditedTweet("EditedTweetId", "EDIT: This is the new text of the tweet.", bindings);
        addEditActivity("editId", bindings);
    }


    // public void addOriginal(Bindings bindings) {
    //     addPostActivity("postId", bindings);      
    //     addOriginalAuthor("originalAuthorId", "Some original author name", bindings);
    //     addAuthorProps(TweetType.ORIGINAL, "OriginalAuthorPropsId", "1", 
    //                    "Some original author name", "Some original author username", 
    //                    "True", "20", "20", 
    //                    bindings);
    //     addOriginalTweet("OriginalTweetId", "This is the text of the tweet.", bindings);
    //     addOriginalTweetProps("OriginalTweetPropsId", "Some timestamp", 
    //                           "Some place", "20", "20", 
    //                           "20", "20", 
    //                           bindings);
    // }

    public void addOriginal(Bindings bindings) {
        addPostActivity("postId", bindings);      
        addOriginalAuthor("originalAuthorId", "Some original author name", bindings);
        addAuthorProps(TweetType.ORIGINAL, "OriginalAuthorPropsId", "1", 
                       "Some original author name", "Some original author username", 
                       "True", "20", "20", 
                       bindings);
        addOriginalTweet("OriginalTweetId", "This is the text of the tweet.", bindings);
        addOriginalTweetProps("OriginalTweetPropsId", "Some timestamp", 
                              "Some place", "20", "20", 
                              "20", "20", 
                              bindings);
    }



    // public void addReaction(Bindings bindings) {
    //     addReactActivity("reactId2", bindings);
    //     addReactionAuthor("reactionAuthorId2", "Some second reaction author name", bindings);
    //     addAuthorProps(TweetType.REACTION, "ReactionAuthorPropsId2", "1",
    //                   "Some second reaction author name", "Some second reaction author username", "True", 
    //                   "100", "200", 
    //                   bindings);
    //     addReactionTweet("ReactionTweetId2", "quote", "This is the text of the quote.", bindings);
    //     addReactionTweetProps("ReactionTweetPropsId2", "Some other timestamp", 
    //                           "Some other place", "quoted", "985929573745", 
    //                           bindings);
    // }

    public void addReaction(Bindings bindings) {
        addReactActivity("reactId2", bindings);
        addReactionAuthor("reactionAuthorId2", "Some second reaction author name", bindings);
        addAuthorProps(TweetType.REACTION, "ReactionAuthorPropsId2", "1",
                      "Some second reaction author name", "Some second reaction author username", "True", 
                      "100", "200", 
                      bindings);
        addReactionTweet("ReactionTweetId2", "quote", "This is the text of the quote.", bindings);
        addReactionTweetProps("ReactionTweetPropsId2", "Some other timestamp", 
                              "Some other place", "quoted", "985929573745", 
                              bindings);
    }



    public void bind(String file_json) {
        ProvFactory pFactory = template.getpFactory();

        Bindings bindings = new Bindings(pFactory);

        addOriginal(bindings);

        if(this.template.getoriginalIsEdited()) {
            addEdited(bindings);
        }

        addReaction(bindings);


        bindings.addVariableBindingsAsAttributeBindings();
        bindings.exportToJson(file_json);
    }

    
    public static void main(String[] args) throws URISyntaxException {
        if (args.length!=1) throw new UnsupportedOperationException("main to be called with filename");
        String file_json=args[0];
        
        Template template = new Template(InteropFramework.getDefaultFactory());
        Binding binding = new Binding(template);
        binding.bind(file_json);

        String currentWorkingDirectory = System.getProperty("user.dir");
        String filePath = currentWorkingDirectory + "/src/main/java/com/rug/Binding.java";
        System.out.println("Path to the current working file: " + filePath);

        // TODO: 
        // - find the path to the values.json file
        // - parse the JSON file into a DataClass object
        // - create a member variable for the Binding class: a DataClass object
        // - assign each parameter in the function to values in the DataClass objeect
    }
}
