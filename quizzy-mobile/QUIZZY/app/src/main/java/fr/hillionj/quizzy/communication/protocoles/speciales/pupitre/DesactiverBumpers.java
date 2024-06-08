package fr.hillionj.quizzy.communication.protocoles.speciales.pupitre;

import fr.hillionj.quizzy.communication.protocoles.Protocole;
import fr.hillionj.quizzy.communication.protocoles.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class DesactiverBumpers extends Protocole
{
    public DesactiverBumpers(String trame)
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
        return TypeProtocole.DESACTIVER_BUMPERS;
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
