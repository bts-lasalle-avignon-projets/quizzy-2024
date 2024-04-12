package fr.hillionj.quizzy.navigation.quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.bluetooth.GestionnaireBluetooth;
import fr.hillionj.quizzy.bluetooth.Peripherique;
import fr.hillionj.quizzy.databinding.FragmentHomeBinding;
import fr.hillionj.quizzy.navigation.pupitres.FragmentPupitre;
import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.questionnaire.Question;
import fr.hillionj.quizzy.questionnaire.Quiz;
import fr.hillionj.quizzy.receveurs.speciales.Participant;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class FragmentQuiz extends Fragment
{
    private FragmentHomeBinding binding;
    private Button              btnLancerQuiz, btnAbandonnerQuiz;
    private TextView question;
    private final List<TextView> propositions = new ArrayList<>();
    private ProgressBar barreProgression;
    private static FragmentQuiz vueActive = null;

    private static final String TAG = "_FragmentQuiz";
    public static FragmentQuiz    getVueActive()
    {
        return vueActive;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup               container,
                             Bundle                  savedInstanceState)
    {
        ModeleQuiz homeViewModel = new ViewModelProvider(this).get(ModeleQuiz.class);

        binding   = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vueActive = this;

        btnLancerQuiz     = root.findViewById(R.id.btn_lancer);
        btnAbandonnerQuiz = root.findViewById(R.id.btn_arreter);
        Log.d(TAG, "CREATE");
        question = root.findViewById(R.id.question);
        barreProgression = root.findViewById(R.id.barreProgression);
        barreProgression.setMax(100);
        propositions.clear();
        propositions.add(root.findViewById(R.id.reponse1));
        propositions.add(root.findViewById(R.id.reponse2));
        propositions.add(root.findViewById(R.id.reponse3));
        propositions.add(root.findViewById(R.id.reponse4));
        mettreAjourEtatBoutons();
        mettreAjourDeroulement();

        btnLancerQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                // Test QUIZ
                Quiz quiz = Quiz.getQuizEnCours();
                for(Peripherique peripherique:
                    GestionnaireBluetooth.getGestionnaireBluetooth(null, null)
                      .getPeripheriquesConnectes())
                {
                    if(peripherique.getNom().startsWith("quizzy-p"))
                    {
                        quiz.ajouterParticipant(
                          new Participant(peripherique.getNom(), peripherique));
                    }
                }
                quiz.genererQuiz(null, 0);
                quiz.demarrer();
                mettreAjourEtatBoutons();
            }
        });

        btnAbandonnerQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Quiz.getQuizEnCours().arreter();
                mettreAjourEtatBoutons();
            }
        });

        return root;
    }

    public void mettreAjourEtatBoutons()
    {
        if(Quiz.getQuizEnCours().estTermine())
        {
            btnLancerQuiz.setEnabled(true);
            btnAbandonnerQuiz.setEnabled(false);
        }
        else
        {
            btnLancerQuiz.setEnabled(false);
            btnAbandonnerQuiz.setEnabled(true);
        }
    }

    public void mettreAjourDeroulement() {
        if (Quiz.getQuizEnCours().estTermine()) {
            question.setText("");
            for (TextView proposition : propositions) {
                proposition.setText("");
            }
        } else {
            Question questionEnCours = Quiz.getQuizEnCours().getQuestionEnCours();
            question.setText(questionEnCours.getQuestion());
            List<String> propositionsEnCours = questionEnCours.getReponses();
            int indiceReponse = questionEnCours.getNumeroBonneReponse() - 1;
            for (int i = 0; i < propositionsEnCours.size(); i++) {
                propositions.get(i).setText(propositionsEnCours.get(i));
                if (i == indiceReponse && Quiz.getQuizEnCours().getTempsQuestionEnCours() >= Quiz.getQuizEnCours().getQuestionEnCours().getTemps()) {
                    propositions.get(i).setBackgroundColor(Color.argb(50, 0, 255, 0));
                } else {
                    propositions.get(i).setBackgroundResource(R.drawable.bg_sub_rounded);
                }
            }
        }
        mettreAjourBarreDeProgression();
    }

    public void mettreAjourBarreDeProgression() {
        if (Quiz.getQuizEnCours().estTermine()) {
            barreProgression.setProgress(0);
            return;
        }
        long heureDemarrageQuestion = Quiz.getQuizEnCours().getHeureDemarrageQuestion();
        if (heureDemarrageQuestion == 0) {
            barreProgression.setProgress(0);
            return;
        }
        double tempsProgressionSecondes = Quiz.getQuizEnCours().getTempsQuestionEnCours();
        int pourcentageProgression = (int) (tempsProgressionSecondes / (double) Quiz.getQuizEnCours().getQuestionEnCours().getTemps() * 100.0);
        barreProgression.setProgress(pourcentageProgression);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}