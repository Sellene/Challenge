package challenge_it.racbit.model.reports.generators;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.record.CFRuleRecord.ComparisonOperator;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFConditionalFormattingRule;
import org.apache.poi.xssf.usermodel.XSSFFontFormatting;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import challenge_it.racbit.model.core.Country;
import challenge_it.racbit.model.core.IReportGenerator;
import challenge_it.racbit.model.core.Product;
import challenge_it.racbit.model.core.Product.InsurancePackage;
import challenge_it.racbit.model.core.exceptions.CurrencyConversionException;
import challenge_it.racbit.model.core.exceptions.ReportGenerationException;
import challenge_it.racbit.model.reports.configurations.Broker;
import challenge_it.racbit.model.reports.configurations.ConfigurationReader;
import challenge_it.racbit.model.reports.configurations.CrossReference;
import challenge_it.racbit.model.reports.configurations.ExcelPair;
import challenge_it.racbit.model.reports.configurations.RateShopReportConfiguration;
import challenge_it.racbit.model.reports.configurations.RateShopReportConfigurationReader;
import challenge_it.racbit.model.reports.exchangeRate.ExchangeRateService;

/**
 * Class that generates a RateShopUK Report
 * 
 *  @author C�tia Moreira e Jo�o Taborda
 *
 */
public class RateShopUKReportGenerator implements IReportGenerator {
	
	private class RateShopUKReportInfo{
		private Calendar _startDate;
		private Calendar _endDate;
		private String _destination;
		
		public RateShopUKReportInfo(Calendar doDate, Calendar puDate, String destination){
			_startDate = puDate;
			_endDate = doDate;
			_destination = destination;
		}
		
		public Calendar getStartDate(){
			return _startDate;
		}
		
		public Calendar getEndDate(){
			return _endDate;
		}
		
		public String getDestination(){
			return _destination;
		}
	}
	
	
	private static final String XML_CONFIGURATION = "RateShop_Reports/UK_Reports/RateShopUKReportConfiguration.xml";
	private static final String XML_SCHEMA = "RateShop_Reports/RateShopReportSchema.xsd";
	private static final String XML_TRANSFORMATION = "ReportTransformation.xsl";
	
	private static final String PATH = "templates/";
	
	@Override
	public void generate(Calendar reportDate, Country country, Iterable<Product> results) throws ReportGenerationException, CurrencyConversionException {
		try {
			
			RateShopReportConfiguration config = (RateShopReportConfiguration) new RateShopReportConfigurationReader().read(XML_CONFIGURATION, XML_SCHEMA, XML_TRANSFORMATION);
			
			Workbook workbook = new XSSFWorkbook(RateShopUKReportGenerator.class.getClassLoader().getResourceAsStream(PATH + config.getTemplateFilename()));
			Sheet sheet = workbook.getSheetAt(config.getSheetNumber());
			
			// Gets from the result the complete information about a broker
			// This is used to know the concrete number of suppliers each broker
			// This method also returns the information about the destination and the days to fill the report values
			RateShopUKReportInfo reportInfo = completeBrokerInformation(config, results);
			
			setFixedValues(sheet, config, reportInfo);
						
			int brokerFirstSupplierIndex = 0;
				
			for(Broker broker : config.getBrokers().values())
			{
				for (Product product : broker.getProducts()) {
					
					CrossReference supplierCell = broker.getSuppliersMap().get(product.getSupplier());
					CrossReference groupCell =  config.getGroupsMap().get(product.getGroup().toString());
					
					
					if(supplierCell != null && groupCell != null)
					{
						Cell newCell = sheet.getRow(groupCell.getRow()).createCell(supplierCell.getColumn() + brokerFirstSupplierIndex);
						
						CellStyle style = getDefaultCellStyle(workbook);
		
						if(product.getInsurancePackage() == InsurancePackage.NO_EXCESS)
							style.setFillForegroundColor(HSSFColor.YELLOW.index);
						else 
							if(product.getInsurancePackage() == InsurancePackage.FULLY_REFUNDABLE)
								style.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
		
						newCell.setCellStyle(style);
						newCell.setCellValue(product.getPrice());
						newCell.setCellType(Cell.CELL_TYPE_NUMERIC);
					}
				}
				
				setMinimumColumn(workbook, sheet, config, broker, brokerFirstSupplierIndex);
				setTableCells(workbook,sheet, config, broker, brokerFirstSupplierIndex);
				/// TODO ???????setMinimumColor(workbook,sheet, config, broker, brokerFirstSupplierIndex);
				fillSuppliersHeader(workbook, sheet, config.getGridValuesFirstCell(), brokerFirstSupplierIndex, broker);
				
				brokerFirstSupplierIndex += broker.getSuppliersList().size() + 1;
			}
			
			fillGroups(workbook, sheet, config);
			saveFile(workbook, reportInfo);
		}
		catch (CurrencyConversionException e){
			throw e;
		}
		catch (Exception e) {
			throw new ReportGenerationException(e);
		}
	}
	
