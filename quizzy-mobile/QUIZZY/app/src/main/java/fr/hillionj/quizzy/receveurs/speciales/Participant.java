package fr.hillionj.quizzy.receveurs.speciales;

import fr.hillionj.quizzy.bluetooth.Peripherique;
import fr.hillionj.quizzy.receveurs.ReceveurProtocole;

public class Participant implements ReceveurProtocole {

    private String nom;
    private String pid;
    private Peripherique peripherique;
    private int score = 0;
    private boolean repondu = false;

    public Participant(String nom, Peripherique peripherique) {
        this.nom = nom;
        this.pid = "P" + peripherique.getIndicePeripherique();
        this.peripherique = peripherique;
    }

    public String getNom() {
        return nom;
    }

    @Override
    public Peripherique getPeripherique() {
        return peripherique;
    }

    @Override
    public boolean estUnEcran() {
        return true;
    }

    public String getPID() {
        return pid;
    }

    public boolean estRepondu() {
        return repondu;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setRepondu(boolean repondu) {
        this.repondu = repondu;
    }
}
