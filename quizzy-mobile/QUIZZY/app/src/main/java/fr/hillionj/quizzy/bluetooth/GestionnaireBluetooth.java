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

import androidx.appcompat.app.AppCompatActivity;
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
    private AppCompatActivity activite;

    public synchronized static GestionnaireBluetooth getGestionnaireBluetooth(AppCompatActivity activite, Handler handler)
    {
        if(gestionnaireBluetooth == null)
            gestionnaireBluetooth = new GestionnaireBluetooth(activite, handler);
        return gestionnaireBluetooth;
    }

    @SuppressLint("MissingPermission")
    private GestionnaireBluetooth(AppCompatActivity activite, Handler handler)
    {
        this.activite = activite;
        this.handler = handler;
        if(!verifierPermissions())
        {
            return;
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    }

    @SuppressLint("MissingPermission")
    public void initialiser()
    {
        if (bluetoothAdapter == null) {
            FragmentPupitre.getVueActive().desactiverBoutons();
            Toast.makeText(activite.getApplicationContext(), "Bluetooth non activé !", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Toast.makeText(activite.getApplicationContext(), "Bluetooth non activé !", Toast.LENGTH_SHORT).show();
            Intent activeBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activite.startActivityForResult(activeBlueTooth, 0);
        } else {
            rechercherPeripheriquesConnus();
            initialiserSpinner(FragmentPupitre.getVueActive().spinnerListePeripheriques);
            initialiserListView(FragmentPupitre.getVueActive().listViewPeripheriquesConnectes);
        }
    }

    public void initialiserSpinner(Spinner spinnerListePeripheriques) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(activite, android.R.layout.simple_spinner_item, noms);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerListePeripheriques.setAdapter(adapter);
        adapter.setNotifyOnChange(true);
        spinnerListePeripheriques.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                peripherique = peripheriques.get(position);
                if (peripherique.estConnecte()) {
                    FragmentPupitre.getVueActive().activerBoutonDeconnecter();
                } else {
                    FragmentPupitre.getVueActive().activerBoutonConnecter();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    public void initialiserListView(ListView listViewPeripheriquesConnectes) {
        if (this.adapterPeripheriquesConnectes == null) {
            this.adapterPeripheriquesConnectes = new ArrayAdapter<>(activite, android.R.layout.simple_list_item_1);
        }
        listViewPeripheriquesConnectes.setAdapter(this.adapterPeripheriquesConnectes);
    }

    @SuppressLint("MissingPermission")
    public void rechercherPeripheriquesConnus()
    {
        for (BluetoothDevice blueDevice : bluetoothAdapter.getBondedDevices()) {
            peripheriques.add(new Peripherique(blueDevice, handler, peripheriques.size()));
            noms.add(blueDevice.getName());
            FragmentPupitre.getVueActive().activerBoutonConnecter();
        }
        if (peripheriques.isEmpty()) {
            peripheriques.add(new Peripherique(null, handler, peripheriques.size()));
        }
        if (noms.isEmpty()) {
            noms.add("Aucun");
        }
    }

    public List<Peripherique> getPeripheriquesConnectes()
    {
        List<Peripherique> liste = new ArrayList<>();
        for (Peripherique peripherique : peripheriques) {
            if (peripherique.estConnecte()) {
                liste.add(peripherique);
            }
        }
        return liste;
    }

    public void ajouterPeripheriqueConnecter(int indicePeripherique) {
        adapterPeripheriquesConnectes.add(peripheriques.get(indicePeripherique).getNom());
    }

    public boolean connecter()
    {
        if (peripherique == null) {
            return false;
        }
        peripherique.connecter();
        return true;
    }

    public boolean deconnecter()
    {
        if (peripherique != null) {
            boolean succesDeconnexion = peripherique.deconnecter();
            if (succesDeconnexion) {
                adapterPeripheriquesConnectes.remove(peripherique.getNom());
            }
            return succesDeconnexion;
        }
        return false;
    }

    private boolean verifierPermissions() {
        if (ActivityCompat.checkSelfPermission(activite, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activite, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 1);
            return false;
        }
        if (ActivityCompat.checkSelfPermission(activite, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activite, new String[]{Manifest.permission.BLUETOOTH_ADMIN}, 1);
            return false;
        }
        if (ActivityCompat.checkSelfPermission(activite, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activite, new String[]{Manifest.permission.BLUETOOTH}, 1);
            return false;
        }
        return true;
    }

    public ActivitePrincipale getActivite() {
        return (ActivitePrincipale) activite;
    }
}
