package fr.hillionj.quizzy.ihm.spinner;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.Participant;

public class ListViewParticipants extends BaseAdapter {

    private Context context;
    private List<Participant> participants = Parametres.getParametres().getParticipants();

    public ListViewParticipants(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return participants.size();
    }

    @Override
    public Object[] getItem(int position) {
        Participant participant  =participants.get(position);
        String description = "Non associé";
        int couleur = Color.RED;
        if (participant.getPeripherique() != null) {
            description = participant.getPeripherique().getNom();
        }

        return new Object[] {participant.getNom(), description, couleur};
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

        TextView text1 = convertView.findViewById(R.id.text1);
        TextView text2 = convertView.findViewById(R.id.text2);
        View colorView = convertView.findViewById(R.id.couleur);

        text1.setText(nom);
        text2.setText(description);
        colorView.setBackgroundColor(couleur);

        return convertView;
    }
}
