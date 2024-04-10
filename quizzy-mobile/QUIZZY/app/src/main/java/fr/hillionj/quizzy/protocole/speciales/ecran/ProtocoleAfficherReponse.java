package fr.hillionj.quizzy.protocole.speciales.ecran;

import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.protocole.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ProtocoleAfficherReponse extends Protocole
{
    @Override
    public String getFormat()
    {
        return "$H\n";
    }

    @Override
    public String getTrame()
    {
        return getFormat();
    }

    @Override
    public TypeProtocole getType()
    {
        return TypeProtocole.AFFICHER_REPONSE;
    }
}
