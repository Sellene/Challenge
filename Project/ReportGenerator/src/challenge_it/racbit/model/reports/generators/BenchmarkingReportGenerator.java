package challenge_it.racbit.model.reports.generators;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import challenge_it.racbit.model.core.Country;
import challenge_it.racbit.model.core.IReportGenerator;
import challenge_it.racbit.model.core.Product;
import challenge_it.racbit.model.core.Product.SupplierType;
import challenge_it.racbit.model.core.exceptions.CurrencyConversionException;
import challenge_it.racbit.model.core.exceptions.ReportGenerationException;
import challenge_it.racbit.model.reports.configurations.BenchmarkingReportConfiguration;
import challenge_it.racbit.model.reports.configurations.BenchmarkingReportConfigurationReader;
import challenge_it.racbit.model.reports.exchangeRate.ExchangeRateService;
import challenge_it.racbit.model.reports.generators.utils.BenchmarkingReportInfo;
import challenge_it.racbit.model.reports.generators.utils.BenchmarkingReportInfo.BenchmarkingDay;
import challenge_it.racbit.model.reports.generators.utils.BenchmarkingReportInfo.BenchmarkingGroup;
import challenge_it.racbit.model.reports.generators.utils.BenchmarkingReportInfo.BenchmarkingLocation;
import challenge_it.racbit.model.reports.generators.utils.CellStyles;
import challenge_it.racbit.model.reports.generators.utils.CrossReference;
import challenge_it.racbit.model.reports.generators.utils.IPredicate;


public class BenchmarkingReportGenerator implements IReportGenerator {
	
	private String TITLE = "BENCHMARKING TO";
	/**
	 * Path to the xml configuration file
	 */
	private static final String XML_CONFIGURATION = "configuration/Benchmarking_Reports/BenchmarkingReportConfiguration.xml";
	
	/**
	 * Path to the xml schema file
	 */
	private static final String XML_SCHEMA = "configuration/Benchmarking_Reports/BenchmarkingReportSchema.xsd";
	
	/**
	 * Path to the xml transformation file
	 */
	private static final String XML_TRANSFORMATION = "configuration/ReportTransformation.xsl";
	
	@Override
	public void generate(Calendar reportDate, Country country, Iterable<Product> results) throws ReportGenerationException,
			CurrencyConversionException {
		
		HashMap<String, CrossReference> brokers = new HashMap<String, CrossReference>();
		
		BenchmarkingReportConfiguration config = (BenchmarkingReportConfiguration) new BenchmarkingReportConfigurationReader().read(XML_CONFIGURATION, XML_SCHEMA, XML_TRANSFORMATION);
		
		BenchmarkingReportInfo info = processInformation(config, results, brokers);
		
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		int offset = -1;
		
		offset = fill(workbook, sheet, config, brokers, info.getRegulars(), HSSFColor.LIME.index, offset);
		
		fill(workbook, sheet, config, brokers, info.getLowCosts(), HSSFColor.PINK.index, offset+1);
		
		
		setFixedValues(workbook, sheet, config, reportDate);
		
		try {
			saveFile(workbook, reportDate, country);
		} catch (IOException e) {
			throw new ReportGenerationException(e);
		}
	}

