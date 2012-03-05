package kr.ggogun.geolist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class GeometricActivity extends Activity {
	
	private ArrayList<HashMap<String, Object>> totalList;
	private static final String NAMEKEY = "name";
	private static final String DESC_STR = "description";
	private static final String IMGKEY = "IMG";
	int currentSize =0;
    List<ScanResult> results;
    ListView listView;
    ProgressDialog dialog;
    SeekBar bar;
    SimpleAdapter adapter;
    
    int maxProgess=10;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        listView = (ListView)findViewById(R.id.list);
        bar = (SeekBar)findViewById(R.id.seekBar1);
        totalList = new ArrayList<HashMap<String,Object>>();
        
         	 
        Button btns = (Button) findViewById(R.id.morebtn);
        
        addMore();
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading. Please wait...");
        
        btns.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.show();
				new Thread(new Runnable(){
		    		public void run(){
						try {
							 Thread.sleep(1000);
							 dialog.dismiss();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		    		}
			    }).start();
				addMore();
				listView.setAdapter(adapter);
			}
		});
        

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
			}

			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				Log.d("PBS","current " + seekBar.getProgress());
				maxProgess = (seekBar.getProgress()+10)/5*5;
				alertMessage();
				if(maxProgess < currentSize){
					removeItem();
				}
			}

			
        	
        });
        
        
        adapter = new SimpleAdapter(this, totalList, R.layout.listbox, 
        		new String[]{NAMEKEY,DESC_STR,IMGKEY}, new int[]{R.id.text1,R.id.text2,R.id.img});
       		
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); 
 
        listView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				
				int position = arg0.getPositionForView(arg1);
				HashMap<String, Object> item = new HashMap<String, Object>();  
				item = totalList.get(position);
				
				AlertDialog.Builder alert = new AlertDialog.Builder(GeometricActivity.this);
				alert.setIcon(R.drawable.happy);
				alert.setTitle(item.get(NAMEKEY).toString() + " Connect?");
				alert.setMessage(item.get(NAMEKEY).toString()+ " 연결?");
				alert.setPositiveButton("확인",
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int id) {

	                        	dialog.cancel();

	                        }
	                    });
				alert.setNegativeButton("취소",
	                    new DialogInterface.OnClickListener() {
	                        public void onClick(DialogInterface dialog, int id) {
	                            dialog.cancel();
	                        }
	                    });

				alert.show();
			}
		
        
        });
     
     
       
   
    }
    
    private void removeItem() {

    	dialog.show();
		new Thread(new Runnable(){
    		public void run(){
				try {
					 Thread.sleep(1000);
					 dialog.dismiss();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
    		}
	    }).start();
    	Log.d("PBS", "max p " + maxProgess + "  " + "cur " + currentSize);
    	for(int i = maxProgess; i < currentSize;i++){
    		totalList.remove(maxProgess);
    	}
    	listView.setAdapter(adapter);
    	currentSize = maxProgess;
    	alertMessage();
	}
    
    public void alertMessage(){
    	

    	Toast.makeText(this, "Current Max List is " + maxProgess, Toast.LENGTH_SHORT).show();
    }
   
    
    public void addMore(){
    	
    	
    	
    	
    	
    	
    	if(currentSize+5 > maxProgess){
    		AlertDialog alert = new AlertDialog.Builder(this)
    		.setIcon(R.drawable.happy)
    		.setTitle("Exceed MAX List")
    		.setMessage("If you want more List, Move the SeekBar")
    		.setPositiveButton( "OK", new DialogInterface.OnClickListener()
    		{
    		
	    		public void onClick(DialogInterface dialog, int which)
	    		{
	    			dialog.dismiss();
	    		}
    		}).show();
    		
    	}else{
	    	for(int i = currentSize;i < currentSize+5;i++){
		        HashMap<String, Object> map = new HashMap<String, Object>();
		        map.put(NAMEKEY, (i+1) + "name");
		        map.put(DESC_STR, (i+1) + " desc");
		        switch(i%4){
		        case 0:{
		        	map.put(IMGKEY, R.raw.one );
		        	break;
		        }
		        case 1:
		        	map.put(IMGKEY, R.raw.two );
		        	break;
		        case 2:
		        	map.put(IMGKEY, R.raw.three );
		        	break;
		        case 3:
		        	map.put(IMGKEY, R.raw.four );
		        	break;
		        }
		        
		        totalList.add(map);
	        }
	    	
	    	
	    	
	    	currentSize+=5;
	    	
    	}
	    			
    }
    
    
    
}