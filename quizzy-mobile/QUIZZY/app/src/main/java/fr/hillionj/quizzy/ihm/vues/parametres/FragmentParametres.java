package fr.hillionj.quizzy.ihm.vues.parametres;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.databinding.FragmentNotificationsBinding;
import fr.hillionj.quizzy.ihm.spinner.Spinner;
import fr.hillionj.quizzy.ihm.spinner.speciales.SpinnerParticipants;
import fr.hillionj.quizzy.ihm.spinner.speciales.SpinnerPeripheriques;
import fr.hillionj.quizzy.ihm.spinner.speciales.SpinnerThemes;
import fr.hillionj.quizzy.parametres.Parametres;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class FragmentParametres extends Fragment
{
    private Button  btn_associer_participant, btn_creer_participant;
    private Spinner spinner_peripheriques_connectes, spinner_noms_participants, spinner_themes;
    private TextInputLayout textInput_nombre_question;

    private Parametres parametres;

    private void initialiserInterface(View vue)
    {
        btn_associer_participant        = vue.findViewById(R.id.btn_associer_participant);
        btn_creer_participant           = vue.findViewById(R.id.btn_creer_participant);
        spinner_peripheriques_connectes = new SpinnerPeripheriques(vue, R.id.spinner_peripheriques_connectes);
        spinner_noms_participants       = new SpinnerParticipants(vue, R.id.spinner_noms_participants);
        spinner_themes                  = new SpinnerThemes(vue, R.id.spinner_themes);
        textInput_nombre_question       = vue.findViewById(R.id.textInput_nombre_question);
        textInput_nombre_question.getEditText().setText(parametres.getNombreDeQuestions()  + "");
    }

    @Override
    public void onDestroyView()
    {
        int nombreQuestion;
        try
        {
            nombreQuestion = Integer.parseInt(textInput_nombre_question.getEditText().getText().toString());
        }
        catch(Exception e)
        {
            nombreQuestion = 20;
        }
        Parametres.getParametres().setNombreDeQuestions(nombreQuestion);
        super.onDestroyView();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup               container,
                             Bundle                  savedInstanceState)
    {
        ModeleParametres notificationsViewModel = new ViewModelProvider(this).get(ModeleParametres.class);
        View root = FragmentNotificationsBinding.inflate(inflater, container, false).getRoot();

        this.parametres = Parametres.getParametres();

        initialiserInterface(root);

        return root;
    }
}