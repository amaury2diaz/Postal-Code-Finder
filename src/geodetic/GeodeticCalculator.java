package geodetic;
/*	File Name: GeodeticCalculator Class
 *	Course Name: Data Structures CST 8130
 *	Lab	Section:CST 8130-301
 *	Student	Name: Amaury Diaz Diaz
 *	Date: Apr-14-2015
 */
/*
 * References:(Rex Woollard (2015) personal communication)
 */
public class GeodeticCalculator {

  /**
   * Calculates geodetic distance between two points specified by latitude/longitude using Vincenty inverse formula for ellipsoids
   *
   * @param latitude1 first point latitude in decimal degrees
   * @param longitude1 first point longitude in decimal degrees
   * @param latitude2 second point latitude in decimal degrees
   * @param longitude2 second point longitude in decimal degrees
   * @returns distance in meters between points with 5.10<sup>-4</sup> precision
   * @see <a href="http://www.movable-type.co.uk/scripts/latlong-vincenty.html">Originally posted here</a>
   */
	public static double distVincenty(Coordinate topLeft, Coordinate bottomRight) {
		return distVincenty(topLeft.getLatitude(), topLeft.getLongitude(), bottomRight.getLatitude(), bottomRight.getLongitude());
	}
  public static double distVincenty(double latitude1, double longitude1, double latitude2, double longitude2) {
    double f = 1 / 298.257223563; // WGS-84 ellipsoid params
    double L = Math.toRadians(longitude2 - longitude1);
    double U1 = Math.atan((1.0 - f) * Math.tan(Math.toRadians(latitude1)));
    double U2 = Math.atan((1.0 - f) * Math.tan(Math.toRadians(latitude2)));
    double sinU1 = Math.sin(U1), cosU1 = Math.cos(U1);
    double sinU2 = Math.sin(U2), cosU2 = Math.cos(U2);

    double sinLambda, cosLambda, sinSigma, cosSigma, sigma, sinAlpha, cosSqAlpha, cos2SigmaM;
    double lambda = L;
    double lambdaP;
    double iterLimit = 100.0;
    do {
      sinLambda = Math.sin(lambda);
      cosLambda = Math.cos(lambda);
      sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda) + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
      if (sinSigma == 0.0)
        return 0.0; // co-incident points
      cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
      sigma = Math.atan2(sinSigma, cosSigma);
      sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
      cosSqAlpha = 1.0 - sinAlpha * sinAlpha;
      cos2SigmaM = cosSigma - 2.0 * sinU1 * sinU2 / cosSqAlpha;
      if (Double.isNaN(cos2SigmaM))
        cos2SigmaM = 0; // equatorial line: cosSqAlpha=0 (§6)
      double C = f / 16.0 * cosSqAlpha * (4.0 + f * (4.0 - 3.0 * cosSqAlpha));
      lambdaP = lambda;
      lambda = L + (1.0 - C) * f * sinAlpha
          * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1.0 + 2.0 * cos2SigmaM * cos2SigmaM)));
    } while (Math.abs(lambda - lambdaP) > 1.0e-12 && --iterLimit > 0);

    if (iterLimit == 0)
      return Double.NaN; // formula failed to converge

    double a = 6378137;
    double b = 6356752.314245;
    double uSq = cosSqAlpha * (a * a - b * b) / (b * b);
    double A = 1.0 + uSq / 16384.0 * (4096.0 + uSq * (-768.0 + uSq * (320.0 - 175.0 * uSq)));
    double B = uSq / 1024.0 * (256.0 + uSq * (-128.0 + uSq * (74.0 - 47.0 * uSq)));
    double deltaSigma = B * sinSigma * (cos2SigmaM + B / 4.0 * (cosSigma * (-1.0 + 2.0 * cos2SigmaM * cos2SigmaM) - B / 6.0 * cos2SigmaM * (-3.0 + 4.0 * sinSigma * sinSigma) * (-3.0 + 4.0 * cos2SigmaM * cos2SigmaM)));
    double distance = b * A * (sigma - deltaSigma);

    return distance;
  }
}
