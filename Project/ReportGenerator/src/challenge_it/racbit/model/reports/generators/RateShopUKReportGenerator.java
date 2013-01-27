package challenge_it.racbit.model.reports.generators;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.ComparisonOperator;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetConditionalFormatting;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFConditionalFormattingRule;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import challenge_it.racbit.model.core.Country;
import challenge_it.racbit.model.core.IReportGenerator;
import challenge_it.racbit.model.core.Product;
import challenge_it.racbit.model.core.Product.InsurancePackage;
import challenge_it.racbit.model.core.exceptions.CurrencyConversionException;
import challenge_it.racbit.model.core.exceptions.ReportGenerationException;
import challenge_it.racbit.model.reports.configurations.Broker;
import challenge_it.racbit.model.reports.configurations.CrossReference;
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
	
	/**
	 * Class that represents the basic information for an UK Report
	 * 
	 *  @author C�tia Moreira e Jo�o Taborda
	 *
	 */
	private class RateShopUKReportInfo{
		
		/**
		 * The start date of the report
		 */
		private Calendar _startDate;
		
		/**
		 * The end date of the report
		 */
		private Calendar _endDate;
		
		/**
		 * The destination referred by the report
		 */
		private String _destination;
		
		public RateShopUKReportInfo(Calendar doDate, Calendar puDate, String destination){
			_startDate = puDate;
			_endDate = doDate;
			_destination = destination;
		}
		
		/**
		 * Gets the start date
		 * 
		 * @return the start date
		 */
		public Calendar getStartDate(){
			return _startDate;
		}
		
		/**
		 * Gets the end date
		 * 
		 * @return the end date
		 */
		public Calendar getEndDate(){
			return _endDate;
		}
		
		/**
		 * Gets the destination
		 * 
		 * @return the destination
		 */
		public String getDestination(){
			return _destination;
		}
	}
	
	/**
	 * Path to the xml configuration file
	 */
	private static final String XML_CONFIGURATION = "configuration/RateShop_Reports/UK_Reports/RateShopUKReportConfiguration.xml";
	
	/**
	 * Path to the xml schema file
	 */
	private static final String XML_SCHEMA = "configuration/RateShop_Reports/RateShopReportSchema.xsd";
	
	/**
	 * Path to the xml transformation file
	 */
	private static final String XML_TRANSFORMATION = "configuration/ReportTransformation.xsl";
	
	/**
	 * Path where the templates are saved
	 */
	private static final String PATH = "templates/";
	
	/**
	 * Generates the UK report
	 * 
	 * The execution order is as follows:
	 * 1. Read XML configuration file and apply the schema and the transformation using RateShopReportConfiguration class
	 * 2. Set the info used by RateShopUKReportInfo 
	 * 3. Combine the information obtained from XML file with the information from the iterator to fill the table with the values 
	 * 3.1. The styles for the table cells are applied at the same time
	 * 3.2. The column used for minimum values is created separately for each broker 
	 * 3.3. The line that has the suppliers names is also created for each broker
	 * 3.4. Because the table could have more than one broker, there is a pointer that should be set after the steps above
	 * 4. Fill the column with the groups names
	 * 5. Save the file
	 * 
	 * @param reportDate The date that should be on the file name
	 * @param country The country that should be on the file name
	 * @param results The iterator which have the values used to fill the table
	 * @throws ReportGenerationException, CurrencyConversionException
	 */
	@Override
	public void generate(Calendar reportDate, Country country, Iterable<Product> results) throws ReportGenerationException, CurrencyConversionException {
		try {
			
			RateShopReportConfiguration config = (RateShopReportConfiguration) new RateShopReportConfigurationReader().read(XML_CONFIGURATION, XML_SCHEMA, XML_TRANSFORMATION);
			
			Workbook workbook = new XSSFWorkbook(RateShopUKReportGenerator.class.getClassLoader().getResourceAsStream(PATH + config.getTemplateFilename()));
			Sheet sheet = workbook.getSheetAt(config.getSheetNumber());
			
			RateShopUKReportInfo reportInfo = completeBrokerInformation(config, results);
			
			setFixedValues(sheet, config, reportInfo);
						
			int brokerFirstSupplierIndex = config.getGridValuesFirstCell().getColumn();
				
			for(String brokerName : config.getBrokersList())
			{
				Broker broker = config.getBrokers().get(brokerName);
				
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
				
				if(broker.hasMinimum()){
					setMinimumColumn(workbook, sheet, config, broker, brokerFirstSupplierIndex);
					setMinimumColor(workbook, sheet, config, brokerFirstSupplierIndex, broker);
				}
				
				setTableCellsWithoutValue(workbook, sheet, config, broker, brokerFirstSupplierIndex);

				fillSuppliersHeader(workbook, sheet, config.getGridValuesFirstCell(), brokerFirstSupplierIndex, broker);
				
				brokerFirstSupplierIndex += broker.getSuppliersList().size() + (broker.hasMinimum()?1:0);
			}
			
			fillGroups(workbook, sheet, config);
			saveFile(workbook, reportInfo, reportDate, country);
		}
		catch (CurrencyConversionException e){
			throw e;
		}
		catch (Exception e) {
			throw new ReportGenerationException(e);
		}
	}
	
	/**
	 * Fills the groups' column with the names
	 * 
	 * @param workbook The representation of the file
	 * @param sheet The representation of the sheet
	 * @param config The object that holds the information read from XML file
	 */
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

	/**
	 * Fills the suppliers' line with the names
	 * 
	 * @param workbook The representation of the file
	 * @param sheet The representation of the sheet
	 * @param firstCellOfTheGrid Represents the first cell that has numeric values
	 * @param brokerIndex The index of the designated broker
	 * @param broker The broker that has the suppliers
	 */
	private void fillSuppliersHeader(Workbook workbook, Sheet sheet, CrossReference firstCellOfTheGrid, int brokerIndex, Broker broker) {

		int row = firstCellOfTheGrid.getRow()-1; //Refers to the line above the firstCellOfTheGrid
		
		Cell supplierCell = null;

		for (String  supplier : broker.getSuppliersList()) {
			CellStyle supplierCellStyle = getDefaultCellStyle(workbook);
			supplierCellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
			
			supplierCell = sheet.getRow(row).createCell(brokerIndex + broker.getSuppliersMap().get(supplier).getColumn());
			supplierCell.setCellStyle(supplierCellStyle);
			supplierCell.setCellValue(supplier);
			supplierCell.setCellType(Cell.CELL_TYPE_STRING);
		}
		
		if(!broker.hasMinimum()){
			CellStyle supplierCellStyle = getDefaultCellStyle(workbook);
			supplierCellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
			supplierCellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
			supplierCell.setCellStyle(supplierCellStyle);
		}

		CellStyle brokerCellStyle = getDefaultCellStyle(workbook);
		brokerCellStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		brokerCellStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		brokerCellStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
		brokerCellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
		
		Cell brokerName = sheet.getRow(row-1).createCell(brokerIndex);
		brokerName.setCellValue(broker.getName());
		brokerName.setCellStyle(brokerCellStyle);
		
		int lastColumn = brokerIndex + broker.getSuppliersList().size() + (broker.hasMinimum()?0:-1);
		
		for(int i = brokerIndex+1; i <= lastColumn;i++){
			sheet.getRow(row-1).createCell(i).setCellStyle(brokerCellStyle);
		}
		
		sheet.addMergedRegion(new CellRangeAddress(
	            row-1, //first row (0-based)
	            row-1, //last row  (0-based)
	            brokerIndex, //first column (0-based)
	            lastColumn  //last column  (0-based)
	    ));
	}

	/**
	 * Gets the complete information about a broker from the results
	 * This is used to know the concrete number of suppliers each broker
	 * 
	 * @param config The object that holds the information read from XML file
	 * @param results The iterator which have the values used to fill the table
	 * @return the information about the destination and the days to fill the report values
	 */
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
					b.addSupplier(product.getSupplier(), new CrossReference(config.getGridValuesFirstCell().getRow()-1, b.getSuppliersList().size()));
				}
				b.addProduct(product);
			}
			
		}
		
		return reportInfo;
	}

	/**
	 * Gets the complete information about a broker from the results
	 * This is used to know the concrete number of suppliers each broker
	 * 
	 * @param workbook The representation of the file
	 * @param sheet The representation of the sheet
	 * @param config The object that holds the information read from XML file
	 * @param brokerIndex The index of the designated broker
	 * @param broker The broker that has the suppliers
	 */
	private void setMinimumColor(Workbook workbook, Sheet sheet, RateShopReportConfiguration config, int brokerIndex, Broker broker) {
		int lastColumn = brokerIndex + broker.getSuppliersList().size();
		int firstRow = config.getGridValuesFirstCell().getRow();
		
		for(int i = firstRow; i < firstRow+config.getGroupsList().size(); i++){
			
			CellValue cellMinimumValue = workbook.getCreationHelper().createFormulaEvaluator().evaluate(sheet.getRow(i).getCell(lastColumn));
			
			SheetConditionalFormatting cf = sheet.getSheetConditionalFormatting();
			XSSFConditionalFormattingRule rule = (XSSFConditionalFormattingRule) cf.createConditionalFormattingRule(
			     ComparisonOperator.EQUAL, 
			     cellMinimumValue.getNumberValue() + "",
			     null
			);

			 // Create pattern with red background
			rule.createFontFormatting().setFontColorIndex(HSSFColor.RED.index);

			 // Define a region containing first column
			CellRangeAddress[] cra = {new CellRangeAddress(i, i, brokerIndex, lastColumn-1) };

			 // Apply Conditional Formatting rule defined above to the regions  
			cf.addConditionalFormatting(cra, rule);
		}		
	}

	/**
	 * Searches the table for cells that doesn't have any value and creates them with a specific style
	 * 
	 * @param workbook The representation of the file
	 * @param sheet The representation of the sheet
	 * @param config The object that holds the information read from XML file
	 * @param b The broker which table will be read
	 * @param firstColumn The initial column to start read
	 */
	private void setTableCellsWithoutValue(Workbook workbook, Sheet sheet, RateShopReportConfiguration config, Broker b, int firstColumn) {
		
		int lastColumn = firstColumn + b.getSuppliersList().size();
			
		for(int i = config.getGridValuesFirstCell().getRow(); i < config.getGridValuesFirstCell().getRow() + config.getGroupsList().size(); i++)
		{
			Cell newCell = null;
			
			for(int j = firstColumn; j < lastColumn ; j++)
			{
				if(sheet.getRow(i).getCell(j) == null)
				{
					newCell = sheet.getRow(i).createCell(j);
					CellStyle style = getDefaultCellStyle(workbook);
					style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
					style.setFillPattern(CellStyle.SOLID_FOREGROUND);
					newCell.setCellStyle(style);
				}
			}
			
			if(!b.hasMinimum()){
				CellStyle style = getDefaultCellStyle(workbook);
				style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
				style.setFillPattern(CellStyle.SOLID_FOREGROUND);
				style.setBorderRight(CellStyle.BORDER_MEDIUM);
				newCell.setCellStyle(style);
			}
		}
		
	}

	/**
	 * Used to create a minimum column for brokers that requires it
	 * 
	 * @param workbook The representation of the file
	 * @param sheet The representation of the sheet
	 * @param config The object that holds the information read from XML file
	 * @param broker The broker that has the suppliers
	 * @param brokerIndex The index of the designated broker
	 */
	private void setMinimumColumn(Workbook workbook, Sheet sheet, RateShopReportConfiguration config, Broker broker, int brokerIndex) {
		int column = brokerIndex + broker.getSuppliersList().size();
		int row = config.getGridValuesFirstCell().getRow()-1;
		String columnLetter = CellReference.convertNumToColString(brokerIndex);
		
		// Set the Minimum function
		for (int i = 0; i< config.getGroupsList().size(); i++) { 	
			Cell minimum = sheet.getRow(++row).createCell(column);
			CellStyle minimumCellStyle = getDefaultCellStyle(workbook);
			minimumCellStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
			minimum.setCellStyle(minimumCellStyle);
			minimum.setCellFormula("MIN(" + columnLetter + (row+1) +":INDIRECT(ADDRESS(ROW(),COLUMN()-1,4)))");
			minimum.setCellType(Cell.CELL_TYPE_FORMULA);
		}
		
		// Define the Minimum header with the name in the XML Configuration 
		Cell mininumHeader = sheet.getRow(config.getGridValuesFirstCell().getRow()-1).createCell(column);
		CellStyle minimumHeaderStyle = getDefaultCellStyle(workbook);
		minimumHeaderStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		minimumHeaderStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		minimumHeaderStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
		mininumHeader.setCellStyle(minimumHeaderStyle);
		
		mininumHeader.setCellValue(broker.getMinimumColumnName());
		mininumHeader.setCellType(Cell.CELL_TYPE_STRING);
		
	}		

	/**
	 * Gets the default style used for the cells that contains values
	 * 
	 * @param workbook The representation of the file
	 * @return the default cell style
	 */
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
	private void saveFile(Workbook workbook, RateShopUKReportInfo reportInfo, Calendar reportDate, Country country) throws IOException{
		FileOutputStream out;
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH_mm");
		
		// TODO: Specify output file location and country???
		out = new FileOutputStream(String.format("R %s RATE_SHOP_UK_%s_%s_A_%s.xlsx", dateFormat.format(reportDate.getTime()), reportInfo.getDestination(), getDate(reportInfo.getStartDate()), getDate(reportInfo.getEndDate())));
	
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