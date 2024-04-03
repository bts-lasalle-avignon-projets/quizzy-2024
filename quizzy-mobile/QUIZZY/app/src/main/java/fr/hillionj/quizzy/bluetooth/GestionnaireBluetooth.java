package fr.hillionj.quizzy.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.ActivitePrincipale;
import fr.hillionj.quizzy.navigation.pupitres.FragmentPupitre;

public class GestionnaireBluetooth
{
    private static final String TAG = "_GestionnaireBluetooth"; //!< TAG pour les logs
    private static GestionnaireBluetooth gestionnaireBluetooth = null;
    private Handler                    handler = null;
    private BluetoothAdapter         bluetoothAdapter;
    private Peripherique             peripherique;
    private final List<Peripherique> peripheriques             = new ArrayList<>();
    private final List<String>   noms                          = new ArrayList<>();
    private ArrayAdapter<String> adapterPeripheriquesConnectes = null;

    public synchronized static GestionnaireBluetooth getGestionnaireBluetooth(Handler handler)
    {
        if(gestionnaireBluetooth == null)
            gestionnaireBluetooth = new GestionnaireBluetooth(handler);
        return gestionnaireBluetooth;
    }

    @SuppressLint("MissingPermission")
    private GestionnaireBluetooth(Handler handler)
    {
        this.handler = handler;
        if(!verifierPermissions())
        {
            return;
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void initialiser()
    {
    }

    public void rechercherPeripheriquesConnus()
    {
    }

    public List<Peripherique> getPeripheriquesConnectes()
    {
        return null;
    }

    public boolean connecter()
    {
        return true;
    }

    public boolean deconnecter()
    {
        return false;
    }

    private boolean verifierPermissions()
    {
        return true;
    }
}
