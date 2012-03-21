package kr.ggogun.foursquare;

import java.util.ArrayList;

import kr.ggogun.daummap.DaumMapActivity;
import kr.ggogun.foursquare.FoursquareApp.FsqAuthListener;
import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
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
	    private SeekBar seekBar;
	    private TextView countOfData;
	    
	    public static boolean isDaum = false;
	 
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
	        seekBar						= (SeekBar) findViewById(R.id.seekbar);
	        countOfData					= (TextView) findViewById(R.id.count);
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
	 
	        mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					
					FsqVenue tmp = mNearbyList.get(position);
			
					String[] mapList = {"Google", "Daum"};
					
//					Builder dialog = new AlertDialog.Builder(FoursquareExampleActivity.this);
//					dialog.setTitle("Select Map");
//					
//					dialog.setSingleChoiceItems(mapList, -1, new DialogInterface.OnClickListener() {
//			            public void onClick(DialogInterface dialog, int item) {
//			            	if(item == 1){
//			            		isDaum = true;
//			            	}
//			              dialog.dismiss();
//			            }
//					});
//
//					dialog.show();
					
				//	if(isDaum == true){
						double[] loc = {tmp.location.getLatitude(), tmp.location.getLongitude()};
						 Log.d("PBS","Send  " +  Double.toString(loc[0]) + ",  " + Double.toString(loc[1]));
						Intent intent =  new Intent(FoursquareExampleActivity.this, DaumMapActivity.class);
						
						intent.putExtra("location", loc);
						startActivity(intent);
					//}
					
					
					
				}
	        	
	        	
			});
	        
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
	 
	                double lat, lon;
	                
	                if (latitude.equals("") || longitude.equals("")) {
	                    Toast.makeText(FoursquareExampleActivity.this, "Latitude or longitude is empty", Toast.LENGTH_SHORT).show();
	                	lat  = Double.valueOf("37.22174313");
		                lon  = Double.valueOf("127.18788824");
	                  // return;
	                }else{	 
		                lat  = Double.valueOf(latitude);
		                lon  = Double.valueOf(longitude);
	                }
		                
	              
	                
	                
	                
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
	                   
	                    
	                    
	                    	  
	                    countOfData.setText(mFsqApp.getCountData());
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