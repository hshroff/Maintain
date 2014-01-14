package com.freyasystems.maintain2;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.freyasystems.maintain2.dao.MaintainDS;
import com.freyasystems.maintain2.dao.NoteVO;
import com.freyasystems.maintain2.dao.StepDataRuleVO;
import com.freyasystems.maintain2.dao.StepVO;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass. Activities that
 * contain this fragment must implement the
 * {@link StepDataFragment.OnFragmentInteractionListener} interface to handle
 * interaction events. Use the {@link StepDataFragment#newInstance} factory
 * method to create an instance of this fragment.
 * 
 */
public class StepDataFragment extends DialogFragment {
	private String TAG = "StepDataFragment";

	private static final String ARG_STEPID = "StepID";

	private Long stepID;

	private MaintainDS maintainDS;

	private OnFragmentInteractionListener mListener;

	private Context applicationContext;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment StepDataFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static StepDataFragment newInstance(Long stepID) {
		StepDataFragment fragment = new StepDataFragment();
		Bundle args = new Bundle();
		args.putLong(ARG_STEPID, stepID);
		fragment.setArguments(args);
		return fragment;
	}

	public StepDataFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			stepID = getArguments().getLong(ARG_STEPID);
			// Log.d(TAG, "stepID passed is " + stepID);
		} else {
			Log.d(TAG, "No stepID passed.");
		}
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Inflate the layout for this fragment
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		applicationContext = getActivity().getApplicationContext();

		if (stepID != null) {
			maintainDS = new MaintainDS(applicationContext);
			maintainDS.open();
			StepVO step = maintainDS.getStep(stepID);
			final List<StepDataRuleVO> rules = maintainDS
					.getStepDataRules(step);
			maintainDS.close();

			if (rules.size() > 0) {
				final View stepDataTableView = inflater.inflate(
						R.layout.fragment_step_data2, null);
				final TableLayout stepDataTable = (TableLayout) stepDataTableView
						.findViewById(R.id.StepDataTableLayout);
				
				((TextView)stepDataTableView.findViewById(R.id.StepDataTable_StepSequence)).setText("Additional Information Required for Step " + step.getSequence());

				int field_text_size = 22;

				for (final StepDataRuleVO rule : rules) {
					TableRow row = new TableRow(stepDataTable.getContext());

					// Rule ID
					TextView ruleID = new TextView(stepDataTable.getContext());
					ruleID.setVisibility(View.INVISIBLE);
					ruleID.setText(rule.getId().toString());
					row.addView(ruleID);

					// Rule Name
					TextView ruleName = new TextView(stepDataTable.getContext());
					ruleName.setText(rule.getRuleName());
					ruleName.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					ruleName.setTextSize(TypedValue.COMPLEX_UNIT_SP,
							field_text_size);

					row.addView(ruleName);

					// Rule Value
					if (rule.getNote() == null) {
						// new note
						EditText ruleValue = new EditText(
								stepDataTable.getContext());
						ruleValue.setTextSize(TypedValue.COMPLEX_UNIT_SP,
								field_text_size);
						ruleValue.setImeOptions(EditorInfo.IME_ACTION_DONE);

						ruleValue.setHint(rule.getRuleCaption());
						row.addView(ruleValue);
					} else {
						// Existing Note
						// Rule ID
						TextView noteID = new TextView(
								stepDataTable.getContext());
						//noteID.setId((int) (100 + rule.getDataRuleID())); // 100+ note id
						noteID.setVisibility(View.INVISIBLE);
						noteID.setText(rule.getNote().getNoteID().toString()); // set value to NoteID for existing Note

						EditText ruleValue = new EditText(
								stepDataTable.getContext());
						ruleValue.setTextSize(TypedValue.COMPLEX_UNIT_SP,
								field_text_size);
						ruleValue.setImeOptions(EditorInfo.IME_ACTION_DONE);

						ruleValue.setText(rule.getNote().getNote());
						row.addView(ruleValue);
						row.addView(noteID);
					}

					stepDataTable.addView(row);
				}

				builder.setView(stepDataTableView).setNeutralButton(
						android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Loop through all table rows and create/update
								// notes
								for (int index = 0; index < stepDataTable
										.getChildCount(); index++) {
									TableRow row = (TableRow) stepDataTable
											.getChildAt(index);
									Long dataRuleID = new Long(
											(String) ((TextView) row
													.getChildAt(0)).getText());
									NoteVO tempNote = new NoteVO();
									tempNote.setNote(((EditText) row
											.getChildAt(2)).getText()
											.toString());

									maintainDS.open();

									if (row.getChildCount() == 3) {
										// create Note
										StepDataRuleVO rule = maintainDS.getStepDataRule(dataRuleID);
										maintainDS.addNote(tempNote, rule);
									} else if (row.getChildCount() == 4) {
										// update Note
										Long noteID = new Long((String) ((TextView) row
														.getChildAt(3))
														.getText());
										tempNote.setNoteID(noteID);
										maintainDS.updateNote(tempNote);
									}
									maintainDS.close();
								}
							//	((ImageButton) stepDataTableView.findViewById(R.id.Step_ShowDataRuleButton)).setVisibility(View.VISIBLE);
								dialog.cancel(); // close the dialog
							}
						});
				return builder.create();
			}

			else {
				Log.d(TAG, "No rules found for stepID " + stepID);
				return null;
			}
		} else {
			Log.d(TAG, "No stepID passed so have nothing to display.");
			return null;
		}

		// return inflater.inflate(R.layout.fragment_step_data, container,
		// false);
	}

	/*
	 * // TODO: Rename method, update argument and hook method into UI event
	 * public void onButtonPressed(Uri uri) { if (mListener != null) {
	 * mListener.onFragmentInteraction(uri); } }
	 * 
	 * @Override public void onAttach(Activity activity) {
	 * super.onAttach(activity); try { mListener =
	 * (OnFragmentInteractionListener) activity; } catch (ClassCastException e)
	 * { throw new ClassCastException(activity.toString() +
	 * " must implement OnFragmentInteractionListener"); } }
	 * 
	 * @Override public void onDetach() { super.onDetach(); mListener = null; }
	 */
	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
