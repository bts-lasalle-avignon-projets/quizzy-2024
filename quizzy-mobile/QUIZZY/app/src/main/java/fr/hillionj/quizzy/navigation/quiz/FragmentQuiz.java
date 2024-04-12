package fr.hillionj.quizzy.navigation.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.bluetooth.GestionnaireBluetooth;
import fr.hillionj.quizzy.bluetooth.Peripherique;
import fr.hillionj.quizzy.databinding.FragmentHomeBinding;
import fr.hillionj.quizzy.questionnaire.Quiz;
import fr.hillionj.quizzy.receveurs.speciales.Participant;

public class FragmentQuiz extends Fragment
{
    private FragmentHomeBinding binding;
    private Button              btnLancerQuiz;
    private Button              btnAbandonnerQuiz;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup               container,
                             Bundle                  savedInstanceState)
    {
        ModeleQuiz homeViewModel = new ViewModelProvider(this).get(ModeleQuiz.class);

        binding   = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        btnLancerQuiz     = root.findViewById(R.id.btn_lancer);
        btnAbandonnerQuiz = root.findViewById(R.id.btn_arreter);
        mettreAjourEtatBoutons();

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

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}