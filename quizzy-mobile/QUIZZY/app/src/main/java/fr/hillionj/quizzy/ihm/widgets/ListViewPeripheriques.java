package fr.hillionj.quizzy.ihm.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.communication.bluetooth.Peripherique;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;
import fr.hillionj.quizzy.session.Session;

public class ListViewPeripheriques extends BaseAdapter {

    private Context context;
    private List<Peripherique> peripheriques = Parametres.getParametres().getPeripheriques();
    private ListView liste;

    public ListViewPeripheriques(AppCompatActivity activity, int id) {
        this.context = activity.getApplicationContext();
        liste = activity.findViewById(id);
        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Peripherique peripherique = peripheriques.get(i);
                if (peripherique.seConnecte()) {
                    return;
                }
                if (peripherique.estConnecte()) {
                    peripherique.deconnecter();
                } else {
                    peripherique.connecter();
                }
            }
        });
        mettreAjour();
    }

    @Override
    public int getCount() {
        return peripheriques.size();
    }

    @Override
    public Object[] getItem(int position) {
        Peripherique peripherique  =peripheriques.get(position);
        String description = "Non associé";
        if (Parametres.getParametres().estUnEcran(peripherique)) {
            if (peripherique.estConnecte()) {
                description = "Connecté";
            } else if (peripherique.seConnecte()) {
                description = "Connexion...";
            } else {
                description = "Déconnecté";
            }
        } else {
            Participant participantAssocier = Parametres.getParametres().getParticipantAssocier(peripherique);
            if (participantAssocier != null) {
                description = participantAssocier.getNom();
            }
        }
        int couleur = Color.RED;
        if (peripherique.estConnecte()) {
            couleur = Color.GREEN;
        } else if (peripherique.seConnecte()) {
            couleur = Color.YELLOW;
        }
        int logoPeripherique = R.drawable.bumper;
        if (Parametres.getParametres().estUnEcran(peripherique)) {
            logoPeripherique = R.drawable.ecran;
        }
        return new Object[] {peripherique.getNom(), description, couleur, logoPeripherique};
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false);
        }

        Object[] objet = getItem(position);

        String nom = (String) objet[0];
        String description = (String) objet[1];
        int couleur = (int) objet[2];
        int logoPeripherique = (int) objet[3];

        TextView text1 = convertView.findViewById(R.id.text1);
        TextView text2 = convertView.findViewById(R.id.text2);
        ImageView logo_peripherique = convertView.findViewById(R.id.logo_peripherique);

        text1.setText(nom);
        text2.setText(description);
        logo_peripherique.setImageDrawable(AppCompatResources.getDrawable(context, logoPeripherique));
        logo_peripherique.setColorFilter(couleur, PorterDuff.Mode.SRC_ATOP);

        return convertView;
    }

    public void mettreAjour() {
        liste.setAdapter(this);
    }
}
