package kr.ggogun.manbogi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Ready extends Activity
  implements View.OnClickListener, RadioGroup.OnCheckedChangeListener
{
  Boolean bool_gps;
  Button btn_gps;
  Button btn_internt;
  Button goButton;
  boolean isMobileConn;
  boolean isWifiConn;
  Boolean kgOrlb;
  LocationManager locationManager;
  RadioGroup radio_weight;
  TextView tv_gps;
  TextView tv_internet;
  EditText weight;

  public Ready()
  {
    Boolean localBoolean = Boolean.valueOf(true);
    this.kgOrlb = localBoolean;
  }

  private void moveConfigGPS(String paramString)
  {
    Intent localIntent = null;
    if (paramString == "gps")
      localIntent = new Intent("android.settings.LOCATION_SOURCE_SETTINGS");
    while (true)
    {
      startActivity(localIntent);
      checkStat();
      localIntent = new Intent("android.settings.WIRELESS_SETTINGS");
      if (paramString != "internet") return;
        continue;
      
    }
  }

  public void checkStat()
  {
    LocationManager localLocationManager = (LocationManager)getSystemService("location");
    this.locationManager = localLocationManager;
    Boolean localBoolean1=true;
    Boolean localBoolean2;
    if (!this.locationManager.isProviderEnabled("gps"))
    {
      this.tv_gps.setTextColor(Color.RED);
      this.tv_gps.setText("연결되지 않았습니다");
      localBoolean1 = Boolean.valueOf(false);
    }else{
    	this.tv_gps.setTextColor(Color.BLUE);
        this.tv_gps.setText("연결되었음");
    }
    
    this.bool_gps = localBoolean1; 
    while(true)
    {
      ConnectivityManager localConnectivityManager = (ConnectivityManager)getSystemService("connectivity");
      boolean bool1 = localConnectivityManager.getNetworkInfo(1).isConnected();
      this.isWifiConn = bool1;
      boolean bool2 = localConnectivityManager.getNetworkInfo(0).isConnected();
      this.isMobileConn = bool2;
      if ((this.isWifiConn) || (this.isMobileConn))
        break;
      this.tv_internet.setTextColor(Color.RED);
      this.tv_internet.setText(" 연결되지 않았습니다");
      
      this.tv_gps.setTextColor(Color.BLUE);
      this.tv_gps.setText("연결");
      localBoolean2 = Boolean.valueOf(true);
      return;
    }
    if (this.isWifiConn)
    {
      this.tv_internet.setTextColor(Color.BLUE);
      this.tv_internet.setText(" Wifi 연결 됨");
      return;
    }
    if (!this.isMobileConn){
     
    this.tv_internet.setTextColor(Color.BLUE);
    this.tv_internet.setText(" 3G 연결 됨");
    return;
    }
  }

  public void goRun()
  {
    RunInfo.userWeight = Float.parseFloat(this.weight.getText().toString());
    RunInfo.totalDistance = 0.0F;
    RunInfo.lastLocation = null;
    if (!this.kgOrlb.booleanValue())
      RunInfo.userWeight = (float)(RunInfo.userWeight * 0.454D);
    Intent localIntent = new Intent(this, Running.class);
    startActivity(localIntent);
    finish();
  }

  public void onCheckedChanged(RadioGroup paramRadioGroup, int paramInt)
  {
    if (paramInt == 2131034136)
    {
      Boolean localBoolean1 = Boolean.valueOf(true);
      this.kgOrlb = localBoolean1;
      return;
    }
    if (paramInt != 2131034137)
      return;
    Boolean localBoolean2 = Boolean.valueOf(false);
    this.kgOrlb = localBoolean2;
  }

  public void onClick(View paramView)
  {
    Button localButton1 = this.btn_gps;
    if (paramView == localButton1)
    {
      moveConfigGPS("gps");
      return;
    }
    Button localButton2 = this.btn_internt;
    if (paramView == localButton2)
    {
      moveConfigGPS("internet");
      return;
    }
    Button localButton3 = this.goButton;
    if (paramView != localButton3)
      return;
    if (!this.bool_gps.booleanValue())
    {
      Toast.makeText(this, "GPS연결을 확인하세요", 0).show();
      return;
    }
    if ((!this.isWifiConn) && (!this.isMobileConn))
    {
      Toast.makeText(this, "인터넷 확인하세요", 0).show();
      return;
    }
    if (this.weight.length() > 1)
    {
      goRun();
      return;
    }
    Toast.makeText(this, "몸무게를 입력하세요", 0).show();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.main);
    TextView localTextView1 = (TextView)findViewById(R.id.text_set_gps);
    this.tv_gps = localTextView1;
    TextView localTextView2 = (TextView)findViewById(R.id.text_set_internet);
    this.tv_internet = localTextView2;
    Button localButton1 = (Button)findViewById(R.id.GoButton);
    this.goButton = localButton1;
    Button localButton2 = (Button)findViewById(R.id.btn_gps);
    this.btn_gps = localButton2;
    Button localButton3 = (Button)findViewById(R.id.btn_internet);
    this.btn_internt = localButton3;
    EditText localEditText = (EditText)findViewById(R.id.EditText01);
    this.weight = localEditText;
    RadioGroup localRadioGroup = (RadioGroup)findViewById(R.id.radio_group);
    this.radio_weight = localRadioGroup;
    this.radio_weight.setOnCheckedChangeListener(this);
    this.btn_gps.setOnClickListener(this);
    this.btn_internt.setOnClickListener(this);
    this.goButton.setOnClickListener(this);
    RunInfo.initialization();
    checkStat();
  }

  protected void onPause()
  {
    checkStat();
    super.onPause();
  }

  protected void onResume()
  {
    checkStat();
    super.onResume();
  }
}
