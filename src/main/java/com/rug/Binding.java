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

    public void addReactionTweet(Bindings bindings) {
        bindings.addVariable(template.qn_var("react_id"), template.qn_tw("reactId2"));
        bindings.addVariable(template.qn_var("reaction_author_id"), template.qn_tw("reactionAuthorId2"));
        bindings.addAttribute("ag_r_name", "Some second reaction author name");
        bindings.addVariable(template.qn_var("reaction_author_props_id2"), template.qn_tw("ReactionAuthorPropsId2"));
        bindings.addAttribute("REPLY_credible", "1");
        bindings.addAttribute("REPLY_name", "Some second reaction author name");
        bindings.addAttribute("REPLY_username", "Some second reaction author username");
        bindings.addAttribute("REPLY_verified", "True");
        bindings.addAttribute("REPLY_followers_count", "100");
        bindings.addAttribute("REPLY_following_count", "200");
        bindings.addVariable(template.qn_var("reaction_tweet_id"), template.qn_tw("ReactionTweetId2"));
        bindings.addAttribute("reply/retweet/quote", "quote");
        bindings.addAttribute("reaction_text", "This is the text of the quote.");
        bindings.addVariable(template.qn_var("reaction_tweet_props_id"), template.qn_tw("ReactionTweetPropsId2"));
        bindings.addAttribute("REPLY_created_at", "Some other timestamp");
        bindings.addAttribute("REPLY_location", "Some other place");
        bindings.addAttribute("REPLY_reference_type", "quoted");
        bindings.addAttribute("REPLY_reference_id", "985929573745");
    }

    public void bind(String file_json) {
        ProvFactory pFactory = template.getpFactory();

        Bindings bindings = new Bindings(pFactory);
        bindings.addVariable(template.qn_var("original_tweet_id"), template.qn_tw("OriginalTweetId"));
        bindings.addAttribute("original_text", "This is the text of the tweet.");
        bindings.addVariable(template.qn_var("post_id"), template.qn_tw("postId"));        
        bindings.addVariable(template.qn_var("original_author_id"),template.qn_tw("originalAuthorId"));
        bindings.addAttribute("ag_o_name", "Some original author name");
        bindings.addVariable(template.qn_var("original_tweet_props_id"), template.qn_tw("OriginalTweetPropsId"));
        bindings.addAttribute("ORIGINAL_created_at", "Some timestamp");
        bindings.addAttribute("ORIGINAL_location", "Some place");
        bindings.addAttribute("ORIGINAL_like_count", "20");
        bindings.addAttribute("ORIGINAL_quote_count", "20");
        bindings.addAttribute("ORIGINAL_reply_count", "20");
        bindings.addAttribute("ORIGINAL_retweet_count", "20");
        bindings.addVariable(template.qn_var("edited_tweet_id"), template.qn_tw("EditedTweetId"));
        bindings.addAttribute("edited_text", "EDIT: This is the new text of the tweet.");
        bindings.addVariable(template.qn_var("edit_id"), template.qn_tw("editId"));
        bindings.addVariable(template.qn_var("original_author_props_id"), template.qn_tw("OriginalAuthorPropsId"));
        bindings.addAttribute("ORIGINAL_credible", "1");
        bindings.addAttribute("ORIGINAL_name", "Some orignial author name");
        bindings.addAttribute("ORIGINAL_username", "Some original author username");
        bindings.addAttribute("ORIGINAL_verified", "True");
        bindings.addAttribute("ORIGINAL_followers_count", "20");
        bindings.addAttribute("ORIGINAL_following_count", "20");
        bindings.addVariable(template.qn_var("react_id"), template.qn_tw("reactId"));
        bindings.addVariable(template.qn_var("reaction_author_id"), template.qn_tw("reactionAuthorId"));
        bindings.addAttribute("ag_r_name", "Some reaction author name");
        bindings.addVariable(template.qn_var("reaction_author_props_id"), template.qn_tw("ReactionAuthorPropsId"));
        bindings.addAttribute("REPLY_credible", "0");
        bindings.addAttribute("REPLY_name", "Some reaction author name");
        bindings.addAttribute("REPLY_username", "Some reaction author username");
        bindings.addAttribute("REPLY_verified", "False");
        bindings.addAttribute("REPLY_followers_count", "40");
        bindings.addAttribute("REPLY_following_count", "60");
        bindings.addVariable(template.qn_var("reaction_tweet_id"), template.qn_tw("ReactionTweetId"));
        bindings.addAttribute("reply/retweet/quote", "reply");
        bindings.addAttribute("reaction_text", "This is the text of the reaction.");
        bindings.addVariable(template.qn_var("reaction_tweet_props_id"), template.qn_tw("ReactionTweetPropsId"));
        bindings.addAttribute("REPLY_created_at", "Some timestamp");
        bindings.addAttribute("REPLY_location", "Some place");
        bindings.addAttribute("REPLY_reference_type", "replied_to");
        bindings.addAttribute("REPLY_reference_id", "123456");

        addReactionTweet(bindings);
        








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
