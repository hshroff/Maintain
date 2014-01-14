package com.freyasystems.maintain2;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.freyasystems.maintain2.adapters.StepContentListAdapter;
import com.freyasystems.maintain2.dao.MaintainDS;
import com.freyasystems.maintain2.dao.StepContentVO;
import com.freyasystems.maintain2.dao.StepDataRuleVO;
import com.freyasystems.maintain2.dao.StepVO;

public class StepDetailActivity extends FragmentActivity {

	private String TAG = "StepDetailActivity";
	private MaintainDS maintainDS;

	private static String STEPID_TAG = "stepID";
	
	private StepVO step;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_step_detail);
		
		Bundle b = getIntent().getExtras();
		Long stepID = b.getLong(STEPID_TAG);

		maintainDS = new MaintainDS(getApplicationContext());
		maintainDS.open();
		step = maintainDS.getStep(stepID);
		List<StepContentVO> contents = maintainDS.getStepContent(step);
		
		if (step.getWarning() != null && step.getWarning().length() > 0) {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setTitle(R.string.AlertTitle);
			alert.setMessage(step.getWarning());
			alert.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							Log.d(TAG,
									"Warning alert acknowledged by user.");
						}
					});
			AlertDialog alertDialog = alert.create();
			alertDialog.show();
		}

		displayStep(step);
		
		//Notes flag
		ImageButton showNotesButton = (ImageButton) findViewById(R.id.StepDetail_ShowDataRuleButton);
		List<StepDataRuleVO> notes = maintainDS.getStepDataRuleNotes(step);
		maintainDS.close();
		
		if(notes.size() > 0){
			showNotesButton.setVisibility(View.VISIBLE);
			showNotesButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					DialogFragment stepDataFragment = new StepDataFragment();

					Bundle b = new Bundle();
					b.putLong("StepID", step.getStepID());
					/*if(step.getState().equals(Constants.STEP_COMPLETED)){
						
					}*/
					stepDataFragment.setArguments(b);
					stepDataFragment.show(getSupportFragmentManager(), "StepDataDialogFragment");
				}
			});
		}else{
			showNotesButton.setVisibility(View.INVISIBLE);
		}
		
		StepContentListAdapter scAdapter = new StepContentListAdapter(
			this, android.R.layout.simple_list_item_1, contents);
		ListView scListView = (ListView) findViewById(R.id.StepDetailContentListView);
		scListView.setAdapter(scAdapter);
		
	}

	private void displayStep(StepVO step){

		((TextView)findViewById(R.id.StepDetail_State)).setText("State: " + step.getState());
		
		ImageView statusImg = (ImageView) findViewById(R.id.StepDetail_StatusLogo);
		
		if(step.getState().equals(Constants.STEP_PENDING)){
			statusImg.setImageResource(R.drawable.status_not_started);
		}else if(step.getState().equals(Constants.STEP_IN_PROGRESS)){
			statusImg.setImageResource(R.drawable.status_in_progress);
		}else if(step.getState().equals(Constants.STEP_COMPLETED)){
			statusImg.setImageResource(R.drawable.status_complete);
			((CheckBox)findViewById(R.id.StepDetail_CompleteCheckBox)).setChecked(true);
		}
	}
	
	public void markCompleteClick(View view) {
		//Log.d(TAG, "currentPage is " + currentPage);
		
		// Is the view now checked?
		boolean checked = ((CheckBox) view).isChecked();
		//View rootView = view.getRootView();
		//String stepId = (String) ((TextView)rootView.findViewById(R.id.StepContentStepIDHolder)).getText();
		
		//Log.d(TAG, "inside markCompleteClick, got stepId of " + stepId);
		
		if (checked) {
			//Log.d(TAG, "Step marked complete, stepId=" + stepId);
			// check to see if any step content needs to be captured
			
			maintainDS.open();
			List<StepDataRuleVO> rules = maintainDS.getStepDataRules(step);
			
			if (rules.size() > 0) {
				((ImageButton) findViewById(R.id.StepDetail_ShowDataRuleButton)).setVisibility(View.VISIBLE);
				
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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.step_detail, menu);
		return true;
	}

}
