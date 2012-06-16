package kr.ggogun.manbogi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class RunDBAdpter
{
  private static final String DATABASE_CREATE = "create table runlog (_id integer primary key autoincrement, db_title text not null, db_saveTime text not null, db_totalDistance text not null, db_timerBuffer text not null, db_kcal text not null, db_stepCnt text not null);";
  private static final String DATABASE_NAME = "rundb";
  private static final String DATABASE_TABLE = "runlog";
  private static final int DATABASE_VERSION = 1;
  public static final String KEY_KCAL = "db_kcal";
  public static final String KEY_ROWID = "_id";
  public static final String KEY_SAVETIME = "db_saveTime";
  public static final String KEY_STEPCNT = "db_stepCnt";
  public static final String KEY_TIMERBUFFER = "db_timerBuffer";
  public static final String KEY_TITLE = "db_title";
  public static final String KEY_TOTALDISTANCE = "db_totalDistance";
  private static final String TAG = "RunBAdapter";
  private final Context mCtx;
  private SQLiteDatabase mDb;
  private DatabaseHelper mDbHelper;

  public RunDBAdpter(Context paramContext)
  {
    this.mCtx = paramContext;
  }

  public void close()
  {
    this.mDbHelper.close();
  }

  public long createLogs(String[] paramArrayOfString)
  {
    ContentValues localContentValues = new ContentValues();
    String str1 = paramArrayOfString[0];
    localContentValues.put("db_title", str1);
    String str2 = paramArrayOfString[1];
    localContentValues.put("db_saveTime", str2);
    String str3 = paramArrayOfString[2];
    localContentValues.put("db_totalDistance", str3);
    String str4 = paramArrayOfString[3];
    localContentValues.put("db_timerBuffer", str4);
    String str5 = paramArrayOfString[4];
    localContentValues.put("db_kcal", str5);
    String str6 = paramArrayOfString[5];
    localContentValues.put("db_stepCnt", str6);
    return this.mDb.insert("runlog", null, localContentValues);
  }

  public boolean deleteLogs(String paramString)
  {
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String str = "db_title=" + paramString;
    if (localSQLiteDatabase.delete("runlog", str, null) > 0);
    return true;
//    for (int i = 1; ; i = 0)
//      return i;
  }

  public Cursor fetchAllNotes()
  {
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String[] arrayOfString1 = new String[7];
    arrayOfString1[0] = "_id";
    arrayOfString1[1] = "db_title";
    arrayOfString1[2] = "db_saveTime";
    arrayOfString1[3] = "db_totalDistance";
    arrayOfString1[4] = "db_timerBuffer";
    arrayOfString1[5] = "db_kcal";
    arrayOfString1[6] = "db_stepCnt";
    String[] arrayOfString2 = null;
    String str1 = null;
    String str2 = null;
    String str3 = null;
    return localSQLiteDatabase.query("runlog", arrayOfString1, null, arrayOfString2, str1, str2, str3);
  }

  public Cursor fetchNote(String paramString)
    throws SQLException
  {
	  boolean bool;
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String[] arrayOfString = new String[6];
    arrayOfString[0] = "db_title";
    arrayOfString[1] = "db_saveTime";
    arrayOfString[2] = "db_totalDistance";
    arrayOfString[3] = "db_timerBuffer";
    arrayOfString[4] = "db_kcal";
    arrayOfString[5] = "db_stepCnt";
    String str1 = "db_title=" + paramString;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    String str5 = null;
    Cursor localCursor = localSQLiteDatabase.query(true, "runlog", arrayOfString, str1, null, str2, str3, str4, str5);
    if (localCursor != null)
     bool = localCursor.moveToFirst();
    return localCursor;
  }

  public RunDBAdpter open()
    throws SQLException
  {
    Context localContext = this.mCtx;
    DatabaseHelper localDatabaseHelper = new DatabaseHelper(localContext);
    this.mDbHelper = localDatabaseHelper;
    SQLiteDatabase localSQLiteDatabase = this.mDbHelper.getWritableDatabase();
    this.mDb = localSQLiteDatabase;
    return this;
  }

  public boolean updateNote(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("db_title", paramString1);
    localContentValues.put("db_saveTime", paramString2);
    localContentValues.put("db_totalDistance", paramString3);
    localContentValues.put("db_timerBuffer", paramString4);
    localContentValues.put("db_kcal", paramString5);
    localContentValues.put("db_stepCnt", paramString6);
    SQLiteDatabase localSQLiteDatabase = this.mDb;
    String str = "db_saveTime=" + paramString2;
    if (localSQLiteDatabase.update("runlog", localContentValues, str, null) > 0);
//    for (int i = 1; ; i = 0)
//      return i;
    return true;
  }

  private static class DatabaseHelper extends SQLiteOpenHelper
  {
    DatabaseHelper(Context paramContext)
    {
      super(paramContext, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL("create table runlog (_id integer primary key autoincrement, db_title text not null, db_saveTime text not null, db_totalDistance text not null, db_timerBuffer text not null, db_kcal text not null, db_stepCnt text not null);");
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      String str = "Upgrading database from version " + paramInt1 + " to " + paramInt2 + ", which will destroy all old data";
      int i = Log.w("RunBAdapter", str);
      paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS runlog");
      onCreate(paramSQLiteDatabase);
    }
  }
}