	private int fill(Workbook workbook, Sheet sheet, BenchmarkingReportConfiguration config, HashMap<String, CrossReference> brokers, Map<String, BenchmarkingLocation> locations, short color, int offset) {
		
		Row row = null;
		int groupOffset = offset+1;
		
		for (BenchmarkingLocation location : locations.values()) {
			for(BenchmarkingGroup group : location.getGroups().values()){		
				for(BenchmarkingDay day : group.getDays().values()){
					offset++;
					
					row = sheet.getRow(config.getGridValuesFirstCell().getRow() + offset);
					
					if(row == null)
						row = sheet.createRow(config.getGridValuesFirstCell().getRow() + offset);
					
					Cell dayCell = row.createCell(config.getNumberOfDaysCell().getColumn());
					dayCell.setCellValue(day.getNumberOfDays());
					dayCell.setCellStyle(CellStyles.setThinBorders(CellStyles.setBold(CellStyles.getDefaultCellStyle(workbook), CellStyles.TEXT_SIZE_SMALL, workbook)));
					
					Cell groupCell = row.createCell(config.getGroupCell().getColumn());
					groupCell.setCellValue(group.getGroupName());
					groupCell.setCellStyle(CellStyles.setThinBorders(CellStyles.setBold(CellStyles.getDefaultCellStyle(workbook), CellStyles.TEXT_SIZE_SMALL, workbook)));
					
					Cell locationCell = row.createCell(config.getLocationCell().getColumn());
					locationCell.setCellValue(location.getLocationName());
					locationCell.setCellStyle(CellStyles.setBackground(CellStyles.setColor(CellStyles.getDefaultCellStyle(workbook), HSSFColor.WHITE.index, CellStyles.TEXT_SIZE_MEDIUM, workbook), color));
					sheet.autoSizeColumn(config.getLocationCell().getColumn(), true);
					
					for (Product product : day.getProducts().values()) {
						CrossReference crossReference = brokers.get(product.getBroker());
												
						Cell productPrice = row.createCell(crossReference.getColumn());
						productPrice.setCellValue(product.getPrice());
						productPrice.setCellType(Cell.CELL_TYPE_NUMERIC);
						
						Cell productSupplier = row.createCell(crossReference.getColumn()+1);
						productSupplier.setCellValue(product.getSupplier());
						
						productPrice.setCellStyle(CellStyles.setBackground(CellStyles.setThinBorders(CellStyles.getDefaultCellStyle(workbook)), HSSFColor.LIGHT_GREEN.index));
						productSupplier.setCellStyle(CellStyles.setBackground(CellStyles.setThinBorders(CellStyles.getDefaultCellStyle(workbook)), HSSFColor.LIGHT_GREEN.index));
						
					}
				}

				if(group.getDays().size() > 1){
					sheet.addMergedRegion(new CellRangeAddress(
						row.getRowNum()-group.getDays().size()+1, //first row (0-based)
						row.getRowNum(), //last row  (0-based)
						config.getGroupCell().getColumn(), //first column (0-based)
						config.getGroupCell().getColumn()  //last column  (0-based)
					));
				}
				
			}
			offset++;	
			
			//These CrossReference objects are used to indicate the corners of the table
			CrossReference locationCellNameIdxFirst = new CrossReference((config.getLocationCell().getRow()+1) + groupOffset, config.getLocationCell().getColumn());
			CrossReference lastCellValueIdxLast = new CrossReference((config.getLocationCell().getRow()+1) + (offset-groupOffset-1) + groupOffset, (brokers.size()*2)+4); //TO CHANGE
			
			//merge location name cell
			sheet.addMergedRegion(new CellRangeAddress(
					locationCellNameIdxFirst.getRow(), //first row (0-based)
					lastCellValueIdxLast.getRow(), //last row  (0-based)
					locationCellNameIdxFirst.getColumn(), //first column (0-based)
					config.getLocationCell().getColumn()  //last column  (0-based)
				));

			groupOffset = offset+1; //update group offset	
			
			setTableBorders(workbook, sheet, locationCellNameIdxFirst, lastCellValueIdxLast);
			
			setCurrencyCell(workbook, sheet, locationCellNameIdxFirst, lastCellValueIdxLast, "£", HSSFColor.YELLOW.index);
			
			
			//merge currency cell
			sheet.addMergedRegion(new CellRangeAddress(
					locationCellNameIdxFirst.getRow(), //first row (0-based)
					lastCellValueIdxLast.getRow(), //last row  (0-based)
					locationCellNameIdxFirst.getColumn(), //first column (0-based)
					config.getLocationCell().getColumn()  //last column  (0-based)
				));
			
		}
		return offset;
	}

