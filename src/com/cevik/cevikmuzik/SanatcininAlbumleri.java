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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/*
 * CevikMuzik: Simple Music Player for Android
 * 
 * (c) 2014-2015 Canberk Güzeler, Ferhat Yeşiltarla, Mert Levent Oğuz Kırat
 * 
 * 
 */
public class SanatcininAlbumleri extends Activity {

	public String[] dizi=new String[10];
	private ArrayAdapter arrayAdapter;
	private ListView liste;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sanatcininalbumleri);
		Bundle extras = getIntent().getExtras();
		String sanatcininadi;
		if (extras != null) {
			sanatcininadi = extras.getString("sanatciadi");
		    
		}
		else{
			sanatcininadi= "Örnek Sanatçı";
		}
		((TextView)findViewById(R.id.sanatcininadi)).setText(sanatcininadi);
		//Albüm Listeme
		for (int i = 0; i < dizi.length; i++)
		{
			dizi[i]=sanatcininadi+" Albüm "+i;
		}
		if (extras != null) {
			sanatcininadi = extras.getString("sanatciadi");
		    
		}
		liste=(ListView)findViewById(R.id.listView1);
		String[] salbumliste=getAlbumList(sanatcininadi).toArray(new String[getAlbumList(sanatcininadi).size()]);
		Arrays.sort(salbumliste);
		arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, salbumliste);
		liste.setAdapter(arrayAdapter);
		liste.setOnItemClickListener(new OnItemClickListener() 
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {	    
            	Bundle extras = getIntent().getExtras();
        		String sanatcininadi;
        		if (extras != null) {
        			sanatcininadi = extras.getString("sanatciadi");
        		    
        		}
        		else{
        			sanatcininadi= "Örnek Sanatçı";
        		}
            	String item = ((TextView)view).getText().toString();	                
            	Toast.makeText(SanatcininAlbumleri.this, item,Toast.LENGTH_LONG).show();
                Intent intent=new Intent(SanatcininAlbumleri.this,SanatcininAlbumlerininSarkilari.class);
                intent.putExtra("sanatciadi", sanatcininadi);
                intent.putExtra("albumadi", item);
                startActivity(intent);
            	
            	
            
                
            }
        });
		
		

	}

	public ArrayList<String> getAlbumList(String sanatci){
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
				if(thisArtist.equals(sanatci)){
				if(!liste.contains(thisAlbum)){
				liste.add(thisAlbum);
				}}
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
