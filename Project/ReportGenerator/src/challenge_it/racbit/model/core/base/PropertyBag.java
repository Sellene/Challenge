package challenge_it.racbit.model.core.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class whose instances represent property bags. Instances are immutable, 
 * meaning, the set of properties never changes.
 * 
 * @author Paulo Pereira
 */
public final class PropertyBag implements Iterable<Property<?>> {

	/**
	 * Class whose instances are used to build {@link PropertyBag} instances.
	 * This class instances are not thread-safe.
	 * 
	 * Once a {@link PropertyBag} instance is obtained through a call to 
	 * {@link Builder#build()} the builder instance is rendered unusable.
	 */
	public static class Builder
	{
		/**
		 * The properties list. A {@code null} value indicates that the builder
		 * instance has already been used to produce the property bag instance.
		 */
		private List<Property<?>> _properties;
	
		/**
		 * Initiates a builder instance. Each instance can only be used to build
		 * one property bag.
		 */
		private Builder()
		{
			_properties = new ArrayList<Property<?>>();
		}
		
		/**
		 * Adds the given properties to the property bag instance being built.
		 * 
		 * @param properties The properties to be added to the property bag 
		 * instance being built
		 * @return The builder instance, thereby allowing method call chaining
		 * @throws {@link java.lang.IllegalStateException} if the property bag 
		 * instance has already been built
		 */
		public Builder add(Property<?>... properties)
		{
			
			if(_properties == null) throw new IllegalStateException();
			_properties.addAll(Arrays.asList(properties));
			return this;
		}
		
		/**
		 * Builds a {@link PropertyBag} instance with the properties already 
		 * added to the builder.
		 * 
		 * @return The {@link PropertyBag} instance
		 * @throws {@link java.lang.IllegalStateException} if the property bag 
		 * instance has already been built
		 */
		public PropertyBag build()
		{
			if(_properties == null) throw new IllegalStateException();
			Property<?>[] _props = _properties.toArray(new Property<?>[_properties.size()]);
			PropertyBag newInstance = new PropertyBag(_props);
			_properties = null;
			return newInstance;
		}
	}
	
	/**
	 * Factory method that produces a property bag builder.
	 * 
	 * @return A new builder instance
	 */
	public static Builder getBuilder()
	{
		return new Builder();
	}

	/**
	 * The container of properties. 
	 */
	private final Property<?>[] _properties; 
	
	/**
	 * Initiates an instance with the given set of properties. The received 
	 * array is not copied and, for that reason, the constructor is private.
	 * {@see PropertyBag.Builder}
	 * 
	 * @param properties The properties set
	 */
	private PropertyBag(Property<?>... properties)
	{
		_properties = properties;
	}

	/**
	 * Gets the property with the given name. If two properties with the same 
	 * name exist, returns the first one found.
	 * 
	 * @param name The property name
	 * @return The property instance, or {@literal null} if the product does
	 * not contain a property with the given name.
	 * @throws NullPointerException if {@code name} is {@literal null}
	 */
	public Property<?> get(String name)
	{
		for(Property<?> property : _properties)
			if(name.equals(property.getName()))
				return property;
		return null;
	}
	
	/**
	 * Gets an iterator for the bag properties.
	 * 
	 * @return The iterator instance
	 */
	public Iterator<Property<?>> iterator() 
	{
		return new Iterator<Property<?>>() {
			private int _currentIdx = 0;
			
			public boolean hasNext() { return _currentIdx < _properties.length; }

			public Property<?> next() 
			{
				if(!hasNext()) throw new NoSuchElementException();
				return _properties[_currentIdx++];
			}

			public void remove() { throw new UnsupportedOperationException(); }
			
		};
	}
	
	/**
	 * Gets the set of properties represented as an array.
	 * 
	 * @return An array bearing the instance's properties.
	 */
	public Property<?>[] toArray()
	{
		return Arrays.copyOf(_properties, _properties.length);
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * Two instances are equal if they have the same number of properties and
	 * all properties are equal. {@see Property#equals(Object)
	 */
	@Override 
	public boolean equals(Object other)
	{
        boolean result = false;
        if (other instanceof PropertyBag) 
        {
        	PropertyBag that = (PropertyBag) other;
        	if(_properties.length == that._properties.length)
        	{
            	result = true;
            	for(Property<?> curr : this._properties)
            	{
            		Property<?> foundAtOther = ((PropertyBag) other).get(curr.getName());
            		if(foundAtOther == null || !curr.equals(foundAtOther))
            		{
            			result = false;
            			break;
            		}
            	}
        	}
        }
        return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override 
	public int hashCode()
	{
		return java.util.Objects.hash((Object[]) _properties);
	}
	
	/**
	 * Gets the string representation of the current instance.
	 * 
	 * @return The string representation of the current instance
	 */
	@Override
	public String toString()
	{
		return Arrays.toString(_properties);
	}
}
