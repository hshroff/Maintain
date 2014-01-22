/**
 *
 */
package com.freyasystems.maintain2.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.freyasystems.maintain2.dao.MaintainDS;
import com.freyasystems.maintain2.dao.MediaItemVO;
import com.freyasystems.maintain2.dao.StepContentVO;
import com.freyasystems.maintain2.dao.StepDataRuleVO;
import com.freyasystems.maintain2.dao.StepVO;
import com.freyasystems.maintain2.dao.TaskVO;
import com.google.gson.Gson;

/**
 * This class contains all sync logic (get and put) for the application.
 *
 * @author shroffh Freya Systems, LLC.
 */
public class SyncActivity extends Activity {

	private static String TAG = "SyncActivity";
	private String SYNC_IP = "192.168.1.9:8080";

	private String BASE_URL = "http://" + SYNC_IP + "/MaintainOps/rest/sync"; // "http://192.168.1.10:8080/MaintainOps/rest/sync";

	private String media_url = BASE_URL + "/download?mediaItemID=";
	private String video_media_url = BASE_URL + "/stream?mediaItemID=";
	private String send_status_task_url = BASE_URL + "/updateTask";
	private String send_status_step_url = BASE_URL + "/updateStep";

	private MaintainDS maintainDS;
	private MediaHelper mediaHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SYNC_IP = getPrefValue("pref_sync_url");
		sync();
	}

	public void sync() {
		// Get device ID
		// Pull data from URL
		// Put data to URL b
		// Resolve conflicts, if any
		// Populate local database

		mediaHelper = new MediaHelper(getApplicationContext());

		syncTasks();

		/*
		 * sycnNotes();
		 */
		// Once done, refresh the tasks list and display
		finish();
	}

	private void getMediaItemVideo(MediaItemVO mediaItem) {
		String url = video_media_url + mediaItem.getMediaItemID();

		try {
			byte[] content = new SyncByteArray().execute(url).get();
			Log.d(TAG, "Got content from " + url + ", content lenght is "
					+ content.length);

			String mCurrentPhotoPath = mediaHelper.save(mediaItem, content);

			// Add media to gallery
			Intent mediaScanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			File f = new File(mCurrentPhotoPath);
			Uri contentUri = Uri.fromFile(f);
			mediaScanIntent.setData(contentUri);
			this.sendBroadcast(mediaScanIntent);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.getLocalizedMessage());
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.getLocalizedMessage());
		}
	}

	private void getMediaItemImage(MediaItemVO mediaItem) {
		String url = media_url + mediaItem.getMediaItemID();

		try {

			Bitmap downloadedBitmap = new SyncMedia().execute(url).get();
			Log.d(TAG, "Got bitmap from " + url + ", bitmap is "
					+ downloadedBitmap.getByteCount());
			String mCurrentPhotoPath = mediaHelper.saveImage(mediaItem,
					downloadedBitmap);

			// Add picture to gallery
			Intent mediaScanIntent = new Intent(
					Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
			File f = new File(mCurrentPhotoPath);
			Uri contentUri = Uri.fromFile(f);
			mediaScanIntent.setData(contentUri);
			this.sendBroadcast(mediaScanIntent);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.getLocalizedMessage());
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d(TAG, e.getLocalizedMessage());
		}
	}

	private void syncTasks() {
		Gson gson = new Gson();

		// Load Tasks
		String tasks_url = BASE_URL + "/tasks?assignedTo="
				+ getPrefValue("pref_assignedTo");
		JSONArray results;
		// ArrayList<TaskVO> tasks = new ArrayList<TaskVO>();
		maintainDS = new MaintainDS(getApplicationContext());
		Log.d(TAG, "tasks_url: " + tasks_url);

		try {

			results = (JSONArray) new SyncTask().execute(tasks_url).get();
			Log.d(TAG, "jsonArray is " + results.toString());
			// tasksJson = (JSONObject) new SyncTask().execute(tasks_url).get();

			maintainDS.open();
			// maintainDS.removeLocalData();
			for (int j = 0; j < results.length(); j++) {
				try {
					JSONObject jsonObject = (JSONObject) results.get(j);
					Log.d(TAG, "Task JSON: " + jsonObject.toString());

					TaskVO task = gson.fromJson(jsonObject.toString(),
							TaskVO.class);

					Log.d(TAG, "Task WO: " + task.getWorkOrderNumber());

					TaskVO existingTask = maintainDS.getTask(task.getTaskID());
					if (existingTask != null) {
						Log.d(TAG, existingTask.getTaskID() + " task already exists, send status back to server...");
						sendStatus(existingTask);
					} else {
						Log.d(TAG, task.getTaskID() + " task is new, so create DB record...");
						addTask(task);
					}

					Log.d(TAG, "Complete");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			maintainDS.close();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendStatus(TaskVO task) throws InterruptedException, ExecutionException {
		//Task status
		SendStatus sendStatusRequest = new SendStatus();
		String taskResult = "";
		StringBuilder url = new StringBuilder(send_status_task_url)
				.append("?")
				.append("taskID=").append(task.getTaskID())
				.append("&state=").append(task.getState());

		taskResult = sendStatusRequest.execute(url.toString()).get();

		Log.d(TAG, "sendStatus taskResult " + taskResult);

		//Steps status
		List<StepVO> steps = maintainDS.getSteps(task);
		for (StepVO step : steps) {
			SendStatus sendStepStatusRequest = new SendStatus();

			StringBuilder stepUrl = new StringBuilder(send_status_step_url)
			.append("?")
			.append("stepID=").append(step.getStepID())
			.append("&state=").append(step.getState());

			String stepResult = sendStepStatusRequest.execute(stepUrl.toString()).get();
			Log.d(TAG, "sendStatus stepResult " + stepResult);
		}
	}

	private void addTask(TaskVO task) {
		maintainDS.addTask(task);

		// Save all task steps
		Log.d(TAG, "Task " + task.getTaskID() + " steps...");
		for (StepVO step : task.getSteps()) {
			Log.d(TAG, "-- Step: " + step.getStepID());

			// Adding WO and TL to step, since they aren't provided
			// in the server copy
			step.setWorkOrderNumber(task.getWorkOrderNumber());
			step.setTailNumber(task.getTailNumber());
			step.setTaskID(task.getTaskID());
			maintainDS.addStep(step);

			// Save all step content
			for (StepContentVO content : step.getStepcontents()) {
				content.setStepID(step.getStepID());

				maintainDS.addStepContent(content);

				if (content.getMediaItemID() != 0) {
					Log.d(TAG,
							"Content " + content.getStepName()
									+ " has a media item with id of "
									+ content.getMediaItemID());
					// Create record in mediaitem table
					maintainDS.addMediaItem(content.getMediaItem());

					if (content.getMediaItem().getType().equals("MP4")) {
						// download video
						getMediaItemVideo(content.getMediaItem());
					} else {
						// download image
						getMediaItemImage(content.getMediaItem());
					}

				}

			}

			// Save all step data rules
			Log.d(TAG, "---Step " + step.getStepID() + " data rules...");
			for (StepDataRuleVO rule : step.getStepdatarules()) {
				Log.d(TAG, "--- Data Rule: " + rule.getRuleName());
				rule.setWorkOrderNumber(task.getWorkOrderNumber());
				rule.setTailNumber(task.getTailNumber());
				rule.setStepID(step.getStepID());
				rule.setTaskID(task.getTaskID());
				maintainDS.addStepDataRule(rule);
			}
		}
	}

	private void sycnNotes() {
		// Upload Notes to server
		Gson gson = new Gson();
		JSONArray results;
		String notes_url = BASE_URL + "/new_notes";
		maintainDS.open();
		StepVO step = maintainDS.getStep(1);
		List<StepDataRuleVO> datarules = maintainDS.getStepDataRuleNotes(step);
		maintainDS.close();

		results = null; // reset results json array

		JSONArray notesJsonArray = new JSONArray();

		for (StepDataRuleVO datarule : datarules) {
			if (datarule.getNote() != null) {
				notesJsonArray.put(gson.toJson(datarule.getNote()));
			}
		}

		Log.d(TAG, "Got " + notesJsonArray.length()
				+ " notes to send, JsonArray is " + notesJsonArray.toString());

		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("notesJson", notesJsonArray
					.toString()));
			// params.add(new BasicNameValuePair("notesJson",
			// "my json string"));
			results = (JSONArray) new SyncTask().execute(notes_url, params)
					.get();
			Log.d(TAG, "jsonArray is " + results.toString());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getPrefValue(String pref_name) {
		SharedPreferences sharedPrefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		return sharedPrefs.getString(pref_name, "");
	}
}
