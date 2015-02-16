package com.cevik.cevikmuzik;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
public class CalmaListesiDB extends SQLiteOpenHelper{
    
   public SQLiteDatabase DB;
   public String DBPath;
   public static String DBName = "cevikmuzik";
   public static final int version = '1';
   public static Context currentContext;
   public static String tableName = "calmalisteleri";
    

   public CalmaListesiDB(Context context) {
       super(context, DBName, null, version);
       currentContext = context;
       DBPath = "/data/data/" + context.getPackageName() + "/databases";
       createDatabase();

   }

   @Override
   public void onCreate(SQLiteDatabase db) {
       // TODO Auto-generated method stub
        
   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // TODO Auto-generated method stub
        
   }

   private void createDatabase() {
       boolean dbExists = checkDbExists();
        
       if (dbExists) {
           // do nothing
       } else {
    	   //Eğer daha önce DB oluşturulmadıysa yaratıyorum. Denemek için Favorilerim diye bir şey ekledim, çalma listelerinde gözüküyor.
           DB = currentContext.openOrCreateDatabase(DBName, 0, null);
           DB.execSQL("CREATE TABLE IF NOT EXISTS " +
                   tableName +
                   " (calmalistesiadi VARCHAR);");
           DB.execSQL("CREATE TABLE IF NOT EXISTS sarkilar (calmalistesiadi VARCHAR, sarkiadi VARCHAR);");
            
           DB.execSQL("INSERT INTO " +
                   tableName +
                   " Values ('Favorilerim');");
           
       }
        
        
   }
   public boolean createCalmaListesi(String name) {
       boolean dbExists = checkDbExists();
        
       if (dbExists) {
    	   DB = currentContext.openOrCreateDatabase(DBName, 0, null);
           DB.execSQL("INSERT INTO calmalisteleri Values ("+DatabaseUtils.sqlEscapeString(name)+");");
           return true;
          
       } else {
    	   //Database hatası false.
    	   return false;
           
           
       }}
   public boolean createSarki(String songname, String clname) {
       boolean dbExists = checkDbExists();
        
       if (dbExists) {
    	   DB = currentContext.openOrCreateDatabase(DBName, 0, null);
    	   
           DB.execSQL("INSERT INTO sarkilar Values ("+DatabaseUtils.sqlEscapeString(clname)+","+DatabaseUtils.sqlEscapeString(songname)+");");
           return true;
          
       } else {
    	   //Database hatası false.
    	   return false;
           
           
       }}
   public boolean deleteSarki(String songname, String clname) {
       boolean dbExists = checkDbExists();
        
       if (dbExists) {
    	   DB = currentContext.openOrCreateDatabase(DBName, 0, null);
    	   
           DB.execSQL("DELETE FROM sarkilar WHERE calmalistesiadi="+DatabaseUtils.sqlEscapeString(clname)+" AND sarkiadi="+DatabaseUtils.sqlEscapeString(songname)+";");
           return true;
          
       } else {
    	   //Database hatası false.
    	   return false;
           
           
       }}
   
       public boolean deleteCalmaListesi(String name) {
           boolean dbExists = checkDbExists();
            
           if (dbExists) {
        	   DB = currentContext.openOrCreateDatabase(DBName, 0, null);
               DB.execSQL("DELETE FROM calmalisteleri WHERE calmalistesiadi="+DatabaseUtils.sqlEscapeString(name)+";");
               //Çalma listesini sildikten sonra içindeki şarkıları kaldırıyor.
               DB.execSQL("DELETE FROM sarkilar WHERE calmalistesiadi="+DatabaseUtils.sqlEscapeString(name)+";");

               return true;
              
           } else {
        	   //Database hatası false.
        	   return false;
               
               
           }
        
        
   }
   public boolean checkDbExists() {
       SQLiteDatabase checkDB = null;

       try {
           String myPath = DBPath +"/"+ DBName;
           checkDB = SQLiteDatabase.openDatabase(myPath, null,
                   SQLiteDatabase.OPEN_READONLY);

       } catch (SQLiteException e) {

           // database does't exist yet.

       }

       if (checkDB != null) {

           checkDB.close();

       }

       return checkDB != null ? true : false;
   }
}
