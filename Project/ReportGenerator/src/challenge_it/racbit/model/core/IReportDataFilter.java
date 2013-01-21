package challenge_it.racbit.model.core;

import challenge_it.racbit.model.core.base.PropertyBag;


/**
 * Specifies the contract to be implemented by all product report filters.
 * Each filter produces, for the given results, the data that will be included
 * in the associated report.
 * 
 * @author Paulo Pereira
 */
public interface IReportDataFilter {
	
	/**
	 * Gets the products' data filtered from the given products. 
	 * 
	 * @param brokerResults The collection bearing the products existing at a 
	 * specific broker
	 * @return The resulting filtered data
	 */
	public Iterable<Product> filter(String broker, Iterable<PropertyBag> brokerResults); 
}
