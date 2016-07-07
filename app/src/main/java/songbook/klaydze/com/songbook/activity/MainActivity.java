package songbook.klaydze.com.songbook.activity;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import songbook.klaydze.com.songbook.R;
import songbook.klaydze.com.songbook.adapter.SongListAdapter;
import songbook.klaydze.com.songbook.decoration.recyclerview.DividerItemDecoration;
import songbook.klaydze.com.songbook.model.SongListItem;

public class MainActivity extends AppCompatActivity
                                        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;

    // Song list container
    private RecyclerView recylerSongList;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<SongListItem> songListItems;
    private RecyclerView.Adapter songListAdapter;

    private Intent intent;

    protected void onCreate(Bundle savedInstanceState) {

        getDefaultTheme();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setUpToolBar();

        setUpNavigationDrawer();

        readSongList();
    }

    public void getDefaultTheme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        int themeId = sharedPreferences.getInt("THEME", 0);
        setAppTheme(themeId);
    }

    public void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void setUpNavigationDrawer() {
                /*drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        drawerFragment.setDrawerListener(this);*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.fragment_navigation_drawer);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void setAppTheme(int themeId) {
        switch (themeId) {
            case 1:
                setTheme(R.style.AppThemeRedLight);
                break;
            case 2:
                setTheme(R.style.AppThemeBlueLight);
                break;
            case 3:
                setTheme(R.style.AppThemeRedDark);
                break;
            default:
                setTheme(R.style.AppThemeRedLight);
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void readSongList() {
        SongListItem songDetails;

        linearLayoutManager = new LinearLayoutManager(MainActivity.this);

        recylerSongList = (RecyclerView) findViewById(R.id.recylerSongList);
        recylerSongList.setLayoutManager(linearLayoutManager);
        recylerSongList.setHasFixedSize(true);

        songListItems = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
            JSONArray jsonArray = jsonObject.getJSONArray("rows");

            for (int i = 0; i <= jsonArray.length(); i++) {
                JSONArray obj = jsonArray.getJSONArray(i);

                songDetails = new SongListItem(obj.get(3).toString(), obj.get(1).toString(), obj.get(2).toString(), false);
                songListItems.add(songDetails);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        songListAdapter = new SongListAdapter(songListItems);
        recylerSongList.setAdapter(songListAdapter);
        recylerSongList.addItemDecoration(new DividerItemDecoration(MainActivity.this, null));
    }

    private String loadJSONFromAsset() {
        InputStream inputStream;
        String jsonData = null;

        try {
            inputStream = getAssets().open("songlist.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();

            jsonData = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonData;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            // finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
