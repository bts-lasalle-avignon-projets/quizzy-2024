package fr.hillionj.quizzy.ihm.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import java.util.List;

import fr.hillionj.quizzy.Quizzy;
import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.communication.bluetooth.Peripherique;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.ihm.vues.VueHistorique;
import fr.hillionj.quizzy.ihm.vues.VueParametres;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;
import fr.hillionj.quizzy.session.Session;

public class ListViewHistorique extends BaseAdapter {

    private Context context;
    private AppCompatActivity activite;
    private List<Session> sessionsPrecedentes;
    private ListView liste;

    public ListViewHistorique(AppCompatActivity activite, View vue, int id) {
        this.activite = activite;
        this.context = vue.getContext();
        liste = vue.findViewById(id);
        mettreAjour();
    }

    @Override
    public int getCount() {
        return sessionsPrecedentes.size();
    }

    @Override
    public Object[] getItem(int position) {
        Session session = sessionsPrecedentes.get(position);
        String titre = session.getHorodatage();
        String description = session.getParticipants().size() + " participants - " + session.getQuestions().size() + " questions";

        return new Object[] {session, titre, description};
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_items_historique, parent, false);
        }

        Object[] objet = getItem(position);
        Session session = (Session) objet[0];
        String titre = (String) objet[1];
        String description = (String) objet[2];

        TextView text1 = convertView.findViewById(R.id.text1);
        TextView text2 = convertView.findViewById(R.id.text2);
        convertView.findViewById(R.id.btn_voir_session).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activite, VueHistorique.class);
                intent.putExtra("idEvaluation", session.getIdEvaluation());
                activite.startActivity(intent);
            }
        });
        convertView.findViewById(R.id.btn_supprimer_session).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Parametres.getParametres().getBaseDeDonnees().supprimerSession(session);
                IHM.getIHM().mettreAjourHistorique();
            }
        });

        text1.setText(titre);
        text2.setText(description);
        return convertView;
    }

    public void mettreAjour() {
        this.sessionsPrecedentes = Parametres.getParametres().getBaseDeDonnees().getHistorique();
        liste.setAdapter(this);
    }
}
