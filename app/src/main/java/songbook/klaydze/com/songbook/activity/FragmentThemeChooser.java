package songbook.klaydze.com.songbook.activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import songbook.klaydze.com.songbook.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentThemeChooser extends DialogFragment implements View.OnClickListener {

    private View view;

    private CardView cardView1, cardView2;
    private Button btnSetTheme, btnCancel;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private int currentThemeId;

    private Intent intent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Save current theme to use when user press dismiss inside dialog
        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        currentThemeId = sharedPreferences.getInt("THEME", 0);

        view = inflater.inflate(R.layout.fragment_theme_chooser, container);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialogButtons();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.card_view1:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((SettingsActivity) getActivity()).setThemeFromThemeChooser(1);
                break;
            case R.id.card_view2:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                ((SettingsActivity) getActivity()).setThemeFromThemeChooser(2);
                break;
            case R.id.btnSetTheme:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                break;
            case R.id.btnCancel:
                sharedPreferences.edit().putBoolean("THEMECHANGED", false).apply();
                ((SettingsActivity) getActivity()).setThemeFromThemeChooser(currentThemeId);
                getDialog().dismiss();
                break;
        }
    }

    private void dialogButtons() {
        cardView1 = (CardView) view.findViewById(R.id.card_view1);
        cardView2 = (CardView) view.findViewById(R.id.card_view2);

        btnSetTheme = (Button) view.findViewById(R.id.btnSetTheme);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);

        cardView1.setOnClickListener(this);
        cardView2.setOnClickListener(this);

        btnSetTheme.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

}
