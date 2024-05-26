package fr.hillionj.quizzy.session;

import androidx.appcompat.app.AppCompatActivity;

import fr.hillionj.quizzy.bdd.BaseDeDonnees;
import fr.hillionj.quizzy.parametres.Parametres;

public class Session {

    private BaseDeDonnees baseDeDonnees;
    private Parametres parametres;

    public Session(final Parametres parametres) {
        this.parametres = parametres;
        this.baseDeDonnees = new BaseDeDonnees(parametres.getActivite().getApplicationContext());
    }

    public BaseDeDonnees getBaseDeDonnees() {
        return baseDeDonnees;
    }
}
