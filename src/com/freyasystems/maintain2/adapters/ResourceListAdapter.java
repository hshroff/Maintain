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
import android.widget.TextView;

import com.freyasystems.maintain2.R;
import com.freyasystems.maintain2.dao.ResourceVO;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class ResourceListAdapter extends ArrayAdapter<ResourceVO> {

	private final Context context;
	private final List<ResourceVO> resources;
	//private String TAG = "ResourceListAdapter";
	
	/**
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public ResourceListAdapter(Context context, int textViewResourceId,
			List<ResourceVO> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.resources = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View rowView = inflater.inflate(R.layout.template_resourcelist, parent, false);
			ResourceVO resource = resources.get(position);			
			
			((TextView)rowView.findViewById(R.id.Verify_Resource_ShortDesc)).setText(resource.getShortDescription());
			((TextView)rowView.findViewById(R.id.Resource_Qty)).setText("Qty: " + resource.getQuantity());
			
			return rowView;
	}

}
