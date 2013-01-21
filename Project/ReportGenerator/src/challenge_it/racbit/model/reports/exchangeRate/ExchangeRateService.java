package challenge_it.racbit.model.reports.exchangeRate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import challenge_it.racbit.model.core.exceptions.CurrencyConversionException;

import com.google.gson.Gson;

/**
 * Class that access to the conversion service
 * 
 * (http://www.google.com/ig/calculator?hl=en&q=1EUR=?GBP -> {lhs: "1 Euro",rhs: "0.801398951 British pounds",error: "",icc: true})
 * 
 *  @author Cátia Moreira e João Taborda
 *
 */
public class ExchangeRateService {

	/**
	 * Gets the conversion rate from an amount to another
	 * 
	 * @param from The currency to convert ( e.g.: EUR, USD, GBP, ...)
	 * @param to The currency wanted ( e.g.: EUR, USD, GBP, ...)
	 * @param numOfDecimals The number of decimals
	 * @return The current currency
	 * @throws IOException When there is no connection
	 * @throws CurrencyConversionException When occurs an error on convert result
	 */
	public static double getExchangeRate(String from, String to, int numOfDecimals) throws IOException, CurrencyConversionException {
		URL url;
		try {
			url = new URL(String.format("http://www.google.com/ig/calculator?q=1%s=?%s", from, to));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
			BufferedReader buff = new BufferedReader(in);
			
			Conversion c = new Gson().fromJson(buff.readLine(), Conversion.class);
			return c.exchangeRate(numOfDecimals);
			
		} catch (MalformedURLException e) {
			//Em principio não ocorre, porque o url lida com isso
		} catch (IOException e) {
			System.out.println("There's no connection.");
			throw e;
		} catch (CurrencyConversionException e) {
			System.out.println("Error in the conversion result");
			throw e;
		}
		
		return 0;
	}
}
