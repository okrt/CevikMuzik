package com.cevik.cevikmuzik;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ListView;

import java.util.ArrayList;  
import java.util.Arrays;  

import android.widget.ArrayAdapter; 
import android.app.Activity;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
public class CalmaListesiSec extends Activity {

	private ArrayList<String> results = new ArrayList<String>();
    private String tableName = CalmaListesiDB.tableName;
    private SQLiteDatabase newDB;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calmalistesisec);
	 // results.add("+ Yeni Ekle");
	  openAndQueryDatabase();
          
          
          ListView listemiz = (ListView)findViewById(R.id.listView1);	
	        ArrayAdapter<String> veriAdaptoru=new ArrayAdapter<String>
	        (CalmaListesiSec.this, android.R.layout.simple_list_item_1, android.R.id.text1, results);
	        listemiz.setAdapter(veriAdaptoru);
	       
	        listemiz.setOnItemClickListener(new OnItemClickListener() 
	        {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) {	                
	               	String item = ((TextView)view).getText().toString();
	               	Bundle extras = getIntent().getExtras();
	        		
	        		if (extras != null) {
	        			String sarkiadi = extras.getString("sarkiadi");
	        			CalmaListesiDB dbHelper = new CalmaListesiDB(CalmaListesiSec.this.getApplicationContext());
				          newDB = dbHelper.getWritableDatabase();
				          dbHelper.createSarki(sarkiadi, item);
				          //Sarki eklendi
				          Toast.makeText(CalmaListesiSec.this,"Çalma Listesine Eklendi",Toast.LENGTH_SHORT).show(); 	
				          Intent intent = new Intent(CalmaListesiSec.this, MainActivity.class);
				          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
				          startActivity(intent);
	        		}
	        		
	            	
	            
	                
	            }
	        });
	        
	       
         
}
  private void openAndQueryDatabase() {
      try {
          CalmaListesiDB dbHelper = new CalmaListesiDB(CalmaListesiSec.this.getApplicationContext());
          newDB = dbHelper.getWritableDatabase();
          Cursor c = newDB.rawQuery("SELECT * FROM calmalisteleri", null);

          if (c != null ) {
              if  (c.moveToFirst()) {
                  do {
                      String cladi = c.getString(c.getColumnIndex("calmalistesiadi"));
                      results.add(cladi);
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