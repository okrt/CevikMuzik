package com.cevik.cevikmuzik;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
/*
 * CevikMuzik: Simple Music Player for Android
 * 
 * (c) 2014-2015 Canberk Güzeler, Ferhat Yeşiltarla, Mert Levent, Oğuz Kırat
 * 
 * 
 */
public class CalmaListesiEkle extends Activity {

	private SQLiteDatabase newDB;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calmalistesiekle);
final Button ekle=(Button)findViewById(R.id.btnCLekle);
final EditText cladi=(EditText)findViewById(R.id.editTextCLadi);	
		
		ekle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CalmaListesiDB dbHelper = new CalmaListesiDB(getApplicationContext());
		          newDB = dbHelper.getWritableDatabase();
		          dbHelper.createCalmaListesi(cladi.getText().toString());
		          Toast.makeText(CalmaListesiEkle.this,"Çalma Listesi Oluşturuldu",Toast.LENGTH_SHORT).show(); 	
		          Intent intent = new Intent(CalmaListesiEkle.this, MainActivity.class);
		          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
		          startActivity(intent);
				
			}});
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
