package fr.hillionj.quizzy.ihm.popup;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.ihm.vues.VueParametres;
import fr.hillionj.quizzy.ihm.vues.VueParticipants;
import fr.hillionj.quizzy.ihm.vues.VueSession;
import fr.hillionj.quizzy.parametres.ArgumentLancement;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;
import fr.hillionj.quizzy.session.Session;

public class PopupCreerParticipant extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        setCancelable(false);

        View vue = inflater.inflate(R.layout.popup_creer_participant, container, false);
        initialiserVue(vue);
        return vue;
    }

    public void initialiserVue(View vue) {
        TextView texte_erreur_creer_participant = vue.findViewById(R.id.texte_erreur_creer_participant);
        Button btn_creer_participant = vue.findViewById(R.id.btn_creer_participant);
        EditText editText = vue.findViewById(R.id.entree_nom_participant);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String nom = charSequence.toString();
                if (nom.length() > 0) {
                    Participant participantAvecLeMemeNom = Parametres.getParametres().getParticipant(nom);
                    if (participantAvecLeMemeNom != null) {
                        btn_creer_participant.setEnabled(false);
                        texte_erreur_creer_participant.setVisibility(View.VISIBLE);
                        texte_erreur_creer_participant.setText("Erreur : Ce nom est déjà utilisé");
                    } else {
                        btn_creer_participant.setEnabled(true);
                        texte_erreur_creer_participant.setVisibility(View.INVISIBLE);
                    }
                } else {
                    btn_creer_participant.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        btn_creer_participant.setOnClickListener(v -> {
            String nom = editText.getText().toString();
            Participant participant = Parametres.getParametres().getBaseDeDonnees().creerParticipant(nom);
            Parametres.getParametres().getParticipants().add(participant);
            IHM.getIHM().mettreAjourSpinnerParticipants();
            dismiss();
        });

        vue.findViewById(R.id.btn_annuler).setOnClickListener(v -> {
            dismiss();
        });
    }
}
