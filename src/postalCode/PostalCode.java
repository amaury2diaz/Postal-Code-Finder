package postalCode;

import geodetic.Coordinate;

import java.util.Comparator;

/*	File Name: PostalCode Class
 *	Course Name: Data Structures CST 8130
 *	Lab	Section:CST 8130-301
 *	Student	Name: Amaury Diaz Diaz
 *	Date: Apr-14-2015
 */
/*
 * References:(Rex Woollard (2015) personal communication)
 */
/**
 * This class contains the postal codes with its respective city,
 * province and coordinates. 
 */
public class PostalCode {

	private String code;
	private String city;
	private String province;
	private Coordinate coordinate;
	
	/**
	 * This constructor takes a string as a parameter and then
	 * uses the split method to separate each string and initialize
	 * each variable.
	 */
	public PostalCode(String inputLine) {
		String[] fields = inputLine.split("\\||\\r\\n");
		code = fields[1];
		city = fields[2].intern();
		province = fields[3].intern();
		coordinate = new Coordinate(Double.parseDouble(fields[6]),Double.parseDouble(fields[7]));
	}//end of PostalCode(String)
	
    /**
     * This method returns a formatted string.
     */
	@Override
	public String toString() {
		return String.format("%s %s %s %s", code, city, province, coordinate.toString());
	}//end of toString()
  
    /**
     * This method returns a String that represents the postal code
     */
	public String getCode() {
		return code;
	}//end of getCode()
    
	/**
     * This method returns a String that represents the city
     */
	public String getCity() {
		return city;
	}//end of getCity()

	/**
     * This method returns a String that represents the province
     */
	public  String getProvince() {
		return province;
	}//end of getProvince()

	/**
     * This method returns a coordinate object
     */
	public Coordinate getCoordinate(){
		return coordinate;
	}
	
	


}//end of PostalCode class
