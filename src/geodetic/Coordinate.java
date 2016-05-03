package geodetic;

/*	File Name: Coordinate Class
 *	Course Name: Data Structures CST 8130
 *	Lab	Section:CST 8130-301
 *	Student	Name: Amaury Diaz Diaz
 *	Date: Apr-14-2015
 */
/*
 * References:(Rex Woollard (2015) personal communication)
 */
public final class Coordinate {
	private final double latitude;
	private final double longitude;
	private int hash;
	
	public Coordinate(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() { return latitude; }
	public double getLongitude() { return longitude; }
	
	public double distanceTo(Coordinate targetCoordinate) {
		return GeodeticCalculator.distVincenty(latitude, longitude, targetCoordinate.latitude, targetCoordinate.longitude);
	}

	/** Not currently implemented. Future Use: When two Coordinate objects have IDENTICAL values, could implement a hash table to eliminate duplicates, and return a reference to an existing IDENTICAL Coordinate. */
	public Coordinate intern() {
		return this;
	}
	
	/**
	 * Key Concept: A hashCode() method MUST return the SAME value for objects that are deemed to be identical.
	 * Multiply latitude/longitude as a double. Ultimately convert to a 32-bit int.
	 * Retain 6 decimal places of significance by multiplying by 100000.0
	 * Convert to long (64-bit); Modulus Integer.MAX_VALUE to render an int
	 */
	@Override
	public final int hashCode() {
		if (hash == 0) {
			final double DECIMAL_SIGNIFICANCE = 100000.0;
			hash = (int)(((long)((latitude*longitude)*DECIMAL_SIGNIFICANCE))%Integer.MAX_VALUE);
		}
		return hash;
	}
	
	@Override
	public final boolean equals(Object object) {
		if (object instanceof Coordinate) {
			Coordinate coordinate = (Coordinate)object;
			final double EPSILON = 0.0000001; // explicitly defined level of precision. Precision smaller does not matter.
			if (Math.abs(coordinate.latitude-latitude) < EPSILON  &&  Math.abs(coordinate.longitude-longitude) < EPSILON)
				return true;
			// if (Double.doubleToLongBits(coordinate.latitude) == Double.doubleToLongBits(latitude) && Double.doubleToLongBits(coordinate.longitude) == Double.doubleToLongBits(longitude)) 
			// if (new Double(coordinate.latitude).equals(new Double(latitude)) && new Double(coordinate.longitude).equals(new Double(longitude))) 
			// if (coordinate.latitude == latitude && coordinate.longitude == longitude) // Easy but unsafe test.
		}
		return false;
	}

	@Override
	public String toString() {
		return String.format("[%8.4f:%8.4f]", latitude, longitude);
	}
}
