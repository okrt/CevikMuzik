package com.cevik.cevikmuzik;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
/*
 * CevikMuzik: Simple Music Player for Android
 * 
 * (c) 2014-2015 Canberk Güzeler, Ferhat Yeşiltarla, Mert Levent, Oğuz Kırat
 * 
 * 
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {
    public TabPagerAdapter(FragmentManager fm) {
    super(fm);
    // TODO Auto-generated constructor stub
  }
  @Override
  public Fragment getItem(int i) {
    switch (i) {
        case 0:
            
            return new Sanatcilar();
        case 1:
           
            return new Albumler();
        case 2:
            return new Sarkilar();
        case 3:
        	return new CalmaListeleri();
        }
    return null;
  }
  @Override
  public int getCount() {
    // TODO Auto-generated method stub
    return 4; //No of Tabs
  }
    }