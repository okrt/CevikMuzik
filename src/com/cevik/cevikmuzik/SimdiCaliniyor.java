package com.cevik.cevikmuzik;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.InputFilter.LengthFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.content.ServiceConnection;
import android.widget.MediaController.MediaPlayerControl;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
/*
 * CevikMuzik: Simple Music Player for Android
 * 
 * (c) 2014-2015 Canberk Güzeler, Ferhat Yeşiltarla, Mert Levent, Oğuz Kırat
 * 
 * 
 */

public class SimdiCaliniyor extends Activity implements OnSeekBarChangeListener {
	MediaPlayer mediaPlayer = new MediaPlayer();
	
	public long currentSongID;
	SeekBar seekbar;
	String calansarkiadi;
	String sonrakisarkiadi;
	String oncekisarkiadi;
	String[] sarkiadilistesi;
	boolean tekrarla=false;
	boolean karisik=false;
	int busarkisirasi;
	public boolean sessiz=true;
	public int sarmamiktari=10000;
	Handler seekHandler = new Handler();
	public Bundle getExtras(){
		 Bundle extras = getIntent().getExtras();
		return extras;
	}
	
	 public void onStartTrackingTouch(SeekBar seekBar) {
	    }

	    public void onStopTrackingTouch(SeekBar seekBar) {
	    }
	public void sarkiyiCal(long sarkiid){
		Log.i("CevikMuzik", "==========YENİ ŞARKI ÇALINIYOR===========");
		try {
			mediaPlayer.reset();
		}
		catch(Exception e)
		{
			
			
		}
		Uri contentUri = ContentUris.withAppendedId(
		        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, sarkiid);

	
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			mediaPlayer.setDataSource(getApplicationContext(), contentUri);
			
			
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
		mediaPlayer.setWakeMode(getBaseContext(), PowerManager.PARTIAL_WAKE_LOCK);
			try {
				mediaPlayer.prepare();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		seekbar.setMax(mediaPlayer.getDuration());
		 if(tekrarla==true){mediaPlayer.setLooping(true);}
		Log.i("CevikMuzik", "Bu sarkinin sirasi: "+busarkisirasi);
		if(busarkisirasi==0)
		{
			oncekisarkiadi=sarkiadilistesi[sarkiadilistesi.length-1];
		}
		else{
			oncekisarkiadi=sarkiadilistesi[busarkisirasi-1];
		}
		Log.i("CevikMuzik", "Onceki Sarki Adi:" + oncekisarkiadi);
		if(busarkisirasi+1==sarkiadilistesi.length)
		{
		//busarkisirasi=sarkiadilistesi.length-1;
		sonrakisarkiadi=sarkiadilistesi[0];
	
		}
		
		else{
			
		sonrakisarkiadi=sarkiadilistesi[busarkisirasi+1];
		
		}
		Log.i("CevikMuzik", "Sonraki Sarki Adi:" + sonrakisarkiadi);
		mediaPlayer.start();
		Log.i("CevikMuzik", "♫ Şimdi Çalınıyor...");
		seekUpdation();
		
		
	}
	
	Runnable run = new Runnable() {

		@Override
		public void run() {
			seekUpdation();
		}
	};
public void sonrakiSarkiyiSalla(){
	Log.i("CevikMuzik", "Karışık mod aktif. Bir sonraki şarkı sallanıyor...");
	int idx = new Random().nextInt(sarkiadilistesi.length);
	String random = (sarkiadilistesi[idx]);
	Log.i("CevikMuzik", "Sallanan sonraki şarkı: "+random);
	sonrakisarkiadi=random;
}
	public void seekUpdation() {

		seekbar.setProgress(mediaPlayer.getCurrentPosition());
		if(mediaPlayer.getCurrentPosition()+1000>=mediaPlayer.getDuration())
		{
			if(!tekrarla){
			Log.i("CevikMuzik", "Sonraki şarkıya geçiliyor...");
			if(karisik==true){sonrakiSarkiyiSalla();}
			sarkiyiCal(getSarkiID(sonrakisarkiadi));
			}
			else{Log.i("CevikMuzik", "Tekrar modu açık şarkı değiştirilmiyor...");
			}
					}		
		
		seekHandler.postDelayed(run, 1000);
		
	}
public void sarkiBilgileriniGoster(String sanatci,String album,String sarki){
	calansarkiadi=sarki;
	Log.i("CevikMuzik", "Ekrandaki şarkı bilgileri güncelleniyor...");
	((TextView)findViewById(R.id.tvSarki)).setText(sarki);
    ((TextView)findViewById(R.id.tvAlbum)).setText(album);
    ((TextView)findViewById(R.id.tvSanatci)).setText(sanatci);
    busarkisirasi=Arrays.asList(sarkiadilistesi).indexOf(sarki);
}
	public long getSarkiID(String sarki){
		Log.i("CevikMuzik", "==> Yeni şarkı çalmaya hazırlanıyor...");
		

		ContentResolver musicResolver = getContentResolver();
		long thisId=0;
		long thisalbumid=0;
		Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
		//iterate over results if valid
		Log.i("CevikMuzik", "Şarkı bilgileri kütüphaneden alınıyor...");
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
			int albumid = musicCursor.getColumnIndex
					(android.provider.MediaStore.Audio.Media.ALBUM_ID);;
			//add songs to list
			do {
				
				String thisTitle = musicCursor.getString(titleColumn);
				String thisArtist = musicCursor.getString(artistColumn);
				String thisAlbum = musicCursor.getString(albumColumn);
				
				if(thisTitle.equals(sarki)){
					thisId = musicCursor.getLong(idColumn);
					thisalbumid=musicCursor.getInt(albumid);
					sarkiBilgileriniGoster(thisArtist,thisAlbum,thisTitle);
					//Artwork
					Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
			          Uri uri = ContentUris.withAppendedId(sArtworkUri, thisalbumid);
			          ContentResolver res = getContentResolver();
			          InputStream in;
			          try {
			              in = res.openInputStream(uri);
			              Bitmap albumart=BitmapFactory.decodeStream(in);
			              ImageView albumimage=(ImageView)findViewById(R.id.imageView1);
			              albumimage.setImageBitmap(albumart);
			              } catch (FileNotFoundException e) {
			                 Log.w("CevikMuzik", "Albüm kapağı bulunamadı, varsayılan bir değer atanıyor.");
			                 ImageView albumimage=(ImageView)findViewById(R.id.imageView1);
			                 albumimage.setImageResource(R.drawable.simdicaliniyor_logo);
			              }
					//artwork
					break;
				}
				//String thisArtist = musicCursor.getString(artistColumn);
				//String thisAlbum = musicCursor.getString(albumColumn);
	
			} 
			while (musicCursor.moveToNext());
		}
		return thisId;
	}
	@Override
	protected void onPause(){
		super.onPause();
		try{
			mediaPlayer.stop();
			}
			catch(Exception ex)
			{ ex.printStackTrace();}
			
	}
	@Override
	protected void onStart() {
Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			String sarkiadi = extras.getString("sarkiadi");
			calansarkiadi=sarkiadi;
			String albumadi = extras.getString("albumadi");
			String sanatciadi = extras.getString("sanatciadi");
			sarkiadilistesi=extras.getStringArray("sarkiadilistesi");
			busarkisirasi=Arrays.asList(sarkiadilistesi).indexOf(sarkiadi);
			
			//sonrakisarkiadi=sarkiadilistesi[busarkisirasi+1];
			try{
			sarkiyiCal(getSarkiID(sarkiadi));
			}
			catch(Exception ex){
				
			}
			
		}
		super.onStart();
		
		
		
	}
	public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        if(fromUser){
        mediaPlayer.seekTo(progress);
        seekbar.setProgress(progress);}
 }
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simdicaliniyor);
		seekbar = (SeekBar) findViewById(R.id.seekBar1);
		seekbar.setOnSeekBarChangeListener(this);
		
		
		Bundle extras = getIntent().getExtras();
		
		if (extras != null) {
			String sarkiadi = extras.getString("sarkiadi");
			String albumadi = extras.getString("albumadi");
			String sanatciadi = extras.getString("sanatciadi");
			((TextView)findViewById(R.id.tvSarki)).setText(sarkiadi);
		    ((TextView)findViewById(R.id.tvAlbum)).setText(albumadi);
		    ((TextView)findViewById(R.id.tvSanatci)).setText(sanatciadi);
		   
		}
		
		final ImageView durdur=(ImageView)findViewById(R.id.durdur);
		
		
		durdur.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					if(mediaPlayer.isPlaying()){						
						durdur.setImageResource(R.drawable.oynat);
						Toast.makeText(SimdiCaliniyor.this,"Şarkı Durduruldu",Toast.LENGTH_SHORT).show(); 			
						mediaPlayer.pause();
						
						
					}
					else{
						durdur.setImageResource(R.drawable.durdur);
						Toast.makeText(SimdiCaliniyor.this,"Şarkı Oynatılıyor",Toast.LENGTH_SHORT).show(); 
						mediaPlayer.start();
					}
				}
				catch(Exception ex)
				{ex.printStackTrace();}
			}
		});
		
		final ImageView ilerisar=(ImageView)findViewById(R.id.ilerisar);
		
		
		ilerisar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					
					if(mediaPlayer.getCurrentPosition()+sarmamiktari<mediaPlayer.getDuration()){
					mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+sarmamiktari);}
					else{
						Toast.makeText(SimdiCaliniyor.this,"Biliyoruz sıkıldınız ama şarkı zaten bitmek üzere :)",Toast.LENGTH_SHORT).show();
					}
				}
				catch(Exception ex)
				{ex.printStackTrace();}
			}
		});
