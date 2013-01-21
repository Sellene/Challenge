package challenge_it.racbit.model.core.base;

/** Added to correct the checkArgument Error **/
import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.Optional;


/**
 * Class whose instances represent properties. Property instances are 
 * immutable and are characterized by their name (a String) and the 
 * associated value. Note that immutable guarantees are not extended to
 * T instances; instead, they refer to the association between the property's
 * name and value.
 * 
 * @param <T> The type of the property's value
 * 
 * @author Paulo Pereira
 */
public final class Property<T>
{
	/**
	 * Factory method that produces a property instance with the given parameters.
	 * 
	 * TODO: Since instances are immutable, they may be safely shared, and hence cached to
	 * reduce working set size.
	 * 
	 * @param name The property's name
	 * @param value The property's value
	 * @return The property instance
	 * @throws NullPointerException if {@code name} is {@literal null}
	 * @throws IllegalArgumentException if {@code name} is an empty string or exclusively
	 * composed of separator characters
	 */
	public static <T> Property<T> newInstance(String name, T value)
	{
		checkArgument(!(name=name.trim()).isEmpty(), "Empty string is an illegal property name");
		return new Property<T>(name, value); 
	}
	
	/**
	 * The property name.
	 */
	private final String _name;
	
	/**
	 * The property value.
	 */
	private final Optional<T> _value;
	
	/**
	 * Creates an instance with the given parameters.
	 * 
	 * @param name The property's name
	 * @param value The property's value
	 */
	private Property(String name, T value) 
	{ 
		_name = name;
		_value = Optional.fromNullable(value); 
	}
	
	/**
	 * Gets the property name. 
	 * 
	 * @return The property's name
	 */
	public String getName() { return _name; }
	
	/**
	 * Gets the property value represented as a {@link com.google.common.base.Optional}
	 * instance.
	 *  
	 * @return The property's value
	 */
	public Optional<T> getValue() { return _value; }

	/**
	 * {@inheritDoc}
	 */
	@Override 
	public boolean equals(Object other)
	{
        boolean result = false;
        if (other instanceof Property<?>) 
        {
        	Property<?> that = (Property<?>) other;
        	result = (this == that) || (this._name.equals(that._name) && this._value.equals(that._value));
        }
        return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override 
	public int hashCode()
	{
		return java.util.Objects.hash(_name, _value);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override 
	public String toString()
	{
		return new StringBuilder(_name)
						.append(": ")
						.append(_value.isPresent() ? _value.get().toString() : "null")
						.toString();
	}
}