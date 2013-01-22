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
	
	private List<String> _groupsList;

	private Map<String, Broker> _brokers;

	private CrossReference _gridValuesFirstCell;
	
	/**
	 * Creates an instance of Configuration
	 */
	public RateShopReportConfiguration(){
		_groupsList = new LinkedList<String>();
		_groupsMap = new HashMap<String, CrossReference>();
		_brokers = new HashMap<String, Broker>();
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

	public void addBroker(String name, Broker b){
		_brokers.put(name, b);
	}
	
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
	
	public List<String> getGroupsList(){
		return _groupsList;
	}

	public CrossReference getGroupsBeginningCell() {
		return _groupsBeginningCell;
	}

	public void setGroupsBeginningCell(CrossReference _groupsBeginningCell) {
		this._groupsBeginningCell = _groupsBeginningCell;
	}

	public CrossReference getGridValuesFirstCell() {
		return _gridValuesFirstCell;
	}

	public void setGridValuesFirstCell(CrossReference _gridValuesFirstCell) {
		this._gridValuesFirstCell = _gridValuesFirstCell;
	}
}