package fr.hillionj.quizzy.parametres;

import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.ihm.popup.PopupAucunParticipant;
import fr.hillionj.quizzy.ihm.popup.PopupFinSession;
import fr.hillionj.quizzy.ihm.popup.PopupNonConfigurer;
import fr.hillionj.quizzy.ihm.popup.PopupNonConnecte;
import fr.hillionj.quizzy.session.Session;

public enum ArgumentLancement {

    NON_CONNECTER, NON_CONFIGURER, AUCUN_PARTICIPANT;

    public void envoyerPopup(Session session, IHM ihm) {
        switch (this) {
            case NON_CONNECTER:
                new PopupNonConnecte(session).show(ihm.getActiviteActive().getSupportFragmentManager(), "PopupNonConnecte");
                break;
            case AUCUN_PARTICIPANT:
                new PopupAucunParticipant(session).show(ihm.getActiviteActive().getSupportFragmentManager(), "PopupAucunParticipant");
                break;
            case NON_CONFIGURER:
                new PopupNonConfigurer(session).show(ihm.getActiviteActive().getSupportFragmentManager(), "PopupNonConfigurer");
                break;
        }
    }
}
