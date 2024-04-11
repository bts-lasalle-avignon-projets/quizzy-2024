package fr.hillionj.quizzy.questionnaire;

import fr.hillionj.quizzy.bluetooth.Peripherique;

public class Participant {

    private String nom;
    private String pid;
    private Peripherique peripherique;

    public Participant(String nom, Peripherique peripherique) {
        this.nom = nom;
        this.pid = "P" + peripherique.getIndicePeripherique();
        this.peripherique = peripherique;
    }

    public String getNom() {
        return nom;
    }

    public Peripherique getPeripherique() {
        return peripherique;
    }

    public String getPID() {
        return pid;
    }
}
