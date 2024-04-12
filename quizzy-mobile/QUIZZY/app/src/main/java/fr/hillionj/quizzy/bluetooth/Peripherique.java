package fr.hillionj.quizzy.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import fr.hillionj.quizzy.ActivitePrincipale;
import fr.hillionj.quizzy.protocole.GestionnaireProtocoles;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class Peripherique extends Thread
{
    private final String    TAG = "_Peripherique";
    private String          nom;
    private String          adresse;
    private int             indicePeripherique;
    private Handler         handler       = null;
    private BluetoothDevice device        = null;
    private BluetoothSocket socket        = null;
    private InputStream     receiveStream = null;
    private OutputStream    sendStream    = null;
    private TReception      tReception;

    @SuppressLint("MissingPermission")
    public Peripherique(BluetoothDevice device, Handler handler, int indicePeripherique)
    {
        Log.d(TAG,
              "Peripherique() nom = " + device.getName() + " - adresse mac = " +
                device.getAddress() + " - indicePeripherique = " + indicePeripherique);
        this.device             = device;
        this.handler            = handler;
        this.indicePeripherique = indicePeripherique;
        if(device != null)
        {
            this.nom     = device.getName();
            this.adresse = device.getAddress();
        }
        else
        {
            this.nom     = "Aucun";
            this.adresse = "";
        }
    }
    public String getNom()
    {
        return nom;
    }

    public String getAdresse()
    {
        return adresse;
    }

    public int getIndicePeripherique()
    {
        return indicePeripherique;
    }

    public boolean estConnecte()
    {
        return socket != null && socket.isConnected();
    }

    @SuppressLint("MissingPermission")
    public void connecter()
    {
        Log.d(TAG,
              "connecter() nom = " + getNom() + " - adresse mac = " + getAdresse() +
                " - indicePeripherique = " + indicePeripherique);
        try
        {
            socket = device.createRfcommSocketToServiceRecord(
              UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            receiveStream = socket.getInputStream();
            sendStream    = socket.getOutputStream();
        }
        catch(IOException e)
        {
            e.printStackTrace();
            socket = null;
        }
        if(socket != null)
        {
            tReception = new TReception();
        }
        new Thread() {
            @SuppressLint("MissingPermission")
            @Override
            public void run()
            {
                try
                {
                    socket.connect();
                    Message msg = Message.obtain();
                    msg.what    = GestionnaireProtocoles.CODE_CONNEXION_BLUETOOTH;
                    msg.obj     = indicePeripherique;
                    handler.sendMessage(msg);
                    tReception.start();
                }
                catch(IOException e)
                {
                    Log.d(TAG, "connecter() erreur connect socket");
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.what    = GestionnaireProtocoles.CODE_ERREUR_CONNEXION_BLUETOOTH;
                    msg.obj     = indicePeripherique;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public boolean deconnecter()
    {
        Log.d(TAG,
              "deconnecter() nom = " + getNom() + " - adresse mac = " + getAdresse() +
                " - indicePeripherique = " + indicePeripherique);
        try
        {
            tReception.arreter();
            socket.close();
            Message msg = Message.obtain();
            msg.what    = GestionnaireProtocoles.CODE_DECONNEXION_BLUETOOTH;
            msg.obj     = indicePeripherique;
            handler.sendMessage(msg);
            return true;
        }
        catch(IOException e)
        {
            Log.d(TAG, "deconnecter() erreur close socket");
            e.printStackTrace();
            return false;
        }
    }

    public void envoyer(String data)
    {
        /*Log.d(TAG,
              "envoyer() nom = " + getNom() + " - adresse mac = " + getAdresse() +
                " - indicePeripherique = " + indicePeripherique + " - datas = " + data);*/
        if(socket == null)
            return;
        try
        {
            sendStream.write(data.getBytes());
            sendStream.flush();
        }
        catch(IOException e)
        {
            Log.d(TAG, "envoyer() erreur send socket");
            e.printStackTrace();
        }
    }

    private class TReception extends Thread
    {
        private final String TAG  = "_TReception";
        private boolean      fini = false;

        @Override
        public void run()
        {
            recevoir();
        }

        public void recevoir()
        {
            while(!fini)
            {
                try
                {
                    if(receiveStream.available() > 0)
                    {
                        byte buffer[] = new byte[100];
                        int  k        = receiveStream.read(buffer, 0, 100);
                        if(k > 0)
                        {
                            byte rawdata[] = new byte[k];
                            for(int i = 0; i < k; i++)
                                rawdata[i] = buffer[i];
                            String data = new String(rawdata);
                            /*Log.d(TAG,
                                  "recevoir() nom = " + getNom() + " - adresse mac = " +
                                    getAdresse() + " - indicePeripherique = " + indicePeripherique +
                                    " - datas = " + data);*/
                            Message msg = Message.obtain();
                            msg.what    = GestionnaireProtocoles.CODE_RECEPTION_BLUETOOTH;
                            msg.obj     = data;
                            msg.arg1    = indicePeripherique;
                            handler.sendMessage(msg);
                        }
                    }
                    try
                    {
                        Thread.sleep(250);
                    }
                    catch(InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
                catch(IOException e)
                {
                    Log.d(TAG, "envoyer() erreur read socket");
                    e.printStackTrace();
                }
            }
        }

        public void arreter()
        {
            if(fini == false)
            {
                fini = true;
            }
            try
            {
                Thread.sleep(250);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