	private void fillGroups(Workbook workbook, Sheet sheet, RateShopReportConfiguration config) {
		int row = config.getGridValuesFirstCell().getRow();
		int column = config.getGridValuesFirstCell().getColumn()-1;
		
		for (String  group : config.getGroupsList()) {
			Cell groupCell = sheet.getRow(row++).createCell(column);
			CellStyle groupCellStyle = getDefaultCellStyle(workbook);
			groupCellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
			groupCell.setCellStyle(groupCellStyle);
			groupCell.setCellValue(group);
			groupCell.setCellType(Cell.CELL_TYPE_STRING);
		}
	}

	private void fillSuppliersHeader(Workbook workbook, Sheet sheet, CrossReference firstCellOfTheGrid, int brokerIndex, Broker broker) {

		int row = firstCellOfTheGrid.getRow()-1;
		int firstColumn = firstCellOfTheGrid.getColumn() + brokerIndex;
		
		for (String  supplier : broker.getSuppliersList()) {
			Cell supplierCell = sheet.getRow(row).createCell(firstColumn + broker.getSuppliersMap().get(supplier).getColumn());
			CellStyle supplierCellStyle = getDefaultCellStyle(workbook);
			supplierCellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
			supplierCell.setCellStyle(supplierCellStyle);
			supplierCell.setCellValue(supplier);
			supplierCell.setCellType(Cell.CELL_TYPE_STRING);
		}
		
		//TODO: Unir as celulas po nome do broker
	}

