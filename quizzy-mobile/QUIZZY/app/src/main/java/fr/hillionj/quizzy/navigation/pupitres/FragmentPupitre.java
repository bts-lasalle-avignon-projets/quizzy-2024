package fr.hillionj.quizzy.navigation.pupitres;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import fr.hillionj.quizzy.ActivitePrincipale;
import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.bluetooth.GestionnaireBluetooth;
import fr.hillionj.quizzy.databinding.FragmentDashboardBinding;

public class FragmentPupitre extends Fragment {
    private FragmentDashboardBinding binding;
    public Button btnConnecter, btnDeconnecter;
    public Spinner spinnerListePeripheriques;
    public ListView listViewPeripheriquesConnectes;
    private static FragmentPupitre vueActive = null;
    public static FragmentPupitre getVueActive() {
        return vueActive;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ModelePupitre dashboardViewModel = new ViewModelProvider(this).get(ModelePupitre.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        return root;
    }

    public void initialiserVue(View vue) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void desactiverBoutons() {
    }

    public void activerBoutonConnecter() {
    }

    public void activerBoutonDeconnecter() {
    }
    public void onClick(View v)
    {
    }
}