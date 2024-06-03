package fr.hillionj.quizzy.ihm;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.ihm.popup.PopupNonConnecte;
import fr.hillionj.quizzy.ihm.vues.VueParticipants;
import fr.hillionj.quizzy.ihm.vues.VueSession;
import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.session.Session;

public class IHM {

    private static IHM ihm;
    private List<Object> ihmActives = new ArrayList<>();
    private Parametres parametres;
    private AppCompatActivity activiteActive = null;

    @Nullable
    private Object getIHMActive(Class<?> typeObjet) {
        for (Object ihmActive : ihmActives) {
            if (typeObjet.isInstance(ihmActive)) {
                return ihmActive;
            }
        }
        return null;
    }

    public static IHM getIHM() {
        return IHM.ihm;
    }

    public IHM(Parametres parametres, AppCompatActivity activiteActive) {
        IHM.ihm = this;
        this.activiteActive = activiteActive;
        this.parametres = parametres;
    }

    public void ajouterIHM(Object pageIHM) {
        if (pageIHM instanceof AppCompatActivity) {
            Log.d("RED_", "Activite active: " + pageIHM.toString());
            activiteActive = (AppCompatActivity) pageIHM;
        }
        for (Object ihmActive : ihmActives) {
            if (pageIHM.getClass() == ihmActive.getClass()) {
                ihmActives.remove(ihmActive);
                break;
            }
        }
        ihmActives.add(pageIHM);
    }

    public void mettreAjourListeParticipants() {
        VueParticipants vueParticipants = (VueParticipants) getIHMActive(VueParticipants.class);
        if (vueParticipants != null)
            vueParticipants.mettreAjourListeParticipants();
        VueSession vueSession = (VueSession) getIHMActive(VueSession.class);
        if (vueSession != null)
            vueSession.mettreAjourListeParticipants();
        PopupNonConnecte nonConnecte = (PopupNonConnecte) getIHMActive(PopupNonConnecte.class);
        if (nonConnecte != null)
            nonConnecte.mettreAjourEtatBoutons();
    }

    public void afficherInterface() {
        VueSession vueSession = (VueSession) getIHMActive(VueSession.class);
        if (vueSession != null) {
            vueSession.afficherInterface();
        }
    }

    public AppCompatActivity getActiviteActive() {
        return activiteActive;
    }

    public void afficherBoutons() {
        VueSession vueSession = (VueSession) getIHMActive(VueSession.class);
        if (vueSession != null)
            vueSession.afficherBoutons();
    }

    public void afficherChrono() {
        VueSession vueSession = (VueSession) getIHMActive(VueSession.class);
        if (vueSession != null)
            vueSession.afficherChrono();
    }

    public AppCompatActivity getActivite(Class<?> typeActivite) {
        return (AppCompatActivity) getIHMActive(typeActivite);
    }

    public void fermerActivite(Class<?> typeActivite) {
        Object activite = getIHMActive(typeActivite);
        if (activite instanceof AppCompatActivity) {
            ((AppCompatActivity)activite).finish();
        }
    }

    public void fermerPopups() {
        for (Object ihmActive : ihmActives) {
            if (ihmActive instanceof DialogFragment) {
                try {
                    ((DialogFragment)ihmActive).dismiss();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
