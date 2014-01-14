/**
 * 
 */
package com.freyasystems.maintain2.fragments;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.freyasystems.maintain2.R;
import com.freyasystems.maintain2.adapters.StepContentListAdapter;
import com.freyasystems.maintain2.dao.MaintainDS;
import com.freyasystems.maintain2.dao.StepContentVO;
import com.freyasystems.maintain2.dao.StepVO;

/**
 * @author shroffh Freya Systems, LLC.
 */
public class StepContentViewFragment extends DialogFragment {

	private String TAG = "StepContentViewFragment";
	private MaintainDS maintainDS;

	private static String STEPID_TAG = "stepID";
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		final View v = inflater.inflate(R.layout.fragment_pager_list, null);
		Bundle b = getArguments();
		Long stepID = b.getLong(STEPID_TAG);

		maintainDS = new MaintainDS(v.getContext());
		maintainDS.open();
		final StepVO step = maintainDS.getStep(stepID);
		List<StepContentVO> contents = maintainDS.getStepContent(step);
		maintainDS.close();

		StepContentListAdapter scAdapter = new StepContentListAdapter(
				v.getContext(), android.R.layout.simple_list_item_1, contents);
		ListView scListView = (ListView) v
				.findViewById(R.id.StepContentListView);
		scListView.setAdapter(scAdapter);

		return builder.create();
	}
}
