package challenge_it.racbit.model.configurations;

import java.util.LinkedList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RateShopReportConfigurationReader extends ConfigurationReader{

	@Override
	protected Configuration getConfiguration(Document doc) {
		Node templateFilename = doc.getElementsByTagName("Template").item(0);
		Node sheetNum = doc.getElementsByTagName("Sheet").item(0);
		Node destination = doc.getElementsByTagName("Destination").item(0);
		Node month = doc.getElementsByTagName("Month").item(0);
		Node days = doc.getElementsByTagName("Days").item(0);		
		Node rate = doc.getElementsByTagName("ConversionRate").item(0);
		Node generate = doc.getElementsByTagName("Generate").item(0);
		CrossReference begin = getCell(generate.getFirstChild());
		
		NodeList brokers = doc.getElementsByTagName("Broker");
		NodeList suppliers;
		NodeList groups = doc.getElementsByTagName("Group");
		
		RateShopReportConfiguration configuration = new RateShopReportConfiguration();

		configuration.setTemplateFilename(templateFilename.getFirstChild().getTextContent());
		configuration.setSheetNumber(Integer.parseInt(sheetNum.getFirstChild().getTextContent()));
		configuration.setDestinationCell(getCell(destination));
		configuration.setMonthCell(getCell(month));
		configuration.setDayCell(getCell(days));
		configuration.setRateCell(getCell(rate));
		configuration.setBegin(begin);

		Node node;
		List <Broker> brokerList = new LinkedList<Broker>();
		
		for(int i=0; i<brokers.getLength(); i++){
			node = brokers.item(i);
			NamedNodeMap attr = node.getAttributes();
			boolean hasMinimum = Boolean.parseBoolean(attr.getNamedItem("hasMinimum").getTextContent());
			String name = attr.getNamedItem("name").getTextContent();
			String columnName = null;
			
			if(hasMinimum)
				columnName =attr.getNamedItem("minimumName").getTextContent();
				
			Broker b = new Broker(name, hasMinimum, columnName);
			
			suppliers = node.getFirstChild().getChildNodes();
			
			for(int j=0; j<suppliers.getLength(); j++){
				node = suppliers.item(j); 
				b.addSupplier(node.getFirstChild().getTextContent(), new CrossReference(new ExcelPair<Integer, Integer>(null, begin.getRow()-1), new ExcelPair<String, Integer>(null, begin.getColumn()+j)));
			}
			
			brokerList.add(b);
		}

		for(int j=0; j<groups.getLength(); j++){
			node = groups.item(j); 
			configuration.addGroup(node.getFirstChild().getTextContent(), new CrossReference(new ExcelPair<Integer, Integer>(null, begin.getRow()+j), new ExcelPair<String, Integer>(null, begin.getColumn()-1)));
		}
		
		configuration.setBrokers(brokerList);
		return configuration;
	}

}
