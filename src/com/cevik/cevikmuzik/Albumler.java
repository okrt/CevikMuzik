package com.cevik.cevikmuzik;

import android.net.Uri;

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
import android.content.ContentResolver;
import android.content.Intent;
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
public class Albumler extends Fragment {
	
   @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) {
          View albumler = inflater.inflate(R.layout.albumler_frag, container, false);

          ListView listemiz = (ListView)albumler.findViewById(R.id.listView1);	
          String[] albumlistesi=getAlbumList().toArray(new String[getAlbumList().size()]);
          Arrays.sort(albumlistesi);
	        ArrayAdapter<String> veriAdaptoru=new ArrayAdapter<String>
	        (getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, albumlistesi);
	        listemiz.setAdapter(veriAdaptoru);
	       
	        listemiz.setOnItemClickListener(new OnItemClickListener() 
	        {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) {	                
	            	String item = ((TextView)view).getText().toString();	                
	            	Toast.makeText(getActivity(), item,Toast.LENGTH_LONG).show();
	            	 Intent intent=new Intent(getActivity(),SanatcininAlbumlerininSarkilari.class);
	            	 intent.putExtra("sanatciadi", "Örnek Sanatçı");
	                 intent.putExtra("albumadi", item);
	                 
		                startActivity(intent);
	            	
	            	
	            
	                
	            }
	        });
        //  ((TextView)sanatcilar.findViewById(R.id.textView)).setText("Android");
          return albumler;
}
   
   public ArrayList<String> getAlbumList(){
		//şarkıları sorgula
		ArrayList<String> liste =  new ArrayList<String>();
		ContentResolver musicResolver = getActivity().getContentResolver();
		
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
				if(!liste.contains(thisAlbum)){
				liste.add(thisAlbum);
				}
				//String thisArtist = musicCursor.getString(artistColumn);
				//String thisAlbum = musicCursor.getString(albumColumn);
	
			} 
			while (musicCursor.moveToNext());
		}
		return(liste);
	}

}