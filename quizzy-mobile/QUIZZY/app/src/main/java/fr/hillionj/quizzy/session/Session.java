package fr.hillionj.quizzy.session;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.hillionj.quizzy.bdd.BaseDeDonnees;
import fr.hillionj.quizzy.ihm.IHM;
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
    private int indiceQuestion = 0;
    private final Map<Participant, Integer> score = new HashMap<>();
    private final GestionnaireSonore gestionnaireSonore;

    public Session(final Parametres parametres, AppCompatActivity activite) {
        this.parametres = parametres;
        this.baseDeDonnees = new BaseDeDonnees(activite.getApplicationContext());
        this.gestionnaireSonore = new GestionnaireSonore(activite);
    }

    public void lancer() {
        questions = this.baseDeDonnees.getNouveauQuiz(parametres);
        IHM.getIHM().afficherInterface(this);
        this.participants = parametres.getParticipants();
    }

    public void stopper() {

    }

    public void suivant() {
        indiceQuestion++;
        envoyerQuestion();
    }

    public void precedent() {
        indiceQuestion = indiceQuestion > 0 ? indiceQuestion - 1 : 0;
        envoyerQuestion();
    }

    private void envoyerQuestion() {
        IHM.getIHM().afficherInterface();
    }

    public void sauvegarder() {
    }

    public BaseDeDonnees getBaseDeDonnees() {
        return baseDeDonnees;
    }

    public GestionnaireSonore getGestionnaireSonore() {
        return gestionnaireSonore;
    }

    public Question getQuestionActuelle() {
        return questions.get(indiceQuestion);
    }

    public int getNumeroQuestion() {
        return indiceQuestion + 1;
    }

    public int getTotalQuestions() {
        return questions.size();
    }
}
