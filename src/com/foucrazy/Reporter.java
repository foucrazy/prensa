package com.foucrazy;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;

public class Reporter {

	public void generateXml(File[] files,String path) {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation implementation = builder.getDOMImplementation();
			Document document = implementation.createDocument(null, "prensa", null);
			document.setXmlVersion("1.0"); // asignamos la version de nuestro XML
			
			for (File file : Arrays.asList(files)){
				Element element=null;
				if (file.isDirectory()){
					element = document.createElement("directory");
					Text text = document.createTextNode(file.getName());
					element.appendChild(text);
				}else{
					element = document.createElement("file");
					Text text = document.createTextNode(file.getName());
					element.appendChild(text);					
				}				
				document.getDocumentElement().appendChild(element);
			}
			
			Source source = new DOMSource(document);
			Result result = new StreamResult(new java.io.File(path)); //nombre del archivo
			Result console = new StreamResult(System.out);
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.transform(source, result);
			//transformer.transform(source, console);
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
	}
}
