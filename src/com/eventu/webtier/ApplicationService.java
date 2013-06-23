package com.eventu.webtier;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class ApplicationService {

	public static JsonObject register(HttpServletRequest request, JsonObject jObj) {
		
		//JsonObject jData = newObj.get("data").getAsJsonObject();
		String uEmail = jObj.get("userEmail").getAsString();
		String uPass = jObj.get("userPasswork").getAsString();

		if( DataService.userEmailExist(uEmail) ){
			return JsonHelper.failJson("email ready exits");
		}
		else{
			DataService.registerUser(uEmail, uPass);
			return login(request, jObj);
			//JsonObject retJson = new JsonObject();
			//retJson.addProperty("userID", userID);
			//TODO Session
			//return JsonHelper.succJson(retJson);
			//TODO call login function!
			
		}
	}

	public static JsonObject login(HttpServletRequest request, JsonObject jObj) {
		
		String uEmail = jObj.get("userEmail").getAsString();
		String uPass = jObj.get("userPasswork").getAsString();
		
		Integer uid = DataService.login(uEmail, uPass);
		//null if fail
		
		if( uid!=null ){
			//generate session id
			Integer sessionId = SessionService.generateSessionId(request, uEmail, uPass);
			JsonObject retJson = new JsonObject();
			retJson.addProperty("userID", sessionId);
			return JsonHelper.succJson(retJson);
		}
		else
		{
			//return a Json Object that says the reason of login fail
			return JsonHelper.failJson("Email and Password dont match!");
		}		
		
		
	}

	
	//TODO use email to view user profile.
	
	public static JsonObject addFriend(HttpServletRequest request,
			JsonObject jObj) {
		// TODO Auto-generated method stub
		//check doc 
		Integer sessionID = jObj.get("userID").getAsInt();
		String friendId = jObj.get("friendId").getAsString();
		
		Integer userID = SessionService.getUID(sessionID);
		
		if (userID == -1){
			return JsonHelper.failJson("login fail");
		}
		else{
			int ret = DataService.addFriend(userID, friendId);
			if (ret != 0 ){
				return JsonHelper.failJson("already your friend");
			}
			else{
				return JsonHelper.succJson( new JsonObject() );
			}
		}
		

	}

	public static JsonObject updateLocation(HttpServletRequest request,
			JsonObject newObj) {
		// TODO Auto-generated method stub
		return null;
	}

	public static JsonObject getEvent(HttpServletRequest request,
			JsonObject newObj) {
		// TODO Auto-generated method stub
		return null;
	}

	public static JsonObject createEvent(HttpServletRequest request,
			JsonObject newObj) {
		// TODO Auto-generated method stub
		return null;
	}

	public static JsonObject allFriendsQuery(HttpServletRequest request,
			JsonObject newObj) {
		// TODO Auto-generated method stub
		return null;
	}

	public static JsonObject nearbyFriends(HttpServletRequest request,
			JsonObject newObj) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
