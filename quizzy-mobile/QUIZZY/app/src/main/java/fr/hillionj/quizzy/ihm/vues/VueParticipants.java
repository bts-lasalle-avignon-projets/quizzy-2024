package fr.hillionj.quizzy.ihm.vues;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import fr.hillionj.quizzy.R;
import fr.hillionj.quizzy.communication.bluetooth.Peripherique;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.ihm.widgets.ListViewPeripheriques;
import fr.hillionj.quizzy.ihm.widgets.SpinnerParticipants;
import fr.hillionj.quizzy.ihm.widgets.SpinnerPeripheriques;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;

public class VueParticipants extends AppCompatActivity {

    private ListViewPeripheriques liste_participants;
    private SpinnerParticipants spinner_participants;
    private SpinnerPeripheriques spinner_peripheriques;
    private Button btn_associer;

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

        initialiserAttributs();
        initialiserAssociation();

        IHM.getIHM().ajouterIHM(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        IHM.getIHM().ajouterIHM(this);
    }

    private void initialiserAttributs() {
        btn_associer = findViewById(R.id.btn_associer);
        spinner_participants = new SpinnerParticipants(this, R.id.spinner_participants);
        spinner_peripheriques = new SpinnerPeripheriques(this, R.id.spinner_peripheriques);
        liste_participants = new ListViewPeripheriques(this, R.id.liste_peripheriques);
    }

    private void initialiserAssociation() {
        btn_associer.setOnClickListener(v -> {
            int positionSpinnerParticipant = spinner_participants.getSpinner().getSelectedItemPosition();
            int positionSpinnerPeripheriques = spinner_peripheriques.getSpinner().getSelectedItemPosition();
            Participant participant = positionSpinnerParticipant == 0 ? null : Parametres.getParametres().getParticipants().get(positionSpinnerParticipant - 1);
            Peripherique peripherique = spinner_peripheriques.getPeripheriques().get(positionSpinnerPeripheriques);
            for (Participant participant1 : Parametres.getParametres().getParticipants()) {
                if (participant1.getPeripherique() == peripherique) {
                    participant1.setPeripherique(null);
                }
            }
            if (participant != null) {
                participant.setPeripherique(peripherique);
                Parametres.getParametres().getSession().getGestionnaireProtocoles().ajouterParticipant(participant);
            }
            mettreAjourListeParticipants();
        });
    }

    public void mettreAjourListeParticipants() {
        liste_participants.mettreAjour();
    }
}