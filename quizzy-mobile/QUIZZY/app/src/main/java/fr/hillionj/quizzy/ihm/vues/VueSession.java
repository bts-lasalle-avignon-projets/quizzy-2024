package fr.hillionj.quizzy.ihm.vues;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.ihm.widgets.ListViewPeripheriques;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.session.Question;
import fr.hillionj.quizzy.session.Session;

public class VueSession extends AppCompatActivity {

    private ListViewPeripheriques liste;
    private Session session;
    private TextView question, chronometre, progression;
    private TextView[] propositions;
    private Button btn_stopper, btn_reinitialiser, btn_pause, btn_suivant, btn_precedent;
    private DecimalFormat format = new DecimalFormat("00.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activite_session);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getSupportActionBar().hide();

        IHM.getIHM().ajouterIHM(this);

        liste = new ListViewPeripheriques(this, R.id.liste_participants);
        question = findViewById(R.id.question);
        propositions = new TextView[4];
        propositions[0] = findViewById(R.id.proposition1);
        propositions[1] = findViewById(R.id.proposition2);
        propositions[2] = findViewById(R.id.proposition3);
        propositions[3] = findViewById(R.id.proposition4);
        chronometre = findViewById(R.id.chronometre);
        progression = findViewById(R.id.progression);
        btn_stopper = findViewById(R.id.btn_stopper);
        btn_pause = findViewById(R.id.btn_pause);
        btn_reinitialiser = findViewById(R.id.btn_reinitialiser);
        btn_suivant = findViewById(R.id.btn_suivant);
        btn_precedent = findViewById(R.id.btn_precedent);

        btn_suivant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.suivant();
            }
        });

        btn_precedent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.precedent();
            }
        });

        Parametres.getParametres().getSession().lancer();
    }

    public void mettreAjourListeParticipants() {
        liste.mettreAjour();
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public void afficherInterface() {
        Question question = session.getQuestionActuelle();
        this.question.setText(question.getQuestion());
        for (int i = 0; i < propositions.length; i++) {
            propositions[i].setText(session.getQuestionActuelle().getPropositions().get(i));
        }
        afficherChrono();
        progression.setText(session.getNumeroQuestion() + "/" + session.getTotalQuestions());
        afficherBoutons();
    }

    public void afficherBoutons(){
        btn_precedent.setEnabled(session.getNumeroQuestion() != 1);
        btn_suivant.setEnabled(session.getNumeroQuestion() != session.getTotalQuestions());
    }

    public void afficherChrono() {
        Question question = session.getQuestionActuelle();
        double tempsRestant = Parametres.getParametres().getSession().getTempsRestant();
        chronometre.setText(format.format(tempsRestant).replace(',', '.'));
        if (tempsRestant == 0.0) {
            chronometre.setTextColor(Color.RED);
        } else if (tempsRestant < 3.0) {
            chronometre.setTextColor(Color.YELLOW);
        } else {
            chronometre.setTextColor(getResources().getColor(R.color.white));
        }
    }
}