package com.developer.nita.hisabKitab.dialogBox;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.nita.hisabKitab.MainActivity;
import com.developer.nita.hisabKitab.PartnerActivity;
import com.developer.nita.hisabKitab.R;
import com.developer.nita.hisabKitab.adapter.AutoComplete;
import com.developer.nita.hisabKitab.db.Partner;
import com.developer.nita.hisabKitab.utility.EditPartner;

public class EditDialogBox {
	private Context mContext;
	private AlertDialog.Builder builder;
	private EditPartner editPartner;
	private Partner oldPartner;
	public EditDialogBox(Context context, Partner oldPartner)
	{
		this.mContext = context;
		this.oldPartner = oldPartner;
		editPartner = new EditPartner(this.mContext);
		this.initBuilder();
	}
	private void initBuilder() {
		this.builder = new AlertDialog.Builder(mContext);
		LayoutInflater inflater = (LayoutInflater)mContext.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		final View addView = inflater.inflate(R.layout.add, null);
		this.setPreviousValues(addView);
		this.setAutoSuggestField(addView);
		builder.setView(addView);
		builder.setTitle("Edit Partner");
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				AutoCompleteTextView v = (AutoCompleteTextView) addView.findViewById(R.id.title2);
				String name=v.getText().toString();
				if(editPartner.isPresent(name))
				{
					Toast.makeText(mContext, "Name Already Exist!", Toast.LENGTH_LONG).show();
				}
				else if(name.length() >= 3)
				{
					Log.d("Name Recieved to insert ", name);
					RadioGroup radio = (RadioGroup) addView.findViewById(R.id.radioSex);
					int selectedId = radio.getCheckedRadioButtonId();
					String sex = ((RadioButton) addView.findViewById(selectedId)).getText().toString();
					String phoneNumbers = ((EditText)addView.findViewById(R.id.phone)).getText().toString();
					int partnerId = oldPartner.getID();
					editPartner.updatePartnerValues(new Partner(partnerId,name,sex), phoneNumbers);
					refreshActivity(partnerId);
				}
				else
				{
					Toast.makeText(mContext, "New name should be atleast 3 character Long", Toast.LENGTH_LONG).show();
				}
			}
		});
		builder.setNegativeButton("Cancel", null);
	}
	
	public void showDialog()
	{
		this.builder.create();
		this.builder.show();
	}


	private void refreshActivity(int partnerId) {
		Intent intent = new Intent(mContext, PartnerActivity.class);
		intent.putExtra(MainActivity.PARTNER_ID, partnerId);
		mContext.startActivity(intent);
	}
	
	private void setPreviousValues(View addView) {
		AutoCompleteTextView name = (AutoCompleteTextView)addView.findViewById(R.id.title2);
		RadioButton male = (RadioButton)addView.findViewById(R.id.radioMale);
		RadioButton female = (RadioButton)addView.findViewById(R.id.radioFemale);
		name.setHint(this.oldPartner.getName());
		if (oldPartner.getSex().equals("Male"))
		{
			male.setSelected(true);
		}
		else
		{
			female.setSelected(true);
		}
	}
	
	private void setAutoSuggestField(final View addView) {
    	ContentResolver content = this.mContext.getContentResolver();
        Cursor cursor = content.query(Contacts.CONTENT_URI,
                AutoComplete.CONTACT_PROJECTION, null, null, null);
        AutoComplete.ContactListAdapter adapter =
                new AutoComplete.ContactListAdapter(mContext, cursor);

        AutoCompleteTextView textView = (AutoCompleteTextView)
                addView.findViewById(R.id.title2);
        textView.setAdapter(adapter);
        textView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View view,
					int arg2, long arg3) {
				String phoneNumbers = (String) ((TextView)view.findViewById(R.id.contactNumber)).getText();
				((TextView)addView.findViewById(R.id.phone)).setText(phoneNumbers);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				String phoneNumbers = (String) ((TextView)view.findViewById(R.id.contactNumber)).getText();
				((TextView)addView.findViewById(R.id.phone)).setText(phoneNumbers);
			}
		});
	}
}
