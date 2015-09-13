package com.developer.nita.hisabKitab;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.developer.nita.hisabKitab.StringManipulator.StringManipulator;
import com.developer.nita.hisabKitab.date.DateHandler;
import com.developer.nita.hisabKitab.db.DatabaseHandler;
import com.developer.nita.hisabKitab.db.Partner;
import com.developer.nita.hisabKitab.db.PartnerDetail;
import com.developer.nita.hisabKitab.dialogBox.EditDialogBox;
import com.developer.nita.hisabKitab.dialogBox.HelpDialogBox;
import com.developer.nita.hisabKitab.dialogBox.itemInfoDialogBox;
import com.developer.nita.hisabKitab.utility.Utility;

public class PartnerActivity extends Activity {

	public int partnerId;
	public Partner partner;
	private DatabaseHandler db;
	private TableLayout mainLayout;
	private boolean rowFlag=true;
	public final static String PARTNER_ID = "com.developer.nita.hisabKitab.partnerDetail.PARTNERID";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);
        initDatabase();//first thing to do
        getInfoFromPreviousActivity();
        //getActionBar().setDisplayHomeAsUpEnabled(true);
        mainLayout = (TableLayout) findViewById(R.id.main_table);
        this.setScreen();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    public void initDatabase()
    {
    	db = DatabaseHandler.getInstance(getApplicationContext());
    }
    
    public void setScreen()
    {
    	this.setScreenName();
    	this.setScreenTotalAmount();
    	this.populateRows();
	}

	private void setScreenName(){
    	TextView screenName = (TextView)findViewById(R.id.name_text);
    	screenName.setText(partner.getName());
    }
    
    private void setScreenTotalAmount()
    {
    	double totalAmount = db.getTotalAmount(partnerId);
    	TextView screenAmount = (TextView)findViewById(R.id.total_amount_text);
    	screenAmount.setText(StringManipulator.getDescriptionHeaderPartnerActivity(db.isPersonMale(partner.getName()), totalAmount));
    }

	private void populateRows() {
		List<PartnerDetail> details = db.getPartnerDetails(partnerId);
		Iterator<PartnerDetail> it = details.iterator();
		this.setRowFlag(true);
		while(it.hasNext())
		{
			PartnerDetail detail = it.next();
			if(mainLayout != null)
			{
				this.addOnScreen(detail);
			}
		}
	}
	
	public boolean isRowFlag() {
		return rowFlag;
	}

	public void setRowFlag(boolean rowFlag) {
		this.rowFlag = rowFlag;
	}
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
//        AdapterView.AdapterContextMenuInfo info;
        super.onCreateContextMenu(menu, view, menuInfo);
        try {
        	menu.setHeaderTitle("Options");
            Log.d("View id = ", String.valueOf(view.getId()));
        	menu.add(1, view.getId(), 1, "View");
        	menu.add(1, view.getId(), 2, "Delete");
        	menu.add(1, view.getId(), 3, "Cancel");
        } catch (ClassCastException e) {
            Log.e("ContextMenuError", "bad menuInfo", e);
            return;
        }
    } 
	
	@Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        int menuId = item.getOrder();
        switch(menuId)
        {
        	case 1:
        		this.viewItemInDisplayBox(itemId);
        		return true;
        	case 2:
        		this.delete(itemId);
        		Toast.makeText(this, String.valueOf(itemId) , Toast.LENGTH_LONG).show();
        		return true;
        	default:
        		return false;
        		
        }
    }
	
	private void viewItemInDisplayBox(int itemId) {
		PartnerDetail itemdetail = db.getPartnerDetail(partnerId, itemId);
		itemInfoDialogBox dialog = new itemInfoDialogBox(PartnerActivity.this, itemdetail, partner.getName());
		dialog.show();
	}

	private void delete(int itemId) {
		db.deletePartnerDetail(partnerId, itemId);
		this.setScreenTotalAmount();
		this.deleteRowFromScreen(itemId);
	}

	private void deleteRowFromScreen(int itemId) {
		mainLayout.removeView(mainLayout.findViewById(itemId));
	}

	@SuppressWarnings("deprecation")
	private void addOnScreen(final PartnerDetail detail)
	{
		TableLayout tl = mainLayout;
		TableRow tr = new TableRow(this);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		tr.setLayoutParams(lp);
		tr.setId(detail.getId());
        Log.d("Row id = ", String.valueOf(detail.getId()));
		tr.setLongClickable(true);
		tr.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				viewItemInDisplayBox(detail.getId());
			}
		});
		registerForContextMenu(tr);
		if(this.isRowFlag())
		{
			tr.setBackgroundColor(Color.GREEN);
		}
		else
		{
			tr.setBackgroundColor(Color.WHITE);
		}
		tr.setPadding(5, 5, 5, 5);
		TextView tvLeft = this.getCellOnScreen(detail.getItem());
		TextView tvCenterLeft = this.getCellOnScreen(Double.toString(detail.getCredit()));
		TextView tvCenterRight = this.getCellOnScreen(Double.toString(detail.getDebit()));
		TextView tvRight = this.getCellOnScreen(new DateHandler(detail.getDate()).getDate());
		tr.addView(tvLeft);
		tr.addView(tvCenterLeft);
		tr.addView(tvCenterRight);
		tr.addView(tvRight);
		tl.addView(tr, new TableLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		this.setRowFlag(!this.isRowFlag());
	}

	@SuppressWarnings("deprecation")
	private TextView getCellOnScreen(String text)
	{
		TextView textView= new TextView(this);
		LayoutParams lp = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.CENTER;
		textView.setLayoutParams(lp);
		textView.setTextColor(Color.BLACK);
		textView.setPadding(5, 5, 5, 5);
		textView.setText(text);
		return textView;
	}
    
    public void getInfoFromPreviousActivity()
    {
    	Intent intent = getIntent();
        partnerId = intent.getIntExtra(MainActivity.PARTNER_ID, -1);
        Log.d("Partner Id received :: ", String.valueOf(partnerId));
        partner = db.getPartner(partnerId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_partner, menu);
		initMenuEditorAction(menu);
        return (super.onCreateOptionsMenu(menu));
    }
    
    private void initDateValue(View view)
 {
		TextView dateToday = null;
		if (view != null) {
			dateToday = (TextView) view.findViewById(R.id.date_view);
		}
		else{
			Log.d("Current Date", "View is Null");
		}
		if (dateToday != null) {
			setTodayDate(dateToday);
		}
		else{
			Log.d("Current Date", "Text View is Null");
		}
    }
    
    private void setTodayDate(TextView dateToday)
    {
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String currentDate = dateFormat.format(new Date());
    	Log.d("Current Date", currentDate);
    	dateToday.setText(currentDate);
    }
    
	public void initMenuEditorAction(Menu menu) {
		EditText addDetail = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			View v = menu.findItem(R.id.addDetail).getActionView();
			if (v != null) {
				addDetail = (EditText) v.findViewById(R.id.edit_item);
			}
		}
		if (addDetail != null) {
			addDetail.setOnEditorActionListener(keyboardAction);
		}
	}

	private TextView.OnEditorActionListener keyboardAction = new OnEditorActionListener() {

		@Override
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (event == null || event.getAction() == KeyEvent.ACTION_UP) {
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}
			return true;
		}
	};

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
			case R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.addDetail:
				this.addDetail();
				return true;
			case R.id.share:
				this.startNextActivity();
				return true;
			case R.id.edit_partner:
				this.initEditDialogBox();
				return true;
			case R.id.about:
                Toast.makeText(this, R.string.about_message, Toast.LENGTH_LONG)
                        .show();
                return (true);
            case R.id.help:
                this.showHelpDialogBox();
                return true;
		}
		return super.onOptionsItemSelected(item);
	}

    private void showHelpDialogBox() {
        HelpDialogBox dialog;
        String text = "<html><body>" +
                "<p align=\"justify\">" +
                "<b><u>Partner Page</u></b><br/>" +
                "It has all details about transactions you had with your partner. Add all the transaction with respect to you." +
                "If you have given 20 for X which was supposed to be given by your partner then add X, you gave 20. And if your partner" +
                "gave on your behalf add Y, partner gave.<br/><br/>" +
                "<b>What all you can do from here,</b>" +
                "<ul>" +
                "   <li>Add a transaction</li>" +
                "   <li>Share</li>" +
                "   <li>Edit this Partner</li>" +
                "</ul>"
                + "</p> "
                + "</body></html>";
        dialog = new HelpDialogBox(this, text);
        dialog.show();
    }

    private void initEditDialogBox() {
		EditDialogBox dialogBox = new EditDialogBox(PartnerActivity.this, this.partner);
		dialogBox.showDialog();
	}

	private void startNextActivity() {
		Intent intent = new Intent(this, ShareActivity.class);
		Log.d("Id passed to parent tab :: ", String.valueOf(partnerId));
		intent.putExtra(PARTNER_ID, partnerId);
		startActivity(intent);
		
	}

	private void addItemDetails(PartnerDetail partnerDetail) {
		db.addPartnerDetail(partnerDetail);
		Log.d("Details Insertion ", "On screen");
		this.addOnScreen(partnerDetail);
		this.setScreenTotalAmount();
//		addInList(partner);
//		refresh();
//		Toast.makeText(getApplicationContext(), "Partner Added Successfully", Toast.LENGTH_SHORT).show();
		
	}
	
	private void initDialogBox(View view)
	{
		this.initDateValue(view);
	}
	
	private void addDetail() {
		final View addView = getLayoutInflater().inflate(R.layout.add_detail, null);
		new AlertDialog.Builder(this).setTitle("Add Transaction").setView(addView)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						EditText v = (EditText) addView.findViewById(R.id.edit_item);
						String item=v.getText().toString();
						EditText youGave = (EditText) addView.findViewById(R.id.you_edit_text);
						String youGaveString = youGave.getText().toString();
						EditText heGave = (EditText) addView.findViewById(R.id.he_edit_text);
						String heGaveString = heGave.getText().toString();
						if("".equals(heGaveString))
						{
							heGaveString = "0";
						}
						if("".equals(youGaveString))
						{
							youGaveString = "0";
						}
						if(heGaveString.equals("0") && youGaveString.equals("0"))
						{
							Toast.makeText(getApplicationContext(), "Invalid Transaction!", Toast.LENGTH_LONG).show();
						}
						else if(item.length() >= 3)
						{
							Log.d("item Recieved to insert ", item);

							double you = Utility.roundDouble(Double.valueOf(youGaveString));
							double he = Utility.roundDouble(Double.valueOf(heGaveString));
							TextView dateText = (TextView) addView.findViewById(R.id.date_view);
							String currentDate = (String) dateText.getText();
							Log.d("Details Insertion", "Values "+partner.getName()+" "+item+" "+Double.toString(you)+" "+Double.toString(he)+" "+currentDate);
							addItemDetails(new PartnerDetail(db.getNextItemId(partnerId), partnerId, item, you, he, currentDate));
						}
						else
						{
							Toast.makeText(getApplicationContext(), "Item should be atleast 3 character Long", Toast.LENGTH_LONG).show();
						}
					}
				}).setNegativeButton("Cancel", null).show();
		this.initDialogBox(addView);
	}
}
