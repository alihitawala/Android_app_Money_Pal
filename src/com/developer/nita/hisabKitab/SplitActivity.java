package com.developer.nita.hisabKitab;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.developer.nita.hisabKitab.adapter.InteractiveArrayAdapter;
import com.developer.nita.hisabKitab.db.DatabaseHandler;
import com.developer.nita.hisabKitab.db.Partner;
import com.developer.nita.hisabKitab.db.PartnerDetail;
import com.developer.nita.hisabKitab.domainobject.PartnerCheckModel;
import com.developer.nita.hisabKitab.utility.Utility;

public class SplitActivity extends ListActivity {
	private DatabaseHandler db;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.split_activity);
        initDatabase();//first thing to do
        View splitView = (View)this.findViewById(R.id.split_view);
        ArrayAdapter<PartnerCheckModel> adapter = new InteractiveArrayAdapter(this,
        		this.getModel(), splitView);
        setListAdapter(adapter);
        this.setScreen();
        this.setEventHandler();
    }
	
	private void setScreen()
	{
		TextView text = (TextView) findViewById(R.id.date_split);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String currentDate = dateFormat.format(new Date());
		text.setText(currentDate);
	}

	private void setEventHandler() {
		EditText amountEditText = (EditText) findViewById(R.id.amount_split);
		EditText countMembers = (EditText) findViewById(R.id.split_in);
		Button cancelButton  = (Button) findViewById(R.id.button1);
		Button okButton  = (Button) findViewById(R.id.button2);
		amountEditText.setOnKeyListener(new MyOnKey());
		countMembers.setOnKeyListener(new MyOnKey());
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startMainActivity();
			}
		});
		okButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				collectAndUpdate();
			}
		});
	}
	
	private void collectAndUpdate()
	{
		List<Integer> partnerIds = new ArrayList<Integer>();
		ListAdapter adapter = getListAdapter();
		for (int i=0;i<adapter.getCount();i++)
		{
			PartnerCheckModel model = (PartnerCheckModel) adapter.getItem(i);
			if (model.isSelected())
			{
				partnerIds.add(model.getId());
			}
		}
		if (partnerIds.size() == 0)
		{
			Toast.makeText(this, "Please select atleast one Partner", Toast.LENGTH_SHORT).show();
			return;
		}
		EditText amountEditText = (EditText) findViewById(R.id.amount_split);
		EditText countMembers = (EditText) findViewById(R.id.split_in);
		EditText itemName = (EditText) findViewById(R.id.item_split);
		TextView date = (TextView) findViewById(R.id.date_split);
		if (itemName.getText().toString().length() < 3)
		{
			Toast.makeText(this, "Item name should be atleast 3 char long", Toast.LENGTH_SHORT).show();
			return;
		}
		String amountString = amountEditText.getText().toString();
		if (amountString.length() == 0 )
		{
			Toast.makeText(this, "Please enter the amount in the Amount field", Toast.LENGTH_SHORT).show();
			return;
		}
		String countString = countMembers.getText().toString();
		if (countString.length() == 0)
		{
			Toast.makeText(this, "Please enter the count in the \'Split in\' field", Toast.LENGTH_SHORT).show();
			return;
		}
		Double amount = Double.valueOf(amountString);
		int count = Integer.parseInt(countString);
		if (amount.intValue() == 0 || count == 0)
		{
			Toast.makeText(this, "Amount and Split in can't be 0", Toast.LENGTH_SHORT).show();
			return;
		}
		String itemString = itemName.getText().toString();
		Double zero = 0.0;
		Iterator<Integer> it = partnerIds.iterator();
		Double splitAmount = Utility.roundDouble(amount/count);
		while (it.hasNext())
		{
			int partnerId = it.next();
			db.addPartnerDetail(new PartnerDetail(db.getNextItemId(partnerId), partnerId, itemString, splitAmount, zero, date.getText().toString()));
		}
		Toast.makeText(this, "Items Added succesfully.", Toast.LENGTH_SHORT).show();
		this.startMainActivity();
	}
	
	private void startMainActivity()
	{
		Intent intent = new Intent(this, MainActivity.class);
		NavUtils.navigateUpTo(this, intent);
	}

	private List<PartnerCheckModel> getModel() {
		List<Partner> partners = db.getAllPartners();
		List<PartnerCheckModel> list = new ArrayList<PartnerCheckModel>();
		Iterator<Partner> it = partners.iterator();
		while (it.hasNext())
		{
			Partner partner = it.next();
			list.add(new PartnerCheckModel(partner.getName(), partner.getID()));
		}
		return list;
	}

	public void initDatabase()
    {
    	db = DatabaseHandler.getInstance(getApplicationContext());
    }
	
	private class MyOnKey implements View.OnKeyListener
	{

		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(event.getAction() == KeyEvent.ACTION_UP)
			{
				EditText amountEditText = (EditText) findViewById(R.id.amount_split);
				EditText countMembers = (EditText) findViewById(R.id.split_in);
				TextView perHeadAmount = (TextView) findViewById(R.id.per_head_amount);
				String text = amountEditText.getText().toString();
				String countText = countMembers.getText().toString();
				Double amount = 0.0;
				int count = 0;
				if (text.length() == 0 || countText.length() == 0)
				{
					perHeadAmount.setText("0.0");
					return false;
				}
				amount = Double.parseDouble(text);
				count = Integer.parseInt(countText);
				if (amount == 0 || count == 0)
				{
					perHeadAmount.setText("0.0");
					return false;
				}
				else
				{
					Double perHead = amount/count;
					long value = Math.round(perHead);
					perHeadAmount.setText(String.valueOf(value));
				}
				Log.d("amount updated : ", String.valueOf(amount));
				Log.d("count updated : ", String.valueOf(count));
			}
			return false;
		}
		
	}

}
