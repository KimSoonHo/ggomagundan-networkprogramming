package com.qualcomm.QCARSamples.ImageTargets;

import java.io.IOException;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Geometric {
	
 private Location locations;
	Context mContext;
	
	public Geometric(Context con){
		this.mContext = con;
	}
	
	
	
	
	public void checkMyLocation(){
		
		LocationManager locationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
		 boolean isGPS = locationManager.isProviderEnabled (LocationManager.GPS_PROVIDER);
		   Criteria criteria = new Criteria();
		 
		   
		   String provider = locationManager.getBestProvider(criteria, true);
		   
		
		   if(isGPS == false){
			   Log.d("PBS", "Now Gps OFF");
			  // Toast.makeText(mContext, "if turn on GPS, you can be served high accuracy" , 5000).show();
			   AlertDialog alert = new AlertDialog.Builder( mContext )
				.setMessage( "if turn on GPS, you can be served high accuracy" )
				.setPositiveButton( "OK", new DialogInterface.OnClickListener()
				
				{
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				})
				.show();
		   }
		  
		 //if gps off, getting network service
		if(provider == null){ 
		    Toast.makeText(mContext, "no GPS Provider", Toast.LENGTH_SHORT).show();
		    
		    provider = LocationManager.NETWORK_PROVIDER;
		    locations = locationManager.getLastKnownLocation(provider);
		   }
		   
		   locations = locationManager.getLastKnownLocation(provider);
		   
		   locationManager.requestLocationUpdates(provider, 10000, 100, new MyLocationListener());
		   
		   if(locations == null){
		    try{
		     Thread.sleep(10000);
		    }catch(InterruptedException e){
		     e.printStackTrace();
		    }
		    locations = locationManager.getLastKnownLocation(provider);    
		   }
		   
		   getAddress();
		  }
		  
		  private class MyLocationListener implements LocationListener{

		   public void onLocationChanged(Location location) {
		    // TODO Auto-generated method stub
		    locations = location;
		    getAddress();
		   }

		   public void onProviderDisabled(String provider) {
		    // TODO Auto-generated method stub
		    
		   }

		   public void onProviderEnabled(String provider) {
		    // TODO Auto-generated method stub
	    
		   }

		   public void onStatusChanged(String provider, int status,
		     Bundle extras) {
		    // TODO Auto-generated method stub
		    
		   }
		   
		  }



		//요긴 주소 알아내기.. 위도와 경도값을 알고있으면 구할수 있다.

		public void getAddress() {
		  Geocoder geoCoder = new Geocoder(mContext);
		  double lat = locations.getLatitude();
		  double lng = locations.getLongitude();
		  
		  List<Address> addresses = null;
		  try {
		   addresses = geoCoder.getFromLocation(lat, lng, 5);
		  } catch (IOException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
		  if(addresses.size()>0){
		   Address mAddress = addresses.get(0);
		   //String Area = mAddress.getCountryName();
		   String mAddressStr = lat + ", " + lng + "  " 
				   +"Phone is near this address - "+mAddress.getCountryName()+" "
		       +mAddress.getPostalCode()+" "
		       +mAddress.getLocality()+" "
		       +mAddress.getThoroughfare()+" "
		       +mAddress.getFeatureName();
		   
		   Log.d("PBS", mAddressStr);
		   //Toast.makeText(getBaseContext(), mAddressStr, Toast.LENGTH_SHORT).show();
		  }
		 }
		//출처:[Android] GPS 좌표 받기, 주소알아내기

}
