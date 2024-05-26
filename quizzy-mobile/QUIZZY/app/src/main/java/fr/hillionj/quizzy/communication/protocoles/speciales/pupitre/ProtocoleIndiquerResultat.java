package fr.hillionj.quizzy.communication.protocoles.speciales.pupitre;

import fr.hillionj.quizzy.communication.protocoles.Protocole;
import fr.hillionj.quizzy.communication.protocoles.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ProtocoleIndiquerResultat extends Protocole
{
    public ProtocoleIndiquerResultat(String trame)
    {
        setTrame(trame);
    }

    @Override
    public String getFormat()
    {
        return "$" + getType().getIndiceType() + ";QUESTION;RESULTAT\n";
    }

    @Override
    public TypeProtocole getType()
    {
        return TypeProtocole.INDIQUER_RESULTAT;
    }

    public int getNumeroQuestion()
    {
        return toInt(extraireDonnees().get("QUESTION"));
    }

    public boolean estReponseVraie()
    {
        return toInt(extraireDonnees().get("RESULTAT")) == 1;
    }

    public void genererTrame(int numeroQuestion, boolean reponseVraie)
    {
        super.genererTrame(numeroQuestion + "", reponseVraie + "");
    }
}
