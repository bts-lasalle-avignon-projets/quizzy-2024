package fr.hillionj.quizzy.parametres;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import fr.hillionj.quizzy.Quizzy;
import fr.hillionj.quizzy.communication.GestionnaireBluetooth;
import fr.hillionj.quizzy.communication.Peripherique;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.session.Session;
import fr.hillionj.quizzy.session.WatchDog;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "SdCardPath" })
public class Parametres {

    private static Parametres parametres  = null;
    private int nombreDeQuestions = 20;
    private String theme = null;
    private Session session;
    private List<Peripherique> peripheriques;
    private List<Participant> participants;
    private List<String> themes;
    private Quizzy activitePrincipale;

    public static Parametres getParametres(Quizzy activite) {
        if (parametres == null)
            parametres = new Parametres(activite);
        else
            parametres.activitePrincipale = activite;
        return parametres;
    }

    public static Parametres getParametres() {
        return parametres;
    }

    public Parametres(AppCompatActivity activite) {
        this.peripheriques = new GestionnaireBluetooth(activite).initialiser(activite);
        IHM ihm = new IHM(this, activite);
        this.session = new Session(this, activite, ihm);
        this.participants = this.session.getBaseDeDonnees().getParticipants();
        this.themes = this.session.getBaseDeDonnees().getThemes();
    }

    public String getTheme() {
        return theme;
    }

    public int getNombreDeQuestions() {
        return nombreDeQuestions;
    }

    public List<Peripherique> getPeripheriques() {
        return peripheriques;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public void setNombreDeQuestions(int nombreDeQuestions) {
        this.nombreDeQuestions = nombreDeQuestions;
    }

    public Session getSession() {
        return session;
    }

    public Participant getParticipantAssocier(Peripherique peripherique) {
        for (Participant participant : Parametres.getParametres().getParticipants()) {
            if (participant.getPeripherique() == peripherique) {
                return participant;
            }
        }
        return null;
    }

    public List<String> getThemes() {
        return themes;
    }

    public Quizzy getActivitePrincipale() {
        return activitePrincipale;
    }
}
