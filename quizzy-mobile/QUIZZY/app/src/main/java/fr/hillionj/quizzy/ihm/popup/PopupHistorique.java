package fr.hillionj.quizzy.ihm.popup;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.ihm.widgets.ListViewHistorique;

public class PopupHistorique extends DialogFragment {

    private AppCompatActivity activite;
    private ListViewHistorique historique;

    public PopupHistorique() {
        PopupHistorique popup = (PopupHistorique) IHM.getIHM().getIHMActive(getClass());
        if (popup != null)
            this.activite = popup.activite;
        else
            this.activite = null;
    }

    public PopupHistorique(AppCompatActivity activite) {
        this.activite = activite;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        if (activite != null) {
            IHM.getIHM().ajouterIHM(this);
        }

        View vue = inflater.inflate(R.layout.popup_historique, container, false);
        initialiserVue(vue);
        return vue;
    }

    public void initialiserVue(View vue) {
        if (activite == null) {
            dismiss();
            return;
        }
        historique = new ListViewHistorique(activite, vue, R.id.btn_liste_sessions);
        vue.findViewById(R.id.btn_fermer).setOnClickListener(v -> {
            dismiss();
        });
    }

    public void mettreAjourHistorique() {
        historique.mettreAjour();
    }
}
