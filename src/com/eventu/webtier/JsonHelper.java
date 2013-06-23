/**
 * 
 */
package com.eventu.webtier;

import com.google.gson.JsonObject;

/**
 * @author yanliang
 *
 */
public class JsonHelper {

	static JsonObject failJson(String failReason) {
		JsonObject jObj = new JsonObject();
		jObj.addProperty("Action", "Fail");
		jObj.addProperty("FailureReason",  failReason);
		return jObj;
	}

	public static JsonObject succJson(JsonObject jObj) {
		jObj.addProperty("Action", "Success");
		return jObj;
	}
}
