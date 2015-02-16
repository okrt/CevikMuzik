package com.cevik.cevikmuzik;

/*
 * CevikMuzik: Simple Music Player for Android
 * 
 * (c) 2014-2015 Canberk Güzeler, Ferhat Yeşiltarla, Mert Levent Oğuz Kırat
 * 
 * 
 */

public class Song {
	
	private long id;
	private String title;
	private String artist;
	
	public Song(long songID, String songTitle, String songArtist){
		id=songID;
		title=songTitle;
		artist=songArtist;
	}
	
	public long getID(){return id;}
	public String getTitle(){return title;}
	public String getArtist(){return artist;}

}
