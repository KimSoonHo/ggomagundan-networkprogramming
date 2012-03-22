package kr.ggogun.onoffmix;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import kr.ggogun.onoffmix.data.JSONItem;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultListAdapter extends BaseAdapter {
	private ArrayList<JSONItem> mJsonList;
	private LayoutInflater mInflater;
	

	public ResultListAdapter(Context c) {
		mInflater = LayoutInflater.from(c);
	}
	
	public void setData(ArrayList<JSONItem> poolList) {
		mJsonList = poolList;
		
	}
	
	@Override
	public int getCount() {
		return mJsonList.size();
	}

	@Override
	public Object getItem(int position) {
		return mJsonList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.jsonitemview, null);

			holder = new ViewHolder();

			holder.mTitle = (TextView) convertView.findViewById(R.id.title);
			holder.mIsFree = (ImageView) convertView.findViewById(R.id.isfree);
			holder.mDuring = (TextView) convertView.findViewById(R.id.schedule);
			holder.mRecuit = (TextView) convertView.findViewById(R.id.recuitday);
			holder.mLocation = (TextView) convertView.findViewById(R.id.location);
			holder.mPeople = (TextView) convertView.findViewById(R.id.people);
			holder.mEventImg = (ImageView) convertView.findViewById(R.id.img);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		JSONItem jsonList = mJsonList.get(position);
		JSONItem jsonitem = (JSONItem)getItem(position); 
		holder.mTitle.setText(jsonList.title);
		if((jsonitem.isFree).equals("n")){
			holder.mIsFree.setImageResource(R.drawable.paid);
		}else{
			holder.mIsFree.setImageResource(R.drawable.free);
		}
		
		holder.mDuring.setText(jsonitem.conferStartTime + " \n     ~ " + jsonitem.conferEndtime);
		holder.mRecuit.setText(jsonitem.showStartTime + " ~ " + jsonitem.showEndTime);
		holder.mLocation.setText(jsonList.location);
		holder.mPeople.setText(jsonitem.currentAttend + "/" + jsonitem.totalAttend);
		
		URL url;
		try {
		//	Log.d("PBS","Banner URL : " + jsonitem.bannerUrl);
			url = new URL(jsonitem.bannerUrl);
			
			URLConnection conn =
					 url.openConnection();
					conn.connect();
					BufferedInputStream  bis = new BufferedInputStream(conn.getInputStream());
					Bitmap bm = BitmapFactory.decodeStream(bis); bis.close();
					holder.mEventImg.setImageBitmap(bm); 
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return convertView;
	}
	
	

	
	static class ViewHolder {
		TextView mTitle;
		ImageView mIsFree;
		TextView mDuring;
		TextView mRecuit;
		TextView mLocation;
		TextView mPeople;
		ImageView mEventImg;
	}
}
