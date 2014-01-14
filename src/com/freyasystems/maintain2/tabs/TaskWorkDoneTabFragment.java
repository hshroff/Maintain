/**
 * 
 */
package com.freyasystems.maintain2.tabs;

import java.io.File;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.freyasystems.maintain2.Constants;
import com.freyasystems.maintain2.FullScreenImageActivity;
import com.freyasystems.maintain2.R;
import com.freyasystems.maintain2.dao.MaintainDS;
import com.freyasystems.maintain2.dao.TaskVO;
import com.freyasystems.maintain2.photo.BaseAlbumDirFactory;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class TaskWorkDoneTabFragment extends Fragment {

	private String TAG = "TaskWorkDoneTabFragment";
	private MaintainDS maintainDS;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        
        View taskWorkDoneTabView = (LinearLayout)inflater.inflate(R.layout.tab_task_workdone_layout, container, false);
        
        Bundle b = getArguments();
        final long taskId = b.getLong("TaskID", 1);
		Log.d(TAG, "got taskId of " + taskId);

		maintainDS = new MaintainDS(container.getContext());
		maintainDS.open();

		TaskVO task = maintainDS.getTask(taskId);
		
		
		if (task == null) {
			Log.d(TAG, "No task found with id of " + taskId);
		}
		maintainDS.close();
		
		EditText workDoneEditText = (EditText) taskWorkDoneTabView.findViewById(R.id.WorkDone_WorkDone);
		workDoneEditText.setText(task.getWorkDone());
		workDoneEditText.setKeyListener(null);
		
		// Get Signature
		ContextWrapper cw = new ContextWrapper(this.getActivity()
				.getApplicationContext());

		File directory = cw.getDir(
				getResources().getString(R.string.external_dir),
				Context.MODE_PRIVATE);

		String current = Constants.signatureFileName(task);
		File mypath = new File(directory, current);

		if (mypath.exists() && mypath.canRead()) {
			Log.d(TAG, "Yes, I can read from " + mypath.getPath());

			Bitmap bmImg = BitmapFactory.decodeFile(mypath.getAbsolutePath());

			ImageView signatureImage = (ImageView) taskWorkDoneTabView
					.findViewById(R.id.WorkDone_signatureImageView);
			signatureImage.setImageBitmap(bmImg);
		} else {
			Log.d(TAG, "Cant read from " + mypath.getPath());
		}
		
		//Get Task Picture/Video
		BaseAlbumDirFactory mAlbumStorageDirFactory = new BaseAlbumDirFactory();
		File storageDir = mAlbumStorageDirFactory.getAlbumStorageDir("MaintainPhotos"); 
		storageDir = new File("/storage/emulated/0/Pictures/MaintainPhotos/");
		String imageFileName = Constants.pictureFileName(task);
		
		final File imageFile = new File(storageDir, imageFileName);
		
		Log.d(TAG, "imageFile is " + imageFile.getAbsolutePath());
		
		if (imageFile.exists() && imageFile.canRead()) {
			Bitmap imgBitMap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
			
			ImageView pictureImageView = (ImageView) taskWorkDoneTabView.findViewById(R.id.WorkDone_pictureImageView);
			pictureImageView.setImageBitmap(imgBitMap);
			
			//Full Screen Image View
			pictureImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ImageView imageView = (ImageView)v;
					
					Intent fullScreenIntent = new Intent(v.getContext(), FullScreenImageActivity.class);
					Bundle b = new Bundle();
					b.putString("imagePath", imageFile.getAbsolutePath());
			        fullScreenIntent.putExtras(b);
			        startActivity(fullScreenIntent);			        
				}
			});
		} else {
			Log.d(TAG, "Cant read from " + imageFile.getAbsolutePath());
		}
		
        return taskWorkDoneTabView;
    }
}
