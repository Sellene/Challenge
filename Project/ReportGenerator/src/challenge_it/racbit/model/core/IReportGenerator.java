package challenge_it.racbit.model.core;

import java.util.Calendar;

import challenge_it.racbit.model.core.exceptions.CurrencyConversionException;
import challenge_it.racbit.model.core.exceptions.ReportGenerationException;

/**
 * Specifies the contract to be implemented by all product report generators.
 * 
 * @author Paulo Pereira
 */
public interface IReportGenerator {

	/**
	 * Produces a report with the given arguments.
	 * 
	 * @param reportDate the report date
	 * @param country the country of the products
	 * @param results the products to be included in the report
	 * @throws ReportGenerationException if an error occurred while producing the report
	 * @throws CurrencyConversionException if an error occurred while obtaining the conversion rate
	 */
	public void generate(Calendar reportDate, Country country, Iterable<Product> results) throws ReportGenerationException, CurrencyConversionException;
}
