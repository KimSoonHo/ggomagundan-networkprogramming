package kr.ggogun.onoffmix.data;

import org.json.JSONException;
import org.json.JSONObject;


public class JSONItem {
		public String eventId;
		public String bannerUrl;
        public String title;
        public String location;
        public String conferStartTime;
        public String conferEndtime;
        public String showStartTime;
        public String showEndTime;
        public String isFree;
        public String eventurl;
        public String currentAttend;
        public String totalAttend;
        
        public JSONItem(){
        	eventId = "";
        	title = "this is title";
            location = "this is location";
            conferStartTime="startITme";
            conferEndtime="ednTime";
            showStartTime="stime";
            showEndTime="etime";
            isFree="free";
            eventurl="url";
            currentAttend="cur";
            totalAttend="totla";
        	
        }
        
        JSONObject json = new JSONObject();

		public JSONItem(JSONObject jsonObject) throws JSONException {
			super();
			eventId=jsonObject.getString("eventIdx");
			bannerUrl = jsonObject.getString("bannerUrl");
			title = jsonObject.getString("title");
			location = jsonObject.getString("location");
			conferStartTime = jsonObject.getString("eventStartDateTime");
			conferEndtime = jsonObject.getString("eventEndDateTime");
			showStartTime=jsonObject.getString("recruitStartDateTime");
			showEndTime=jsonObject.getString("recruitEndDateTime");
			isFree=jsonObject.getString("isFree");
			eventurl=jsonObject.getString("eventUrl");
			currentAttend=jsonObject.getString("totalAttend");
			totalAttend=jsonObject.getString("totalCapacity");
		}
        
}

        