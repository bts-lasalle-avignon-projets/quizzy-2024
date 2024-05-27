package fr.hillionj.quizzy.ihm.vues;

public interface QuizzyIHM {

    default void ajouterIHM(QuizzyIHM pageIHM) {
        IHM.getIHM().ajouterIHM(pageIHM);
    }

    default void mettreAjourDeroulement() {

    }
}
