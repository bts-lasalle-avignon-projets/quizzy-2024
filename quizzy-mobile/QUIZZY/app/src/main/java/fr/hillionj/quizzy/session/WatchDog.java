package fr.hillionj.quizzy.session;

import android.util.Log;

import fr.hillionj.quizzy.parametres.Parametres;

public class WatchDog extends Thread {

    private final long tempsPeriode = 1000;

    public WatchDog(Parametres parametres) {
        start();
    }

    public void run() {
        while (true) {
            try {
                loop();
                sleep(tempsPeriode);
            } catch (Exception e) {
                Log.e("QUIZZY_" + this.getClass().getName(), e.getMessage(), e);
            }
        }
    }

    private void loop() {
        Log.d("QUIZZY_" + this.getClass().getName(), "1 ms");
    }

    public void attendre(long millis) {

    }
}
