package fr.hillionj.quizzy.navigation.quiz;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.bluetooth.GestionnaireBluetooth;
import fr.hillionj.quizzy.bluetooth.Peripherique;
import fr.hillionj.quizzy.databinding.FragmentHomeBinding;
import fr.hillionj.quizzy.navigation.parametres.FragmentParametres;
import fr.hillionj.quizzy.questionnaire.Question;
import fr.hillionj.quizzy.questionnaire.Quiz;
import fr.hillionj.quizzy.receveurs.speciales.Ecran;
import fr.hillionj.quizzy.receveurs.speciales.Participant;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class FragmentQuiz extends Fragment
{
    private FragmentHomeBinding binding;
    private Button              btnLancerQuiz, btnAbandonnerQuiz, btnPauseQuiz, btnReinitialiser;
    private TextView            question;
    private final List<TextView> propositions = new ArrayList<>();
    private ListView             liste_participants;
    private ProgressBar          barreProgression;
    private static FragmentQuiz  vueActive                = null;
    private ArrayAdapter<String> adapterListeParticipants = null;

    private static final String TAG = "_FragmentQuiz";
    public static FragmentQuiz  getVueActive()
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

        btnLancerQuiz      = root.findViewById(R.id.btn_lancer);
        btnAbandonnerQuiz  = root.findViewById(R.id.btn_arreter);
        btnPauseQuiz       = root.findViewById(R.id.btn_pause);
        btnReinitialiser   = root.findViewById(R.id.btn_reinitialiser);
        liste_participants = root.findViewById(R.id.liste_participants);
        if(this.adapterListeParticipants == null)
        {
            this.adapterListeParticipants =
              new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        }
        liste_participants.setAdapter(this.adapterListeParticipants);
        Log.d(TAG, "CREATE");
        question         = root.findViewById(R.id.question);
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
                Quiz quiz = Quiz.getQuizEnCours();
                for(Peripherique peripherique:
                    GestionnaireBluetooth.getGestionnaireBluetooth()
                      .getPeripheriquesConnectes())
                {
                    if(peripherique.getNom().startsWith("quizzy-p"))
                    {
                        quiz.ajouterParticipant(FragmentParametres.getParticipant(peripherique));
                    } else if(peripherique.getNom().startsWith("quizzy-e"))
                    {
                        quiz.ajouterEcran(new Ecran(peripherique));
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

        btnPauseQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Quiz.getQuizEnCours().basculerPause();
                mettreAjourEtatBoutons();
            }
        });

        btnReinitialiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Quiz.getQuizEnCours().reinitialiser();
                mettreAjourEtatBoutons();
            }
        });

        return root;
    }

    public void mettreAjoutListeParticipants()
    {
        Quiz quiz = Quiz.getQuizEnCours();
        if(quiz.estTermine())
        {
            adapterListeParticipants.clear();
            return;
        }
        List<Participant> liste = quiz.getParticipants();
        if(adapterListeParticipants.getCount() != quiz.getParticipants().size())
        {
            adapterListeParticipants.clear();
            for(Participant participant: liste)
            {
                adapterListeParticipants.add(participant.getNom());
            }
        }
        for(int i = 0; i < liste.size(); i++)
        {
            Participant participantAssocie = liste.get(i);
            adapterListeParticipants.remove(adapterListeParticipants.getItem(i));
            String affichageParticipant = participantAssocie.getNom() + " : ";
            if(!participantAssocie.estRepondu())
            {
                affichageParticipant += "(en attente)";
            }
            else if(participantAssocie.getNumeroReponse() == 0)
            {
                affichageParticipant += "Aucune réponse";
            }
            else
            {
                affichageParticipant += "Réponse N°" + participantAssocie.getNumeroReponse() +
                                        " (" + participantAssocie.getTempsReponse() + " ms)";
            }
            adapterListeParticipants.insert(affichageParticipant, i);
        }
    }

    public void mettreAjourEtatBoutons()
    {
        if(Quiz.getQuizEnCours().estTermine())
        {
            btnLancerQuiz.setEnabled(true);
            btnPauseQuiz.setEnabled(false);
            btnAbandonnerQuiz.setEnabled(false);
            btnReinitialiser.setEnabled(false);
        }
        else
        {
            btnLancerQuiz.setEnabled(false);
            btnPauseQuiz.setEnabled(!Quiz.getQuizEnCours().estTempsMort());
            btnReinitialiser.setEnabled(!Quiz.getQuizEnCours().estTempsMort());
            btnAbandonnerQuiz.setEnabled(!Quiz.getQuizEnCours().estEnPause());
        }
    }

    public void mettreAjourDeroulement()
    {
        if(Quiz.getQuizEnCours().estTermine())
        {
            question.setText("");
            for(TextView proposition: propositions)
            {
                proposition.setText("");
                proposition.setBackgroundResource(R.drawable.bg_sub_rounded);
            }
        }
        else
        {
            Question questionEnCours = Quiz.getQuizEnCours().getQuestionEnCours();
            question.setText(questionEnCours.getQuestion());
            List<String> propositionsEnCours = questionEnCours.getReponses();
            int          indiceReponse       = questionEnCours.getNumeroBonneReponse() - 1;
            for(int i = 0; i < propositionsEnCours.size(); i++)
            {
                propositions.get(i).setText(propositionsEnCours.get(i));
                if(i == indiceReponse && Quiz.getQuizEnCours().estTempsMort())
                {
                    propositions.get(i).setBackgroundResource(R.drawable.bg_sub_rounded_vert);
                }
                else if(Quiz.getQuizEnCours().getQuestionEnCours().estSelectionnee(i + 1))
                {
                    propositions.get(i).setBackgroundResource(R.drawable.bg_sub_rounded_or);
                }
                else
                {
                    propositions.get(i).setBackgroundResource(R.drawable.bg_sub_rounded);
                }
            }
        }
        mettreAjourBarreDeProgression();
        mettreAjoutListeParticipants();
    }

    public void mettreAjourBarreDeProgression()
    {
        if(Quiz.getQuizEnCours().estTermine())
        {
            barreProgression.getProgressDrawable().setColorFilter(
              Color.GRAY,
              android.graphics.PorterDuff.Mode.SRC_IN);
            barreProgression.setProgress(0);
            return;
        }
        if(Quiz.getQuizEnCours().estTempsMort())
        {
            barreProgression.getProgressDrawable().setColorFilter(
              Color.RED,
              android.graphics.PorterDuff.Mode.SRC_IN);
            return;
        }
        else if(Quiz.getQuizEnCours().estEnPause())
        {
            barreProgression.getProgressDrawable().setColorFilter(
              Color.YELLOW,
              android.graphics.PorterDuff.Mode.SRC_IN);
            return;
        }
        else
        {
            barreProgression.getProgressDrawable().setColorFilter(
              Color.CYAN,
              android.graphics.PorterDuff.Mode.SRC_IN);
        }
        barreProgression.setProgress(getProgression());
    }

    public ProgressBar getBarreProgression()
    {
        return barreProgression;
    }

    private int getProgression()
    {
        long heureDemarrageQuestion = Quiz.getQuizEnCours().getHeureDemarrageQuestion();
        if(heureDemarrageQuestion == 0 && !Quiz.getQuizEnCours().estTempsMort())
        {
            return 0;
        }
        double tempsProgressionSecondes = Quiz.getQuizEnCours().getTempsQuestionEnCours();
        int    pourcentageProgression =
          (int)(tempsProgressionSecondes /
                (double)Quiz.getQuizEnCours().getQuestionEnCours().getTemps() * 100.0);
        return pourcentageProgression;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}