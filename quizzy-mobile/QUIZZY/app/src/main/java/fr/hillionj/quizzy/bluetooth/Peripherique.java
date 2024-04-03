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

public class Peripherique extends Thread
{
    private final String TAG = "_Peripherique";
    private String          nom;
    private String          adresse;
    private Handler         handler       = null;
    private BluetoothDevice device        = null;
    private BluetoothSocket socket        = null;
    private InputStream     receiveStream = null;
    private OutputStream    sendStream    = null;
    private TReception      tReception;
    private int             indicePeripherique;
    @SuppressLint("MissingPermission")
    public Peripherique(BluetoothDevice device, Handler handler, int indicePeripherique)
    {
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

    public boolean estConnecte()
    {
        return socket != null && socket.isConnected();
    }

    @SuppressLint("MissingPermission")
    public void connecter()
    {
        try {
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            receiveStream = socket.getInputStream();
            sendStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            socket = null;
        }
        if (socket != null) {
            tReception = new TReception(handler);
        }
        Log.d(TAG, "connecter()");
        new Thread() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                try {
                    socket.connect();
                    Message msg = Message.obtain();
                    msg.arg1 = GestionnaireBluetooth.getGestionnaireBluetooth(null, handler).getActivite().CODE_CONNEXION;
                    msg.arg2 = indicePeripherique;
                    handler.sendMessage(msg);
                    tReception.start();
                }
                catch (IOException e)
                {
                    System.out.println("<Socket> error connect");
                    e.printStackTrace();
                    Message msg = Message.obtain();
                    msg.arg1 = GestionnaireBluetooth.getGestionnaireBluetooth(null, handler).getActivite().CODE_ERREUR_CONNEXION;
                    msg.arg2 = indicePeripherique;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    public boolean deconnecter()
    {
        try
        {
            tReception.arreter();
            socket.close();
            Message msg = Message.obtain();
            msg.arg1 = GestionnaireBluetooth.getGestionnaireBluetooth(null, handler).getActivite().CODE_DECONNEXION;
            msg.arg2 = indicePeripherique;
            handler.sendMessage(msg);
            return true;
        }
        catch (IOException e)
        {
            System.out.println("<Socket> error close");
            e.printStackTrace();
            return false;
        }
    }

    public void envoyer(String data)
    {
        Log.d(TAG, "envoyer() data = " + data);
        if(socket == null)
            return;
        try
        {
            sendStream.write(data.getBytes());
            sendStream.flush();
        }
        catch (IOException e)
        {
            System.out.println("<Socket> error send");
            e.printStackTrace();
        }
    }

    private class TReception extends Thread
    {
        private final String TAG = "_TReception";
        Handler         handlerUI;
        private boolean fini = false;
        TReception(Handler h)
        {
            handlerUI = h;
        }

        @Override
        public void run()
        {
            while (!fini) {
                try {
                    if (receiveStream.available() > 0) {
                        byte buffer[] = new byte[100];
                        int k = receiveStream.read(buffer, 0, 100);
                        if (k > 0) {
                            byte rawdata[] = new byte[k];
                            for (int i = 0; i < k; i++)
                                rawdata[i] = buffer[i];
                            String data = new String(rawdata);
                            Message msg = Message.obtain();
                            msg.what = GestionnaireBluetooth.getGestionnaireBluetooth(null, handler).getActivite().CODE_RECEPTION;
                            msg.obj = data;
                            handlerUI.sendMessage(msg);
                        }
                    }
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    //System.out.println("<Socket> error read");
                    e.printStackTrace();
                }
            }
        }

        public void arreter()
        {
            if (fini == false) {
                fini = true;
            }
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
