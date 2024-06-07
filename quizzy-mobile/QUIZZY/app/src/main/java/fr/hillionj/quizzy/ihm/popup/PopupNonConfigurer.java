package fr.hillionj.quizzy.ihm.popup;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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

public class PopupNonConfigurer extends DialogFragment {

    private final Session session;
    private Peripherique peripherique = null;

    public PopupNonConfigurer() {
        PopupNonConfigurer popup = (PopupNonConfigurer) IHM.getIHM().getIHMActive(getClass());
        if (popup != null) {
            this.peripherique = popup.peripherique;
            this.session = popup.session;
        } else {
            this.session = null;
        }
    }

    public PopupNonConfigurer(Session session) {
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

        if (session != null) {
            IHM.getIHM().ajouterIHM(this);
        }

        View vue = inflater.inflate(R.layout.popup_non_configurer, container, false);
        initialiserVue(vue);
        return vue;
    }

    public void initialiserVue(@NonNull View vue) {
        if (session == null) {
            dismiss();
            return;
        }
        ((TextView)vue.findViewById(R.id.titre_popup)).setText(peripherique.getNom() + " non configuré");
        vue.findViewById(R.id.btn_deconnecter).setOnClickListener(v -> {
            peripherique.deconnecter();
            dismiss();
            if (session.estValide()) {
                if (IHM.getIHM().getActiviteActive() instanceof VueSession) {
                    session.lancer();
                } else {
                    IHM.getIHM().demarrerActivite(this, IHM.getIHM().getActivite(VueParametres.class), VueSession.class);
                }
            }
        });
        vue.findViewById(R.id.btn_configurer).setOnClickListener(v -> {
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

    private Peripherique getPeripherique() {
        for (Peripherique peripherique : Parametres.getParametres().getPeripheriques()) {
            if (peripherique.estConnecte() && Parametres.getParametres().getParticipantAssocier(peripherique) == null) {
                return peripherique;
            }
        }
        return null;
    }
}
