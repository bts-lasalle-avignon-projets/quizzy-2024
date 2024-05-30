package fr.hillionj.quizzy.ihm.spinner.speciales;

import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fr.hillionj.quizzy.ihm.spinner.Spinner;

public class SpinnerPeripheriques extends Spinner {

    public SpinnerPeripheriques(AppCompatActivity activite, int id) {
        super(activite, id);
    }

    @Override
    public void setOnItemSelectedListener(AdapterView.OnItemSelectedListener itemSelectedListener) {

    }
}
