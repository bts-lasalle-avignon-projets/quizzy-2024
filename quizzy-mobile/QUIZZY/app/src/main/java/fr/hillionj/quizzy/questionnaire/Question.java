package fr.hillionj.quizzy.questionnaire;

import java.util.ArrayList;
import java.util.List;

public class Question
{
    private String       question;
    private List<String> reponses = new ArrayList<>();
    private String       bonneReponse;

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
