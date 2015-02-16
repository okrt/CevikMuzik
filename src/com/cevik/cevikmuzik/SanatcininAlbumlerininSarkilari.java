package com.cevik.cevikmuzik;

import java.util.ArrayList;

import java.util.Arrays;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
public class SanatcininAlbumlerininSarkilari extends Activity {

	public String[] dizi=new String[5];
	private ArrayAdapter arrayAdapter;
	private ListView liste;
	public String[] sasarkiliste;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sanatcininalbumlerininsarkilari);
		Bundle extras = getIntent().getExtras();
	    String albumadi;
		String sanatciadi;
		if (extras != null) {
		    albumadi = extras.getString("albumadi");
		    sanatciadi = extras.getString("sanatciadi");
		    ((TextView)findViewById(R.id.sanatcininadi)).setText(albumadi);
		    
		}
		
		//Albüm Listeme
		for (int i = 0; i < dizi.length; i++)
		{
			dizi[i]=" Şarkı "+i;
		}
		albumadi = extras.getString("albumadi");
	    sanatciadi = extras.getString("sanatciadi");
		liste=(ListView)findViewById(R.id.listView1);
		sasarkiliste=getSarkiList(albumadi).toArray(new String[getSarkiList(albumadi).size()]);
		Arrays.sort(sasarkiliste);
		arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, sasarkiliste);
		liste.setAdapter(arrayAdapter);
		liste.setOnItemClickListener(new OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {	      
            	
            	String sarkiadi = ((TextView)view).getText().toString();
            	Bundle extras = getIntent().getExtras();
            	String albumadi = extras.getString("albumadi");
            	String sanatciadi = extras.getString("sanatciadi");
            	//Toast.makeText(SanatcininAlbumlerininSarkilari.this, item,Toast.LENGTH_LONG).show();
                Intent intent=new Intent(SanatcininAlbumlerininSarkilari.this,SimdiCaliniyor.class);
                intent.putExtra("sarkiadi", sarkiadi);
                intent.putExtra("albumadi", albumadi);
                intent.putExtra("sanatciadi", sanatciadi);
                intent.putExtra("sarkiadilistesi", sasarkiliste);
               // TODO:  intent.putExtra("sarkiidlistesi", getSarkiID(albumadi));
                startActivity(intent);
            	
            	
            
                
            }
        });
		
	    /*Button btn=(Button)findViewById(R.id.button1);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SanatcininAlbumleri.this,SimdiCaliniyor.class);
              //  intent.putExtra("sanatciadi", item);
                startActivity(intent);
				
			}
			
		
		
		});*/
	}

	public ArrayList<String> getSarkiList(String album){
		//şarkıları sorgula
		ArrayList<String> liste =  new ArrayList<String>();
		ContentResolver musicResolver = getContentResolver();
		
		Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
		//iterate over results if valid
		if(musicCursor!=null && musicCursor.moveToFirst()){
			//get columns
			int titleColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media.TITLE);
			int idColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media._ID);
			int artistColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media.ARTIST);
			int albumColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media.ALBUM);

			//add songs to list
			do {
				long thisId = musicCursor.getLong(idColumn);
				String thisTitle = musicCursor.getString(titleColumn);
				String thisArtist = musicCursor.getString(artistColumn);
				String thisAlbum = musicCursor.getString(albumColumn);
				
					if(thisAlbum.equals(album)){
						
						liste.add(thisTitle);
						}
				
				//String thisArtist = musicCursor.getString(artistColumn);
				//String thisAlbum = musicCursor.getString(albumColumn);
	
			} 
			while (musicCursor.moveToNext());
		}
		return(liste);
	}
	public ArrayList<Long> getSarkiID(String album){
		//şarkıları sorgula
		ArrayList<Long> liste =  new ArrayList<Long>();
		ContentResolver musicResolver = getContentResolver();
		
		Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
		//iterate over results if valid
		if(musicCursor!=null && musicCursor.moveToFirst()){
			//get columns
			int titleColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media.TITLE);
			int idColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media._ID);
			int artistColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media.ARTIST);
			int albumColumn = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media.ALBUM);

			//add songs to list
			do {
				long thisId = musicCursor.getLong(idColumn);
				String thisTitle = musicCursor.getString(titleColumn);
				String thisArtist = musicCursor.getString(artistColumn);
				String thisAlbum = musicCursor.getString(albumColumn);
				
					if(thisAlbum.equals(album)){
						
						liste.add(thisId);
						}
				
				//String thisArtist = musicCursor.getString(artistColumn);
				//String thisAlbum = musicCursor.getString(albumColumn);
	
			} 
			while (musicCursor.moveToNext());
		}
		return(liste);
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
