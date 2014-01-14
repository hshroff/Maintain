/**
 * 
 */
package com.freyasystems.maintain2.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import com.freyasystems.maintain2.R;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class StepJustInTimeTrainingFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		//String srcPath = "android.resource://com.freyasystems.maintain2/raw/minnesotanationalguard.mp4";
		
		View dialogView = inflater.inflate(R.layout.step_video_layout, null);

/*		VideoView videoView = (VideoView)dialogView.findViewById(R.id.jin_VideoView);
		videoView.setVideoURI(Uri.parse(srcPath));
*/		
		
		WebView webView = (WebView) dialogView.findViewById(R.id.Step_JIN_Webview);

		webView.loadUrl("file:///android_asset/JustInTime/JustInTime.html");
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setPluginsEnabled(true);

		
		dialogView.bringToFront();
		return builder.create();
	}
}
