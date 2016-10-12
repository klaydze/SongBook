package songbook.klaydze.com.songbook.activity;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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
import android.view.Window;
import android.support.v7.widget.SearchView;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import songbook.klaydze.com.songbook.decoration.recyclerview.DividerItemDecoration;
import songbook.klaydze.com.songbook.realm_model.SongModel;
import songbook.klaydze.com.songbook.R;
import songbook.klaydze.com.songbook.adapter.SongRecyclerViewAdapter;
import songbook.klaydze.com.songbook.model.SongListItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;

    // SongModel list container
    private RecyclerView recyclerSongList;
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

        Realm.setDefaultConfiguration(config);

        if (realm == null)
            realm = Realm.getDefaultInstance();

        setUpToolBar();

        setUpNavigationDrawer();

        setUpRealmRecycler(0);

        handleIntent(getIntent());

        if (realm.isEmpty()) {
            new InitJSONFileToRealm().execute();
        }
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

        // This code will make the icon show its original design rather than a tint design
        navigationView.setItemIconTintList(null);
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
        // SongListItem songDetails;

        linearLayoutManager = new LinearLayoutManager(MainActivity.this);

        recyclerSongList = (RecyclerView) findViewById(R.id.recylerSongList);
        recyclerSongList.setLayoutManager(linearLayoutManager);
        recyclerSongList.setHasFixedSize(true);

        songListItems = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
            JSONArray jsonArray = jsonObject.getJSONArray("Songs");

            for (int i = 0; i <= jsonArray.length(); i++) {
                final JSONObject obj = jsonArray.getJSONObject(i);

                /*songDetails = new SongListItem(obj.getString("Number"), obj.getString("Title"), obj.getString("Artist"), false);
                songListItems.add(songDetails);*/

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        SongModel song = realm.createObject(SongModel.class);
                        try {
                            song.setSongNumber(obj.getString("Number"));
                            song.setSongTitle(obj.getString("Title"));
                            song.setSongArtist( obj.getString("Artist"));
                            song.setSongCountry( obj.getString("Country"));
                            song.setFavorite(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
            inputStream = getAssets().open("Songs.json");
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
        recyclerSongList = (RecyclerView) findViewById(R.id.recylerSongList);
        recyclerSongList.setLayoutManager(linearLayoutManager);
        recyclerSongList.setHasFixedSize(true);

        switch (i) {
            case 0: // PH
                recyclerSongList.setAdapter(new SongRecyclerViewAdapter(getApplicationContext(),
                        realm.where(SongModel.class)
                                .equalTo("songCountry", "PH")
                                .findAllSorted("songTitle")));
                break;
            case 1: // JP
                recyclerSongList.setAdapter(new SongRecyclerViewAdapter(getApplicationContext(),
                        realm.where(SongModel.class)
                                .equalTo("songCountry", "JP")
                                .findAllSorted("songTitle")));
                break;
            case 2: // KR
                recyclerSongList.setAdapter(new SongRecyclerViewAdapter(getApplicationContext(),
                        realm.where(SongModel.class)
                                .equalTo("songCountry", "KR")
                                .findAllSorted("songTitle")));
                break;
            case 3: // CN
                recyclerSongList.setAdapter(new SongRecyclerViewAdapter(getApplicationContext(),
                        realm.where(SongModel.class)
                                .equalTo("songCountry", "CN")
                                .findAllSorted("songTitle")));
                break;
            case 4: // Favorites
                recyclerSongList.setAdapter(new SongRecyclerViewAdapter(getApplicationContext(),
                        realm.where(SongModel.class)
                                .equalTo("isFavorite", true)
                                .findAllSorted("songTitle")));
                break;
            case 5: // International
                recyclerSongList.setAdapter(new SongRecyclerViewAdapter(getApplicationContext(),
                        realm.where(SongModel.class)
                                .equalTo("songCountry", "INT")
                                .findAllSorted("songTitle")));
                break;
        }

        recyclerSongList.addItemDecoration(new DividerItemDecoration(MainActivity.this, null));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =  (SearchView) menu.findItem(R.id.action_search).getActionView();

        if (searchView != null) {
            // Assumes current activity is the searchable activity

            searchView.setSubmitButtonEnabled(false);
            searchView.setQueryRefinementEnabled(true);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
            searchView.setFocusable(true);
            searchView.setQueryHint(getString(R.string.action_search_hint));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.about_dialog);

            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

            dialog.show();
            dialog.getWindow().setAttributes(layoutParams);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_ph_songs) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.nav_item_ph);

            setUpRealmRecycler(0);
        } else if (id == R.id.nav_jp_songs) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.nav_item_jp);

            setUpRealmRecycler(1);
        } else if (id == R.id.nav_kr_songs) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.nav_item_kr);

            setUpRealmRecycler(2);
        } else if (id == R.id.nav_cn_songs) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.nav_item_cn);

            setUpRealmRecycler(3);
        } else if (id == R.id.nav_favorite_songs) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.nav_item_favorites);

            setUpRealmRecycler(4);
        } else if (id == R.id.nav_international_songs) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(R.string.nav_item_international);

            setUpRealmRecycler(5);
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

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {

        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String queryWord = intent.getStringExtra(SearchManager.QUERY);

            recyclerSongList.setAdapter(new SongRecyclerViewAdapter(getApplicationContext(),
                    realm.where(SongModel.class)
                            .contains("songTitle", queryWord, Case.INSENSITIVE)
                            .or()
                            .contains("songArtist", queryWord, Case.INSENSITIVE)
                            .findAllSorted("songTitle")));
        }
    }

    private class InitJSONFileToRealm extends AsyncTask<Void, Integer, Void> {

        int songCount;
        boolean isRunning;
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            isRunning = true;

            progressDialog = new ProgressDialog(MainActivity.this);
            // progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setTitle("Please wait");
            progressDialog.setMessage("Initializing song database for the first time...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            /**
             * You need to create a new object of Realm in this thread.
             Source: https://realm.io/docs/java/latest/#threading
             */
            Realm realmJSON = Realm.getDefaultInstance();

            try {
                JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
                JSONArray jsonArray = jsonObject.getJSONArray("Songs");

                songCount = jsonArray.length();

                for (int i = 0; i <= jsonArray.length(); i++) {
                    final JSONObject obj = jsonArray.getJSONObject(i);

                    realmJSON.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            SongModel song = realm.createObject(SongModel.class);
                            try {
                                song.setSongNumber(obj.getString("Number"));
                                song.setSongTitle(obj.getString("Title"));
                                song.setSongArtist(obj.getString("Artist"));
                                song.setSongCountry(obj.getString("Country"));
                                song.setFavorite(false);
                                if (!obj.getString("Chorus").trim().equals("")) {
                                    song.setSongHasChorus(true);
                                } else {
                                    song.setSongHasChorus(false);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    publishProgress(i);

                    progressDialog.setMax(songCount);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            isRunning = false;
            progressDialog.dismiss();
        }
    }
}
