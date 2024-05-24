package fr.hillionj.quizzy.navigation.parametres;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.bdd.BaseDeDonnees;
import fr.hillionj.quizzy.bluetooth.GestionnaireBluetooth;
import fr.hillionj.quizzy.bluetooth.Peripherique;
import fr.hillionj.quizzy.databinding.FragmentNotificationsBinding;
import fr.hillionj.quizzy.receveurs.speciales.Participant;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class FragmentParametres extends Fragment
{
    private FragmentNotificationsBinding binding;

    private Button  btn_associer_participant, btn_creer_participant;
    private Spinner spinner_peripheriques_connectes, spinner_noms_participants, spinner_themes;
    private TextInputLayout textInput_nombre_question;

    private static final List<Participant> participants = new ArrayList<>();
    private static final List<String> nomsParticipants  = getNomsParticipants();
    private static final List<String> listeThemes       = getThemes();
    private static int                nombreQuestion    = 20;
    private static FragmentParametres vueActive         = null;

    private static List<String> getNomsParticipants()
    {
        List<String> nomsParticipants = new ArrayList<>();
        nomsParticipants.add("Aucun");
        nomsParticipants.addAll(BaseDeDonnees.getInstance().getNomsParticipants());
        return nomsParticipants;
    }

    private static List<String> getThemes()
    {
        List<String> listeThemes = new ArrayList<>();
        listeThemes.add("Aléatoire");
        listeThemes.addAll(BaseDeDonnees.getInstance().getThemes());
        return listeThemes;
    }

    private void initialiserBoutonAssociation()
    {
        btn_associer_participant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String       nomParticipant = (String)spinner_noms_participants.getSelectedItem();
                Peripherique peripherique =
                  GestionnaireBluetooth.getGestionnaireBluetooth().getPeripheriques().get(
                    (int)spinner_peripheriques_connectes.getSelectedItemPosition());
                mettreAjourNomParticipant(peripherique, nomParticipant);
            }
        });
    }

    private void initialiserInterface(View vue)
    {
        btn_associer_participant        = vue.findViewById(R.id.btn_associer_participant);
        btn_creer_participant           = vue.findViewById(R.id.btn_creer_participant);
        spinner_peripheriques_connectes = vue.findViewById(R.id.spinner_peripheriques_connectes);
        spinner_noms_participants       = vue.findViewById(R.id.spinner_noms_participants);
        spinner_themes                  = vue.findViewById(R.id.spinner_themes);
        textInput_nombre_question       = vue.findViewById(R.id.textInput_nombre_question);
        textInput_nombre_question.getEditText().setText(nombreQuestion + "");
    }

    private void initialiserSpinners()
    {
        ArrayAdapter<String> adapter =
          new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, nomsParticipants);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_noms_participants.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        initialiserComportementSpinnerParticipants();

        ArrayAdapter<String> adapterTheme =
          new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, listeThemes);
        adapterTheme.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_themes.setAdapter(adapterTheme);
    }

    private void initialiserComportementSpinnerParticipants()
    {
        spinner_peripheriques_connectes.setOnItemSelectedListener(
          new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id)
              {
                  Peripherique peripherique =
                    GestionnaireBluetooth.getGestionnaireBluetooth().getPeripheriques().get(
                      (int)spinner_peripheriques_connectes.getSelectedItemPosition());
                  if(estExistant(peripherique) && !peripherique.getNom().equals("Aucun"))
                  {
                      Participant participant = getParticipant(peripherique);
                      int         indiceListe = nomsParticipants.indexOf(participant.getNom());
                      if(indiceListe < 0)
                      {
                          spinner_noms_participants.setSelection(0);
                      }
                      else
                      {
                          spinner_noms_participants.setSelection(
                            nomsParticipants.indexOf(participant.getNom()));
                      }
                  }
                  else
                  {
                      spinner_noms_participants.setSelection(0);
                  }
              }

              @Override
              public void onNothingSelected(AdapterView<?> arg0)
              {
              }
          });
    }

    private void mettreAjourNomParticipant(Peripherique peripherique, String nom)
    {
        Participant participant = getParticipant(peripherique);
        if(nom.equals("Aucun"))
        {
            participant.setNom(peripherique.getNom());
        }
        else
        {
            participant.setNom(nom);
        }
    }

    public static int getNombreQuestion()
    {
        return nombreQuestion;
    }

    public static String getThemeChoisi()
    {
        if(vueActive == null || vueActive.spinner_themes.getSelectedItemPosition() == 0)
        {
            return null;
        }
        return (String)vueActive.spinner_themes.getSelectedItem();
    }

    @Override
    public void onDestroyView()
    {
        try
        {
            nombreQuestion =
              Integer.parseInt(textInput_nombre_question.getEditText().getText().toString());
        }
        catch(Exception e)
        {
            nombreQuestion = 20;
        }
        super.onDestroyView();
        binding = null;
    }

    public boolean estExistant(Peripherique peripherique)
    {
        for(Participant participant: participants)
        {
            if(participant.getPeripherique() == peripherique)
            {
                return true;
            }
        }
        return false;
    }

    public static Participant getParticipant(Peripherique peripherique)
    {
        for(Participant participant: participants)
        {
            if(participant.getPeripherique() == peripherique)
            {
                return participant;
            }
        }
        Participant participant = new Participant(peripherique.getNom(), peripherique);
        participants.add(participant);
        return participant;
    }

    public static FragmentParametres getVueActive()
    {
        return vueActive;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup               container,
                             Bundle                  savedInstanceState)
    {
        ModeleParametres notificationsViewModel =
          new ViewModelProvider(this).get(ModeleParametres.class);

        binding   = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vueActive = this;
        initialiserVue(root);

        return root;
    }

    public void initialiserVue(View vue)
    {
        initialiserInterface(vue);

        initialiserBoutonAssociation();

        GestionnaireBluetooth.getGestionnaireBluetooth().mettreAjourSpinnerPeripheriques(
          spinner_peripheriques_connectes);

        initialiserSpinners();
    }
}