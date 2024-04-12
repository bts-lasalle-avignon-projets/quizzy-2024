package fr.hillionj.quizzy.protocole.speciales.pupitre;

import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.protocole.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ProtocoleActiverBuzzers extends Protocole
{
    public ProtocoleActiverBuzzers(String trame)
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
        return TypeProtocole.ACTIVER_BUZZERS;
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
