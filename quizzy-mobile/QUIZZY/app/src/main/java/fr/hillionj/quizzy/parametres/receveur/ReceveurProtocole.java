package fr.hillionj.quizzy.parametres.receveur;

import fr.hillionj.quizzy.communication.bluetooth.Peripherique;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "SdCardPath" })
public class ReceveurProtocole {

    private Peripherique peripherique;

    public Peripherique getPeripherique() {
        return peripherique;
    }

    public void setPeripherique(Peripherique peripherique) {
        this.peripherique = peripherique;
    }
}
