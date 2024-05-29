package fr.hillionj.quizzy.ihm.vues;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
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

    public void ajouterIHM(Object pageIHM) {
        ihmActives.remove(pageIHM);
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
}
