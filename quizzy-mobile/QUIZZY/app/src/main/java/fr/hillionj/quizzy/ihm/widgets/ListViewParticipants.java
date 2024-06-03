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
import fr.hillionj.quizzy.session.EtapeSession;
import fr.hillionj.quizzy.session.Session;

public class ListViewParticipants extends BaseAdapter {

    private Context context;
    private List<Participant> participants = new ArrayList<>();
    private ListView liste;

    public ListViewParticipants(AppCompatActivity activity, int id) {
        this.context = activity.getApplicationContext();
        liste = activity.findViewById(id);
        mettreAjour();
    }

    @Override
    public int getCount() {
        return participants.size();
    }

    @Override
    public Object[] getItem(int position) {
        Participant participant  = participants.get(position);
        int score = Parametres.getParametres().getSession().getScore(participant);
        boolean estRepondu = Parametres.getParametres().getSession().getQuestionActuelle().estSelectionne(participant);
        String description = score + " points";
        Peripherique peripherique = participant.getPeripherique();
        int logoPeripherique = R.drawable.bumper;
        if (!peripherique.getNom().startsWith("quizzy-p")) {
            logoPeripherique = R.drawable.ecran;
        }
        int couleur = Color.WHITE;
        if (estRepondu) {
            if (Parametres.getParametres().getSession().getEtape() == EtapeSession.PAUSE_FIN_QUESTION) {
                if (Parametres.getParametres().getSession().getQuestionActuelle().estPropositionValide(participant)) {
                    couleur = Color.GREEN;
                } else {
                    couleur = Color.RED;
                }
            } else {
                couleur = Color.YELLOW;
            }
        }
        return new Object[] {participant.getNom(), description, logoPeripherique, couleur};
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_items_session, parent, false);
        }

        Object[] objet = getItem(position);

        String nom = (String) objet[0];
        String description = (String) objet[1];
        int logoPeripherique = (int) objet[2];
        int couleur = (int) objet[3];

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
        this.participants.clear();
        for (Participant participant : Parametres.getParametres().getParticipants()) {
            if (participant.getPeripherique() != null) {
                this.participants.add(participant);
            }
        }
        liste.setAdapter(this);
    }
}
