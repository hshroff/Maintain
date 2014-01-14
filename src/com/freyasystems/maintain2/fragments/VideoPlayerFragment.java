/**
 * 
 */
package com.freyasystems.maintain2.fragments;

import java.util.List;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.freyasystems.maintain2.R;
import com.freyasystems.maintain2.dao.MaintainDS;
import com.freyasystems.maintain2.dao.MediaItemVO;

/**
 * @author shroffh Freya Systems, LLC.
 */
public class VideoPlayerFragment extends DialogFragment
{
	static String TAG = "VideoPlayerFragment";
	private static String STEPID_TAG = "stepID";
    String json_string;
    View parent;
    private static MaintainDS maintainDS;

    // Constructor
    public static VideoPlayerFragment newInstance(Long stepId)
    {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        Bundle arguments = new Bundle();
        arguments.putLong(STEPID_TAG, stepId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "reporting live from onCreateDialog");
    	Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN|WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        dialog.setCancelable(true);
        parent= getActivity().getLayoutInflater().inflate(R.layout.fragment_video_view, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        	dialog.setContentView(parent, layoutParams);
        return dialog;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
         super.onActivityCreated(savedInstanceState);

         // Set video holder
         VideoView video_view = (VideoView) parent.findViewById(R.id.step_video_view);
         Bundle args = getArguments();
	     Long stepID = args.getLong(STEPID_TAG);
	     Context applicationContext = parent.getContext();
			maintainDS = new MaintainDS(applicationContext);
			maintainDS.open();
			
			List<MediaItemVO>list = maintainDS.getStepMediaItem(stepID);
			int mediaId = 0;
			for (MediaItemVO mediaItem:list)
			{
				if (mediaItem.getType().equals("MOV"))
				{
					mediaId = mediaItem.getMediaItemID().intValue();
				}
			}
         // Set URL
	     Uri video = null;
	     switch(mediaId)
	     {
	     case 1101:
	 //   	 video = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.ch47dchinookhelicopterresupplyingmountainopeasternafghanistan);
	    	 break;
	     case 1102:
	    	 
	  //  	 video = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.minnesotanationalguard);
	    	 break;
	     }
         
         video_view.setVideoURI(video);

         video_view.setZOrderOnTop(true);
         MediaController mediaController = new MediaController(parent.getContext());
         mediaController.setAnchorView(video_view);
         mediaController.setMediaPlayer(video_view);
         video_view.setMediaController(mediaController);
         
         // Start video
         video_view.start();
    }
}
