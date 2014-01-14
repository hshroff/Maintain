package com.freyasystems.maintain2;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.freyasystems.maintain2.dao.MaintainDS;
import com.freyasystems.maintain2.dao.TaskVO;
import com.freyasystems.maintain2.photo.BaseAlbumDirFactory;
import com.freyasystems.maintain2.tabs.TaskDetailTabFragment;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class VerifyTaskDialogFragment extends DialogFragment {

	private String TAG = "VerifyTaskDialogFragment";
	private MaintainDS maintainDS;

	public String current = null;
	File mypath;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		final View dialogView = inflater.inflate(
				R.layout.fragment_verify_task_dialog, null);
		Bundle b = getArguments();
		Long taskId = b.getLong("TaskID");

		maintainDS = new MaintainDS(dialogView.getContext());
		maintainDS.open();
		final TaskVO task = maintainDS.getTask(taskId);
		maintainDS.close();

		Log.d(TAG,
				"got task with taskId=" + task.getTaskID()
						+ " WorkOrderNumber=" + task.getWorkOrderNumber()
						+ " TailNumber" + task.getTailNumber() + " State"
						+ task.getState());

		setRetainInstance(true);
		builder.setView(dialogView).// Add action buttons
				setPositiveButton(R.string.button_confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// sign in the user ...
								task.setState(Constants.TASK_COMPLETED); // this
																			// should
																			// be
																			// verified
																			// instead,
																			// but
																			// don't
																			// know
																			// what
																			// constitues
																			// a
																			// "complete"
																			// state
																			// yet.
								maintainDS.open();
								maintainDS.updateTask(task);
								maintainDS.close();

								((TaskDetailTabFragment) getTargetFragment())
										.refreshTask(task.getTaskID());
							}
						});

		EditText workDoneEditText = (EditText) dialogView.findViewById(R.id.Verify_WorkDone);
		workDoneEditText.setText(task.getWorkDone());
		workDoneEditText.setKeyListener(null);
		
		TextView techName = (TextView) dialogView.findViewById(R.id.Verify_Tech_Name);
		techName.setText("John Doe");
		
		// Get Signature
		ContextWrapper cw = new ContextWrapper(this.getActivity()
				.getApplicationContext());

		File directory = cw.getDir(
				getResources().getString(R.string.external_dir),
				Context.MODE_PRIVATE);

		current = Constants.signatureFileName(task);
		mypath = new File(directory, current);

		if (mypath.exists() && mypath.canRead()) {
			Log.d(TAG, "Yes, I can read from " + mypath.getPath());

			Bitmap bmImg = BitmapFactory.decodeFile(mypath.getAbsolutePath());

			ImageView signatureImage = (ImageView) dialogView
					.findViewById(R.id.Verify_signatureImageView);
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
			
			ImageView pictureImageView = (ImageView) dialogView.findViewById(R.id.Verify_pictureImageView);
			pictureImageView.setImageBitmap(imgBitMap);
			
			//Full Screen Image View
			pictureImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					ImageView imageView = (ImageView)v;
					
					Intent fullScreenIntent = new Intent(dialogView.getContext(), FullScreenImageActivity.class);
					Bundle b = new Bundle();
					b.putString("imagePath", imageFile.getAbsolutePath());
			        fullScreenIntent.putExtras(b);
			        startActivity(fullScreenIntent);			        
				}
			});
		} else {
			Log.d(TAG, "Cant read from " + imageFile.getAbsolutePath());
		}
		
		return builder.create();
	}

	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance())
			getDialog().setOnDismissListener(null);
		super.onDestroyView();
	}
}
