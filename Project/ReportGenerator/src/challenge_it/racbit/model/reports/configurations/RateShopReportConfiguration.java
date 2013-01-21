package challenge_it.racbit.model.configurations;

import java.util.HashMap;
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
	 * Defines the beggining of suppliers header 
	 */
	private CrossReference _suppliersBeginning;
	
	/**
	 * Defines the beggining of groups 
	 */
	private CrossReference _groupsBeginning;
	
	/**
	 * Defines is the supplier has a table with minimum value
	 */
	private boolean _hasMinimum;
	
	/**
	 * Defines the name of the minimum column
	 */
	private String _minimumName;
	
	/**
	 * Contains all of the suppliers and their column index
	 */
	private Map<String, Integer> _suppliersMap;
	
	/**
	 * Contains all of the groups and their row index
	 */
	private Map<String, CrossReference> _groupsMap;

	private List<Broker> _brokers;

	private CrossReference _begin;
	
	/**
	 * Creates an instance of Configuration
	 */
	public RateShopReportConfiguration(){
		_suppliersMap = new HashMap<String, Integer>();
		_groupsMap = new HashMap<String, CrossReference>();
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
	 * Gets the row and column for the suppliers beginning
	 * 
	 *  @return The row and column
	 */
	public CrossReference getSuppliersBeginning(){
		return _suppliersBeginning;
	}
	

	public void setBrokers(List<Broker> brokers){
		_brokers = brokers;
	}
	
	public List<Broker> getBrokers(){
		return _brokers;
	}
	
	
	/**
	 * Sets the row and column for the suppliers beginning
	 * 
	 * @param c CrossReference with the row and column
	 */
	public void setSuppliersBeginning(CrossReference c){
		_suppliersBeginning = c;
	}
	
	
//	/**
//	 * Gets the list of suppliers and their column index
//	 * 
//	 * @return A map of suppliers
//	 */
//	public Map<String, Integer> getSuppliersMap(){
//		return _suppliersMap;
//	}
//	
//	/**
//	 * Adds a supplier to the map
//	 * 
//	 * @param supplier The name of the supplier
//	 * @param column The column index of the supplier
//	 */
//	public void addSupplier(String supplier, int column){
//		_suppliersMap.put(supplier, column);
//	}
	
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
		_groupsMap.put(group, crossReference);
	}

	/**
	 * Gets the row and column for the groups beginning
	 * 
	 *  @return The row and column
	 */
	public CrossReference getGroupsBeginning(){
		return _groupsBeginning;
	}
	
	/**
	 * Sets the row and column for the groups beginning
	 * 
	 * @param c CrossReference with the row and column
	 */
	public void setGroupsBeginning(CrossReference c){
		_groupsBeginning = c;
	}
	
	/**
	 * Gets the name of the column minimum
	 * 
	 *  @return he name for the column
	 */
	public String getMinimumColumnName(){
		return _minimumName;
	}
	
	/**
	 * Sets the name of the column minimum
	 * 
	 * @param name The name for the column
	 */
	public void setMinimumColumnName(String name){
		_minimumName = name;
	}

	public CrossReference getBegin() {
		return _begin;
	}
	
	public void setBegin(CrossReference c) {
		_begin = c;
	}
}