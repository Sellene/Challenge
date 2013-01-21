package challenge_it.racbit.model.core.exceptions;

/**
 * Class whose instances represent errors that occurred while performing 
 * web scraping.
 * 
 * @author Paulo Pereira
 */
@SuppressWarnings("serial")
public class ScrapingException extends Exception {

	/**
	 * {@inheritDoc}
	 */
	public ScrapingException() {}

	/**
	 * {@inheritDoc}
	 */
	public ScrapingException(String message) { super(message); }

	/**
	 * {@inheritDoc}
	 */
	public ScrapingException(Throwable cause) { super(cause); }

	/**
	 * {@inheritDoc}
	 */
	public ScrapingException(String message, Throwable cause) { super(message, cause); }
}
