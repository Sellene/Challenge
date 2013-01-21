package challenge_it.racbit.model.reports.configurations;

/**
 * 
 * Represents the content of the XML file, with the configuration to generate a XLSX file.
 * 
 *  @author Cátia Moreira e João Taborda
 *
 */
public abstract class Configuration{
	
	/**
	 * Defines the index number of a sheet in XLSX file
	 */
	private int _sheetNumber;

	/**
	 * Defines the row and column for the conversion rate
	 */
	private CrossReference _rateCell;	
	
	/**
	 * Defines the filename of the template file
	 */
	private String _templateFilename;
	
	/**
	 * Gets the index number of a sheet in XLSX file
	 * 
	 * @return The index number
	 */
	public int getSheetNumber(){
		return _sheetNumber;
	}
	
	/**
	 * Sets the index number of a sheet in XLSX file
	 * 
	 * @param num The index number
	 */
	public void setSheetNumber(int num){
		_sheetNumber = num;
	}
	
	
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
	
	/**
	 * Gets the filename of the XLSX template file
	 * 
	 * @return The filename
	 */
	public String getTemplateFilename(){
		return _templateFilename;
	}
	
	/**
	 * Sets the filename of the XLSX template file
	 * 
	 * @param filename The name of the template file
	 */
	public void setTemplateFilename(String filename){
		_templateFilename = filename;
	}
}