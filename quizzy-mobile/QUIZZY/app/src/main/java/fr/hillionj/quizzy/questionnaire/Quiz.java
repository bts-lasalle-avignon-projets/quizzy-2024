package fr.hillionj.quizzy.questionnaire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class Quiz
{
    private String         theme                  = "Aucun";
    private List<Question> questions              = new ArrayList<>();
    private int            indiceQuestionActuelle = -1;
    private boolean termine = false;

    private static Quiz quizEnCours = new Quiz();

    public static Quiz getQuizEnCours() {
        return quizEnCours;
    }

    public void genererQuiz(String theme, int nombreQuestions)
    {
        // Tests questions:
        questions.add(new Question("Quel est le meilleur OS", Arrays.asList("Minitel", "Windows", "Mac", "Linux"), 15));
        questions.add(new Question("Quel est l'autheur de l'application", Arrays.asList("Jules", "Thomas", "Red", "Tvaira"), 30));
    }

    public List<String> getThemes()
    {
        return null;
    }

    public String getTheme()
    {
        return theme;
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
        questions.clear();
        termine = false;
    }

    public boolean estTermine()
    {
        return termine;
    }
}
