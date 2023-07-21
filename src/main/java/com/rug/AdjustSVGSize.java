package com.rug;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class AdjustSVGSize {


    public static void main(String[] args) {


        if (args.length!=1) throw new UnsupportedOperationException("main to be called with filename");
        String file_svg=args[0];

        // Convert the path string to a Path object
        Path path = Paths.get(file_svg);
        // Get the file name from the Path object
        String fileName = path.getFileName().toString();
 

        int height = (fileName.equals("doc1.svg")) ? 6000 : 1500;
        int width = (fileName.equals("doc1.svg")) ? 3000 : 500;


        try {

            // Load the SVG file
            File svgFile = new File(file_svg);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document initial_svg_document = builder.parse(svgFile);

            // Get the root <svg> element
            Element svgElement = initial_svg_document.getDocumentElement();
            
            // Modify the viewBox attribute values
            svgElement.setAttribute("viewBox", String.format("0 0 %s %s", height, width)); 

            // Save the modified SVG file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(svgElement), new StreamResult(new File(file_svg)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
