package fr.hillionj.quizzy.ihm.widgets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.ihm.vues.VueHistorique;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;
import fr.hillionj.quizzy.session.Session;
import fr.hillionj.quizzy.session.contenu.Question;

public class ListViewQuestions extends BaseAdapter {

    private Context context;
    private final Session session;
    private final List<Question> questions;
    private ListView liste;

    public ListViewQuestions(AppCompatActivity activite, int id, Session session) {
        this.session = session;
        this.questions = new ArrayList<>(session.getQuestions());
        this.questions.add(0, null);
        this.questions.add(null);
        this.context = activite.getApplicationContext();
        liste = activite.findViewById(id);
        mettreAjour();
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Object getItem(int position) {
        return questions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_items_questions, parent, false);
        }

        Question question = (Question) getItem(position);
        TextView text1 = convertView.findViewById(R.id.text1);
        if (question != null) {
            text1.setText(position + " : " + question.getQuestion());
            text1.setPadding(0, 10, 0, 0);
        } else if (position == 0) {
            text1.setText("");
            text1.setPadding(0, 50, 0, 0);
        } else {
            text1.setText("Nombre total de points");
            text1.setBackgroundColor(context.getResources().getColor(R.color.dark_red));
            text1.setPadding(0, 5, 0, 5);
        }

        LinearLayout colonne_participant = convertView.findViewById(R.id.colonne_participant);
        for (int i = 0; i < session.getParticipants().size(); i++) {
            Participant participant = session.getParticipants().get(i);
            colonne_participant.addView(new TextView(convertView.getContext()));
            colonne_participant.getChildAt(i).setLayoutParams(new LinearLayout.LayoutParams(250, LinearLayout.LayoutParams.MATCH_PARENT));
            colonne_participant.getChildAt(i).setPadding(5, 0, 0, 0);
            colonne_participant.getChildAt(i).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            if (question == null) {
                if (position == 0) {
                    ((TextView) colonne_participant.getChildAt(i)).setText(participant.getNom());
                } else {
                    ((TextView) colonne_participant.getChildAt(i)).setText(session.getScore(participant) + " pts");
                    colonne_participant.getChildAt(i).setBackgroundColor(context.getResources().getColor(R.color.dark_red));
                }
            } else if (question.estPropositionValide(participant)) {
                ((TextView) colonne_participant.getChildAt(i)).setText("1");
            } else {
                ((TextView) colonne_participant.getChildAt(i)).setText("0");
            }
            ((TextView) colonne_participant.getChildAt(i)).setTextColor(convertView.getResources().getColor(R.color.white));
            ((TextView) colonne_participant.getChildAt(i)).setTextSize(21);
            ((TextView) colonne_participant.getChildAt(i)).setGravity(16);
        }
        return convertView;
    }

    public void mettreAjour() {
        liste.setAdapter(this);
    }
}
