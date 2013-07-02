package test;

import geohash.GeoHash;
import geohash.WGS84Point;

public class geoHashTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		GeoHash yongfeng  = new GeoHash(30.63578, 104.031601, 60);
		
		System.out.println (yongfeng.toBinaryString() );
		System.out.println (yongfeng.toBase32() );
		//System.out.println (yongfeng.toString() );
		

		//  1 km^2 => 29 bits
	}

}
