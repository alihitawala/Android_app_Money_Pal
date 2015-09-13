package com.developer.nita.hisabKitab.StringManipulator;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import android.content.Context;

import com.developer.nita.hisabKitab.date.DateHandler;
import com.developer.nita.hisabKitab.db.DatabaseHandler;
import com.developer.nita.hisabKitab.db.Partner;
import com.developer.nita.hisabKitab.db.PartnerDetail;

public class StringManipulator {
	private int partnerId;
	private Partner partner;
	private Context mContext;
	DatabaseHandler db;
	public static enum LENGTH
	{
		SMALL,
		MEDIUM,
		LARGE
	}
	
	private StringManipulator(){
	}

	public StringManipulator(int partnerId, Context mContext)
	{
		this();
		this.partnerId = partnerId;
		this.mContext = mContext;
		this.initDatabase();
		this.partner = db.getPartner(this.partnerId);
	}
	
    public void initDatabase()
    {
    	db = DatabaseHandler.getInstance(this.mContext);
    }
    
    private List<PartnerDetail> getPartnerDetials()
    {
    	return db.getPartnerDetails(this.partnerId);
    }
    
	public String getDetailedDescription(LENGTH length)
	{
		List<PartnerDetail> partnerDetails = this.getPartnerDetials();
		String msg = this.getBasicMessageString();
		if(partnerDetails.size() == 0)
			return msg;
		//medium
		if(length == LENGTH.MEDIUM)
		{
			msg += this.getIndexForDescription();
			Iterator<PartnerDetail> it = partnerDetails.iterator();
			while(it.hasNext())
			{
				PartnerDetail temp = it.next();
				double iGave = temp.getCredit();
				double youGave = temp.getDebit();
				msg += temp.getItem() + " (";
				if(iGave != 0.0)//I gave
				{
					msg += "-" + String.valueOf(iGave);
					if(youGave != 0.0)
					{
						msg += ", ";
					}
				}
				if(youGave != 0.0)
				{
					msg += "+" + String.valueOf(youGave);
				}
				msg += ")\n";
			}
		}
		//large
		if(length == LENGTH.LARGE)
		{
			msg += this.getIndexForDescription();
			Iterator<PartnerDetail> it = partnerDetails.iterator();
			while(it.hasNext())
			{
				PartnerDetail temp = it.next();
				double iGave = temp.getCredit();
				double youGave = temp.getDebit();
				msg += temp.getItem() + " (";
				if(iGave != 0.0)//I gave
				{
					msg += "-" + String.valueOf(iGave);
					if(youGave != 0.0)
					{
						msg += ", ";
					}
				}
				if(youGave != 0.0)
				{
					msg += "+" + String.valueOf(youGave);
				}
				msg += ") on :: ";
				DateHandler localHandler = new DateHandler(temp.getDate());
				msg += localHandler.getDate();
				msg += "\n";
			}
		}
		return msg;
	}
	
	private String getBasicMessageString()
	{
		double totalAmount = db.getTotalAmount(this.partnerId);
		String msg = "";
		StringTokenizer tokeniser = new StringTokenizer(this.partner.getName(), "\\ ");
		String firstName = tokeniser.nextToken();
		msg += "Hi " + firstName +",\nHow are you?\n"; 
		if(totalAmount > 0)
		{
			msg += "I owe you sum total of " + String.valueOf(totalAmount);
		}
		else if (totalAmount < 0)
		{
			msg += "You owe me sum total of " + String.valueOf(Math.abs(totalAmount));
		}
		else
		{
			msg += "We are now clear as far as money is concerned";
		}
		return msg;
	}
	
	private String getIndexForDescription()
	{
		String msg = "\n\'+\' implies you gave, \'-\' implies I gave\n";
		return msg;
	}
	
	public static String getDescriptionHeaderPartnerActivity(boolean isMale, double amount)
	{
		String string = "";
		if(amount > 0)
		{
			string += "You owe ";
			string += isMale?"him ":"her "; 
		}
		else if (amount < 0)
		{
			string +=isMale?"He ":"She ";
			string +="owes you ";
		}
		else
		{
			string += "Account clear!";
			return string;
		}
		return string + String.valueOf(Math.abs(amount));
	}

	public static String getMainHeaderText(double amount, int size)
    {
        String plural = size > 1 ? "s" :"";
        if (size == 0)
        {
            return "Welcome!";
        }
		else if(amount > 0)
		{
			return "You owe total of " + String.valueOf(Math.abs(amount)) + " to your friend" + plural;
		}
		else if (amount < 0)
		{
            return "Your friend" + plural +" owe you total of " + String.valueOf(Math.abs(amount));
		}
        else
        {
            return "Your account is at par!";
        }
	}
}
