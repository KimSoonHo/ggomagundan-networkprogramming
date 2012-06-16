package kr.ggogun.manbogi;

import android.location.Location;
import com.google.android.maps.OverlayItem;
import java.util.ArrayList;
import java.util.List;

public class RunInfo
{
  public static List<OverlayItem> items;
  public static float kcal;
  public static Location lastLocation;
  public static int lastTime;
  public static ArrayList<String> list_kcal;
  public static ArrayList<String> list_saveTimie;
  public static ArrayList<String> list_stepCnt;
  public static ArrayList<String> list_timerBuffer;
  public static ArrayList<String> list_title;
  public static ArrayList<String> list_totalDistance;
  public static String saveTime;
  public static float speedA;
  public static int stepCnt;
  public static String timerBuffer;
  public static String title;
  public static float totalDistance;
  public static float userWeight;

  public static void cleanList()
  {
    list_title.clear();
    list_saveTimie.clear();
    list_totalDistance.clear();
    list_timerBuffer.clear();
    list_kcal.clear();
    list_stepCnt.clear();
  }

  public static void initialization()
  {
    title = "";
    saveTime = "";
    items = new ArrayList();
    lastLocation = null;
    totalDistance = 0.0F;
    speedA = 0.0F;
    timerBuffer = "";
    lastTime = 0;
    userWeight = 0.0F;
    kcal = 0.0F;
    stepCnt = 0;
    list_title = new ArrayList();
    list_saveTimie = new ArrayList();
    list_totalDistance = new ArrayList();
    list_timerBuffer = new ArrayList();
    list_kcal = new ArrayList();
    list_stepCnt = new ArrayList();
  }

  public static String[] listToarrary()
  {
    String[] arrayOfString = new String[list_saveTimie.size()];
    Object[] arrayOfObject = list_saveTimie.toArray();
    int i = 0;
    while (true)
    {
      int j = arrayOfObject.length;
      if (i >= j)
        return arrayOfString;
      String str = arrayOfObject[i].toString();
      arrayOfString[i] = str;
      i += 1;
    }
  }

  public static String[] logarr()
  {
    String[] arrayOfString = new String[6];
    String str1 = title;
    arrayOfString[0] = str1;
    String str2 = saveTime;
    arrayOfString[1] = str2;
    String str3 = String.valueOf(totalDistance);
    arrayOfString[2] = str3;
    String str4 = timerBuffer;
    arrayOfString[3] = str4;
    String str5 = String.valueOf(kcal);
    arrayOfString[4] = str5;
    String str6 = String.valueOf(stepCnt);
    arrayOfString[5] = str6;
    return arrayOfString;
  }
}

