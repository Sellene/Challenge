package challenge_it.racbit.model.configurations;

/**
 * Represents a row and a column of a cell
 * 
 *  @author Cátia Moreira e João Taborda
 *
 */
public class CrossReference {
	/**
	 * Defines the row index
	 */
	private ExcelPair<Integer,Integer> _row;
	
	/**
	 * Defines the column index
	 */
	private ExcelPair<String,Integer> _column;
	
	/**
	 * Creates a instance of CrossReference
	 * 
	 * @param row The index of the row
	 * @param column The column of the row
	 */
	public CrossReference (ExcelPair<Integer,Integer> row, ExcelPair<String,Integer> column){
		_row = row;
		_column = column;
	}
	
	/**
	 * Gets the excel value for the row index
	 * 
	 * @return The row index
	 */
	public int getExcelRow(){
		return _row.getExcelValue();
	}
	
	/**
	 * Gets the excel value for the column index
	 * 
	 * @return The column index
	 */
	public String getExcelColumn(){
		return _column.getExcelValue();
	}
	
	/**
	 * Gets the value for the row index
	 * 
	 * @return The row index
	 */
	public int getRow(){
		return _row.getActualValue();
	}
	
	/**
	 * Gets the value for the column index
	 * 
	 * @return The column index
	 */
	public int getColumn(){
		return _column.getActualValue();
	}
}