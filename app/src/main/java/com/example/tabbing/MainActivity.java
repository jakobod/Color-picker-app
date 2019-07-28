package com.example.tabbing;

import com.google.android.material.tabs.TabLayout;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tabbing.ui.main.listFragment.ListFragment;
import com.example.tabbing.ui.main.listFragment.RecyclerViewAdapter;
import com.example.tabbing.ui.main.SectionsPagerAdapter;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
                     implements ListFragment.OnListFragmentInteractionListener {
  WifiTask wifiTask;
  private static Context context;
  private static RecyclerView recyclerView = null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = getApplicationContext();
    wifiTask = new WifiTask();
    wifiTask.execute();
    setContentView(R.layout.activity_main);
    SectionsPagerAdapter sectionsPagerAdapter =
             new SectionsPagerAdapter(this, getSupportFragmentManager());
    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);
  }

  @Override
  public void onListFragmentInteraction(RecyclerViewAdapter.RecyclerViewItem item) {
    Toast.makeText(this, "talkin to " + item.getInetAddress(),
                   Toast.LENGTH_SHORT).show();
    wifiTask.setEndpoint(item.getInetAddress());
  }

  @Override
  protected void onResume(){
    super.onResume();
    wifiTask = new WifiTask();
    wifiTask.execute();
  }

  @Override
  protected void onStop(){
    super.onStop();
    if (wifiTask == null) return;
    wifiTask.stop();
  }

  public static Context getContext() {
    return context;
  }

  public static void setRecyclerView(RecyclerView rv) {
    recyclerView = rv;
  }

  public static void updateRecyclerView() {
    RecyclerView.Adapter a = recyclerView.getAdapter();
    try {
      Objects.requireNonNull(a).notifyDataSetChanged();
    } catch (Exception e) {
      // silence the exception.
    }
  }
}