package com.developer.nita.hisabKitab;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.developer.nita.hisabKitab.StringManipulator.StringManipulator;
import com.developer.nita.hisabKitab.adapter.AutoComplete;
import com.developer.nita.hisabKitab.adapter.MainListAdapter;
import com.developer.nita.hisabKitab.db.DatabaseHandler;
import com.developer.nita.hisabKitab.db.Partner;
import com.developer.nita.hisabKitab.db.PartnerPhoneNumber;
import com.developer.nita.hisabKitab.dialogBox.HelpDialogBox;
import com.developer.nita.hisabKitab.utility.PhoneNumberManipulator;
import com.developer.nita.hisabKitab.utility.Utility;

public class MainActivity extends ListActivity {
	public DatabaseHandler db;
	public ArrayList<Partner> list = new ArrayList<Partner>();
	public MainListAdapter adapter;
	public final static String PARTNER_ID = "com.developer.nita.hisabKitab.PARTNERID";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
		initDatabase();//very first initialize database
        initListAdapter();
        initMainHeader();
        initAttachListenerOnList();
        registerContextWithList();
    }

    private void initMainHeader() {
        double total = 0;
        double sumTotal = 0;
        for (Partner partner : list)
        {
            total = partner.getTotalAmount() == 0 ? db.getTotalAmount(partner.getID()) : partner.getTotalAmount();
            partner.setTotalAmount(total);
            sumTotal += total;
        }
        TextView mainHeader = (TextView) findViewById(R.id.main_header);
        mainHeader.setText(StringManipulator.getMainHeaderText(Utility.roundDouble(sumTotal), list.size()));
    }

    private void initAttachListenerOnList() {
    	final ListView lv = getListView();
		lv.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int i,
					long arg3) {
				Partner partner = (Partner) lv.getItemAtPosition(i);
				createNewActivity(partner.getID());
			}
		});
	}

	private void createNewActivity(int partnerId) {
		Intent intent = new Intent(this, PartnerActivity.class);
		Log.d("Id passed :: ", String.valueOf(partnerId));
		intent.putExtra(PARTNER_ID, partnerId);
		startActivity(intent);
		
	}
    
	private void registerContextWithList() {
    	ListView v = getListView();
    	registerForContextMenu(v);
	}
	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
	//        AdapterView.AdapterContextMenuInfo info;
	    super.onCreateContextMenu(menu, view, menuInfo);
	    try {
	    	menu.setHeaderTitle("Options");
	    	menu.add(1, 0, 1, "Open");
	    	menu.add(1, 1, 2, "Edit");
	    	menu.add(1, 2, 3, "Delete");
	    	menu.add(1, 3, 4, "Cancel");
	    } catch (ClassCastException e) {
	        Log.e("ContextMenuError", "bad menuInfo", e);
	        return;
	    }
	} 
	
	@Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            Log.e("id from context ", "bad menuInfo", e);
            return false;
        }
        Partner p = (Partner) getListAdapter().getItem(info.position);
        int menuId = item.getItemId();
        switch(menuId)
        {
        	case 0:
        		createNewActivity(p.getID());
        		Log.d("OPEN", p.getName()) ;
        		return true;
        	case 2:
        		delete(p);
        		Log.d("DELETE", p.getName()) ;
        		return true;
        	default:
        		return false;
        		
        }
    }

	private void delete(Partner p) {
		db.deletePartner(p.getID());
		deleteInList(p);
        initMainHeader();
		refresh();
        Toast.makeText(this, "Partner Deleted :: " + p.getName(), Toast.LENGTH_SHORT).show();
	}
	
	private void deleteInList(Partner p) {
		list.remove(p);
	}
	
	private void initListAdapter() {
        list = db.getAllPartners();
    	adapter = new MainListAdapter(getApplicationContext(), list);//<String>(getApplicationContext(), R.layout.activity_main,R.id.label,list);
        setListAdapter(adapter);
		refresh();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.activity_main, menu);
		initMenuEditorAction(menu);
		return (super.onCreateOptionsMenu(menu));
	}
	
    public void initDatabase()
    {
    	db = DatabaseHandler.getInstance(getApplicationContext());
    }
    
    public void initMenuEditorAction(Menu menu)
    {
    	AutoCompleteTextView add = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			View v = menu.findItem(R.id.add).getActionView();
			if (v != null) {
				add = (AutoCompleteTextView) v.findViewById(R.id.title2);
			} 
		}
		if (add != null) {
			add.setOnEditorActionListener(keyboardAction);
		}
    }
    private TextView.OnEditorActionListener keyboardAction = new OnEditorActionListener() {
		
		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if(event == null || event.getAction() == KeyEvent.ACTION_UP)
			{
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
			return true;
		}
	};
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add:
			add();
			return (true);
		case R.id.split_main:
			this.startSplitActivity();
			return true;
		case R.id.about:
			Toast.makeText(this, R.string.about_message, Toast.LENGTH_LONG)
					.show();
			return (true);
		case R.id.help:
            this.showHelpDialogBox();
			return true;
		}

		return (super.onOptionsItemSelected(item));
	}

    private void showHelpDialogBox() {
        HelpDialogBox dialog;
        String text = "<html><body>" +
                "<p align=\"justify\">" +
                "<b><u>Main Page</u></b><br/>" +
                "It has all your partners at one place, click any partner to see more detail about your transactions.<br/><br/>" +
                "<b>What all you can do from here,</b>" +
                "<ul>" +
                "   <li>Add a Partner</li>" +
                "   <li>Add a Split Bill, for one to many transactions</li>" +
                "</ul>"
                + "</p> "
                + "</body></html>";
        dialog = new HelpDialogBox(this, text);
        dialog.show();
    }

    private void startSplitActivity() {
		Intent intent = new Intent(this, SplitActivity.class);
		startActivity(intent);
	}

	public void refresh()
	{
		adapter.notifyDataSetChanged();
	}
	
	private void addDetails(Partner partner) {
		db.addPartner(partner);
		addInList(partner);
	}
	
	private void addInList(Partner partner) {
		list.add(partner);
        initMainHeader();
		Log.d("id added in addinList method", String.valueOf(partner.getID()));
		refresh();
	}

	private void add() {
		final View addView = getLayoutInflater().inflate(R.layout.add, null);
		this.setAutoSuggestField(addView);
		new AlertDialog.Builder(this).setTitle("Add your Partner").setView(addView)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						AutoCompleteTextView v = (AutoCompleteTextView) addView.findViewById(R.id.title2);
						String name=v.getText().toString();
						if(isPresent(name))
						{
							Toast.makeText(getApplicationContext(), "Name Already Exist!", Toast.LENGTH_LONG).show();
						}
						else if(name.length() >= 3)
						{
							Log.d("Name Recieved to insert ", name);
							RadioGroup radio = (RadioGroup) addView.findViewById(R.id.radioSex);
							int selectedId = radio.getCheckedRadioButtonId();
							String sex = ((RadioButton) addView.findViewById(selectedId)).getText().toString();
							String phoneNumbers = ((EditText)addView.findViewById(R.id.phone)).getText().toString();
							int partnerId = db.getLastPartnerId() + 1;
							addDetails(new Partner(partnerId, name, sex));
							addPhoneNumbers(partnerId, phoneNumbers);
						}
						else
						{
							Toast.makeText(getApplicationContext(), "Name should be atleast 3 character Long", Toast.LENGTH_LONG).show();
						}
					}
				}).setNegativeButton("Cancel", null).show();
	}


	private void addPhoneNumbers(int partnerId,
			String phoneNumbers) {
		List<String> list = PhoneNumberManipulator.getAllPhoneNumbers(phoneNumbers);
		Iterator<String> it = list.iterator();
		while(it.hasNext())
		{
			String phoneNumber = it.next();
			db.addPartnerPhoneNumber(new PartnerPhoneNumber(partnerId, phoneNumber));
			Log.d("Number Added :: ", phoneNumber);
		}
	}
	
	private void setAutoSuggestField(final View addView) {
    	ContentResolver content = this.getContentResolver();
        Cursor cursor = content.query(Contacts.CONTENT_URI,
                AutoComplete.CONTACT_PROJECTION, null, null, null);
        AutoComplete.ContactListAdapter adapter =
                new AutoComplete.ContactListAdapter(this, cursor);

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


	public boolean isPresent(String name) {
		return db.isPresent(name);
	}
}