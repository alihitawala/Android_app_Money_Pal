package com.developer.nita.hisabKitab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.developer.nita.hisabKitab.StringManipulator.StringManipulator;
import com.developer.nita.hisabKitab.StringManipulator.StringManipulator.LENGTH;
import com.developer.nita.hisabKitab.db.DatabaseHandler;
import com.developer.nita.hisabKitab.db.Partner;
import com.developer.nita.hisabKitab.utility.Utility;


public class ShareActivity extends Activity {
    /** Tag string for our debug logs */
	private int partnerId;
	private Partner partner;
	private DatabaseHandler db;
	private StringManipulator stringManipulator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.sms_demo);
        this.initDatabase();
        getInfoFromPreviousActivity();
        
        stringManipulator = new StringManipulator(partnerId, ShareActivity.this);

        // Enable or disable the broadcast receiver depending on the checked
        // state of the checkbox.
//        CheckBox enableCheckBox = (CheckBox) findViewById(R.id.sms_enable_receiver);
//
//        final PackageManager pm = this.getPackageManager();
//        final ComponentName componentName = new ComponentName("com.developer.nita.hisabKitab",
//                "com.developer.nita.hisabKitab.SmsMessageReceiver");
//
//        enableCheckBox.setChecked(pm.getComponentEnabledSetting(componentName) ==
//                                  PackageManager.COMPONENT_ENABLED_STATE_ENABLED);
//
//        enableCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                Log.d(TAG, (isChecked ? "Enabling" : "Disabling") + " SMS receiver");
//
//                pm.setComponentEnabledSetting(componentName,
//                        isChecked ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
//                                : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                        PackageManager.DONT_KILL_APP);
//            }
//        });
        final RadioGroup lengthSelection = (RadioGroup) ShareActivity.this
                .findViewById(R.id.sms_length);
        lengthSelection.requestFocus();
        final EditText contentTextEdit = (EditText) ShareActivity.this
                .findViewById(R.id.sms_content);
        
        contentTextEdit.setText(stringManipulator.getDetailedDescription(LENGTH.MEDIUM));
        
        lengthSelection.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch(checkedId)
				{
					case R.id.small_radio:
						contentTextEdit.setText(stringManipulator.getDetailedDescription(LENGTH.SMALL));
						break;
					case R.id.medium_radio:
						contentTextEdit.setText(stringManipulator.getDetailedDescription(LENGTH.MEDIUM));
						break;
					case R.id.large_radio:
						contentTextEdit.setText(stringManipulator.getDetailedDescription(LENGTH.LARGE));
						break;
				}
			}
		});
        Button shareButton = (Button) findViewById(R.id.share_button);
        shareButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final EditText contentTextEdit = (EditText) ShareActivity.this
                        .findViewById(R.id.sms_content);
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                waIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                String text = contentTextEdit.getText().toString();
                if (waIntent != null) {
                    waIntent.putExtra(Intent.EXTRA_TEXT, text);//
                    startActivity(Intent.createChooser(waIntent, "Share with"));
                } else {
                    Toast.makeText(getApplicationContext(), "Error encountered!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button smsButton = (Button) findViewById(R.id.sms_button);
        smsButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                final EditText contentTextEdit = (EditText) ShareActivity.this
                        .findViewById(R.id.sms_content);
                Intent smsIntent = new Intent(Intent.ACTION_VIEW);
                smsIntent.setType("vnd.android-dir/mms-sms");
                smsIntent.putExtra("address", Utility.getPhoneNumbers(getApplicationContext(), partnerId));
                smsIntent.putExtra("sms_body", contentTextEdit.getText().toString());
                startActivity(smsIntent);
            }
        });
        // Watch for send button clicks and send text messages.
//        Button shareButton = (Button) findViewById(R.id.sms_send_message);
//        shareButton.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                if (TextUtils.isEmpty(recipientTextEdit.getText())) {
//                    Toast.makeText(ShareActivity.this, "Please enter a message recipient.",
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (TextUtils.isEmpty(contentTextEdit.getText())) {
//                    Toast.makeText(ShareActivity.this, "Please enter a message body.",
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                recipientTextEdit.setEnabled(false);
//                contentTextEdit.setEnabled(false);
//
//                SmsManager sms = SmsManager.getDefault();
//
//                List<String> messages = sms.divideMessage(contentTextEdit.getText().toString());
//
//                String recipient = recipientTextEdit.getText().toString();
//                for (String message : messages) {
//                    sms.sendTextMessage(recipient, null, message, PendingIntent.getBroadcast(
//                    		ShareActivity.this, 0, new Intent(ACTION_SMS_SENT), 0), null);
//                }
//            }
//        });

        // Register broadcast receivers for SMS sent and delivered intents
//        registerReceiver(new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String message = null;
//                boolean error = true;
//                switch (getResultCode()) {
//                case Activity.RESULT_OK:
//                    message = "Message sent!";
//                    error = false;
//                    break;
//                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                    message = "Error.";
//                    break;
//                case SmsManager.RESULT_ERROR_NO_SERVICE:
//                    message = "Error: No service.";
//                    break;
//                case SmsManager.RESULT_ERROR_NULL_PDU:
//                    message = "Error: Null PDU.";
//                    break;
//                case SmsManager.RESULT_ERROR_RADIO_OFF:
//                    message = "Error: Radio off.";
//                    break;
//                }
//
//                recipientTextEdit.setEnabled(true);
//                contentTextEdit.setEnabled(true);
//                contentTextEdit.setText("");
//
//                statusView.setText(message);
//                statusView.setTextColor(error ? Color.RED : Color.GREEN);
//            }
//        }, new IntentFilter(ACTION_SMS_SENT));
    }

    
    public void initDatabase()
    {
    	db = DatabaseHandler.getInstance(getApplicationContext());
    }
    
    public void getInfoFromPreviousActivity()
    {
    	Intent intent = getIntent();
        partnerId = intent.getIntExtra(PartnerActivity.PARTNER_ID, -1);
        Log.d("SMS Activity:: ", String.valueOf(partnerId));
        partner = db.getPartner(partnerId);
    }
    
    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_sms, menu);
        return (super.onCreateOptionsMenu(menu));
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.home:
				this.startHomeActivity();
				return true;
				
			case R.id.about:
				Toast.makeText(this, R.string.about_message, Toast.LENGTH_LONG)
						.show();
				return (true);
		}
		return super.onOptionsItemSelected(item);
	}


	private void startHomeActivity() {
		Intent intent = new Intent(this, MainActivity.class);
		NavUtils.navigateUpTo(this, intent);
		//startActivity(intent);
	}
}
