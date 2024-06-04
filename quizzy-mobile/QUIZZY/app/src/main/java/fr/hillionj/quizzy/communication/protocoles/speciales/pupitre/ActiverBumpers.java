package fr.hillionj.quizzy.communication.protocoles.speciales.pupitre;

import fr.hillionj.quizzy.communication.protocoles.Protocole;
import fr.hillionj.quizzy.communication.protocoles.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ActiverBumpers extends Protocole
{
    public ActiverBumpers(String trame)
    {
        setTrame(trame);
    }

    @Override
    public String getFormat()
    {
        return "$" + getType().getIndiceType() + ";QUESTION\n";
    }

    @Override
    public TypeProtocole getType()
    {
        return TypeProtocole.ACTIVER_BUMPERS;
    }

    public int getNumeroQuestion()
    {
        return toInt(extraireDonnees().get("QUESTION"));
    }

    public void genererTrame(int numeroQuestion)
    {
        super.genererTrame(numeroQuestion + "");
    }
}
