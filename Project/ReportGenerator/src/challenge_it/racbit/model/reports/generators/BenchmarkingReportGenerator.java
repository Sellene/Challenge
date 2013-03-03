package challenge_it.racbit.model.reports.generators;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
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
import challenge_it.racbit.model.reports.generators.utils.BenchmarkingReportInfo;
import challenge_it.racbit.model.reports.generators.utils.BenchmarkingReportInfo.BenchmarkingDay;
import challenge_it.racbit.model.reports.generators.utils.BenchmarkingReportInfo.BenchmarkingGroup;
import challenge_it.racbit.model.reports.generators.utils.BenchmarkingReportInfo.BenchmarkingLocation;
import challenge_it.racbit.model.reports.generators.utils.CellStyles;
import challenge_it.racbit.model.reports.generators.utils.CrossReference;


public class BenchmarkingReportGenerator implements IReportGenerator {
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
		
		FileOutputStream out;
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH_mm");
		
		// TODO: Specify output file config.getLocationCell() 
		// Do we net to put the country in the name?
		try {
			out = new FileOutputStream("Teste" + Calendar.getInstance().get(Calendar.MINUTE) + ".xlsx");
			XSSFFormulaEvaluator.evaluateAllFormulaCells((XSSFWorkbook)workbook);
			workbook.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
					
					Cell dayCell = row.createCell(config.getLocationCell().getColumn());
					dayCell.setCellValue(day.getNumberOfDays());
					dayCell.setCellStyle(CellStyles.setThinBorders(CellStyles.setBold(CellStyles.getDefaultCellStyle(workbook), CellStyles.SMALL_TEXT, workbook)));
					
					Cell groupCell = row.createCell(config.getGroupCell().getColumn());
					groupCell.setCellValue(group.getGroupName());
					groupCell.setCellStyle(CellStyles.setThinBorders(CellStyles.setBold(CellStyles.getDefaultCellStyle(workbook), CellStyles.SMALL_TEXT, workbook)));
					
					Cell locationCell = row.createCell(config.getLocationCell().getColumn());
					locationCell.setCellValue(location.getLocationName());
					locationCell.setCellStyle(CellStyles.setBackground(CellStyles.setColor(CellStyles.getDefaultCellStyle(workbook), HSSFColor.WHITE.index, CellStyles.MEDIUM_TEXT, workbook), color));
					
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
			}
			offset++;	
			//row.createCell(offset);
			
			sheet.addMergedRegion(new CellRangeAddress(
					config.getLocationCell().getRow() + groupOffset, //first row (0-based)
					config.getLocationCell().getRow() + (offset-groupOffset-1) + groupOffset, //last row  (0-based)
					config.getLocationCell().getColumn(), //first column (0-based)
					config.getLocationCell().getColumn()  //last column  (0-based)
				));

			groupOffset = offset+1; //update group offset
		}
		return offset;
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
}
