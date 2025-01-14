package fr.hillionj.quizzy.ihm.popup;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.ihm.vues.VueSession;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.session.Session;

public class PopupFinSession extends DialogFragment {

    private final Session session;

    public PopupFinSession() {
        PopupFinSession popup = (PopupFinSession) IHM.getIHM().getIHMActive(getClass());
        if (popup != null)
            this.session = popup.session;
        else
            this.session = null;
    }

    public PopupFinSession(Session session) {
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

        View vue = inflater.inflate(R.layout.pop_up_fin_session, container, false);
        initialiserVue(vue);
        return vue;
    }

    public void initialiserVue(@NonNull View vue) {
        if (session == null) {
            dismiss();
            return;
        }
        vue.findViewById(R.id.btn_annuler).setOnClickListener(v -> {
            IHM.getIHM().fermerActivite(VueSession.class);
            dismiss();
        });
        vue.findViewById(R.id.btn_relancer).setOnClickListener(v -> {
            Session session = Parametres.getParametres().nouvelleSession();
            if (session.estValide()) {
                dismiss();
                session.getGestionnaireProtocoles().preparerRelancement();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                session.lancer();
            }
        });
        Button btn_sauvegarder = vue.findViewById(R.id.btn_sauvegarder);
        btn_sauvegarder.setOnClickListener(v -> {
            session.sauvegarder();
            btn_sauvegarder.setEnabled(false);
        });
    }
}
