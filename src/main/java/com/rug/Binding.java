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

    public void bind(String file_json) {
        ProvFactory pFactory = template.getpFactory();

        Bindings bindings = new Bindings(pFactory);
        bindings.addVariable(template.qn_var("original_tweet_id"), template.qn_tw("OriginalTweetId"));
        bindings.addAttribute("original_text", "This is the text of the tweet.");
        bindings.addVariable(template.qn_var("post_id"), template.qn_tw("postId"));        
        bindings.addVariable(template.qn_var("original_author_id"),template.qn_tw("originalAuthorId"));
        bindings.addAttribute("ag_o_name", "Some original author name");
        bindings.addVariable(template.qn_var("original_tweet_props_id"), template.qn_tw("OriginalTweetPropsId"));
        bindings.addAttribute("ot_created_at", "Some timestamp");
        bindings.addAttribute("ot_location", "Some place");
        bindings.addAttribute("ot_like_count", "20");
        bindings.addAttribute("ot_quote_count", "20");
        bindings.addAttribute("ot_reply_count", "20");
        bindings.addAttribute("ot_retweet_count", "20");
        bindings.addVariable(template.qn_var("used1_id"), template.qn_tw("used1Id"));
        bindings.addVariable(template.qn_var("edited_tweet_id"), template.qn_tw("EditedTweetId"));
        bindings.addAttribute("edited_text", "EDIT: This is the new text of the tweet.");
        bindings.addVariable(template.qn_var("edit_id"), template.qn_tw("editId"));
        bindings.addVariable(template.qn_var("used2_id"), template.qn_tw("used2Id"));
        bindings.addVariable(template.qn_var("original_author_props_id"), template.qn_tw("OriginalAuthorPropsId"));
        bindings.addAttribute("oa_credible", "1");
        bindings.addAttribute("oa_name", "Some orignial author name");
        bindings.addAttribute("oa_username", "Some original author username");
        bindings.addAttribute("oa_verified", "True");
        bindings.addAttribute("oa_followers_count", "20");
        bindings.addAttribute("oa_following_count", "20");
        bindings.addVariable(template.qn_var("react_id"), template.qn_tw("reactId"));
        bindings.addVariable(template.qn_var("reaction_author_id"), template.qn_tw("reactionAuthorId"));
        bindings.addAttribute("ag_r_name", "Some reaction author name");
        bindings.addVariable(template.qn_var("reaction_author_props_id"), template.qn_tw("ReactionAuthorPropsId"));
        bindings.addAttribute("ra_credible", "0");
        bindings.addAttribute("ra_name", "Some reaction author name");
        bindings.addAttribute("ra_username", "Some reaction author username");
        bindings.addAttribute("ra_verified", "False");
        bindings.addAttribute("ra_followers_count", "40");
        bindings.addAttribute("ra_following_count", "60");
        bindings.addVariable(template.qn_var("reaction_tweet_id"), template.qn_tw("ReactionTweetId"));
        bindings.addAttribute("reply/retweet/quote", "reply");
        bindings.addAttribute("reaction_text", "This is the text of the reaction.");




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
