package challenge_it.racbit.model.reports.configurations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 
 * Represents the content of the XML file, with the configuration to generate a Rate Shop Report file.
 * 
 *  @author Cátia Moreira e João Taborda
 *
 */
public class RateShopReportConfiguration extends Configuration{

	/**
	 * Defines the row and column for the destination
	 */
	private CrossReference _destinationCell;
	
	/**
	 * Defines the row and column for the month
	 */
	private CrossReference _monthCell;
	
	/**
	 * Defines the row and column for the days
	 */
	private CrossReference _dayCell;
	
	/**
	 * Defines the beginning of groups 
	 */
	private CrossReference _groupsBeginningCell;
		
	/**
	 * Contains all of the groups and their row index
	 */
	private Map<String, CrossReference> _groupsMap;
	
	/**
	 * The list of the groups' names
	 */
	private List<String> _groupsList;

	/**
	 * Contains all of the brokers
	 */
	private Map<String, Broker> _brokers;
	
	/**
	 * The list of the brokers' names
	 */
	private List<String> _brokersList;

	/**
	 * The reference to the first cell that has values
	 */
	private CrossReference _gridValuesFirstCell;
	
	private int _conversionTableOffset;
	
	/**
	 * Creates an instance of Configuration
	 */
	public RateShopReportConfiguration(){
		_groupsList = new LinkedList<String>();
		_groupsMap = new HashMap<String, CrossReference>();
		_brokers = new HashMap<String, Broker>();
		_brokersList = new LinkedList<String>();
	}

	
	/**
	 * Gets the row and column for the destination
	 * 
	 * @return The row and column
	 */
	public CrossReference getDestinationCell(){
		return _destinationCell;
	}
	
	/**
	 * Sets the row and column for the destination
	 * 
	 * @param c CrossReference with the row and column
	 */
	public void setDestinationCell(CrossReference c){
		_destinationCell = c;
	}
	
	/**
	 * Gets the row and column for the month
	 * 
	 * @return The row and column
	 */
	public CrossReference getMonthCell(){
		return _monthCell;
	}
	
	/**
	 * Sets the row and column for the month
	 * 
	 * @param c CrossReference with the row and column
	 */
	public void setMonthCell(CrossReference c){
		_monthCell = c;
	}
	
	/**
	 * Gets the row and column for the days
	 * 
	 * @return The row and column
	 */
	public CrossReference getDayCell(){
		return _dayCell;
	}
	
	/**
	 * Sets the row and column for the days
	 * 
	 * @param c CrossReference with the row and column
	 */
	public void setDayCell(CrossReference c){
		_dayCell = c;
	}
	
	/**
	 * Adds a broker's name to the list of brokers and a broker to the map of brokers
	 * 
	 * @param name broker's name
	 * @param b broker to add
	 */
	public void addBroker(String name, Broker b){
		_brokersList.add(name);
		_brokers.put(name, b);
	}
	
	/**
	 * Gets the map of brokers
	 * 
	 * @return the map of brokers
	 */
	public Map<String, Broker> getBrokers(){
		return _brokers;
	}
	
	/**
	 * Gets the list of groups and their row index
	 * 
	 * @return A map of groups
	 */
	public Map<String, CrossReference> getGroupsMap(){
		return _groupsMap;
	}
	
	/**
	 * Adds a group to the map
	 * 
	 * @param group The name of the group
	 * @param crossReference The row index of the group
	 */
	public void addGroup(String group, CrossReference crossReference){
		_groupsList.add(group);
		_groupsMap.put(group, crossReference);
	}
	
	/**
	 * Gets the list of brokers
	 * 
	 * @return the list of brokers
	 */
	public List<String> getBrokersList(){
		return _brokersList;
	}
	
	/**
	 * Gets the list of groups
	 * 
	 * @return the list of groups
	 */
	public List<String> getGroupsList(){
		return _groupsList;
	}
	
	/**
	 * Gets the first cell of the groups
	 * 
	 * @return the first cell of the groups
	 */
	public CrossReference getGroupsBeginningCell() {
		return _groupsBeginningCell;
	}

	/**
	 * Sets the first cell of the groups
	 */
	public void setGroupsBeginningCell(CrossReference _groupsBeginningCell) {
		this._groupsBeginningCell = _groupsBeginningCell;
	}

	/**
	 * Gets the first cell of the values
	 * 
	 * @return the first cell of the values
	 */
	public CrossReference getGridValuesFirstCell() {
		return _gridValuesFirstCell;
	}

	/**
	 * Sets the first cell of the values
	 */
	public void setGridValuesFirstCell(CrossReference _gridValuesFirstCell) {
		this._gridValuesFirstCell = _gridValuesFirstCell;
	}


	public int getConversionTableOffset() {
		return _conversionTableOffset;
	}


	public void setConversionTableOffset(int conversionTableOffset) {
		_conversionTableOffset = conversionTableOffset;
	}
}