package fr.hillionj.quizzy.protocole.speciales.application;

import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.protocole.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ProtocoleAcquitement extends Protocole
{
    @Override
    public String getFormat()
    {
        return "$" + getType().getIndiceType() + "\n";
    }

    @Override
    public String getTrame()
    {
        return getFormat();
    }

    @Override
    public TypeProtocole getType()
    {
        return TypeProtocole.ACQUITEMENT;
    }
}
