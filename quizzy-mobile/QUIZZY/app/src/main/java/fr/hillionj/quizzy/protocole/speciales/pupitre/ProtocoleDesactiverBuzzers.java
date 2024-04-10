package fr.hillionj.quizzy.protocole.speciales.pupitre;

import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.protocole.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ProtocoleDesactiverBuzzers extends Protocole
{
    private final String trame;

    public ProtocoleDesactiverBuzzers(String trame)
    {
        this.trame = trame;
    }

    @Override
    public String getFormat()
    {
        return "$E;QUESTION\n";
    }

    @Override
    public String getTrame()
    {
        return trame;
    }

    @Override
    public TypeProtocole getType()
    {
        return TypeProtocole.DESACTIVER_BUZZERS;
    }

    public int getNumeroQuestion()
    {
        return toInt(extraireDonnees().get("QUESTION"));
    }
}
