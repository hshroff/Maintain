/**
 * 
 */
package com.freyasystems.maintain2.adapters;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.freyasystems.maintain2.R;
import com.freyasystems.maintain2.dao.TaskVO;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class TaskListAdapter extends ArrayAdapter<TaskVO> {

	private final Context context;
	private final List<TaskVO> tasks;
	private String TAG = "TaskListAdapter";
	
	/**
	 * @param context
	 * @param textViewResourceId
	 * @param objects
	 */
	public TaskListAdapter(Context context, int textViewResourceId,
			List<TaskVO> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.tasks = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View rowView = inflater.inflate(R.layout.template_tasklist, parent, false);
			TaskVO task = tasks.get(position);			
		//	Log.d(TAG, "got task ID of " + task.getTaskID());
			
			((TextView)rowView.findViewById(R.id.task_list_label)).setText(task.toString());
			
			return rowView;
	}
	
	public void updateTaskList(final List<TaskVO> newlist) {
	    tasks.clear();
	    tasks.addAll(newlist);
	    this.notifyDataSetChanged();
	}
}
