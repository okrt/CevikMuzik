package com.cevik.cevikmuzik;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;
public class MainActivity extends FragmentActivity {
  ViewPager Tab;
    TabPagerAdapter TabAdapter;
  ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
        Tab = (ViewPager)findViewById(R.id.pager);
        
       
        
        
        Tab.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                      actionBar = getActionBar();
                      actionBar.setSelectedNavigationItem(position);                    }
                });
        
        Tab.setAdapter(TabAdapter);
        actionBar = getActionBar();
        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){
      @Override
      public void onTabReselected(android.app.ActionBar.Tab tab,
          FragmentTransaction ft) {
        // TODO Auto-generated method stub
      }
      
   
      
      
      
      @Override
       public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
              Tab.setCurrentItem(tab.getPosition());
              
          }
      
      
      @Override
      public void onTabUnselected(android.app.ActionBar.Tab tab,
          FragmentTransaction ft) {
        // TODO Auto-generated method stub
      }};
      //Add New Tab
      
      
      actionBar.addTab(actionBar.newTab().setText("Sanatçılar").setTabListener(tabListener));
    
      actionBar.addTab(actionBar.newTab().setText("Albümler").setTabListener(tabListener));
      actionBar.addTab(actionBar.newTab().setText("Şarkılar").setTabListener(tabListener));
      actionBar.addTab(actionBar.newTab().setText("Çalma Listeleri").setTabListener(tabListener));

		FragmentManager m = this.getFragmentManager();
	    m.executePendingTransactions();
      
    }
}