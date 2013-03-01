package challenge_it.racbit.model.reports.generators.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import challenge_it.racbit.model.core.Product;
import challenge_it.racbit.model.core.Product.Group;

public class BenchmarkingReportInfo{
	
	public class BenchmarkingLocation {

		private String _name;
		
		private Map<Group, BenchmarkingGroup> _groups;
		
		
		public BenchmarkingLocation (String name){
			_name = name;
			_groups = new TreeMap<Group, BenchmarkingGroup>();
		}
		
		public String getLocationName(){
			return _name;
		}
		
		public Map<Group, BenchmarkingGroup> getGroups(){
			return _groups;
		}
		
		public void addGroup(Group name, BenchmarkingGroup group){
			_groups.put(name, group);
		}
		
		public BenchmarkingGroup checkGroup(Group group){
			return _groups.get(group);
		}
	}
	
	public class BenchmarkingDay {

		private int _numberOfDays;
		
		private Map<String, Product> _products;
		
		public BenchmarkingDay(int numberOfDays) {
			_numberOfDays = numberOfDays;
			_products = new HashMap<String, Product>();
		}
		
		public void addProduct(Product product){
			_products.put(product.getBroker(), product);
		}
		
		public Map<String, Product> getProducts(){
			return _products;
		}
		
		public int getNumberOfDays(){
			return _numberOfDays;
		}
	}

	public class BenchmarkingGroup {

		private Group _group;
		
		private Map<Integer, BenchmarkingDay> _days;
		
		public BenchmarkingGroup(Group group) {
			_group = group;
			_days = new HashMap<Integer, BenchmarkingDay>();
		}

		public BenchmarkingDay checkDay(int numberOfDays) {
			return _days.get(numberOfDays);
		}

		public void addDay(int numberOfDays, BenchmarkingDay day) {
			_days.put(numberOfDays, day);	
		}
		
		public Map<Integer, BenchmarkingDay> getDays(){
			return _days;
		}
		
		public String getGroupName(){
			return _group.name();
		}
	}
	
	private Map<String, BenchmarkingLocation> _regular;
	
	private Map<String, BenchmarkingLocation> _lowCost;
	
	public BenchmarkingReportInfo(){
		_regular = new HashMap<String, BenchmarkingLocation> ();
		_lowCost = new HashMap<String, BenchmarkingLocation> ();
	}
	
	public Map<String, BenchmarkingLocation> getRegulars(){
		return _regular;
	}
	
	public Map<String, BenchmarkingLocation> getLowCosts(){
		return _lowCost;
	}
	
	public void addRegular(String name, BenchmarkingLocation location){
		_regular.put(name, location);
	}
	
	public void addLowCost(String name, BenchmarkingLocation location){
		_lowCost.put(name, location);
	}
	
	public BenchmarkingLocation checkRegularLocation(String name){
		return _regular.get(name);
	}
	
	public BenchmarkingLocation checkLowCostLocation(String name){
		return _lowCost.get(name);
	}
}