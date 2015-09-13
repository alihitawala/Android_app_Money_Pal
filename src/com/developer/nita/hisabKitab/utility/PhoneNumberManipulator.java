package com.developer.nita.hisabKitab.utility;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PhoneNumberManipulator {
	
	public static List<String> getAllPhoneNumbers(String phoneNumbers)
	{
		List<String> phoneList = new ArrayList<String>();
		if (!(phoneNumbers.trim().length() == 0))
		{
			StringTokenizer tokeniser = new StringTokenizer(phoneNumbers, ",;.");
			 
			while (tokeniser.hasMoreElements()) {
				String token = tokeniser.nextToken();
				token = token.replaceAll("-", "");
				token = token.replaceAll(" ", "");
				token = token.trim();
				if(token.length() == 0)
					continue;
				if (token.matches("\\+?[0-9]+"))
				{
					phoneList.add(token);
				}
			}
		}
		return phoneList;
	}

}
