package fr.hillionj.quizzy.ihm.spinner;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.parametres.Parametres;

public abstract class Spinner extends ArrayList<String> {

    private final android.widget.Spinner spinner;
    private final AppCompatActivity activite;

    public Spinner(AppCompatActivity activite, int id, List<String> contenu) {
        super(contenu);
        this.activite = activite;
        this.spinner = activite.findViewById(id);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(Parametres.getParametres().getActivite(), android.R.layout.simple_spinner_item, this);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public Spinner(AppCompatActivity activite, int id) {
        this(activite, id, new ArrayList<>());
    }

    public abstract void setOnItemSelectedListener(AdapterView.OnItemSelectedListener itemSelectedListener);

    protected android.widget.Spinner getSpinner() {
        return spinner;
    }
}
