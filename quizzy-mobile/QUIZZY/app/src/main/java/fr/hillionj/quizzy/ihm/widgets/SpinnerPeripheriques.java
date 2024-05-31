package fr.hillionj.quizzy.ihm.widgets;

import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.communication.Peripherique;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.parametres.Parametres;

public class SpinnerPeripheriques extends ArrayList<String> {

    private Spinner spinner;

    public SpinnerPeripheriques(AppCompatActivity activite, int id) {
        super(getPeripheriques());
        spinner = activite.findViewById(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(IHM.getIHM().getActiviteActive(), R.layout.spinner_quizzy, this);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private static List<String> getPeripheriques()
    {
        List<String> nomsPeripheriques = new ArrayList<>();
        for (Peripherique peripherique : Parametres.getParametres().getPeripheriques()) {
            nomsPeripheriques.add(peripherique.getNom());
        }
        return nomsPeripheriques;
    }

    public Spinner getSpinner() {
        return spinner;
    }
}
