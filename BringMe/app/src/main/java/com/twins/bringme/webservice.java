package com.twins.bringme;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



//Setup Webservice
public class webservice {
	public static String getResponseText(String stringUrl,int readtime) throws IOException
	{
		StringBuilder response  = new StringBuilder();
		try{
			//get URL
			URL url = new URL(stringUrl);
            //HTTP Connection
			HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
			httpconn.setConnectTimeout(10000);
			httpconn.setReadTimeout(readtime);

            //Check responde code
			if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
			{
				BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()),8192);
				String strLine = null;
				while ((strLine = input.readLine()) != null)
				{
					response.append(strLine);
				}
				input.close();  
			}
		}
		catch (Exception e) 
		{
			return "false";
		}
		return response.toString();

	}
}
