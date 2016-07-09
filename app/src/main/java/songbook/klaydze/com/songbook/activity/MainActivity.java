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

        if (realm == null)
            realm = Realm.getInstance(config);

        setUpToolBar();

        setUpNavigationDrawer();

        setUpRealmRecycler(0);

        //loadSongListFromJSON();
    }

    public void getDefaultTheme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        int themeId = sharedPreferences.getInt("THEME", 0);
        setAppTheme(themeId);
    }

    public void setUpToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
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

    private void loadSongListFromJSON() {
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

    private void setUpRealmRecycler(int i) {
        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        recylerSongList = (RecyclerView) findViewById(R.id.recylerSongList);
        recylerSongList.setLayoutManager(linearLayoutManager);
        recylerSongList.setHasFixedSize(true);

        switch (i) {
            case 0: // All Song
                recylerSongList.setAdapter(new SongRecyclerViewAdapter(getApplicationContext(),
                        realm.where(SongModel.class)
                                .findAll()));
                break;
            case 1: // Favorite
                recylerSongList.setAdapter(new SongRecyclerViewAdapter(getApplicationContext(),
                        realm.where(SongModel.class)
                                .equalTo("isFavorite", true)
                                .findAllSorted("songTitle")));
                break;
            case 2: // Local
                recylerSongList.setAdapter(new SongRecyclerViewAdapter(getApplicationContext(),
                        realm.where(SongModel.class)
                                .equalTo("songCountry", "PHI")
                                .findAllSorted("songTitle")));
                break;
            case 3: // International
                recylerSongList.setAdapter(new SongRecyclerViewAdapter(getApplicationContext(),
                        realm.where(SongModel.class)
                                .equalTo("songCountry", "INT")
                                .findAllSorted("songTitle")));
                break;
        }

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
//            setUpRealmRecycler();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_all_songs) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.title_all_songs);

            setUpRealmRecycler(0);
        } else if (id == R.id.nav_favorite_songs) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.title_favorites);

            setUpRealmRecycler(1);
        } else if (id == R.id.nav_local_songs) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.title_local);

            setUpRealmRecycler(2);
        } else if (id == R.id.nav_international_songs) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.title_international);

            setUpRealmRecycler(3);
        }

        if (id == R.id.nav_settings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
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
