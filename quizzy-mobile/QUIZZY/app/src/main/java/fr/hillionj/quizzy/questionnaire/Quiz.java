package fr.hillionj.quizzy.questionnaire;

import java.util.ArrayList;
import java.util.List;

public class Quiz
{
    private String         theme                  = "Aucun";
    private List<Question> questions              = new ArrayList<>();
    private int            indiceQuestionActuelle = -1;

    Quiz()
    {
    }

    public boolean genererQuiz(String theme, int nombreQuestions)
    {
        return false;
    }

    public String getTheme()
    {
        return theme;
    }

    public List<String> getThemes()
    {
        return null;
    }

    public List<Question> getQuestions()
    {
        return questions;
    }

    public void demarrer()
    {
    }

    public void arreter()
    {
    }

    public boolean estTermine()
    {
        return false;
    }
}
