package fr.hillionj.quizzy.ihm.widgets;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;

public class SpinnerParticipants {

    private Spinner spinner;

    public SpinnerParticipants(AppCompatActivity activite, int id) {
        spinner = activite.findViewById(id);
        mettreAjourSpinnerParticipants();
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

    public void mettreAjourSpinnerParticipants() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(IHM.getIHM().getActiviteActive(), R.layout.spinner_quizzy, getParticipants());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
