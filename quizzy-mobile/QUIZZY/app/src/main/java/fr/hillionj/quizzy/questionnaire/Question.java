package fr.hillionj.quizzy.questionnaire;

import java.util.Collections;
import java.util.List;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class Question
{
    private final String question;
    private final List<String> reponses;
    private final int          numeroBonneReponse;
    private final int          temps;

    public Question(String question, List<String> reponses, int temps)
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
}
