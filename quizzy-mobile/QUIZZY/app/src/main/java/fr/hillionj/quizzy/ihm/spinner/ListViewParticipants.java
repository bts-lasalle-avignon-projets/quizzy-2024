package fr.hillionj.quizzy.ihm.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.hillionj.quizzy.R;

public class ListViewParticipants extends BaseAdapter {

    private Context context;
    private List<Map<String, String>> participants;
    private List<Integer> colors; // Liste des couleurs

    public ListViewParticipants(Context context, List<Map<String, String>> participants, List<Integer> colors) {
        this.context = context;
        this.participants = participants;
        this.colors = colors;
    }

    @Override
    public int getCount() {
        return participants.size();
    }

    @Override
    public Object getItem(int position) {
        return participants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(fr.hillionj.quizzy.R.layout.list_items, parent, false);
        }

        Map<String, String> participant = participants.get(position);
        int color = colors.get(new Random().nextInt(3));

        TextView text1 = convertView.findViewById(R.id.text1);
        TextView text2 = convertView.findViewById(R.id.text2);
        View colorView = convertView.findViewById(R.id.couleur);

        text1.setText(participant.get("text1"));
        text2.setText(participant.get("text2"));
        colorView.setBackgroundColor(color);

        return convertView;
    }
}
