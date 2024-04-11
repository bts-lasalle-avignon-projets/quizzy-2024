package fr.hillionj.quizzy.protocole.speciales.ecran;

import fr.hillionj.quizzy.protocole.Protocole;
import fr.hillionj.quizzy.protocole.TypeProtocole;
import fr.hillionj.quizzy.questionnaire.Question;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class ProtocoleEnvoiQuestion extends Protocole
{
    public ProtocoleEnvoiQuestion(String trame)
    {
        setTrame(trame);
    }

    @Override
    public String getFormat()
    {
        return "$" + getType().getIndiceType() + ";LIBELLE;PROP1;PROP2;PROP3;PROP4;REPONSE;TEMPS\n";
    }

    @Override
    public TypeProtocole getType()
    {
        return TypeProtocole.ENVOI_QUESTION;
    }

    public String getLibelle()
    {
        return extraireDonnees().get("LIBELLE");
    }

    public List<String> getPropositions()
    {
        List<String>        propositions = new ArrayList<>();
        Map<String, String> donnees      = extraireDonnees();
        for(int i = 1; i <= 4; i++)
        {
            propositions.add(donnees.get("PROP" + i));
        }
        return propositions;
    }

    public int getTemps()
    {
        return toInt(extraireDonnees().get("TEMPS"));
    }

    public int getNumeroReponse()
    {
        return toInt(extraireDonnees().get("REPONSE"));
    }

    public void genererTrame(Question question) {
        List<String> arguments = new ArrayList<>();
        arguments.add(question.getQuestion());
        arguments.addAll(question.getReponses());
        arguments.add(question.getNumeroBonneReponse() + "");
        arguments.add(question.getTemps() + "");
        super.genererTrame(arguments.toArray(arguments.toArray(new String[0])));
    }
}
