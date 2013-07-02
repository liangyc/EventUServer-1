package com.eventu.webtier;

import java.io.BufferedReader;
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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mysql.jdbc.Driver;
import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

@WebServlet("/Gateway")
public class Gateway extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	HashMap<String, Integer> actionMap = new HashMap<String, Integer>();
	ApplicationService appSer1;
	
	public Gateway() throws Exception {
		super();
		
		actionMap.put("register" ,1);
		actionMap.put("login", 2);
		actionMap.put("addFriend", 3);
		actionMap.put("addFriend", 9);
		actionMap.put("updateLocation", 4);
		actionMap.put("getEvent", 5);
		actionMap.put("createEvent", 6);
		actionMap.put("allFriendsQuery", 7);
		actionMap.put("nearbyFriends", 8);
		
		DataService userDB = new DataService("UserProfile");
		ApplicationService appSer1 = new ApplicationService(userDB);
		
	//	if (!DataService.establishConnection())
	//		throw new Exception("DATA SERVICE UNABLE TO ESTABLISH CONNECTION");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Got connection! --- get");
		
		
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print("Got!");
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IO EXCEPTION CAUGHT");
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) {
		
		System.out.println("Got connection! --- put");
		
		//JsonObject jObj = new JsonObject();
		//JsonObject newObj= new JsonObject();
		// = jObj.getAsJsonObject(request.getParameter("mydata"));

		//HashMap<String, String> inputMap = new HashMap<String, String>();
		//inputMap = new Gson().fromJson(getPostBody(request), inputMap.getClass());
		JsonParser parser = new JsonParser();
		JsonObject newObj = (JsonObject)parser.parse(getPostBody(request));
		//System.out.println(newObj.toString());
		
		String action = newObj.get("action").getAsString();
		JsonObject rtv = null;
		
		switch ( actionMap.get(action) ) {
	        case 1:  
	        	rtv = appSer1.register(request, newObj);
	        	break;
	        case 2:  
	        	rtv = appSer1.login(request, newObj);
	            break;
	        case 3:
	        	rtv =  appSer1.addFriend(request, newObj);
	        case 4:
	        	rtv =  appSer1.getEvent(request, newObj);		
	        case 5:
	        	rtv = appSer1.createEvent(request, newObj);	
	        case 6:
	        	rtv = appSer1.updateLocation(request, newObj);	
	        case 7:
	        	rtv = appSer1.allFriendsQuery(request, newObj);	
	        case 8:
	        	rtv = appSer1.nearbyFriends(request, newObj);	
	        case 9:
	        	rtv = appSer1.removeFriend(request, newObj);
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
		
	
	private String getPostBody(HttpServletRequest request) {
	    StringBuilder sb = new StringBuilder();
	    try {
	        BufferedReader reader = request.getReader();
	        reader.mark(10000);

	        String line;
	        do {
	            line = reader.readLine();
	            sb.append(line).append("\n");
	        } while (line != null);
	        reader.reset();
	        // do NOT close the reader here, or you won't be able to get the post data twice
	    } catch(IOException e) {
	        System.out.println("getPostData couldn't.. get the post data");  // This has happened if the request's reader is closed    
	    }
	    String ret = sb.toString().replace("null", "").trim();
	    System.out.println(ret);
	    return ret;
}

	private void responseJson(JsonObject rtv, HttpServletResponse response) {
			// TODO Auto-generated method stub
		//TODO response.
		
		PrintWriter out;
		try {
			out = response.getWriter();
			out.print(rtv.toString());
			System.out.println("return: /n" + rtv.toString());
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

	
