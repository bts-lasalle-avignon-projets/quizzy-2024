package fr.hillionj.quizzy.ihm.vues;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.parametres.Parametres;

public class VueParametres extends AppCompatActivity {

    private Button btn_lancer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_parametres);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btn_lancer = findViewById(R.id.btn_lancer);
        btn_lancer.setOnClickListener(v -> {
            startActivity(new Intent(this, VueSession.class));
        });
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            Preference configurer_participants = findPreference("configurer_participants");

            configurer_participants.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(@NonNull Preference preference) {
                    getActivity().startActivity(new Intent(getActivity(), VueParticipants.class));
                    return true;
                }
            });
            ListPreference liste_themes = findPreference("liste_themes");
            List<String> themes = new ArrayList<>();

            themes.addAll(Parametres.getParametres().getThemes());
            List<String> themesValeurs = new ArrayList<>();
            for (int i = 0; i < themes.size(); i++) {
                themesValeurs.add(String.valueOf(i));
            }
            liste_themes.setEntries(themes.toArray(new String[0]));
            liste_themes.setEntryValues(themesValeurs.toArray(new String[0]));
        }
    }
}