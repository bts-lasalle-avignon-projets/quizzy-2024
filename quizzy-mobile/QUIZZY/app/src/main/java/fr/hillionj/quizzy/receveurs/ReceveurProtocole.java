package fr.hillionj.quizzy.receveurs;

import fr.hillionj.quizzy.bluetooth.Peripherique;

public interface ReceveurProtocole {

    public Peripherique getPeripherique();

    public boolean estUnEcran();
}
