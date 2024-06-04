package fr.hillionj.quizzy.communication.protocoles.speciales.pupitre;

import fr.hillionj.quizzy.communication.protocoles.Protocole;
import fr.hillionj.quizzy.communication.protocoles.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class EnregistrerTempsQuestion extends Protocole
{
    public EnregistrerTempsQuestion(String trame)
    {
        setTrame(trame);
    }

    @Override
    public String getFormat()
    {
        return "$" + getType().getIndiceType() + ";QUESTION;TEMPS\n";
    }

    @Override
    public TypeProtocole getType()
    {
        return TypeProtocole.ENREGISTRER_TEMPS_QUESTION;
    }

    public int getTempsReponse()
    {
        return toInt(extraireDonnees().get("TEMPS"));
    }

    public int getNumeroQuestion()
    {
        return toInt(extraireDonnees().get("QUESTION"));
    }

    public void genererTrame(int numeroQuestion, int tempsAlloue)
    {
        super.genererTrame(numeroQuestion + "", tempsAlloue + "");
    }
}
