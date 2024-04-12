package fr.hillionj.quizzy.protocole;

import android.util.Log;

import fr.hillionj.quizzy.protocole.speciales.application.ProtocoleAcquitement;
import fr.hillionj.quizzy.protocole.speciales.application.ProtocoleReceptionReponse;
import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleAfficherQuestionPrecedente;
import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleAfficherQuestionSuivante;
import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleAfficherReponse;
import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleEnvoiQuestion;
import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleFinQuiz;
import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleIndicationReponseParticipant;
import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleInscriptionParticipant;
import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleLancement;
import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleLancementQuestion;
import fr.hillionj.quizzy.protocole.speciales.pupitre.ProtocoleActiverBuzzers;
import fr.hillionj.quizzy.protocole.speciales.pupitre.ProtocoleDesactiverBuzzers;
import fr.hillionj.quizzy.protocole.speciales.pupitre.ProtocoleIndicationQuestion;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public enum TypeProtocole {
    LANCEMENT("L"),
    INSCRIPTION_PARTICIPANT("I"),
    ENVOI_QUESTION("G"),
    DEMARRAGE_QUESTION("T"),
    INDICATION_REPONSE_PARTICIPANT("U"),
    AFFICHER_REPONSE("H"),
    AFFICHER_QUESTION_SUIVANTE("S"),
    AFFICHER_QUESTION_PRECEDENTE("P"),
    FIN_QUIZ("F"),
    INDICATION_QUESTION("Q"),
    ACTIVER_BUZZERS("E"),
    DESACTIVER_BUZZERS("D"),
    RECEPTION_REPONSE("R"),
    ACQUITEMENT("A");

    private String              indiceType;
    private static final String TAG = "_TypeProtocole";

    TypeProtocole(String indiceType)
    {
        this.indiceType = indiceType;
    }
    public static TypeProtocole getType(String indiceType)
    {
        for(TypeProtocole type: values())
        {
            if(type.getIndiceType().equals(indiceType))
            {
                return type;
            }
        }
        return null;
    }

    public String getIndiceType()
    {
        return indiceType;
    }

    public Protocole getProtocole(String trame)
    {
        switch(this)
        {
            case LANCEMENT:
                return new ProtocoleLancement();
            case INSCRIPTION_PARTICIPANT:
                return new ProtocoleInscriptionParticipant(trame);
            case ENVOI_QUESTION:
                return new ProtocoleEnvoiQuestion(trame);
            case DEMARRAGE_QUESTION:
                return new ProtocoleLancementQuestion();
            case INDICATION_REPONSE_PARTICIPANT:
                return new ProtocoleIndicationReponseParticipant(trame);
            case ACQUITEMENT:
                return new ProtocoleAcquitement();
            case FIN_QUIZ:
                return new ProtocoleFinQuiz();
            case ACTIVER_BUZZERS:
                return new ProtocoleActiverBuzzers(trame);
            case AFFICHER_REPONSE:
                return new ProtocoleAfficherReponse();
            case RECEPTION_REPONSE:
                return new ProtocoleReceptionReponse(trame);
            case DESACTIVER_BUZZERS:
                return new ProtocoleDesactiverBuzzers(trame);
            case INDICATION_QUESTION:
                return new ProtocoleIndicationQuestion(trame);
            case AFFICHER_QUESTION_SUIVANTE:
                return new ProtocoleAfficherQuestionSuivante();
            case AFFICHER_QUESTION_PRECEDENTE:
                return new ProtocoleAfficherQuestionPrecedente();
            default:
                Log.d(TAG,
                      "Aucun protocole trouvé dans " + getClass().getName() + " pour la trame " +
                        (trame == null ? "null" : trame));
                break;
        }
        return null;
    }
}
