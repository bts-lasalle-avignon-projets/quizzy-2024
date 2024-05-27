package fr.hillionj.quizzy;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import fr.hillionj.quizzy.communication.Peripherique;
import fr.hillionj.quizzy.databinding.ActivityMainBinding;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.Participant;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "SdCardPath" })
public class Quizzy extends AppCompatActivity {

    private Parametres parametres;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.parametres = Parametres.getParametres(this);

        initialiserNavigation();
    }
    private void initialiserNavigation()
    {
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration
                        .Builder(R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                        .build();
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}
