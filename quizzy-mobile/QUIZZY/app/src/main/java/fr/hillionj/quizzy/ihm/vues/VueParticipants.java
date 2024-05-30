package fr.hillionj.quizzy.ihm.vues;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.ihm.spinner.ListViewParticipants;
import fr.hillionj.quizzy.ihm.spinner.Spinner;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.Participant;

public class VueParticipants extends AppCompatActivity {

    private ListView liste_participants;
    private Spinner spinner_participants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activite_participants);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialiserListView();
    }

    public void initialiserBouton() {

    }

    public void initialiserListView() {
        liste_participants = findViewById(R.id.liste_participants);

        List<Map<String, String>> listItems = new ArrayList<>();
        for (Participant participant : Parametres.getParametres().getParticipants()) {
            Map<String, String> map = new HashMap<>();
            map.put("text1", participant.getNom());
            map.put("text2", "Aucun périphérique associé");
            listItems.add(map);
        }

        ListViewParticipants adapter = new ListViewParticipants(this);
        liste_participants.setAdapter(adapter);
    }
}