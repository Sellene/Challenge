package challenge_it.racbit.model.configurations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Broker {

	private String _name;
	private boolean _hasMinimum;
	private String _minColumnName;
	
	private List<String> _suppliersHeader;
	
	/**
	 * Contains all of the suppliers and their column index
	 */
	private Map<String, CrossReference> _suppliersMap;
	
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
	
	public String getName(){
		return _name;
	}

	public List<String> getSuppliersHeaders() {
		return _suppliersHeader;
	}

	public boolean hasMinimum() {
		return _hasMinimum;
	}

	public String getMinimumColumnName() {
		return _minColumnName;
	}
}
