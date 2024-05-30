package fr.hillionj.quizzy.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class Question
{
    private final String question;
    private final List<String> propositions;
    private final int          numeroBonneReponse;
    private final int          temps;
    private List<Integer>      selection = new ArrayList<>();

    public Question(int idQuestion, String question, List<String> propositions, int temps)
    {
        String bonneReponse = propositions.get(0);
        this.propositions       = propositions;
        this.question       = question;
        this.temps          = temps;
        Collections.shuffle(propositions);

        this.numeroBonneReponse = propositions.indexOf(bonneReponse) + 1;
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
        return selection.contains(numeroProposition);
    }

    public void ajouterSelection(int numeroProposition)
    {
        if(!estSelectionnee(numeroProposition))
        {
            selection.add(numeroProposition);
        }
    }

    public List<Integer> getSelection()
    {
        return selection;
    }
}