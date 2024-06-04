package fr.hillionj.quizzy.communication.protocoles.speciales.application;

import fr.hillionj.quizzy.communication.protocoles.Protocole;
import fr.hillionj.quizzy.communication.protocoles.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ReceptionReponse extends Protocole
{
    public ReceptionReponse(String trame)
    {
        setTrame(trame);
    }

    @Override
    public String getFormat()
    {
        return "$" + getType().getIndiceType() + ";QUESTION;REPONSE;TEMPS\n";
    }

    @Override
    public TypeProtocole getType()
    {
        return TypeProtocole.RECEPTION_REPONSE;
    }

    public long getTempsReponse()
    {
        return toLong(extraireDonnees().get("TEMPS"));
    }

    public int getNumeroReponse()
    {
        return toInt(extraireDonnees().get("REPONSE"));
    }

    public int getNumeroQuestion()
    {
        return toInt(extraireDonnees().get("QUESTION"));
    }

    public void genererTrame(int numeroQuestion, int numeroReponse, int tempsReponse)
    {
        super.genererTrame(numeroQuestion + "", numeroReponse + "", tempsReponse + "");
    }
}
