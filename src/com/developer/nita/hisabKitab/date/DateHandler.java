package com.developer.nita.hisabKitab.date;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateHandler {
	private String date;
	private int year;
	private String month;
	//private int intMonth;
	private int dayOfMonth;
	private int hour;
	private int hourOfDay;
	private String minute;
	//private String second;
	
	public DateHandler(String date)
	{
		this.date = date;
		initCalculation();
	}
	
	private void initCalculation()
	{
		Date date;
		Calendar calendar = new GregorianCalendar();
		try {
			date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(this.date);
			calendar.setTime(date);
			this.year = calendar.get(Calendar.YEAR);
			this.month = new DateFormatSymbols().getMonths()[calendar.get(Calendar.MONTH)];
			this.dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
			this.hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
			this.hour = calendar.get(Calendar.HOUR);
			this.minute = this.correctInt(calendar.get(Calendar.MINUTE));
			//this.second = this.correctInt(calendar.get(Calendar.SECOND));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getDate()
	{
		return this.dayOfMonth + " " + this.month;
	}
	
	public String getTime()
	{
		String suffix;
		if (this.hourOfDay > 11)
		{
			suffix = "PM";
		}
		else
		{
			suffix = "AM";
		}
		return this.hour + ":" + this.minute + " " +suffix;
	}
	
	public String getDetailedDate()
	{
		return this.getDate() + ", " + this.year;
	}
	
	public String correctInt(int value)
	{
		if (value < 10)
		{
			return "0"+value;
		}
		return String.valueOf(value);
	}
}