	private void setCurrencyCell(Workbook workbook, Sheet sheet,
			CrossReference locationCellNameIdxFirst,
			CrossReference lastCellValueIdxLast, String symbol, short color) {
		
		Cell currencySymbol = sheet.getRow(locationCellNameIdxFirst.getRow()).getCell(locationCellNameIdxFirst.getColumn()-1);
		currencySymbol.setCellValue(symbol);
		currencySymbol.setCellStyle(CellStyles.setBackground(CellStyles.setBold(CellStyles.getDefaultCellStyle(workbook), CellStyles.TEXT_SIZE_MEDIUM, workbook), color));
				
		sheet.addMergedRegion(new CellRangeAddress(
				locationCellNameIdxFirst.getRow(), //first row (0-based)
				lastCellValueIdxLast.getRow(), //last row  (0-based)
				locationCellNameIdxFirst.getColumn()-1, //first column (0-based)
				locationCellNameIdxFirst.getColumn()-1  //last column  (0-based)
			));
	}

	private void setTableBorders(Workbook workbook, Sheet sheet, CrossReference locationCellNameIdxFirst,
			CrossReference lastCellValueIdxLast) {

		int firstRow = locationCellNameIdxFirst.getRow();
		int lastRow = lastCellValueIdxLast.getRow();
		
		int firstColumn = locationCellNameIdxFirst.getColumn();
		int lastColumn = lastCellValueIdxLast.getColumn();
		
		for(int i = firstColumn-1; i <= lastColumn; i++){
			
			applyStylesToCells(workbook, sheet, firstRow, i, new IPredicate() {
				
				@Override
				public void execute(Cell cell) {
					cell.setCellStyle(CellStyles.setMediumTopBorder(cell.getCellStyle()));
				}
			});
			
			applyStylesToCells(workbook, sheet, lastRow, i, new IPredicate() {
				
				@Override
				public void execute(Cell cell) {
					cell.setCellStyle(CellStyles.setMediumBottomBorder(cell.getCellStyle()));
				}
			});
		}
		
		for(int i = firstRow; i <= lastRow; i++){
			applyStylesToCells(workbook, sheet, i, firstColumn, new IPredicate() {
				
				@Override
				public void execute(Cell cell) {
					cell.setCellStyle(CellStyles.setMediumLeftBorder(cell.getCellStyle()));
				}
			});
			
			applyStylesToCells(workbook, sheet, i, lastColumn, new IPredicate() {
				
				@Override
				public void execute(Cell cell) {
					cell.setCellStyle(CellStyles.setMediumRightBorder(cell.getCellStyle()));
				}
			});
		}
		
	}

	private void applyStylesToCells(Workbook workbook, Sheet sheet, int row, int column,
			IPredicate predicate) {
		
		Cell toPaint = sheet.getRow(row).getCell(column);
		
		if(toPaint == null){
			toPaint = sheet.getRow(row).createCell(column);
			toPaint.setCellStyle(CellStyles.getDefaultCellStyle(workbook));
		}
		
		predicate.execute(toPaint);
	}

	private BenchmarkingReportInfo processInformation(BenchmarkingReportConfiguration config, Iterable<Product> results, HashMap<String, CrossReference> brokers) {
		BenchmarkingReportInfo info = new BenchmarkingReportInfo();
		int column = config.getGridValuesFirstCell().getColumn();
		
		for (Product product : results) {
			BenchmarkingLocation location;
			
			if(product.getSupplierType() == SupplierType.LOW_COST){
				location = info.checkLowCostLocation(product.getLocation());
				
				if(location == null){
					location = info.new BenchmarkingLocation(product.getLocation());
					info.addLowCost(product.getLocation(), location);
				}
			}
			else{
				location = info.checkRegularLocation(product.getLocation());
				
				if(location == null){
					location = info.new BenchmarkingLocation(product.getLocation());
					info.addRegular(product.getLocation(), location);
				}
			}
			
			saveProduct(info, location, product);
		
			if(brokers.get(product.getBroker()) == null){
				brokers.put(product.getBroker(), new CrossReference(6, column));
				column += 2;
			}
			
		}
		
		return info;
	}

