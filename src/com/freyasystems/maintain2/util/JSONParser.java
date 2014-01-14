/**
 * 
 */
package com.freyasystems.maintain2.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * @author shroffh
 * Freya Systems, LLC.
 */
public class JSONParser {
	
	static String TAG = "JSONParser";
	static InputStream is = null;
	static JSONObject jObj = null;
	static JSONArray jArray = null;
	static String json = "";
	
	/**
	 * 
	 */
	public JSONParser() {
		// TODO Auto-generated constructor stub
	}

	// function get json from url
	// by making HTTP POST or GET mehtod
	public JSONArray makeHttpRequest(String url, String method,
	        List<NameValuePair> params) throws IOException {

	    // Making HTTP request
	    try {

	        // check for request method
	        if(method == "POST"){
	            // request method is POST
	            // defaultHttpClient
/*	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            HttpPost httpPost = new HttpPost(url);
	            httpPost.setEntity(new UrlEncodedFormEntity(params));

	            HttpResponse httpResponse = httpClient.execute(httpPost);

	            HttpEntity httpEntity = httpResponse.getEntity();
	            is = httpEntity.getContent();
*/	            
	        	String charset = "UTF-8";
	        	
	            URL urlObject = new URL(url);
	            URLConnection urlConnection = urlObject.openConnection();
	            
	            if(params != null){
	              	//String paramString = URLEncodedUtils.format(params, charset);
	            	//String paramString = URLEncoder.encode((params.get(0)).getValue(), charset);
	            	
	            	//Log.d(TAG, "paramString = " + paramString);
		        	
		        	urlConnection.setDoOutput(true);
		        	urlConnection.setRequestProperty("Accept-Charset", charset);
		        	urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

		        	
		        	UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params);
					OutputStream output = null;
					try {
						output = urlConnection.getOutputStream();
						formEntity.writeTo(output);
					} finally {
						if (output != null)
							try {
								output.close();
							} catch (IOException ioe) {
							}
					}
		        	
		        	/*
		            OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
		            
		            for(NameValuePair nvp : params){
		            	writer.write(nvp.getName() + "=" + URLEncoder.encode((params.get(0)).getValue(), charset));
		            }
		            
		            //writer.write(paramString);
		            writer.flush();*/
	            }
	            	            
	            is = new BufferedInputStream(urlConnection.getInputStream());
	            
	        }else if(method == "GET"){
	        	//NOT USED...DELETE THIS
	            // request method is GET
	            DefaultHttpClient httpClient = new DefaultHttpClient();
	            
	            String paramString = URLEncodedUtils.format(params, "utf-8");
	            url += "?" + paramString;
	            HttpGet httpGet = new HttpGet(url);

	            HttpResponse httpResponse = httpClient.execute(httpGet);
	            HttpEntity httpEntity = httpResponse.getEntity();
	            is = httpEntity.getContent();
	        }
	    } catch (UnsupportedEncodingException e) {
	        e.printStackTrace();
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (Exception ex) {
	        Log.d("Networking", ex.getStackTrace().toString());
	        throw new IOException("Error connecting");
	    }

	    try {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(
	                is, "iso-8859-1"), 8);
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            sb.append(line + "\n");
	        }
	        is.close();
	        json = sb.toString();
	    } catch (Exception e) {
	        Log.e("Buffer Error", "Error converting result " + e.toString());
	    }

	    // try parse the string to a JSON object
	    try {
	    	jArray = new JSONArray(json);
	    } catch (JSONException e) {
	        Log.e("JSON Parser", "Error parsing data " + e.toString());
	    }

	    // return JSON String
	    return jArray;
	}
}
