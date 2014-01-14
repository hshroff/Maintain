/**
 * 
 */
package com.freyasystems.maintain2.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images;
import android.support.v4.app.DialogFragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.freyasystems.maintain2.Constants;
import com.freyasystems.maintain2.R;
import com.freyasystems.maintain2.dao.MaintainDS;
import com.freyasystems.maintain2.dao.TaskVO;
import com.freyasystems.maintain2.photo.PhotoIntentActivity;
import com.freyasystems.maintain2.tabs.TaskDetailTabFragment;

/**
 * @author shroffh Freya Systems, LLC.
 */
public class RecordWorkDialogFragment extends DialogFragment {

	private String TAG = "RecordWorkDialogFragment";
	private MaintainDS maintainDS;

	LinearLayout mContent;
	signature mSignature;
	Button mClear; // , mGetSign, mCancel;
	public static String tempDir;
	public int count = 1;
	public String current = null;
	private Bitmap mBitmap;
	View mView;
	File mypath;

	private String uniqueId;

	/*
	 * The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks. Each method
	 * passes the DialogFragment in case the host needs to query it.
	 */
	public interface RecordWorkDialogListner {
		public void onDialogPositiveClick(DialogFragment dialog);

		public void onDialogNegativeClick(DialogFragment dialog);
	}

	// Use this instance of the interface to deliver action events
	RecordWorkDialogListner mListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = getActivity().getLayoutInflater();

		final View dialogView = inflater.inflate(
				R.layout.fragment_record_work_dialog, null);
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


		// Signature Capture
		tempDir = Environment.getExternalStorageDirectory() + "/"
				+ getResources().getString(R.string.external_dir) + "/";
		ContextWrapper cw = new ContextWrapper(this.getActivity()
				.getApplicationContext());
		File directory = cw.getDir(
				getResources().getString(R.string.external_dir),
				Context.MODE_PRIVATE);

		prepareDirectory();
		/*uniqueId = getTodaysDate() + "_" + getCurrentTime() + "_"
				+ Math.random();*/
		
		//WO_TL_TaskID.png
		//uniqueId = task.getWorkOrderNumber() + "_" + task.getTailNumber() + "_" + task.getTaskID();
		current = Constants.signatureFileName(task);
		mypath = new File(directory, current);
		Log.d(TAG, "Saving signature to " + mypath.getAbsolutePath());

