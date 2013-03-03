package challenge_it.racbit.model.reports.configurations;

import challenge_it.racbit.model.reports.generators.utils.CrossReference;

/**
 * 
 * Represents the content of the XML file, with the configuration to generate a Rate Benchmarking Report file.
 * 
 *  @author Cátia Moreira e João Taborda
 *
 */
public class BenchmarkingReportConfiguration extends Configuration{

	/**
	 * Defines the row and column for the title
	 */
	private CrossReference _titleCell;
	
	/**
	 * Defines the row and column for the consultation date
	 */
	private CrossReference _consultationDateCell;
	
	/**
	 * Defines the row and column for the hour
	 */
	private CrossReference _hourCell;
	
	/**
	 * Defines the row and column for the pick up date
	 */
	private CrossReference _pickUpDateCell;
	
	/**
	 * The reference to the first cell that has values
	 */
	private CrossReference _gridValuesFirstCell;
	
	/**
	 * Defines the row and column for the location
	 */
	private CrossReference _locationCell;
	
	/**
	 * Defines the row and column for the group
	 */
	private CrossReference _groupCell;
	
	/**
	 * Defines the row and column for the number of days
	 */
	private CrossReference _numberOfDaysCell;
	
	/**
	 * Gets the row and column for the consultation date
	 * 
	 * @return The row and column
	 */
	public CrossReference getTitleCell(){
		return _titleCell;
	}
	
	/**
	 * Sets the row and column for the consultation date
	 * 
	 * @param c CrossReference with the row and column
	 */
	public void setTitleCell(CrossReference c){
		_titleCell = c;
	}
	
	
	/**
	 * Gets the row and column for the consultation date
	 * 
	 * @return The row and column
	 */
	public CrossReference getConsultationDateCell(){
		return _consultationDateCell;
	}
	
	/**
	 * Sets the row and column for the consultation date
	 * 
	 * @param c CrossReference with the row and column
	 */
	public void setConsultationDateCell(CrossReference c){
		_consultationDateCell = c;
	}
	
	/**
	 * Gets the row and column for the hour
	 * 
	 * @return The row and column
	 */
	public CrossReference getHourCell(){
		return _hourCell;
	}
	
	/**
	 * Sets the row and column for the hour
	 * 
	 * @param c CrossReference with the row and column
	 */
	public void setHourCell(CrossReference c){
		_hourCell = c;
	}
	
	/**
	 * Gets the row and column for the pick up date
	 * 
	 * @return The row and column
	 */
	public CrossReference getPickUpDateCell(){
		return _pickUpDateCell;
	}
	
	/**
	 * Sets the row and column for the pick up date
	 * 
	 * @param c CrossReference with the row and column
	 */
	public void setPickUpDateCell(CrossReference c){
		_pickUpDateCell = c;
	}
	
	/**
	 * Gets the first cell of the values
	 * 
	 * @return the first cell of the values
	 */
	public CrossReference getGridValuesFirstCell() {
		return _gridValuesFirstCell;
	}

	/**
	 * Sets the first cell of the values
	 */
	public void setGridValuesFirstCell(CrossReference _gridValuesFirstCell) {
		this._gridValuesFirstCell = _gridValuesFirstCell;
	}
	
	/**
	 * Gets the row and column for the location
	 * 
	 * @return The row and column
	 */
	public CrossReference getLocationCell(){
		return _locationCell;
	}
	
	/**
	 * Sets the row and column for the location
	 * 
	 * @param c CrossReference with the row and column
	 */
	public void setLocationCell(CrossReference c){
		_locationCell = c;
	}
	
	/**
	 * Gets the row and column for the group
	 * 
	 * @return The row and column
	 */
	public CrossReference getGroupCell(){
		return _groupCell;
	}
	
	/**
	 * Sets the row and column for the group
	 * 
	 * @param c CrossReference with the row and column
	 */
	public void setGroupCell(CrossReference c){
		_groupCell = c;
	}
	
	/**
	 * Gets the row and column for the number of days
	 * 
	 * @return The row and column
	 */
	public CrossReference getNumberOfDaysCell(){
		return _numberOfDaysCell;
	}
	
	/**
	 * Sets the row and column for the number of days
	 * 
	 * @param c CrossReference with the row and column
	 */
	public void setNumberOfDaysCell(CrossReference c){
		_numberOfDaysCell = c;
	}
}