package fr.hillionj.quizzy.session;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.hillionj.quizzy.Quizzy;
import fr.hillionj.quizzy.bdd.BaseDeDonnees;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.ihm.vues.VueParametres;
import fr.hillionj.quizzy.ihm.vues.VueSession;
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
    private long heureDebutQuestion = 0;
    private final Map<Participant, Integer> score = new HashMap<>();
    private final GestionnaireSonore gestionnaireSonore;
    private final WatchDog watchDog;
    private final IHM ihm;
    private EtapeSession etapeSession = EtapeSession.ARRET;

    public Session(final Parametres parametres, AppCompatActivity activite, IHM ihm) {
        this.parametres = parametres;
        this.ihm = ihm;
        this.baseDeDonnees = new BaseDeDonnees(activite.getApplicationContext());
        this.gestionnaireSonore = new GestionnaireSonore(activite);
        this.watchDog = new WatchDog(ihm);
    }

    public void lancer() {
        questions = this.baseDeDonnees.getNouveauQuiz(parametres);
        this.participants = parametres.getParticipants();
        etapeSession = EtapeSession.MARCHE;
        envoyerQuestion();
    }

    public void stopper() {
        etapeSession = EtapeSession.ARRET;
        ihm.getActivite(VueSession.class).finish();
    }

    public void suivant() {
        indiceQuestion++;
        if (indiceQuestion >= questions.size()) {
            stopper();
            return;
        }
        envoyerQuestion();
    }

    public void precedent() {
        indiceQuestion = indiceQuestion > 0 ? indiceQuestion - 1 : 0;
        envoyerQuestion();
    }

    private void envoyerQuestion() {
        heureDebutQuestion = System.currentTimeMillis();
        IHM.getIHM().afficherInterface(this);
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

    public double getTempsRestant() {
        if (getQuestionActuelle() == null) {
            return 0.0;
        }
        double tempsRestant = (double) getQuestionActuelle().getTempsReponse() - (((double) System.currentTimeMillis() - (double) heureDebutQuestion) / 1000.0);
        return tempsRestant > 0.0 ? tempsRestant : 0.0;
    }

    public void verifierChrono() {
        if (etapeSession != EtapeSession.ARRET && getTempsRestant() == 0.0) {
            etapeSession = EtapeSession.PAUSE_FIN_QUESTION;
            watchDog.pause(5000);
        }
    }

    public void reprendre() {
        if (etapeSession == EtapeSession.PAUSE_FIN_QUESTION) {
            suivant();
        }
        etapeSession = EtapeSession.MARCHE;
    }
}
