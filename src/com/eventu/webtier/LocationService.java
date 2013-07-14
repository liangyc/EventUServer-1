/**
 * 
 */
package com.eventu.webtier;

import geohash.GeoHash;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

import com.google.gson.JsonObject;

/**
 * @author yanliang
 *
 */
public class LocationService {
	
	private Connection conn;
	private String dbName = "LocationDB";
	
	
	public LocationService(){
		
		
		//TODO		
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "mypass");

        try {
        	conn =
			      DriverManager.getConnection("jdbc:" + "mysql" + "://" + "localhost" +
			                                  ":" + "3306" + "/" + dbName,
			                                  connectionProps);
	        
		} catch (SQLException e) {
			e.printStackTrace();
	
		}
	}
	
	
	public int updateLocation(int id, int type, GeoHash geoHash) {
		//type	0 - event, 1 - user
		
		
		String tableName = getTableName(type);
		if(tableName == null) return -2;
			
		String areaHash = geoHash.toBase32().substring(0, 9);
		
		try {
			Statement stmt = conn.createStatement();
			String query = "INSERT INTO "+ tableName +" (geoHash, id) VALUES ('" + areaHash + "','" + id +"'); ";
			int ret = stmt.executeUpdate(query);
			System.out.print(ret);
			return 0;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	
	}


	
	
	public int[] findNearby(int id, int type, GeoHash geoHash) {
		//type 0  for event, 1 for people
		GeoHash[] nearby = geoHash.getAdjacent();
		
		String tableName = getTableName(type);
		if(tableName == null) return null;
		
		ArrayList<Integer> retIDs = new ArrayList<Integer>();
		
		for(GeoHash adj : nearby){
			String areaHash = adj.toBase32().substring(0, 9);
			
			try {
				Statement stmt = conn.createStatement();
				String query = " SELECT * FROM " + tableName + " WHERE geaHash='" + areaHash + "'";
				ResultSet ret = stmt.executeQuery(query);
				
				while(ret.next()){
					int value = ret.getInt("id");
					retIDs.add(value);
				}		
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	
		}
		return convertIntegers(retIDs);
	}
	
	
	private String getTableName(int type) {
		String tableName;
		switch ( type ) {
        case 0:  
        	tableName = "locEvent";
        	break;
        case 1:
        	tableName = "locUser";
        	break;
		default:
			return null;
		}
		return tableName;
	}
	
	private  int[] convertIntegers(ArrayList<Integer> integers)
	{
	    int[] ret = new int[integers.size()];
	    for (int i=0; i < ret.length; i++)
	    {
	        ret[i] = integers.get(i).intValue();
	    }
	    return ret;
	}
	
}
