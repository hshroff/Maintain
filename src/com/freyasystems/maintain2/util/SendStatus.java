/**
 * 
 */
package com.freyasystems.maintain2.util;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author shroffh Freya Systems, LLC.
 */
public class SendStatus extends AsyncTask<String, String, String> {
	//private HashMap<String, String> mData = null;// post data

	/**
	 * constructor
	 */
	public SendStatus() {
		//mData = data;
	}

	/**
	 * background
	 */
	@Override
	protected String doInBackground(String... params) {
		String str = "success";
		final AndroidHttpClient client = AndroidHttpClient
				.newInstance("Android");
		final HttpGet getRequest = new HttpGet(params[0]);
		try {
			HttpResponse response = client.execute(getRequest);
			final int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				Log.w("SendStatus", "Error " + statusCode
						+ " while updating status to " + params[0]);
				return null;
			}
			
		} catch (Exception e) {
			// Could provide a more explicit error message for IOException or
			// IllegalStateException
			getRequest.abort();
			Log.w("SendStatus", "Error " + e.getMessage()
					+ " while updating status to " + params[0]);
			str = "Error " + e.getMessage() + " while updating status to " + params[0];
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return str;
	}

	/**
	 * on getting result
	 */
	@Override
	protected void onPostExecute(String result) {
		// something...
	}
}
