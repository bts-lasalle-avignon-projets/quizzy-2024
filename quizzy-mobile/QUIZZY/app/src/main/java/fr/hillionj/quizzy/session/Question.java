package fr.hillionj.quizzy.session;

import java.util.List;

public class Question {
    private final int idQuestion, tempsReponse;
    private final String question;
    private final List<String> propositions;
    public Question(int idQuestion, String question, List<String> propositions, int tempsReponse) {
        this.idQuestion = idQuestion;
        this.question = question;
        this.propositions = propositions;
        this.tempsReponse = tempsReponse;
    }

    public String getQuestion() {
        return question;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public int getTempsReponse() {
        return tempsReponse;
    }

    public List<String> getPropositions() {
        return propositions;
    }

    public int getNumeroBonneReponse() {
        return 0;
    }
}
