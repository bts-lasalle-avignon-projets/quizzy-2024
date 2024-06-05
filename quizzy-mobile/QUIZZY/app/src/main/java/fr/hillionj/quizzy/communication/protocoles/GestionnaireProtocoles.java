package fr.hillionj.quizzy.communication.protocoles;

import androidx.annotation.NonNull;

import fr.hillionj.quizzy.communication.protocoles.speciales.application.ReceptionReponse;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.AfficherQuestionPrecedente;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.AfficherQuestionSuivante;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.EnregistrerQuestion;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.FinSession;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.EnregistrerSelectionParticipant;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.EnregistrerParticipant;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.LancementSession;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.DemarrerChrono;
import fr.hillionj.quizzy.communication.protocoles.speciales.pupitre.ActiverBumpers;
import fr.hillionj.quizzy.communication.protocoles.speciales.pupitre.DesactiverBumpers;
import fr.hillionj.quizzy.communication.protocoles.speciales.pupitre.EnregistrerTempsQuestion;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.receveur.speciales.Ecran;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;
import fr.hillionj.quizzy.session.contenu.Question;
import fr.hillionj.quizzy.session.Session;

public class GestionnaireProtocoles {

    private final Session session;

    public GestionnaireProtocoles(Session session) {
        this.session = session;
    }

    public void envoyerQuiz() {
        for (Question question : session.getQuestions()) {
            EnregistrerQuestion enregistrerQuestion = (EnregistrerQuestion) Protocole.getProtocole(TypeProtocole.ENREGISTRER_QUESTION);
            enregistrerQuestion.genererTrame(question);
            enregistrerQuestion.envoyer(session.getEcrans());
        }
    }

    public void activerBumpers() {
        for (Participant participant : session.getParticipants())
            activerBumpers(participant);
    }

    public void activerBumpers(Participant participant) {
        Question question = session.getQuestionActuelle();
        EnregistrerTempsQuestion indicationQuestion = (EnregistrerTempsQuestion) Protocole.getProtocole(TypeProtocole.ENREGISTRER_TEMPS_QUESTION);
        indicationQuestion.genererTrame(session.getNumeroQuestion(), question.getTempsReponse());
        indicationQuestion.envoyer(participant);

        ActiverBumpers activerBuzzers = (ActiverBumpers) Protocole.getProtocole(TypeProtocole.ACTIVER_BUMPERS);
        activerBuzzers.genererTrame(session.getNumeroQuestion());
        activerBuzzers.envoyer(participant);
    }

    public void desactiverBumpers(Participant participant) {
        DesactiverBumpers desactiverBuzzers = (DesactiverBumpers) Protocole.getProtocole(TypeProtocole.DESACTIVER_BUMPERS);
        desactiverBuzzers.genererTrame(session.getNumeroQuestion());
        desactiverBuzzers.envoyer(participant);
    }

    public void desactiverBumpers() {
        for (Participant participant : session.getParticipants())
            desactiverBumpers(participant);
    }

    public void indiquerLancement(Ecran ecran) {
        LancementSession lancement = (LancementSession) Protocole.getProtocole(TypeProtocole.LANCEMENT_SESSION);
        lancement.genererTrame();
        lancement.envoyer(ecran);
    }

    public void indiquerLancement() {
        for (Ecran ecran : session.getEcrans())
            indiquerLancement(ecran);
    }

    public void ajouterParticipant(Ecran ecran, @NonNull Participant participant) {
        EnregistrerParticipant inscriptionParticipant = (EnregistrerParticipant) Protocole.getProtocole(TypeProtocole.ENREGISTRER_PARTICIPANT);
        inscriptionParticipant.genererTrame(participant.getNom() /*TODO METTRE PID*/, participant.getNom());
        inscriptionParticipant.envoyer(ecran);
    }

    public void ajouterParticipant(Participant participant) {
        for (Ecran ecran : Parametres.getParametres().getEcrans())
            if (ecran.getPeripherique().estConnecte())
                ajouterParticipant(ecran, participant);
    }

    public void preparerRelancement() {
        for (Ecran ecran : Parametres.getParametres().getEcrans())
            preparerRelancement(ecran);
    }

    public void preparerRelancement(Ecran ecran) {
        indiquerLancement(ecran);
        for (Participant participant : Parametres.getParametres().getParticipants()) {
            if (participant.getPeripherique() != null) {
                ajouterParticipant(ecran, participant);
            }
        }
    }

    public void finSession() {
        for (Ecran ecran : session.getEcrans())
            finSession(ecran);
    }

    public void finSession(Ecran ecran) {
        FinSession finQuiz = (FinSession) Protocole.getProtocole(TypeProtocole.FIN_SESSION);
        finQuiz.genererTrame();
        finQuiz.envoyer(ecran);
    }

    public void questionSuivante() {
        for (Ecran ecran : session.getEcrans())
            questionSuivante(ecran);
    }

    public void questionSuivante(Ecran ecran) {
        AfficherQuestionSuivante questionSuivante = (AfficherQuestionSuivante) Protocole.getProtocole(TypeProtocole.AFFICHER_QUESTION_SUIVANTE);
        questionSuivante.genererTrame();
        questionSuivante.envoyer(ecran);
    }

    public void questionPrecedente() {
        for (Ecran ecran : session.getEcrans())
            questionPrecedente(ecran);
    }

    public void questionPrecedente(Ecran ecran) {
        AfficherQuestionPrecedente questionPrecedente = (AfficherQuestionPrecedente) Protocole.getProtocole(TypeProtocole.AFFICHER_QUESTION_PRECEDENTE);
        questionPrecedente.genererTrame();
        questionPrecedente.envoyer(ecran);
    }

    public void demarrerChrono() {
        for (Ecran ecran : session.getEcrans())
            demarrerChrono(ecran);
    }

    public void demarrerChrono(Ecran ecran) {
        DemarrerChrono lancementQuestion = (DemarrerChrono) Protocole.getProtocole(TypeProtocole.DEMARRER_CHRONO);
        lancementQuestion.genererTrame();
        lancementQuestion.envoyer(ecran);
    }

    public void indiquerReponse(Participant participant, ReceptionReponse receptionReponse) {
        for (Ecran ecran : session.getEcrans())
            indiquerReponse(ecran, participant, receptionReponse);
    }

    public void indiquerReponse(Ecran ecran, Participant participant, ReceptionReponse receptionReponse) {
        EnregistrerSelectionParticipant indicationReponseParticipant = (EnregistrerSelectionParticipant) Protocole.getProtocole(TypeProtocole.ENREGISTRER_SELECTION_PARTICIPANT);
        indicationReponseParticipant.genererTrame(participant.getNom(), receptionReponse.getNumeroReponse(), receptionReponse.getTempsReponse());
        indicationReponseParticipant.envoyer(ecran);
    }
}
