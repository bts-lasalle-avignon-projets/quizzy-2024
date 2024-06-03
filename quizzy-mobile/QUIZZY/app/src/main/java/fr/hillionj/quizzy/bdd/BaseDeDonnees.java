/**
 * @author Thierry VAIRA
 * @author Jules HILLION
 * @file BaseDeDonnees.java
 * @brief La classe assurant la gestion de la base de données SQLite
 */

package fr.hillionj.quizzy.bdd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;
import fr.hillionj.quizzy.session.Question;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "SdCardPath" })
public class BaseDeDonnees extends SQLiteOpenHelper
{
    private static final String nomFichier = "quizzy.db";

    private final SQLiteDatabase sqlite;

    public BaseDeDonnees(Context context)
    {
        super(context, nomFichier, null, 1);
        new CopieurBDD(nomFichier, context).verifier();
        this.sqlite = this.getReadableDatabase();
    }

    private Question genererQuestion(Cursor curseur)
    {
        String idQuestion = curseur.getString(curseur.getColumnIndexOrThrow("idQuestion"));
        String question = curseur.getString(curseur.getColumnIndexOrThrow("question"));
        String prop1    = curseur.getString(curseur.getColumnIndexOrThrow("proposition1"));
        String prop2    = curseur.getString(curseur.getColumnIndexOrThrow("proposition2"));
        String prop3    = curseur.getString(curseur.getColumnIndexOrThrow("proposition3"));
        String prop4    = curseur.getString(curseur.getColumnIndexOrThrow("proposition4"));

        List<String> propositions = Arrays.asList(prop1, prop2, prop3, prop4);

        int nombreCarateres = (question + prop1 + prop2 + prop3 + prop4).length();
        int tempsReponse = getTempsReponse(-1, nombreCarateres);

        return new Question(Integer.parseInt(idQuestion), question, propositions, tempsReponse);
    }

    private int getTempsReponse(int tempsParQuestion, int nombreCarateres)
    {
        if(tempsParQuestion == -1)
            return nombreCarateres / 5 + 1;
        return tempsParQuestion;
    }

    @NonNull
    private String construireRequete(@NonNull Parametres parametres)
    {
        String query =
                "SELECT idQuestion, question, proposition1, proposition2, proposition3, proposition4 FROM questions INNER JOIN themes ON questions.idTheme = themes.idTheme";
        if(parametres.getTheme() != null)
        {
            query += " WHERE themes.theme = \"" + parametres.getTheme() + "\"";
        }
        query += " ORDER BY RANDOM() LIMIT " + parametres.getNombreDeQuestions();
        return query;
    }

    public List<Question> getNouveauQuiz(Parametres parametres) {
        String         query         = construireRequete(parametres);
        Cursor         curseur       = sqlite.rawQuery(query, null);
        List<Question> listeQuestion = new ArrayList<>();
        while(curseur.moveToNext())
        {
            listeQuestion.add(genererQuestion(curseur));
        }
        curseur.close();
        return listeQuestion;
    }

    public List<Participant> getParticipants() {
        Cursor curseur = sqlite.rawQuery("SELECT nom FROM participants ORDER BY nom ASC", null);
        List<Participant> listeParticipants = new ArrayList<>();
        while(curseur.moveToNext())
        {
            String nom = curseur.getString(curseur.getColumnIndexOrThrow("nom"));
            listeParticipants.add(new Participant(nom));
        }
        curseur.close();
        return listeParticipants;
    }

    public List<String> getThemes() {
        Cursor       curseur     = sqlite.rawQuery("SELECT * FROM themes", null);
        List<String> listeThemes = new ArrayList<>();
        while(curseur.moveToNext())
        {
            String nom = curseur.getString(curseur.getColumnIndexOrThrow("theme"));
            listeThemes.add(nom);
        }
        curseur.close();
        return listeThemes;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
