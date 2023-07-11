package com.rug;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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

    private final ProvFactory pFactory;
    private final Namespace ns;

    public Template(ProvFactory pFactory) {
        this.pFactory = pFactory;
        ns=new Namespace();
        ns.addKnownNamespaces();
        ns.register(VAR_PREFIX, VAR_NS);
        ns.register(VARGEN_PREFIX, VARGEN_NS);
        ns.register(FOAF_PREFIX, FOAF_NS);
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

        // Entity declaration with attributes
        Collection<Attribute> tweetAttributes = new ArrayList<>();
        Attribute tweetValue = pFactory.newAttribute(Attribute.AttributeKind.PROV_VALUE, qn_var("text"), pFactory.getName().XSD_ANY_URI);
        tweetAttributes.add(tweetValue);
        Entity tweet = pFactory.newEntity(qn_var("tweet"), tweetAttributes);


        // Agent declaration with attributes
        Collection<Attribute> agentAttributes = new ArrayList<>();
        Attribute type = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, qn_prov("Person"), pFactory.getName().XSD_STRING);
        Attribute name = pFactory.newAttribute(qn_foaf("name"), qn_var("name"), pFactory.getName().XSD_STRING);
        agentAttributes.add(type);
        agentAttributes.add(name);
        Agent author = pFactory.newAgent(qn_var("author"), agentAttributes);


        // Attribution declaration with attributes
        WasAttributedTo attr1 = pFactory.newWasAttributedTo(null, tweet.getId(), author.getId());



        // Create a collection to store statements
        Collection<Statement> statementCollection = new ArrayList<>();
        statementCollection.add(tweet);
        statementCollection.add(author);
        statementCollection.add(attr1);
        Bundle bundle = pFactory.newNamedBundle(qn_vargen("bundleId"), ns, statementCollection);


        Document document = pFactory.newDocument();
        document.getStatementOrBundle().add(bundle);


        // Document document = pFactory.newDocument();
        // document.getStatementOrBundle().addAll(Arrays.asList(tweet, author, attr1));
        // document.setNamespace(ns);


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
