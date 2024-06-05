package fr.hillionj.quizzy.parametres;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.bdd.BaseDeDonnees;
import fr.hillionj.quizzy.communication.bluetooth.GestionnaireBluetooth;
import fr.hillionj.quizzy.communication.bluetooth.Peripherique;
import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.ihm.vues.VueSession;
import fr.hillionj.quizzy.parametres.receveur.speciales.Ecran;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;
import fr.hillionj.quizzy.session.Session;
import fr.hillionj.quizzy.session.contenu.Theme;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "SdCardPath" })
public class Parametres {

    private static Parametres parametres  = null;
    private final BaseDeDonnees baseDeDonnees;
    private int nombreDeQuestions = 5                    ;
    private Theme theme = null;
    private Session session = null;
    private final List<Peripherique> peripheriques;
    private final List<Participant> participants;
    private final List<Theme> themes;
    private AppCompatActivity activitePrincipale;
    private boolean calulAutomatiqueDuTempsDeReponse = false;
    private int tempsReponse = 30;

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
        this.baseDeDonnees = new BaseDeDonnees(activite.getApplicationContext());
        this.session = new Session(this, activite, ihm, baseDeDonnees);
        this.participants = this.baseDeDonnees.getParticipants();
        this.themes = this.baseDeDonnees.getThemes();
    }

    public Theme getTheme() {
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

    public void setTheme(Theme theme) {
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

    public List<Theme> getThemes() {
        return themes;
    }

    public AppCompatActivity getActivitePrincipale() {
        return activitePrincipale;
    }

    public List<Ecran> getEcrans() {
        List<Ecran> ecrans = new ArrayList<>();
        for (Peripherique peripherique : peripheriques) {
            if (estUnEcran(peripherique)) {
                ecrans.add(new Ecran(peripherique));
            }
        }
        return ecrans;
    }

    public Session nouvelleSession() {
        this.session = new Session(this.session);
        VueSession vueSession = (VueSession) IHM.getIHM().getActivite(VueSession.class);
        if (vueSession != null)
            vueSession.setSession(session);
        return this.session;
    }

    public boolean estUnEcran(@NonNull Peripherique peripherique) {
        return !peripherique.getNom().startsWith("quizzy-p");
    }

    public void setCalulAutomatiqueDuTempsDeReponse(boolean calulAutomatiqueDuTempsDeReponse) {
        this.calulAutomatiqueDuTempsDeReponse = calulAutomatiqueDuTempsDeReponse;
    }

    public boolean estCalulAutomatiqueDuTempsDeReponse() {
        return calulAutomatiqueDuTempsDeReponse;
    }

    public void setTempsReponse(int tempsReponse) {
        this.tempsReponse = tempsReponse;
    }

    public int getTempsReponse() {
        return tempsReponse;
    }

    public BaseDeDonnees getBaseDeDonnees() {
        return baseDeDonnees;
    }
}
