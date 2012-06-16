package kr.ggogun.manbogi;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.google.android.maps.Projection;

public class CreateMap extends MapActivity
{
  final int DURATION = 2000;
  int check_log = 0;
  double curTime;
  Dialog customDialogInstance;
  double lastTime = 0.0D;
  private RunDBAdpter mDbHelper;
  MapView mapView = null;

  private void DialogSelectOption()
  {
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this);
    AlertDialog.Builder localBuilder2 = localBuilder1.setTitle("달리기 기록");
    String[] arrayOfString = RunInfo.listToarrary();
    DialogInterface.OnClickListener local3 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        CreateMap.this.check_log = paramInt;
      }
    };
    AlertDialog.Builder localBuilder3 = localBuilder1.setSingleChoiceItems(arrayOfString, 0, local3);
    DialogInterface.OnClickListener local4 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
      }
    };
    AlertDialog.Builder localBuilder4 = localBuilder3.setPositiveButton("기록보기", local4);
    DialogInterface.OnClickListener local5 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
      }
    };
    AlertDialog.Builder localBuilder5 = localBuilder4.setNegativeButton("닫기", local5);
    DialogInterface.OnClickListener local6 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        RunDBAdpter localRunDBAdpter = CreateMap.this.mDbHelper;
        ArrayList localArrayList = RunInfo.list_title;
        int i = CreateMap.this.check_log;
        String str = (String)localArrayList.get(i);
        if (localRunDBAdpter.deleteLogs(str))
        {
          Toast.makeText(CreateMap.this, "기록 삭제 성공", 0).show();
          return;
        }
        Toast.makeText(CreateMap.this, "기록 삭제 실패", 0).show();
      }
    };
    AlertDialog.Builder localBuilder6 = localBuilder5.setNeutralButton("삭제", local6);
    AlertDialog localAlertDialog = localBuilder1.show();
  }

  public void insertDB()
  {
    AlertDialog.Builder localBuilder1 = new AlertDialog.Builder(this);
    AlertDialog.Builder localBuilder2 = localBuilder1.setTitle("기록 저장");
    StringBuilder localStringBuilder = new StringBuilder("\"");
    String str1 = RunInfo.saveTime;
    String str2 = str1 + "\"\n" + "현재 기록을 저장 하시겠습니까?";
    AlertDialog.Builder localBuilder3 = localBuilder1.setMessage(str2);
    DialogInterface.OnClickListener local1 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        int i = 0;
        Cursor localCursor = CreateMap.this.mDbHelper.fetchAllNotes();
        if (!localCursor.moveToNext())
        {
          localCursor.close();
          if (i != 0){ 
        	  RunDBAdpter localRunDBAdpter = CreateMap.this.mDbHelper;
        	  String[] arrayOfString = RunInfo.logarr();
        	  if (localRunDBAdpter.createLogs(arrayOfString) != 0L)
          Toast.makeText(CreateMap.this, "저장 실패", 0).show();
          }
        }
        while (true)
        {
          paramDialogInterface.dismiss();
     
          String str1 = localCursor.getString(1);
          String str2 = RunInfo.title;
          if (!str1.equals(str2))
            break;
          i = 1;
          CreateMap localCreateMap = CreateMap.this;
          StringBuilder localStringBuilder = new StringBuilder("\"");
          String str3 = RunInfo.saveTime;
          String str4 = str3 + "\"" + " 기록 저장 완료";
          Toast.makeText(localCreateMap, str4, 0).show();
          
          Toast.makeText(CreateMap.this, "저장 기록이 존재합니다", 0).show();
          continue;
        }
      }
    };
    AlertDialog.Builder localBuilder4 = localBuilder1.setPositiveButton("저장", local1);
    DialogInterface.OnClickListener local2 = new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramDialogInterface, int paramInt)
      {
        paramDialogInterface.dismiss();
      }
    };
    AlertDialog.Builder localBuilder5 = localBuilder1.setNegativeButton("취소", local2);
    AlertDialog localAlertDialog = localBuilder1.show();
  }

  protected boolean isRouteDisplayed()
  {
    return false;
  }

  public void makeMapView()
  {
    this.mapView.setBuiltInZoomControls(true);
    this.mapView.setClickable(true);
    Drawable localDrawable = getResources().getDrawable(2130837504);
    int i = localDrawable.getIntrinsicWidth();
    int j = localDrawable.getIntrinsicHeight();
    localDrawable.setBounds(0, 0, i, j);
    List localList1 = this.mapView.getOverlays();
    SitesOverlay localSitesOverlay = new SitesOverlay(localDrawable);
    boolean bool = localList1.add(localSitesOverlay);
    List localList2 = RunInfo.items;
    int k = RunInfo.items.size() - 1;
    int m = ((OverlayItem)localList2.get(k)).getPoint().getLatitudeE6();
    List localList3 = RunInfo.items;
    int n = RunInfo.items.size() - 1;
    int i1 = ((OverlayItem)localList3.get(n)).getPoint().getLongitudeE6();
    GeoPoint localGeoPoint = new GeoPoint(m, i1);
    MapController localMapController = this.mapView.getController();
    localMapController.animateTo(localGeoPoint);
    int i2 = localMapController.setZoom(17);
  }

  public void onBackPressed()
  {
    double d1 = System.currentTimeMillis();
    this.curTime = d1;
    double d2 = this.curTime;
    double d3 = this.lastTime;
    if (d2 - d3 < 2000.0D)
    {
      ActivityManager localActivityManager = (ActivityManager)getSystemService("activity");
      String str = getPackageName();
      localActivityManager.restartPackage(str);
    }
    double d4 = this.curTime;
    this.lastTime = d4;
    Toast.makeText(this, "종료하려면 뒤로가기 버튼을 한 번 더 누르세요.", 2000).show();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    CreateMap localCreateMap1 = this;
    localCreateMap1.setContentView(R.layout.resmap);
    RunDBAdpter localRunDBAdpter1 = new RunDBAdpter(getApplicationContext());
    RunDBAdpter localRunDBAdpter2 = localRunDBAdpter1;
    CreateMap localCreateMap2 = this;
//    localRunDBAdpter2.<init>(localCreateMap2);
    RunDBAdpter localRunDBAdpter3 = localRunDBAdpter1;
    this.mDbHelper = localRunDBAdpter3;
    RunDBAdpter localRunDBAdpter4 = this.mDbHelper.open();
    CreateMap localCreateMap3 = this;
    MapView localMapView = (MapView)localCreateMap3.findViewById(R.id.mapview);
    this.mapView = localMapView;
    CreateMap localCreateMap4 = this;
    TextView localTextView1 = (TextView)localCreateMap4.findViewById(R.id.TextView01);
    CreateMap localCreateMap5 = this;
    TextView localTextView2 = (TextView)localCreateMap5.findViewById(R.id.TextView02);
    CreateMap localCreateMap6 = this;
    TextView localTextView3 = (TextView)localCreateMap6.findViewById(R.id.TextView03);
    CreateMap localCreateMap7 = this;
    TextView localTextView4 = (TextView)localCreateMap7.findViewById(R.id.TextView04);
    StringBuilder localStringBuilder1 = new StringBuilder("이동 거리 : ");
    int i2 = (int)RunInfo.totalDistance;
    String str1 = i2 + "m";
    localTextView1.setText(str1);
    StringBuilder localStringBuilder2 = new StringBuilder("달린 시간 : ");
    String str2 = RunInfo.timerBuffer;
    String str3 = str2;
    localTextView2.setText(str3);
    float f1 = RunInfo.userWeight;
    float f2 = RunInfo.lastTime;
    RunInfo.kcal = f1 * f2 * 0.0022F;
    String str4 = String.valueOf(Float.toString(RunInfo.kcal).substring(0, 5));
    String str5 = str4 + "kcal";
    String str6 = "칼로리 소모 : " + str5;
    localTextView3.setText(str6);
    StringBuilder localStringBuilder3 = new StringBuilder("만보계: ");
    int i3 = RunInfo.stepCnt;
    int str7 = i3;
    localTextView4.setText(str7);
    makeMapView();
    Calendar localCalendar = Calendar.getInstance();
    int i4 = localCalendar.get(1);
    int i5 = localCalendar.get(2) + 1;
    int i6 = localCalendar.get(5);
    int i7 = localCalendar.get(11);
    int i8 = localCalendar.get(12);
    RunInfo.title = String.valueOf(i4 + i5 + i6 + i7 + i8);
    String str8 = String.valueOf(i4);
    RunInfo.saveTime = String.valueOf(str8 + "." + i5 + "." + i6 + "_" + i7 + ":" + i8);
  }

  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    MenuItem localMenuItem1 = paramMenu.add(0, 1, 0, "기록 저장");
    MenuItem localMenuItem2 = paramMenu.add(0, 2, 0, "기록 관리");
    MenuItem localMenuItem3 = paramMenu.add(0, 3, 0, "다시 시작");
    return super.onCreateOptionsMenu(paramMenu);
  }

  public boolean onMenuItemSelected(int paramInt, MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    default:
    case 1:
    case 2:
    case 3:
    }
    while (true)
    {
      super.onMenuItemSelected(paramInt, paramMenuItem);
      insertDB();
      selectDB();
      Intent localIntent = new Intent(this, Ready.class);
      startActivity(localIntent);
      finish();
    }
  }

  public void selectDB()
  {
    RunInfo.cleanList();
    Cursor localCursor = this.mDbHelper.fetchAllNotes();
    while (true)
    {
      if (!localCursor.moveToNext())
      {
        localCursor.close();
        DialogSelectOption();
        return;
      }
      ArrayList<String> localArrayList1 = RunInfo.list_title;
      String str1 = localCursor.getString(1);
      boolean bool1 = localArrayList1.add(str1);
      ArrayList<String> localArrayList2 = RunInfo.list_saveTimie;
      String str2 = localCursor.getString(2);
      boolean bool2 = localArrayList2.add(str2);
      ArrayList<String> localArrayList3 = RunInfo.list_totalDistance;
      String str3 = localCursor.getString(3);
      boolean bool3 = localArrayList3.add(str3);
      ArrayList<String> localArrayList4 = RunInfo.list_timerBuffer;
      String str4 = localCursor.getString(4);
      boolean bool4 = localArrayList4.add(str4);
      ArrayList<String> localArrayList5 = RunInfo.list_kcal;
      String str5 = localCursor.getString(5);
      boolean bool5 = localArrayList5.add(str5);
      ArrayList<String> localArrayList6 = RunInfo.list_stepCnt;
      String str6 = localCursor.getString(6);
      boolean bool6 = localArrayList6.add(str6);
    }
  }

  private class SitesOverlay extends ItemizedOverlay<OverlayItem>
  {
    Drawable marker_F;
    Drawable marker_S;

    public SitesOverlay(Drawable arg2)
    {
      super(arg2);
      Drawable localDrawable2 = CreateMap.this.getResources().getDrawable(R.drawable.startmarker);
      this.marker_S = localDrawable2;
      Drawable localDrawable3 = CreateMap.this.getResources().getDrawable(R.drawable.stopmarker);
      this.marker_F = localDrawable3;
      Drawable localDrawable4 = this.marker_S;
      int i = this.marker_S.getIntrinsicWidth();
      int j = this.marker_S.getIntrinsicHeight();
      localDrawable4.setBounds(0, 0, i, j);
      Drawable localDrawable5 = this.marker_F;
      int k = this.marker_F.getIntrinsicWidth();
      int m = this.marker_F.getIntrinsicHeight();
      localDrawable5.setBounds(0, 0, k, m);
      OverlayItem localOverlayItem1 = (OverlayItem)RunInfo.items.get(0);
      Drawable localDrawable6 = this.marker_S;
      localOverlayItem1.setMarker(localDrawable6);
      List localList = RunInfo.items;
      int n = RunInfo.items.size() - 1;
      OverlayItem localOverlayItem2 = (OverlayItem)localList.get(n);
      Drawable localDrawable7 = this.marker_F;
      localOverlayItem2.setMarker(localDrawable7);
    //  Drawable localDrawable8 = boundCenter(localDrawable1);
      Drawable localDrawable9 = boundCenterBottom(this.marker_S);
      Drawable localDrawable10 = boundCenterBottom(this.marker_F);
      populate();
    }

    protected OverlayItem createItem(int paramInt)
    {
      return (OverlayItem)RunInfo.items.get(paramInt);
    }

    public void draw(Canvas paramCanvas, MapView paramMapView, boolean paramBoolean)
    {
      super.draw(paramCanvas, paramMapView, paramBoolean);
      Point localPoint1 = new Point();
      Point localPoint2 = new Point();
      Paint localPaint = new Paint();
      localPaint.setARGB(70, 255, 0, 0);
      localPaint.setStrokeWidth(2.0F);
      localPaint.setAntiAlias(true);
      int i = 0;
      while (true)
      {
        int j = RunInfo.items.size() - 1;
        if (i >= j)
          return;
        Projection localProjection1 = paramMapView.getProjection();
        GeoPoint localGeoPoint1 = ((OverlayItem)RunInfo.items.get(i)).getPoint();
        Point localPoint3 = localProjection1.toPixels(localGeoPoint1, localPoint1);
        Projection localProjection2 = paramMapView.getProjection();
        List localList = RunInfo.items;
        int k = i + 1;
        GeoPoint localGeoPoint2 = ((OverlayItem)localList.get(k)).getPoint();
        Point localPoint4 = localProjection2.toPixels(localGeoPoint2, localPoint2);
        float f1 = localPoint1.x;
        float f2 = localPoint1.y;
        float f3 = localPoint2.x;
        float f4 = localPoint2.y;
        paramCanvas.drawLine(f1, f2, f3, f4, localPaint);
        i += 1;
      }
    }

    protected boolean onTap(int paramInt)
    {
      CreateMap localCreateMap = CreateMap.this;
      String str = ((OverlayItem)RunInfo.items.get(paramInt)).getTitle();
      Toast.makeText(localCreateMap, str, 0).show();
      return true;
    }

    public int size()
    {
      return RunInfo.items.size();
    }
  }
}

