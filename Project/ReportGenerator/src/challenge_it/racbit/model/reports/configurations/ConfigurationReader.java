package challenge_it.racbit.model.reports.configurations;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import challenge_it.racbit.model.core.exceptions.ReportGenerationException;
import challenge_it.racbit.model.reports.generators.utils.CrossReference;

/**
 * Class that reads the XML file into an instance of Configuration
 * 
 *  @author Cátia Moreira e João Taborda
 *
 */
public abstract class ConfigurationReader{

	/**
	 * Gets a Configuration instance with the XML information
	 * 
	 * @param configurationFilename XML filename
	 * @param schemaFilename Schema filename
	 * @param transformationFilename Transformation filename
	 * @return A Configuration instance
	 * @throws ReportGenerationException 
	 */
	
	public Configuration read(String configurationFilename, String schemaFilename, String transformationFilename) throws ReportGenerationException {
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			dbFactory.setIgnoringElementContentWhitespace(true);
			
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(ConfigurationReader.class.getClassLoader().getResourceAsStream(configurationFilename));
			
			validateFile(doc, schemaFilename);

			return getConfiguration(dBuilder.parse(transformFile(doc, transformationFilename)));
		}
		catch (Exception e) {
			throw new ReportGenerationException(e);
		} 			
		
	}

	/**
	 * Gets a ByteArrayInputStream with the transform XML file
	 * 
	 * @param doc
	 * @param transformationFilename
	 * @return ByteArrayInputStream
	 * @throws TransformerException
	 */
	private ByteArrayInputStream transformFile(Document doc, String transformationFilename) throws TransformerException {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer(new StreamSource(ConfigurationReader.class.getClassLoader().getResourceAsStream(transformationFilename)));

		DOMSource source = new DOMSource(doc);
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(data);  

		transformer.transform(source, result);
		
		return new ByteArrayInputStream(data.toByteArray()); 
	}

	/**
	 * Verify the XML file against the XSD Schema.
	 * @param doc
	 * @param schemaFilename
	 * @throws SAXException
	 * @throws IOException
	 */
	private void validateFile(Document doc, String schemaFilename) throws SAXException, IOException {
		SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Source schemaFile = new StreamSource(ConfigurationReader.class.getClassLoader().getResourceAsStream(schemaFilename));
	    		
		Schema schema = factory.newSchema(schemaFile);
		Validator validator = schema.newValidator();
		validator.validate(new DOMSource(doc));
		
	}

	/**
	 * Retrieves the information from the XML file
	 * 
	 * @param doc
	 * @return A Configuration instance
	 */
	protected abstract Configuration getConfiguration(Document doc);

	/**
	 * Gets the row and column from an Element
	 * 
	 * @param node XML element
	 * @return The row and the column of the node
	 */
	protected CrossReference getCell(Node node) {
		String column = node.getFirstChild().getTextContent();
		int row = Integer.parseInt(node.getLastChild().getTextContent());
		
		return new CrossReference(row-1, getColumn(column));
	}	
	
	/**
	 * Converts the column index of a XLST file to numbered indexes. (e.g.: A -> 0; AA->26; ...)
	 * 
	 * @param column
	 * @return The index
	 * @throws IllegalArgumentException When the column is not valid
	 */
	protected int getColumn(String column) throws IllegalArgumentException{
		
		switch(column.length()){
			case 1:
				return getOneChar(column.charAt(0));
			case 2:
				return getTwoChar(column.toCharArray());
			case 3:
				return getThreeChar(column.toCharArray());
			default:
				throw new IllegalArgumentException();
		}
	}

	/**
	 * Converts a char to an index
	 * 
	 * @param letter A char
	 * @return The index
	 */
	private int getOneChar(char letter){
		return letter - 65;
	}
	
	/**
	 * Converts two chars into an index
	 * 
	 * @param letters Two chars array
	 * @return The index
	 */
	private int getTwoChar(char [] letters){
		return getOneChar(letters[1]) + (26*(letters[0]+1));
	}
	
	/**
	 * Converts three chars into a index
	 * 
	 * @param letters Three chars array
	 * @return The index
	 */
	private int getThreeChar(char [] letters){
		return (getTwoChar(Arrays.copyOf(letters, 2))+1) * 26;
	}
}