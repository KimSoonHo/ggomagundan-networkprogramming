package kr.ggogun.onoffmix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import kr.ggogun.onoffmix.data.JSONItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

public class OnoffmixRssActivity extends Activity {
	

	private ListView mListView;// = (ListView)findViewById(R.id.list);
	//private EditText txt = (EditText) findViewById(R.id.text);
	
	
	private ResultListAdapter mAdapter       ;// = new ResultListAdapter(this);
	private ArrayList<JSONItem> mJsonList   ;//  = new ArrayList<JSONItem>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mListView = (ListView)findViewById(R.id.list);
		//private EditText txt = (EditText) findViewById(R.id.text);
		mAdapter        = new ResultListAdapter(this);
		mJsonList     = new ArrayList<JSONItem>();
		
		for(int j =0 ;j < 8;j++){
		String readOnoffMix = readOnOffMixFeed(j+1);
		try {
			JSONArray jsonArray = new JSONArray(readOnoffMix);
			Log.i("PBS",
					"Number of entries " + jsonArray.length());
			JSONObject json = jsonArray.getJSONObject(0);
			JSONArray ja = json.getJSONArray("eventList");
			
			
			for(int i =0;i < ja.length();i++){
				mJsonList.add(new JSONItem(ja.getJSONObject(i)));
				Log.d("PBS",ja.getJSONObject(i).getString("idx") + " ==>   " + ja.getJSONObject(i).getString("title") );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		
	
		mJsonList.add(new JSONItem());
		mJsonList.add(new JSONItem());
		mJsonList.add(new JSONItem());
		mJsonList.add(new JSONItem());
		mJsonList.add(new JSONItem());
		mJsonList.add(new JSONItem());
		mAdapter.setData(mJsonList);
		mListView.setAdapter(mAdapter);
	}

	public String readOnOffMixFeed(int num) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(
				"http://onoffmix.com/_/xmlProxy/xmlProxy.ofm?url=api.onoffmix.com/event/list&output=json&isExposed=1&pageRows=8&recruitEndDateTimeGT=2012-03-20+20:44:36&sort=isSiteRecommend&page="+num);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			Log.d("PBS","statu " + statusCode);
			if (statusCode == 200) {
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					builder.append(line);
					Log.d("PBS",line);
				}
			} else {
				Log.e("PBS", "Failed to download file");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "["+builder.toString()+"]";
	}
}
