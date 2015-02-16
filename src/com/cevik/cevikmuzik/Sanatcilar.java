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
public class Sanatcilar extends Fragment {
	
  @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
              Bundle savedInstanceState) {
	 
          View sanatcilar = inflater.inflate(R.layout.sanatcilar_frag, container, false);
          ListView listemiz = (ListView)sanatcilar.findViewById(R.id.listView1);
          String[] sanatcilistesi=getSanatcilarList().toArray(new String[getSanatcilarList().size()]);
      
          Arrays.sort(sanatcilistesi);
	        ArrayAdapter<String> veriAdaptoru=new ArrayAdapter<String>
	        (getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1,  sanatcilistesi);
	        listemiz.setAdapter(veriAdaptoru);
	       
	        listemiz.setOnItemClickListener(new OnItemClickListener() 
	        {
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view, int position,
	                    long id) {	                
	            	String item = ((TextView)view).getText().toString();	                
	            	//Toast.makeText(getActivity(), item,Toast.LENGTH_LONG).show();
	                Intent intent=new Intent(getActivity(),SanatcininAlbumleri.class);
	                intent.putExtra("sanatciadi", item);
	                startActivity(intent);
	            	
	            	
	            
	                
	            }
	        });
        //  ((TextView)sanatcilar.findViewById(R.id.textView)).setText("Android");
          return sanatcilar;
}
  public ArrayList<String> getSanatcilarList(){
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
				if(!liste.contains(thisArtist)){
				liste.add(thisArtist);
				}
				//String thisArtist = musicCursor.getString(artistColumn);
				//String thisAlbum = musicCursor.getString(albumColumn);
	
			} 
			while (musicCursor.moveToNext());
		}
		return(liste);
	}  

}