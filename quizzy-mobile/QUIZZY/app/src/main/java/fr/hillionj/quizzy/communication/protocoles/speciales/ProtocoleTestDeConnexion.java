package fr.hillionj.quizzy.communication.protocoles.speciales.ecran;

import fr.hillionj.quizzy.communication.protocoles.Protocole;
import fr.hillionj.quizzy.communication.protocoles.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ProtocoleFinQuiz extends Protocole
{
    @Override
    public String getFormat()
    {
        return "$" + getType().getIndiceType() + "\n";
    }

    @Override
    public TypeProtocole getType()
    {
        return TypeProtocole.FIN_QUIZ;
    }
    public void genererTrame()
    {
        super.genererTrame();
    }
}
