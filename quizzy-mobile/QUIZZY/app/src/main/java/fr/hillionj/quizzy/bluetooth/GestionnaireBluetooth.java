package fr.hillionj.quizzy.bluetooth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
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

public class GestionnaireBluetooth {
    private static GestionnaireBluetooth gestionnaireBluetooth;
    public static GestionnaireBluetooth initialiserGestionnaireBluetooth(ActivitePrincipale activite) {
        return null;
    }
    public static GestionnaireBluetooth getGestionnaireBluetooth() {
        return gestionnaireBluetooth;
    }
    private final ActivitePrincipale activite;
    private BluetoothAdapter bluetoothAdapter;
    private Peripherique peripherique;
    private final List<Peripherique> peripheriques = new ArrayList<>();
    private final List<String> noms = new ArrayList<>();
    private ArrayAdapter<String> adapterPeripheriquesConnectes = null;

    @SuppressLint("MissingPermission")
    private GestionnaireBluetooth(ActivitePrincipale activite) {
        this.activite = activite;
        if (!verifierPermissions()) {
            return;
        }
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void initialiser() {
    }

    public void initialiserSpinner(Spinner spinnerListePeripheriques) {
    }

    public void initialiserListView(ListView listViewPeripheriquesConnectes) {
    }

    public void rechercherPeripheriquesConnus() {
    }

    public boolean verifierPermissions() {
        return true;
    }

    public List<Peripherique> getPeripheriquesConnectes() {
        return null;
    }

    public void ajouterPeripheriqueConnecter(int indicePeripherique) {
    }

    public boolean connecter() {
        return true;
    }

    public boolean deconnecter() {
        return false;
    }
}
