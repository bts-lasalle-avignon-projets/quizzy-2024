package fr.hillionj.quizzy.communication.protocoles;

import android.util.Log;

import androidx.annotation.NonNull;

import fr.hillionj.quizzy.communication.protocoles.speciales.application.ProtocoleReceptionReponse;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.ProtocoleAfficherQuestionPrecedente;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.ProtocoleAfficherQuestionSuivante;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.ProtocoleEnvoiQuestion;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.ProtocoleFinQuiz;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.ProtocoleIndicationReponseParticipant;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.ProtocoleInscriptionParticipant;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.ProtocoleLancement;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.ProtocoleLancementQuestion;
import fr.hillionj.quizzy.communication.protocoles.speciales.pupitre.ProtocoleActiverBuzzers;
import fr.hillionj.quizzy.communication.protocoles.speciales.pupitre.ProtocoleDesactiverBuzzers;
import fr.hillionj.quizzy.communication.protocoles.speciales.pupitre.ProtocoleIndicationQuestion;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.receveur.speciales.Ecran;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;
import fr.hillionj.quizzy.session.Question;
import fr.hillionj.quizzy.session.Session;

public class GestionnaireProtocoles {

    private final Session session;

    public GestionnaireProtocoles(Session session) {
        this.session = session;
    }

    public void envoyerQuiz() {
        for (Question question : session.getQuestions()) {
            ProtocoleEnvoiQuestion envoiQuestion = (ProtocoleEnvoiQuestion) Protocole.getProtocole(TypeProtocole.ENVOI_QUESTION);
            envoiQuestion.genererTrame(question);
            envoiQuestion.envoyer(session.getEcrans());
        }
    }

    public void activerBumpers() {
        for (Participant participant : session.getParticipants())
            activerBumpers(participant);
    }

    public void activerBumpers(Participant participant) {
        Question question = session.getQuestionActuelle();
        ProtocoleIndicationQuestion indicationQuestion = (ProtocoleIndicationQuestion) Protocole.getProtocole(TypeProtocole.INDICATION_QUESTION);
        indicationQuestion.genererTrame(session.getNumeroQuestion(), question.getTempsReponse());
        indicationQuestion.envoyer(participant);

        ProtocoleActiverBuzzers activerBuzzers = (ProtocoleActiverBuzzers) Protocole.getProtocole(TypeProtocole.ACTIVER_BUZZERS);
        activerBuzzers.genererTrame(session.getNumeroQuestion());
        activerBuzzers.envoyer(participant);
    }

    public void desactiverBumpers(Participant participant) {
        ProtocoleDesactiverBuzzers desactiverBuzzers = (ProtocoleDesactiverBuzzers) Protocole.getProtocole(TypeProtocole.DESACTIVER_BUZZERS);
        desactiverBuzzers.genererTrame(session.getNumeroQuestion());
        desactiverBuzzers.envoyer(participant);
    }

    public void desactiverBumpers() {
        for (Participant participant : session.getParticipants())
            desactiverBumpers(participant);
    }

    public void indiquerLancement(Ecran ecran) {
        ProtocoleLancement lancement = (ProtocoleLancement) Protocole.getProtocole(TypeProtocole.LANCEMENT);
        lancement.genererTrame();
        lancement.envoyer(ecran);
    }

    public void indiquerLancement() {
        for (Ecran ecran : session.getEcrans())
            indiquerLancement(ecran);
    }

    public void ajouterParticipant(Ecran ecran, @NonNull Participant participant) {
        ProtocoleInscriptionParticipant inscriptionParticipant = (ProtocoleInscriptionParticipant) Protocole.getProtocole(TypeProtocole.INSCRIPTION_PARTICIPANT);
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
        ProtocoleFinQuiz finQuiz = (ProtocoleFinQuiz) Protocole.getProtocole(TypeProtocole.FIN_QUIZ);
        finQuiz.genererTrame();
        finQuiz.envoyer(ecran);
    }

    public void questionSuivante() {
        for (Ecran ecran : session.getEcrans())
            questionSuivante(ecran);
    }

    public void questionSuivante(Ecran ecran) {
        ProtocoleAfficherQuestionSuivante questionSuivante = (ProtocoleAfficherQuestionSuivante) Protocole.getProtocole(TypeProtocole.AFFICHER_QUESTION_SUIVANTE);
        questionSuivante.genererTrame();
        questionSuivante.envoyer(ecran);
    }

    public void questionPrecedente() {
        for (Ecran ecran : session.getEcrans())
            questionPrecedente(ecran);
    }

    public void questionPrecedente(Ecran ecran) {
        ProtocoleAfficherQuestionPrecedente questionPrecedente = (ProtocoleAfficherQuestionPrecedente) Protocole.getProtocole(TypeProtocole.AFFICHER_QUESTION_PRECEDENTE);
        questionPrecedente.genererTrame();
        questionPrecedente.envoyer(ecran);
    }

    public void demarrerChrono() {
        for (Ecran ecran : session.getEcrans())
            demarrerChrono(ecran);
    }

    public void demarrerChrono(Ecran ecran) {
        ProtocoleLancementQuestion lancementQuestion = (ProtocoleLancementQuestion) Protocole.getProtocole(TypeProtocole.DEMARRAGE_QUESTION);
        lancementQuestion.genererTrame();
        lancementQuestion.envoyer(ecran);
    }

    public void indiquerReponse(Participant participant, ProtocoleReceptionReponse receptionReponse) {
        for (Ecran ecran : session.getEcrans())
            indiquerReponse(ecran, participant, receptionReponse);
    }

    public void indiquerReponse(Ecran ecran, Participant participant, ProtocoleReceptionReponse receptionReponse) {
        ProtocoleIndicationReponseParticipant indicationReponseParticipant = (ProtocoleIndicationReponseParticipant) Protocole.getProtocole(TypeProtocole.INDICATION_REPONSE_PARTICIPANT);
        indicationReponseParticipant.genererTrame(participant.getNom(), receptionReponse.getNumeroReponse(), receptionReponse.getTempsReponse());
        indicationReponseParticipant.envoyer(ecran);
    }
}
