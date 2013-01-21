package challenge_it.racbit.model.configurations;

import java.util.HashMap;
import java.util.Map;

public class Location {
	
	/**
	 * Defines the row and column for the location
	 */
	private CrossReference _locationCell;
	
	/**
	 * Contains all of the groups and their row index
	 */
	private Map<String, Integer> _groups;
	
	/**
	 * Creates an instance of Location
	 */
	public Location(CrossReference locationCell){
		_locationCell = locationCell;
		_groups = new HashMap<String, Integer>();
	}
	
	/**
	 * Gets the row and column for the location
	 * 
	 * @return The row and column
	 */
	public CrossReference getLocationCell(){
		return _locationCell;
	}
	
	/**
	 * Gets the list of groups and their row index
	 * 
	 * @return A map of groups
	 */
	public Map<String, Integer> getGroupsMap(){
		return _groups;
	}
	
	/**
	 * Adds a group to the map
	 * 
	 * @param group The name of the group
	 * @param row The row index of the group
	 */
	public void addGroup(String group, int row){
		_groups.put(group, row);
	}
}
