package kr.ggogun.batterystatus;

import android.R.integer;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.TextView;

public class BatteryMainActivity extends Activity {
	
	private TextView contentTxt, deviceWidth, deviceHeight;  
	private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){  
		@Override  
		public void onReceive(Context arg0, Intent intent) {  
			// TODO Auto-generated method stub  
			int level = intent.getIntExtra("level", 0);  
			contentTxt.setText(String.valueOf(level) + "%");  
		}  
	};  

	@Override  
	public void onCreate(Bundle icicle) {  
		super.onCreate(icicle);  
		setContentView(R.layout.main);  
		contentTxt = (TextView) this.findViewById(R.id.status);  
		deviceWidth= (TextView) this.findViewById(R.id.width);
		deviceHeight= (TextView) this.findViewById(R.id.height);
		
		WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		
		deviceWidth.setText(Integer.toString(display.getWidth()));
		deviceHeight.setText(Integer.toString(display.getHeight()));
		
		
		this.registerReceiver(this.mBatInfoReceiver,   
				new IntentFilter(Intent.ACTION_BATTERY_CHANGED));  
		
	}  
	
	

	
}