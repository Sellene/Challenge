package challenge_it.racbit.model.reports.configurations;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RateShopReportConfigurationReader extends ConfigurationReader{

	@Override
	protected Configuration getConfiguration(Document doc) {
		final int FIRST_IDX = 0;
		
		RateShopReportConfiguration configuration = new RateShopReportConfiguration();
		
		configuration.setTemplateFilename(doc.getElementsByTagName("Template").item(FIRST_IDX).getFirstChild().getTextContent());
		
		configuration.setSheetNumber(Integer.parseInt( doc.getElementsByTagName("Sheet").item(FIRST_IDX).getFirstChild().getTextContent()));

		configuration.setDestinationCell(getCell(doc.getElementsByTagName("Destination").item(FIRST_IDX)));
		
		configuration.setMonthCell(getCell(doc.getElementsByTagName("Month").item(FIRST_IDX)));
		
		configuration.setDayCell(getCell(doc.getElementsByTagName("Days").item(FIRST_IDX)));
		
		configuration.setRateCell(getCell(doc.getElementsByTagName("ConversionRate").item(FIRST_IDX)));
		
		CrossReference gridBeginCell = getCell(doc.getElementsByTagName("Generate").item(FIRST_IDX).getFirstChild());
		configuration.setGridValuesFirstCell(gridBeginCell);
		
		NodeList brokers = doc.getElementsByTagName("Broker");
		
		for(int i=0; i<brokers.getLength(); i++){
			Node brokerNode = brokers.item(i);
			NamedNodeMap attr = brokerNode.getAttributes();
			boolean hasMinimum = Boolean.parseBoolean(attr.getNamedItem("hasMinimum").getTextContent());
			String name = attr.getNamedItem("name").getTextContent();
			String columnName = null;
			
			if(hasMinimum)
				columnName =attr.getNamedItem("minimumName").getTextContent();
				
			Broker broker = new Broker(name, hasMinimum, columnName);
			
			NodeList suppliers = brokerNode.getFirstChild().getChildNodes();
			
			for(int j=0; j<suppliers.getLength(); j++){
				Node supplierNode = suppliers.item(j); 
				broker.addSupplier(supplierNode.getFirstChild().getTextContent(), new CrossReference(gridBeginCell.getRow()-1, j));
			}
			
			configuration.addBroker(name, broker);
		}

		NodeList groups = doc.getElementsByTagName("Group");
		
		for(int j=0; j<groups.getLength(); j++){
			Node groupNode = groups.item(j); 
			configuration.addGroup(groupNode.getFirstChild().getTextContent(), new CrossReference(gridBeginCell.getRow()+j, gridBeginCell.getColumn()-1));
		}
		

		return configuration;
	}

}
