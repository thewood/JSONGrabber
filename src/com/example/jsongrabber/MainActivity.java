package com.example.jsongrabber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends Activity {

	public static String [] keywords = null;
	public static ArrayAdapter<String> adapter = null;
	private static String jsonString = null;
    private static int userId = 19;
    private static final String target = "http://www.growmyinstagram.com/instagram/api/api.php?userid=" + userId + "&";
	//ArrayList<JSONArray> array;
	JSONArray jsonArray;
	
	String key;
	String [] stringarray = new String[0];
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		refresh();	
	}
	public void refresh() {

		
		new BackGrabber().execute(target + "command=mykeywords");
		
		ListView list = (ListView) findViewById(R.id.keyList);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringarray); 
        list.setAdapter(adapter);
	}

    public void sendHandler(View view) {
        EditText keyText = (EditText) findViewById(R.id.keyText);
        key = keyText.getText().toString();
        new BackGrabber().execute(target + "keyword1="+key);
        refresh();

    }

	public void grabHandler(View view) {
		EditText keyText = (EditText) findViewById(R.id.keyText);
		key = keyText.getText().toString();
        Log.v("~~~","trying to put this in tokener: "+jsonString);
		try {
				JSONArray array = (JSONArray) new JSONTokener(jsonString).nextValue();

		        stringarray = new String[array.length()];
		        for (int i = 0; i < array.length(); i++) {
		            JSONObject jobj = array.getJSONObject(i);
		            stringarray[i] = (jobj.getString(key)); 
		            Log.v("***",stringarray[i]);

		        }
	            
		} catch (JSONException e) {
			Log.v("***","error parsing the file");
			e.printStackTrace();

		}
		refresh();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private class BackGrabber extends AsyncTask<String, Void, String> {
		
        @Override
        protected void onPreExecute() {

            super.onPreExecute();
        }

		@Override
		  protected String doInBackground(String... urls) {
		    // Making HTTP request
			String url = urls[0];
			StringBuilder builder = new StringBuilder();
            Log.d("***","about to http");
		    try {
		        DefaultHttpClient httpClient = new DefaultHttpClient();
		        HttpPost httpPost = new HttpPost(url);

		        HttpResponse httpResponse = httpClient.execute(httpPost);
		        HttpEntity httpEntity = httpResponse.getEntity();
		        InputStream is = httpEntity.getContent();    
		        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		        String line;
                Log.d("***","are we there yet?");
		        while ((line = reader.readLine()) != null) {
		            builder.append(line);
                    Log.v("---",line);
		        } 
		    } catch (UnsupportedEncodingException e) {
		        e.printStackTrace();
		    } catch (ClientProtocolException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		    
		   String result = builder.toString();
		   return result;
		   
		 }

		 @Override
		 protected void onPostExecute(String result) {
			 jsonString = result;

		 }
	}

}
