package challenge_it.racbit.model.reports.generators.utils;


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
	private int _row;
	
	/**
	 * Defines the column index
	 */
	private int _column;
	
	/**
	 * Creates a instance of CrossReference
	 * 
	 * @param row The index of the row
	 * @param column The column of the row
	 */
	public CrossReference (int row, int column){
		_row = row;
		_column = column;
	}
	
	
	/**
	 * Gets the value for the row index
	 * 
	 * @return The row index
	 */
	public int getRow(){
		return _row;
	}
	
	/**
	 * Gets the value for the column index
	 * 
	 * @return The column index
	 */
	public int getColumn(){
		return _column;
	}
}