	private RateShopUKReportInfo completeBrokerInformation(RateShopReportConfiguration config, Iterable<Product> results) {
		RateShopUKReportInfo reportInfo = null;
		
		Map<String, Broker> brokers = config.getBrokers();
		
		for (Product product : results) {
			
			if(reportInfo == null){
				Calendar puDate = Calendar.getInstance();
				puDate.setTime(product.getStartDate());
				
				Calendar doDate = Calendar.getInstance();
				doDate.setTime(product.getStartDate());
				doDate.add(Calendar.DAY_OF_MONTH, product.getNumberOfDays());
				
				reportInfo = new RateShopUKReportInfo(puDate, doDate, product.getLocation());
			}
			
			Broker b = brokers.get(product.getBroker());
			
			if(b != null){
				
				if(!b.getSuppliersList().contains(product.getSupplier())){
					b.addSupplier(product.getSupplier(), new CrossReference(new ExcelPair<Integer, Integer>(null, config.getGridValuesFirstCell().getRow()-1), new ExcelPair<String, Integer>(null, config.getGridValuesFirstCell().getColumn() + b.getSuppliersList().size())));
				}
				b.addProduct(product);
			}
			
		}
		
		return reportInfo;
	}

//	private void setTableStyleBoundariesAndStyles(RateShopReportConfiguration config, Sheet sheet, Workbook workbook, int lastColumnOfLastBroker) {
//		int firstRow = config.getGridValuesFirstCell().getRow(); 
//		int lastRow = firstRow + config.getGroupsList().size();
//		int firstColumn = config.getGridValuesFirstCell().getColumn();
//		int lastColumn = firstColumn + lastColumnOfLastBroker;  
//		
//		
//		
//		//� necess�rio? cada celula que se cria j� gera o seu proprio border
//		
//		
//		Cell cell;
//
//		//Sets left border
//		for(int i = firstRow; i<lastRow; i++){
//			cell = sheet.getRow(i).getCell(firstColumn);
//			CellStyle styleLeft = workbook.createCellStyle();
//			styleLeft.cloneStyleFrom(cell.getCellStyle());
//			styleLeft.setBorderLeft(CellStyle.BORDER_MEDIUM);
//			cell.setCellStyle(styleLeft);
//		}
//		
//		//Sets top border
//		for(int i = firstColumn; i<lastColumn; i++){
//			cell = sheet.getRow(firstRow).getCell(i);
//			CellStyle styleTop = workbook.createCellStyle();
//			styleTop.cloneStyleFrom(cell.getCellStyle());
//			styleTop.setBorderTop(CellStyle.BORDER_MEDIUM);
//			cell.setCellStyle(styleTop);
//		}
//		
//		//Sets bottom border
//		for(int i = firstColumn-1; i<lastColumn; i++){
//			cell = sheet.getRow(lastRow-1).getCell(i);
//			CellStyle styleBottom = workbook.createCellStyle();
//			styleBottom.cloneStyleFrom(cell.getCellStyle());
//			styleBottom.setBorderBottom(CellStyle.BORDER_MEDIUM);
//			cell.setCellStyle(styleBottom);
//		}
//		
//		//Sets right border
//		for(int i = firstRow; i<lastRow; i++){
//			cell = sheet.getRow(i).getCell(lastColumn);
//			CellStyle styleRight = workbook.createCellStyle();
//			styleRight.cloneStyleFrom(cell.getCellStyle());
//			styleRight.setBorderRight(CellStyle.BORDER_MEDIUM);
//			cell.setCellStyle(styleRight);
//		}
//		
//	}

//	private void setMinimumColor(RateShopReportConfiguration config, Sheet sheet, Workbook workbook, int firstRow, int lastRow, int firstColumn, int lastColumn) {
//		int column = config.getGridValuesFirstCell().getColumn() + broker.getSuppliersHeaders().size();
//		int row = config.getGridValuesFirstCell().getRow();
//		
//		Cell minimumCell; 
//		
//		Cell cell;
//		CellStyle style = workbook.createCellStyle();
//		style = getDefaultCellStyle(workbook);
//
//		for(int i = firstRow; i<lastRow; i++){
//			
//			minimumCell = sheet.getRow(i).getCell(lastColumn);
//			FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
//			
//			CellValue cellValue = evaluator.evaluate(minimumCell);
//			
//			SheetConditionalFormatting cf = sheet.getSheetConditionalFormatting();
//			XSSFConditionalFormattingRule rule = (XSSFConditionalFormattingRule) cf.createConditionalFormattingRule(
//			     ComparisonOperator.EQUAL, 
//			     cellValue.getNumberValue() + "",
//			     null
//			);
//
//			 // Create pattern with red background
//			XSSFFontFormatting fontFmt = rule.createFontFormatting();
//			fontFmt.setFontColorIndex(HSSFColor.RED.index);
//
//			 // Define a region containing first column
//			CellRangeAddress[] cra = {new CellRangeAddress(i, i, 
//					 firstColumn, lastColumn-1) };
//
//			 // Apply Conditional Formatting rule defined above to the regions  
//			cf.addConditionalFormatting(cra, rule);
//		}		
//	}

	private void setTableCells(Workbook workbook, Sheet sheet, RateShopReportConfiguration config, Broker b, int BrokerIndex) {
		
		for(int i = config.getGridValuesFirstCell().getRow(); i < config.getGroupsList().size(); i++)
		{
			for(int j= BrokerIndex; j < BrokerIndex + b.getSuppliersList().size(); j++)
			{
				if(sheet.getRow(i).getCell(j) == null)
				{
					Cell newCell = sheet.getRow(i).createCell(j);
					
					CellStyle style = getDefaultCellStyle(workbook);
					style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
					style.setFillPattern(CellStyle.SOLID_FOREGROUND);
					
					newCell.setCellStyle(style);
				}
			}
		}
		
	}

	private void setMinimumColumn(Workbook workbook, Sheet sheet, RateShopReportConfiguration config, Broker broker, int brokerIndex) {
		int column = config.getGridValuesFirstCell().getColumn() + brokerIndex + broker.getSuppliersList().size();
		int row = config.getGridValuesFirstCell().getRow()-1;
		String colLetter = config.getGridValuesFirstCell().getExcelColumn(); // TODO: Letra do k? Ser� k isto do excel agr � msm necess�rio ou � melhor fazer a convers�o
		
		// Set the Minimum function
		for (int i = 0; i< config.getGroupsList().size(); i++) { 	
			Cell minimum = sheet.getRow(++row).createCell(column);
			CellStyle minimumCellStyle = getDefaultCellStyle(workbook);
			minimumCellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
			minimum.setCellStyle(minimumCellStyle);
			minimum.setCellFormula("MIN(" + colLetter + (row+1) +":INDIRECT(ADDRESS(ROW(),COLUMN()-1,4)))");
			minimum.setCellType(Cell.CELL_TYPE_FORMULA);
		}
		

		// Define the Minimum header with the name in the XML Configuration 
		Cell mininumHeader = sheet.getRow(config.getGridValuesFirstCell().getRow()-1).createCell(column);
		CellStyle minimumHeaderStyle = getDefaultCellStyle(workbook);
		minimumHeaderStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		minimumHeaderStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		mininumHeader.setCellStyle(minimumHeaderStyle);
		
		mininumHeader.setCellValue(broker.getMinimumColumnName());
		mininumHeader.setCellType(Cell.CELL_TYPE_STRING);
		
	}		

