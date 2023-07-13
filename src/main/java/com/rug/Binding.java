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
        bindings.addAttribute("value", "This is the text of the tweet.");
        bindings.addVariable(template.qn_var("post_id"), template.qn_tw("postId"));        
        bindings.addVariable(template.qn_var("original_author_id"),template.qn_tw("originalAuthorId"));
        bindings.addVariable(template.qn_var("assoc1_id"), template.qn_tw("assoc1Id"));
        bindings.addVariable(template.qn_var("original_post_props_id"), template.qn_tw("originalPostPropsId"));
        bindings.addAttribute("created_at", "Some timestamp");
        bindings.addAttribute("location", "Some place");
        bindings.addAttribute("like_count", "20");
        bindings.addAttribute("quote_count", "20");
        bindings.addAttribute("reply_count", "20");
        bindings.addAttribute("retweet_count", "20");
        bindings.addVariable(template.qn_var("used1_id"), template.qn_tw("used1Id"));

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
