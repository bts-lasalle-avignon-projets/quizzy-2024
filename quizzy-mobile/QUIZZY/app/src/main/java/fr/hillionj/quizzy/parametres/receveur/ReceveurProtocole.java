package fr.hillionj.quizzy.parametres;

import fr.hillionj.quizzy.communication.Peripherique;

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