	private CellStyle getDefaultCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);

		style.setBorderBottom(CellStyle.BORDER_THIN);
		style.setBorderLeft(CellStyle.BORDER_THIN);
		style.setBorderRight(CellStyle.BORDER_THIN);
		style.setBorderTop(CellStyle.BORDER_THIN);
		
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		
	    Font font = workbook.createFont();
	    font.setFontHeightInPoints((short)8);
	    font.setFontName("Verdana");
		style.setFont(font);
		
		return style;
	}


	/**
	 * Set the fixed values in the report (Destination, Month, Days, Broker and Conversion Rate)
	 * 
	 * @param brokerName The broker
	 * @param destination The destination
	 * @param puDate The initial date
	 * @param doDate The end date
	 * @throws IllegalArgumentException When the conversion rate isn't valid
	 * @throws IOException When can't access to the service that provides the conversion rate
	 * @throws ConversionException When the ExchangeRateService gives a error 
	 */
	private void setFixedValues(Sheet sheet, RateShopReportConfiguration config, RateShopUKReportInfo reportInfo) throws CurrencyConversionException {
		try{
			// Set Destination
			sheet.getRow(config.getDestinationCell().getRow()).getCell(config.getDestinationCell().getColumn()).setCellValue(reportInfo.getDestination());
			
			// Set Days and Month
			Locale l = new Locale("pt", "PT");
			
			sheet.getRow(config.getMonthCell().getRow()).getCell(config.getMonthCell().getColumn()).setCellValue(reportInfo.getStartDate().getDisplayName(Calendar.MONTH, Calendar.LONG, l));
			
			sheet.getRow(config.getDayCell().getRow()).getCell(config.getDayCell().getColumn()).setCellValue(reportInfo.getStartDate().get(Calendar.DATE) + " a " + reportInfo.getEndDate().get(Calendar.DATE));
					
			// Set Rate
			double exchangeRate = ExchangeRateService.getExchangeRate("EUR", "GBP", 4);
			
			if(exchangeRate == 0)
				throw new IllegalArgumentException();
				
			Cell excRate = sheet.getRow(config.getRateCell().getRow()).getCell(config.getRateCell().getColumn());
			excRate.setCellValue(exchangeRate);
			excRate.setCellType(Cell.CELL_TYPE_NUMERIC);
		
		}catch (Exception e) {
			throw new CurrencyConversionException(e);
		}
		
	}

	
	/**
	 * Saves a XLSX Report with a specific name
	 * 
	 * @param destination The destination
	 * @param puDate The initial date
	 * @param doDate The end date
	 * @throws IOException When can't create the file
	 */
	private void saveFile(Workbook workbook, RateShopUKReportInfo reportInfo) throws IOException{
		FileOutputStream out;
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH_mm");
		Calendar cal = Calendar.getInstance();
		
		// TODO: Specify output file location
		out = new FileOutputStream(String.format("R %s RATE_SHOP_UK_%s_%s_A_%s.xlsx", dateFormat.format(cal.getTime()), reportInfo.getDestination(), getDate(reportInfo.getStartDate()), getDate(reportInfo.getEndDate())));
	
		XSSFFormulaEvaluator.evaluateAllFormulaCells((XSSFWorkbook)workbook);
		workbook.write(out);
		out.close();
	}

	/**
	 * Gets a String with the dates for the file name.
	 * 
	 * @param date 
	 * @return a String with the following style 07_Set_89
	 */
	private String getDate(Calendar date) {
		Locale l = new Locale("pt", "PT");
		
		int day = date.get(Calendar.DATE);
		String month = date.getDisplayName(Calendar.MONTH, Calendar.SHORT, l);
		int year = date.get(Calendar.YEAR);
				
		return String.format("%d_%s_%d", day, month, year);
	}
}