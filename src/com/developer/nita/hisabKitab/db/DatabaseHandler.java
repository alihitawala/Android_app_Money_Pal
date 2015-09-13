package com.developer.nita.hisabKitab.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDoneException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.developer.nita.hisabKitab.utility.Utility;

public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    private static final String DATABASE_NAME = "partnerManager.sqlite";
 
    private static final String TABLE_PARTNERS = "partners";
    private static final String TABLE_PARTNER_DETAIL = "partner_detail";
    private static final String TABLE_PARTNER_PHONE_DETAIL = "partner_phone_detail";
 
    private static final String KEY_ID = "id";
    private static final String FK_KEY_PARTNER_ID = "partnerId";
    private static final String KEY_NAME = "name";
    private static final String KEY_ITEM = "item";
    private static final String KEY_SEX = "sex";
    private static final String KEY_CREDIT = "credit";
    private static final String KEY_DEBIT = "debit";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_DATE = "date";
    private static final DatabaseHandler instance = null;
    private DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    public static DatabaseHandler getInstance(Context context)
    {
    	if(instance == null)
    	{
    		return new DatabaseHandler(context);
    	}
    	else
    	{
    		return instance;
    	}
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	String CREATE_PARTNERS_TABLE = "CREATE TABLE " + TABLE_PARTNERS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_SEX + " TEXT" + ")";
        db.execSQL(CREATE_PARTNERS_TABLE);
        
        String CREATE_PARTNER_DETAIL_TABLE = "CREATE TABLE " + TABLE_PARTNER_DETAIL + "("
                + KEY_ID + " INTEGER," + FK_KEY_PARTNER_ID + " INTEGER,"+ KEY_ITEM + " TEXT,"
                + KEY_CREDIT + " REAL," + KEY_DEBIT + " REAL,"+ KEY_DATE + " TEXT," + " PRIMARY KEY("
                + KEY_ID + "," + FK_KEY_PARTNER_ID + "))";
        db.execSQL(CREATE_PARTNER_DETAIL_TABLE);
        
        String CREATE_PARTNER_PHONE_TABLE = "CREATE TABLE " + TABLE_PARTNER_PHONE_DETAIL + "("
                + FK_KEY_PARTNER_ID + " INTEGER, " + KEY_PHONE + " TEXT," + " PRIMARY KEY("
                + FK_KEY_PARTNER_ID + "," + KEY_PHONE + "))";
        db.execSQL(CREATE_PARTNER_PHONE_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNER_DETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTNER_PHONE_DETAIL);
        // Create tables again
        onCreate(db);
    }
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new contact
    public void addPartner(Partner partner) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ID, partner.getID());
        values.put(KEY_NAME, partner.getName()); // Contact Name
        values.put(KEY_SEX, partner.getSex()); // Contact Phone
 
        // Inserting Row
        db.insert(TABLE_PARTNERS, null, values);
        db.close(); // Closing database connection
    }
    
    public void addPartnerPhoneNumber(PartnerPhoneNumber partnerPhoneNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(FK_KEY_PARTNER_ID, partnerPhoneNumber.getPartnerId());
        values.put(KEY_PHONE, partnerPhoneNumber.getPhone()); // Contact Phone
 
        // Inserting Row
        db.insert(TABLE_PARTNER_PHONE_DETAIL, null, values);
        db.close(); // Closing database connection
    }
    
    public void addPartnerDetail(PartnerDetail partnerDetail) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ID, partnerDetail.getId());
        values.put(FK_KEY_PARTNER_ID, partnerDetail.getPartnerId()); 
        values.put(KEY_ITEM, partnerDetail.getItem()); 
        values.put(KEY_DEBIT, partnerDetail.getDebit());
        values.put(KEY_CREDIT, partnerDetail.getCredit());
        values.put(KEY_DATE, partnerDetail.getDate());
 
        // Inserting Row
        db.insert(TABLE_PARTNER_DETAIL, null, values);
        db.close(); // Closing database connection
    }
 
    public int getNextItemId(int partnerId) {
    	SQLiteDatabase db = this.getReadableDatabase();
    	final SQLiteStatement stmt = db
                .compileStatement("SELECT MAX(" + KEY_ID + ") FROM " + TABLE_PARTNER_DETAIL + " WHERE "+ FK_KEY_PARTNER_ID + " = '" + partnerId +"'" );
    	int id;
    	try
    	{
    		id = (int) stmt.simpleQueryForLong();
    	}
    	catch(SQLiteDoneException e)
    	{
    		id = 0;
    	}
		Log.d("value of item id = ", String.valueOf(id+1));
        return id+1;
	}

	// Getting single contact
    public Partner getPartner(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_PARTNERS, new String[] { KEY_ID,
                KEY_NAME, KEY_SEX }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        else
        	return null;

        Partner partner = new Partner(id,
                cursor.getString(1), cursor.getString(2));
        // return contact
        db.close();
        cursor.close();
        return partner;
    }
    

    public Partner getPartner(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_PARTNERS, new String[] { KEY_ID,
                KEY_NAME, KEY_SEX }, KEY_NAME + "=?",
                new String[] { String.valueOf(name) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        else
        	return null;

        Partner partner = new Partner(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        db.close();
        cursor.close();
        return partner;
    }
    
    public List<PartnerDetail> getPartnerDetails(int  partnerId) {
    	SQLiteDatabase db = this.getWritableDatabase();
        
        List<PartnerDetail> details = new ArrayList<PartnerDetail>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PARTNER_DETAIL+ " WHERE " + FK_KEY_PARTNER_ID + " = \"" + partnerId+ "\"";
        
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PartnerDetail detail = new PartnerDetail(partnerId,"",0.0,0.0,"");
                detail.setId(Integer.parseInt(cursor.getString(0)));
                detail.setPartnerId(Integer.parseInt(cursor.getString(1)));
                detail.setItem(cursor.getString(2));
                detail.setCredit(cursor.getDouble(3));
                detail.setDebit(cursor.getDouble(4));
                detail.setDate(cursor.getString(5));
                // Adding contact to list
                details.add(detail);
            } while (cursor.moveToNext());
        }
        db.close();
        if (cursor != null)
            cursor.close();
        return details;
    }
    
    public List<PartnerPhoneNumber> getAllPhoneNumbers(int  partnerId) {
    	SQLiteDatabase db = this.getWritableDatabase();
        
        List<PartnerPhoneNumber> phoneNumbers = new ArrayList<PartnerPhoneNumber>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_PARTNER_PHONE_DETAIL + " WHERE " + FK_KEY_PARTNER_ID + " = \"" + partnerId+ "\"";
        
        Cursor cursor = db.rawQuery(selectQuery, null);
        
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PartnerPhoneNumber phoneNumber = new PartnerPhoneNumber(partnerId,"");
                phoneNumber.setPhone(cursor.getString(1));
                phoneNumbers.add(phoneNumber);
            } while (cursor.moveToNext());
        }
        db.close();
        if (cursor != null)
            cursor.close();
        return phoneNumbers;
    }
    
    public double getTotalAmount(int partnerId)
    {
    	double amount = 0.0;
    	SQLiteDatabase db = this.getWritableDatabase();
        // Select All Query
        Cursor cursor = db.query(TABLE_PARTNER_DETAIL, new String[]{KEY_DEBIT,
                KEY_CREDIT}, FK_KEY_PARTNER_ID + "=?",
                new String[]{String.valueOf(partnerId)}, null, null, null, null);
        
        // looping through all rows and adding 
        if (cursor.moveToFirst()) {
            do {
                amount += cursor.getDouble(0);
                amount -= cursor.getDouble(1);
            } while (cursor.moveToNext());
        }
        db.close();
        if (cursor != null)
            cursor.close();
        return Utility.roundDouble(amount);
    }
    // Getting All Contacts
    public ArrayList<Partner> getAllPartners() {
        ArrayList<Partner> contactList = new ArrayList<Partner>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_PARTNERS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Partner partner = new Partner();
                partner.setID(Integer.parseInt(cursor.getString(0)));
                partner.setName(cursor.getString(1));
                partner.setSex(cursor.getString(2));
                // Adding contact to list
                contactList.add(partner);
            } while (cursor.moveToNext());
        }
        db.close();
        if (cursor != null)
            cursor.close();
        // return contact list
        return contactList;
    }
 
    // Updating single contact
    public int updatePartner(Partner contact) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_SEX, contact.getSex());
 
        // updating row
        int result = db.update(TABLE_PARTNERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
        return result;
    }
 
 // Deleting single contact
