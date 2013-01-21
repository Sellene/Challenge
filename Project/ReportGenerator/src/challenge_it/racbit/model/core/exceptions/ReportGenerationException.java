package challenge_it.racbit.model.core.exceptions;

/**
 * Class whose instances represent errors that occurred while generating 
 * a report.
 * 
 * @author Paulo Pereira
 */
@SuppressWarnings("serial")
public class ReportGenerationException extends Exception {

	/**
	 * {@inheritDoc}
	 */
	public ReportGenerationException() {}

	/**
	 * {@inheritDoc}
	 */
	public ReportGenerationException(String message) { super(message); }

	/**
	 * {@inheritDoc}
	 */
	public ReportGenerationException(Throwable cause) { super(cause); }

	/**
	 * {@inheritDoc}
	 */
	public ReportGenerationException(String message, Throwable cause) { super(message, cause); }
}
