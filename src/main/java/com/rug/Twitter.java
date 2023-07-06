package com.rug;

import java.util.Arrays;

import org.openprovenance.prov.interop.Formats;
import org.openprovenance.prov.interop.InteropFramework;
import org.openprovenance.prov.model.Agent;
import org.openprovenance.prov.model.Document;
import org.openprovenance.prov.model.Entity;
import org.openprovenance.prov.model.Namespace;
import org.openprovenance.prov.model.QualifiedName;
import org.openprovenance.prov.model.ProvFactory;
import org.openprovenance.prov.model.WasAttributedTo;
import org.openprovenance.prov.model.WasDerivedFrom;


public class Twitter 
{
    
    public static final String TWITTER_NS = "https://twitter.com";
    public static final String TWITTER_PREFIX = "twitter";

    private final ProvFactory pFactory;
    private final Namespace ns;

    public Twitter(ProvFactory pFactory) {
        this.pFactory = pFactory;
        ns=new Namespace();
        ns.addKnownNamespaces();
        ns.register(TWITTER_PREFIX, TWITTER_NS);
    }

    public QualifiedName qn(String n) {
        return ns.qualifiedName(TWITTER_PREFIX, n, pFactory);
    }

    public Document makeDocument() {     
        Entity edited_tweet = pFactory.newEntity(qn("edited-source-tweet"));
        edited_tweet.setValue(pFactory.newValue("This is an edited source tweet.", pFactory.getName().XSD_STRING));
        
        Entity original_tweet = pFactory.newEntity(ns.qualifiedName(TWITTER_PREFIX,"tweet-id",pFactory));

        Agent andrei = pFactory.newAgent(qn("Andrei"), "Andrei Stoica");

        WasAttributedTo attr1 = pFactory.newWasAttributedTo(null,original_tweet.getId(), andrei.getId());
        WasAttributedTo attr2 = pFactory.newWasAttributedTo(null,edited_tweet.getId(), andrei.getId());

        WasDerivedFrom wdf = pFactory.newWasDerivedFrom(edited_tweet.getId(), original_tweet.getId());

        Document document = pFactory.newDocument();
        document.getStatementOrBundle().addAll(Arrays.asList(original_tweet, edited_tweet, andrei, attr1, attr2, wdf));
        document.setNamespace(ns);
        return document;
    }
    
    public void doConversions(Document document, String file) {
        InteropFramework intF=new InteropFramework();
        intF.writeDocument(file, document);     
        intF.writeDocument(System.out, Formats.ProvFormat.PROVN, document);
    }

    public void closingBanner() {
        System.out.println("*************************");
    }

    public void openingBanner() {
        System.out.println("*************************");
        System.out.println("* Converting document  ");
        System.out.println("*************************");
    }
    

    public static void main( String[] args )
    {
        if (args.length!=1) throw new UnsupportedOperationException("main to be called with filename");
        String file=args[0];
        
        Twitter little=new Twitter(InteropFramework.getDefaultFactory());
        little.openingBanner();
        Document document = little.makeDocument();
        little.doConversions(document, file);
        little.closingBanner();    
    }
}