//    public void deletePartner(Partner contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_PARTNERS, KEY_ID + " = ?",
//                new String[] { String.valueOf(contact.getID()) });
//        db.close();
//    }
    // Deleting single contact
    public void deletePartner(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PARTNERS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
        this.deleteAllPartnerDetails(id);
        this.deleteAllPartnerPhoneNumbers(id);
    }
    
    public void deleteAllPartnerPhoneNumbers(int partnerId) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_PARTNER_PHONE_DETAIL, FK_KEY_PARTNER_ID + " = \'"+partnerId+"\'",
                null);
        db.close();
    }

	public void deletePartnerDetail(int partnerId, int itemId) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_PARTNER_DETAIL, KEY_ID + " = \'"+itemId+"\' AND "+ FK_KEY_PARTNER_ID + " = \'"+partnerId+"\'",
                null);
        db.close();
    }
    
    public void deleteAllPartnerDetails(int partnerId) {
    	SQLiteDatabase db = this.getWritableDatabase();
    	db.delete(TABLE_PARTNER_DETAIL, FK_KEY_PARTNER_ID + " = \'"+partnerId+"\'",
                null);
        db.close();
    }
    
    public boolean isPresent(String name)
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    	 
        Cursor cursor = db.query(TABLE_PARTNERS, new String[] { KEY_ID,
                KEY_NAME, KEY_SEX }, KEY_NAME + "=?",
                new String[] { name }, null, null, null, null);
        boolean isPresent = false;
        if (cursor != null)
        {
        	if(cursor.getCount() > 0)
        	{
        		Log.d("Number of partner already", "more than zero");
        		isPresent = true;
        	}
        	else
        	{
        		Log.d("Number of partner already", "zero");
        	}
            cursor.close();
        }
        else
        {
    		Log.d("Number of partner already", "cursor is null");
        }
        db.close();
        return isPresent;
    }
 
    // Getting contacts Count
    public int getLastPartnerId() {
        String maxQuery = "SELECT  max( " + KEY_ID +  " ) FROM " + TABLE_PARTNERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(maxQuery, null);
        int index = 0;
        if(cursor != null && cursor.moveToFirst())
        {
        	index = cursor.getInt(0);
        	cursor.close();
        }
        return index;
    }

	public boolean isPersonMale(String partnerName) {
   	 
        String genderQuery = "SELECT " + KEY_SEX + " FROM " + TABLE_PARTNERS + " WHERE "+ KEY_NAME + " = \'" + partnerName +"\'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PARTNERS, new String[] { KEY_SEX }, KEY_NAME + "=?",
                new String[] { partnerName }, null, null, null, null);
        boolean isMale = false;
        if (cursor.moveToFirst()) {
            isMale =  "Male".equals(cursor.getString(0));
        }
        db.close();
        if (cursor != null) {
            cursor.close();
        }
        return isMale;
	}

	public PartnerDetail getPartnerDetail(int partnerId, int itemId) {
		String selectQuery = "SELECT * FROM " + TABLE_PARTNER_DETAIL + " WHERE "
						+ FK_KEY_PARTNER_ID + " = \'" + partnerId +"\' and " +
						KEY_ID + " = \'" + itemId +"\'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        PartnerDetail result = null;
        if (cursor.moveToFirst()) {
        	result =  new PartnerDetail(itemId, partnerId, cursor.getString(2), cursor.getDouble(3), cursor.getDouble(4), cursor.getString(5));
        }
        db.close();
        if (cursor != null)
            cursor.close();
        return result;
	}
	
//	public void updatePartner(Partner partner)
//	{
//		String updateQuery = "UPDATE " + TABLE_PARTNERS + " SET " + KEY_NAME + " = \'" + partner.getName() + "\', " +
//				KEY_SEX + " = \'" + partner.getSex() + "\' "+ " WHERE "+ KEY_ID + " = " + partner.getID();
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(updateQuery, null);
//	}
}
