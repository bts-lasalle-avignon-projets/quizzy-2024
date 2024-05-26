package fr.hillionj.quizzy.ihm.spinner.speciales;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.ihm.spinner.Spinner;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.Participant;

public class SpinnerThemes extends Spinner {

    public SpinnerThemes(View vue, int id) {
        super(vue, id, getThemes());
    }

    @Override
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener itemSelectedListener) {

    }

    private static List<String> getThemes()
    {
        List<String> listeThemes = new ArrayList<>();
        listeThemes.add("Aléatoire");
        listeThemes.addAll(Parametres.getParametres().getSession().getBaseDeDonnees().getThemes());
        return listeThemes;
    }
}
