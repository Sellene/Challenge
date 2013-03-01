package challenge_it.racbit.model.reports.configurations;

import challenge_it.racbit.model.reports.generators.utils.CrossReference;

/**
 * 
 * Represents the content of the XML file, with the configuration to generate a XLSX file.
 * 
 *  @author Cátia Moreira e João Taborda
 *
 */
public abstract class Configuration{

	/**
	 * Defines the row and column for the conversion rate
	 */
	private CrossReference _rateCell;	
	
	
	/**
	 * Gets the row and column for the conversion rate
	 * 
	 * @return The row and column
	 */
	public CrossReference getRateCell(){
		return _rateCell;
	}
	
	/**
	 * Sets the row and column for the conversion rate
	 * 
	 * @param c CrossReference with the row and column
	 */
	public void setRateCell(CrossReference c){
		_rateCell = c;
	}
}