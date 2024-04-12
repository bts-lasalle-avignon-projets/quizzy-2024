package fr.hillionj.quizzy.questionnaire;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.hillionj.quizzy.bluetooth.GestionnaireBluetooth;
import fr.hillionj.quizzy.bluetooth.Peripherique;
import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.protocole.TypeProtocole;
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
import fr.hillionj.quizzy.receveurs.speciales.Ecran;
import fr.hillionj.quizzy.receveurs.speciales.Participant;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class Quiz
{
    private String            theme                   = "Aucun";
    private List<Question>    questions               = new ArrayList<>();
    private List<Participant> participants            = new ArrayList<>();
    private List<Ecran>       ecrans                  = new ArrayList<>();
    private boolean           termine                 = true;
    private int               indiceProchaineQuestion = 0;

    private static final String TAG         = "_Quiz";
    private static Quiz         quizEnCours = new Quiz();

    public static Quiz getQuizEnCours()
    {
        return quizEnCours;
    }

    public void genererQuiz(String theme, int nombreQuestions)
    {
        // Tests questions:
        questions.add(new Question("Quel est le meilleur OS",
                                   Arrays.asList("Minitel", "Windows", "Mac", "Linux"),
                                   15));
        questions.add(new Question("Quel est l'autheur de l'application",
                                   Arrays.asList("Jules", "Thomas", "Red", "Tvaira"),
                                   30));
    }

    public List<String> getThemes()
    {
        return null;
    }

    public String getTheme()
    {
        return theme;
    }

    public List<Question> getQuestions()
    {
        return questions;
    }

    public void demarrer()
    {
        termine = false;
        ecrans.clear();
        for(Peripherique peripherique:
            GestionnaireBluetooth.getGestionnaireBluetooth(null, null).getPeripheriquesConnectes())
        {
            if(peripherique.getNom().startsWith("quizzy-e"))
            {
                ecrans.add(new Ecran(peripherique));
            }
        }
        // Test Ecran
        ecrans.add(new Ecran(null));
        // Fin Test Ecran

        for(Participant participant: participants)
        {
            participant.setScore(0);
            ProtocoleInscriptionParticipant inscriptionParticipant =
              (ProtocoleInscriptionParticipant)Protocole.getProtocole(
                TypeProtocole.INSCRIPTION_PARTICIPANT);
            inscriptionParticipant.genererTrame(participant.getPID(), participant.getNom());
            inscriptionParticipant.envoyer(ecrans);
        }
        for(Question question: questions)
        {
            ProtocoleEnvoiQuestion envoiQuestion =
              (ProtocoleEnvoiQuestion)Protocole.getProtocole(TypeProtocole.ENVOI_QUESTION);
            envoiQuestion.genererTrame(question);
            envoiQuestion.envoyer(ecrans);
        }

        ProtocoleLancement lancement =
          (ProtocoleLancement)Protocole.getProtocole(TypeProtocole.LANCEMENT);
        lancement.genererTrame();
        lancement.envoyer(ecrans);

        envoyerQuestionSuivante();
    }

    public void ajouterParticipant(Participant participant)
    {
        participants.add(participant);
    }

    public void supprimerParticipant(Participant participant)
    {
        if(participants.contains(participant))
        {
            participants.remove(participant);
        }
    }

    public void arreter()
    {
        ProtocoleFinQuiz finQuiz = (ProtocoleFinQuiz)Protocole.getProtocole(TypeProtocole.FIN_QUIZ);
        finQuiz.genererTrame();
        finQuiz.envoyer(ecrans);

        ProtocoleDesactiverBuzzers desactiverBuzzers =
          (ProtocoleDesactiverBuzzers)Protocole.getProtocole(TypeProtocole.DESACTIVER_BUZZERS);
        desactiverBuzzers.genererTrame(indiceProchaineQuestion);
        desactiverBuzzers.envoyer(participants);

        participants.clear();
        questions.clear();
        termine                 = true;
        indiceProchaineQuestion = 0;
    }

    private void renitialiserReponses()
    {
        for(Participant participant: participants)
        {
            participant.setRepondu(false);
        }
    }

    public void envoyerQuestionSuivante()
    {
        if(indiceProchaineQuestion >= questions.size())
        {
            arreter();
            return;
        }
        renitialiserReponses();

        ProtocoleAfficherQuestionSuivante afficherQuestionSuivante =
          (ProtocoleAfficherQuestionSuivante)Protocole.getProtocole(
            TypeProtocole.AFFICHER_QUESTION_SUIVANTE);
        afficherQuestionSuivante.genererTrame();
        afficherQuestionSuivante.envoyer(ecrans);

        envoyerQuestion();

        ProtocoleLancementQuestion lancementQuestion =
          (ProtocoleLancementQuestion)Protocole.getProtocole(TypeProtocole.DEMARRAGE_QUESTION);
        lancementQuestion.genererTrame();
        lancementQuestion.envoyer(ecrans);

        indiceProchaineQuestion++;
    }

    public void envoyerQuestionPrecedente()
    {
        renitialiserReponses();
        if(indiceProchaineQuestion > 0)
        {
            indiceProchaineQuestion--;
        }
        envoyerQuestion();
        ProtocoleAfficherQuestionPrecedente afficherQuestionPrecedente =
          (ProtocoleAfficherQuestionPrecedente)Protocole.getProtocole(
            TypeProtocole.AFFICHER_QUESTION_PRECEDENTE);
        afficherQuestionPrecedente.genererTrame();
        afficherQuestionPrecedente.envoyer(ecrans);
    }

    public void recupererReponseSaisie(Peripherique peripherique, ProtocoleReceptionReponse receptionReponse)
    {
        Participant participant = getParticipant(peripherique);
        if(participant == null)
        {
            return;
        }
        participant.setRepondu(true);

        Log.d(TAG,
              "Réponse de " + participant.getNom() + " : Réponse N°" +
                receptionReponse.getNumeroReponse() + " en " + receptionReponse.getTempsReponse() +
                "ms.");

        ProtocoleDesactiverBuzzers desactiverBuzzers =
          (ProtocoleDesactiverBuzzers)Protocole.getProtocole(TypeProtocole.DESACTIVER_BUZZERS);
        desactiverBuzzers.genererTrame(indiceProchaineQuestion);
        desactiverBuzzers.envoyer(participant);

        ProtocoleIndicationReponseParticipant indicationReponseParticipant =
          (ProtocoleIndicationReponseParticipant)Protocole.getProtocole(
            TypeProtocole.INDICATION_REPONSE_PARTICIPANT);
        indicationReponseParticipant.genererTrame(participant.getPID(),
                                                  receptionReponse.getNumeroReponse(),
                                                  receptionReponse.getTempsReponse());
        indicationReponseParticipant.envoyer(ecrans);

        verifierParticipants();
    }

    private void verifierParticipants()
    {
        for(Participant participant: participants)
        {
            if(!participant.estRepondu())
            {
                return;
            }
        }

        ProtocoleAfficherReponse afficherReponse =
          (ProtocoleAfficherReponse)Protocole.getProtocole(TypeProtocole.AFFICHER_REPONSE);
        afficherReponse.genererTrame();
        afficherReponse.envoyer(ecrans);

        envoyerQuestionSuivante();
    }

    private Participant getParticipant(Peripherique peripherique)
    {
        for(Participant participant: participants)
        {
            if(participant.getPeripherique() == peripherique)
            {
                return participant;
            }
        }
        return null;
    }

    private void envoyerQuestion()
    {
        ProtocoleIndicationQuestion indicationQuestion =
          (ProtocoleIndicationQuestion)Protocole.getProtocole(TypeProtocole.INDICATION_QUESTION);
        indicationQuestion.genererTrame(indiceProchaineQuestion + 1,
                                        questions.get(indiceProchaineQuestion).getTemps());
        indicationQuestion.envoyer(participants);

        ProtocoleActiverBuzzers activerBuzzers =
          (ProtocoleActiverBuzzers)Protocole.getProtocole(TypeProtocole.ACTIVER_BUZZERS);
        activerBuzzers.genererTrame(indiceProchaineQuestion + 1);
        activerBuzzers.envoyer(participants);
    }

    public boolean estTermine()
    {
        return termine;
    }
}
