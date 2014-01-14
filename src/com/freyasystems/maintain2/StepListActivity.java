package com.freyasystems.maintain2;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.freyasystems.maintain2.dao.MaintainDS;
import com.freyasystems.maintain2.dao.StepContentVO;
import com.freyasystems.maintain2.dao.StepDataRuleVO;
import com.freyasystems.maintain2.dao.StepVO;
import com.freyasystems.maintain2.dao.TaskVO;

public class StepListActivity extends FragmentActivity {
	
	private static String TAG = "StepListActivity";

	private static MaintainDS maintainDS;
	List<StepVO> steps; 
	TaskVO task;
	
	static Context applicationContext;

	TableLayout stepsTable;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step_list);
		
		Bundle b = getIntent().getExtras();

		long taskId = b.getLong("TaskID", 1);
		
		applicationContext = getApplicationContext();
		maintainDS = new MaintainDS(applicationContext);
		maintainDS.open();

		task = maintainDS.getTask(taskId);
		
		if (task == null) {
			Log.d(TAG, "No task found with id of " + taskId);
		}

		if(task.getState().equals(Constants.TASK_PENDING) ||task.getState().equals(Constants.TASK_IN_PROGRESS)){
			((CheckBox)findViewById(R.id.StepListCompleteAllCheckBox)).setChecked(false);
		}else{
			((CheckBox)findViewById(R.id.StepListCompleteAllCheckBox)).setChecked(true);
		}
		
		// get steps
		steps = maintainDS.getSteps(task);
		maintainDS.close();

		Log.d(TAG, "Found " + steps.size() + " steps for task " + taskId);
		
		//Table View
		int field_text_size = 22;
		stepsTable = (TableLayout) findViewById(R.id.StepListTableView);
		
	
		for(StepVO step : steps){
			TableRow row = new TableRow(stepsTable.getContext());
			
			//Sequence
			/*TextView sequence = new TextView(stepsTable.getContext());
			sequence.setText(step.getSequence().toString());
			sequence.setTextSize(TypedValue.COMPLEX_UNIT_SP, field_text_size);
			row.addView(sequence);*/
			
			//Initial Step Content
			maintainDS.open();
			List<StepContentVO> contents = maintainDS.getStepContent(step);
			maintainDS.close();
			
			TextView initialContent = new TextView(stepsTable.getContext());
			initialContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, field_text_size);
			TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT,1.0f);
			initialContent.setLayoutParams(params);
			
			String initialContentText = "";
			for(StepContentVO content : contents){
				if(content.getContent() != null && content.getContent() != "" && content.getContent().length() > 0){
					initialContentText = content.getContent();
					break;
				}
			}

			Log.d(TAG, "initial content is " + initialContentText);
			initialContent.setText(initialContentText);
			
			row.addView(initialContent);
			
			final Long stepId = step.getStepID();
			row.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Bundle b = new Bundle();
					b.putLong("stepID", stepId);
				
					Intent showStepDetail = new Intent(v.getContext(), StepDetailActivity.class);
					showStepDetail.putExtras(b);
					startActivity(showStepDetail);
				}
			});
			
			//Complete Checkbox
			CheckBox complete = new CheckBox(stepsTable.getContext());
			complete.setText("Complete");
			complete.setTextSize(TypedValue.COMPLEX_UNIT_SP, field_text_size);
			
			if(task.getState().equals(Constants.STEP_COMPLETED)){
				complete.setChecked(true);
			}
			
			complete.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						maintainDS.open();
						StepVO step = maintainDS.getStep(stepId);
						List<StepDataRuleVO> rules = maintainDS.getStepDataRules(step);
						
						if (rules.size() > 0) {							
							DialogFragment stepDataFragment = new StepDataFragment();

							Bundle b = new Bundle();
							b.putLong("StepID", step.getStepID());
							stepDataFragment.setArguments(b);
							stepDataFragment.show(getSupportFragmentManager(), "StepDataDialogFragment");
							
						} else {
							Log.d(TAG, "No rules found for step " + step.getStepID());
						}

						// update step status to complete
						step.setState(Constants.STEP_COMPLETED);
						maintainDS.updateStep(step);
						maintainDS.close();
					}
				}
			});
			row.addView(complete);
			
			stepsTable.addView(row);
		}
	}

	public void completeAll(View view) {
		
		final CheckBox buttonView = (CheckBox) view;
		boolean checked = buttonView.isChecked();
		
		if(checked){
			AlertDialog.Builder alert = new AlertDialog.Builder(buttonView.getContext());
			alert.setTitle("Are you sure?");
			alert.setMessage("This will mark all steps complete, are you sure?");
			alert.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Log.d(TAG, "Complete All checked");
							for (int index = 1; index < stepsTable.getChildCount(); index++) {
								//Skip the first row
								TableRow row = (TableRow) stepsTable.getChildAt(index);
								((CheckBox) row.getChildAt(1)).setChecked(true);
							}
						}
					});
			alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog,
						int whichButton) {
					buttonView.setChecked(false);
					dialog.cancel();
				}
			});
			AlertDialog alertDialog = alert.create();
			alertDialog.show();
		}
	}

	
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.step_list, menu);
		return true;
	}*/

}
