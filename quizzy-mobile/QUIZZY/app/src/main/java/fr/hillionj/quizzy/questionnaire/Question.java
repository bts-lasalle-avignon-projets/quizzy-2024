package fr.hillionj.quizzy.questionnaire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Question {

    private String question;
    private List<String> reponses = new ArrayList<>();

    private String reponseVraie;

    public Question(String question, List<String> reponses) {
        this.reponseVraie = reponses.get(0);
        this.reponses = reponses;
        this.question = question;
    }

    public String getReponseVraie() {
        return reponseVraie;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getReponses() {
        return reponses;
    }
}
