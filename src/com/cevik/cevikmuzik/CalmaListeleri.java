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
public class CalmaListeleri extends Fragment {

	private ArrayList<String> results = new ArrayList<String>();
    private String tableName = CalmaListesiDB.tableName;
    private SQLiteDatabase newDB;
  @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) {
	  results.add("+ Yeni Ekle");
	  openAndQueryDatabase();
          View calmalisteleri = inflater.inflate(R.layout.calmalisteleri_frag, container, false);
          
          ListView listemiz = (ListView)calmalisteleri.findViewById(R.id.listView1);	
	        ArrayAdapter<String> veriAdaptoru=new ArrayAdapter<String>
	        (getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, results);
	        listemiz.setAdapter(veriAdaptoru);
	       
	        listemiz.setOnItemClickListener(new OnItemClickListener() 
	        {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) {	                
	               	String item = ((TextView)view).getText().toString();
	               	if (item=="+ Yeni Ekle"){ Intent intent=new Intent(getActivity(),CalmaListesiEkle.class);startActivity(intent);}
	               	// BURADA ÇALMA LİSTESİ EKLEME EKRANINA GÖNDERİRİZ
	               	else{
	               		//BURADA ÇALMA LİSTESİNDEKİ ŞARKILARIN LİSTESİNE GÖNDERİRİZ
	            	 Intent intent=new Intent(getActivity(),CalmaListesindekiSarkilar.class);
	                 intent.putExtra("cladi", item);
		                startActivity(intent);
	               	}
	            	
	            
	                
	            }
	        });
	        listemiz.setLongClickable(true);
	        listemiz.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
	        	 public boolean onItemLongClick(AdapterView<?> arg0, View view,
	                     int pos, long id) {
	        		 String item = ((TextView)view).getText().toString();
	        		 CalmaListesiDB dbHelper = new CalmaListesiDB(getActivity().getApplicationContext());
			          newDB = dbHelper.getWritableDatabase();
			          dbHelper.deleteCalmaListesi(item);
			          Toast.makeText(getActivity(),"Çalma Listesi Silindi!",Toast.LENGTH_SHORT).show(); 	
			          Intent intent = new Intent(getActivity(), MainActivity.class);
			          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
			          startActivity(intent);
	                 	

	                 return true;
	             }
	        	
	        });
	        Toast.makeText(getActivity(),"Herhangi bir çalma listesini silmek için üzerine basılı tutun.",Toast.LENGTH_SHORT).show(); 
          return calmalisteleri;
}
  private void openAndQueryDatabase() {
      try {
          CalmaListesiDB dbHelper = new CalmaListesiDB(getActivity().getApplicationContext());
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