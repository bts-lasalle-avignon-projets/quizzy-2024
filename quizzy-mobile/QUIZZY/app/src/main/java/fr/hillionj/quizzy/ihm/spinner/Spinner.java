package fr.hillionj.quizzy.ihm.spinner;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.parametres.Parametres;

public abstract class Spinner extends ArrayList<String> {

    private final android.widget.Spinner spinner;
    private final View vue;

    public Spinner(View vue, int id, List<String> contenu) {
        super(contenu);
        this.vue = vue;
        this.spinner = vue.findViewById(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Parametres.getParametres().getActivite(), android.R.layout.simple_spinner_item, this);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public Spinner(View vue, int id) {
        this(vue, id, new ArrayList<>());
    }

    public abstract void setOnItemSelectedListener(AdapterView.OnItemSelectedListener itemSelectedListener);

    protected android.widget.Spinner getSpinner() {
        return spinner;
    }
}
