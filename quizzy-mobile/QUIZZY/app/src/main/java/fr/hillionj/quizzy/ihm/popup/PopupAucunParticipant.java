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
import androidx.fragment.app.DialogFragment;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.ihm.vues.VueParametres;
import fr.hillionj.quizzy.ihm.vues.VueParticipants;
import fr.hillionj.quizzy.ihm.vues.VueSession;
import fr.hillionj.quizzy.parametres.ArgumentLancement;
import fr.hillionj.quizzy.session.Session;

public class PopupAucunParticipant extends DialogFragment {

    private final Session session;

    public PopupAucunParticipant(Session session) {
        this.session = session;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        setCancelable(false);

        View vue = inflater.inflate(R.layout.popup_aucun_participant, container, false);
        initialiserVue(vue);
        return vue;
    }

    public void initialiserVue(View vue) {
        vue.findViewById(R.id.btn_continuer).setOnClickListener(v -> {
            session.ajouterArgument(ArgumentLancement.AUCUN_PARTICIPANT);
            dismiss();
            if (session.estValide()) {
                if (IHM.getIHM().getActiviteActive() instanceof VueSession) {
                    session.lancer();
                } else {
                    startActivity(new Intent(IHM.getIHM().getActivite(VueParametres.class), VueSession.class));
                }
            }
        });
        vue.findViewById(R.id.btn_configurer_popup_aucun_participant).setOnClickListener(v -> {
            IHM.getIHM().fermerPopups();
            if (IHM.getIHM().getActiviteActive() instanceof VueSession) {
                IHM.getIHM().fermerActivite(VueSession.class);
            }
            startActivity(new Intent(IHM.getIHM().getActivite(VueParametres.class), VueParticipants.class));
        });
        vue.findViewById(R.id.btn_annuler).setOnClickListener(v -> {
            dismiss();
        });
    }
}
