package com.developer.nita.hisabKitab.utility;

import android.content.Context;
import com.developer.nita.hisabKitab.db.DatabaseHandler;
import com.developer.nita.hisabKitab.db.PartnerPhoneNumber;

import java.util.List;

/**
 * Created by aliHitawala on 5/25/13.
 */
public class Utility {
    public static DatabaseHandler db;
    public static String getPhoneNumbers(Context context, int partnerId)
    {
        if (db == null)
        {
            db = DatabaseHandler.getInstance(context);
        }
        List<PartnerPhoneNumber> phoneNumbers = db.getAllPhoneNumbers(partnerId);
        String numbers = "";
        int last = phoneNumbers.size();
        for(int i = 0; i < last ; i++)
        {
            numbers += phoneNumbers.get(i).getPhone();
            if (i != last -1)
            {
                numbers += "; ";
            }
        }
        return numbers;
    }

    public static double roundDouble(double d)
    {
        return Math.round(d * 100)/100.0d;
    }
}
