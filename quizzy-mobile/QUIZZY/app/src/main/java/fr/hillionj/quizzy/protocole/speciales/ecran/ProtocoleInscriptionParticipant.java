package fr.hillionj.quizzy.protocole.speciales.ecran;

import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.protocole.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ProtocoleInscriptionParticipant extends Protocole
{
    private final String trame;

    public ProtocoleInscriptionParticipant(String trame)
    {
        this.trame = trame;
    }
    @Override
    public String getFormat()
    {
        return "$I;PID;NOM\n";
    }
    @Override
    public String getTrame()
    {
        return trame;
    }

    @Override
    public TypeProtocole getType()
    {
        return TypeProtocole.INSCRIPTION_PARTICIPANT;
    }

    public String getNom()
    {
        return extraireDonnees().get("NOM");
    }

    public String getPID()
    {
        return extraireDonnees().get("PID");
    }
}
