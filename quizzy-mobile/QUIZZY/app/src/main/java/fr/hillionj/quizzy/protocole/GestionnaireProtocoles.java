package fr.hillionj.quizzy.protocole;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

import fr.hillionj.quizzy.ActivitePrincipale;
import fr.hillionj.quizzy.bluetooth.GestionnaireBluetooth;
import fr.hillionj.quizzy.bluetooth.Peripherique;
import fr.hillionj.quizzy.navigation.pupitres.FragmentPupitre;
import fr.hillionj.quizzy.protocole.speciales.application.ProtocoleReceptionReponse;
import fr.hillionj.quizzy.receveurs.speciales.Participant;
import fr.hillionj.quizzy.questionnaire.Quiz;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class GestionnaireProtocoles
{
    private static final String           TAG = "_GestionnaireProtocoles";
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

    public void traiterProtocoleEntrant(Peripherique peripherique, Protocole protocole)
    {
        switch(protocole.getType())
        {
            case RECEPTION_REPONSE:
                Quiz.getQuizEnCours().recupererReponseSaisie(peripherique,
                                                             (ProtocoleReceptionReponse)protocole);
                break;
            case ACQUITEMENT:
                Log.d(TAG, protocole.getClass().getSimpleName());
                break;
            default:
                Log.e(TAG,
                      "Aucun protocole entrant pour " + protocole.getTrame() + " n'est trouvé");
                break;
        }
    }

    public Handler initialiserHandler(ActivitePrincipale activite)
    {
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                switch(msg.what)
                {
                    case CODE_CONNEXION_BLUETOOTH:
                        FragmentPupitre.getVueActive().activerBoutonDeconnecter();
                        GestionnaireBluetooth.getGestionnaireBluetooth(null, null)
                          .ajouterPeripheriqueConnecter((int)msg.obj);
                        break;
                    case CODE_ERREUR_CONNEXION_BLUETOOTH:
                        FragmentPupitre.getVueActive().activerBoutonConnecter();
                        Toast
                          .makeText(activite.getApplicationContext(),
                                    "Erreur de connexion",
                                    Toast.LENGTH_SHORT)
                          .show();
                        break;
                    case CODE_RECEPTION_BLUETOOTH:
                        for(String trame: ((String)msg.obj).split("\n"))
                        {
                            Protocole    protocole = Protocole.traiterTrame(trame + "\n");
                            Peripherique peripherique =
                              GestionnaireBluetooth.getGestionnaireBluetooth(null, null)
                                .getPeripheriques()
                                .get(msg.arg1);
                            if(protocole != null)
                            {
                                traiterProtocoleEntrant(peripherique, protocole);
                            }
                        }
                    default:
                        break;
                }
            }
        };
        return handler;
    }
}
