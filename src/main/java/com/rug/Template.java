package com.rug;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Date;


import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.*;
import org.openprovenance.prov.rdf.*;




public class Template {

    public static final String VAR_NS = "http://openprovenance.org/var#";
    public static final String VAR_PREFIX = "var";

    public static final String VARGEN_NS = "http://openprovenance.org/vargen#";
    public static final String VARGEN_PREFIX ="vargen";

    public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
    public static final String FOAF_PREFIX ="foaf";

    public static final String TMPL_NS = "http://openprovenance.org/tmpl#";
    public static final String TMPL_PREFIX ="tmpl";

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


    public void openingBanner() {
        System.out.println("*************************");
        System.out.println("* Converting document  ");
        System.out.println("*************************");
    }

    public void doConversions(Document document, String file) {
        InteropFramework intF=new InteropFramework();
        intF.writeDocument(file, Formats.ProvFormat.PROVN, document);
    }

    public void closingBanner() {
        System.out.println("*************************");
    }
        
    public Document createTemplate() {

        // 1. ENTITY - originalTweet - declaration with attributes
        Collection<Attribute> tweetAttributes = new ArrayList<>();
        Attribute tweetValue = pFactory.newAttribute(Attribute.AttributeKind.PROV_VALUE, qn_var("text"), pFactory.getName().XSD_ANY_URI);
        tweetAttributes.add(tweetValue);
        Entity entity_originalTweet = pFactory.newEntity(qn_var("original_post_id"), tweetAttributes);



        // 2. ACTIVITY - post - declaration with attributes
        Collection<Attribute> postActivityAttributes = new ArrayList<>();
        Attribute postType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "publish", pFactory.getName().XSD_STRING);
        postActivityAttributes.add(postType);

        // As I want to add the type attribute to the post activity, the constructor requires to define 2
        // XMLGregorianCalendar values for start and end date.
        // Create Date Object
        Date current_date = new Date();
        XMLGregorianCalendar xmlStartDate = null;
        XMLGregorianCalendar xmlEndDate = null;
        // Gregorian Calendar object creation
        GregorianCalendar gc = new GregorianCalendar();
        // giving current date and time to gc
        gc.setTime(current_date);
        
        try {
            xmlStartDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);

            // Add 2 days to the current timestamp
            gc.add(GregorianCalendar.DAY_OF_YEAR, 2);

            xmlEndDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Activity activity_post = pFactory.newActivity(qn_var("post_id"), xmlStartDate, xmlEndDate, postActivityAttributes);
        


        // 3. GENERATION - wgb1 - declaration with attributes
        WasGeneratedBy wgb1 = pFactory.newWasGeneratedBy(null, entity_originalTweet.getId(), activity_post.getId());


        // 4. AGENT - originalAuthor - declaration with attributes
        Collection<Attribute> originalAuthorAttributes = new ArrayList<>();
        Attribute originalAuthorType = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, qn_prov("Person"), pFactory.getName().XSD_STRING);
        Attribute originalAuthorName = pFactory.newAttribute(qn_foaf("name"), qn_var("name"), pFactory.getName().XSD_STRING);
        originalAuthorAttributes.add(originalAuthorType);
        originalAuthorAttributes.add(originalAuthorName);
        Agent agent_originalAuthor = pFactory.newAgent(qn_var("original_author_id"), originalAuthorAttributes);


        // 5. ASSOCIATION - assoc1 - declaration with attributes
        Collection<Attribute> assoc1Attributes = new ArrayList<>();
        Attribute assoc1Role = pFactory.newAttribute(Attribute.AttributeKind.PROV_ROLE, "author", pFactory.getName().XSD_STRING);
        assoc1Attributes.add(assoc1Role);
        WasAssociatedWith assoc1 = pFactory.newWasAssociatedWith(qn_var("assoc1_id"), activity_post.getId(), agent_originalAuthor.getId(), (QualifiedName)null, assoc1Attributes);



        // Create a collection to store statements
        Collection<Statement> statementCollection = new ArrayList<>();
        statementCollection.addAll(Arrays.asList(entity_originalTweet, activity_post, agent_originalAuthor, wgb1, assoc1));

        Bundle bundle = pFactory.newNamedBundle(qn_vargen("bundleId"), ns, statementCollection);


        Document document = pFactory.newDocument();
        document.getStatementOrBundle().add(bundle);

        return document;
    }


    public static void main( String[] args )
    {
        if (args.length!=1) throw new UnsupportedOperationException("main to be called with filename");
        String file=args[0];
        
        Template template=new Template(InteropFramework.getDefaultFactory());

        template.openingBanner();
        Document document = template.createTemplate();
        template.doConversions(document, file);
        template.closingBanner();

    }

}
