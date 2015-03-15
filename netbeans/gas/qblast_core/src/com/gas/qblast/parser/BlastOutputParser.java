package com.gas.qblast.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

import com.gas.qblast.core.output.BlastOutput;

public class BlastOutputParser {

	private static void test_01() throws JAXBException, FileNotFoundException,
			ParserConfigurationException, SAXException {
		JAXBContext context = JAXBContext.newInstance("com.bsas.qblast.core");
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setSchema(null);

		File file = new File("D:\\tmp\\blastoutput2.xml");
		// InputStreamReader isr = new InputStreamReader(file);
		FileReader reader = new FileReader(file);

		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = parserFactory.newSAXParser();

		XMLReader xmlReader = saxParser.getXMLReader();
		xmlReader
				.setFeature(
						"http://apache.org/xml/features/nonvalidating/load-external-dtd",
						false);

		SAXSource saxSource = new SAXSource(xmlReader, new InputSource(reader));

		BlastOutput output = (BlastOutput) unmarshaller.unmarshal(saxSource);


		Marshaller marshaller = context.createMarshaller();
		StringWriter stringWriter = new StringWriter();
		marshaller.marshal(output, stringWriter);

		String test = stringWriter.toString();
	}

	public static void main(String[] args) throws JAXBException,
			FileNotFoundException, ParserConfigurationException, SAXException {
		InputStream inputStream = BlastOutput.class
				.getResourceAsStream("blastoutput.xml");
		BlastOutputParser parser = new BlastOutputParser();
		BlastOutput output = parser.parse(inputStream);
	}

	public static BlastOutput  parse(String contents) {
		ByteArrayInputStream s = new ByteArrayInputStream(contents.getBytes());
		return parse(s);
	}

	public static BlastOutput parse(InputStream inputStream) {

		BlastOutput output = null;

		try {
			JAXBContext context = JAXBContext.newInstance("com.bsas.qblast.core.output");

			Unmarshaller unmarshaller = context.createUnmarshaller();

			unmarshaller.setSchema(null);

			// File file = new File("D:\\tmp\\blastoutput2.xml");
			// FileReader reader = new FileReader(file);

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));

			SAXParserFactory parserFactory = SAXParserFactory.newInstance();
			SAXParser saxParser = parserFactory.newSAXParser();

			XMLReader xmlReader = saxParser.getXMLReader();

			xmlReader
					.setFeature(
							"http://apache.org/xml/features/nonvalidating/load-external-dtd",
							false);

			SAXSource saxSource = new SAXSource(xmlReader, new InputSource(
					reader));

			output = (BlastOutput) unmarshaller.unmarshal(saxSource);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXNotRecognizedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return output;
	}
}
