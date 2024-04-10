package fr.hillionj.quizzy.questionnaire;

import java.util.List;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class Question
{
    private final String question;
    private final List<String> reponses;
    private final String       bonneReponse;

    public Question(String question, List<String> reponses)
    {
        this.bonneReponse = reponses.get(0);
        this.reponses     = reponses;
        this.question     = question;
    }

    public String getBonneReponse()
    {
        return bonneReponse;
    }

    public String getQuestion()
    {
        return question;
    }

    public List<String> getReponses()
    {
        return reponses;
    }
}
