package fr.hillionj.quizzy.communication.protocoles.speciales;

import fr.hillionj.quizzy.communication.protocoles.Protocole;
import fr.hillionj.quizzy.communication.protocoles.TypeProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class VerifierConnexion extends Protocole
{
    @Override
    public String getFormat()
    {
        return "$" + getType().getIndiceType() + "\n";
    }

    @Override
    public TypeProtocole getType()
    {
        return TypeProtocole.TEST_CONNEXION;
    }
    public void genererTrame()
    {
        super.genererTrame();
    }
}
