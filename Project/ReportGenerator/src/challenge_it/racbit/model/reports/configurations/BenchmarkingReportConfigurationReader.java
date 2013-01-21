 package challenge_it.racbit.model.configurations;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BenchmarkingReportConfigurationReader extends ConfigurationReader{

	@Override
	protected Configuration getConfiguration(Document doc) {
		Node templateFilename = doc.getElementsByTagName("Template").item(0);
		Node sheetNum = doc.getElementsByTagName("Sheet").item(0);
		Node broker = doc.getElementsByTagName("Broker").item(0);
		Node rate = doc.getElementsByTagName("ConversionRate").item(0);
		Node consultingDate = doc.getElementsByTagName("ConsultingDate").item(0);
		Node hour = doc.getElementsByTagName("Hour").item(0);
		Node pickUpDate = doc.getElementsByTagName("PickUpDate").item(0);
		Node numberOfDays = doc.getElementsByTagName("NumberOfDays").item(0);
		NodeList locations = doc.getElementsByTagName("Location");
		
		BenchmarkingReportConfiguration configuration = new BenchmarkingReportConfiguration();

		configuration.setTemplateFilename(templateFilename.getFirstChild().getTextContent());
		configuration.setSheetNumber(Integer.parseInt(sheetNum.getFirstChild().getTextContent()));
		
		NodeList brokerChilds = broker.getChildNodes();
		
		configuration.setBrokerCell(getCell(brokerChilds.item(0)));
		configuration.setBrokerPriceColumn(getColumn(brokerChilds.item(1).getTextContent()));
		configuration.setBrokerSupplierColumn(getColumn(brokerChilds.item(2).getTextContent()));
		
		configuration.setRateCell(getCell(rate));
		configuration.setConsultingDate(getCell(consultingDate));
		configuration.setHour(getCell(hour));
		configuration.setPickupDate(getCell(pickUpDate));
		configuration.setNumberOfDays(getColumn(numberOfDays.getFirstChild().getTextContent()));
		
		Node node;
		NamedNodeMap attr;
		Location location;
		NodeList groups;

		for(int i=0; i<locations.getLength(); i++){
			node = locations.item(i);
			
			attr = node.getAttributes();
			groups = node.getLastChild().getChildNodes();
			
			location = new Location(getCell(node.getFirstChild()));
			
			for(int j=0; j<groups.getLength(); j++){
				node = groups.item(j); 
				location.addGroup(node.getFirstChild().getTextContent(), Integer.parseInt(node.getLastChild().getTextContent())-1);
			}
			
			if(attr.item(1).getTextContent().equals("Regular"))
				configuration.addRegularLocations(location);
			else
				configuration.addLowLocations(location);
		}
		
		
		return configuration;
	}

}
