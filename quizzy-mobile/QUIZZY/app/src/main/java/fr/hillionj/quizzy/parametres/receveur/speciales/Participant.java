package fr.hillionj.quizzy.parametres.receveur.speciales;

import fr.hillionj.quizzy.communication.bluetooth.Peripherique;
import fr.hillionj.quizzy.parametres.receveur.ReceveurProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "SdCardPath" })
public class Participant extends ReceveurProtocole {

    private String nom;
    private Peripherique peripherique;

    public Participant(final String nom) {
        this.nom = nom;
    }

    public Participant(final String nom, final Peripherique peripherique) {
        this.nom = nom;
        setPeripherique(peripherique);
    }

    public String getNom() {
        return nom;
    }
}
