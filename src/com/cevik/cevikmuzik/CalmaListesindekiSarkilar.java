package com.cevik.cevikmuzik;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemClickListener;
/*
 * CevikMuzik: Simple Music Player for Android
 * 
 * (c) 2014-2015 Canberk Güzeler, Ferhat Yeşiltarla, Mert Levent, Oğuz Kırat
 * 
 * 
 */
public class CalmaListesindekiSarkilar extends Activity {

	public String[] dizi=new String[5];
	private ArrayAdapter arrayAdapter;
	private ListView liste;
	public String[] sasarkiliste;
	String cladi;
	private ArrayList<String> results = new ArrayList<String>();
    private String tableName = CalmaListesiDB.tableName;
    private SQLiteDatabase newDB;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calmalistesindekisarkilar);
		Bundle extras = getIntent().getExtras();

		
		if (extras != null) {
		    cladi = extras.getString("cladi");
		  
		    ((TextView)findViewById(R.id.sanatcininadi)).setText(cladi);
		    
		}
		
		 openAndQueryDatabase();
         
         
         ListView listemiz = (ListView)findViewById(R.id.listView1);	
	        ArrayAdapter<String> veriAdaptoru=new ArrayAdapter<String>
	        (CalmaListesindekiSarkilar.this, android.R.layout.simple_list_item_1, android.R.id.text1, results);
	        listemiz.setAdapter(veriAdaptoru);
	       
	        listemiz.setOnItemClickListener(new OnItemClickListener() 
	        {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) {	                
	               	String item = ((TextView)view).getText().toString();
	               	Intent intent=new Intent(CalmaListesindekiSarkilar.this,SimdiCaliniyor.class);
	                intent.putExtra("sarkiadi", item);
	                intent.putExtra("albumadi", "");
	                intent.putExtra("sanatciadi", "");
	                intent.putExtra("sarkiadilistesi", results.toArray(new String[results.size()]));
	               // TODO:  intent.putExtra("sarkiidlistesi", getSarkiID(albumadi));
	                startActivity(intent);
	        		
	            	
	            
	                
	            }
	        });
	        listemiz.setLongClickable(true);
	        Toast.makeText(CalmaListesindekiSarkilar.this,"Çalma listesinden şarkı silmek için şarkıya basılı tutun.",Toast.LENGTH_SHORT).show();  

	        listemiz.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
	        	 public boolean onItemLongClick(AdapterView<?> arg0, View view,
	                     int pos, long id) {
	        		 String item = ((TextView)view).getText().toString();
	        		 CalmaListesiDB dbHelper = new CalmaListesiDB(CalmaListesindekiSarkilar.this.getApplicationContext());
			          newDB = dbHelper.getWritableDatabase();
			          dbHelper.deleteSarki(item,cladi);
			          Toast.makeText(CalmaListesindekiSarkilar.this,"Şarkı Çalma Listesinden Kaldırıldı",Toast.LENGTH_SHORT).show(); 	
			          Intent intent = new Intent(CalmaListesindekiSarkilar.this, MainActivity.class);
			          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
			          startActivity(intent);
	                 	

	                 return true;
	             }
	        	
	        });
	       
        
}
 private void openAndQueryDatabase() {
     try {
         CalmaListesiDB dbHelper = new CalmaListesiDB(CalmaListesindekiSarkilar.this.getApplicationContext());
         newDB = dbHelper.getWritableDatabase();
         Cursor c = newDB.rawQuery("SELECT sarkiadi FROM sarkilar WHERE calmalistesiadi="+DatabaseUtils.sqlEscapeString(cladi), null);

         if (c != null ) {
             if  (c.moveToFirst()) {
                 do {
                     String sarkiadi = c.getString(c.getColumnIndex("sarkiadi"));
                     results.add(sarkiadi);
                 }while (c.moveToNext());
             } 
         }           
     } catch (SQLiteException se ) {
         Log.e(getClass().getSimpleName(), "Could not create or Open the database");
     } finally {
         if (newDB != null) 
            // newDB.execSQL("DELETE FROM " + tableName);
             newDB.close();
     }

 }

}
