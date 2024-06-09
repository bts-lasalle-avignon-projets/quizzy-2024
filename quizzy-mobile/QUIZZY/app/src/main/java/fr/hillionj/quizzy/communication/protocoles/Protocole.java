package fr.hillionj.quizzy.communication.protocoles;

import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.hillionj.quizzy.parametres.receveur.ReceveurProtocole;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public abstract class Protocole
{

    private String trame;

    public abstract String        getFormat();
    public abstract TypeProtocole getType();

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
    public static Protocole getProtocole(@NonNull TypeProtocole type)
    {
        return type.getProtocole(null);
    }

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
                sb.append(";").append(st);
            }
        }
        sb.append("\n");
        setTrame(sb.toString());
    }

    public void envoyer(@NonNull List<? extends ReceveurProtocole> receveursProtocoles)
    {
        for(ReceveurProtocole receveur: receveursProtocoles)
        {
            envoyer(receveur);
        }
    }

    public void envoyer(@NonNull ReceveurProtocole receveur)
    {
        if (receveur.getPeripherique() != null) {
            Log.v("QUIZZY_" + this.getClass().getSimpleName() + "_TEST1", "-> " + receveur.getPeripherique().getNom() + ": " + getTrame());
            receveur.getPeripherique().envoyer(getTrame());
        }
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

    public boolean estValide()
    {
        return estValide(getTrame().split(";"));
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
            Log.e("QUIZZY_" + this.getClass().getName(), e.getMessage(), e);
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
            Log.e("QUIZZY_" + this.getClass().getName(), e.getMessage(), e);
        }
        return longInt;
    }
}
