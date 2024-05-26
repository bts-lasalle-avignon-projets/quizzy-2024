package fr.hillionj.quizzy.ihm.spinner.speciales;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.ihm.spinner.Spinner;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.Participant;

public class SpinnerParticipants extends Spinner {

    public SpinnerParticipants(View vue, int id) {
        super(vue, id, getParticipants());
    }

    private static List<String> getParticipants()
    {
        List<String> nomsParticipants = new ArrayList<>();
        nomsParticipants.add("Aucun");
        for (Participant participant : Parametres.getParametres().getParticipants()) {
            nomsParticipants.add(participant.getNom());
        }
        return nomsParticipants;
    }

    @Override
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener itemSelectedListener) {

    }
}
