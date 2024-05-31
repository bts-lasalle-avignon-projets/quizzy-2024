package fr.hillionj.quizzy.session;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import fr.hillionj.quizzy.ihm.IHM;
import fr.hillionj.quizzy.parametres.Parametres;

public class WatchDog {

    private final long tempsPeriode = 1;
    private long heureDebutTempsPause = 0;
    private long tempsPause = 0;
    private ScheduledExecutorService scheduler;
    private IHM ihm;
    private boolean etaitEnPause = false;

    public WatchDog(IHM ihm) {
        this.ihm = ihm;
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleWithFixedDelay(() -> {
            try
            {
                loop();
            }
            catch(Exception e)
            {
                Log.e("QUIZZ_" + this.getClass().getName(), e.getMessage(), e);
            }
        }, 0, tempsPeriode, TimeUnit.MILLISECONDS);
    }

    private void loop() {
        if (estEnPause()) {
            etaitEnPause = true;
            return;
        }
        if (etaitEnPause) {
            reprendreSession();
            etaitEnPause  = false;
        }
        afficherChrono();
    }

    private void afficherBoutons() {
        ihm.getActiviteActive().runOnUiThread(() -> {
            ihm.afficherBoutons();
        });
    }

    private void afficherChrono() {
        ihm.getActiviteActive().runOnUiThread(() -> {
            Parametres.getParametres().getSession().verifierChrono();
            ihm.afficherChrono();
        });
    }

    public void arreter() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }

    public void pause() {
        pause(-1);
    }

    public void pause(long millis) {
        tempsPause = millis;
        heureDebutTempsPause = System.currentTimeMillis();
    }

    public boolean estEnPause() {
        return tempsPause != 0 && (tempsPause == -1 || System.currentTimeMillis() - heureDebutTempsPause < tempsPause);
    }

    public void reprendre() {
        tempsPause = 0;
    }

    public void reprendreSession() {
        ihm.getActiviteActive().runOnUiThread(() ->
        {
            Parametres.getParametres().getSession().reprendre();
        });
    }
}
