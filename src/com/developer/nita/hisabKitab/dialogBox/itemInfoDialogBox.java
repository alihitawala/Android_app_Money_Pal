package com.developer.nita.hisabKitab.dialogBox;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.developer.nita.hisabKitab.R;
import com.developer.nita.hisabKitab.date.DateHandler;
import com.developer.nita.hisabKitab.db.PartnerDetail;

public class itemInfoDialogBox extends Dialog {
//	private Context mContext = null;
	private PartnerDetail detail = null;
	private String partnerName = null;
	
	public itemInfoDialogBox(Context context, PartnerDetail detail, String name) {
		super(context, R.style.CustomDialogTheme);
//		this.mContext = context;
		this.detail = detail;
		partnerName = name;
	}
	/**
	}
	* Standard Android on create method that gets called when the activity initialized.
	*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.item_info);
		this.setCanceledOnTouchOutside(true);
		this.setValueOnDialogBox();
	}
	
	public void setValueOnDialogBox()
	{
		TextView name = (TextView)findViewById(R.id.partner_name);
		TextView itemName = (TextView)findViewById(R.id.item_name);
		TextView heGave = (TextView)findViewById(R.id.he_value);
		TextView youGave = (TextView)findViewById(R.id.you_value);
		TextView dateValue = (TextView)findViewById(R.id.date_value);
		TextView timeValue = (TextView)findViewById(R.id.time_value);
//		ImageView peopleImage = (ImageView)findViewById(R.id.partner_image);
		name.setText(partnerName);
		itemName.setText(detail.getItem());
		heGave.setText(String.valueOf(detail.getDebit()));
		youGave.setText(String.valueOf(detail.getCredit()));
		DateHandler dateHandler = new DateHandler(detail.getDate());
		dateValue.setText(dateHandler.getDetailedDate());
		timeValue.setText(dateHandler.getTime());
//		int resId;
//		if(db.isPersonMale(this.partnerName))
//		{
//			resId = this.mContext.getResources().getIdentifier("male", "drawable", this.mContext.getPackageName());
//			
//		}
//		else
//		{
//			resId = this.mContext.getResources().getIdentifier("female", "drawable", this.mContext.getPackageName());
//		}
//		peopleImage.setImageResource(resId);
	}

}