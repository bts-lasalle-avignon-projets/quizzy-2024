package fr.hillionj.quizzy.communication.protocoles;

import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.ProtocoleEnvoiQuestion;
import fr.hillionj.quizzy.communication.protocoles.speciales.pupitre.ProtocoleActiverBuzzers;
import fr.hillionj.quizzy.communication.protocoles.speciales.pupitre.ProtocoleDesactiverBuzzers;
import fr.hillionj.quizzy.communication.protocoles.speciales.pupitre.ProtocoleIndicationQuestion;
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
        indicationQuestion.envoyer(session.getParticipants());

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
}
