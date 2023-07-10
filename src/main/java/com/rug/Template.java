package com.rug;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.interop.InteropMediaType;
import org.openprovenance.prov.model.*;



public class Template {

    public static final String VAR_NS = "http://openprovenance.org/var#";
    public static final String VAR_PREFIX = "var";

    public static final String BUNDLE_NS = "http://openprovenance.org/vargen#";
    public static final String BUNDLE_PREFIX ="vargen";

    public static final String FOAF_NS = "http://xmlns.com/foaf/0.1/";
    public static final String FOAF_PREFIX ="foaf";

    private final ProvFactory pFactory;
    private final Namespace ns;

    public Template(ProvFactory pFactory) {
        this.pFactory = pFactory;
        ns=new Namespace();
        ns.addKnownNamespaces();
        ns.register(VAR_PREFIX, VAR_NS);
        ns.register(BUNDLE_PREFIX, BUNDLE_NS);
        ns.register(FOAF_PREFIX, FOAF_NS);
    }

    public QualifiedName qn_var(String n) {
        return ns.qualifiedName(VAR_PREFIX, n, pFactory);
    }

    public QualifiedName qn_vargen(String n) {
        return ns.qualifiedName(BUNDLE_PREFIX, n, pFactory);
    }


    public void openingBanner() {
        System.out.println("*************************");
        System.out.println("* Converting document  ");
        System.out.println("*************************");
    }

    public void doConversions(Document document, String file) {
        InteropFramework intF=new InteropFramework();
        intF.writeDocument(file, Formats.ProvFormat.SVG, document);
    }

    public void closingBanner() {
        System.out.println("*************************");
    }
        
    public Document createTemplate() {

        Entity tweet = pFactory.newEntity(qn_var("tweet"));
        tweet.setValue(pFactory.newValue("var:value", pFactory.getName().XSD_STRING));


        Attribute type = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "prov:Person", ns.qualifiedName("prov", "Person", pFactory));
        Attribute name = pFactory.newAttribute(Attribute.AttributeKind.PROV_TYPE, "var:name", ns.qualifiedName("foaf", "name", pFactory));

        Collection<Attribute> agentAttributes = new ArrayList<>();
        agentAttributes.add(type);
        agentAttributes.add(name);
        Agent author = pFactory.newAgent(qn_var("author"), agentAttributes);



        WasAttributedTo attr1 = pFactory.newWasAttributedTo(null, tweet.getId(), author.getId());



        // Create a collection to store statements
        Collection<Statement> statementCollection = new ArrayList<>();
        statementCollection.add(tweet);
        statementCollection.add(author);
        statementCollection.add(attr1);

        Bundle bundle = pFactory.newNamedBundle(qn_vargen("id"), ns, statementCollection);
        bundle.setNamespace(ns);


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
