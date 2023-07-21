package com.rug.model2;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.template.expander.Bindings;
import org.openprovenance.prov.interop.InteropFramework;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URISyntaxException;


public class Binding2 {

    private final Template2 template;
    private final DataClass2 data;
    
    public Binding2(Template2 template, DataClass2 data) {
        this.template = template;
        this.data = data;
    }

    public void addOriginalAuthorProps(String original_author_props_id, String ORIGINAL_credible,
                                String ORIGINAL_username, String ORIGINAL_verified,
                                String ORIGINAL_followers_count, String ORIGINAL_following_count,
                                Bindings bindings) {
        bindings.addVariable(template.qn_var("original_author_props_id"), template.qn_tw(original_author_props_id));
        bindings.addAttribute("ORIGINAL_credible", ORIGINAL_credible);
        bindings.addAttribute("ORIGINAL_username", ORIGINAL_username);
        bindings.addAttribute("ORIGINAL_verified", ORIGINAL_verified);
        bindings.addAttribute("ORIGINAL_followers_count", ORIGINAL_followers_count);
        bindings.addAttribute("ORIGINAL_following_count", ORIGINAL_following_count);
    }


    public void addPostActivity(String post_id, Bindings bindings) {
        bindings.addVariable(template.qn_var("post_id"), template.qn_tw(post_id));        
    }
    
    public void addOriginalAuthor(String original_author_id, String ag_o_name, Bindings bindings) {
        bindings.addVariable(template.qn_var("original_author_id"),template.qn_tw(original_author_id));
        bindings.addAttribute("ag_o_name", ag_o_name);
    }

    public void addOriginalTweet(String original_tweet_id, String original_text, 
                                 String ORIGINAL_created_at, String ORIGINAL_location,
                                 Bindings bindings) {
        bindings.addVariable(template.qn_var("original_tweet_id"), template.qn_tw(original_tweet_id));
        bindings.addAttribute("original_text", original_text);
        bindings.addAttribute("ORIGINAL_created_at", ORIGINAL_created_at);
        bindings.addAttribute("ORIGINAL_location", ORIGINAL_location);


    }


    public void addReactActivity(String react_id, Bindings bindings) {
        bindings.addVariable(template.qn_var("react_id"), template.qn_tw(react_id));
    }

    public void addReactionAuthors(String reaction_group_of_authors_id, String nr_of_distinct_authors, Bindings bindings) {
        bindings.addVariable(template.qn_var("reaction_group_of_authors_id"), template.qn_tw(reaction_group_of_authors_id));
        bindings.addAttribute("nr_of_distinct_authors", nr_of_distinct_authors);
    }

    public void addReactionTweets(String reaction_group_of_tweets_id, String time_interval, 
                                 String nr_of_replies, String nr_of_quotes, String nr_of_retweets,
                                 Bindings bindings) {
        bindings.addVariable(template.qn_var("reaction_group_of_tweets_id"), template.qn_tw(reaction_group_of_tweets_id));
        bindings.addAttribute("time_interval", time_interval);
        bindings.addAttribute("nr_of_replies", nr_of_replies);
        bindings.addAttribute("nr_of_quotes", nr_of_quotes);
        bindings.addAttribute("nr_of_retweets", nr_of_retweets);
    }



    public void addOriginal(Bindings bindings) {
        addPostActivity(data.getOriginal().getPostId(), bindings);      
        addOriginalAuthor(data.getOriginal().getOriginalAuthorId(), data.getOriginal().getAgOName(), bindings);
        addOriginalAuthorProps(data.getOriginal().getOriginalAuthorPropsId(), data.getOriginal().getOriginalCredible(),
                               data.getOriginal().getOriginalUsername(), data.getOriginal().getOriginalVerified(),
                               data.getOriginal().getOriginalFollowersCount(), data.getOriginal().getOriginalFollowingCount(),
                               bindings);
        addOriginalTweet(data.getOriginal().getOriginalTweetId(), data.getOriginal().getOriginalText(),
                        data.getOriginal().getOriginalCreatedAt(), data.getOriginal().getOriginalLocation(),
                        bindings);
    }


    public void addGroupOfReactions(Bindings bindings) {
        addReactActivity(data.getGroupOfReactions().getReactId(), bindings);
        addReactionAuthors(data.getGroupOfReactions().getReactionGroupOfAuthorsId(), 
                           data.getGroupOfReactions().getNrOfDistinctAuthors(),
                           bindings);
        addReactionTweets(data.getGroupOfReactions().getReactionGroupOfTweetsId(), data.getGroupOfReactions().getTimeInterval(),
                          data.getGroupOfReactions().getNrOfReplies(), data.getGroupOfReactions().getNrOfQuotes(),
                          data.getGroupOfReactions().getNrOfRetweets(),
                          bindings);
    }




    public void bind(String file_json) {
        ProvFactory pFactory = template.getpFactory();

        Bindings bindings = new Bindings(pFactory);

        addOriginal(bindings);

        addGroupOfReactions(bindings);


        bindings.addVariableBindingsAsAttributeBindings();
        bindings.exportToJson(file_json);
    }

    
    public static void main(String[] args) throws URISyntaxException {
        if (args.length!=1) throw new UnsupportedOperationException("main to be called with filename");
        String file_json=args[0];
        
        Template2 template = new Template2(InteropFramework.getDefaultFactory());


        String currentWorkingDirectory = System.getProperty("user.dir");
        String dataPath = currentWorkingDirectory + "/src/main/python/model2/output/data2.json";

        // Create an ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read the JSON file and map it to the DataClass
            DataClass2 data = objectMapper.readValue(new File(dataPath), DataClass2.class);

            Binding2 binding = new Binding2(template, data);
            binding.bind(file_json);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
