
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mysql.jdbc.Driver;

@WebServlet("/Gateway")
public class SimpleSer extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	Connection conn = null;
	
    /**
     * @throws SQLException 
     * @see HttpServlet#HttpServlet()
     */
    public SimpleSer() throws SQLException {
        super();
        // TODO Auto-generated constructor stub
        //Serial code now
        
        //test git stuff
        conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", "root");
        connectionProps.put("password", "mypass");

        // Using a driver manager:
        //DriverManager.registerDriver(new Driver());
        conn =
              DriverManager.getConnection("jdbc:" + "mysql" + "://" + "localhost" +
                                          ":" + "3306" + "/" + "testdb",
                                          connectionProps);
          //conn.setCatalog("testdb");
        System.out.println("Connected to database");
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String result= performQuery();
		JsonObject responseObject = new JsonObject();
		if(result!=null)
			responseObject.addProperty("true", result);
		else
			responseObject.addProperty("false", "");	
		out.print(responseObject.toString());
		out.close();
	}

	private String performQuery()  {
		try {
			Statement st = conn.createStatement();
			ResultSet uprs = st.executeQuery("SELECT VALUE FROM testdb.TEST_TABLE LIMIT 0, 1000");
			ArrayList<String> retArr = new ArrayList<String>();
			while (uprs.next()) {
				//TODO
				retArr.add(uprs.getString("VALUE"));
			}
			st.close();
			return retArr.toString();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

}
