/**
 * 
 */
package com.eventu.webtier;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * @author yanliang
 *
 */
public class JsonHelper {

	static JsonObject failJson(String failReason) {
		JsonObject jObj = new JsonObject();
		jObj.addProperty("action", "Fail");
		jObj.addProperty("FailureReason",  failReason);
		return jObj;
	}

	public static JsonObject succJson(JsonObject jObj) {
		jObj.addProperty("action", "Success");
		return jObj;
	}
	
	
	public static JsonObject array2J(ArrayList arr ) {
		
		JsonObject retJ = new JsonObject();
		
		StringBuffer sb = new StringBuffer();
		sb.append("{\"friends\":[");
		
		for(Object uid: arr){
			sb.append("{\"userID\":"+uid+"},");
		}
		
		sb.deleteCharAt(sb.length()-1);
		
		sb.append("]}");
		
		JsonParser jsonParser = new JsonParser();
		JsonObject jo = (JsonObject)jsonParser.parse(sb.toString());
		
		return jo;
		
	}
	
}
