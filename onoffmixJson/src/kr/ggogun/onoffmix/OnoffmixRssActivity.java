package kr.ggogun.onoffmix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import kr.ggogun.ads.YYGAds;
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


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class OnoffmixRssActivity extends Activity {
	

	private ListView mListView;
	private ImageView adsBox;
	
	private int random;
	private ResultListAdapter mAdapter       ;
	private ArrayList<JSONItem> mJsonList   ;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTitle(R.string.title);
		mListView = (ListView)findViewById(R.id.list);
		adsBox = (ImageView) findViewById(R.id.ads);
		mAdapter        = new ResultListAdapter(this);
		mJsonList     = new ArrayList<JSONItem>();
		
		Toast.makeText(this,"Loading ......", Toast.LENGTH_LONG).show();
    	
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
				//Log.d("PBS",ja.getJSONObject(i).getString("idx") + " ==>   " + ja.getJSONObject(i).getString("title") );
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		
	
		YYGAds ad = new YYGAds(adsBox);
		random = ad.getRandom();
		adsBox.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = null;
				if(random ==1){
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://sanchaeexpo.co.kr/"));
				}else if(random == 2){
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://jihun.yyg.go.kr/home/jihun/artfestival/outline"));
				}
				startActivity(intent);
				
			}
		});
		mAdapter.setData(mJsonList);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				  	JSONItem data = mJsonList.get(position);
	                
	                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(data.eventurl));
	                
	                startActivity(intent);
				
			}
		
		
		
		});
			
			
		
		
        
       
   
			
			
		
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
         //super.onListItemClick(l, v, pzosition, id);
         
         JSONItem data = mJsonList.get(position);
         
         Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(data.eventurl));
         
         startActivity(intent);
 }
	
	
	
	public String readOnOffMixFeed(int num) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		
		
		HttpGet httpGet = new HttpGet(
				"http://onoffmix.com/_/xmlProxy/xmlProxy.ofm?url=api.onoffmix.com/event/list&output=json&isExposed=1&pageRows=8&recruitEndDateTimeGT="
						+ getCurrentTime() + "&sort=isSiteRecommend&page="+num);
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

	private String getCurrentTime() {
		SimpleDateFormat formatter = new SimpleDateFormat ( "yyyy-MM-dd+HH:mm:ss", Locale.KOREA );
		Date currentTime = new Date ( );
		String dTime = formatter.format ( currentTime );
		return dTime;
				
	}
	
	
}
