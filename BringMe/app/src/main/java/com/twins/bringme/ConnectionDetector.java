package com.twins.bringme;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


//Class to detect network connectivity

public class ConnectionDetector {
	
	private Context _context;
	
	public ConnectionDetector(Context context){
		this._context = context;
	}

	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		  if (connectivity != null) 
		  {
			  //Get network info
			  NetworkInfo info = connectivity.getActiveNetworkInfo();
			  if (info != null) 
			  {
				  //Check network connection
				  if (info.getState() == NetworkInfo.State.CONNECTED)
					  {
						  return true;
					  }
			  }
		  }
		  return false;
	}
}
