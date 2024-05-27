package fr.hillionj.quizzy.son;

import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

import fr.hillionj.quizzy.R;

@SuppressWarnings({ "SpellCheckingInspection", "unused" })
public class GestionnaireSonore
{

    private MediaPlayer bonne_reponse, mauvaise_reponse, nouvelle_question,
            reponses_vraies_et_fausses, selection_reponse;

    public GestionnaireSonore(AppCompatActivity activite)
    {
        bonne_reponse = MediaPlayer.create(activite, R.raw.bonne_reponse);
        mauvaise_reponse =
                MediaPlayer.create(activite, R.raw.mauvaise_reponse);
        nouvelle_question =
                MediaPlayer.create(activite, R.raw.nouvelle_question);
        reponses_vraies_et_fausses =
                MediaPlayer.create(activite, R.raw.reponses_vraies_et_fausses);
        selection_reponse =
                MediaPlayer.create(activite, R.raw.selection_reponse);
    }

    public void jouerBonneReponse()
    {
        bonne_reponse.start();
    }

    public void jouerMauvaiseReponse()
    {
        mauvaise_reponse.start();
    }

    public void jouerReponsesVaries()
    {
        reponses_vraies_et_fausses.start();
    }

    public void jouerNouvelleQuestion()
    {
        nouvelle_question.start();
    }

    public void jouerSelectionReponse()
    {
        selection_reponse.start();
    }
}
