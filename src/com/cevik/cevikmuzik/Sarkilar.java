package com.cevik.cevikmuzik;

import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ListView;

import java.util.ArrayList;  
import java.util.Arrays;  
import java.util.Collections;
import java.util.Comparator;

import com.cevik.cevikmuzik.MusicController;
import com.cevik.cevikmuzik.MusicService;
import com.cevik.cevikmuzik.Song;
import com.cevik.cevikmuzik.SongAdapter;



import android.widget.ArrayAdapter; 
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
/*
 * CevikMuzik: Simple Music Player for Android
 * 
 * (c) 2014-2015 Canberk Güzeler, Ferhat Yeşiltarla, Mert Levent Oğuz Kırat
 * 
 * 
 */
public class Sarkilar extends Fragment {
	public String[] dizi =  {};
	public ArrayList<String> songList;
	private ListView songView;
	String[] sarkilarlistesi;
	//service
	private MusicService musicSrv;
	private Intent playIntent;
	//binding
	private boolean musicBound=false;

	//controller
	private MusicController controller;

	//activity and playback pause flags
	private boolean paused=false, playbackPaused=false;

  @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) {

	  
	  View sarkilar = inflater.inflate(R.layout.sarkilar_frag, container, false);
          ListView listemiz = (ListView)sarkilar.findViewById(R.id.listView1);	
          sarkilarlistesi=getSongList().toArray(new String[getSongList().size()]);
          Arrays.sort(sarkilarlistesi);
	        ArrayAdapter<String> veriAdaptoru=new ArrayAdapter<String>
	        (getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, sarkilarlistesi);
	        listemiz.setAdapter(veriAdaptoru);
	       
	        listemiz.setOnItemClickListener(new OnItemClickListener() 
	        {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) {	                
	            	String item = ((TextView)view).getText().toString();	                
	            	Toast.makeText(getActivity(), item,Toast.LENGTH_LONG).show();
	            	   Intent intent=new Intent(getActivity(),SimdiCaliniyor.class);
	                   intent.putExtra("sarkiadi", item);
	                   intent.putExtra("albumadi", item +" (Albüm)");
	                   intent.putExtra("sanatciadi", item +"(Sanatçı)");
	                   intent.putExtra("sarkiadilistesi", sarkilarlistesi);
	                   startActivity(intent);
	            	
	            	
	            
	                
	            }
	        });
        //  ((TextView)sanatcilar.findViewById(R.id.textView)).setText("Android");
          return sarkilar;
}


	//Cihazdan şarkıları çekmeye yarayan önemli fonksiyon
	public ArrayList<String> getSongList(){
		//şarkıları sorgula
		ArrayList<String> liste =  new ArrayList<String>();
		ContentResolver musicResolver;
	
		musicResolver =  getActivity().getContentResolver();
	
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
				liste.add(thisTitle);
				//String thisArtist = musicCursor.getString(artistColumn);
				//String thisAlbum = musicCursor.getString(albumColumn);
	
			} 
			while (musicCursor.moveToNext());
		}
		return(liste);
	}






}