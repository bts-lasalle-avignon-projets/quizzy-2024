package fr.hillionj.quizzy.questionnaire;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private List<Question> questions = new ArrayList<>();
    private int indiceQuestionActuelle = -1;
    private String theme = "Aucun";
    public String getTheme() {
        return theme;
    }
    public List<Question> getQuestions() {
        return questions;
    }

    public boolean genererQuiz(String theme, int nombreQuestion) {
        // TODO Partie Base de données
        return false;
    }

    public List<String> getThemes() {
        // TODO Partie Base de données
        return null;
    }

    public void demarrer() {
        
    }

    public void arreter() {

    }

    public Question questionSuivante() {
        return null;
    }

    public boolean estTermine() {
        return false;
    }
}
