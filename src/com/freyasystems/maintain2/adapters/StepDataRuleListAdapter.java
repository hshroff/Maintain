/**
 * 
 */
package com.freyasystems.maintain2.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.freyasystems.maintain2.R;
import com.freyasystems.maintain2.dao.StepDataRuleVO;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class StepDataRuleListAdapter extends ArrayAdapter<StepDataRuleVO> {
	private final Context context;
	private final List<StepDataRuleVO> rules;
	private String TAG = "StepDataRuleListAdapter";
	
	
	/**
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	@Deprecated
	public StepDataRuleListAdapter(Context context, int textViewResourceId,
			List<StepDataRuleVO> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.rules = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View rowView = inflater.inflate(R.layout.template_stepdatarule, parent, false);
			StepDataRuleVO rule = rules.get(position);
		//	Log.d(TAG, "got rule " + rule.getRuleName());
			
			((TextView)rowView.findViewById(R.id.StepDataRule_Name)).setText(rule.getRuleName());
			((EditText)rowView.findViewById(R.id.StepDataRule_Value)).setHint(rule.getRuleCaption());
			
			EditText edit = (EditText)rowView.findViewById(R.id.StepDataRule_Value);
			edit.clearFocus();
			//edit.requestFocus();
			
			return rowView;
	}

}
