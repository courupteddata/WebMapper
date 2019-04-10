package net;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

public class NetHelper
{
	public static boolean touch(String serverIP, int port)
	{
		try
		{
			Socket server = new Socket(serverIP, port);
			new PrintWriter(server.getOutputStream(), true);
			new BufferedReader(new InputStreamReader(server.getInputStream()));
			server.close();
		}
		catch (IOException e)
		{
			return false;
		}
		
		return true;
	}
	
	public static String getWork() throws IOException
	{
		URL url = new URL("http://map.codinginc.com/api/getwork.php");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		String tmp = response.toString();
		
		if(!((tmp.toLowerCase()).startsWith("http://")) && !((tmp.toLowerCase()).startsWith("https://")))
		{
			tmp = "http://" + tmp;
		}
	
		
		return tmp.trim();
	}
	
	
	
	public static void sendResults(String originalURL, ArrayList<String> external,String key) throws IOException
	{
			String data = Library.serialize(external);
			String pass = Library.serialize(key);
			String original = Library.serialize(originalURL);
			 
		
		
			
			URL url = new URL("http://map.codinginc.com/api/submit.php");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
	 
			
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent",  utils.Library.userAgent);
		
	 
			String urlParameters = "data="+ data + "&key=" + pass + "&url=" + original;
	 
			
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
	 
		

	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
	 
			//print result
			System.out.println(response.toString());
	 
		}
	public static void suggestion(ArrayList<String> list) throws IOException
	{
		String data = Library.serialize(list);

		 
			
		URL url = new URL("http://map.codinginc.com/api/suggestion.php");
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
 
		
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent",  utils.Library.userAgent);
	
 
		String urlParameters = "list="+ data;
 
		
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
	
		



		
		
	}
	 
	
	
		
	public static void main(String[] args) throws IOException
	{
		/**String url = "example1.com";
		
		ArrayList<String> list = new ArrayList<>();
		list.add("ASDFSFD.COM");
		list.add("https://google.com");
		list.add("http://test.com");
		list.add("extremeexample.com");
		list.add("yup.co");
		
		String key = "sadf789/*-";
		
		
		NetHelper.sendResults(url, list, key);
		NetHelper.suggestion(list);**/
		
	}
}

