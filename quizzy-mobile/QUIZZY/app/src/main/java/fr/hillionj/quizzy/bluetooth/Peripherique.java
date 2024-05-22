package fr.hillionj.quizzy.bluetooth;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.telecom.DisconnectCause;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import fr.hillionj.quizzy.ActivitePrincipale;
import fr.hillionj.quizzy.protocole.GestionnaireProtocoles;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
@SuppressLint("MissingPermission")
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

    private void demarrerThread()
    {
        new Thread() {
            @SuppressLint("MissingPermission")
            @Override
            public void run()
            {
                try
                {
                    socket.connect();
                    envoyerCodeAuHandler(GestionnaireProtocoles.CODE_CONNEXION_BLUETOOTH,
                                         indicePeripherique);
                    tReception.start();
                }
                catch(IOException e)
                {
                    Log.e(TAG, "connecter() erreur connect socket", e);
                    envoyerCodeAuHandler(GestionnaireProtocoles.CODE_ERREUR_CONNEXION_BLUETOOTH,
                                         indicePeripherique);
                }
            }
        }.start();
    }

    private void envoyerCodeAuHandler(int what, Object obj, Integer arg1)
    {
        Message msg = Message.obtain();
        msg.what    = what;
        msg.obj     = obj;
        if(arg1 != null)
        {
            msg.arg1 = (int)arg1;
        }
        handler.sendMessage(msg);
    }

    private void initialiserReception()
    {
        if(socket != null)
        {
            tReception = new TReception();
        }
    }

    private void initialiserSocket()
    {
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
    }

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

    public boolean estInterrompu()
    {
        if(!estConnecte())
        {
            return false;
        }
        try
        {
            if(receiveStream.available() == 0)
            {
                receiveStream.read();
            }
            sendStream.write(0);
            sendStream.flush();
            return false;
        }
        catch(IOException e)
        {
        }
        return true;
    }

    public void connecter()
    {
        Log.d(TAG,
              "connecter() nom = " + getNom() + " - adresse mac = " + getAdresse() +
                " - indicePeripherique = " + indicePeripherique);
        initialiserSocket();
        initialiserReception();
        demarrerThread();
    }

    public void envoyerCodeAuHandler(int what, Object obj)
    {
        envoyerCodeAuHandler(what, obj, null);
    }

    public boolean deconnecter()
    {
        return deconnecter(true);
    }

    public boolean deconnecter(boolean estPrevue)
    {
        Log.d(TAG,
              "deconnecter() nom = " + getNom() + " - adresse mac = " + getAdresse() +
                " - indicePeripherique = " + indicePeripherique);
        try
        {
            tReception.arreter();
            socket.close();
            envoyerCodeAuHandler(GestionnaireProtocoles.CODE_DECONNEXION_BLUETOOTH,
                                 estPrevue,
                                 indicePeripherique);
            return true;
        }
        catch(IOException e)
        {
            Log.e(TAG, "deconnecter() erreur close socket", e);
            return false;
        }
    }

    public void envoyer(String data)
    {
        if(socket == null)
            return;
        try
        {
            sendStream.write(data.getBytes());
            sendStream.flush();
        }
        catch(IOException e)
        {
            Log.e(TAG, "envoyer() erreur send socket", e);
            signalerInterruption();
        }
    }

    public void signalerInterruption()
    {
        deconnecter(false);
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
                        traiterReception();
                    }
                    attendre(250);
                }
                catch(IOException e)
                {
                    Log.d(TAG, "envoyer() erreur read socket");
                    e.printStackTrace();
                }
            }
        }

        private void traiterReception() throws IOException
        {
            byte buffer[] = new byte[100];
            int  k        = receiveStream.read(buffer, 0, 100);
            if(k > 0)
            {
                byte rawdata[] = new byte[k];
                for(int i = 0; i < k; i++)
                    rawdata[i] = buffer[i];
                envoyerCodeAuHandler(GestionnaireProtocoles.CODE_RECEPTION_BLUETOOTH,
                                     new String(rawdata),
                                     indicePeripherique);
            }
        }

        private void attendre(long millisecondes)
        {
            try
            {
                Thread.sleep(millisecondes);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        public void arreter()
        {
            fini = true;
            attendre(250);
        }
    }
}
