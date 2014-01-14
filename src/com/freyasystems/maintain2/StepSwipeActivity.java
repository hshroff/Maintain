package com.freyasystems.maintain2;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.freyasystems.maintain2.dao.TaskVO;
import com.freyasystems.maintain2.fragments.IETPViewerFragment;

public class StepSwipeActivity extends FragmentActivity {
	private static String TAG = "StepSwipeActivity";
	private static String STEPID_TAG = "stepID";

	private static MaintainDS maintainDS;

	MyAdapter mAdapter;

	ViewPager mPager;

	int currentPage = 0;

	static Long currentstepID;

	List<StepVO> steps;

	static Context applicationContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_pager);

		Bundle b = getIntent().getExtras();

		long taskId = b.getLong("TaskID", 1);

		applicationContext = getApplicationContext();
		maintainDS = new MaintainDS(applicationContext);
		maintainDS.open();

		TaskVO task = maintainDS.getTask(taskId);
		if (task == null) {
			Log.d(TAG, "No task found with id of " + taskId);
		}

		// get steps
		steps = maintainDS.getSteps(task);
		maintainDS.close();

		Log.d(TAG, "Found " + steps.size() + " steps for task " + taskId);

		mAdapter = new MyAdapter(getSupportFragmentManager(), steps.size(),
				steps);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setSaveEnabled(false);
		mPager.setAdapter(mAdapter);

		MyPageChangeListner mPageChangeListner = new MyPageChangeListner(this);

		mPager.setOnPageChangeListener(mPageChangeListner);
		mPageChangeListner.onPageSelected(0);

		// Watch for button clicks.
		// Button button = (Button) findViewById(R.id.goto_first);
		// button.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// mPager.setCurrentItem(0);
		// }
		// });
		// button = (Button) findViewById(R.id.goto_last);
		// button.setOnClickListener(new OnClickListener() {
		// public void onClick(View v) {
		// mPager.setCurrentItem(steps.size() - 1);
		// }
		// });
	}

	private class MyPageChangeListner implements ViewPager.OnPageChangeListener {
		Context context;

		/**
		 * 
		 */
		public MyPageChangeListner(Context context) {
			this.context = context;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
		 * (int)
		 */
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			Log.d(TAG, "page selected is " + arg0);
			currentPage = arg0;

			StepVO step = steps.get(currentPage);
			if (step.getWarning() != null && step.getWarning().length() > 0
					&& !step.getWarning().equals("null")) {
				Log.d(TAG, "warning is >" + step.getWarning() + "<");
				AlertDialog.Builder alert = new AlertDialog.Builder(context);
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

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#
		 * onPageScrollStateChanged(int)
		 */
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub

		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled
		 * (int, float, int)
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub

		}
	}

	public static class MyAdapter extends FragmentStatePagerAdapter {
		int count;
		List<StepVO> steps;

		public MyAdapter(FragmentManager fragmentManager, int c,
				List<StepVO> steps) {
			super(fragmentManager);
			this.count = c;
			this.steps = steps;
		}

		@Override
		public int getCount() {
			return count;
		}

		@Override
		public Fragment getItem(int position) {
			Log.d(TAG, "called MyAdapter.getItem(" + position + ")");
			return ArrayListFragment.newInstance(steps.get(position)
					.getStepID());
			// return ArrayListFragment.newInstance(steps.get(position));
			// return ArrayListFragment.newInstance(position);
		}

		public void setSteps(List<StepVO> steps) {
			this.steps = steps;
			notifyDataSetChanged();
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	public static class ArrayListFragment extends ListFragment {
		int mNum;

		Long stepID;

		/**
		 * Create a new instance of CountingFragment, providing "num" as an
		 * argument.
		 */
		// static ArrayListFragment newInstance(StepVO step) {
		static ArrayListFragment newInstance(Long stepID) {
			ArrayListFragment f = new ArrayListFragment();
			// f.step = step;
			f.stepID = stepID;

			// Supply num input as an argument.
			Bundle args = new Bundle();
			args.putLong(STEPID_TAG, stepID);
			f.setArguments(args);

			return f;
		}

		/**
		 * When creating, retrieve this instance's number from its arguments.
		 */
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			Bundle args = getArguments();
			Long stepID = args.getLong(STEPID_TAG);
			Log.d(TAG, "onCreate got step " + stepID);
		}

		/**
		 * The Fragment's UI is just a simple text view showing its instance
		 * number.
		 */
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			// Log.d(TAG,
			// "inside ArrayListFragment onCreateView, this renders display values");

			final View v = inflater.inflate(R.layout.fragment_pager_list,
					container, false);
			/*
			 * View tv = v.findViewById(R.id.text); ((TextView)
			 * tv).setText("Step: " + step.getStepID());
			 * Log.d("Activity Logger", "Step " + step.getStepID() +
			 * " viewed.");
			 * 
			 * ((TextView) v.findViewById(R.id.StepSwipeDetail_State))
			 * .setText(step.getState());
			 * 
			 * ((TextView)v.findViewById(R.id.StepContentStepIDHolder)).setText(step
			 * .getStepID().toString());
			 */
			Bundle args = getArguments();
			Long stepID = args.getLong(STEPID_TAG);
			currentstepID = stepID;

			// MaintainDS maintainDS = new MaintainDS(applicationContext);
			maintainDS.open();
			final StepVO step = maintainDS.getStep(stepID);
			Log.d(TAG,
					"step " + step.getStepID() + " has state of "
							+ step.getState());

			displayStep(v, step);

			// Notes flag
			ImageButton showNotesButton = (ImageButton) v
					.findViewById(R.id.Step_ShowDataRuleButton);
			List<StepDataRuleVO> notes = maintainDS.getStepDataRuleNotes(step);
			if (notes.size() > 0) {
				showNotesButton.setVisibility(View.VISIBLE);
				showNotesButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						DialogFragment stepDataFragment = new StepDataFragment();

						Bundle b = new Bundle();
						b.putLong("StepID", step.getStepID());
						/*
						 * if(step.getState().equals(Constants.STEP_COMPLETED)){
						 * 
						 * }
						 */
						stepDataFragment.setArguments(b);
						stepDataFragment.show(getFragmentManager(),
								"StepDataDialogFragment");
					}
				});
				// set OnClick
			} else {
				showNotesButton.setVisibility(View.INVISIBLE);
			}

			/*** Loop throght StepContent and build display ***/
			List<StepContentVO> contents = maintainDS.getStepContent(step);
			maintainDS.close();

			StepContentListAdapter scAdapter = new StepContentListAdapter(
					v.getContext(), android.R.layout.simple_list_item_1,
					contents);
			ListView scListView = (ListView) v
					.findViewById(R.id.StepContentListView);
			scListView.setAdapter(scAdapter);
			return v;
		}

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);
			setListAdapter(new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_list_item_1));
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			// Log.i("FragmentList", "Item clicked: " + id);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.step, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.Step_IETP_Action:
			IETPViewerFragment ietpDialog = new IETPViewerFragment();
			ietpDialog.show(getFragmentManager(), "Maintain IETP");
			return true;
		case R.id.Step_VIDEO_Action:
			StepVO step = steps.get(currentPage);

			Intent videoIntent = new Intent(this.getApplicationContext(),
					VideoActivity.class);
			Bundle b = new Bundle();
			b.putLong("stepID", step.getStepID());
			videoIntent.putExtras(b);
			startActivity(videoIntent);

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void markCompleteClick(View view) {
		// Log.d(TAG, "currentPage is " + currentPage);

		// Is the view now checked?
		boolean checked = ((CheckBox) view).isChecked();
		View rootView = view.getRootView();
		// String stepId = (String)
		// ((TextView)rootView.findViewById(R.id.StepContentStepIDHolder)).getText();

		// Log.d(TAG, "inside markCompleteClick, got stepId of " + stepId);

		if (checked) {
			// Log.d(TAG, "Step marked complete, stepId=" + stepId);
			// check to see if any step content needs to be captured

			maintainDS.open();
			StepVO step = steps.get(currentPage);
			// StepVO step = maintainDS.getStep(currentPage+1);

			List<StepDataRuleVO> rules = maintainDS.getStepDataRules(step);

			if (rules.size() > 0) {
				((ImageButton) findViewById(R.id.Step_ShowDataRuleButton))
						.setVisibility(View.VISIBLE);

				DialogFragment stepDataFragment = new StepDataFragment();

				Bundle b = new Bundle();
				b.putLong("StepID", step.getStepID());
				stepDataFragment.setArguments(b);
				stepDataFragment.show(getSupportFragmentManager(),
						"StepDataDialogFragment");
			} else {
				Log.d(TAG, "No rules found for step " + step.getStepID());
			}

			// update step status to complete
			step.setState(Constants.STEP_COMPLETED);
			maintainDS.updateStep(step);
			maintainDS.close();
			steps.set(currentPage, step); // replace existing step with updated
											// one in steps array
			mAdapter.notifyDataSetChanged();
		}
	}

	private static void displayStep(View view, StepVO step) {
		((TextView) view.findViewById(R.id.text)).setText("Step: "
				+ step.getSequence());
		// Log.d("Activity Logger", "Step " + step.getStepID() + " viewed.");

		((TextView) view.findViewById(R.id.StepSwipeDetail_State))
				.setText("State: " + step.getState());
		((TextView) view.findViewById(R.id.StepContentStepIDHolder))
				.setText(step.getStepID().toString());

		ImageView statusImg = (ImageView) view
				.findViewById(R.id.StepSwipeDetail_StatusLogo);

		if (step.getState().equals(Constants.STEP_PENDING)) {
			statusImg.setImageResource(R.drawable.status_not_started);
		} else if (step.getState().equals(Constants.STEP_IN_PROGRESS)) {
			statusImg.setImageResource(R.drawable.status_in_progress);
		} else if (step.getState().equals(Constants.STEP_COMPLETED)) {
			statusImg.setImageResource(R.drawable.status_complete);
			((CheckBox) view.findViewById(R.id.StepCompleteCheckBox))
					.setChecked(true);
		}
	}
}
