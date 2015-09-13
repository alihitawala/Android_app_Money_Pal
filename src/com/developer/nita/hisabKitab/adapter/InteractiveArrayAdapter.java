package com.developer.nita.hisabKitab.adapter;

import android.widget.ArrayAdapter;

import java.util.List;

import com.developer.nita.hisabKitab.R;
import com.developer.nita.hisabKitab.domainobject.PartnerCheckModel;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

public class InteractiveArrayAdapter extends ArrayAdapter<PartnerCheckModel> {

	 private final List<PartnerCheckModel> list;
	  private final Activity context;
	  private int countSplitMembers = 1;
	  private View splitView;

	  public InteractiveArrayAdapter(Activity context, List<PartnerCheckModel> list, View splitView) {
	    super(context, R.layout.check_row, list);
	    this.context = context;
	    this.list = list;
	    LayoutInflater inflator = context.getLayoutInflater();
	    this.splitView = splitView;
	  }

	  static class ViewHolder {
	    protected TextView text;
	    protected CheckBox checkbox;
	  }

	  @Override
	  public View getView(int position, View convertView, final ViewGroup parent) {
	    View view = null;
	    if (convertView == null) {
	      LayoutInflater inflator = context.getLayoutInflater();
	      view = inflator.inflate(R.layout.check_row, null);
	      final ViewHolder viewHolder = new ViewHolder();
	      viewHolder.text = (TextView) view.findViewById(R.id.label);
	      viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
	      viewHolder.checkbox
	          .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

	            @Override
	            public void onCheckedChanged(CompoundButton buttonView,
	                boolean isChecked) {
	            	PartnerCheckModel element = (PartnerCheckModel) viewHolder.checkbox
	                  .getTag();
	            	EditText countMembers = (EditText)splitView.findViewById(R.id.split_in);
	            	TextView countChecked = (TextView)splitView.findViewById(R.id.num_checked);
	            	element.setSelected(buttonView.isChecked());
	            	if (isChecked)
	            	{
	            		countSplitMembers++;
	            	}
	            	else
	            	{
	            		countSplitMembers--;
	            	}
	            	countMembers.setText(String.valueOf(countSplitMembers));
	            	countMembers.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_UNKNOWN));
	            	countChecked.setText(String.valueOf(countSplitMembers - 1));
	            }
	          });
	      view.setTag(viewHolder);
	      viewHolder.checkbox.setTag(list.get(position));
	    } else {
	      view = convertView;
	      ((ViewHolder) view.getTag()).checkbox.setTag(list.get(position));
	    }
	    ViewHolder holder = (ViewHolder) view.getTag();
	    holder.text.setText(list.get(position).getName());
	    holder.checkbox.setChecked(list.get(position).isSelected());
	    return view;
	  }
}
