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

    public PopupNonConnecte() {
        PopupNonConnecte popup = (PopupNonConnecte) IHM.getIHM().getIHMActive(getClass());
        if (popup != null) {
            this.peripherique = popup.peripherique;
            this.session = popup.session;
            this.estAfficher = popup.estAfficher;
        } else {
            this.session = null;
        }
    }

    public PopupNonConnecte(Session session) {
        this.peripherique = getPeripherique();
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

        if (session != null) {
            IHM.getIHM().ajouterIHM(this);
        }

        initialiserVue(vue);
        return vue;
    }

    public void initialiserVue(@NonNull View vue) {
        if (session == null) {
            dismiss();
            return;
        }
        ((TextView)vue.findViewById(R.id.titre_popup)).setText(peripherique.getNom() + " non connecté");
        btn_connecter = vue.findViewById(R.id.btn_connecter);
        btn_connecter.setOnClickListener(v -> {
            peripherique.connecter();
            mettreAjourEtatBoutons();
        });
        vue.findViewById(R.id.btn_dissocier).setOnClickListener(v -> {
            Parametres.getParametres().getParticipantAssocier(peripherique).setPeripherique(null);
            dismiss();
            if (session.estValide()) {
                if (IHM.getIHM().getActiviteActive() instanceof VueSession) {
                    session.lancer();
                } else {
                    IHM.getIHM().demarrerActivite(this, IHM.getIHM().getActivite(VueParametres.class), VueSession.class);
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
                    IHM.getIHM().demarrerActivite(this, IHM.getIHM().getActivite(VueParametres.class), VueSession.class);
                }
            }
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        estAfficher = false;
    }

    private Peripherique getPeripherique() {
        for (Participant participant : Parametres.getParametres().getParticipants()) {
            if (participant.getPeripherique() != null && !participant.getPeripherique().seConnecte() && !participant.getPeripherique().estConnecte()) {
                return participant.getPeripherique();
            }
        }
        return null;
    }
}
