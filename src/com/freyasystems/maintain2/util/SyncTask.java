/**
 * 
 */
package com.freyasystems.maintain2.util;

import java.io.IOException;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;

import android.os.AsyncTask;


/**
 * @author shroffh
 * Freya Systems, LLC.
 * @param <Progress>
 * @param <Params>
 * @param <Progress>
 * @param <Params>
 * @param <Result>
 */
public class SyncTask<Progress, Params> extends AsyncTask<Params, Progress, JSONArray> {

	String result = "";
	
	protected void onPreExecute() {
    }
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected JSONArray doInBackground(Params... arguments) {
		JSONParser jsonParser = new JSONParser();
		String url = (String)arguments[0];
		List<NameValuePair> params = null;
		try {
			if(arguments.length > 1)
				params = (List<NameValuePair>) arguments[1];
			
			return jsonParser.makeHttpRequest(url, "POST", params);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(Void v) {
		
    } // protected void onPostExecute(Void v)
}
