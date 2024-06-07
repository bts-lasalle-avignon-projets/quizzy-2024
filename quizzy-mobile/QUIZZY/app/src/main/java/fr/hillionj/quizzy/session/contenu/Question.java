package fr.hillionj.quizzy.session.contenu;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class Question
{
    private final String question;
    private final List<String> propositions;
    private final int          numeroBonneReponse;
    private final int          temps;
    private final int idQuestion;
    private Map<Participant, Integer> selection = new HashMap<>();

    public Question(int idQuestion, String question, @NonNull List<String> propositions, int temps)
    {
        this.idQuestion = idQuestion;
        this.propositions       = propositions;
        this.question       = question;
        this.temps          = temps;
        if (propositions != null) {
            String bonneReponse = propositions.get(0);
            Collections.shuffle(propositions);
            this.numeroBonneReponse = propositions.indexOf(bonneReponse) + 1;
        } else {
            this.numeroBonneReponse = -1;
        }
    }

    public int getNumeroBonneReponse()
    {
        return numeroBonneReponse;
    }

    public String getQuestion()
    {
        return question;
    }

    public List<String> getPropositions()
    {
        return propositions;
    }

    public int getTempsReponse()
    {
        return temps;
    }

    public boolean estSelectionnee(int numeroProposition)
    {
        return selection.containsValue(numeroProposition);
    }

    public boolean estSelectionne(Participant participant) {
        return selection.containsKey(participant) && selection.get(participant) != -1;
    }

    public void ajouterSelection(Participant participant, int numeroProposition)
    {
        selection.put(participant, numeroProposition);
    }

    public boolean estPropositionValide(Participant participant) {
        if (!selection.containsKey(participant)) {
            return false;
        }
        return numeroBonneReponse == -1 || selection.get(participant) == numeroBonneReponse - 1;
    }

    public int getIdQuestion() {
        return idQuestion;
    }
}