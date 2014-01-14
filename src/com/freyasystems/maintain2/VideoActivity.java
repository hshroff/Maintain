package com.freyasystems.maintain2;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.widget.MediaController;
import android.widget.VideoView;

import com.freyasystems.maintain2.dao.MaintainDS;
import com.freyasystems.maintain2.dao.MediaItemVO;

public class VideoActivity extends Activity {

	private static MaintainDS maintainDS;
	static String TAG = "VideoActivity";
	private static String STEPID_TAG = "stepID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		
		final VideoView video_view = (VideoView)findViewById(R.id.fullscreen_video);
		
		
		Bundle args = getIntent().getExtras();
		
	     Long stepID = args.getLong(STEPID_TAG);
	     Context applicationContext = this.getApplicationContext();
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
	    	// video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.ch47dchinookhelicopterresupplyingmountainopeasternafghanistan);
	    	 break;
	     case 1102:
	    	 
	    	// video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.minnesotanationalguard);
	    	 break;
	     }
        
        video_view.setVideoURI(video);

        video_view.setZOrderOnTop(true);
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video_view);
        mediaController.setMediaPlayer(video_view);
        video_view.setMediaController(mediaController);
        
        // Start video
        video_view.start();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.video, menu);
		return true;
	}

}
