package com.example.tabbing;

import android.content.Context;
import android.os.Bundle;

import com.example.tabbing.ui.main.listFragment.ListFragment;
import com.example.tabbing.ui.main.listFragment.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Toast;

import com.example.tabbing.ui.main.SectionsPagerAdapter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements ListFragment.OnListFragmentInteractionListener {
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
    SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
    ViewPager viewPager = findViewById(R.id.view_pager);
    viewPager.setAdapter(sectionsPagerAdapter);
    TabLayout tabs = findViewById(R.id.tabs);
    tabs.setupWithViewPager(viewPager);
    FloatingActionButton fab = findViewById(R.id.fab);

    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Snackbar.make(view, "cleared hosts, also sent broadcast", Snackbar.LENGTH_LONG)
            .setAction("Action", null).show();
        Set<InetAddress> hosts = new HashSet<>();
        try {
          hosts.add(InetAddress.getByName("0.0.0.0"));
        } catch (UnknownHostException e) {
          e.printStackTrace();
        }
        RecyclerViewAdapter.addItems(hosts);
        MainActivity.updateRecyclerView();
      }
    });
  }

  @Override
  public void onListFragmentInteraction(RecyclerViewAdapter.RecyclerViewItem item) {
    Toast.makeText(this, "taking item " + item.id, Toast.LENGTH_SHORT).show();
    WifiTask.endpoint = item.inetAddress;
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
    wifiTask.requestDisconnect();
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
      a.notifyDataSetChanged();
    } catch (Exception e) {}
  }
}