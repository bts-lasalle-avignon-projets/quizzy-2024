package fr.hillionj.quizzy.session;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.hillionj.quizzy.bdd.BaseDeDonnees;
import fr.hillionj.quizzy.communication.bluetooth.Peripherique;
import fr.hillionj.quizzy.communication.protocoles.GestionnaireProtocoles;
import fr.hillionj.quizzy.communication.protocoles.speciales.application.ProtocoleReceptionReponse;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.ihm.popup.PopupFinSession;
import fr.hillionj.quizzy.parametres.ArgumentLancement;
import fr.hillionj.quizzy.parametres.receveur.speciales.Ecran;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;
import fr.hillionj.quizzy.son.GestionnaireSonore;

public class Session {

    private BaseDeDonnees baseDeDonnees;
    private Parametres parametres;
    private List<Participant> participants = new ArrayList<>();
    private List<Ecran> ecrans = new ArrayList<>();
    private List<Question> questions = null;
    private int indiceQuestion = 0;
    private long heureDebutQuestion = 0;
    private final Map<Participant, Integer> score = new HashMap<>();
    private final GestionnaireSonore gestionnaireSonore;
    private final GestionnaireProtocoles gestionnaireProtocoles;
    private final WatchDog watchDog;
    private final IHM ihm;
    private List<ArgumentLancement> arguments = new ArrayList<>();
    private EtapeSession etapeSession = EtapeSession.ARRET;


    public Session(@NonNull final Session sessionPrecedente) {
        this.parametres = sessionPrecedente.parametres;
        this.ihm = sessionPrecedente.ihm;
        this.baseDeDonnees = sessionPrecedente.baseDeDonnees;
        this.gestionnaireSonore = sessionPrecedente.gestionnaireSonore;
        this.gestionnaireProtocoles = new GestionnaireProtocoles(this);
        this.watchDog = sessionPrecedente.watchDog;
    }

    public Session(final Parametres parametres, @NonNull AppCompatActivity activite, IHM ihm) {
        this.parametres = parametres;
        this.ihm = ihm;
        this.baseDeDonnees = new BaseDeDonnees(activite.getApplicationContext());
        this.gestionnaireSonore = new GestionnaireSonore(activite);
        this.gestionnaireProtocoles = new GestionnaireProtocoles(this);
        this.watchDog = new WatchDog(ihm);
    }

    public boolean estValide() {
        ArgumentLancement argumentManquant = getArgumentManquant();
        if (argumentManquant != null) {
            argumentManquant.envoyerPopup(this, ihm);
            return false;
        }
        return true;
    }

    @Nullable
    public ArgumentLancement getArgumentManquant() {
        for (Peripherique peripherique : parametres.getPeripheriques()) {
            if (Parametres.getParametres().estUnEcran(peripherique)) {
                continue;
            }
            Participant participant = parametres.getParticipantAssocier(peripherique);
            if (!peripherique.estConnecte() && !peripherique.seConnecte() && participant != null && !estArgument(ArgumentLancement.NON_CONNECTER)) {
                return ArgumentLancement.NON_CONNECTER;
            } else if ((peripherique.estConnecte() || peripherique.seConnecte()) && participant == null && !estArgument(ArgumentLancement.NON_CONFIGURER)) {
                return ArgumentLancement.NON_CONFIGURER;
            }
        }
        return getParticipantsValides().isEmpty() && !estArgument(ArgumentLancement.AUCUN_PARTICIPANT) ? ArgumentLancement.AUCUN_PARTICIPANT : null;
    }

    private boolean estArgument(ArgumentLancement argumentLancement) {
        for (ArgumentLancement argument : arguments) {
            if (argument == argumentLancement) {
                return true;
            }
        }
        return false;
    }

    public void lancer() {
        ihm.fermerPopups();
        questions = this.baseDeDonnees.getNouveauQuiz(parametres);
        this.participants = getParticipantsValides();
        ihm.mettreAjourListeParticipants();
        for (Ecran ecran : Parametres.getParametres().getEcrans()) {
            if (ecran.getPeripherique().estConnecte()) {
                this.ecrans.add(ecran);
            }
        }
        etapeSession = EtapeSession.MARCHE;
        gestionnaireProtocoles.envoyerQuiz();
        gestionnaireProtocoles.indiquerLancement();
        envoyerQuestion();
    }

