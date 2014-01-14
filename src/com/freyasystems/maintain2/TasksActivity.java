package com.freyasystems.maintain2;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.freyasystems.maintain2.adapters.TaskListAdapter;
import com.freyasystems.maintain2.dao.MaintainDS;
import com.freyasystems.maintain2.dao.TaskVO;
import com.freyasystems.maintain2.util.SyncActivity;

public class TasksActivity extends Activity {
	private static String TAG = "TasksActivity";
	
	private MaintainDS maintainDS;

	private TaskListAdapter taskAdapter;
	private ListView taskListView; 
	private List<TaskVO> tasks;
	
	static Context applicationContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tasks);

		applicationContext = getApplicationContext();
		maintainDS = new MaintainDS(applicationContext);
		// maintainDS.resetDatabase(); //*** DATABASE RESET ***
		maintainDS.open();
		tasks = maintainDS.getAllTasks();
		maintainDS.close();

		taskAdapter = new TaskListAdapter(this,
				android.R.layout.simple_list_item_1, tasks);
		
		taskListView = (ListView) findViewById(R.id.taskListView);
		taskListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				TaskVO task = tasks.get(position);

				Log.d(ACTIVITY_SERVICE, "Task " + position
						+ " clicked! with id of " + task.getTaskID());

				Intent taskIntent = new Intent(getApplicationContext(),
						TabsFragmentActivity.class);
				Bundle b = new Bundle();
				// b.putLong("TaskID", id+1); //TEMP FIX until correct taskID
				// can be obtained from onItemClick event
				b.putLong("TaskID", task.getTaskID());
				taskIntent.putExtras(b);
				startActivity(taskIntent);
			}
		});
		taskListView.setAdapter(taskAdapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tasks, menu);
		return true;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.d(TAG, "inside onResume");
		
		maintainDS.open();
		tasks = maintainDS.getAllTasks();
		maintainDS.close();
		
		//Toast.makeText(this, "Syncing...", 5000).show();
		Log.d(TAG, "notifying task adapter now");
		//taskListView.refreshDrawableState();
		((TaskListAdapter) taskListView.getAdapter()).updateTaskList(tasks);
		//Toast.makeText(this, "Sync Complete", 1000).show();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.sync:
			//Toast.makeText(this, "Syncing", 5000).show();
			//Toast.makeText(this, "Sync Complete", 1000).show();
			Intent syncIntent = new Intent(getApplicationContext(), SyncActivity.class);
			startActivity(syncIntent);
			return true;
		case R.id.action_settings:
			Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
			startActivity(settingsIntent);
			return true;
		case R.id.resetdb:
			maintainDS.resetDatabase();
			//Toast.makeText(this, "Resetting database...", 5000).show();
			taskListView.refreshDrawableState();
			((TaskListAdapter) taskListView.getAdapter()).updateTaskList(new ArrayList<TaskVO>());
			//Toast.makeText(this, "Database Reset Complete", 1000).show();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
