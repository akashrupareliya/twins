package com.twins.bringme;


//Class to get URLS and to validate input fields.

public class AppFieldNames {

	//URLs to add and retrive data to & from database.
	public static String ADD_PRODUCT_URL="http://mgov.netau.net/add_product_detail.php?";
	public static String SIGNUP="register";
	public static String RATING_URL="http://mgov.netau.net/submit_rating.php?";



	//Function for validation.Check if input is blank or not.
	public static boolean IsStringValid(String str)
	{
		if(str!=null && !(str.equals("")) && str.length()!=0)
			return true;
		else
			return false;
	}

}
