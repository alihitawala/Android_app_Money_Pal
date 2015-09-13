package com.developer.nita.hisabKitab.adapter;

import java.util.ArrayList;

import com.developer.nita.hisabKitab.R;
import com.developer.nita.hisabKitab.StringManipulator.StringManipulator;
import com.developer.nita.hisabKitab.db.DatabaseHandler;
import com.developer.nita.hisabKitab.db.Partner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainListAdapter extends ArrayAdapter<Partner> {
	private final Context context;
	private final ArrayList<Partner> values;
	public DatabaseHandler db;
	
	public MainListAdapter(Context context, ArrayList<Partner> values) {
	    super(context, R.layout.list_black_text, values);
	    this.context = context;
	    this.values = values;
	    initDatabase();
	}
	
	public void initDatabase()
	{
		db = DatabaseHandler.getInstance(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.list_black_text, parent, false);
	    TextView textView = (TextView) rowView.findViewById(R.id.label);
	    TextView infoView = (TextView) rowView.findViewById(R.id.info);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
	    textView.setText(values.get(position).getName());
	    Partner partner = values.get(position);
        boolean isMale = "Male".equals(values.get(position).getSex());
	    if(isMale)
	    {
	      imageView.setImageResource(R.drawable.male);
	    } else {
	      imageView.setImageResource(R.drawable.female);
	    }
	    double totalAmount = partner.getTotalAmount();
        partner.setTotalAmount(totalAmount);
    	infoView.setText(StringManipulator.getDescriptionHeaderPartnerActivity(isMale, totalAmount));
	    return rowView;
	  }
}
