package com.rug.model3;

import org.openprovenance.prov.model.*;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.Formats;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import javax.xml.datatype.XMLGregorianCalendar;



public class Template3 {

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

    public enum TweetType {
        ORIGINAL,
        REACTION
    }

    public Template3(ProvFactory pFactory) {
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

    public QualifiedName qn_tmpl(String n) {
        return ns.qualifiedName(TMPL_PREFIX, n, pFactory);
    }


    public void saveDocument(Document document, String file_provn, String file_svg, String file_png) {
        InteropFramework intF=new InteropFramework();
        intF.writeDocument(file_provn, Formats.ProvFormat.PROVN, document);
        intF.writeDocument(file_svg, Formats.ProvFormat.SVG, document);
        // intF.writeDocument(file_png, Formats.ProvFormat.PNG, document);
    }

        
    public Document createTemplateDocument() {

        // 1. ENTITY - entity_originalTweet
        Collection<Attribute> allTextualTweetsAtTimepointAttributes = new ArrayList<>();
        Attribute allTextualTweetsAtTimepointType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "ALL TEXTUAL TWEETS AT TIMEPOINT", pFactory.getName().XSD_STRING);
        Attribute allTextualTweetsAtTimepointDate = pFactory.newAttribute(qn_tw("date"), qn_var("date"), pFactory.getName().XSD_DATE);
        Attribute allTextualTweetsAtTimepointTimeInterval = pFactory.newAttribute(qn_tw("time_interval"), qn_var("time_interval"), pFactory.getName().XSD_STRING);
        Attribute allTextualTweetsAtTimepointPercentageOutOfDayTweets = pFactory.newAttribute(qn_tw("percentage_out_of_day_tweets"), qn_var("percentage_out_of_day_tweets"), pFactory.getName().XSD_STRING);
        Attribute allTextualTweetsAtTimepointNumberOfOriginalTweets = pFactory.newAttribute(qn_tw("number_of_original_tweets"), qn_var("nr_of_original_tweets"), pFactory.getName().XSD_INT);
        Attribute allTextualTweetsAtTimepointNumberOfReplies = pFactory.newAttribute(qn_tw("number_of_replies"), qn_var("nr_of_replies"), pFactory.getName().XSD_INT);
        Attribute allTextualTweetsAtTimepointNumberOfQuotes = pFactory.newAttribute(qn_tw("number_of_quotes"), qn_var("nr_of_quotes"), pFactory.getName().XSD_INT);
        // Attribute allTextualTweetsAtTimepointNumberOfRetweets = pFactory.newAttribute(qn_tw("number_of_retweets"), qn_var("nr_of_retweets"), pFactory.getName().XSD_INT);
        
        allTextualTweetsAtTimepointAttributes.addAll(Arrays.asList(allTextualTweetsAtTimepointType, 
                                                                   allTextualTweetsAtTimepointDate,
                                                                   allTextualTweetsAtTimepointTimeInterval,
                                                                   allTextualTweetsAtTimepointPercentageOutOfDayTweets,
                                                                   allTextualTweetsAtTimepointNumberOfOriginalTweets,
                                                                   allTextualTweetsAtTimepointNumberOfReplies,
                                                                   allTextualTweetsAtTimepointNumberOfQuotes));

        Entity entity_allTextualTweetsAtTimepoint = pFactory.newEntity(qn_var("all_textual_tweets_at_timepoint_id"), allTextualTweetsAtTimepointAttributes);


        // 2. ACTIVITY - activity_post
        Collection<Attribute> postOrReactActivityAttributes = new ArrayList<>();
        Attribute postOrReactType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "publish", pFactory.getName().XSD_STRING);
        postOrReactActivityAttributes.add(postOrReactType);
        Activity activity_postOrReact = pFactory.newActivity(qn_var("post_or_react_id"), (XMLGregorianCalendar)null, (XMLGregorianCalendar)null, postOrReactActivityAttributes);
        

        // 3. GENERATION - gen1
        WasGeneratedBy gen1 = pFactory.newWasGeneratedBy(null, entity_allTextualTweetsAtTimepoint.getId(), activity_postOrReact.getId());


        // 4. AGENT - agent_originalAuthor
        Collection<Attribute> groupOfAuthorsAttributes = new ArrayList<>();
        Attribute groupOfAuthorsType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "GROUP OF USERS", pFactory.getName().XSD_STRING);
        Attribute groupOfAuthorsNrOfDistinctAuthors = pFactory.newAttribute(qn_tw("number_of_distinct_authors"), qn_var("nr_of_distinct_authors"), pFactory.getName().XSD_INT);
        Attribute groupOfAuthorsAvgNrOfFollowersTop10Influencers = pFactory.newAttribute(qn_tw("average_number_of_followers_top_10_influencers"), qn_var("avg_nr_of_followers_top_10_influencers"), pFactory.getName().XSD_INT);
        Attribute groupOfAuthorsAvgNrOfFollowersAllUsers = pFactory.newAttribute(qn_tw("average_number_of_followers_all_users"), qn_var("avg_nr_of_followers_all_users"), pFactory.getName().XSD_INT);

        
        groupOfAuthorsAttributes.addAll(Arrays.asList(groupOfAuthorsType, 
                                                      groupOfAuthorsNrOfDistinctAuthors,
                                                      groupOfAuthorsAvgNrOfFollowersTop10Influencers,
                                                      groupOfAuthorsAvgNrOfFollowersAllUsers));
        Agent agent_groupOfAuthors = pFactory.newAgent(qn_var("group_of_authors_id"), groupOfAuthorsAttributes);


        // 5. ASSOCIATION - assoc1
        Collection<Attribute> assoc1Attributes = new ArrayList<>();
        Attribute assoc1Role = pFactory.newAttribute(Attribute.AttributeKind.PROV_ROLE, "authors", pFactory.getName().XSD_STRING);
        assoc1Attributes.add(assoc1Role);
        WasAssociatedWith assoc1 = pFactory.newWasAssociatedWith(null, activity_postOrReact.getId(), agent_groupOfAuthors.getId(), (QualifiedName)null, assoc1Attributes);


        // Create a collection to store statements
        Collection<Statement> statementCollection = new ArrayList<>();
        statementCollection.addAll(Arrays.asList(entity_allTextualTweetsAtTimepoint, 
                                                 activity_postOrReact, 
                                                 gen1, 
                                                 agent_groupOfAuthors, 
                                                 assoc1));


        Bundle bundle = pFactory.newNamedBundle(qn_vargen("bundle_id"), ns, statementCollection);

        Document document = pFactory.newDocument();
        document.getStatementOrBundle().add(bundle);


        return document;
    }


    public static void main( String[] args )
    {
        if (args.length!=3) throw new UnsupportedOperationException("main to be called with 3 filenames");
        String file_provn=args[0];
        String file_svg=args[1];
        String file_png= args[2];
        
        Template3 template = new Template3(InteropFramework.getDefaultFactory());

        Document document = template.createTemplateDocument();
        template.saveDocument(document, file_provn, file_svg, file_png);
    }

}
