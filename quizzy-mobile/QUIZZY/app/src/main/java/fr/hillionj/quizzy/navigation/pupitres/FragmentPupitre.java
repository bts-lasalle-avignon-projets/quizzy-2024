package fr.hillionj.quizzy.navigation.pupitres;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.bluetooth.GestionnaireBluetooth;
import fr.hillionj.quizzy.bluetooth.Peripherique;
import fr.hillionj.quizzy.databinding.FragmentDashboardBinding;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class FragmentPupitre extends Fragment
{
    private FragmentDashboardBinding binding;
    public Button                    btnConnecter, btnDeconnecter;
    public Spinner                   spinnerListePeripheriques;
    public ListView                  listViewPeripheriquesConnectes;
    private static FragmentPupitre   vueActive = null;
    public static FragmentPupitre    getVueActive()
    {
        return vueActive;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup               container,
                             Bundle                  savedInstanceState)
    {
        ModelePupitre dashboardViewModel = new ViewModelProvider(this).get(ModelePupitre.class);
        binding   = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vueActive = this;
        initialiserVue(root);
        return root;
    }

    public void initialiserVue(View vue)
    {
        initialiserBoutons(vue);
        spinnerListePeripheriques      = vue.findViewById(R.id.liste_peripheriques);
        listViewPeripheriquesConnectes = vue.findViewById(R.id.listViewPeripheriquesConnectes);

        GestionnaireBluetooth gestionnaireBluetooth = GestionnaireBluetooth.getGestionnaireBluetooth();
        gestionnaireBluetooth.mettreAjourSpinnerPeripheriques(spinnerListePeripheriques);
        gestionnaireBluetooth.mettreAjourListViewPeripheriques(listViewPeripheriquesConnectes);
    }

    private void initialiserBoutons(View vue) {
        btnConnecter = initialiserBouton(vue, R.id.bouton_connecter);
        btnDeconnecter = initialiserBouton(vue, R.id.bouton_deconnecter);
    }

    private Button initialiserBouton(View vue, int bouton_connecter) {
        Button bouton = vue.findViewById(bouton_connecter);
        bouton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentPupitre.this.onClick(v);
            }
        });
        return bouton;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

    public void mettreAjourEtatBoutons() {
        Peripherique peripheriqueSelectionne = GestionnaireBluetooth.getGestionnaireBluetooth().getPeripheriqueSelectionne();
        if(peripheriqueSelectionne.estConnecte())
        {
            btnConnecter.setEnabled(false);
            btnDeconnecter.setEnabled(true);
            spinnerListePeripheriques.setEnabled(true);
        } else
        {
            btnConnecter.setEnabled(true);
            btnDeconnecter.setEnabled(false);
            spinnerListePeripheriques.setEnabled(true);
        }
    }

    public void onClick(View v)
    {
        if(v.getId() == R.id.bouton_connecter && GestionnaireBluetooth.getGestionnaireBluetooth().connecter())
        {
            mettreAjourEtatBoutons();
        }
        else if(v.getId() == R.id.bouton_deconnecter)
        {
            GestionnaireBluetooth.getGestionnaireBluetooth().deconnecter();
            mettreAjourEtatBoutons();
        }
    }
}