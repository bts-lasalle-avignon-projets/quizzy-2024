package fr.hillionj.quizzy.protocole;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public abstract class Protocole
{
    private static final String TAG = "_Protocole";
    public static Protocole     traiterTrame(String trame)
    {
        if(trame == null || !trame.contains(";"))
        {
            return null;
        }
        String        indiceTypeProtocole = trame.split(";")[0].replace("$", "");
        TypeProtocole type                = TypeProtocole.getType(indiceTypeProtocole);
        if(type == null)
        {
            return null;
        }
        return type.getProtocole(trame);
    }
    public static Protocole getProtocole(TypeProtocole type)
    {
        return type.getProtocole(null);
    }

    public abstract String        getFormat();
    public abstract String        getTrame();
    public abstract TypeProtocole getType();

    public String genererTrame(String... contenu)
    {
        if(!estValide(false, contenu))
        {
            return null;
        }
        StringBuilder sb = new StringBuilder("$" + getType().getIndiceType());
        if(contenu != null)
        {
            for(String st: contenu)
            {
                sb.append(";" + st);
            }
        }
        sb.append("\n");
        return sb.toString();
    }

    public boolean estValide(boolean trameComplete, String... contenu)
    {
        int nombreArgumentsRequis = getFormat().split(";").length;
        if(nombreArgumentsRequis == 1 && contenu == null)
        {
            return true;
        }
        int nombreArgumentsActuel = contenu == null ? 0 : contenu.length;
        if(!trameComplete)
        {
            nombreArgumentsRequis--;
        }
        return nombreArgumentsRequis == nombreArgumentsActuel;
    }

    public boolean estValide(String... contenu)
    {
        return estValide(true, contenu);
    }

    protected Map<String, String> extraireDonnees()
    {
        if(!estValide(getTrame().split(";")))
        {
            return null;
        }
        Map<String, String> donnees = new HashMap<>();
        String[] cles               = getFormat().replace("$", "").replace("\n", "").split(";");
        String[] arguments          = getTrame().replace("$", "").replace("\n", "").split(";");
        for(int i = 1; i < arguments.length; i++)
        {
            donnees.put(cles[i], arguments[i]);
        }
        return donnees;
    }

    public int toInt(String string)
    {
        int integer = 0;
        try
        {
            if(string != null)
            {
                integer = Integer.parseInt(string);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return integer;
    }

    public long toLong(String string)
    {
        long longInt = 0;
        try
        {
            if(string != null)
            {
                longInt = Long.parseLong(string);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return longInt;
    }
}
