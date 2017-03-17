package com.example.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

public class RequestServer extends AsyncTask<Object, Void, String> {

	public RequestResult delegate = null;

    @Override
    protected String doInBackground(Object... params) {

    	Map<String, String> q = (Map<String, String>) params[0];
    	
    	String path = (String) params[1];
    	
        Log.d("huyleonis", path);
        return postData(q, path);
    }
        

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("huyleonis", s);
        delegate.processFinish(s);
    }

    private String postData(Map<String, String> params, String path) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://prm391.azurewebsites.net/Android/" + path);
        
        
        List<NameValuePair> postParams = new ArrayList<NameValuePair>();
        if (params != null) {
        	
            for (Entry<String, String> entry : params.entrySet()) {
            	String name = entry.getKey();
            	String value;
				try {
					value = new String(entry.getValue().getBytes("UTF-8"), "UTF-8");
					postParams.add(new BasicNameValuePair(name, value));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}            	
            	            	
            	
    		}
        }

        try {
            // Add your data
            

            //httppost.setEntity(new StringEntity(query, HTTP.UTF_8));

            httppost.setEntity(new UrlEncodedFormEntity(postParams, "UTF-8"));


            // Execute HTTP Post Request

            ResponseHandler<String> responseHandler = new BasicResponseHandler();

            //This is the response from a java application
            String response = httpclient.execute(httppost, responseHandler);

            return response.trim();
        } catch (ClientProtocolException e) {
            Log.e("huyleonis", e.getMessage());
        	return "Exception: " + e.getClass() + " - " + e.getMessage();
        } catch (IOException e) {
        	Log.e("huyleonis", e.getMessage());
        	return "Exception: " + e.getClass() + " - " + e.getMessage();
        }        
    }

    public interface RequestResult {    	
        void processFinish(String result);
    }

}
