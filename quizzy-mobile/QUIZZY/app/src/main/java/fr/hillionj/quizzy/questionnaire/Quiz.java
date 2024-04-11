package fr.hillionj.quizzy.questionnaire;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.hillionj.quizzy.bluetooth.Peripherique;
import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.protocole.TypeProtocole;
import fr.hillionj.quizzy.protocole.speciales.application.ProtocoleReceptionReponse;
import fr.hillionj.quizzy.protocole.speciales.ecran.ProtocoleAfficherQuestionSuivante;
import fr.hillionj.quizzy.protocole.speciales.pupitre.ProtocoleActiverBuzzers;
import fr.hillionj.quizzy.protocole.speciales.pupitre.ProtocoleDesactiverBuzzers;
import fr.hillionj.quizzy.protocole.speciales.pupitre.ProtocoleIndicationQuestion;
import fr.hillionj.quizzy.receveurs.speciales.Participant;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class Quiz
{
    private String         theme                  = "Aucun";
    private List<Question> questions              = new ArrayList<>();
    private List<Participant> participants = new ArrayList<>();
    private boolean termine = false;
    private int indiceProchaineQuestion = 0;

    private static final String TAG = "_Quiz";
    private static Quiz quizEnCours = new Quiz();

    public static Quiz getQuizEnCours() {
        return quizEnCours;
    }

    public void genererQuiz(String theme, int nombreQuestions)
    {
        // Tests questions:
        questions.add(new Question("Quel est le meilleur OS", Arrays.asList("Minitel", "Windows", "Mac", "Linux"), 15));
        questions.add(new Question("Quel est l'autheur de l'application", Arrays.asList("Jules", "Thomas", "Red", "Tvaira"), 30));
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
        for (Participant participant : participants) {
            participant.setScore(0);
        }
        // TODO envoyer le QUIZ aux écrans
        questionSuivante();
    }

    public void ajouterParticipant(Participant participant) {
        participants.add(participant);
    }

    public void supprimerParticipant(Participant participant) {
        if (participants.contains(participant)) {
            participants.remove(participant);
        }
    }

    public void arreter()
    {
        questions.clear();
        termine = false;
        indiceProchaineQuestion = 0;
    }

    private void renitialiserReponses() {
        for (Participant participant : participants) {
            participant.setRepondu(false);
        }
    }

    public void questionSuivante() {
        if (indiceProchaineQuestion >= questions.size()) {
            arreter();
            return;
        }
        renitialiserReponses();
        envoyerQuestion();
        indiceProchaineQuestion++;
    }

    public void questionPrecedente() {
        renitialiserReponses();
        if (indiceProchaineQuestion > 0) {
            indiceProchaineQuestion--;
        }
        envoyerQuestion();
    }

    public void reponseSaisie(Peripherique peripherique, ProtocoleReceptionReponse receptionReponse) {
        Participant participant = getParticipant(peripherique);
        if (participant == null) {
            return;
        }
        participant.setRepondu(true);

        Log.d(TAG, "Réponse de " + participant.getNom() + " : Réponse N°" + receptionReponse.getNumeroReponse() + " en " + receptionReponse.getTempsReponse() + "ms.");

        ProtocoleDesactiverBuzzers desactiverBuzzers = (ProtocoleDesactiverBuzzers) Protocole.getProtocole(TypeProtocole.DESACTIVER_BUZZERS);
        desactiverBuzzers.genererTrame(indiceProchaineQuestion);
        desactiverBuzzers.envoyer(participant);

        verifierParticipants();
    }

    private void verifierParticipants() {
        for (Participant participant : participants) {
            if (!participant.estRepondu()) {
                return;
            }
        }
        questionSuivante();
    }

    private Participant getParticipant(Peripherique peripherique) {
        for (Participant participant : participants) {
            if (participant.getPeripherique() == peripherique) {
                return participant;
            }
        }
        return null;
    }

    private void envoyerQuestion() {
        ProtocoleIndicationQuestion indicationQuestion = (ProtocoleIndicationQuestion) Protocole.getProtocole(TypeProtocole.INDICATION_QUESTION);
        indicationQuestion.genererTrame(indiceProchaineQuestion + 1, questions.get(indiceProchaineQuestion).getTemps());
        indicationQuestion.envoyer(participants);

        ProtocoleActiverBuzzers activerBuzzers = (ProtocoleActiverBuzzers) Protocole.getProtocole(TypeProtocole.ACTIVER_BUZZERS);
        activerBuzzers.genererTrame(indiceProchaineQuestion + 1);
        activerBuzzers.envoyer(participants);

        ProtocoleAfficherQuestionSuivante afficherQuestionSuivante = (ProtocoleAfficherQuestionSuivante) Protocole.getProtocole(TypeProtocole.AFFICHER_QUESTION_SUIVANTE);
        afficherQuestionSuivante.genererTrame();
        afficherQuestionSuivante.envoyer(new ArrayList<>()); // TODO Faire la liste des écrans d'affichage
    }

    public boolean estTermine()
    {
        return termine;
    }
}
