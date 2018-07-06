package harParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class HarParser {
	static String PROXY_ADDRESS =  "http://localhost:8181/proxy/8081/har";
	static String WANTED_CONNECTION = "127.0.0.1";
	private static StringBuffer sendGet() throws Exception {

		String url = PROXY_ADDRESS;
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		
	//System.out.println(response.toString());
		return response;
	}
	
	  
	public static JSONArray parse(JSONArray full) {
		JSONArray parsed = new JSONArray(); 
		
		for (int i = 0; i < full.length(); i++) {
			  try {
				
				  if(full.getJSONObject(i).getString("serverIPAddress").equals(WANTED_CONNECTION))
				  {
					  parsed.put(full.getJSONObject(i));
				  }
			
			  
			  } catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}


		
		
		return parsed;
	}
	
	public static void writeToFile(JSONObject arr) throws IOException {
	
		String jsonString = arr.toString();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("connections.har"));
	    writer.write(jsonString);
	     
	    writer.close();
		
	
	
		
	}
	
	
	  public static void main(String [ ] args) throws Exception 
	  {
		  
		  StringBuffer output = sendGet();
		  JSONObject jsonObj = new JSONObject(output.toString());
		  //System.out.print(jsonObj.getJSONObject("log").getString("version"));
		  //Get connection data
		  JSONArray connections = jsonObj.getJSONObject("log").getJSONArray("entries");
		  
		  System.out.print("\nArray length before parse :"+ connections.length());
		  
		  
		  JSONArray arr = parse(connections);
		  
		  System.out.print("\nAfter parse " + arr.length());
		  
		  jsonObj.getJSONObject("log").remove("entries");
		  jsonObj.getJSONObject("log").put("entries", arr);
		  writeToFile(jsonObj);
		  System.out.print("\nDONE");
		  
		  
	  }	  
	  
	  
	  
	  
	
}
