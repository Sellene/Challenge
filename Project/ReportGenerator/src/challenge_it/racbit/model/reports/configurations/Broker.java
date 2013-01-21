package challenge_it.racbit.model.reports.configurations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Represents the information contained in the Broker
 * 
 *  @author Cátia Moreira e João Taborda
 *
 */
public class Broker {

	
	/**
	 * Defines the name of the broker
	 */
	private String _name;
	
	/**
	 * States that the broker was a minimum
	 */
	private boolean _hasMinimum;
	
	/**
	 * Defines the name of the minimum column
	 */
	private String _minColumnName;
	
	/**
	 * List to fill the suppliers header
	 */
	private List<String> _suppliersHeader;
	
	/**
	 * Contains all of the suppliers and their column index
	 */
	private Map<String, CrossReference> _suppliersMap;
	
	/**
	 * Creates a Broker instance
	 * 
	 * @param name The broker name
	 * @param mininum If has a minimum column
	 * @param columnName The name of the minimum column
	 */
	public Broker(String name, boolean mininum, String columnName){
		_name = name;
		_hasMinimum = mininum;
		_minColumnName = columnName;
		_suppliersHeader = new LinkedList<>();
		_suppliersMap = new HashMap<String, CrossReference>();
	}
	
	/**
	 * Gets the list of suppliers and their column index
	 * 
	 * @return A map of suppliers
	 */
	public Map<String, CrossReference> getSuppliersMap(){
		return _suppliersMap;
	}
	
	/**
	 * Adds a supplier to the map
	 * 
	 * @param supplier The name of the supplier
	 * @param column The column index of the supplier
	 */
	public void addSupplier(String supplier, CrossReference ref){
		_suppliersHeader.add(supplier);
		_suppliersMap.put(supplier, ref);
	}
	
	/**
	 * Get the Broker name
	 * 
	 * @return The broker name
	 */
	public String getName(){
		return _name;
	}

	/**
	 * Gets the supplier List
	 * 
	 * @return The suppliers list
	 */
	public List<String> getSuppliersHeaders() {
		return _suppliersHeader;
	}

	/**
	 * @return 
	 */
	public boolean hasMinimum() {
		return _hasMinimum;
	}

	/**
	 * @return
	 */
	public String getMinimumColumnName() {
		return _minColumnName;
	}
}
