package fr.hillionj.quizzy.receveurs.speciales;

import fr.hillionj.quizzy.bluetooth.Peripherique;
import fr.hillionj.quizzy.receveurs.ReceveurProtocole;

public class Ecran implements ReceveurProtocole
{
    private Peripherique peripherique;

    public Ecran(Peripherique peripherique)
    {
        this.peripherique = peripherique;
    }

    @Override
    public Peripherique getPeripherique()
    {
        return peripherique;
    }
}
