package com.eventu.webtier;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mysql.jdbc.Driver;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

@WebServlet("/Gateway")
public class Gateway extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	HashMap<String, Integer> actionMap = new HashMap<String, Integer>();
	
	public Gateway() throws Exception {
		super();
		
		actionMap.put("register" ,1);
		actionMap.put("login", 2);
		actionMap.put("addFriend", 3);
		actionMap.put("updateLocation", 4);
		actionMap.put("getEvent", 5);
		actionMap.put("createEvent", 6);
		actionMap.put("allFriendsQuery", 7);
		actionMap.put("nearbyFriends", 8);
		
		
		
		if (!DataService.establishConnection())
			throw new Exception("DATA SERVICE UNABLE TO ESTABLISH CONNECTION");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		//TODO 
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		JsonObject jObj = new JsonObject();
		JsonObject newObj = jObj.getAsJsonObject(request.getParameter("mydata"));
		
		String action = newObj.get("action").getAsString();
		JsonObject rtv = null;
		
		switch ( actionMap.get(action) ) {
	        case 1:  
	        	rtv = ApplicationService.register(request, newObj);
	        	break;
	        case 2:  
	        	rtv = ApplicationService.login(request, newObj);
	            break;
	        case 3:
	        	rtv = ApplicationService.addFriend(request, newObj);
	        case 4:
	        	rtv = ApplicationService.getEvent(request, newObj);		
	        case 5:
	        	rtv = ApplicationService.createEvent(request, newObj);	
	        case 6:
	        	rtv = ApplicationService.updateLocation(request, newObj);	
	        case 7:
	        	rtv = ApplicationService.allFriendsQuery(request, newObj);	
	        case 8:
	        	rtv = ApplicationService.nearbyFriends(request, newObj);	
	        default:
	            rtv = null;
		}        
		
		if(rtv != null ){
			responseJson(rtv, response);
		}
		else{
			//TODO return no such action!
		}
			
   
	}
		
	
	private void responseJson(JsonObject rtv, HttpServletResponse response) {
			// TODO Auto-generated method stub
		response.
		
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(rtv.toString());
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO EXCEPTION CAUGHT");
		}
	}


}


/*     
String actionType = newObj.get("action").getAsString();
if(actionType.equals("register")){
	JsonObject rtv = ApplicationService.register(request, newObj);
	PrintWriter out;
	try {
		out = response.getWriter();
		out.print(rtv.toString());
	} catch (IOException e) {
		e.printStackTrace();
		System.out.println("IO EXCEPTION CAUGHT");
	}
}*/

	
