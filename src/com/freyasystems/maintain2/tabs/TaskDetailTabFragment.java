/**
 * 
 */
package com.freyasystems.maintain2.tabs;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.freyasystems.maintain2.Constants;
import com.freyasystems.maintain2.R;
import com.freyasystems.maintain2.StepListActivity;
import com.freyasystems.maintain2.StepSwipeActivity;
import com.freyasystems.maintain2.VerifyTaskDialogFragment;
import com.freyasystems.maintain2.dao.MaintainDS;
import com.freyasystems.maintain2.dao.StepVO;
import com.freyasystems.maintain2.dao.TaskVO;
import com.freyasystems.maintain2.fragments.RecordWorkDialogFragment;

/**
 * @author shroffh Freya Systems, LLC.
 */
public class TaskDetailTabFragment extends Fragment {
	private String TAG = "TaskDetailTabFragment";
	private MaintainDS maintainDS;
	static Context applicationContext;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (container == null) {
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}

		View taskDetailTabView = (RelativeLayout) inflater.inflate(
				R.layout.tab_taskdetail_layout, container, false);

		Bundle b = getArguments();
		final long taskId = b.getLong("TaskID", 1);
		Log.d(TAG, "got taskId of " + taskId);

		applicationContext = getActivity().getApplicationContext();

		maintainDS = new MaintainDS(applicationContext);
		maintainDS.open();

		final TaskVO task = maintainDS.getTask(taskId);
		

		if (task == null) {
			Log.d(TAG, "No task found with id of " + taskId);
		}

		displayTask(task, taskDetailTabView);

		List<StepVO> steps = maintainDS.getSteps(task);
		maintainDS.close();
		
		Button workButton = (Button) taskDetailTabView
				.findViewById(R.id.Task_Detail_Work_Button);
		if(steps.size() > 0){
			workButton.setEnabled(true);
			workButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					// Set task state to 'In Progress' when technician opens it up
					// to work.

					if (task.getState().equals(Constants.TASK_PENDING)) {
						task.setState(Constants.TASK_IN_PROGRESS);
						maintainDS.open();
						maintainDS.updateTask(task);
						maintainDS.close();
						refreshTask(taskId);
					}

					String stepDisplayPref = getDisplayType(task);
					
					Intent workIntent;
					//if(taskId % 2 == 1){ //odd
					if(stepDisplayPref.equals("Check List")){
						workIntent = new Intent(view.getContext(), StepListActivity.class);
					}else{ //even
						workIntent = new Intent(view.getContext(), StepSwipeActivity.class);
					}
					
					Bundle bundle = new Bundle();
					bundle.putLong("TaskID", taskId);
					workIntent.putExtras(bundle);
					startActivity(workIntent);
				}
			});
		}else{
			workButton.setText("No Steps");
			workButton.setEnabled(false);
		}
		
		final RecordWorkDialogFragment recordWorkDialog = new RecordWorkDialogFragment();
		recordWorkDialog.setTargetFragment(this, getTargetRequestCode());

		Button completeButton = (Button) taskDetailTabView
				.findViewById(R.id.Task_Detail_Complete_Button);
		completeButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// DialogFragment recordWorkDialog = new
				// RecordWorkDialogFragment();
				Bundle b = new Bundle();
				b.putLong("TaskID", taskId);
				recordWorkDialog.setArguments(b);
				recordWorkDialog.show(getFragmentManager(),
						"RecordWorkDialogFragment");
			}
		});

		return taskDetailTabView;
	}

	public void displayTask(TaskVO task, View taskDetailTabView) {
		final Long taskId = task.getTaskID();

		// ((TextView)taskDetailTabView.findViewById(R.id.Task_Detail_TaskID)).setText(new
		// Long(task.getTaskID()).toString());
		((TextView) taskDetailTabView.findViewById(R.id.Task_Detail_TaskCode))
				.setText("Code: " + task.getTaskCode());
		((TextView) taskDetailTabView.findViewById(R.id.Task_Detail_State))
				.setText("State: " + task.getState());
		((TextView) taskDetailTabView.findViewById(R.id.Task_Detail_ShortDesc))
				.setText(task.getShortDescription());
		((TextView) taskDetailTabView.findViewById(R.id.Task_Detail_LongDesc))
				.setText(task.getLongDescription());

		// State
		Button completeButton = (Button) taskDetailTabView
				.findViewById(R.id.Task_Detail_Complete_Button);
		Button workButton = (Button) taskDetailTabView
				.findViewById(R.id.Task_Detail_Work_Button);
		if (task.getState().equals(Constants.TASK_COMPLETED)) {
			completeButton.setText(Constants.TASK_COMPLETED);
			completeButton.setEnabled(false);
			workButton.setText("Review");
		} else if (task.getState().equals(Constants.TASK_PENDING)
				|| task.getState().equals(Constants.TASK_IN_PROGRESS)) {
			// Change button to Sign
			completeButton.setText(Constants.TASK_ACTION_SIGN);
			completeButton.setEnabled(true);
		} else if (task.getState().equals(Constants.TASK_SIGNED)) {
			// Change button to Verify
			completeButton.setText(Constants.TASK_ACTION_VERIFY);
			completeButton.setEnabled(true);
			workButton.setText("Review Steps");

			final VerifyTaskDialogFragment verifyTaskDialog = new VerifyTaskDialogFragment();
			verifyTaskDialog.setTargetFragment(this, getTargetRequestCode());

			completeButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Bundle b = new Bundle();
					b.putLong("TaskID", taskId);
					verifyTaskDialog.setArguments(b);
					verifyTaskDialog.show(getFragmentManager(),
							"VerifyTaskDialogFragment");
				}
			});

			// RecordWorkDialog should retrieve signature, picture & video
			// recorded in Sign action.
		} else if (task.getState().equals(Constants.TASK_VERIFIED)) {
			completeButton.setText(Constants.TASK_ACTION_COMPLETE);
			completeButton.setEnabled(true);
			workButton.setText("Review");
			// RecordWorkDialog should allow one to complete the task
		}
	}

	public void refreshTask(Long taskId) {
		maintainDS.open();
		TaskVO task = maintainDS.getTask(taskId);
		maintainDS.close();

		View taskDetailTabView = this.getView();
		displayTask(task, taskDetailTabView);
	}
	
	private String getDisplayType(TaskVO task){
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
		String stepDisplayPref = sharedPrefs.getString("pref_step_display_list", "Auto");
		
		if(stepDisplayPref.equals("Auto")){
			if(task.getDisplayType() != null) 
				stepDisplayPref = task.getDisplayType();
			else
				stepDisplayPref = "Check List";
		}
		
		Log.d(TAG, "For task " +task.getTaskID() +" display type is " + stepDisplayPref);
		
		return stepDisplayPref;
	}
}
