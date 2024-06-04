package fr.hillionj.quizzy.communication.protocoles.speciales.ecran;

import fr.hillionj.quizzy.communication.protocoles.Protocole;
import fr.hillionj.quizzy.communication.protocoles.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class EnregistrerSelectionParticipant extends Protocole
{
    public EnregistrerSelectionParticipant(String trame)
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
        return TypeProtocole.ENREGISTRER_SELECTION_PARTICIPANT;
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

    public void genererTrame(String pid, int numeroReponse, long tempsReponse)
    {
        super.genererTrame(pid, numeroReponse + "", tempsReponse + "");
    }
}
