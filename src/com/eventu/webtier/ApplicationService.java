package com.eventu.webtier;

import java.util.ArrayList;

import geohash.GeoHash;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


public class ApplicationService {

	DataService myDS;
	LocationService myLS;
	
	public ApplicationService() {
		
		myDS = new DataService("UserProfile");
		myLS = new LocationService();
	}

	public  JsonObject register(HttpServletRequest request, JsonObject jObj) {
		
		//JsonObject jData = newObj.get("data").getAsJsonObject();
		String uEmail = jObj.get("userEmail").getAsString();
		String uPass = jObj.get("password").getAsString();

		if( myDS.userEmailExist(uEmail) ){
			return JsonHelper.failJson("email ready exits");
		}
		else{
			int userID = myDS.registerUser(uEmail, uPass);
			
			Integer sessionId = SessionService.generateSessionId(request, uEmail, uPass, userID);
			
			JsonObject retJson = new JsonObject();
			retJson.addProperty("userID", sessionId);
			return JsonHelper.succJson(retJson);
			//return login(request, jObj);
			//JsonObject retJson = new JsonObject();
			//retJson.addProperty("userID", userID);
			//TODO Session
			//return JsonHelper.succJson(retJson);
			//TODO call login function!
			
		}
	}

	public JsonObject login(HttpServletRequest request, JsonObject jObj) {
		
		String uEmail = jObj.get("userEmail").getAsString();
		String uPass = jObj.get("password").getAsString();
		
		Integer uid = myDS.login(uEmail, uPass);
		//null if fail
		
		if( uid>=0 ){
			//generate session id
			Integer sessionId = SessionService.generateSessionId(request, uEmail, uPass, uid);
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
	
	public JsonObject addFriend(HttpServletRequest request,
			JsonObject jObj) {
		
		//check doc 
		Integer sessionID = jObj.get("userID").getAsInt();
		Integer friendId = jObj.get("friendID").getAsInt();
		
		Integer userID = SessionService.getUID(sessionID);
		
		if (userID == -1){
			return JsonHelper.failJson("login fail");
		}
		else{
			int ret = myDS.addFriend(userID, friendId);
			if (ret != 0 ){
				return JsonHelper.failJson("already your friend");
			}
			else{
				return JsonHelper.succJson( new JsonObject() );
			}
		}
		

	}

	public  JsonObject removeFriend(HttpServletRequest request,
			JsonObject jObj) {
		
		//check doc 
		Integer sessionID = jObj.get("userID").getAsInt();
		Integer friendId = jObj.get("friendID").getAsInt();
		
		Integer userID = SessionService.getUID(sessionID);
		
		if (userID == -1){
			return JsonHelper.failJson("login fail");
		}
		else{
			int ret = myDS.removeFriend(userID, friendId);
			if (ret != 0 ){
				return JsonHelper.failJson("Delete fail!");
			}
			else{
				return JsonHelper.succJson( new JsonObject() );
			}
		}
		

	}
	
	
	public JsonObject updateLocation(HttpServletRequest request,
			JsonObject jObj) {
		Float latitude = jObj.get("latitude").getAsFloat();
		Float longitude = jObj.get("longitude").getAsFloat();
		
		GeoHash geoHash = new GeoHash(latitude, longitude, 60);
		
		Integer sessionID = jObj.get("userID").getAsInt();
		Integer userID = SessionService.getUID(sessionID);
		
		if (userID == -1){
			return JsonHelper.failJson("login fail");
		}
		else{
			int eventID = myLS.updateLocation(userID, 1, geoHash);
			
			if (eventID == -1 ){
				return JsonHelper.failJson("update location failed!..");
			}
			else{
				JsonObject retJson = new JsonObject();
				retJson.addProperty("eventID", eventID);
				return JsonHelper.succJson( retJson );
			}
			
		}
		
	}

	public JsonObject getEvent(HttpServletRequest request,
			JsonObject jObj) {
		
		//TODO
		
		Integer sessionID = jObj.get("userID").getAsInt();
		Integer userID = SessionService.getUID(sessionID);
		
		return null;
	}

	public  JsonObject createEvent(HttpServletRequest request,
			JsonObject jObj) {
		
		Integer sessionID = jObj.get("userID").getAsInt();
		Integer userID = SessionService.getUID(sessionID);

		String eventName = jObj.get("eventName").getAsString();
		int eventPremission = jObj.get("eventPremission").getAsInt(); //0 - public 1-private 2-....
		String eventTime = jObj.get("eventTime").getAsString();
		
		Float latitude = jObj.get("latitude").getAsFloat();
		Float longitude = jObj.get("longitude").getAsFloat();
		GeoHash geoHash = new GeoHash(latitude, longitude, 60);
		
		String description = jObj.get("description").getAsString();
		
		if (userID == -1){
			return JsonHelper.failJson("login fail");
		}
		else{
			int ret = myDS.createEvent(userID, eventName, eventPremission, eventTime, latitude, longitude, geoHash);
			
			if (ret != 0 ){
				return JsonHelper.failJson("creat event failed!..");
			}
			else{
				return JsonHelper.succJson( new JsonObject() );
			}
			
		}

	}

	public JsonObject allFriendsQuery(HttpServletRequest request,
			JsonObject jObj) {
		
		Integer sessionID = jObj.get("userID").getAsInt();
		
		Integer userID = SessionService.getUID(sessionID);
		
		if (userID == -1){
			return JsonHelper.failJson("login fail");
		}
		else{
			ArrayList<Integer> ret = myDS.allFriendsQuery(userID);
			if (ret == null ){
				return JsonHelper.failJson("Error!");
			}
			else if( ret.size() == 0 ){
				return JsonHelper.failJson("You dont have any friends!");
			}
			else{
				
				JsonObject jo = JsonHelper.array2J(ret);
				return JsonHelper.succJson(jo);
				
			}
		}
	}
		
	

	public  JsonObject nearbyFriends(HttpServletRequest request,
			JsonObject newObj) {
		// TODO Auto-generated method stub
		return null;
	}

	public JsonObject detailProfile(HttpServletRequest request,
			JsonObject jObj) {
		
		Integer sessionID = jObj.get("userID").getAsInt();
		Integer friendID = jObj.get("friendID").getAsInt();
		
		Integer userID = SessionService.getUID(sessionID);
		
		if (userID == -1){
			return JsonHelper.failJson("login fail");
		}
		else{
			
			JsonObject jo = myDS.detailProfile(friendID);
			return JsonHelper.succJson(jo);
		}
	}

	public JsonObject joinEvent(HttpServletRequest request, JsonObject jObj) {
		Integer sessionID = jObj.get("userID").getAsInt();
		Integer EventID = jObj.get("EventID").getAsInt();
		
		Integer userID = SessionService.getUID(sessionID);
		
		if (userID == -1){
			return JsonHelper.failJson("login fail");
		}
		else{
			//TODO check friendship, privacy
			int ret = myDS.joinEvent(userID, EventID);
			
			if(ret == 0){
				return JsonHelper.succJson(new JsonObject());
			}
			else if(ret == 1){
				return JsonHelper.failJson("request sent!"); //in protoType this is success
			}
			else{
				return JsonHelper.failJson("Somethings wrong");
			}
		}

	}

	public JsonObject viewEvent(HttpServletRequest request, JsonObject jObj) {
		
		Integer sessionID = jObj.get("userID").getAsInt();
		Integer eventID = jObj.get("eventID").getAsInt();
		
		Integer userID = SessionService.getUID(sessionID);
		
		if (userID == -1){
			return JsonHelper.failJson("login fail");
		}
		else{
			//TODO check friendship, privacy
			JsonObject jo = myDS.viewEvent(userID, eventID);
			
			String failReason = jo.get("Failed").getAsString();
			if( failReason!= null){
				return JsonHelper.failJson(failReason);
			}
			else{
				return JsonHelper.succJson(jo);
			}
		}
		

	}

	public JsonObject nearbyEvent(HttpServletRequest request, JsonObject jObj) {
		
		Integer sessionID = jObj.get("userID").getAsInt();
			
		Integer userID = SessionService.getUID(sessionID);
	
		Float latitude = jObj.get("latitude").getAsFloat();
		Float longitude = jObj.get("longitude").getAsFloat();
		GeoHash geoHash = new GeoHash(latitude, longitude, 60);
		
		ArrayList<Integer> eventIDs = myLS.findNearby( 0, geoHash);
		
		
		
		//TODO
		if(eventIDs == null){
			return JsonHelper.failJson("Fail");
		}
		else{
			JsonObject jo = JsonHelper.array2J(eventIDs);
			return JsonHelper.succJson(jo);
		}

	}

	
	
	
}
