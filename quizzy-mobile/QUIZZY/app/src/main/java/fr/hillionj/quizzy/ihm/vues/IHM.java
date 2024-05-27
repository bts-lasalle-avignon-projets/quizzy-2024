package fr.hillionj.quizzy.ihm.vues;

import java.util.ArrayList;
import java.util.List;
import fr.hillionj.quizzy.ihm.vues.session.FragmentQuiz;
import fr.hillionj.quizzy.parametres.Parametres;

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

    public void mettreAjourDeroulement() {
        FragmentQuiz ihmSession = (FragmentQuiz) getIHMActive(FragmentQuiz.class);
        if (ihmSession == null)
            return;
        ihmSession.mettreAjourDeroulement();
    }

    public void ajouterIHM(Object pageIHM) {
        ihmActives.remove(pageIHM);
        ihmActives.add(pageIHM);
    }

    private Object getIHMActive(Class<?> typeObjet) {
        for (Object ihmActive : ihmActives) {
            if (typeObjet.isInstance(ihmActive)) {
                return ihmActive;
            }
        }
        return null;
    }
}