    @NonNull
    private List<Participant> getParticipantsValides() {
        List<Participant> listeParticipants = new ArrayList<>();
        for (Peripherique peripherique : parametres.getPeripheriques()) {
            Participant participant = parametres.getParticipantAssocier(peripherique);
            if ((peripherique.estConnecte() || peripherique.seConnecte()) && participant == null && estArgument(ArgumentLancement.NON_CONFIGURER)) {
                listeParticipants.add(new Participant(peripherique.getNom(), peripherique));
            } else if ((peripherique.estConnecte() || peripherique.seConnecte()) && participant != null) {
                listeParticipants.add(participant);
            }
        }
        return listeParticipants;
    }

    public void stopper() {
        etapeSession = EtapeSession.ARRET;
        gestionnaireProtocoles.finSession();
        new PopupFinSession(this).show(ihm.getActiviteActive().getSupportFragmentManager(), "PopupFinSession");
    }

    public void suivant() {
        if (indiceQuestion + 1 >= questions.size()) {
            stopper();
            return;
        }
        indiceQuestion++;
        gestionnaireProtocoles.questionSuivante();
        envoyerQuestion();
    }

    public void precedent() {
        indiceQuestion = indiceQuestion > 0 ? indiceQuestion - 1 : 0;
        gestionnaireProtocoles.questionPrecedente();
        envoyerQuestion();
    }

    private void envoyerQuestion() {
        if (etapeSession == EtapeSession.PAUSE_FIN_QUESTION) {
            etapeSession = EtapeSession.MARCHE;
            watchDog.reprendre();
        }
        gestionnaireProtocoles.demarrerChrono();
        gestionnaireProtocoles.activerBumpers();
        heureDebutQuestion = System.currentTimeMillis();
        ihm.afficherInterface();
    }

    public void selectionnerProposition(Participant participant, @NonNull ProtocoleReceptionReponse receptionReponse) {
        if (etapeSession == EtapeSession.ARRET) {
            return;
        }
        Question question = questions.get(receptionReponse.getNumeroQuestion() - 1);
        if (!estSelectionne(participant, question)) {
            question.ajouterSelection(participant, receptionReponse.getNumeroReponse() - 1);
        }
        gestionnaireProtocoles.desactiverBumpers(participant);
        gestionnaireProtocoles.indiquerReponse(participant, receptionReponse);
        ihm.afficherInterface();
    }

    private boolean estSelectionne(Participant participant, @NonNull Question question) {
        return question.estSelectionne(participant);
    }

    public void sauvegarder() {

    }

    public BaseDeDonnees getBaseDeDonnees() {
        return baseDeDonnees;
    }

    public GestionnaireSonore getGestionnaireSonore() {
        return gestionnaireSonore;
    }

    public GestionnaireProtocoles getGestionnaireProtocoles() {
        return gestionnaireProtocoles;
    }

    public Question getQuestionActuelle() {
        return questions == null || indiceQuestion >= questions.size() ? null : questions.get(indiceQuestion);
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
        return Math.max(tempsRestant, 0.0);
    }

    public void verifierChrono() {
        if (etapeSession != EtapeSession.ARRET && (getTempsRestant() == 0.0 || estReponduParTous())) {
            etapeSession = EtapeSession.PAUSE_FIN_QUESTION;
            ihm.afficherInterface();
            watchDog.pause(5000);
        }
    }

    private boolean estReponduParTous() {
        //if (participants.isEmpty()) {
            return false;
        /*}
        for (Participant participant : participants) {
            if (!getQuestionActuelle().estSelectionne(participant)) {
                return false;
            }
        }
        return true;*/
    }

    public void reprendre() {
        EtapeSession derniereEtape = etapeSession;
        if (etapeSession != EtapeSession.ARRET) {
            etapeSession = EtapeSession.MARCHE;
        }
        if (derniereEtape == EtapeSession.PAUSE_FIN_QUESTION) {
            suivant();
        } else if (derniereEtape == EtapeSession.PAUSE) {
            heureDebutQuestion += System.currentTimeMillis() - watchDog.getHeureDebutTempsPause();
            watchDog.reprendre();
        }
    }

    public void pause() {
        etapeSession = EtapeSession.PAUSE;
        watchDog.pause();
    }

    public List<Ecran> getEcrans() {
        return ecrans;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public EtapeSession getEtape() {
        return etapeSession;
    }

    public List<ArgumentLancement> getArguments() {
        return arguments;
    }

    public void ajouterArgument(ArgumentLancement argumentLancement) {
        arguments.add(argumentLancement);
    }

    public int getScore(Participant participant) {
        int score = 0;
        if (questions == null) {
            return score;
        }
        for (Question question : questions) {
            if (question == getQuestionActuelle() && etapeSession == EtapeSession.PAUSE_FIN_QUESTION && question.estPropositionValide(participant)) {
                score++;
            } else if (question != getQuestionActuelle() && question.estPropositionValide(participant)) {
                score++;
            }
        }
        return score;
    }
}
