package challenge_it.racbit.model.core;

import java.util.Date;

import challenge_it.racbit.model.core.base.Property;
import challenge_it.racbit.model.core.base.PropertyBag;
import challenge_it.racbit.model.core.base.StrongTypedPropertyBagBase;

/**
 * Strong type implementation of a {@link PropertyBag} that represents products to 
 * be included in generated reports.
 * 
 * @author Paulo Pereira
 */
public final class Product extends StrongTypedPropertyBagBase {

	/**
	 * Enumeration used to specify the insurance type associated to products to be 
	 * included in reports.
	 */
	public static enum InsurancePackage { UNKNOWW, REGULAR, NO_EXCESS, FULLY_REFUNDABLE }
	
	/**
	 * Enumeration used to specify the group to which products to be included in reports 
	 * belong (e.g. vehicle's class)
	 */
	public static enum Group { MINI_A, C, P, E, E1, J, J1, Q, F, K, G, L, H, N, O, Z, NOT_MAPPED }
	
	/**
	 * Enumeration used to specify the supplier type of the product' supplier.
	 */
	public static enum SupplierType { UNKNOWN, TRADITIONAL, LOW_COST }
	
	/**
	 * Contains the product's properties 
	 */
	private final PropertyBag _properties;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PropertyBag propertyBag() 
	{
		return _properties;
	}

	/**
	 * Constants used to refer to the product's property names
	 */
	private static final String BROKER = "broker", LOCATION = "location", SUPPLIER = "supplier", SUPPLIER_TYPE = "sType",
			GROUP = "group", PRICE = "price", INSURANCE = "insurance",
			START_DATE = "start", DAYS_COUNT = "countDays";

	/**
	 * Creates an instance with the given arguments.
	 * 
	 * @param broker The product broker
	 * @param location The product location (city)
	 * @param supplier The product supplier
	 * @param supplierType The product supplier type
	 * @param group The product group
	 * @param price The product price
	 * @param insurance The product insurance package
	 * @param startDate The product's rate start date
	 * @param numberOfDays The product's rate number of days
	 * @throws NullPointerException if any argument is {@literal null}
	 */
	public Product(String broker, String location, String supplier, Product.SupplierType supplierType, Group group, 
			double price, InsurancePackage insurance, Date startDate, int numberOfDays)
	{
		if(supplier == null || supplierType == null || group == null || insurance == null || startDate == null)
			throw new NullPointerException();
		
		_properties = PropertyBag.getBuilder()
						.add(
							Property.newInstance(BROKER, broker),
							Property.newInstance(LOCATION, location),
							Property.newInstance(SUPPLIER, supplier),
							Property.newInstance(SUPPLIER_TYPE, supplierType),
							Property.newInstance(GROUP, group),
							Property.newInstance(PRICE, price),
							Property.newInstance(INSURANCE, insurance),
							Property.newInstance(START_DATE, startDate),
							Property.newInstance(DAYS_COUNT, numberOfDays))
						.build();
	}
	
	/**
	 * Gets the product broker
	 * 
	 * @return The product broker
	 */
	public String getBroker()
	{
		return (String) _properties.get(BROKER).getValue().get();
	}
	
	/**
	 * Gets the product location (e.g. Lisboa).
	 * 
	 * @return The product supplier
	 */
	public String getLocation()
	{
		return (String) _properties.get(LOCATION).getValue().get();
	}

	/**
	 * Gets the product supplier (e.g. Auto Europe).
	 * 
	 * @return The product supplier
	 */
	public String getSupplier()
	{
		return (String) _properties.get(SUPPLIER).getValue().get();
	}

	/**
	 * Gets the product's supplier type.
	 * 
	 * @return The product supplier type
	 */
	public SupplierType getSupplierType()
	{
		return (SupplierType) _properties.get(SUPPLIER_TYPE).getValue().get();
	}
	
	/**
	 * Gets the product group (e.g. economic class).
	 * 
	 * @return The product group
	 */
	public Group getGroup()
	{
		return (Group) _properties.get(GROUP).getValue().get();
	}

	/**
	 * Gets the product price.
	 * 
	 * @return The product price
	 */
	public double getPrice()
	{
		return (Double) _properties.get(PRICE).getValue().get();
	}
	
	/**
	 * Gets the product insurance package.
	 * 
	 * @return The product insurance package
	 */
	public InsurancePackage getInsurancePackage()
	{
		return (InsurancePackage) _properties.get(INSURANCE).getValue().get();
	}
	
	/**
	 * Gets the product's rate start date.
	 * 
	 * @return The product's rate start date
	 */
	public Date getStartDate()
	{
		return (Date) _properties.get(START_DATE).getValue().get();
	}

	/**
	 * Gets the product's rate day interval (number of days).
	 * 
	 * @return The product's rate day interval
	 */
	public int getNumberOfDays()
	{
		return (Integer) _properties.get(DAYS_COUNT).getValue().get();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString()
	{
		return new StringBuilder("{ Supplier: ")
			.append(getSupplier())
			.append(", Group: ")
			.append(getGroup())
			.append(", Price: ")
			.append(getPrice())
			.append(", Insurance: ")
			.append(getInsurancePackage())
			.append(" }").toString();
	}
}
