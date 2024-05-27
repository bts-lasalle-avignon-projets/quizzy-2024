package fr.hillionj.quizzy.parametres;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.logging.Handler;

import fr.hillionj.quizzy.bdd.BaseDeDonnees;
import fr.hillionj.quizzy.communication.GestionnaireBluetooth;
import fr.hillionj.quizzy.communication.Peripherique;
import fr.hillionj.quizzy.ihm.vues.IHM;
import fr.hillionj.quizzy.session.Session;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "SdCardPath" })
public class Parametres {

    private static Parametres parametres  = null;
    private AppCompatActivity activite;
    private int nombreDeQuestions = 20;
    private String theme = null;
    private Session session;
    private List<Peripherique> peripheriques;
    private List<Participant> participants;

    private void initaliserApplication() {
        this.peripheriques = new GestionnaireBluetooth(activite).initialiser(activite);
        this.session = new Session(this, activite);
        this.participants = this.session.getBaseDeDonnees().getParticipants();
        new IHM(this);
    }

    public static Parametres getParametres(AppCompatActivity activite) {
        if (parametres == null) {
            parametres = new Parametres(activite);
        } else {
            parametres.setActivite(activite);
        }
        return parametres;
    }

    public static Parametres getParametres() {
        return parametres;
    }

    public Parametres(AppCompatActivity activite) {
        this.activite = activite;

        initaliserApplication();
    }

    public void setActivite(AppCompatActivity activite) {
        this.activite = activite;
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

    public AppCompatActivity getActivite() {
        return activite;
    }

    public Session getSession() {
        return session;
    }
}
