package challenge_it.racbit.model.reports.generators;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;

public class CellStyles {

	public static final short SMALL_TEXT = 8;
	public static final short MEDIUM_TEXT = 10;
	public static final short LARGE_TEXT = 12;
	
	/**
	 * Gets the default style used for the cells that contains values
	 * 
	 * @param workbook The representation of the file
	 * @return the default cell style
	 */
	public static CellStyle getDefaultCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
	    Font font = workbook.createFont();
	    font.setFontHeightInPoints(SMALL_TEXT);
	    font.setFontName("Verdana");
		style.setFont(font);
		
		style.setDataFormat(workbook.createDataFormat().getFormat("0.00"));
		
		return style;
	}
	
	public static CellStyle setThinBorders(CellStyle style){
		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		
		return style;
	}
	
	public static CellStyle setMediumBorders(CellStyle style){
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		style.setBorderTop(CellStyle.BORDER_MEDIUM);
		
		return style;
	}
	
	public static CellStyle setMediumBottomBorder(CellStyle style){
		style.setBorderBottom(CellStyle.BORDER_MEDIUM);
		
		return style;
	}
	
	public static CellStyle setMediumRightBorder(CellStyle style){
		style.setBorderRight(CellStyle.BORDER_MEDIUM);
		
		return style;
	}
	
	public static CellStyle setMediumLeftBorder(CellStyle style){
		style.setBorderLeft(CellStyle.BORDER_MEDIUM);
		
		return style;
	}
	
	public static CellStyle setMediumTopBorder(CellStyle style){
		style.setBorderTop(CellStyle.BORDER_MEDIUM);
		
		return style;
	}
	
	public static CellStyle setBoldAndColor(CellStyle style, short color, short textSize, Workbook workbook){
		Font font = workbook.createFont();
	    font.setFontHeightInPoints(textSize);
	    font.setFontName("Verdana");
	    font.setColor(color);
	    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		
		return style;
	}
	
	public static CellStyle setColor(CellStyle style, short color, short textSize, Workbook workbook){
		Font font = workbook.createFont();
	    font.setFontHeightInPoints(textSize);
	    font.setFontName("Verdana");
	    font.setColor(color);
		style.setFont(font);
		
		return style;
	}
	
	public static CellStyle setBold(CellStyle style, short textSize, Workbook workbook){
		Font font = workbook.createFont();
	    font.setFontHeightInPoints(textSize);
	    font.setFontName("Verdana");
	    font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style.setFont(font);
		
		return style;
	}
	
	public static CellStyle setBackground(CellStyle style, short color){
		style.setFillForegroundColor(color);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		return style;
	}
	
	public static CellStyle setRotation(CellStyle style){
		style.setRotation((short)90);
		
		return style;
	}
}