final ImageView gerisar=(ImageView)findViewById(R.id.gerisar);
		
		
		gerisar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					
					if(mediaPlayer.getCurrentPosition()-sarmamiktari<0){
						mediaPlayer.seekTo(0);}
					else{
						
						mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-sarmamiktari);}
					
				}
				catch(Exception ex)
				{ex.printStackTrace();}
			}
		});	
final ImageView imgsonrakisarki=(ImageView)findViewById(R.id.sonrakisarki);
		
		
		imgsonrakisarki.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					
					//mediaPlayer.stop();
					//mediaPlayer.release();
					if(karisik==true){sonrakiSarkiyiSalla();}
					sarkiyiCal(getSarkiID(sonrakisarkiadi));
					
				}
				catch(Exception ex)
				{ex.printStackTrace();}
			}
		});
		
final ImageView imgoncekisarki=(ImageView)findViewById(R.id.oncekisarki);
		
		
		imgoncekisarki.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try{
					
					//mediaPlayer.stop();
					//mediaPlayer.release();
					sarkiyiCal(getSarkiID(oncekisarkiadi));
					
				}
				catch(Exception ex)
				{ex.printStackTrace();}
			}
		});	
		
		ImageView ucnokta=(ImageView)findViewById(R.id.ucnokta);
		ucnokta.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 PopupMenu popup = new PopupMenu(SimdiCaliniyor.this,v);  
		            //Inflating the Popup using xml file  
		            popup.getMenuInflater().inflate(R.menu.simdicaliniyor_menu, popup.getMenu());  
		           
		            //registering popup with OnMenuItemClickListener  
		            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
		             public boolean onMenuItemClick(MenuItem item) {  
		              //Toast.makeText(SimdiCaliniyor.this,"Buna tıkladınız : " + item.getTitle(),Toast.LENGTH_SHORT).show();
		              if(item.getTitle().equals("Sessiz")){
		            	  
		            	  if(sessiz==true){
		            		  mediaPlayer.setVolume(0, 0);
		            		  sessiz=false;
				              Toast.makeText(SimdiCaliniyor.this,"Sessize alındı",Toast.LENGTH_SHORT).show();

		            	  }
		            	  else{mediaPlayer.setVolume(1,1);
	            		  sessiz=true;
	            		  Toast.makeText(SimdiCaliniyor.this,"Sesliye alındı",Toast.LENGTH_SHORT).show();}
		            	  
		              }
		              else if(item.getTitle().equals("Tekrarla"))
		              {
		            	  if(tekrarla==true){
		            		  mediaPlayer.setLooping(false);
		            		  Toast.makeText(SimdiCaliniyor.this,"Tekrar Kapalı",Toast.LENGTH_SHORT).show();
		            		  tekrarla=false;
		            	
		            }
		            	  else{mediaPlayer.setLooping(true);
		            	  Toast.makeText(SimdiCaliniyor.this,"Tekrar Açık",Toast.LENGTH_SHORT).show();
		            	  tekrarla=true;
		            	  }		            	  
		              }
		              
		              else if(item.getTitle().equals("Karışık çal"))
		              {
		            	  if(karisik==true){
		            		  
		            		  Toast.makeText(SimdiCaliniyor.this,"Karışık çalma kapalı",Toast.LENGTH_SHORT).show();
		            		  karisik=false;
		            	
		            }
		            	  else{
		            	  Toast.makeText(SimdiCaliniyor.this,"Karışık çalma aktif.",Toast.LENGTH_SHORT).show();
		            	  karisik=true;
		            	  }		            	  
		              }
		              else if(item.getTitle().equals("Çalma Listesine Ekle"))
		              {
		            	  Intent intent = new Intent(SimdiCaliniyor.this, CalmaListesiSec.class);
		            	  intent.putExtra("sarkiadi", calansarkiadi);
				         // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); 
				          startActivity(intent);     	  
		              }
		              else if(item.getTitle().equals("Hakkında"))
		              {
		            	  new AlertDialog.Builder(SimdiCaliniyor.this)
		            	    .setTitle("ÇevikMüzik Hakkında")
		            	    .setMessage("ÇevikMüzik v2.0\n©2014-2015\n\nÇevikMüzik Geliştiricileri:\nCanberk Güzeler\nFerhat Yeşiltarla\nMert Levent\nOğuz Kırat\n\nWe ♥ Java")
		            	   
		            	     
		            	    
		            	     .show();
		              }
		              return true;  
		             }  
		            });  
		  
		            popup.show();//showing popup menu  
				
			}
		});
	
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
