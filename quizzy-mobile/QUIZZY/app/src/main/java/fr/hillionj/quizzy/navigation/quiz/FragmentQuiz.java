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
import fr.hillionj.quizzy.databinding.FragmentHomeBinding;
import fr.hillionj.quizzy.questionnaire.EtapeQuiz;
import fr.hillionj.quizzy.questionnaire.Question;
import fr.hillionj.quizzy.questionnaire.Quiz;
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

    @NonNull
    private View initialiserVue(@NonNull LayoutInflater inflater, ViewGroup container)
    {
        ModeleQuiz homeViewModel = new ViewModelProvider(this).get(ModeleQuiz.class);

        binding   = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vueActive = this;

        initialiserBoutons(root);
        initialiserListeParticipants(root);
        initialiserAffichage(root);
        return root;
    }

    private void assicierBoutonsEtFonctionalites()
    {
        associerBoutonDemarrer();
        associerBoutonAbandonner();
        associerBoutonPause();
        associerBoutonReinitialiser();
    }

    private void associerBoutonReinitialiser()
    {
        btnReinitialiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Quiz.getQuizEnCours().reinitialiser();
                mettreAjourEtatBoutons();
            }
        });
    }

    private void associerBoutonPause()
    {
        btnPauseQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Quiz.getQuizEnCours().basculerPause();
                mettreAjourEtatBoutons();
            }
        });
    }

    private void associerBoutonAbandonner()
    {
        btnAbandonnerQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Quiz.getQuizEnCours().arreter();
                mettreAjourEtatBoutons();
            }
        });
    }

    private void associerBoutonDemarrer()
    {
        btnLancerQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Quiz quiz = Quiz.getQuizEnCours();
                quiz.genererQuiz();
                quiz.demarrer();
                mettreAjourEtatBoutons();
            }
        });
    }

    private void initialiserBoutons(View root)
    {
        btnLancerQuiz     = root.findViewById(R.id.btn_lancer);
        btnAbandonnerQuiz = root.findViewById(R.id.btn_arreter);
        btnPauseQuiz      = root.findViewById(R.id.btn_pause);
        btnReinitialiser  = root.findViewById(R.id.btn_reinitialiser);
    }

    private void initialiserListeParticipants(View root)
    {
        liste_participants = root.findViewById(R.id.liste_participants);
        if(this.adapterListeParticipants == null)
        {
            this.adapterListeParticipants =
              new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        }
    }

    private void initialiserAffichage(View root)
    {
        liste_participants.setAdapter(this.adapterListeParticipants);
        question         = root.findViewById(R.id.question);
        barreProgression = root.findViewById(R.id.barreProgression);
        barreProgression.setMax(100);
        propositions.clear();
        propositions.add(root.findViewById(R.id.reponse1));
        propositions.add(root.findViewById(R.id.reponse2));
        propositions.add(root.findViewById(R.id.reponse3));
        propositions.add(root.findViewById(R.id.reponse4));
    }

    private void ajouterSuffixeAuxParticipants(List<Participant> liste)
    {
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

    private void verifierTailleListeParticipants(Quiz quiz, List<Participant> liste)
    {
        if(adapterListeParticipants.getCount() != quiz.getParticipants().size())
        {
            adapterListeParticipants.clear();
            for(Participant participant: liste)
            {
                adapterListeParticipants.add(participant.getNom());
            }
        }
    }

    private void effacerQuestionEtPropositions()
    {
        question.setText("");
        for(TextView proposition: propositions)
        {
            proposition.setText("");
            proposition.setBackgroundResource(R.drawable.bg_sub_rounded);
        }
    }

    private void mettreAjourPropositions(Question questionEnCours)
    {
        List<String> propositionsEnCours = questionEnCours.getReponses();
        int          indiceReponse       = questionEnCours.getNumeroBonneReponse() - 1;
        for(int i = 0; i < propositionsEnCours.size(); i++)
        {
            propositions.get(i).setText(propositionsEnCours.get(i));
            if(i == indiceReponse && Quiz.getQuizEnCours().estTempsMort() &&
               Quiz.getQuizEnCours().getEtape() == EtapeQuiz.AFFICHAGE_QUESTION_SUIVANTE)
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

    private void setCouleur(int color)
    {
        barreProgression.getProgressDrawable().setColorFilter(
          color,
          android.graphics.PorterDuff.Mode.SRC_IN);
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

    public static FragmentQuiz getVueActive()
    {
        return vueActive;
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
        verifierTailleListeParticipants(quiz, liste);
        ajouterSuffixeAuxParticipants(liste);
    }

    public void mettreAjourEtatBoutons()
    {
        Log.d(TAG, "estTempsMort : " + Quiz.getQuizEnCours().estTempsMort());
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
            effacerQuestionEtPropositions();
        }
        else
        {
            Question questionEnCours = Quiz.getQuizEnCours().getQuestionEnCours();
            question.setText(questionEnCours.getQuestion());
            mettreAjourPropositions(questionEnCours);
        }
        mettreAjourBarreDeProgression();
        mettreAjoutListeParticipants();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup               container,
                             Bundle                  savedInstanceState)
    {
        View root = initialiserVue(inflater, container);
        Log.d(TAG, "CREATE");
        mettreAjourEtatBoutons();
        mettreAjourDeroulement();

        assicierBoutonsEtFonctionalites();
        return root;
    }

    public void mettreAjourBarreDeProgression()
    {
        if(Quiz.getQuizEnCours().estTermine())
        {
            setCouleur(Color.GRAY);
            barreProgression.setProgress(0);
        }
        else if(Quiz.getQuizEnCours().estTempsMort())
        {
            setCouleur(Color.RED);
        }
        else if(Quiz.getQuizEnCours().estEnPause())
        {
            setCouleur(Color.YELLOW);
        }
        else
        {
            setCouleur(Color.CYAN);
            barreProgression.setProgress(getProgression());
        }
    }

    public ProgressBar getBarreProgression()
    {
        return barreProgression;
    }
}