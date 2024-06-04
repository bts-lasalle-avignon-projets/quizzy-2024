package fr.hillionj.quizzy.communication.protocoles;

import android.util.Log;

import fr.hillionj.quizzy.communication.protocoles.speciales.VerifierConnexion;
import fr.hillionj.quizzy.communication.protocoles.speciales.application.Acquitement;
import fr.hillionj.quizzy.communication.protocoles.speciales.application.ReceptionReponse;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.AfficherQuestionPrecedente;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.AfficherQuestionSuivante;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.AfficherReponse;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.EnregistrerQuestion;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.FinSession;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.EnregistrerSelectionParticipant;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.EnregistrerParticipant;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.LancementSession;
import fr.hillionj.quizzy.communication.protocoles.speciales.ecran.DemarrerChrono;
import fr.hillionj.quizzy.communication.protocoles.speciales.pupitre.ActiverBumpers;
import fr.hillionj.quizzy.communication.protocoles.speciales.pupitre.DesactiverBumpers;
import fr.hillionj.quizzy.communication.protocoles.speciales.pupitre.EnregistrerTempsQuestion;
import fr.hillionj.quizzy.communication.protocoles.speciales.pupitre.EnregistrerResultat;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public enum TypeProtocole {
    TEST_CONNEXION("0"),
    LANCEMENT_SESSION("L"),
    ENREGISTRER_PARTICIPANT("I"),
    ENREGISTRER_QUESTION("G"),
    DEMARRER_CHRONO("T"),
    ENREGISTRER_SELECTION_PARTICIPANT("U"),
    AFFICHER_REPONSE("H"),
    AFFICHER_QUESTION_SUIVANTE("S"),
    AFFICHER_QUESTION_PRECEDENTE("P"),
    FIN_SESSION("F"),
    ENREGISTRER_TEMPS_QUESTION("Q"),
    ACTIVER_BUMPERS("E"),
    DESACTIVER_BUMPERS("D"),
    RECEPTION_REPONSE("R"),
    ACQUITEMENT("A"),
    ENREGISTRER_RESULTAT("B");

    private String              indiceType;

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
            case LANCEMENT_SESSION:
                return new LancementSession();
            case ENREGISTRER_PARTICIPANT:
                return new EnregistrerParticipant(trame);
            case ENREGISTRER_QUESTION:
                return new EnregistrerQuestion(trame);
            case DEMARRER_CHRONO:
                return new DemarrerChrono();
            case ENREGISTRER_SELECTION_PARTICIPANT:
                return new EnregistrerSelectionParticipant(trame);
            case ACQUITEMENT:
                return new Acquitement();
            case FIN_SESSION:
                return new FinSession();
            case ACTIVER_BUMPERS:
                return new ActiverBumpers(trame);
            case AFFICHER_REPONSE:
                return new AfficherReponse();
            case RECEPTION_REPONSE:
                return new ReceptionReponse(trame);
            case DESACTIVER_BUMPERS:
                return new DesactiverBumpers(trame);
            case ENREGISTRER_TEMPS_QUESTION:
                return new EnregistrerTempsQuestion(trame);
            case AFFICHER_QUESTION_SUIVANTE:
                return new AfficherQuestionSuivante();
            case AFFICHER_QUESTION_PRECEDENTE:
                return new AfficherQuestionPrecedente();
            case ENREGISTRER_RESULTAT:
                return new EnregistrerResultat(trame);
            case TEST_CONNEXION:
                return new VerifierConnexion();
            default:
                Log.d("QUIZZY_" + this.getClass().getName(),
                      "Aucun protocole trouvé dans " + getClass().getName() + " pour la trame " +
                        (trame == null ? "null" : trame));
                break;
        }
        return null;
    }
}
