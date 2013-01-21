package challenge_it.racbit.model.reports.exchangeRate;

import challenge_it.racbit.model.core.exceptions.CurrencyConversionException;

/**
 * Class that represents the information received in JSON from the ExchangeRateService
 * Because the JSON string must be converted to an object in Java, the instance fields should have the same name as the ones in the JSON string 
 * 
 * (http://www.google.com/ig/calculator?hl=en&q=1EUR=?GBP -> {lhs: "1 Euro",rhs: "0.801398951 British pounds",error: "",icc: true})
 * 
 *  @author Cátia Moreira e João Taborda
 *
 */
public class Conversion {

	/**
	 * The result of the conversion
	 */
	private String rhs;
	
	/**
	 * Indicates if the conversion was successful
	 */
	private boolean icc;
	
	/**
	 * Gets the conversion rate round to the number of decimals
	 * 
	 * @param numberOfDecimals - Number of decimal to round
	 * @return The conversion rate
	 * @throws ConversionException When couldn't get the converted rate
	 */
	public double exchangeRate(int numberOfDecimals) throws CurrencyConversionException {
		if(!icc)
			throw new CurrencyConversionException();
		return roundToDecimals(Double.parseDouble(rhs.split(" ")[0]), numberOfDecimals);
	}
	
	/**
	 * @param d - The value to round
	 * @param c - The number of decimals
	 * @return the value conversion
	 */
	private static double roundToDecimals(double d, int c) {
		int temp=(int)((d*Math.pow(10,c)));
		return (((double)temp)/Math.pow(10,c));
	}
}
