package challenge_it.racbit.model.core;

import java.io.IOException;
import java.util.Calendar;

import challenge_it.racbit.model.core.base.PropertyBag;
import challenge_it.racbit.model.core.exceptions.ScrapingException;

/**
 * Specifies the contract to be implemented by all product brokers.
 * 
 * Broker products are described by {@link challenge_it.racbit.model.core.base.PropertyBag}
 * instances. Each product instance may contain the following properties ({@link 
 * challenge_it.racbit.model.core.base.Property} instances):
 * <ul>
 *  <li> Location: String </li>
 *  <li> Start: Date </li>
 *  <li> Day Span: Integer </li>
 *  <li> Price: Double (mandatory) </li>
 *  <li> Broker Group: String </li>
 *  <li> Group: Product.Group </li>
 *  <li> Name: String </li>
 *  <li> Capacity: String </li>
 *  <li> Doors: String </li>
 *  <li> AC: Boolean </li>
 *  <li> Insurance Package: Product.InsurancePackage </li>
 *  <li> Supplier: String </li>
 *  <li> Supplier Type: Product.SupplierType </li>
 * </ul>
 * 
 * @author Paulo Pereira
 */
public interface IBroker {

	/**
	 * Gets the product list from the current broker.  
	 * 
	 * @param start the pickup date 
	 * @param end The drop-off date
	 * @param country The destination country
	 * @param location The location where the product is to be acquired (i.e. city) 
	 *  
	 * @return The products made available by the broker
	 */
	public Iterable<PropertyBag> getProducts(Calendar start, Calendar end, Country country, String location) 
			throws IOException, ScrapingException;
	
	/**
	 * Gets the broker's name.
	 * 
	 * @return the broker's name
	 */
	public String getName();
	
	/**
	 * Gets the string representation of the broker's base URL.
	 * 
	 * @return the broker's base URL 
	 */
	public String getBaseURL();
}
