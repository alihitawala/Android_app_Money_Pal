/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.developer.nita.hisabKitab.adapter;

import com.developer.nita.hisabKitab.R;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.FilterQueryProvider;
import android.widget.Filterable;
import android.widget.TextView;

public class AutoComplete {

    // XXX compiler bug in javac 1.5.0_07-164, we need to implement Filterable
    // to make compilation work
    public static class ContactListAdapter extends CursorAdapter implements Filterable {
        @SuppressWarnings("deprecation")
		public ContactListAdapter(Context context, Cursor c) {
            super(context, c);
            mContent = context.getContentResolver();
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            final LayoutInflater inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(
                    R.layout.auto_suggest_view, parent, false);
            ((TextView)view.findViewById(R.id.contactName)).setText(cursor.getString(COLUMN_DISPLAY_NAME));
            ((TextView)view.findViewById(R.id.contactNumber)).setText(getPhoneNumber(cursor));
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
        	((TextView)view.findViewById(R.id.contactName)).setText(cursor.getString(COLUMN_DISPLAY_NAME));
            ((TextView)view.findViewById(R.id.contactNumber)).setText(getPhoneNumber(cursor));
        }

        @Override
        public String convertToString(Cursor cursor) {
            return cursor.getString(COLUMN_DISPLAY_NAME);
        }

        @Override
        public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
            FilterQueryProvider filter = getFilterQueryProvider();
            if (filter != null) {
                return filter.runQuery(constraint);
            }

            Uri uri = Uri.withAppendedPath(
                    Contacts.CONTENT_FILTER_URI,
                    Uri.encode(constraint.toString()));
            return mContent.query(uri, CONTACT_PROJECTION, null, null, null);
        }

        
        public String getPhoneNumber(Cursor cursor)
        {
        	if (Integer.parseInt(cursor.getString(COLUMN_HAS_PHONE_NUMBER)) > 0) {
                 Cursor pCursor = mContent.query(
    		    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
    		    new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.TYPE}, 
    		    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
    		    new String[]{cursor.getString(COLUMN_ID)}, null);
                String value="";
                boolean flag = true;
    	        while (pCursor.moveToNext()) {
    	        	if(flag)
    	        	{
    	        		flag = false;
    	        	}
    	        	else
    	        	{
    	        		value += SEPERATOR;
    	        	}
    	        	value += pCursor.getString(0);
    	        } 
    	        pCursor.close();
    	        return value;
    	    }
        	return "No Phone number Found";
        }

        private ContentResolver mContent;
    }

    public static final String[] CONTACT_PROJECTION = new String[] {
        Contacts._ID,
        Contacts.DISPLAY_NAME,
        Contacts.HAS_PHONE_NUMBER
    };

    private static final int COLUMN_ID = 0;
    private static final int COLUMN_DISPLAY_NAME = 1;
    private static final int COLUMN_HAS_PHONE_NUMBER = 2;
    private static final String SEPERATOR = ", ";
}