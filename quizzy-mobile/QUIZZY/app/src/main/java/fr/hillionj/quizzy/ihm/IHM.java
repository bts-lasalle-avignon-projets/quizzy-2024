package fr.hillionj.quizzy.ihm;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.ihm.vues.VueParticipants;
import fr.hillionj.quizzy.ihm.vues.VueSession;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.session.Session;

public class IHM {

    private static IHM ihm;
    private List<Object> ihmActives = new ArrayList<>();
    private Parametres parametres;

    public static IHM getIHM() {
        return IHM.ihm;
    }

    public IHM(Parametres parametress) {
        IHM.ihm = this;
        this.parametres = parametress;
    }

    public void ajouterIHM(Object pageIHM) {
        for (Object ihmActive : ihmActives) {
            if (pageIHM.getClass() == ihmActive.getClass()) {
                ihmActives.remove(ihmActive);
                break;
            }
        }
        ihmActives.add(pageIHM);
    }

    @Nullable
    private Object getIHMActive(Class<?> typeObjet) {
        for (Object ihmActive : ihmActives) {
            if (typeObjet.isInstance(ihmActive)) {
                return ihmActive;
            }
        }
        return null;
    }

    public void mettreAjourListeParticipants() {
        VueParticipants vueParticipants = (VueParticipants) getIHMActive(VueParticipants.class);
        if (vueParticipants != null)
            vueParticipants.mettreAjourListeParticipants();
        VueSession vueSession = (VueSession) getIHMActive(VueSession.class);
        if (vueSession != null)
            vueSession.mettreAjourListeParticipants();
    }

    public void afficherInterface(Session session) {
        VueSession vueSession = (VueSession) getIHMActive(VueSession.class);
        if (vueSession != null) {
            vueSession.setSession(session);
            vueSession.afficherInterface();
        }
    }

    public void afficherInterface() {
        VueSession vueSession = (VueSession) getIHMActive(VueSession.class);
        if (vueSession != null)
            vueSession.afficherInterface();
    }
}
