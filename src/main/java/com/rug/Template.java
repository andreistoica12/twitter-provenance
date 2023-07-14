package com.rug;


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
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.rdf.*;
import org.openprovenance.prov.template.expander.Bindings;
import org.openprovenance.prov.template.expander.BindingsBean;
import org.openprovenance.prov.template.expander.BindingsJson;




public class Template {

    public static final String VAR_NS = "http://openprovenance.org/var#";
    public static final String VAR_PREFIX = "var";

    public static final String VARGEN_NS = "http://openprovenance.org/vargen#";
    public static final String VARGEN_PREFIX ="vargen";

    public static final String TMPL_NS = "http://openprovenance.org/tmpl#";
    public static final String TMPL_PREFIX ="tmpl";

    public static final String TW_NS = "http://twitter.com/";
    public static final String TW_PREFIX = "tw";

    private final ProvFactory pFactory;
    private final Namespace ns;

    public Template(ProvFactory pFactory) {
        this.pFactory = pFactory;
        ns=new Namespace();
        ns.addKnownNamespaces();
        ns.register(VAR_PREFIX, VAR_NS);
        ns.register(VARGEN_PREFIX, VARGEN_NS);
        ns.register(TMPL_PREFIX, TMPL_NS);
        ns.register(TW_PREFIX, TW_NS);
    }

    public ProvFactory getpFactory() {
        return pFactory;
    }

    public QualifiedName qn_prov(String n) {
        return ns.qualifiedName("prov", n, pFactory);
    }

    public QualifiedName qn_var(String n) {
        return ns.qualifiedName(VAR_PREFIX, n, pFactory);
    }

    public QualifiedName qn_vargen(String n) {
        return ns.qualifiedName(VARGEN_PREFIX, n, pFactory);
    }

    public QualifiedName qn_tw(String n) {
        return ns.qualifiedName(TW_PREFIX, n, pFactory);
    }


    public void openingBanner() {
        System.out.println("*************************");
        System.out.println("* Converting document  ");
        System.out.println("*************************");
    }

    public void doConversions(Document document, String file_provn, String file_svg) {
        InteropFramework intF=new InteropFramework();
        intF.writeDocument(file_provn, Formats.ProvFormat.PROVN, document);
        intF.writeDocument(file_svg, Formats.ProvFormat.SVG, document);
    }

    public void closingBanner() {
        System.out.println("*************************");
    }

    public Collection<Attribute> createOriginalTweetProps(ProvFactory pFactory) {
        Collection<Attribute> originalTweetPropsAttributes = new ArrayList<>();
        Attribute originalTweetPropsType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "tweet properties", pFactory.getName().XSD_STRING);
        Attribute originalTweetPropsCreatedAt = pFactory.newAttribute(qn_tw("created_at"), qn_var("ot_created_at"), pFactory.getName().XSD_DATETIMESTAMP);
        Attribute originalTweetPropsLocation = pFactory.newAttribute(qn_tw("location"), qn_var("ot_location"), pFactory.getName().PROV_LOCATION);
        Attribute originalTweetPropsLikeCount = pFactory.newAttribute(qn_tw("like_count"), qn_var("ot_like_count"), pFactory.getName().XSD_INT);
        Attribute originalTweetPropsQuoteCount = pFactory.newAttribute(qn_tw("quote_count"), qn_var("ot_quote_count"), pFactory.getName().XSD_INT);
        Attribute originalTweetPropsReplyCount = pFactory.newAttribute(qn_tw("reply_count"), qn_var("ot_reply_count"), pFactory.getName().XSD_INT);
        Attribute originalTweetPropsRetweetCount = pFactory.newAttribute(qn_tw("retweet_count"), qn_var("ot_retweet_count"), pFactory.getName().XSD_INT);

        originalTweetPropsAttributes.addAll(Arrays.asList(originalTweetPropsType,
                                                          originalTweetPropsCreatedAt,
                                                          originalTweetPropsLocation,
                                                          originalTweetPropsLikeCount,
                                                          originalTweetPropsQuoteCount,
                                                          originalTweetPropsReplyCount,
                                                          originalTweetPropsRetweetCount));
        
