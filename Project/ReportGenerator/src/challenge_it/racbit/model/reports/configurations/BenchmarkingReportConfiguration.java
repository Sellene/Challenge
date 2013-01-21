package challenge_it.racbit.model.configurations;

import java.util.LinkedList;
import java.util.List;

/**
 * 
 * Represents the content of the XML file, with the configuration to generate a Brenchmarking Report file.
 * 
 *  @author Cátia Moreira e João Taborda
 *
 */
public class BenchmarkingReportConfiguration extends Configuration{

	/**
	 * Defines the column for the broker price
	 */
	private int _brokerPriceColumn;
	
	/**
	 * Defines the column for the broker supplier
	 */
	private int _brokerSupplierColumn;
	
	/**
	 * Defines the column for the number of days
	 */
	private int _numberOfDays;
	
	/**
	 * Defines the row and column for the consulting date
	 */
	private CrossReference _consultingDate;
	
	/**
	 * Defines the row and column for the hour
	 */
	private CrossReference _hour;
	
	/**
	 * Defines the row and column for the pick up date
	 */
	private CrossReference _pickupDate;
	
	
	/**
	 * Contains all of the locations for regular prices
	 */
	private List<Location> _regularLocations;
	
	/**
	 * Contains all of the locations for low cost prices
	 */
	private List<Location> _lowLocations;

	
	public BenchmarkingReportConfiguration(){
		_regularLocations = new LinkedList<Location>();
		_lowLocations = new LinkedList<Location>();
	}
	
	/**
	 * Gets the column index for the broker price
	 * 
	 * @return the column index
	 */
	public int getBrokerPriceColumn() {
		return _brokerPriceColumn;
	}

	/**
	 * Sets the column index for the broker price
	 * 
	 * @param brokerPriceColumn The column index
	 */
	public void setBrokerPriceColumn(int brokerPriceColumn) {
		_brokerPriceColumn = brokerPriceColumn;
	}

	/**
	 * Gets the column index for the broker supplier
	 * 
	 * @return the column index
	 */
	public int getBrokerSupplierColumn() {
		return _brokerSupplierColumn;
	}

	/**
	 * Sets the column index for the broker supplier
	 * 
	 * @param brokerPriceColumn The column index
	 */
	public void setBrokerSupplierColumn(int brokerSupplierColumn) {
		_brokerSupplierColumn = brokerSupplierColumn;
	}

	/**
	 * Gets the column index for the number of days
	 * 
	 * @return the column index
	 */
	public int getNumberOfDays() {
		return _numberOfDays;
	}

	/**
	 * Sets the column index for the number of days
	 * 
	 * @param brokerPriceColumn The column index
	 */
	public void setNumberOfDays(int numberOfDays) {
		_numberOfDays = numberOfDays;
	}

	/**
	 * Gets the row and column for the consulting day
	 * 
	 * @return The row and column
	 */
	public CrossReference getConsultingDate() {
		return _consultingDate;
	}

	/**
	 * Sets the row and column for the consulting day
	 * 
	 * @param consultingDate CrossReference with the row and column
	 */
	public void setConsultingDate(CrossReference consultingDate) {
		_consultingDate = consultingDate;
	}

	/**
	 * Gets the row and column for the hour
	 * 
	 * @return The row and column
	 */
	public CrossReference getHour() {
		return _hour;
	}

	/**
	 * Sets the row and column for the hour
	 * 
	 * @param hour CrossReference with the row and column
	 */
	public void setHour(CrossReference hour) {
		_hour = hour;
	}

	/**
	 * Gets the row and column for the pick up date
	 * 
	 * @return The row and column
	 */
	public CrossReference getPickupDate() {
		return _pickupDate;
	}

	/**
	 * Sets the row and column for the pick up date
	 * 
	 * @param pickupDate CrossReference with the row and column
	 */
	public void setPickupDate(CrossReference pickupDate) {
		_pickupDate = pickupDate;
	}

	/**
	 * Gets the list of location for regular prices
	 * 
	 * @return A list of location
	 */
	public List<Location> getRegularLocations() {
		return _regularLocations;
	}

	/**
	 * Adds a location to the regular Location list
	 * 
	 * @param regularLocation The location to be added
	 */
	public void addRegularLocations(Location regularLocation) {
		_regularLocations.add(regularLocation);
	}

	/**
	 * Gets the list of location for low prices
	 * 
	 * @return A list of location
	 */
	public List<Location> getLowLocations() {
		return _lowLocations;
	}

	/**
	 * Adds a location to the low cost Location list
	 * 
	 * @param lowLocation The location to be added
	 */
	public void addLowLocations(Location lowLocations) {
		_lowLocations.add(lowLocations);
	}
	
	
	
}
