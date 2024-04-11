package fr.hillionj.quizzy.protocole;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.hillionj.quizzy.receveurs.ReceveurProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public abstract class Protocole
{
    private static final String TAG = "_Protocole";
    public static Protocole     traiterTrame(String trame)
    {
        if(trame == null)
        {
            return null;
        }
        String        indiceTypeProtocole = trame.split(";")[0].replace("$", "").replace("\n", "");
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

    private String trame;

    public abstract String        getFormat();
    public abstract TypeProtocole getType();

    public String getTrame()
    {
        return trame;
    }

    public void setTrame(String trame)
    {
        this.trame = trame;
    }

    protected void genererTrame(String... contenu)
    {
        if(!estValide(false, contenu))
        {
            setTrame(null);
            return;
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
        setTrame(sb.toString());
    }

    public void envoyer(List<? extends ReceveurProtocole> receveursProtocoles) {
        for (ReceveurProtocole receveur : receveursProtocoles) {
            envoyer(receveur);
        }
    }

    public void envoyer(ReceveurProtocole receveur) {
        Log.d(TAG,  "(" + receveur.getPeripherique().getNom() +  ") Envoi du Protocole " + getClass().getSimpleName() + " : " + getTrame().replace("\n", "\\n"));
        receveur.getPeripherique().envoyer(getTrame());
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
