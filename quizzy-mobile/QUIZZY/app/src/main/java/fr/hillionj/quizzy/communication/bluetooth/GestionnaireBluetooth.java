package fr.hillionj.quizzy.communication.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.communication.protocoles.Protocole;
import fr.hillionj.quizzy.communication.protocoles.speciales.application.ProtocoleReceptionReponse;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.parametres.Parametres;

@SuppressLint("MissingPermission")
public class GestionnaireBluetooth {

    public final int               CODE_CONNEXION_BLUETOOTH        = 33;
    public final int               CODE_DECONNEXION_BLUETOOTH      = 34;
    public final int               CODE_RECEPTION_BLUETOOTH        = 35;
    public final int               CODE_ERREUR_CONNEXION_BLUETOOTH = 36;
    private final BluetoothAdapter bluetoothAdapter;
    private final Handler handler;

    public GestionnaireBluetooth(AppCompatActivity activite) {
        this.handler = initialiserHandler();
        if(!verifierPermissions(activite)) {
            bluetoothAdapter = null;
            return;
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null || !bluetoothAdapter.isEnabled())
        {
            creerToast(activite, "Bluetooth non activé !");
            if (!bluetoothAdapter.isEnabled()) {
                Intent activeBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activite.startActivityForResult(activeBlueTooth, 0);
            }
        }
    }

    public List<Peripherique> initialiser(AppCompatActivity activite) {
        if (bluetoothAdapter != null && bluetoothAdapter.isEnabled())
        {
            return rechercherPeripheriquesConnus();
        }
        return new ArrayList<>();
    }

    private List<Peripherique> rechercherPeripheriquesConnus()
    {
        List<Peripherique> list = new ArrayList<>();
        for(BluetoothDevice blueDevice: bluetoothAdapter.getBondedDevices())
        {
            list.add(new Peripherique(this, blueDevice, list.size()));
        }
        return list;
    }

    private boolean verifierPermissions(AppCompatActivity activite)
    {
        List<String> permissionsManquantes = new ArrayList<>();
        verifierPermission(activite, permissionsManquantes, Manifest.permission.BLUETOOTH_CONNECT);
        verifierPermission(activite, permissionsManquantes, Manifest.permission.BLUETOOTH_ADMIN);
        verifierPermission(activite, permissionsManquantes, Manifest.permission.BLUETOOTH);
        if(!permissionsManquantes.isEmpty())
        {
            ActivityCompat.requestPermissions(
                    activite,
                    permissionsManquantes.toArray(new String[permissionsManquantes.size()]),
                    1);
            return false;
        }
        return true;
    }

    private void verifierPermission(AppCompatActivity activite, List<String> permissionsManquantes, String permission)
    {
        if (ActivityCompat.checkSelfPermission(activite, permission) != PackageManager.PERMISSION_GRANTED)
        {
            permissionsManquantes.add(permission);
        }
    }

    private void creerToast(AppCompatActivity activite, String message)
    {
        Toast.makeText(activite.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Handler initialiserHandler()
    {
        Handler handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                Peripherique peripherique = Parametres.getParametres().getPeripheriques().get(msg.arg1);
                switch(msg.what)
                {
                    case CODE_CONNEXION_BLUETOOTH:
                    case CODE_ERREUR_CONNEXION_BLUETOOTH:
                        peripherique.setSeConnecte(false);
                        IHM.getIHM().mettreAjourListeParticipants();
                        break;
                    case CODE_RECEPTION_BLUETOOTH:
                        for (String trame : ((String) msg.obj).split("\n")) {
                            Protocole protocoleRecue = Protocole.traiterTrame(trame);
                            if (protocoleRecue == null) {
                                continue;
                            }
                            Log.v("QUIZZY_" + this.getClass().getName(), "<- " + peripherique.getNom() + ": " + trame);
                            switch (protocoleRecue.getType()) {
                                case RECEPTION_REPONSE:
                                    Parametres.getParametres().getSession().selectionnerProposition(Parametres.getParametres().getParticipantAssocier(peripherique), (ProtocoleReceptionReponse) protocoleRecue);
                                    break;
                                default:
                                     break;
                            }
                        }
                        break;
                    case CODE_DECONNEXION_BLUETOOTH:
                        IHM.getIHM().mettreAjourListeParticipants();
                        //traiterDeconnexion(msg);
                        break;
                    default:
                        break;
                }
            }
        };
        return handler;
    }

    public Handler getHandler() {
        return handler;
    }
}
