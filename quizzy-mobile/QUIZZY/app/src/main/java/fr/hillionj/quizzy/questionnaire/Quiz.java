package fr.hillionj.quizzy.questionnaire;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fr.hillionj.quizzy.bluetooth.GestionnaireBluetooth;
import fr.hillionj.quizzy.bluetooth.Peripherique;
import fr.hillionj.quizzy.navigation.quiz.FragmentQuiz;
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
    private boolean           pause                 = false;
    private int               indiceQuestion = 0;
    private long heureDemarrageTempsMort = 0;
    private long heureDemarrageTempsPause = 0;
    private long totalTempsPause = 0;

    public static final long tempsEntreQuestion = 5000;
    private static final String TAG         = "_Quiz";
    private static final Quiz         quizEnCours = new Quiz();
    private long heureDemarrageQuestion = 0;

    public Quiz() {
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    FragmentQuiz.getVueActive().getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            FragmentQuiz.getVueActive().mettreAjourBarreDeProgression();
                        }
                    });
                    if (!estTermine() && getTempsQuestionEnCours() >= getQuestionEnCours().getTemps() && !estTempsMort() && !estEnPause()) {
                        afficherReponse();
                        FragmentQuiz.getVueActive().getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                FragmentQuiz.getVueActive().mettreAjourDeroulement();
                            }
                        });
                    }
                    if (estTempsMort() && System.currentTimeMillis() - heureDemarrageTempsMort > tempsEntreQuestion) {
                        heureDemarrageTempsMort = 0;
                        FragmentQuiz.getVueActive().getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                envoyerQuestionSuivante();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

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

    public void arreter()
    {
        ProtocoleFinQuiz finQuiz = (ProtocoleFinQuiz)Protocole.getProtocole(TypeProtocole.FIN_QUIZ);
        finQuiz.genererTrame();
        finQuiz.envoyer(ecrans);

        ProtocoleDesactiverBuzzers desactiverBuzzers =
          (ProtocoleDesactiverBuzzers)Protocole.getProtocole(TypeProtocole.DESACTIVER_BUZZERS);
        desactiverBuzzers.genererTrame(indiceQuestion);
        desactiverBuzzers.envoyer(participants);

        participants.clear();
        questions.clear();
        termine                 = true;
        indiceQuestion = 0;

        FragmentQuiz.getVueActive().mettreAjourDeroulement();
        FragmentQuiz.getVueActive().mettreAjourEtatBoutons();
    }

    private void renitialiserReponses()
    {
        for(Participant participant: participants)
        {
            participant.setRepondu(false, 0, 0);
        }
    }

    public void envoyerQuestionSuivante()
    {
        heureDemarrageTempsMort = 0;
        FragmentQuiz.getVueActive().mettreAjourEtatBoutons();
        heureDemarrageQuestion = 0;
        totalTempsPause = 0;
        if(indiceQuestion >= questions.size())
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

        indiceQuestion++;
        heureDemarrageQuestion = System.currentTimeMillis();
        FragmentQuiz.getVueActive().mettreAjourDeroulement();
    }

    public void envoyerQuestionPrecedente()
    {
        heureDemarrageTempsMort = 0;
        FragmentQuiz.getVueActive().mettreAjourEtatBoutons();
        heureDemarrageQuestion = 0;
        totalTempsPause = 0;
        renitialiserReponses();
        if(indiceQuestion > 0)
        {
            indiceQuestion--;
        }
        envoyerQuestion();

        ProtocoleAfficherQuestionPrecedente afficherQuestionPrecedente =
          (ProtocoleAfficherQuestionPrecedente)Protocole.getProtocole(
            TypeProtocole.AFFICHER_QUESTION_PRECEDENTE);
        afficherQuestionPrecedente.genererTrame();
        afficherQuestionPrecedente.envoyer(ecrans);

        heureDemarrageQuestion = System.currentTimeMillis();
        FragmentQuiz.getVueActive().mettreAjourDeroulement();
    }

    public void recupererReponseSaisie(Peripherique peripherique, ProtocoleReceptionReponse receptionReponse)
    {
        Participant participant = getParticipant(peripherique);
        if(participant == null)
        {
            return;
        }
        long tempsReponse = receptionReponse.getTempsReponse() + totalTempsPause;
        participant.setRepondu(true, receptionReponse.getNumeroReponse(), tempsReponse);
        getQuestionEnCours().ajouterSelection(receptionReponse.getNumeroReponse());

        Log.d(TAG,
              "Réponse de " + participant.getNom() + " : Réponse N°" +
                receptionReponse.getNumeroReponse() + " en " + tempsReponse +
                "ms.");

        ProtocoleDesactiverBuzzers desactiverBuzzers =
          (ProtocoleDesactiverBuzzers)Protocole.getProtocole(TypeProtocole.DESACTIVER_BUZZERS);
        desactiverBuzzers.genererTrame(indiceQuestion);
        desactiverBuzzers.envoyer(participant);

        ProtocoleIndicationReponseParticipant indicationReponseParticipant =
          (ProtocoleIndicationReponseParticipant)Protocole.getProtocole(
            TypeProtocole.INDICATION_REPONSE_PARTICIPANT);
        indicationReponseParticipant.genererTrame(participant.getPID(),
                                                  receptionReponse.getNumeroReponse(),
                tempsReponse);
        indicationReponseParticipant.envoyer(ecrans);

        FragmentQuiz.getVueActive().mettreAjourDeroulement();

        verifierParticipants();
    }

    private void verifierParticipants()
    {
        if (participants.isEmpty()) {
            return;
        }
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

        if (!estEnPause()) {
            afficherReponse();
        }
        FragmentQuiz.getVueActive().mettreAjourDeroulement();
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
        indicationQuestion.genererTrame(indiceQuestion + 1,
                                        questions.get(indiceQuestion).getTemps());
        indicationQuestion.envoyer(participants);

        ProtocoleActiverBuzzers activerBuzzers =
          (ProtocoleActiverBuzzers)Protocole.getProtocole(TypeProtocole.ACTIVER_BUZZERS);
        activerBuzzers.genererTrame(indiceQuestion + 1);
        activerBuzzers.envoyer(participants);
    }

    public boolean estTermine()
    {
        return termine;
    }

    public Question getQuestionEnCours() {
        return questions.get(indiceQuestion - 1);
    }

    public long getHeureDemarrageQuestion() {
        return heureDemarrageQuestion;
    }

    public double getTempsQuestionEnCours() {
        if (heureDemarrageQuestion == 0) {
            return -1;
        }
        long tempActuelle = System.currentTimeMillis();
        long difference = tempActuelle - heureDemarrageQuestion;
        return (double) difference / 1000.0;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public boolean estTempsMort() {
        return heureDemarrageTempsMort > 0;
    }

    public long getDifferenceTempsMort() {
        return System.currentTimeMillis() - heureDemarrageTempsMort;
    }

    public void afficherReponse() {
        heureDemarrageTempsMort = System.currentTimeMillis();
        FragmentQuiz.getVueActive().mettreAjourEtatBoutons();
    }

    public void basculerPause() {
        pause = !pause;
        if (estEnPause()) {
            heureDemarrageTempsPause = System.currentTimeMillis();

            ProtocoleDesactiverBuzzers activerBuzzers = (ProtocoleDesactiverBuzzers)Protocole.getProtocole(TypeProtocole.DESACTIVER_BUZZERS);
            activerBuzzers.genererTrame(indiceQuestion);
            activerBuzzers.envoyer(participants);
        } else {
            long tempsPause = System.currentTimeMillis() - heureDemarrageTempsPause;
            totalTempsPause = System.currentTimeMillis() - heureDemarrageQuestion - tempsPause;
            heureDemarrageQuestion += tempsPause;
            heureDemarrageTempsPause = 0;
            verifierParticipants();

            ProtocoleActiverBuzzers activerBuzzers = (ProtocoleActiverBuzzers)Protocole.getProtocole(TypeProtocole.ACTIVER_BUZZERS);
            activerBuzzers.genererTrame(indiceQuestion);
            activerBuzzers.envoyer(participants);
        }
    }

    public boolean estEnPause() {
        return pause;
    }
}
