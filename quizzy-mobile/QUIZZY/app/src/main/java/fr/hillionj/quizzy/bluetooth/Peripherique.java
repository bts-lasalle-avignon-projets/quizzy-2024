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

public class Peripherique extends Thread {
    private String nom, adresse;
    private Handler handler = null;
    private BluetoothDevice device = null;
    private BluetoothSocket socket = null;
    private InputStream receiveStream = null;
    private OutputStream sendStream = null;
    private TReception tReception;
    private int indicePeripherique;
    @SuppressLint("MissingPermission")
    public Peripherique(BluetoothDevice device, Handler handler, int indicePeripherique) {
        this.device = device;
        this.handler = handler;
        this.indicePeripherique = indicePeripherique;
        if (device != null) {
            this.nom = device.getName();
            this.adresse = device.getAddress();
        } else {
            this.nom = "Aucun";
            this.adresse = "";
        }
    }
    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public boolean estConnecte() {
        return false;
    }

    public void connecter() {

    }

    public boolean deconnecter()
    {
        return false;
    }

    public void envoyer(String data)
    {
    }

    private class TReception extends Thread {
        Handler handlerUI;
        private boolean fini = false;

        TReception(Handler h) {
            handlerUI = h;
        }

        @Override
        public void run() {

        }

        public void arreter() {
        }
    }
}
