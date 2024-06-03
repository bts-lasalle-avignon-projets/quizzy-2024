package fr.hillionj.quizzy.parametres;

import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.Quizzy;
import fr.hillionj.quizzy.communication.bluetooth.GestionnaireBluetooth;
import fr.hillionj.quizzy.communication.bluetooth.Peripherique;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.ihm.vues.VueSession;
import fr.hillionj.quizzy.parametres.receveur.speciales.Ecran;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;
import fr.hillionj.quizzy.session.Session;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "SdCardPath" })
public class Parametres {

    private static Parametres parametres  = null;
    private int nombreDeQuestions = 20;
    private String theme = null;
    private Session session = null;
    private final List<Peripherique> peripheriques;
    private final List<Participant> participants;
    private final List<String> themes;
    private AppCompatActivity activitePrincipale;

    public static Parametres getParametres(AppCompatActivity activite) {
        Log.d("QUIZZY_" + Parametres.class.getName(), "Instanciation de Parametres: " + activite);
        if (parametres == null) {
            parametres = new Parametres(activite);
        }
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

    public AppCompatActivity getActivitePrincipale() {
        return activitePrincipale;
    }

    public List<Ecran> getEcrans() {
        return new ArrayList<>(); //@TODO RENVOYER LES PERIPHERIQUES ECRAN
    }

    public Session nouvelleSession() {
        this.session = new Session(this.session);
        VueSession vueSession = (VueSession) IHM.getIHM().getActivite(VueSession.class);
        if (vueSession != null)
            vueSession.setSession(session);
        return this.session;
    }
}
