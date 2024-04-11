package fr.hillionj.quizzy.protocole.speciales.ecran;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.protocole.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ProtocoleIndicationReponseParticipant extends Protocole
{
    public ProtocoleIndicationReponseParticipant(String trame)
    {
        setTrame(trame);
    }

    @Override
    public String getFormat()
    {
        return "$" + getType().getIndiceType() + ";PID;REPONSE;TEMPS\n";
    }

    @Override
    public TypeProtocole getType()
    {
        return TypeProtocole.INDICATION_REPONSE_PARTICIPANT;
    }

    public String getPID()
    {
        return extraireDonnees().get("PID");
    }

    public int getTempsReponse()
    {
        return toInt(extraireDonnees().get("TEMPS"));
    }

    public int getNumeroReponse()
    {
        return toInt(extraireDonnees().get("REPONSE"));
    }

    public void genererTrame(String pid, int numeroReponse, long tempsReponse) {
        super.genererTrame(pid, numeroReponse + "", tempsReponse + "");
    }
}
