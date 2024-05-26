package fr.hillionj.quizzy.parametres;

import fr.hillionj.quizzy.communication.Peripherique;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "SdCardPath" })
public class Participant extends ReceveurProtocole {

    private String nom;
    private Peripherique peripherique;

    public Participant(final String nom) {
        this.nom = nom;
    }

    public String getNom() {
        return nom;
    }
}