        return originalTweetPropsAttributes;
    }

    public Collection<Attribute> createAuthorProps(ProvFactory pFactory, String typeOfAuthor) {
        Collection<Attribute> authorProps = new ArrayList<>();
        Attribute authorPropsType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "user properties", pFactory.getName().XSD_STRING);
        Attribute authorPropsCredible = pFactory.newAttribute(qn_tw("credible"), qn_var(typeOfAuthor+"_credible"), pFactory.getName().XSD_INT);
        Attribute authorPropsName = pFactory.newAttribute(qn_tw("name"), qn_var(typeOfAuthor+"_name"), pFactory.getName().XSD_STRING);
        Attribute authorPropsUsername = pFactory.newAttribute(qn_tw("username"), qn_var(typeOfAuthor+"_username"), pFactory.getName().XSD_STRING);
        Attribute authorPropsVerified = pFactory.newAttribute(qn_tw("verified"), qn_var(typeOfAuthor+"_verified"), pFactory.getName().XSD_BOOLEAN);
        Attribute authorPropsFollowersCount = pFactory.newAttribute(qn_tw("followers_count"), qn_var(typeOfAuthor+"_followers_count"), pFactory.getName().XSD_INT);
        Attribute authorPropsFollowingCount = pFactory.newAttribute(qn_tw("following_count"), qn_var(typeOfAuthor+"_following_count"), pFactory.getName().XSD_INT);

        authorProps.addAll(Arrays.asList(authorPropsType,
                                         authorPropsCredible,
                                         authorPropsName,
                                         authorPropsUsername,
                                         authorPropsVerified,
                                         authorPropsFollowersCount,
                                         authorPropsFollowingCount));

        return authorProps;
    }
        
    public Document createTemplate() {

        // 1. ENTITY - entity_originalTweet
        Collection<Attribute> originalTweetAttributes = new ArrayList<>();
        Attribute originalTweetValue = pFactory.newAttribute(Attribute.AttributeKind.PROV_VALUE, qn_var("original_text"), pFactory.getName().XSD_ANY_URI);
        Attribute originalTweetType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "original tweet", pFactory.getName().XSD_STRING);
        originalTweetAttributes.addAll(Arrays.asList(originalTweetValue, originalTweetType));
        Entity entity_originalTweet = pFactory.newEntity(qn_var("original_tweet_id"), originalTweetAttributes);



        // 2. ACTIVITY - activity_post
        Collection<Attribute> postActivityAttributes = new ArrayList<>();
        Attribute postType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "publish", pFactory.getName().XSD_STRING);
        postActivityAttributes.add(postType);

        // As I want to add the type attribute to the post activity, the constructor requires to define 2
        // XMLGregorianCalendar values for start and end date.

        // 1st option: null values
        Activity activity_post = pFactory.newActivity(qn_var("post_id"), (XMLGregorianCalendar)null, (XMLGregorianCalendar)null, postActivityAttributes);
        
        // // 2nd option: some values (they may be useful further forward)
        // // Create Date Object
        // Date current_date = new Date();
        // XMLGregorianCalendar xmlStartDate = null;
        // XMLGregorianCalendar xmlEndDate = null;
        // // Gregorian Calendar object creation
        // GregorianCalendar gc = new GregorianCalendar();
        // // giving current date and time to gc
        // gc.setTime(current_date);
        
        // try {
        //     xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

        //     // Add 2 days to the current timestamp
        //     gc.add(GregorianCalendar.DAY_OF_YEAR, 2);

        //     xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        // }
        // catch (Exception e) {
        //     e.printStackTrace();
        // }

        // Activity activity_post = pFactory.newActivity(qn_var("post_id"), xmlStartDate, xmlEndDate, postActivityAttributes);



        // 3. GENERATION - gen1
        WasGeneratedBy gen1 = pFactory.newWasGeneratedBy(null, entity_originalTweet.getId(), activity_post.getId());


        // 4. AGENT - agent_originalAuthor
        Collection<Attribute> originalAuthorAttributes = new ArrayList<>();
        Attribute originalAuthorType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, qn_prov("Person"), pFactory.getName().XSD_STRING);
        Attribute originalAuthorName = pFactory.newAttribute(qn_tw("name"), qn_var("ag_o_name"), pFactory.getName().XSD_STRING);
        originalAuthorAttributes.add(originalAuthorType);
        originalAuthorAttributes.add(originalAuthorName);
        Agent agent_originalAuthor = pFactory.newAgent(qn_var("original_author_id"), originalAuthorAttributes);


        // 5. ASSOCIATION - assoc1
        Collection<Attribute> assoc1Attributes = new ArrayList<>();
        Attribute assoc1Role = pFactory.newAttribute(Attribute.AttributeKind.PROV_ROLE, "author", pFactory.getName().XSD_STRING);
        assoc1Attributes.add(assoc1Role);
        WasAssociatedWith assoc1 = pFactory.newWasAssociatedWith(null, activity_post.getId(), agent_originalAuthor.getId(), (QualifiedName)null, assoc1Attributes);

        // 6. ENTITY - entity_originalTweetProps
        Entity entity_originalTweetProps = pFactory.newEntity(qn_var("original_tweet_props_id"), createOriginalTweetProps(pFactory));


        // 7 USAGE - used1
        Used used1 = pFactory.newUsed(qn_var("used1_id"), activity_post.getId(), entity_originalTweetProps.getId());

        // 8. ENTITY - entity_editedTweet
        Collection<Attribute> editedTweetAttributes = new ArrayList<>();
        Attribute editedTweetValue = pFactory.newAttribute(Attribute.AttributeKind.PROV_VALUE, qn_var("edited_text"), pFactory.getName().XSD_ANY_URI);
        Attribute editedTweetType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "edited tweet", pFactory.getName().XSD_STRING);
        editedTweetAttributes.addAll(Arrays.asList(editedTweetValue, editedTweetType));
        Entity entity_editedTweet = pFactory.newEntity(qn_var("edited_tweet_id"), editedTweetAttributes);

        // 9. DERIVATION - deriv1
        WasDerivedFrom deriv1 = pFactory.newWasDerivedFrom(entity_editedTweet.getId(), entity_originalTweet.getId());

        // 10. ACTIVITY - activity_edit
        Collection<Attribute> editActivityAttributes = new ArrayList<>();
        Attribute editType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "publish", pFactory.getName().XSD_STRING);
        editActivityAttributes.add(editType);
        Activity activity_edit = pFactory.newActivity(qn_var("edit_id"), (XMLGregorianCalendar)null, (XMLGregorianCalendar)null, editActivityAttributes);

        // 11. GENERATION - gen2
        WasGeneratedBy gen2 = pFactory.newWasGeneratedBy(null, entity_editedTweet.getId(), activity_edit.getId());

        // 12. ASSOCIATION - assoc2
        Collection<Attribute> assoc2Attributes = new ArrayList<>();
        Attribute assoc2Role = pFactory.newAttribute(Attribute.AttributeKind.PROV_ROLE, "editor", pFactory.getName().XSD_STRING);
        assoc2Attributes.add(assoc2Role);
        WasAssociatedWith assoc2 = pFactory.newWasAssociatedWith(null, activity_edit.getId(), agent_originalAuthor.getId(), (QualifiedName)null, assoc2Attributes);

        // 13. USAGE - used2
        Used used2 = pFactory.newUsed(qn_var("used2_id"), activity_edit.getId(), entity_originalTweetProps.getId());

        // 14. ENTITY - entity_originalAuthorProps
        Entity entity_originalAuthorProps = pFactory.newEntity(qn_var("original_author_props_id"), createAuthorProps(pFactory, "oa"));

        // 15. ATTRIBUTION - attr1
        WasAttributedTo attrib1 = pFactory.newWasAttributedTo(null, entity_originalAuthorProps.getId(), agent_originalAuthor.getId());

        // 16. ACTIVITY - activity_react
        Collection<Attribute> reactActivityAttributes = new ArrayList<>();
        Attribute reactType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "publish", pFactory.getName().XSD_STRING);
        reactActivityAttributes.add(reactType);
        Activity activity_react = pFactory.newActivity(qn_var("react_id"), (XMLGregorianCalendar)null, (XMLGregorianCalendar)null, reactActivityAttributes);

        // 17. COMMUNICATION - wasInformedBy1
        WasInformedBy wasInformedBy1 = pFactory.newWasInformedBy(null, qn_var("react_id"), qn_var("post_id"));


        // 18. AGENT - agent_reactionAuthor
        Collection<Attribute> reactionAuthorAttributes = new ArrayList<>();
        Attribute reactionAuthorType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, qn_prov("Person"), pFactory.getName().XSD_STRING);
        Attribute reactionAuthorName = pFactory.newAttribute(qn_tw("name"), qn_var("ag_r_name"), pFactory.getName().XSD_STRING);
        reactionAuthorAttributes.addAll(Arrays.asList(reactionAuthorType,
                                                      reactionAuthorName));
        Agent agent_reactionAuthor = pFactory.newAgent(qn_var("reaction_author_id"), reactionAuthorAttributes);


        // 19. ASSOCIATION - assoc3
        Collection<Attribute> assoc3Attributes = new ArrayList<>();
        Attribute assoc3Role = pFactory.newAttribute(Attribute.AttributeKind.PROV_ROLE, "author", pFactory.getName().XSD_STRING);
        assoc3Attributes.add(assoc3Role);
        WasAssociatedWith assoc3 = pFactory.newWasAssociatedWith(null, activity_react.getId(), agent_reactionAuthor.getId(), (QualifiedName)null, assoc2Attributes);

        // 20. ENTITY - entity_reactionAuthorProps
        Entity entity_reactionAuthorProps = pFactory.newEntity(qn_var("reaction_author_props_id"), createAuthorProps(pFactory, "ra"));

        // 21. ATTRIBUTION - attr2
        WasAttributedTo attrib2 = pFactory.newWasAttributedTo(null, entity_reactionAuthorProps.getId(), agent_reactionAuthor.getId());

        // 22. ENTITY - entity_reactionTweet
        Collection<Attribute> reactionTweetAttributes = new ArrayList<>();
        Attribute reactionTweetValue = pFactory.newAttribute(Attribute.AttributeKind.PROV_VALUE, qn_var("reaction_text"), pFactory.getName().XSD_ANY_URI);
        Attribute reactionTweetType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, qn_var("reply/retweet/quote"), pFactory.getName().XSD_STRING);
        reactionTweetAttributes.addAll(Arrays.asList(reactionTweetValue, reactionTweetType));
        Entity entity_reactionTweet = pFactory.newEntity(qn_var("reaction_tweet_id"), reactionTweetAttributes);

        // 23. GENERATION - gen3
        WasGeneratedBy gen3 = pFactory.newWasGeneratedBy(null, entity_reactionTweet.getId(), activity_react.getId());




        // TODO: 
        // - continue with the template: reaction-01-props, self-loop (Mirela), 
        // - check if the template manages multiple reactions well
        // - see how to fit everything into the visuals of the SVG
        // - see how to retrieve data from the dataset (Python) - probably some JSON file
        // - see how to plug the data into the Binding object (Java)



        


        // Create a collection to store statements
        Collection<Statement> statementCollection = new ArrayList<>();
        statementCollection.addAll(Arrays.asList(entity_originalTweet, 
                                                 activity_post, 
                                                 agent_originalAuthor, 
                                                 gen1, 
                                                 assoc1,
                                                 entity_originalTweetProps,
                                                 used1,
                                                 entity_editedTweet,
                                                 deriv1,
                                                 activity_edit,
                                                 gen2,
                                                 assoc2,
                                                 used2,
                                                 entity_originalAuthorProps,
                                                 attrib1,
                                                 activity_react,
                                                 wasInformedBy1,
                                                 agent_reactionAuthor,
                                                 assoc3,
                                                 entity_reactionAuthorProps,
                                                 attrib2,
                                                 entity_reactionTweet,
                                                 gen3));

        Bundle bundle = pFactory.newNamedBundle(qn_vargen("bundle_id"), ns, statementCollection);

        Document document = pFactory.newDocument();
        document.getStatementOrBundle().add(bundle);


        return document;
    }


    public static void main( String[] args )
    {
        if (args.length!=2) throw new UnsupportedOperationException("main to be called with 2 filenames");
        String file_provn=args[0];
        String file_svg=args[1];
        
        Template template=new Template(InteropFramework.getDefaultFactory());

        template.openingBanner();
        Document document = template.createTemplate();
        template.doConversions(document, file_provn, file_svg);
        template.closingBanner();

    }

}
