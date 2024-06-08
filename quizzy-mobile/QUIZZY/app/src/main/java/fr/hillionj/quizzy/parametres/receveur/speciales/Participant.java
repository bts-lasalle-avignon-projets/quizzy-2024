package fr.hillionj.quizzy.parametres.receveur.speciales;

import fr.hillionj.quizzy.communication.bluetooth.Peripherique;
import fr.hillionj.quizzy.parametres.receveur.ReceveurProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "SdCardPath" })
public class Participant extends ReceveurProtocole {

    private int idParticipant;
    private String nom;
    private Peripherique peripherique;

    public Participant(final int idParticipant, final String nom) {
        this.idParticipant = idParticipant;
        this.nom = nom;
    }

    public Participant(final int idParticipant, final String nom, final Peripherique peripherique) {
        this.nom = nom;
        setPeripherique(peripherique);
    }

    public String getNom() {
        return nom;
    }

    public int getIdParticipant() {
        return idParticipant;
    }
}
