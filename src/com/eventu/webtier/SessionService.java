package com.eventu.webtier;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionService {
	
	upadteSession(HttpServletRequest request){
		
		HttpSession s = request.getSession(true);
		s.setAttribute("userID", "dsf");
		
	}

	public static Integer generateSessionId(HttpServletRequest request,
			String uEmail, String uPass) {
		
		Random r = new Random();
		Integer random = r.nextInt();
		
		Integer unique =  uEmail.hashCode()^uPass.hashCode()^random;
		
		//table
		// Unique(session id, int)       userID       
		//TODO http authe..
		
		return unique;
		
	}

	public static Integer getUID(Integer sessionID) {
		// TODO Auto-generated method stub
		//-1 for not exist
		return null;
	}
	
	
	
}