		mContent = (LinearLayout) dialogView.findViewById(R.id.signatureLayout);
		mSignature = new signature(dialogView.getContext(), null);
		mSignature.setBackgroundColor(Color.LTGRAY);
		mContent.addView(mSignature, LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		mClear = (Button) dialogView.findViewById(R.id.clear);
		mView = mContent;

		mClear.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.v("log_tag", "Panel Cleared");
				mSignature.clear();
			}
		});
		// End of Signature Capture

		setRetainInstance(true);

		ImageButton takePhotoButton = (ImageButton) dialogView
				.findViewById(R.id.RecordWO_TakePhotoButton);
		takePhotoButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent photoIntent = new Intent(v.getContext(),
						PhotoIntentActivity.class);
				Bundle bundle = new Bundle();
				bundle.putLong("TaskID", task.getTaskID());
				photoIntent.putExtras(bundle);
				startActivity(photoIntent);
			}
		});

		EditText workDoneEditText = (EditText) dialogView.findViewById(R.id.RecordWO_WorkDone);
		workDoneEditText.setText(task.getWorkDone());
		
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(dialogView)
				// Add action buttons
				.setPositiveButton(R.string.button_save,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// sign in the user ...
								mView.setDrawingCacheEnabled(true);
								mSignature.save(mView);

								task.setState(Constants.TASK_SIGNED);
								task.setWorkDone(((EditText) dialogView.findViewById(R.id.RecordWO_WorkDone)).getText().toString());
								maintainDS.open();
								maintainDS.updateTask(task);
								maintainDS.close();
								
								
								((TaskDetailTabFragment)getTargetFragment()).refreshTask(task.getTaskID());
								//RecordWorkDialogFragment.this.getDialog().dismiss();
								// getTargetFragment()
								/*((TabsFragmentActivity) getActivity())
										.doPositiveClick();
								*///mListener.onDialogPositiveClick(dialogView);
								//mListener.notify();
							}
						})
				.setNegativeButton(R.string.button_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								RecordWorkDialogFragment.this.getDialog()
										.cancel();
							}
						});

		return builder.create();
	}

	// Override the Fragment.onAttach() method to instantiate the
	// RecordWorkDialogFragment
	
	/*public void onAttach(Fragment fragment) {
		//super.onAttach(fragment); // Verify that the host activity implements
									// the callback interface
		try { // Instantiate the RecordWorkDialogFragment so we can send events
				// to the host
			mListener = (RecordWorkDialogListner) fragment;
		} catch (ClassCastException e) { // The activity doesn't implement the
											// interface, throw exception
			throw new ClassCastException(fragment.toString()
					+ " must implement RecordWorkDialogListner");
		}
	}
*/
	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance())
			getDialog().setOnDismissListener(null);
		super.onDestroyView();
	}

	// Signature Capture

	private String getTodaysDate() {

		final Calendar c = Calendar.getInstance();
		int todaysDate = (c.get(Calendar.YEAR) * 10000)
				+ ((c.get(Calendar.MONTH) + 1) * 100)
				+ (c.get(Calendar.DAY_OF_MONTH));
		Log.w("DATE:", String.valueOf(todaysDate));
		return (String.valueOf(todaysDate));

	}

	private String getCurrentTime() {

		final Calendar c = Calendar.getInstance();
		int currentTime = (c.get(Calendar.HOUR_OF_DAY) * 10000)
				+ (c.get(Calendar.MINUTE) * 100) + (c.get(Calendar.SECOND));
		Log.w("TIME:", String.valueOf(currentTime));
		return (String.valueOf(currentTime));

	}

	private boolean prepareDirectory() {
		try {
			if (makedirs()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(
					this.getActivity().getApplicationContext(),
					"Could not initiate File System.. Is Sdcard mounted properly?",
					1000).show();
			return false;
		}
	}

	private boolean makedirs() {
		File tempdir = new File(tempDir);
		if (!tempdir.exists())
			tempdir.mkdirs();

		if (tempdir.isDirectory()) {
			File[] files = tempdir.listFiles();
			for (File file : files) {
				if (!file.delete()) {
					System.out.println("Failed to delete " + file);
				}
			}
		}
		return (tempdir.isDirectory());
	}

	public class signature extends View {
		private static final float STROKE_WIDTH = 5f;
		private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
		private Paint paint = new Paint();
		private Path path = new Path();

		private float lastTouchX;
		private float lastTouchY;
		private final RectF dirtyRect = new RectF();

		public signature(Context context, AttributeSet attrs) {
			super(context, attrs);
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(STROKE_WIDTH);
		}

		public void save(View v) {
			Log.v("log_tag", "Width: " + v.getWidth());
			Log.v("log_tag", "Height: " + v.getHeight());
			if (mBitmap == null) {
				mBitmap = Bitmap.createBitmap(mContent.getWidth(),
						mContent.getHeight(), Bitmap.Config.RGB_565);
				;
			}
			Canvas canvas = new Canvas(mBitmap);
			try {
				FileOutputStream mFileOutStream = new FileOutputStream(mypath);

				v.draw(canvas);
				mBitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
				mFileOutStream.flush();
				mFileOutStream.close();
				String url = Images.Media.insertImage(getActivity()
						.getContentResolver(), mBitmap, "title", null);
				Log.v("log_tag", "url: " + url);
				// In case you want to delete the file
				// boolean deleted = mypath.delete();
				// Log.v("log_tag","deleted: " + mypath.toString() + deleted);
				// If you want to convert the image to string use base64
				// converter

			} catch (Exception e) {
				Log.v("log_tag", e.toString());
			}
		}

		public void clear() {
			path.reset();
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawPath(path, paint);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float eventX = event.getX();
			float eventY = event.getY();
			// mGetSign.setEnabled(true);

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				path.moveTo(eventX, eventY);
				lastTouchX = eventX;
				lastTouchY = eventY;
				return true;

			case MotionEvent.ACTION_MOVE:

			case MotionEvent.ACTION_UP:

				resetDirtyRect(eventX, eventY);
				int historySize = event.getHistorySize();
				for (int i = 0; i < historySize; i++) {
					float historicalX = event.getHistoricalX(i);
					float historicalY = event.getHistoricalY(i);
					expandDirtyRect(historicalX, historicalY);
					path.lineTo(historicalX, historicalY);
				}
				path.lineTo(eventX, eventY);
				break;

			default:
				debug("Ignored touch event: " + event.toString());
				return false;
			}

			invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
					(int) (dirtyRect.top - HALF_STROKE_WIDTH),
					(int) (dirtyRect.right + HALF_STROKE_WIDTH),
					(int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

			lastTouchX = eventX;
			lastTouchY = eventY;

			return true;
		}

		private void debug(String string) {
		}

		private void expandDirtyRect(float historicalX, float historicalY) {
			if (historicalX < dirtyRect.left) {
				dirtyRect.left = historicalX;
			} else if (historicalX > dirtyRect.right) {
				dirtyRect.right = historicalX;
			}

			if (historicalY < dirtyRect.top) {
				dirtyRect.top = historicalY;
			} else if (historicalY > dirtyRect.bottom) {
				dirtyRect.bottom = historicalY;
			}
		}

		private void resetDirtyRect(float eventX, float eventY) {
			dirtyRect.left = Math.min(lastTouchX, eventX);
			dirtyRect.right = Math.max(lastTouchX, eventX);
			dirtyRect.top = Math.min(lastTouchY, eventY);
			dirtyRect.bottom = Math.max(lastTouchY, eventY);
		}
	}
}
