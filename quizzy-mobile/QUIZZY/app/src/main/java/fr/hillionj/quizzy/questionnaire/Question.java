package fr.hillionj.quizzy.questionnaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class Question
{
    private final String question;
    private final List<String> reponses;
    private final int          numeroBonneReponse;
    private final int          temps;
    private List<Integer>      selection = new ArrayList<>();

    public Question(int idQuestion, String question, List<String> reponses, int temps)
    {
        String bonneReponse = reponses.get(0);
        this.reponses       = reponses;
        this.question       = question;
        this.temps          = temps;
        Collections.shuffle(reponses);

        this.numeroBonneReponse = reponses.indexOf(bonneReponse) + 1;
    }

    public int getNumeroBonneReponse()
    {
        return numeroBonneReponse;
    }

    public String getQuestion()
    {
        return question;
    }

    public List<String> getReponses()
    {
        return reponses;
    }

    public int getTemps()
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
