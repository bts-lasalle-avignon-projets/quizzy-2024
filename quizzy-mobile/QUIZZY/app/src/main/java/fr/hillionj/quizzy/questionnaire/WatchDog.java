package fr.hillionj.quizzy.questionnaire;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import fr.hillionj.quizzy.navigation.quiz.FragmentQuiz;

@SuppressWarnings({ "SpellCheckingInspection", "unused"})
public class WatchDog {

    private final Quiz quiz;

    private static final String TAG = "_WatchDog";

    public WatchDog(Quiz quiz) {
        this.quiz = quiz;

        Executors.newScheduledThreadPool(1).scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    verifierQuiz();
                }
                catch(Exception e)
                {
                    Log.e(TAG, e.getMessage(), e);
                }
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

    public void verifierQuiz() {
        if (FragmentQuiz.getVueActive() == null || FragmentQuiz.getVueActive().getActivity() == null) {
            return;
        }
        mettreAjourBarreDeProgression();
        verifierTempsMortApresReponse();
        verifierTempsMortApresQuestion();
    }

    private void verifierTempsMortApresReponse() {
        Question questionEnCours = quiz.getQuestionEnCours();
        if(questionEnCours != null && quiz.getEtape() == EtapeQuiz.ATTENTE  && quiz.getTempsQuestionEnCours() >= questionEnCours.getTemps() && !quiz.estTempsMort() && !quiz.estEnPause())
        {
            quiz.setEtape(EtapeQuiz.AFFICHAGE_REPONSE);
            quiz.demarrerTempsMort();
            afficherSelection();
        }
    }

    private void verifierTempsMortApresQuestion() {
        if(quiz.estTempsMort() && System.currentTimeMillis() - quiz.heureDemarrageTempsMort > Quiz.tempsEntreQuestion)
        {
            if (quiz.getEtape() == EtapeQuiz.AFFICHAGE_REPONSE) {
                quiz.setEtape(EtapeQuiz.AFFICHAGE_QUESTION_SUIVANTE);
                quiz.demarrerTempsMort();
                afficherReponse();
            } else {
                quiz.heureDemarrageTempsMort = 0;
                envoyerQuestionSuivante();
            }
        }
    }

    private void envoyerQuestionSuivante() {
        FragmentQuiz.getVueActive().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                quiz.envoyerQuestionSuivante();
            }
        });
    }

    private void mettreAjourBarreDeProgression() {
        FragmentQuiz.getVueActive().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                FragmentQuiz.getVueActive().mettreAjourBarreDeProgression();
            }
        });
    }

    private void afficherReponse() {
        FragmentQuiz.getVueActive().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                quiz.afficherReponse();
                FragmentQuiz.getVueActive().mettreAjourDeroulement();
            }
        });
    }

    private void afficherSelection() {
        FragmentQuiz.getVueActive().getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                FragmentQuiz.getVueActive().mettreAjourDeroulement();
                FragmentQuiz.getVueActive().mettreAjourEtatBoutons();
            }
        });
    }
}