	private void saveProduct(BenchmarkingReportInfo info, BenchmarkingLocation location, Product product) {
		BenchmarkingGroup group = location.checkGroup(product.getGroup());
		if(group == null){
			group = info.new BenchmarkingGroup(product.getGroup());
			location.addGroup(product.getGroup(), group);
		}
		
		BenchmarkingDay day = group.checkDay(product.getNumberOfDays());
		if(day == null){
			day = info.new BenchmarkingDay(product.getNumberOfDays());
			group.addDay(product.getNumberOfDays(), day);
		}
		
		day.addProduct(product);
	}
	
	private void setFixedValues(Workbook workbook, Sheet sheet, BenchmarkingReportConfiguration config, Calendar reportDate) throws CurrencyConversionException {
		setTitle(workbook, sheet, config);
		setConsultationDate(workbook, sheet, config, reportDate);
		setHour(workbook, sheet, config, reportDate);
		setPickUpDate(workbook, sheet, config, reportDate);
		setRate(workbook, sheet, config);
	}
	
	private void setTitle(Workbook workbook, Sheet sheet, BenchmarkingReportConfiguration config) {
		Row title = sheet.getRow(config.getTitleCell().getRow());
		
		if(title == null)
			title = sheet.createRow(config.getTitleCell().getRow());
		
		
		sheet.addMergedRegion(new CellRangeAddress(
				title.getRowNum(), //first row (0-based)
				title.getRowNum(), //last row  (0-based)
	            config.getTitleCell().getColumn(), //first column (0-based)
	            config.getTitleCell().getColumn()+3  //last column  (0-based)
	    ));	
		
		Cell cell = title.createCell(config.getTitleCell().getColumn());
		cell.setCellValue(TITLE);
		cell.setCellStyle(CellStyles.setAligment(CellStyles.setBoldAndColor(CellStyles.getDefaultCellStyle(workbook), HSSFColor.RED.index, CellStyles.TEXT_SIZE_MEDIUM, workbook), CellStyles.ALIGN_LEFT));		
		
	}

	private void setConsultationDate(Workbook workbook, Sheet sheet, BenchmarkingReportConfiguration config, Calendar reportDate) {
		Row consultationDate = sheet.getRow(config.getConsultationDateCell().getRow());
		
		if(consultationDate == null)
			consultationDate = sheet.createRow(config.getConsultationDateCell().getRow());
		
		
		sheet.addMergedRegion(new CellRangeAddress(
				consultationDate.getRowNum(), //first row (0-based)
				consultationDate.getRowNum(), //last row  (0-based)
	            config.getConsultationDateCell().getColumn(), //first column (0-based)
	            config.getConsultationDateCell().getColumn()+3  //last column  (0-based)
	    ));	
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
		
		Cell cell = consultationDate.createCell(config.getConsultationDateCell().getColumn());
		cell.setCellValue(String.format("DATA DE CONSULTA: %s", dateFormat.format(reportDate.getTime())));
		cell.setCellStyle(CellStyles.setAligment(CellStyles.setBold(CellStyles.getDefaultCellStyle(workbook), CellStyles.TEXT_SIZE_MEDIUM, workbook), CellStyles.ALIGN_LEFT));		
		
	}

	private void setHour(Workbook workbook, Sheet sheet, BenchmarkingReportConfiguration config, Calendar reportDate) {
		Row hour = sheet.getRow(config.getHourCell().getRow());
		
		if(hour == null)
			hour = sheet.createRow(config.getHourCell().getRow());
		
		
		sheet.addMergedRegion(new CellRangeAddress(
				hour.getRowNum(), //first row (0-based)
				hour.getRowNum(), //last row  (0-based)
	            config.getHourCell().getColumn(), //first column (0-based)
	            config.getHourCell().getColumn()+3  //last column  (0-based)
	    ));	
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm");
		
		Cell cell = hour.createCell(config.getHourCell().getColumn());
		cell.setCellValue(String.format("HORA: %s", dateFormat.format(reportDate.getTime())));
		cell.setCellStyle(CellStyles.setAligment(CellStyles.setBold(CellStyles.getDefaultCellStyle(workbook), CellStyles.TEXT_SIZE_MEDIUM, workbook), CellStyles.ALIGN_LEFT));		
		
	}

