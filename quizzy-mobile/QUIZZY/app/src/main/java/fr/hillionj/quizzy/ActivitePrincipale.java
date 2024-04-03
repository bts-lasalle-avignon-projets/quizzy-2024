package fr.hillionj.quizzy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

public class ActivitePrincipale extends AppCompatActivity {
    private ActivityMainBinding binding;
    private GestionnaireBluetooth gestionnaireBluetooth;
    public final int CODE_CONNEXION = 33, CODE_RECEPTION = 35, CODE_DECONNEXION = 34, CODE_ERREUR_CONNEXION = 36;
    public Handler handler;
    private static ActivitePrincipale activitePrincipale;
    public static ActivitePrincipale getActivite() {
        return activitePrincipale;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitePrincipale = this;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        gestionnaireBluetooth = GestionnaireBluetooth.initialiserGestionnaireBluetooth(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Peripherique peripherique : gestionnaireBluetooth.getPeripheriquesConnectes()) {
            peripherique.deconnecter();
        }
    }
}