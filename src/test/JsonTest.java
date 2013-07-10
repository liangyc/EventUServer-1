package test;

import javax.servlet.http.HttpServletRequest;

import com.eventu.webtier.ApplicationService;
import com.eventu.webtier.DataService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataService userDB = new DataService("UserProfile");
		ApplicationService appSer1 = new ApplicationService(userDB);

//test register

		HttpServletRequest request = null;
		//String json = "{\"password\":\"cb0769dcb7e8d3f7b30762254e649cd52fd32b5be6298b0bf4896724353f3f\",\"action\":\"login\",\"userEmail\":\"5@dsf.sdf\"}";
		//String json  = "{\"friendID\":\"2\",\"action\":\"removeFriend\",\"userID\":\"13\"}";
		//String json  = "{\"action\":\"allFriend\",\"userID\":\"13\"}";
		String json  = "{\"action\":\"detailProfile\",\"userID\":\"13\", \"friendID\":\"1\"}";
		JsonParser jsonParser = new JsonParser();
		JsonObject jo = (JsonObject)jsonParser.parse(json);
		
		JsonObject rtv = appSer1.detailProfile(request, jo );
		System.out.println(rtv);

		
	}

}
