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
import challenge_it.racbit.model.reports.generators.utils.BenchmarkingReportInfo;
import challenge_it.racbit.model.reports.generators.utils.BenchmarkingReportInfo.BenchmarkingDay;
import challenge_it.racbit.model.reports.generators.utils.BenchmarkingReportInfo.BenchmarkingGroup;
import challenge_it.racbit.model.reports.generators.utils.BenchmarkingReportInfo.BenchmarkingLocation;
import challenge_it.racbit.model.reports.generators.utils.CrossReference;


public class BenchmarkingReportGenerator implements IReportGenerator {
	private final CrossReference FIRST_CELL = new CrossReference(6, 5);
	private final CrossReference LOCATION = new CrossReference(6, 1);
	private final CrossReference GROUPS = new CrossReference(6, 2);
	private final CrossReference DAYS = new CrossReference(6, 3);
	
	@Override
	public void generate(Calendar reportDate, Country country, Iterable<Product> results) throws ReportGenerationException,
			CurrencyConversionException {
		
		HashMap<String, CrossReference> brokers = new HashMap<String, CrossReference>();
		
		//BenchmarkingReportConfiguration config = (BenchmarkingReportConfiguration) new BenchmarkingReportConfigurationReader().read(XML_CONFIGURATION, XML_SCHEMA, XML_TRANSFORMATION);
		
		BenchmarkingReportInfo info = processInformation(results, brokers);
		
		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		int offset = -1;
		
		offset = fill(workbook, sheet, brokers, info.getRegulars(), HSSFColor.LIME.index, offset);
		
		fill(workbook, sheet, brokers, info.getLowCosts(), HSSFColor.PINK.index, offset+1);
		
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

	private int fill(Workbook workbook, Sheet sheet, HashMap<String, CrossReference> brokers, Map<String, BenchmarkingLocation> locations, int color, int offset) {
				
		for (BenchmarkingLocation location : locations.values()) {
			for(BenchmarkingGroup group : location.getGroups().values()){
				for(BenchmarkingDay day : group.getDays().values()){
					offset++;
					
					Row row = sheet.getRow(FIRST_CELL.getRow() + offset);
					
					if(row == null)
						row = sheet.createRow(FIRST_CELL.getRow() + offset);
					
					Cell dayCell = row.createCell(DAYS.getColumn());
					dayCell.setCellValue(day.getNumberOfDays());
					
					Cell groupCell = row.createCell(GROUPS.getColumn());
					groupCell.setCellValue(group.getGroupName());
					
					Cell locationCell = row.createCell(LOCATION.getColumn());
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

	private BenchmarkingReportInfo processInformation(Iterable<Product> results, HashMap<String, CrossReference> brokers) {
		BenchmarkingReportInfo info = new BenchmarkingReportInfo();
		int column = 5;
		
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
		BenchmarkingGroup group = location.checkGroup(product.getGroup().name());
		if(group == null){
			group = info.new BenchmarkingGroup(product.getGroup().name());
			location.addGroup(product.getGroup().name(), group);
		}
		
		BenchmarkingDay day = group.checkDay(product.getNumberOfDays());
		if(day == null){
			day = info.new BenchmarkingDay(product.getNumberOfDays());
			group.addDay(product.getNumberOfDays(), day);
		}
		
		day.addProduct(product);
	}
}
