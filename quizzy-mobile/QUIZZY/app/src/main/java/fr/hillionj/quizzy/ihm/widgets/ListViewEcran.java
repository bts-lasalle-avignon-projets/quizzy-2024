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
import fr.hillionj.quizzy.parametres.receveur.speciales.Ecran;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;
import fr.hillionj.quizzy.session.EtapeSession;

public class ListViewEcran extends BaseAdapter {

    private Context context;
    private List<Ecran> ecrans = new ArrayList<>();
    private ListView liste;

    public ListViewEcran(AppCompatActivity activity, int id) {
        this.context = activity.getApplicationContext();
        liste = activity.findViewById(id);
        mettreAjour();
    }

    @Override
    public int getCount() {
        return ecrans.size();
    }

    @Override
    public Object[] getItem(int position) {
        Ecran ecran = ecrans.get(position);
        int couleur = Color.GREEN;
        return new Object[] {ecran.getPeripherique().getNom(), couleur};
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
        int couleur = (int) objet[1];

        TextView text1 = convertView.findViewById(R.id.text1);
        TextView text2 = convertView.findViewById(R.id.text2);
        ImageView logo_peripherique = convertView.findViewById(R.id.logo_peripherique);

        text1.setText(nom);
        text2.setText("");
        logo_peripherique.setImageDrawable(AppCompatResources.getDrawable(context, R.drawable.ecran));
        logo_peripherique.setColorFilter(couleur, PorterDuff.Mode.SRC_ATOP);

        return convertView;
    }

    public void mettreAjour() {
        this.ecrans.clear();
        for (Ecran ecran : Parametres.getParametres().getEcrans()) {
            if (ecran.getPeripherique() != null && ecran.getPeripherique().estConnecte()) {
                this.ecrans.add(ecran);
            }
        }
        liste.setAdapter(this);
    }
}
