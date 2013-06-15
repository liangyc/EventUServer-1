package com.eventu.webtier;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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

@WebServlet("/Gateway")
public class Gateway extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public Gateway() throws Exception {
		super();
		if (!DataService.establishConnection())
			throw new Exception("DATA SERVICE UNABLE TO ESTABLISH CONNECTION");
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {

		JsonObject jObj = new JsonObject();
		JsonObject newObj = jObj
				.getAsJsonObject(request.getParameter("mydata"));
		
		String actionType = newObj.get("action").getAsString();
		if(actionType.equals("register")){
			boolean result = LoginService.register(request, newObj);
		}
		else if(actionType.equals("")){
			
		}
		else if(actionType.equals("")){
			
		}
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
	}

}
