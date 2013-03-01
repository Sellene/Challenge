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
		
		// TODO: Specify output file location 
		// Do we net to put the country in the name?
		try {
			out = new FileOutputStream("Teste" + Calendar.getInstance().get(Calendar.MINUTE));
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

	private int fill(Workbook workbook, Sheet sheet, BenchmarkingReportConfiguration config, HashMap<String, CrossReference> brokers, Map<String, BenchmarkingLocation> locations, int color, int offset) {
				
		for (BenchmarkingLocation location : locations.values()) {
			for(BenchmarkingGroup group : location.getGroups().values()){
				for(BenchmarkingDay day : group.getDays().values()){
					offset++;
					
					Row row = sheet.getRow(config.getGridValuesFirstCell().getRow() + offset);
					
					if(row == null)
						row = sheet.createRow(config.getGridValuesFirstCell().getRow() + offset);
					
					Cell dayCell = row.createCell(config.getNumberOfDaysCell().getColumn());
					dayCell.setCellValue(day.getNumberOfDays());
					
					Cell groupCell = row.createCell(config.getGroupCell().getColumn());
					groupCell.setCellValue(group.getGroupName());
					
					Cell locationCell = row.createCell(config.getLocationCell().getColumn());
					locationCell.setCellValue(location.getLocationName());
					
					for (Product product : day.getProducts().values()) {
						CrossReference crossReference = brokers.get(product.getBroker());
												
						Cell productPrice = row.createCell(crossReference.getColumn());
						productPrice.setCellValue(product.getPrice());
						productPrice.setCellType(Cell.CELL_TYPE_NUMERIC);
						
						Cell productSupplier = row.createCell(crossReference.getColumn()+1);
						productSupplier.setCellValue(product.getSupplier());
					}
				}				
			}
			offset++;
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
