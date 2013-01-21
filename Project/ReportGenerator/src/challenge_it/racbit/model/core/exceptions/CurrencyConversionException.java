package challenge_it.racbit.model.core.exceptions;


/**
 * Class whose instances represent errors that occurred while obtaining 
 * a currency rate conversion.
 * 
 * @author Paulo Pereira
 */
@SuppressWarnings("serial")
public class CurrencyConversionException extends Exception {

	/**
	 * {@inheritDoc}
	 */
	public CurrencyConversionException() {}

	/**
	 * {@inheritDoc}
	 */
	public CurrencyConversionException(String message) { super(message); }

	/**
	 * {@inheritDoc}
	 */
	public CurrencyConversionException(Throwable cause) { super(cause); }

	/**
	 * {@inheritDoc}
	 */
	public CurrencyConversionException(String message, Throwable cause) { super(message, cause); }
}
