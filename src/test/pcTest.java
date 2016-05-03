package test;

import geodetic.Coordinate;
import postalCode.PostalCodeIndex;

/*	File Name: PostalCodeIndexTest Class
 *	Course Name: Data Structures CST 8130
 *	Lab	Section:CST 8130-301
 *	Student	Name: Amaury Diaz Diaz
 *	Date: Jan-28-2015
 */
/*
 * References:(Rex Woollard (2015) personal communication)
 */
/*
 * This class contains a method main. ?
 */
public class pcTest {
	public static void main(String[] args) {
		PostalCodeIndex index = new PostalCodeIndex();
		index.findClosest(new Coordinate(60,-135));
	}//end of method main
}//end of PostalCodeIndexTest class
