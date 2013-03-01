package challenge_it.racbit.model.reports.configurations;

import org.w3c.dom.Document;

public class BenchmarkingReportConfigurationReader extends ConfigurationReader{

	/**
	 * Reads the XML file and retrieves all the information
	 * 
	 * @param doc The representation of the XML file
	 * @return an instance of Configuration that has the information read from XML file
	 */
	@Override
	protected Configuration getConfiguration(Document doc) {
		final int FIRST_IDX = 0;
		
		BenchmarkingReportConfiguration configuration = new BenchmarkingReportConfiguration();
		
		configuration.setRateCell(getCell(doc.getElementsByTagName("ConversionRate").item(FIRST_IDX)));
		
		configuration.setConsultationDateCell(getCell(doc.getElementsByTagName("ConsultationDate").item(FIRST_IDX)));
		
		configuration.setHourCell(getCell(doc.getElementsByTagName("Hour").item(FIRST_IDX)));
		
		configuration.setPickUpDateCell(getCell(doc.getElementsByTagName("PickUpDate").item(FIRST_IDX)));
		
		configuration.setGridValuesFirstCell(getCell(doc.getElementsByTagName("Begin").item(FIRST_IDX)));
		
		configuration.setLocationCell(getCell(doc.getElementsByTagName("Location").item(FIRST_IDX)));
		
		configuration.setGroupCell(getCell(doc.getElementsByTagName("Group").item(FIRST_IDX)));
		
		configuration.setNumberOfDaysCell(getCell(doc.getElementsByTagName("NumberOfDays").item(FIRST_IDX)));
		
		return configuration;
	}

}
