package fr.hillionj.quizzy.ihm.popup;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.communication.bluetooth.Peripherique;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.ihm.vues.VueParametres;
import fr.hillionj.quizzy.ihm.vues.VueParticipants;
import fr.hillionj.quizzy.ihm.vues.VueSession;
import fr.hillionj.quizzy.parametres.ArgumentLancement;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;
import fr.hillionj.quizzy.session.Session;

public class PopupNonConnecte extends DialogFragment {

    private final Session session;
    private Peripherique peripherique = null;
    private boolean estAfficher = true;

    private Button btn_connecter;

    public PopupNonConnecte(Session session) {
        for (Participant participant : Parametres.getParametres().getParticipants()) {
            if (participant.getPeripherique() != null && !participant.getPeripherique().seConnecte() && !participant.getPeripherique().estConnecte()) {
                this.peripherique = participant.getPeripherique();
                break;
            }
        }
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
        View vue = inflater.inflate(R.layout.popup_non_connecte, container, false);

        IHM.getIHM().ajouterIHM(this);

        initialiserVue(vue);
        return vue;
    }

    public void initialiserVue(@NonNull View vue) {
        ((TextView)vue.findViewById(R.id.titre_popup)).setText(peripherique.getNom() + " non connecté");
        btn_connecter = vue.findViewById(R.id.btn_connecter);
        btn_connecter.setOnClickListener(v -> {
            peripherique.connecter();
            mettreAjourEtatBoutons();
        });
        vue.findViewById(R.id.btn_dissocier).setOnClickListener(v -> {
            for (Participant participant : Parametres.getParametres().getParticipants()) {
                if (participant.getPeripherique() != null && !participant.getPeripherique().estConnecte() && !participant.getPeripherique().seConnecte()) {
                    participant.setPeripherique(null);
                }
            }
            dismiss();
            if (session.estValide()) {
                if (IHM.getIHM().getActiviteActive() instanceof VueSession) {
                    session.lancer();
                } else {
                    startActivity(new Intent(IHM.getIHM().getActivite(VueParametres.class), VueSession.class));
                }
            }
        });
        vue.findViewById(R.id.btn_annuler).setOnClickListener(v -> {
            dismiss();
        });
    }

    public void mettreAjourEtatBoutons() {
        if (peripherique.seConnecte()) {
            btn_connecter.setEnabled(false);
        } else if (!peripherique.estConnecte()) {
            btn_connecter.setEnabled(true);
        } else if (estAfficher) {
            dismiss();
            if (session.estValide()) {
                if (IHM.getIHM().getActiviteActive() instanceof VueSession) {
                    session.lancer();
                } else {
                    startActivity(new Intent(IHM.getIHM().getActivite(VueParametres.class), VueSession.class));
                }
            }
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        estAfficher = false;
    }
}
