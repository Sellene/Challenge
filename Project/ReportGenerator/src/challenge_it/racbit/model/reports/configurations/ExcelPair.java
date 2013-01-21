package challenge_it.racbit.model.configurations;

/**
 * Represents a pair containing the excel value and the value that would be used
 * 
 *  @author Cátia Moreira e João Taborda
 *
 */
 public class ExcelPair<E,V>{
 
	/**
	 * Represents the excel value
	 */
	private E _excelValue;
	
	/**
	 * Represents the value to be used
	 */
	private V _actualValue;
	
	/**
	 * Creates a instance of ExcelPair
	 * 
	 * @param excel The excel value
	 * @param value The value to be used
	 */
	public ExcelPair(E excel, V value){
		_excelValue = excel;
		_actualValue = value;
	}
	
	/**
	 * Gets the excel value
	 * 
	 * @return The excel value
	 */
	public E getExcelValue(){
		return _excelValue;
	}
	
	/**
	 * Gets the value to be used
	 * 
	 * @return The value
	 */
	public V getActualValue(){
		return _actualValue;
	}
}