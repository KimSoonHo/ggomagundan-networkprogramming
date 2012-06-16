package kr.ggogun.manbogi;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class Running extends Activity
  implements LocationListener, SensorEventListener
{
  private static final int DATA_X = 0;
  private static final int DATA_Y = 1;
  private static final int DATA_Z = 2;
  public static int SHAKE_THRESHOLD = 800;
  static int oldTime;
  TextView Coordinates;
  final int DURATION = 2000;
  private Sensor accelerormeterSensor;
  int cnt = 0;
  TextView cntTv;
  int curTime;
  float distance;
  String distanceToStart;
  TextView gpsTv;
  String howFast = "";
  ImageView img_right;
  private long lastTime;
  private float lastX;
  private float lastY;
  private float lastZ;
  TextView latTv;
  LocationManager location = null;
  TextView lonTv;
  TextView seekTitle;
  SeekBar seekbar;
  private SensorManager sensorManager;
  private float speed;
  TextView speedTv;
  TextView stepTv;
  Button stopButton;
  boolean switchLocation = true;
  double temp = 0.0D;
  double thisTime;
  private float x;
  private float y;
  private float z;

  public static void secToHHMMSS(int paramInt)
  {
    int i = paramInt % 60;
    int j = paramInt / 60 % 60;
    int k = paramInt / 3600;
    Object[] arrayOfObject = new Object[3];
    Integer localInteger1 = Integer.valueOf(k);
    arrayOfObject[0] = localInteger1;
    Integer localInteger2 = Integer.valueOf(j);
    arrayOfObject[1] = localInteger2;
    Integer localInteger3 = Integer.valueOf(i);
    arrayOfObject[2] = localInteger3;
    RunInfo.timerBuffer = String.format("%02d:%02d:%02d", arrayOfObject);
    RunInfo.lastTime = paramInt;
  }

  public GeoPoint getPoint(double paramDouble1, double paramDouble2)
  {
    int i = (int)(paramDouble1 * 0.0F);
    int j = (int)(paramDouble2 * 0.0F);
    return new GeoPoint(i, j);
  }

  public void onAccuracyChanged(Sensor paramSensor, int paramInt)
  {
  }

  public void onBackPressed()
  {
    double d1 = System.currentTimeMillis();
    this.thisTime = d1;
    double d2 = this.thisTime;
    double d3 = this.temp;
    if (d2 - d3 < 2000.0D)
    {
      ActivityManager localActivityManager = (ActivityManager)getSystemService("activity");
      String str = getPackageName();
      localActivityManager.restartPackage(str);
    }
    double d4 = this.thisTime;
    this.temp = d4;
    Toast.makeText(this, "종료하려면 뒤로가기 버튼을 한번 더 누르세요", 2000).show();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(R.layout.gorun);
    setRequestedOrientation(1);
    getWindow().addFlags(128);
    SensorManager localSensorManager = (SensorManager)getSystemService("sensor");
    this.sensorManager = localSensorManager;
    Sensor localSensor = this.sensorManager.getDefaultSensor(1);
    this.accelerormeterSensor = localSensor;
    Button localButton1 = (Button)findViewById(R.id.StopButton);
    this.stopButton = localButton1;
    TextView localTextView1 = (TextView)findViewById(R.id.TextView04);
    this.lonTv = localTextView1;
    TextView localTextView2 = (TextView)findViewById(R.id.TextView05);
    this.latTv = localTextView2;
    TextView localTextView3 = (TextView)findViewById(R.id.TextView06);
    this.cntTv = localTextView3;
    TextView localTextView4 = (TextView)findViewById(R.id.TextView07);
    this.speedTv = localTextView4;
    TextView localTextView5 = (TextView)findViewById(R.id.TextView08);
    this.gpsTv = localTextView5;
    TextView localTextView6 = (TextView)findViewById(R.id.TextView09);
    this.stepTv = localTextView6;
    TextView localTextView7 = (TextView)findViewById(R.id.TextViewTitle);
    this.seekTitle = localTextView7;
    SeekBar localSeekBar1 = (SeekBar)findViewById(R.id.SeekBar01);
    this.seekbar = localSeekBar1;
    ImageView localImageView = (ImageView)findViewById(R.id.img_right);
    this.img_right = localImageView;
    RunInfo.items.clear();
    oldTime = (int)System.currentTimeMillis() / 1000;
    LocationManager localLocationManager1 = (LocationManager)getSystemService("location");
    this.location = localLocationManager1;
    Criteria localCriteria = new Criteria();
    localCriteria.setAccuracy(1);
    localCriteria.setPowerRequirement(2);
    String str = this.location.getBestProvider(localCriteria, true);
    LocationManager localLocationManager2 = this.location;
    Running localRunning = this;
    localLocationManager2.requestLocationUpdates(str, 3000L, 10.0F, localRunning);
    this.stopButton.setEnabled(false);
    Button localButton2 = this.stopButton;
    View.OnClickListener local1 = new View.OnClickListener()
    {
      public void onClick(View paramView)
      {
        Running.this.switchLocation = false;
        Running localRunning = Running.this;
        Intent localIntent = new Intent(localRunning, CreateMap.class);
        Running.this.startActivity(localIntent);
        Running.this.finish();
      }
    };
    localButton2.setOnClickListener(local1);
    SeekBar localSeekBar2 = this.seekbar;
    SeekBar.OnSeekBarChangeListener local2 = new SeekBar.OnSeekBarChangeListener()
    {
      public void onProgressChanged(SeekBar paramSeekBar, int paramInt, boolean paramBoolean)
      {
        TextView localTextView = Running.this.seekTitle;
        StringBuilder localStringBuilder = new StringBuilder("만보계 민감도 설정 : ");
        String str1 = String.valueOf(paramInt + 600);
        String str2 = str1;
        localStringBuilder.append(str2);
        localTextView.setText(str2);
        Running.SHAKE_THRESHOLD = paramInt + 600;
      }

      public void onStartTrackingTouch(SeekBar paramSeekBar)
      {
      }

      public void onStopTrackingTouch(SeekBar paramSeekBar)
      {
      }
    };
    localSeekBar2.setOnSeekBarChangeListener(local2);
    this.img_right.setImageResource(2130837508);
  }

  public void onLocationChanged(Location paramLocation)
  {
    int i = (int)System.currentTimeMillis() / 1000;
    this.curTime = i;
    if (paramLocation.hasSpeed())
    {
      RunInfo.speedA = paramLocation.getSpeed();
      String str1 = Float.toString(RunInfo.speedA);
      this.howFast = str1;
      TextView localTextView1 = this.speedTv;
      StringBuilder localStringBuilder1 = new StringBuilder("속도 : ");
      String str2 = Float.toString(paramLocation.getSpeed());
      String str3 = str2 + " m/s";
      localTextView1.setText(str3);
      if (this.howFast.length() >= 4)
      {
        String str4 = this.howFast.substring(0, 4);
        this.howFast = str4;
      }
    }
    TextView localTextView2 = this.lonTv;
    StringBuilder localStringBuilder2 = new StringBuilder("경도 : ");
    String str5 = Double.toString(paramLocation.getLongitude());
    String str6 = str5;
    localTextView2.setText(str6);
    TextView localTextView3 = this.latTv;
    StringBuilder localStringBuilder3 = new StringBuilder("위도 : ");
    String str7 = Double.toString(paramLocation.getLatitude());
    String str8 = str7;
    localTextView3.setText(str8);
    TextView localTextView4 = this.cntTv;
    StringBuilder localStringBuilder4 = new StringBuilder("수신좌표 : ");
    int j = this.cnt;
    int str9 = j;
    localTextView4.setText(str9);
    int k = this.cnt + 1;
    this.cnt = k;
    if (this.cnt == 1)
    {
      this.img_right.setImageResource(2130837509);
      this.gpsTv.setText("");
      Toast.makeText(this, "이제 달리세요!", 0).show();
      this.stopButton.setEnabled(true);
    }
    while (true)
    {
      if (RunInfo.lastLocation != null)
      {
        Location localLocation = RunInfo.lastLocation;
        float f1 = paramLocation.distanceTo(localLocation);
        this.distance = f1;
      }
      RunInfo.lastLocation = paramLocation;
      float f2 = RunInfo.totalDistance;
      float f3 = this.distance;
      RunInfo.totalDistance = f2 + f3;
      String str10 = Integer.toString((int)RunInfo.totalDistance);
      this.distanceToStart = str10;
      int m = this.curTime;
      int n = oldTime;
      secToHHMMSS(m - n);
      if (!this.switchLocation)
        return;
      List localList = RunInfo.items;
      double d1 = paramLocation.getLatitude();
      double d2 = paramLocation.getLongitude();
      GeoPoint localGeoPoint = getPoint(d1, d2);
      String str11 = String.valueOf(this.distanceToStart);
      StringBuilder localStringBuilder5 = new StringBuilder(str11).append("m").append(", ");
      String str12 = RunInfo.timerBuffer;
      StringBuilder localStringBuilder6 = localStringBuilder5.append(str12).append(", ");
      String str13 = this.howFast;
      StringBuilder localStringBuilder7 = localStringBuilder6.append(str13).append("m/s").append(", ");
      int i1 = RunInfo.stepCnt;
      String str14 = i1 + "걸음";
      OverlayItem localOverlayItem = new OverlayItem(localGeoPoint, str14, "");
      boolean bool = localList.add(localOverlayItem);
      
      this.img_right.setImageResource(R.drawable.right_blue);
      return;
    }
  }

  public void onProviderDisabled(String paramString)
  {
  }

  public void onProviderEnabled(String paramString)
  {
  }

  public void onSensorChanged(SensorEvent paramSensorEvent)
  {
    if (paramSensorEvent.sensor.getType() != 1)
      return;
    long l1 = System.currentTimeMillis();
    long l2 = this.lastTime;
    long l3 = l1 - l2;
    if (l3 <= 100L)
      return;
    this.lastTime = l1;
    int i = (int) paramSensorEvent.values[0];
    this.x = i;
    int j = (int) paramSensorEvent.values[1];
    this.y = j;
    int k = (int) paramSensorEvent.values[2];
    this.z = k;
    float f1 = this.x;
    float f2 = this.y;
    float f3 = f1 + f2;
    float f4 = this.z;
    float f5 = f3 + f4;
    float f6 = this.lastX;
    float f7 = f5 - f6;
    float f8 = this.lastY;
    float f9 = f7 - f8;
    float f10 = this.lastZ;
    float f11 = Math.abs(f9 - f10);
    float f12 = (float)l3;
    float f13 = f11 / f12 * 10000.0F;
    this.speed = f13;
    float f14 = this.speed;
    float f15 = SHAKE_THRESHOLD;
    if (f14 > f15)
    {
      RunInfo.stepCnt += 1;
      TextView localTextView = this.stepTv;
      StringBuilder localStringBuilder = new StringBuilder("만보계: ");
      
      String str = Integer.toString(RunInfo.stepCnt);
      Log.d("PBS",str);
      localStringBuilder.append(str);
      localTextView.setText(localStringBuilder);
    }
    int n = (int) paramSensorEvent.values[0];
    this.lastX = n;
    int i1 = (int) paramSensorEvent.values[1];
    this.lastY = i1;
    int i2 = (int) paramSensorEvent.values[2];
    this.lastZ = i2;
  }

  protected void onStart()
  {
    super.onStart();
    if (this.accelerormeterSensor == null)
      return;
    SensorManager localSensorManager = this.sensorManager;
    Sensor localSensor = this.accelerormeterSensor;
    boolean bool = localSensorManager.registerListener(this, localSensor, 1);
  }

  public void onStatusChanged(String paramString, int paramInt, Bundle paramBundle)
  {
  }

  protected void onStop()
  {
    super.onStop();
    if (this.sensorManager == null)
      return;
    this.sensorManager.unregisterListener(this);
  }
}

