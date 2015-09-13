package com.developer.nita.hisabKitab.utility;

import java.util.Iterator;
import java.util.List;

import android.content.Context;

import com.developer.nita.hisabKitab.db.DatabaseHandler;
import com.developer.nita.hisabKitab.db.Partner;
import com.developer.nita.hisabKitab.db.PartnerPhoneNumber;

public class EditPartner {
	private static DatabaseHandler db;
	private Context mContext;
	public EditPartner(Context mContext)
	{
		this.mContext = mContext;
		this.initDatabase();
	}
	
	private void initDatabase() {
		db = DatabaseHandler.getInstance(this.mContext);
	}

	public boolean isPresent(String name)
	{
		return db.isPresent(name);
	}
	public void updatePartnerValues(Partner newPartner, String phoneNumbers)
	{
		db.updatePartner(newPartner);
		//phone update
		db.deleteAllPartnerPhoneNumbers(newPartner.getID());
		List<String> phoneNumberList = PhoneNumberManipulator.getAllPhoneNumbers(phoneNumbers);
		Iterator<String> it = phoneNumberList.iterator();
		while (it.hasNext())
		{
			db.addPartnerPhoneNumber(new PartnerPhoneNumber(newPartner.getID(), it.next()));
		}
	}
}
