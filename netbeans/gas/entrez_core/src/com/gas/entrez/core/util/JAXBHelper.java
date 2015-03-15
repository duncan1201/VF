package com.gas.entrez.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;


public class JAXBHelper {
	public static Object unmarshall(String contextPath, String xmlString) throws JAXBException, FileNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException, SAXException
	{
		JAXBContext context = JAXBContext.newInstance(contextPath);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setSchema(null);
				
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = parserFactory.newSAXParser();
		
		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		
		SAXSource saxSource = new SAXSource(xmlReader, new InputSource(new StringReader(xmlString))); 		
		// file -> Java Object
		
		
		// from Java Object to String
		Object ret = unmarshaller.unmarshal(saxSource);
		return ret;
	}

	public static Object unmarshall(String contextPath, File xmlFile) throws JAXBException, FileNotFoundException, InstantiationException, IllegalAccessException, ParserConfigurationException, SAXException
	{
		JAXBContext context = JAXBContext.newInstance(contextPath);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setSchema(null);
		
		FileReader reader = new FileReader(xmlFile);
		
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = parserFactory.newSAXParser();
		
		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		
		SAXSource saxSource = new SAXSource(xmlReader, new InputSource(reader)); 		
		// file -> Java Object
		
		
		// from Java Object to String
		Object ret = unmarshaller.unmarshal(saxSource);
		return ret;
	}
	
	public static void main(String[] args) throws FileNotFoundException, JAXBException, InstantiationException, IllegalAccessException, ParserConfigurationException, SAXException{
		Object object = JAXBHelper.unmarshall("com.bsas.entrez.core.ESearch",  new File("D:\\tmp\\ESearchResult.xml"));
		System.out.println(object.getClass().getName());
		//ESearchResult esearchResult = (ESearchResult)object;
		
		//Object tmp = esearchResult.getCountOrRetMaxOrRetStartOrQueryKeyOrWebEnvOrIdListOrTranslationSetOrTranslationStackOrQueryTranslationOrERROR();
	}
}
