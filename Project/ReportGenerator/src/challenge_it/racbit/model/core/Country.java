package challenge_it.racbit.model.core;

/**
 * Enumeration that represents countries where we operate.
 * 
 * @author Paulo Pereira
 */
public enum Country {
	
	Portugal (Suffix.PT, "Lisboa", "Porto", "Faro", "Funchal"),
	Espanha (Suffix.ES, "Madrid", "Barcelona", "Málaga");

	private static enum Suffix { PT, ES }
	
	private final String[] _locations;
	private final Suffix _suffix;
	
	Country(Suffix suffix, String... locations) { _suffix = suffix; _locations = locations; }
	public String[] getLocations() { return _locations; }
	public String getSuffix() { return _suffix.toString(); }
}
