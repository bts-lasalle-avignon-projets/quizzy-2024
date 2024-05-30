package fr.hillionj.quizzy.ihm.widgets;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.Participant;

public class SpinnerParticipants extends ArrayList<String> {

    private Spinner spinner;

    public SpinnerParticipants(AppCompatActivity activite, int id) {
        super(getParticipants());
        spinner = activite.findViewById(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Parametres.getParametres().getActivite(), R.layout.spinner_quizzy, this);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
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

    public Spinner getSpinner() {
        return spinner;
    }
}
