package fr.hillionj.quizzy.protocole.speciales.ecran;

import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.protocole.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ProtocoleInscriptionParticipant extends Protocole
{
    public ProtocoleInscriptionParticipant(String trame)
    {
        setTrame(trame);
    }
    @Override
    public String getFormat()
    {
        return "$" + getType().getIndiceType() + ";PID;NOM\n";
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

    public void genererTrame(String pid, String nom)
    {
        super.genererTrame(pid, nom);
    }
}
