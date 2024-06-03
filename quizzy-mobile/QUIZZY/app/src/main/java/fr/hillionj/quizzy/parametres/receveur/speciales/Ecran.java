package fr.hillionj.quizzy.parametres.receveur.speciales;

import fr.hillionj.quizzy.communication.bluetooth.Peripherique;
import fr.hillionj.quizzy.parametres.receveur.ReceveurProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "SdCardPath" })
public class Ecran extends ReceveurProtocole {

    public Ecran(Peripherique peripherique) {
        setPeripherique(peripherique);
    }
}
