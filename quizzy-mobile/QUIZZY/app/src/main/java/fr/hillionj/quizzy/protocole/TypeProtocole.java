package fr.hillionj.quizzy.protocole;

import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleEnvoiQuestion;
import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleIndicationReponseParticipant;
import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleInscriptionParticipant;
import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleLancement;
import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleLancementQuestion;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public enum TypeProtocole {
    LANCEMENT("L"),
    INSCRIPTION_PARTICIPANT("I"),
    ENVOI_QUESTION("Q"),
    DEMARRAGE_QUESTION("T"),
    INDICATION_REPONSE_PARTICIPANT("R"),
    AFFICHER_REPONSE("H"),
    AFFICHER_QUESTION_SUIVANTE("S"),
    AFFICHER_QUESTION_PRECEDENTE("P"),
    FIN_QUIZ("F"),
    INDICATION_QUESTION("G"),
    ACTIVER_BUZZERS("D"),
    DESACTIVER_BUZZERS("E"),
    RECEPTION_REPONSE("R"),
    ACQUITEMENT("A");

    private String indiceType;

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
            default:
                break;
        }
        return null;
    }
}
