package com.rug;

import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.batik.util.SVGConstants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.svg.SVGSVGElement;
import org.w3c.dom.svg.SVGRect;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class SvgResizer {
    public static void main(String[] args) {
        if (args.length != 1) {
            throw new UnsupportedOperationException("main to be called with filename");
        }
        String file_svg = args[0];
        try {
            // Load the original SVG file
            File svgFile = new File(file_svg);

            // Create a Batik DOM document
            Document document = createBatikDocument(svgFile);

            // Get the root <svg> element
            Element svgElement = document.getDocumentElement();

            // Check if the root element is <svg>
            if (svgElement.getTagName().equals(SVGConstants.SVG_SVG_TAG)) {
                // Get the Bounding Box of the <svg> element
                Rectangle bounds = getBoundingBox(svgElement);

                // Update the width and height attributes of the <svg> element
                svgElement.setAttributeNS(null, SVGConstants.SVG_WIDTH_ATTRIBUTE, String.valueOf(bounds.width));
                svgElement.setAttributeNS(null, SVGConstants.SVG_HEIGHT_ATTRIBUTE, String.valueOf(bounds.height));

                // Save the modified SVG file
                File outputFile = new File(file_svg);
                FileOutputStream outputStream = new FileOutputStream(outputFile);
                writeDocument(document, outputStream);

                System.out.println("Resized SVG saved as: " + outputFile.getAbsolutePath());
            } else {
                System.err.println("The root element is not <svg>.");
            }

            // You can now further process the modified SVG file as needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Document createBatikDocument(File svgFile) throws Exception {
        String parser = XMLResourceDescriptor.getXMLParserClassName();
        SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
        Document document = factory.createDocument(svgFile.toURI().toString());
        if (document == null) {
            throw new RuntimeException("Failed to create Batik document.");
        }
        return document;
    }

    private static Rectangle getBoundingBox(Element svgElement) {
        SVGSVGElement svgSvgElement = (SVGSVGElement) svgElement;
        SVGRect svgRect = svgSvgElement.getBBox();
        return new Rectangle((int) svgRect.getX(), (int) svgRect.getY(), (int) svgRect.getWidth(), (int) svgRect.getHeight());
    }

    private static void writeDocument(Document document, FileOutputStream outputStream) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(document), new StreamResult(outputStream));
        outputStream.flush();
        outputStream.close();
    }
}
