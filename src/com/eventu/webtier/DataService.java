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

public class DataService {

	private Connection conn;
	private String dbName;
	private LocationService locSer;
	
	public DataService(String dbName){
		
		
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
        
        
        locSer = new LocationService();
	}

	public boolean userEmailExist(String uEmail) {
		
		try {
			//TODO fix connection, make it object
			Statement stmt = conn.createStatement();
			String query = "SELECT * FROM UserAccount WHERE userEmail = '" + uEmail + "'";
			ResultSet ret = stmt.executeQuery(query);

			if (ret.next()) 
				return true;
			else
				return false;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
		
	}

	public int registerUser(String uEmail, String uPass) {
		
		
		try {
			Statement stmt = conn.createStatement();
			String query = "INSERT INTO UserInfo (userEmail ) VALUES ('" + uEmail + "')";
			System.out.println(query);
			stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			
			ResultSet rs = stmt.getGeneratedKeys();
			
			int userID = -1;
			if (rs.next()) {
				userID  = rs.getInt(1);	 
			}
			
			
			
			query = "INSERT INTO UserAccount (userEmail, userPassword, userID) VALUES ('" + uEmail + "','" +uPass+"','" + userID+ "')";
			stmt.executeUpdate(query);
			
			System.out.println(userID);
			return userID;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	public Integer login(String uEmail, String uPass) {
		
		try {
			Statement stmt = conn.createStatement();
			String query = " SELECT userPassword, userID FROM UserAccount WHERE userEmail='" + uEmail + "'";
			ResultSet ret = stmt.executeQuery(query);

			if (ret.next()) {
				String dbPass = ret.getString("userPassword").trim();
				
				if(dbPass.equals(uPass) ){
					int uid = ret.getInt("userID");
					return uid;
				}
				else{
					//System.out.println("password Wrong");
					return -1;
				}
			}

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return -1;
	}

	public int addFriend(Integer userID, Integer friendID) {
		
		// TODO Auto-generated method stub
		//add to request table
		//TODO two way invitation conflict 
		//first check conflict
		
		try {
			Statement stmt = conn.createStatement();
			
			//TODO add request
			/*
			String query = " SELECT requestID FROM Requests WHERE receiverID='" + userID + "' AND senderID='" + friendId + "'";
			ResultSet ret = stmt.executeQuery(query);

			if (ret.next()) {
				//already exist
				String dbPass = ret.getString("requestID");
				
				return ret.getInt("userID");
			}else{
				
			}
			*/
			
			String query = "INSERT INTO UserFriend (userID, friendID) VALUES ('" + userID + "','" + friendID +"'); ";
			int ret = stmt.executeUpdate(query);
			//System.out.println(ret);
			
			query = "INSERT INTO UserFriend (userID, friendID) VALUES ('" + friendID + "','" + userID +"') ;";
			ret = stmt.executeUpdate(query);
			//System.out.println(ret);
			
			return 0;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		
	}

	public int updateLocation(Integer id, Long latitude,
			Long longitude, GeoHash geoHash, int type) {
		
		locSer.updateLocation(id, type,  geoHash);
		
		return 0;
	}

	public int removeFriend(Integer userID, Integer friendID) {
		
		try {
			Statement stmt = conn.createStatement();
			String query = "DELETE FROM UserFriend WHERE userID = '" + userID + "'; ";
			
			int ret = stmt.executeUpdate(query);
			//System.out.println(ret);
			query = "DELETE FROM UserFriend WHERE userID = '" + friendID + "'; ";
			ret = stmt.executeUpdate(query);
			return 0;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}

	}

	public int createEvent(Integer userID, String eventName,
			int eventPremission, String eventTime, Float latitude,
			Float longitude, GeoHash geoHash) {
		
		
		try {
			Statement stmt = conn.createStatement();
			String query = "INSERT INTO EventInfo (eventName, time, public, lat, lon, geohash) VALUES " +
					"('" + eventName + "','" +eventTime+"','" + eventPremission + "','" +latitude+"','" + longitude +"','"+ geoHash.toBase32()+"');";
			
			//System.out.println(query);
			int ret = stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			
			ResultSet rs = stmt.getGeneratedKeys();
			
			int eventID = -1;
			if (rs.next()) {
				eventID  = rs.getInt(1);	 
			}
			
			
			
			query = "INSERT INTO EventUser (eventID, userID, accessibility) VALUES" +
					"('" + eventID + "','" +userID+"',' 0' )";
			int ret2 = stmt.executeUpdate(query);
			
			int locRet = locSer.updateLocation(eventID, 0,  geoHash);
			if (locRet < 0)
				return (locRet-10);
			else
				return 0;//TODO
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
		
		
	}

	public ArrayList<Integer> allFriendsQuery(Integer userID) {
		
		try {
			Statement stmt = conn.createStatement();
			String query = " SELECT friendID FROM UserFriend WHERE userID='" + userID + "'";
			ResultSet ret = stmt.executeQuery(query);

			//JsonObject retJ = new JsonObject();
			
			ArrayList<Integer> idArray = new ArrayList<Integer>();
			while (ret.next()) {
				int friendID = ret.getInt("friendID");
				idArray.add(friendID);
			}
			
			return idArray;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}

	public JsonObject detailProfile(Integer friendID) {
		try {
			
			//TODO check friendship, privacy
			
			Statement stmt = conn.createStatement();
			String query = " SELECT * FROM UserInfo WHERE userID='" + friendID + "'";
			ResultSet ret = stmt.executeQuery(query);
			
			JsonObject jo = new JsonObject();
			if(ret.next()){
				String value = ret.getString("userEmail");
				jo.addProperty("userEmail", value);
				
				value = ret.getString("userName");
				jo.addProperty("userName", value);
				
				int picID = ret.getInt("picID");
				query = " SELECT picAddr FROM UserPic WHERE picID='" + picID + "'";
				ResultSet picret = stmt.executeQuery(query);
				if(picret.next()){
					value = picret.getString("picAddr");
					jo.addProperty("picAddr", value);
				}else{
					return null;
				}
				
				return jo;
			}
			else{ 
				return null;
			}
		}catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
			
		
	}

	public int joinEvent(Integer userID, Integer eventID) {
		
		try {
			Statement stmt = conn.createStatement();
			String query = " SELECT * FROM EventInfo WHERE eventID='" + eventID + "'";
			ResultSet ret = stmt.executeQuery(query);
			
			
			if(ret.next()){
				int isPublic = ret.getInt("public");//TODO test it
				
				if(isPublic == 0){
					//public
					query = "INSERT INTO EventUser (eventID, userID, accessibility) VALUES" +
							"('" + eventID + "','" +userID+"','"  + "', 1 )";
					ResultSet ret2 = stmt.executeQuery(query);
					return 0;
				}
				else{
					//private
					//TODO send request
					query = "INSERT INTO EventUser (eventID, userID, accessibility) VALUES" +
							"('" + eventID + "','" +userID+"','"  + "', 1 )";
					ResultSet ret2 = stmt.executeQuery(query);
					return 1;//TODO return request sent, just add it in prototype
				}
				

			}
			else{ 
				return -1;//No such event
			}
		}catch (SQLException e) {
			
			e.printStackTrace();
			return -1;
		}
	}

	public JsonObject viewEvent(Integer userID, Integer eventID) {
		
		try {
			
			//TODO check userEvent, privacy
			Statement stmt = conn.createStatement();
			String query = " SELECT * FROM EventInfo WHERE eventID='" + eventID + "'";
			ResultSet ret = stmt.executeQuery(query);
			
			JsonObject jo = new JsonObject();
			if(ret.next()){
				
				//check public, joined or not
				int isPublic = ret.getInt("public");
				if(isPublic != 0){
					//private
					query = " SELECT * FROM EventUser WHERE eventID='" + eventID + "' AND userID = '" + userID +"')";
					ResultSet userEventret = stmt.executeQuery(query);
					
					if( ! ret.next() ){
						jo.addProperty("Failed", "you dont have premission");
						return jo;
					}
				}
				
				String value = ret.getString("eventName");
				jo.addProperty("eventName", value);
				
				value = ret.getString("time");
				jo.addProperty("time", value);
				
				value = Float.toString (ret.getFloat("long") );
				jo.addProperty("long", value);

				value = Float.toString (ret.getFloat("lat") );
				jo.addProperty("lat", value);
				
				value = ret.getString("descript");
				jo.addProperty("descript", value);
				
				return jo;
			}
			else{ 
				jo.addProperty("Failed", "no such event");
				return jo;
			}
		}catch (SQLException e) {
			
			e.printStackTrace();
			return null;
		}
	}

	
	
}
