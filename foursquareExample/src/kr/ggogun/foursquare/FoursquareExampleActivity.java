package kr.ggogun.foursquare;

import java.util.ArrayList;

import kr.ggogun.foursquare.FoursquareApp.FsqAuthListener;

import android.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/*
Client ID QE3M2XVKOZNHF3CW4NYZ1LLZYZQOXVDMAFORKI1KKH3HOPEN

Client Secret QOZCS5NDG5Q50KFXRCSFOR4W05BDZ3LTZDY5ENVWVE4XCUFL
 */



public class FoursquareExampleActivity extends Activity {
	 	private FoursquareApp mFsqApp;
	    private ListView mListView;
	    private NearbyAdapter mAdapter;
	    private ArrayList<FsqVenue> mNearbyList;
	    private ProgressDialog mProgress;
	 
	    public static final String CLIENT_ID = "QE3M2XVKOZNHF3CW4NYZ1LLZYZQOXVDMAFORKI1KKH3HOPEN";
	    public static final String CLIENT_SECRET = "QOZCS5NDG5Q50KFXRCSFOR4W05BDZ3LTZDY5ENVWVE4XCUFL";
	 
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	 
	        setContentView(R.layout.main);
	 
	        final TextView nameTv       = (TextView) findViewById(R.id.tv_name);
	        Button connectBtn           = (Button) findViewById(R.id.b_connect);
	        final EditText latitudeEt   = (EditText) findViewById(R.id.et_latitude);
	        final EditText longitudeEt  = (EditText) findViewById(R.id.et_longitude);
	        Button goBtn                = (Button) findViewById(R.id.b_go);
	        mListView                   = (ListView) findViewById(R.id.lv_places);
	 
	        mFsqApp         = new FoursquareApp(this, CLIENT_ID, CLIENT_SECRET);
	 
	        mAdapter        = new NearbyAdapter(this);
	        mNearbyList     = new ArrayList<FsqVenue>();
	        mProgress       = new ProgressDialog(this);
	 
	        mProgress.setMessage("Loading data ...");
	 
	        if (mFsqApp.hasAccessToken()) nameTv.setText("Connected as " + mFsqApp.getUserName());
	 
	        FsqAuthListener listener = new FsqAuthListener() {
	            @Override
	            public void onSuccess() {
	                Toast.makeText(FoursquareExampleActivity.this, "Connected as " + mFsqApp.getUserName(), Toast.LENGTH_SHORT).show();
	                nameTv.setText("Connected as " + mFsqApp.getUserName());
	            }
	 
	            @Override
	            public void onFail(String error) {
	                Toast.makeText(FoursquareExampleActivity.this, error, Toast.LENGTH_SHORT).show();
	            }
	        };
	 
	        mFsqApp.setListener(listener);
	 
	        //get access token and user name from foursquare
	        connectBtn.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                mFsqApp.authorize();
	            }
	        });
	 
	        //use access token to get nearby places
	        goBtn.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                String latitude  = latitudeEt.getText().toString();
	                String longitude = longitudeEt.getText().toString();
	 
	                if (latitude.equals("") || longitude.equals("")) {
	                    Toast.makeText(FoursquareExampleActivity.this, "Latitude or longitude is empty", Toast.LENGTH_SHORT).show();
	                    return;
	                }
	 
	                double lat  = Double.valueOf(latitude);
	                double lon  = Double.valueOf(longitude);
	 
	                loadNearbyPlaces(lat, lon);
	            }
	        });
	    }
	 
	    private void loadNearbyPlaces(final double latitude, final double longitude) {
	        mProgress.show();
	 
	        new Thread() {
	            @Override
	            public void run() {
	                int what = 0;
	 
	                try {
	 
	                    mNearbyList = mFsqApp.getNearby(latitude, longitude);
	                } catch (Exception e) {
	                    what = 1;
	                    e.printStackTrace();
	                }
	 
	                mHandler.sendMessage(mHandler.obtainMessage(what));
	            }
	        }.start();
	    }
	 
	    private Handler mHandler = new Handler() {
	        @Override
	        public void handleMessage(Message msg) {
	            mProgress.dismiss();
	 
	            if (msg.what == 0) {
	                if (mNearbyList.size() == 0) {
	                    Toast.makeText(FoursquareExampleActivity.this, "No nearby places available", Toast.LENGTH_SHORT).show();
	                    return;
	                }
	 
	                mAdapter.setData(mNearbyList);
	                mListView.setAdapter(mAdapter);
	            } else {
	                Toast.makeText(FoursquareExampleActivity.this, "Failed to load nearby places", Toast.LENGTH_SHORT).show();
	            }
	        }
	    };
}