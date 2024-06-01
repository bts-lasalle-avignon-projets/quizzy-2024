package fr.hillionj.quizzy.communication;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import fr.hillionj.quizzy.ihm.IHM;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
@SuppressLint("MissingPermission")
public class Peripherique extends Thread
{
    private String          nom, adresseMAC;
    private int             indicePeripherique;
    private BluetoothDevice device        = null;
    private BluetoothSocket socket        = null;
    private InputStream     receiveStream = null;
    private OutputStream    sendStream    = null;
    private TReception      tReception;
    private GestionnaireBluetooth gestionnaireBluetooth;
    private boolean seConnecte = false;

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
                    envoyerCodeAuHandler(gestionnaireBluetooth.CODE_CONNEXION_BLUETOOTH);
                    tReception.start();
                }
                catch(IOException e)
                {
                    Log.e("QUIZZY_" + this.getClass().getName(), "connecter() erreur connect socket", e);
                    envoyerCodeAuHandler(gestionnaireBluetooth.CODE_ERREUR_CONNEXION_BLUETOOTH);
                }
            }
        }.start();
    }

    public boolean seConnecte() {
        return seConnecte;
    }

    public void setSeConnecte(boolean seConnecte) {
        this.seConnecte = seConnecte;
    }

    private void envoyerCodeAuHandler(int what, Object obj)
    {
        Message msg = Message.obtain();
        msg.what    = what;
        msg.obj     = obj;
        msg.arg1 = indicePeripherique;
        gestionnaireBluetooth.getHandler().sendMessage(msg);
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
            Log.e("QUIZZY_" + this.getClass().getName(), "initialiserSocket() erreur init socket", e);
            socket = null;
            seConnecte = false;
            IHM.getIHM().mettreAjourListeParticipants();
        }
    }

    public Peripherique(GestionnaireBluetooth gestionnaireBluetooth, @NonNull BluetoothDevice device, int indicePeripherique)
    {
        this.gestionnaireBluetooth = gestionnaireBluetooth;
        this.device             = device;
        this.indicePeripherique = indicePeripherique;
        this.nom     = device.getName();
        this.adresseMAC = device.getAddress();
    }
    public String getNom()
    {
        return nom;
    }

    public String getAdresse()
    {
        return adresseMAC;
    }

    public int getIndicePeripherique()
    {
        return indicePeripherique;
    }

    public boolean estConnecte()
    {
        return socket != null && socket.isConnected();
    }

    public void connecter()
    {
        seConnecte = true;
        IHM.getIHM().mettreAjourListeParticipants();
        initialiserSocket();
        initialiserReception();
        demarrerThread();
    }

    public void envoyerCodeAuHandler(int what)
    {
        envoyerCodeAuHandler(what, null);
    }

    public boolean deconnecter()
    {
        return deconnecter(true);
    }

    public boolean deconnecter(boolean estPrevue)
    {
        IHM.getIHM().mettreAjourListeParticipants();
        try
        {
            tReception.arreter();
            socket.close();
            envoyerCodeAuHandler(gestionnaireBluetooth.CODE_DECONNEXION_BLUETOOTH, estPrevue);
            return true;
        }
        catch(IOException e)
        {
            Log.e("QUIZZY_" + this.getClass().getName(), "deconnecter() erreur close socket", e);
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
            Log.e("QUIZZY_" + this.getClass().getName(), "envoyer() erreur send socket", e);
            signalerInterruption();
        }
    }

    public void signalerInterruption()
    {
        deconnecter(false);
    }

    private class TReception extends Thread
    {
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
                    Log.d("QUIZZY_" + this.getClass().getName(), "envoyer() erreur read socket");
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
                envoyerCodeAuHandler(gestionnaireBluetooth.CODE_RECEPTION_BLUETOOTH, new String(rawdata));
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
