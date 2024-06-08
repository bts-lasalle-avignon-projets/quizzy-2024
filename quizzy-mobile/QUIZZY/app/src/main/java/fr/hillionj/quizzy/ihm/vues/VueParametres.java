package fr.hillionj.quizzy.ihm.vues;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.EditTextPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.Quizzy;
import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.session.contenu.Theme;

public class VueParametres extends AppCompatActivity {

    private Button btn_lancer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activite_parametres);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment(this))
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        IHM.getIHM().ajouterIHM(this);

        btn_lancer = findViewById(R.id.btn_lancer);
        btn_lancer.setOnClickListener(v -> {
            if (Parametres.getParametres().getSession().estValide()) {
                IHM.getIHM().demarrerActivite(this, this, VueSession.class);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        IHM.getIHM().ajouterIHM(this);
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {

        private final AppCompatActivity activite;

        public SettingsFragment() {
            this.activite = null;
        }

        public SettingsFragment(AppCompatActivity activite) {
            this.activite = activite;
        }

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

            EditTextPreference entree_nombre_de_questions = findPreference("entree_nombre_de_questions");
            Parametres.getParametres().setNombreDeQuestions(Integer.parseInt(entree_nombre_de_questions.getText()));
            entree_nombre_de_questions.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    try {
                        Parametres.getParametres().setNombreDeQuestions(Integer.parseInt(newValue.toString()));
                    } catch (Exception exception) {
                        return false;
                    }
                    return true;
                }
            });

            EditTextPreference entree_temps = findPreference("entree_temps");
            Parametres.getParametres().setTempsReponse(Integer.parseInt(entree_temps.getText()));
            entree_temps.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    try {
                        Parametres.getParametres().setTempsReponse(Integer.parseInt(newValue.toString()));
                    } catch (Exception exception) {
                        return false;
                    }
                    return true;
                }
            });

            SwitchPreference switch_calcul_de_temps = findPreference("switch_calcul_de_temps");
            Parametres.getParametres().setCalulAutomatiqueDuTempsDeReponse(switch_calcul_de_temps.isChecked());
            if (Parametres.getParametres().estCalulAutomatiqueDuTempsDeReponse()) {
                entree_temps.setLayoutResource(R.layout.custom_edit_text_preference_disabled);
            } else {
                entree_temps.setLayoutResource(R.layout.custom_edit_text_preference);
            }
            entree_temps.setEnabled(!Parametres.getParametres().estCalulAutomatiqueDuTempsDeReponse());
            switch_calcul_de_temps.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    Parametres.getParametres().setCalulAutomatiqueDuTempsDeReponse((boolean) newValue);
                    entree_temps.setEnabled(!((boolean) newValue));
                    if ((boolean) newValue) {
                        entree_temps.setLayoutResource(R.layout.custom_edit_text_preference_disabled);
                    } else {
                        entree_temps.setLayoutResource(R.layout.custom_edit_text_preference);
                    }
                    return true;
                }
            });

            ListPreference liste_themes = findPreference("liste_themes");

            List<String> themes = new ArrayList<>();
            for (Theme theme : Parametres.getParametres().getThemes()) {
                themes.add(theme.getNom());
            }
            List<String> themesValeurs = new ArrayList<>();
            for (int i = 0; i < themes.size(); i++) {
                themesValeurs.add(String.valueOf(i));
            }
            liste_themes.setEntries(themes.toArray(new String[0]));
            liste_themes.setEntryValues(themesValeurs.toArray(new String[0]));
            Parametres.getParametres().setTheme(Parametres.getParametres().getThemes().get(Integer.parseInt(liste_themes.getValue())));

            liste_themes.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    Parametres.getParametres().setTheme(Parametres.getParametres().getThemes().get(Integer.parseInt(newValue.toString())));
                    return true;
                }
            });

            SwitchPreference switch_tester_ta_connexion = findPreference("switch_tester_ta_connexion");
            Parametres.getParametres().setTesterLaConnexion(switch_tester_ta_connexion.isChecked());
            switch_tester_ta_connexion.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(@NonNull Preference preference, Object newValue) {
                    Parametres.getParametres().setTesterLaConnexion((boolean) newValue);
                    return true;
                }
            });
        }
    }
}