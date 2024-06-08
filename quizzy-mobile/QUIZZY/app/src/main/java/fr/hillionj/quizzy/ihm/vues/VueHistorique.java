package fr.hillionj.quizzy.ihm.vues;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.ihm.widgets.ListViewQuestions;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.session.Session;

public class VueHistorique extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activite_historique);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        int idEvaluation = getIntent().getExtras().getInt("idEvaluation");
        Session session = Parametres.getParametres().getBaseDeDonnees().getSession(idEvaluation);

        new ListViewQuestions(this, R.id.liste_resultats, session);
    }
}
