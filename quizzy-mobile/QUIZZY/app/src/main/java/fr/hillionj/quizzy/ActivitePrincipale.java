/**
 * @file ActivitePrincipale.java
 * @brief Déclaration de l'activité principale
 * @author Jules HILLION
 */

package fr.hillionj.quizzy;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import fr.hillionj.quizzy.bdd.BaseDeDonnees;
import fr.hillionj.quizzy.bluetooth.GestionnaireBluetooth;
import fr.hillionj.quizzy.bluetooth.Peripherique;
import fr.hillionj.quizzy.databinding.ActivityMainBinding;
import fr.hillionj.quizzy.protocole.GestionnaireProtocoles;
import fr.hillionj.quizzy.questionnaire.GestionnaireBruitage;

/**
 * @class EcranPrincipal
 * @brief L'activité principale
 */

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ActivitePrincipale extends AppCompatActivity
{
    private static final String TAG = "_ActivitePrincipale"; //!< TAG pour les logs (cf. Logcat)

    private ActivityMainBinding   binding;

    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        initialiserActivite();

        if (!estInitialiser()) {
            initialiserCommunication();
            BaseDeDonnees.initialiser(this);
            GestionnaireBruitage.initialiser(this);
        }
        GestionnaireProtocoles.getGestionnaireProtocoles().setActivite(this);
        GestionnaireBluetooth.getGestionnaireBluetooth().setActivite(this);
    }

    /**
     * @brief Méthode appelée au démarrage après le onCreate() ou un restart
     * après un onStop()
     */
    @Override
    protected void onStart()
    {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    /**
     * @brief Méthode appelée après onStart() ou après onPause()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    /**
     * @brief Méthode appelée après qu'une boîte de dialogue s'est affichée (on
     * reprend sur un onResume()) ou avant onStop() (activité plus visible)
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    /**
     * @brief Méthode appelée lorsque l'activité n'est plus visible
     */
    @Override
    protected void onStop()
    {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    /**
     * @brief Méthode appelée à la destruction de l'application (après onStop()
     * et détruite par le système Android)
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    private void initialiserActivite()
    {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initialiserNavigation();
    }
    private void initialiserNavigation()
    {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration  appBarConfiguration =
          new AppBarConfiguration
            .Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
            .build();
        NavController navController =
          Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void initialiserCommunication()
    {
        Handler handler = GestionnaireProtocoles.getGestionnaireProtocoles().initialiserHandler(this);
        GestionnaireBluetooth.initialiser(this, handler);
    }

    public boolean estInitialiser() {
        return GestionnaireBluetooth.getGestionnaireBluetooth() != null;
    }
}