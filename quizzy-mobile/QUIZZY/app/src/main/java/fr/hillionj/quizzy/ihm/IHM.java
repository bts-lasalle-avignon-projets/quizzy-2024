package fr.hillionj.quizzy.ihm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import fr.hillionj.quizzy.ihm.popup.PopupAucunParticipant;
import fr.hillionj.quizzy.ihm.popup.PopupHistorique;
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
    public Object getIHMActive(Class<?> typeObjet) {
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
            if (getActiviteActive() instanceof VueSession && (!(pageIHM instanceof VueSession))) {
                Parametres.getParametres().getSession().getGestionnaireProtocoles().preparerRelancement();
                Parametres.getParametres().nouvelleSession();
            }
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
        if (vueSession != null)
            vueSession.afficherInterface();
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

    public void mettreAjourHistorique() {
        PopupHistorique popupHistorique = (PopupHistorique) getIHMActive(PopupHistorique.class);
        if (popupHistorique != null)
            popupHistorique.mettreAjourHistorique();
    }

    public void demarrerActivite(Object lanceur, Context contexte, Class<?> typeActivite) {
        Intent intent = new Intent(contexte, typeActivite);
        if (VueSession.class.isAssignableFrom(typeActivite)) {
            intent.putExtra("estRecree", false);
        }
        if (lanceur instanceof Fragment) {
            ((Fragment) lanceur).startActivity(intent);
        } else {
            ((AppCompatActivity) lanceur).startActivity(intent);
        }
    }
}
