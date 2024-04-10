package fr.hillionj.quizzy.protocole.speciales.pupitre;

import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.protocole.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ProtocoleActiverBuzzers extends Protocole
{
    private final String trame;

    public ProtocoleActiverBuzzers(String trame)
    {
        this.trame = trame;
    }

    @Override
    public String getFormat()
    {
        return "$D;QUESTION\n";
    }

    @Override
    public String getTrame()
    {
        return trame;
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
}
