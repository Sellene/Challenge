package challenge_it.racbit.model.core.base;

import java.util.Iterator;

/**
 * Base class for strong typed implementation of a {@link PropertyBag}.
 * 
 * Common behavior is implemented through delegation to the associated 
 * {@link PropertyBag} instance, which is obtained by calling the 
 * template method {@link #propertyBag()}.
 * 
 * @author Paulo Pereira
 */
public abstract class StrongTypedPropertyBagBase implements Iterable<Property<?>> {

	/**
	 * Template method to get the {@link PropertyBag} instance to be used.
	 * 
	 * @return the {@link PropertyBag} instance to be used.
	 */
	protected abstract PropertyBag propertyBag();

	/**
	 * Gets an iterator for the bag's properties.
	 * 
	 * @return The iterator instance
	 */
	@Override
	public Iterator<Property<?>> iterator() 
	{
		return propertyBag().iterator();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object other)
	{
        boolean result = false;
        if (getClass() == other.getClass() && other instanceof StrongTypedPropertyBagBase) 
        {
        	StrongTypedPropertyBagBase that = (StrongTypedPropertyBagBase) other;
        	result = (this == that) || this.propertyBag().equals(that.propertyBag());
        }
        return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override 
	public int hashCode()
	{
		return propertyBag().hashCode();
	}
}