	private void setPickUpDate(Workbook workbook, Sheet sheet, BenchmarkingReportConfiguration config, Calendar reportDate) {
		Row pickUpDate = sheet.getRow(config.getPickUpDateCell().getRow());
		
		if(pickUpDate == null)
			pickUpDate = sheet.createRow(config.getPickUpDateCell().getRow());
		
		
		sheet.addMergedRegion(new CellRangeAddress(
				pickUpDate.getRowNum(), //first row (0-based)
				pickUpDate.getRowNum(), //last row  (0-based)
	            config.getPickUpDateCell().getColumn(), //first column (0-based)
	            config.getPickUpDateCell().getColumn()+3  //last column  (0-based)
	    ));	
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
		
		Cell cell = pickUpDate.createCell(config.getPickUpDateCell().getColumn());
		cell.setCellValue(String.format("DATA DE PICK UP: %s", dateFormat.format(reportDate.getTime())));
		cell.setCellStyle(CellStyles.setBackground(CellStyles.setAligment(CellStyles.setBold(CellStyles.getDefaultCellStyle(workbook), CellStyles.TEXT_SIZE_MEDIUM, workbook), CellStyles.ALIGN_LEFT), HSSFColor.YELLOW.index));		
		
	}

	/**
	 * Set the current conversion rate
	 * 
	 * @param workbook The workbook for the Excel File
	 * @param sheet The sheet that is used
	 * @param config The RateShopReportConfiguration instance
	 * @throws CurrencyConversionException
	 */
	private void setRate(Workbook workbook, Sheet sheet, BenchmarkingReportConfiguration config) throws CurrencyConversionException {
		
		double exchangeRate = ExchangeRateService.getExchangeRate("EUR", "GBP", 4);
		
		if(exchangeRate == 0)
			throw new IllegalArgumentException();
		
		Row rate = sheet.getRow(config.getRateCell().getRow());
		
		if(rate == null)
			rate = sheet.createRow(config.getRateCell().getRow());
		
		
		sheet.addMergedRegion(new CellRangeAddress(
				rate.getRowNum(), //first row (0-based)
				rate.getRowNum(), //last row  (0-based)
				config.getRateCell().getColumn(), //first column (0-based)
				config.getRateCell().getColumn() + 1  //last column  (0-based)
	    ));
		
		Cell excRate = rate.createCell(config.getRateCell().getColumn());
		excRate.setCellValue(exchangeRate);
		excRate.setCellType(Cell.CELL_TYPE_NUMERIC);
		excRate.setCellStyle(CellStyles.setBackground(CellStyles.setBold(CellStyles.setDataFormat(workbook, CellStyles.getDefaultCellStyle(workbook), CellStyles.DECIMAL_POINT_FOUR), CellStyles.TEXT_SIZE_SMALL, workbook), IndexedColors.LIME.index));	
	}
	
	/**
	 * Saves a XLSX Report with a specific name
	 * 
	 * @param destination The destination
	 * @param puDate The initial date
	 * @param doDate The end date
	 * @throws IOException When can't create the file
	 */
	private void saveFile(Workbook workbook, Calendar reportDate, Country country) throws IOException{
		FileOutputStream out;
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH_mm");
		
		// TODO: Specify output file location 
		// Do we net to put the country in the name?
		out = new FileOutputStream(String.format("R %s Benchmarking_UK_PT.xlsx", dateFormat.format(reportDate.getTime())));
	
		XSSFFormulaEvaluator.evaluateAllFormulaCells((XSSFWorkbook)workbook);
		workbook.write(out);
		out.close();
	}

	

}
