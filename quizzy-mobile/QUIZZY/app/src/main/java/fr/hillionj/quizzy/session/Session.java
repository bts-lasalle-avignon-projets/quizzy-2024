package fr.hillionj.quizzy.session;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.hillionj.quizzy.bdd.BaseDeDonnees;
import fr.hillionj.quizzy.parametres.Ecran;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.Participant;
import fr.hillionj.quizzy.son.GestionnaireSonore;

public class Session {

    private BaseDeDonnees baseDeDonnees;
    private Parametres parametres;
    private List<Participant> participants = null;
    private List<Ecran> ecrans = null;
    private List<Question> questions = null;
    private final Map<Participant, Integer> score = new HashMap<>();
    private final GestionnaireSonore gestionnaireSonore;

    public Session(final Parametres parametres, AppCompatActivity activite) {
        this.parametres = parametres;
        this.baseDeDonnees = new BaseDeDonnees(activite.getApplicationContext());
        this.gestionnaireSonore = new GestionnaireSonore(activite);
    }

    public void jouer() {
        this.baseDeDonnees.getNouveauQuiz(parametres);
        this.participants = parametres.getParticipants();
    }

    public void abandonner() {

    }

    public void sauvegarder() {

    }

    public BaseDeDonnees getBaseDeDonnees() {
        return baseDeDonnees;
    }

    public GestionnaireSonore getGestionnaireSonore() {
        return gestionnaireSonore;
    }
}
