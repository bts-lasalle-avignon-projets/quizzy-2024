package fr.hillionj.quizzy.protocole.speciales.ecran;

import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.protocole.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ProtocoleLancement extends Protocole
{
    @Override
    public String getFormat()
    {
        return "$L\n";
    }

    @Override
    public String getTrame()
    {
        return getFormat();
    }

    @Override
    public TypeProtocole getType()
    {
        return TypeProtocole.LANCEMENT;
    }
}
