/**
 * 
 */
package com.freyasystems.maintain2.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.freyasystems.maintain2.R;

/**
 * @author shroffh Freya Systems, LLC.
 */
public class IETPViewerFragment extends DialogFragment {
	private String TAG = "IETPViewerFragment";

	/*
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks. Each method
	 * passes the DialogFragment in case the host needs to query it.
	 */
	public interface IETPViewerListner {
		public void onDialogPositiveClick(DialogFragment dialog);
	}

	// Use this instance of the interface to deliver action events
	IETPViewerListner mListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		View dialogView = inflater.inflate(R.layout.fragment_ietp_view, null);

		WebView webView = (WebView) dialogView.findViewById(R.id.Step_IETP_WebView);

		webView.loadUrl("file:///android_asset/IETP/IETP_Sample.html");
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setPluginsEnabled(true);

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(dialogView)
		// Add action buttons
				.setPositiveButton(android.R.id.closeButton,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								IETPViewerFragment.this.getDialog().cancel();
							}
						});

		return builder.create();
	}
}
