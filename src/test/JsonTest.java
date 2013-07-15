package test;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.eventu.webtier.ApplicationService;
import com.eventu.webtier.DataService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JsonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ApplicationService appSer1 = new ApplicationService();

//test register

		HttpServletRequest request = null;
		//String json = "{\"password\":\"cb0769dcb7e8d3f7b30762254e649cd52fd32b5be62fdsfsdf724353f3f\",\"action\":\"login\",\"userEmail\":\"7@dsf.sdf\"}";
		//String json  = "{\"friendID\":\"2\",\"action\":\"removeFriend\",\"userID\":\"13\"}";
		String json  = "{\"action\":\"allFriend\",\"userID\":\"13\"}";
		//String json  = "{\"action\":\"createEvent\",\"userID\":\"13\", \"eventName\":\"testEvent4\", \"eventPremission\":0, \"eventTime\":'2013-08-01 16:00:00', \"latitude\":37.33509, \"longitude\":-121.88611, \"description\":\"this is test event 2\"  }";
		//String json = "{\"action\":\"nearbyEvent\", \"userID\":\"13\",\"latitude\":37.33509, \"longitude\":-121.88611}";
		JsonParser jsonParser = new JsonParser();
		JsonObject jo = (JsonObject)jsonParser.parse(json);
		
		JsonObject rtv = appSer1.allFriendsQuery(request, jo );
		System.out.println(rtv);

		/*
		ArrayList<TestClass> arr = new ArrayList<TestClass>();
		arr.add( new TestClass("ss1",111) );
		arr.add( new TestClass("ss2",222) );
		JsonObject retJ = new JsonObject();
		
		Gson gson = new Gson();
		java.lang.reflect.Type listOfTestObject = new TypeToken<List<TestClass>>(){}.getType();
		String s = gson.toJson(arr, listOfTestObject);
		
		retJ.addProperty("list", s);
		
		System.out.println(retJ.toString());
		//INSERT INTO UserInfo (userEmail ) VALUES ('7@dsf.sdf')
		 * 
		 */
	}

	
	
}
