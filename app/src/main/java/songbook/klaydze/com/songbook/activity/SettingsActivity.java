package songbook.klaydze.com.songbook.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import songbook.klaydze.com.songbook.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;

    // Settings items
    SwitchCompat switchCompatAppAutoUpdate;
    CheckedTextView checkBox, radioButton;
    FrameLayout frameLayoutSwitch, frameLayoutCheckBox, frameLayoutRadioButton;
    RelativeLayout relativeLayoutChooseTheme;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Boolean homeButton = false,
                themeChanged;

    int themeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDefaultTheme();

        setContentView(R.layout.activity_settings);

        setUpToolBar();

        settingsButtons();

        themeChanged();
    }

    @Override
    public void onBackPressed() {
        intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frameLayoutSwitch:
                if (switchCompatAppAutoUpdate.isChecked())
                    switchCompatAppAutoUpdate.setChecked(false);
                else
                    switchCompatAppAutoUpdate.setChecked(true);
                break;
            case R.id.frameLayoutCheckBox:
                if (checkBox.isChecked())
                    checkBox.setChecked(false);
                else
                    checkBox.setChecked(true);
                break;
            case R.id.frameLayoutRadioButton:
                if (radioButton.isChecked())
                    radioButton.setChecked(false);
                else
                    radioButton.setChecked(true);
                break;
            case R.id.relativeLayoutChooseTheme:
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentThemeChooser dialog = new FragmentThemeChooser();
                dialog.show(fragmentManager, "fragment_color_chooser");
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (!homeButton) {
                NavUtils.navigateUpFromSameTask(SettingsActivity.this);
            }
            if (homeButton) {
                if (!themeChanged) {
                    editor = sharedPreferences.edit();
                    editor.putBoolean("DOWNLOAD", false);
                    editor.apply();
                }
                intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        // Get support to the toolbar and change its title
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void settingsButtons() {
        relativeLayoutChooseTheme = (RelativeLayout) findViewById(R.id.relativeLayoutChooseTheme);
        frameLayoutSwitch = (FrameLayout) findViewById(R.id.frameLayoutSwitch);
        frameLayoutCheckBox = (FrameLayout) findViewById(R.id.frameLayoutCheckBox);
        frameLayoutRadioButton = (FrameLayout) findViewById(R.id.frameLayoutRadioButton);
        switchCompatAppAutoUpdate = (SwitchCompat) findViewById(R.id.switchWidgetAppAutoUpdate);
        checkBox = (CheckedTextView) findViewById(R.id.checkBox);
        radioButton = (CheckedTextView) findViewById(R.id.radioButton);

        frameLayoutSwitch.setOnClickListener(this);
        frameLayoutCheckBox.setOnClickListener(this);
        frameLayoutRadioButton.setOnClickListener(this);
        relativeLayoutChooseTheme.setOnClickListener(this);
    }

    public void getDefaultTheme() {
        sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        this.themeId = sharedPreferences.getInt("THEME", 0);
        setAppTheme(this.themeId);
    }

    public void setAppTheme(int themeId) {
        switch (themeId) {
            case 1:
                setTheme(R.style.AppThemeRedLight);
                break;
            case 2:
                setTheme(R.style.AppThemeBlueLight);
                break;
            default:
                setTheme(R.style.AppThemeRedLight);
                break;
        }
    }

    private void themeChanged() {
        themeChanged = sharedPreferences.getBoolean("THEMECHANGED", false);
        homeButton = true;
    }

    public void setThemeFromThemeChooser(int themeId) {
        switch (themeId) {
            case 1:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 1).apply();
                break;
            case 2:
                editor = sharedPreferences.edit();
                editor.putInt("THEME", 2).apply();
                break;
        }
    }
}
