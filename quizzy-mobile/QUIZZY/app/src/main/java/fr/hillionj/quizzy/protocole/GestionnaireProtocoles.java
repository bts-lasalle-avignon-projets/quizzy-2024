package fr.hillionj.quizzy.protocole;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.List;

import fr.hillionj.quizzy.ActivitePrincipale;
import fr.hillionj.quizzy.bluetooth.GestionnaireBluetooth;
import fr.hillionj.quizzy.bluetooth.Peripherique;
import fr.hillionj.quizzy.navigation.pupitres.FragmentPupitre;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class GestionnaireProtocoles
{
    private static GestionnaireProtocoles gestionnaireProtocoles;
    public static final int               CODE_CONNEXION_BLUETOOTH        = 33;
    public static final int               CODE_RECEPTION_BLUETOOTH        = 35;
    public static final int               CODE_DECONNEXION_BLUETOOTH      = 34;
    public static final int               CODE_ERREUR_CONNEXION_BLUETOOTH = 36;
    public static GestionnaireProtocoles  getGestionnaireProtocoles()
    {
        if(gestionnaireProtocoles == null)
        {
            gestionnaireProtocoles = new GestionnaireProtocoles();
        }
        return gestionnaireProtocoles;
    }

    public void envoyerProtocole(List<Peripherique> peripheriques, Protocole protocole)
    {
        for(Peripherique peripherique: peripheriques)
        {
            peripherique.envoyer(protocole.getTrame());
        }
    }

    public Handler initialiserHandler(ActivitePrincipale activite)
    {
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch(msg.arg1)
                {
                    case CODE_CONNEXION_BLUETOOTH:
                        FragmentPupitre.getVueActive().activerBoutonDeconnecter();
                        GestionnaireBluetooth.getGestionnaireBluetooth(null, null)
                          .ajouterPeripheriqueConnecter(msg.arg2);
                        break;
                    case CODE_ERREUR_CONNEXION_BLUETOOTH:
                        FragmentPupitre.getVueActive().activerBoutonConnecter();
                        Toast
                          .makeText(activite.getApplicationContext(),
                                    "Erreur de connexion",
                                    Toast.LENGTH_SHORT)
                          .show();
                        break;
                    default:
                        break;
                }
            }
        };
        return handler;
    }
}
