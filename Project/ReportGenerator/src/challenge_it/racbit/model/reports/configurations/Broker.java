package challenge_it.racbit.model.reports.configurations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import challenge_it.racbit.model.core.Product;

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
	 * List of the broker's products
	 */
	private List<Product> _brokerProducts;
	
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
		_suppliersHeader = new LinkedList<String>();
		_brokerProducts = new LinkedList<Product>();
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
	public List<String> getSuppliersList() {
		return _suppliersHeader;
	}

	/**
	 * Gets the flag that indicates if the broker should have a minimum column
	 * 
	 * @return the value for _hasMinimum
	 */
	public boolean hasMinimum() {
		return _hasMinimum;
	}

	/**
	 * Gets the name of the minimum column
	 * 
	 * @return the value of _minColumnName
	 */
	public String getMinimumColumnName() {
		return _minColumnName;
	}

	/**
	 * Gets the list of the broker's products
	 * 
	 * @return the value of _brokerProducts
	 */
	public List<Product> getProducts() {
		return _brokerProducts;
	}

	/**
	 * Adds a product to the list of products
	 */
	public void addProduct(Product product) {
		_brokerProducts.add(product);
	}
}
