package com.example.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class RequestServer extends AsyncTask<String, Void, String> {

	public RequestResult delegate = null;

    @Override
    protected String doInBackground(String... params) {

        Log.d("huyleonis", params[0]);
        return postData(params[0], params[1]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.d("huyleonis", s);
        delegate.processFinish(s);
    }

    private String postData(String toPost, String url) {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        //This is the data to send
        String query = toPost; //any data to send

        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            byte[] utf8 = query.getBytes("UTF-8");
            query = new String(utf8, "UTF-8");
            nameValuePairs.add(new BasicNameValuePair("query", query));

            //httppost.setEntity(new StringEntity(query, HTTP.UTF_8));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));


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
