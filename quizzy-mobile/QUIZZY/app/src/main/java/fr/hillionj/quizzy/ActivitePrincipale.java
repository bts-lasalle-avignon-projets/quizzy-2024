/**
 * @file ActivitePrincipale.java
 * @brief Déclaration de l'activité principale
 * @author Jules HILLION
 */

package fr.hillionj.quizzy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import fr.hillionj.quizzy.bluetooth.GestionnaireBluetooth;
import fr.hillionj.quizzy.bluetooth.Peripherique;
import fr.hillionj.quizzy.databinding.ActivityMainBinding;
import fr.hillionj.quizzy.navigation.pupitres.FragmentPupitre;

/**
 * @class EcranPrincipal
 * @brief L'activité principale
 */
public class ActivitePrincipale extends AppCompatActivity
{
    /**
     * Constantes
     */
    private static final String TAG = "_ActivitePrincipale"; //!< TAG pour les logs (cf. Logcat)
    public final int            CODE_CONNEXION        = 33;
    public final int            CODE_RECEPTION        = 35;
    public final int            CODE_DECONNEXION      = 34;
    public final int            CODE_ERREUR_CONNEXION = 36;

    private ActivityMainBinding   binding;
    private GestionnaireBluetooth gestionnaireBluetooth;
    public Handler                handler;

    /**
     * @brief Méthode appelée à la création de l'activité
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration  appBarConfiguration =
          new AppBarConfiguration
            .Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
            .build();
        NavController navController =
          Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.arg1) {
                    case CODE_CONNEXION:
                        FragmentPupitre.getVueActive().activerBoutonDeconnecter();
                        GestionnaireBluetooth.getGestionnaireBluetooth(null, null).ajouterPeripheriqueConnecter(msg.arg2);
                        break;
                    case CODE_ERREUR_CONNEXION:
                        FragmentPupitre.getVueActive().activerBoutonConnecter();
                        Toast.makeText(getApplicationContext(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };

        gestionnaireBluetooth = GestionnaireBluetooth.getGestionnaireBluetooth(this, handler);
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
        for(Peripherique peripherique: gestionnaireBluetooth.getPeripheriquesConnectes())
        {
            peripherique.deconnecter();
        }
    }
}