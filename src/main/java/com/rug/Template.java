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

    public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
    public static final String FOAF_PREFIX ="foaf";

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
        ns.register(FOAF_PREFIX, FOAF_NS);
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

    public QualifiedName qn_foaf(String n) {
        return ns.qualifiedName(FOAF_PREFIX, n, pFactory);
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
        Attribute originalTweetPropsType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "original tweet properties", pFactory.getName().XSD_STRING);
        Attribute originalTweetPropsCreatedAt = pFactory.newAttribute(qn_tw("created_at"), qn_var("created_at"), pFactory.getName().XSD_DATETIMESTAMP);
        Attribute originalTweetPropsLocation = pFactory.newAttribute(qn_tw("location"), qn_var("location"), pFactory.getName().PROV_LOCATION);
        Attribute originalTweetPropsLikeCount = pFactory.newAttribute(qn_tw("like_count"), qn_var("like_count"), pFactory.getName().XSD_INT);
        Attribute originalTweetPropsQuoteCount = pFactory.newAttribute(qn_tw("quote_count"), qn_var("quote_count"), pFactory.getName().XSD_INT);
        Attribute originalTweetPropsReplyCount = pFactory.newAttribute(qn_tw("reply_count"), qn_var("reply_count"), pFactory.getName().XSD_INT);
        Attribute originalTweetPropsRetweetCount = pFactory.newAttribute(qn_tw("retweet_count"), qn_var("retweet_count"), pFactory.getName().XSD_INT);

        originalTweetPropsAttributes.addAll(Arrays.asList(originalTweetPropsType,
                                                          originalTweetPropsCreatedAt,
                                                          originalTweetPropsLocation,
                                                          originalTweetPropsLikeCount,
                                                          originalTweetPropsQuoteCount,
                                                          originalTweetPropsReplyCount,
                                                          originalTweetPropsRetweetCount));
        
        return originalTweetPropsAttributes;
    }
        
    public Document createTemplate() {

        // 1. ENTITY - entity_originalTweet
        Collection<Attribute> originalTweetAttributes = new ArrayList<>();
        Attribute originalTweetValue = pFactory.newAttribute(Attribute.AttributeKind.PROV_VALUE, qn_var("text"), pFactory.getName().XSD_ANY_URI);
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
        Attribute originalAuthorName = pFactory.newAttribute(qn_foaf("name"), qn_var("name"), pFactory.getName().XSD_STRING);
        originalAuthorAttributes.add(originalAuthorType);
        originalAuthorAttributes.add(originalAuthorName);
        Agent agent_originalAuthor = pFactory.newAgent(qn_var("original_author_id"), originalAuthorAttributes);


        // 5. ASSOCIATION - assoc1
        Collection<Attribute> assoc1Attributes = new ArrayList<>();
        Attribute assoc1Role = pFactory.newAttribute(Attribute.AttributeKind.PROV_ROLE, "author", pFactory.getName().XSD_STRING);
        assoc1Attributes.add(assoc1Role);
        WasAssociatedWith assoc1 = pFactory.newWasAssociatedWith(qn_var("assoc1_id"), activity_post.getId(), agent_originalAuthor.getId(), (QualifiedName)null, assoc1Attributes);


        // 6. ENTITY - entity_originalTweetProps
        Entity entity_originalTweetProps = pFactory.newEntity(qn_var("original_post_props_id"), createOriginalTweetProps(pFactory));


        // 7 USAGE - used1
        Used used1 = pFactory.newUsed(qn_var("used1_id"), activity_post.getId(), entity_originalTweetProps.getId());

        // Create a collection to store statements
        Collection<Statement> statementCollection = new ArrayList<>();
        statementCollection.addAll(Arrays.asList(entity_originalTweet, 
                                                 activity_post, 
                                                 agent_originalAuthor, 
                                                 gen1, 
                                                 assoc1,
                                                 entity_originalTweetProps,
                                                 used1));

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
