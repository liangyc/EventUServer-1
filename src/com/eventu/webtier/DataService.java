package com.eventu.webtier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataService {

	static Connection conn = null;
	
	public static boolean establishConnection(){
		//TODO		
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "mypass");

        try {
			conn =
			      DriverManager.getConnection("jdbc:" + "mysql" + "://" + "localhost" +
			                                  ":" + "3306" + "/" + "testdb",
			                                  connectionProps);
	        return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static boolean userEmailExist(String uEmail) {
		// TODO Auto-generated method stub
		return false;
	}

	public static int registerUser(String uEmail, String uPass) {
		// TODO Auto-generated method stub
		return 0;
	}

	public static Integer login(String uEmail, String uPass) {
		// TODO Auto-generated method stub
		return false;
	}

	public static int addFriend(Integer userID, String friendId) {
		// TODO Auto-generated method stub
		//add to request table
		
		
		//TODO two way invitation conflict 
	}

	
	
}
