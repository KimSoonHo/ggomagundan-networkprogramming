package kr.ggogun.onoffmix;

import java.util.ArrayList;

import org.json.JSONObject;

import kr.ggogun.onoffmix.data.JSONItem;
import android.content.Context;
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
			convertView = mInflater.inflate(R.layout.test, null);

			holder = new ViewHolder();

			holder.mTitle = (TextView) convertView.findViewById(R.id.title);
			holder.mIsFree = (TextView) convertView.findViewById(R.id.isfree);
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
		if((jsonitem.isFree).equals("y")){
			holder.mIsFree.setText("FREE");
		}else{
			holder.mIsFree.setText("PAID");
		}
		
		holder.mDuring.setText(jsonitem.conferStartTime + " ~ " + jsonitem.conferEndtime);
		holder.mRecuit.setText(jsonitem.showStartTime + " ~ " + jsonitem.showEndTime);
		holder.mLocation.setText(jsonList.location);
		holder.mPeople.setText(jsonitem.currentAttend + "/" + jsonitem.totalAttend);
	//	holder.mEventImg.setImage();
		
		
		return convertView;
	}
	
	

	
	static class ViewHolder {
		TextView mTitle;
		TextView mIsFree;
		TextView mDuring;
		TextView mRecuit;
		TextView mLocation;
		TextView mPeople;
		ImageView mEventImg;
	}
}
