package fr.hillionj.quizzy.protocole.speciales.pupitre;

import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.protocole.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ProtocoleIndicationQuestion extends Protocole
{
    private final String trame;

    public ProtocoleIndicationQuestion(String trame)
    {
        this.trame = trame;
    }

    @Override
    public String getFormat()
    {
        return "$" + getType().getIndiceType() + ";QUESTION;TEMPS\n";
    }

    @Override
    public String getTrame()
    {
        return trame;
    }

    @Override
    public TypeProtocole getType()
    {
        return TypeProtocole.INDICATION_QUESTION;
    }

    public int getTempsReponse()
    {
        return toInt(extraireDonnees().get("TEMPS"));
    }

    public int getNumeroQuestion()
    {
        return toInt(extraireDonnees().get("QUESTION"));
    }
}
