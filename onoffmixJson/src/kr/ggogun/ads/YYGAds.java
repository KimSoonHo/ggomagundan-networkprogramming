package kr.ggogun.ads;

import kr.ggogun.onoffmix.R;
import android.content.Intent;
import android.net.Uri;
import android.sax.StartElementListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.app.Activity;

public class YYGAds {
	
	ImageView ads;
	int random;
	
	public YYGAds(ImageView view){
		
		ads = view;
		makeRandom();
		ads.setImageResource(setAds());
		
	}

	public int getRandom(){
		
		return random;
		
	}
	private int setAds() {
		
		switch(random){
		case 1:
			return R.drawable.ads;
		case 2:
			return R.drawable.ads1;
		}
		return 0;
		
	}

	private void makeRandom() {

		random = 1 + (int)(Math.random()*2);
		
	}

	
}
