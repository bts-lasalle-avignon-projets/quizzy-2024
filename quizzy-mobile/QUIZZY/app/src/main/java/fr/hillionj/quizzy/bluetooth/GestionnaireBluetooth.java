package fr.hillionj.quizzy.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.navigation.pupitres.FragmentPupitre;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "MissingPermission" })
public class GestionnaireBluetooth
{
    private Handler           handler          = null;
    private BluetoothAdapter  bluetoothAdapter = null;
    private Peripherique      peripherique;
    private AppCompatActivity activite;

    private static final List<Peripherique> peripheriques              = new ArrayList<>();
    private static final List<String>    noms                          = new ArrayList<>();
    private static ArrayAdapter<String>  adapterPeripheriquesConnectes = null;
    private static final String          TAG = "_GestionnaireBluetooth"; //!< TAG pour les logs
    private static GestionnaireBluetooth gestionnaireBluetooth = null;

    private void creerToast(AppCompatActivity activite, String message)
    {
        Toast.makeText(activite.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void definirComportementSpinner(Spinner spinnerListePeripheriques)
    {
        spinnerListePeripheriques.setOnItemSelectedListener(
          new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id)
              {
                  peripherique = peripheriques.get(position);
                  if(FragmentPupitre.getVueActive() != null)
                  {
                      FragmentPupitre.getVueActive().mettreAjourEtatBoutons();
                  }
              }

              @Override
              public void onNothingSelected(AdapterView<?> arg0)
              {
              }
          });
    }

    private boolean verifierPermissions()
    {
        List<String> permissionsManquantes = new ArrayList<>();
        verifierPermission(permissionsManquantes, Manifest.permission.BLUETOOTH_CONNECT);
        verifierPermission(permissionsManquantes, Manifest.permission.BLUETOOTH_ADMIN);
        verifierPermission(permissionsManquantes, Manifest.permission.BLUETOOTH);
        if(!permissionsManquantes.isEmpty())
        {
            ActivityCompat.requestPermissions(
              activite,
              permissionsManquantes.toArray(new String[permissionsManquantes.size()]),
              1);
        }
        return true;
    }

    private void verifierPermission(List<String> permissionsManquantes, String permission)
    {
        if(ActivityCompat.checkSelfPermission(activite, Manifest.permission.BLUETOOTH_CONNECT) !=
           PackageManager.PERMISSION_GRANTED)
        {
            permissionsManquantes.add(permission);
            Log.d(TAG, "verifierPermissions() request permission");
        }
    }

    public synchronized static GestionnaireBluetooth initialiser(AppCompatActivity activite,
                                                                 Handler           handler)
    {
        gestionnaireBluetooth = new GestionnaireBluetooth(activite, handler);
        if(gestionnaireBluetooth.bluetoothAdapter == null)
        {
            gestionnaireBluetooth.creerToast(activite, "Bluetooth non activé !");
        }
        else if(!gestionnaireBluetooth.bluetoothAdapter.isEnabled())
        {
            gestionnaireBluetooth.creerToast(activite, "Bluetooth non activé !");
            Intent activeBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activite.startActivityForResult(activeBlueTooth, 0);
        }
        else
        {
            gestionnaireBluetooth.rechercherPeripheriquesConnus();
        }
        return gestionnaireBluetooth;
    }

    public synchronized static GestionnaireBluetooth getGestionnaireBluetooth()
    {
        return gestionnaireBluetooth;
    }

    @SuppressLint("MissingPermission")
    private GestionnaireBluetooth(AppCompatActivity activite, Handler handler)
    {
        Log.d(TAG, "GestionnaireBluetooth()");
        this.activite = activite;
        this.handler  = handler;
        if(!verifierPermissions())
        {
            bluetoothAdapter = null;
            return;
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void mettreAjourSpinnerPeripheriques(Spinner spinnerListePeripheriques)
    {
        ArrayAdapter<String> adapter =
          new ArrayAdapter<>(activite, android.R.layout.simple_spinner_item, noms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListePeripheriques.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        definirComportementSpinner(spinnerListePeripheriques);
    }

    public void mettreAjourListViewPeripheriques(ListView listViewPeripheriquesConnectes)
    {
        if(this.adapterPeripheriquesConnectes == null)
        {
            this.adapterPeripheriquesConnectes =
              new ArrayAdapter<>(activite, android.R.layout.simple_list_item_1);
        }
        listViewPeripheriquesConnectes.setAdapter(this.adapterPeripheriquesConnectes);
    }

    public void setActivite(AppCompatActivity activite)
    {
        this.activite = activite;
    }

    @SuppressLint("MissingPermission")
    public void rechercherPeripheriquesConnus()
    {
        for(BluetoothDevice blueDevice: bluetoothAdapter.getBondedDevices())
        {
            peripheriques.add(new Peripherique(blueDevice, handler, peripheriques.size()));
            noms.add(blueDevice.getName());
        }
        if(peripheriques.isEmpty())
        {
            peripheriques.add(new Peripherique(null, handler, peripheriques.size()));
        }
        if(noms.isEmpty())
        {
            noms.add("Aucun");
        }
    }

    public List<Peripherique> getPeripheriquesConnectes()
    {
        List<Peripherique> liste = new ArrayList<>();
        for(Peripherique peripherique: peripheriques)
        {
            if(peripherique.estConnecte())
            {
                liste.add(peripherique);
            }
        }
        return liste;
    }

    public void ajouterPeripheriqueConnecter(int indicePeripherique)
    {
        adapterPeripheriquesConnectes.add(peripheriques.get(indicePeripherique).getNom());
    }

    public void retirerPeripheriqueConnecter(int indicePeripherique)
    {
        adapterPeripheriquesConnectes.remove(peripheriques.get(indicePeripherique).getNom());
    }

    public boolean connecter()
    {
        if(peripherique == null)
        {
            return false;
        }
        peripherique.connecter();
        return true;
    }

    public void deconnecter()
    {
        if(peripherique != null)
        {
            peripherique.deconnecter();
        }
        return;
    }

    public Peripherique getPeripheriqueSelectionne()
    {
        return peripherique;
    }

    public List<Peripherique> getPeripheriques()
    {
        return peripheriques;
    }
}
