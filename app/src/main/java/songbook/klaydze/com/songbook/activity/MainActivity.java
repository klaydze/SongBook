package songbook.klaydze.com.songbook.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import songbook.klaydze.com.songbook.realm_model.SongModel;
import songbook.klaydze.com.songbook.R;
import songbook.klaydze.com.songbook.adapter.SongListAdapter;
import songbook.klaydze.com.songbook.adapter.SongRecyclerViewAdapter;
import songbook.klaydze.com.songbook.decoration.recyclerview.DividerItemDecoration;
import songbook.klaydze.com.songbook.model.SongListItem;

public class MainActivity extends AppCompatActivity
                                        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;

    // SongModel list container
    private RecyclerView recylerSongList;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<SongListItem> songListItems;
    private RecyclerView.Adapter songListAdapter;

    private Intent intent;

    private Realm realm;

    protected void onCreate(Bundle savedInstanceState) {

        getDefaultTheme();

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        RealmConfiguration config = new RealmConfiguration.Builder(getApplicationContext())
                                    .deleteRealmIfMigrationNeeded()
                                    .build();

        realm = Realm.getInstance(config);

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

                realm.beginTransaction();
                SongModel song = realm.createObject(SongModel.class);
                song.setSongId(Integer.parseInt(obj.get(0).toString()));
                song.setSongNumber(obj.get(3).toString());
                song.setSongTitle(obj.get(1).toString());
                song.setSongArtist(obj.get(2).toString());
                song.setSongCountry(obj.get(4).toString());
                song.setFavorite(Integer.parseInt(obj.get(5).toString()) != 0);
                realm.commitTransaction();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*songListAdapter = new SongListAdapter(songListItems);
        recylerSongList.setAdapter(songListAdapter);
        recylerSongList.addItemDecoration(new DividerItemDecoration(MainActivity.this, null));*/
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

    private void setUpRealmRecycler() {
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recylerSongList = (RecyclerView) findViewById(R.id.recylerSongList);
        recylerSongList.setLayoutManager(linearLayoutManager);
        recylerSongList.setHasFixedSize(true);
        recylerSongList.setAdapter(new SongRecyclerViewAdapter(getApplicationContext(),
                                                                realm.where(SongModel.class).findAll()));
        recylerSongList.addItemDecoration(new DividerItemDecoration(MainActivity.this, null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
        } else if (id == R.id.action_refresh) {
            setUpRealmRecycler();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        realm.close();
    }
}
