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
        initialiserVue(root);
        if(vueActive == null)
        {
            vueActive = this;
            GestionnaireBluetooth.getGestionnaireBluetooth(null, null).initialiser();
        }
        vueActive = this;

        return root;
    }

    public void initialiserVue(View vue)
    {
        btnConnecter = vue.findViewById(R.id.bouton_connecter);
        btnConnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FragmentPupitre.this.onClick(v);
            }
        });

        btnDeconnecter = vue.findViewById(R.id.bouton_deconnecter);
        btnDeconnecter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FragmentPupitre.this.onClick(v);
            }
        });
        spinnerListePeripheriques      = vue.findViewById(R.id.liste_peripheriques);
        listViewPeripheriquesConnectes = vue.findViewById(R.id.listViewPeripheriquesConnectes);
        if(vueActive != null)
        {
            btnConnecter.setEnabled(vueActive.btnConnecter.isEnabled());
            btnDeconnecter.setEnabled(vueActive.btnDeconnecter.isEnabled());
            spinnerListePeripheriques.setEnabled(vueActive.spinnerListePeripheriques.isEnabled());
            GestionnaireBluetooth.getGestionnaireBluetooth(null, null)
              .initialiserSpinner(spinnerListePeripheriques);
            GestionnaireBluetooth.getGestionnaireBluetooth(null, null)
              .initialiserListView(listViewPeripheriquesConnectes);
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }

    public void desactiverBoutons()
    {
        btnConnecter.setEnabled(false);
        btnDeconnecter.setEnabled(false);
        spinnerListePeripheriques.setEnabled(false);
    }

    public void activerBoutonConnecter()
    {
        btnConnecter.setEnabled(true);
        btnDeconnecter.setEnabled(false);
        spinnerListePeripheriques.setEnabled(true);
    }

    public void activerBoutonDeconnecter()
    {
        btnConnecter.setEnabled(false);
        btnDeconnecter.setEnabled(true);
        spinnerListePeripheriques.setEnabled(true);
    }
    public void onClick(View v)
    {
        if(v.getId() == R.id.bouton_connecter &&
           GestionnaireBluetooth.getGestionnaireBluetooth(null, null).connecter())
        {
            desactiverBoutons();
        }
        else if(v.getId() == R.id.bouton_deconnecter &&
                GestionnaireBluetooth.getGestionnaireBluetooth(null, null).deconnecter())
        {
            activerBoutonConnecter();
        }
    }
}