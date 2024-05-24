/**
 * @author Thierry VAIRA
 * @author Jules HILLION
 * @file BaseDeDonnees.java
 * @brief La classe assurant la gestion de la base de données SQLite
 */

package fr.hillionj.quizzy.bdd;

import static android.provider.MediaStore.getVersion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import fr.hillionj.quizzy.questionnaire.Question;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "SdCardPath" })
public class BaseDeDonnees extends SQLiteOpenHelper
{
    private static final String TAG                = "_BaseDeDonnees"; //!< TAG pour les logs
    private static final String QUIZZY_BDD         = "quizzy.db"; //!< Nom de la base de données
    private static final int    VERSION_QUIZZY_BDD = 1; //!< Version de la base de données

    private static BaseDeDonnees baseDeDonnees =
      null;                               //!< Instance unique de BaseDeDonnees (singleton)
    private SQLiteDatabase sqlite = null; //<! Accès à la base de données SQLite
    private final String   QUIZZY_CHEMIN;
    private final Context  context;

    private BaseDeDonnees(Context context)
    {
        super(context, QUIZZY_BDD, null, VERSION_QUIZZY_BDD);
        Log.d(TAG, "BaseDeDonnees()");

        QUIZZY_CHEMIN = "/data/data/" + context.getPackageName() + "/databases/";
        this.context  = context;

        verifierBaseDeDonnes();
    }

    private void copierOctets(InputStream input, OutputStream output) throws IOException
    {
        byte[] buffer = new byte[1024];
        int length;
        while((length = input.read(buffer)) > 0)
        {
            output.write(buffer, 0, length);
        }
    }

    private void fermerLesFlux(OutputStream output, InputStream input)
    {
        if(output != null)
        {
            try
            {
                output.close();
            }
            catch(IOException e)
            {
                Log.e(TAG, e.getMessage(), e);
            }
        }
        if(input != null)
        {
            try
            {
                input.close();
            }
            catch(IOException e)
            {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }

    @NonNull
    private String construireRequete(int nombreQuestion, String theme)
    {
        String query =
          "SELECT question,proposition1,proposition2,proposition3,proposition4 FROM questions";
        if(theme != null)
        {
            query += " WHERE theme = '" + theme + "'";
        }
        query += " ORDER BY RANDOM() LIMIT " + nombreQuestion;
        return query;
    }

    private Question genererQuestion(Cursor curseur, int tempsParQuestion)
    {
        String question = curseur.getString(curseur.getColumnIndexOrThrow("question"));
        String prop1    = curseur.getString(curseur.getColumnIndexOrThrow("proposition1"));
        String prop2    = curseur.getString(curseur.getColumnIndexOrThrow("proposition2"));
        String prop3    = curseur.getString(curseur.getColumnIndexOrThrow("proposition3"));
        String prop4    = curseur.getString(curseur.getColumnIndexOrThrow("proposition4"));

        List<String> propositions = getPropositions(prop1, prop2, prop3, prop4);

        int tempsReponse = getTempsReponse(tempsParQuestion, question, prop1, prop2, prop3, prop4);

        return new Question(question, propositions, tempsReponse);
    }

    @NonNull
    private List<String> getPropositions(String prop1, String prop2, String prop3, String prop4)
    {
        List<String> propositions = new ArrayList<>();
        propositions.add(prop1);
        propositions.add(prop2);
        propositions.add(prop3);
        propositions.add(prop4);
        return propositions;
    }

    private int getTempsReponse(int    tempsParQuestion,
                                String question,
                                String prop1,
                                String prop2,
                                String prop3,
                                String prop4)
    {
        int tempsReponse = tempsParQuestion;
        if(tempsReponse == -1)
        {
            tempsReponse = (question + prop1 + prop2 + prop3 + prop4).length() / 5;
            if(tempsReponse == 0)
            {
                tempsReponse++;
            }
        }
        return tempsReponse;
    }

    public synchronized static BaseDeDonnees getInstance()
    {
        return baseDeDonnees;
    }

    public synchronized static void initialiser(Context context)
    {
        baseDeDonnees = new BaseDeDonnees(context);
    }

    public List<String> getThemes()
    {
        Cursor       curseur     = sqlite.rawQuery("SELECT DISTINCT theme FROM questions", null);
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
    public void onCreate(SQLiteDatabase sqlite)
    {
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlite, int oldVersion, int newVersion)
    {
    }

    public void verifierBaseDeDonnes()
    {
        File fichierBaseDeDonnes = new File(QUIZZY_CHEMIN + QUIZZY_BDD);
        try
        {
            if(!fichierBaseDeDonnes.exists())
            {
                copierBaseDeDonnees(context.getAssets().open(QUIZZY_BDD), fichierBaseDeDonnes);
            }
        }
        catch(Exception e)
        {
            Log.e(TAG, e.getMessage(), e);
        }
        sqlite = this.getReadableDatabase();
    }

    public void copierBaseDeDonnees(InputStream source, File destination) throws IOException
    {
        InputStream  input  = null;
        OutputStream output = null;
        try
        {
            new File(QUIZZY_CHEMIN).mkdirs();
            input  = new BufferedInputStream(source);
            output = new BufferedOutputStream(new FileOutputStream(destination));
            copierOctets(input, output);
        }
        finally
        {
            fermerLesFlux(output, input);
        }
    }

    public List<String> getNomsParticipants()
    {
        Cursor curseur = sqlite.rawQuery("SELECT nom FROM participants ORDER BY nom ASC", null);

        List<String> listeParticipants = new Vector<>();

        while(curseur.moveToNext())
        {
            String nom = curseur.getString(curseur.getColumnIndexOrThrow("nom"));
            listeParticipants.add(nom);
        }
        curseur.close();

        return listeParticipants;
    }

    public List<Question> getQuestionnaire(int nombreQuestion)
    {
        return getQuestionnaire(nombreQuestion, null, -1);
    }

    public List<Question> getQuestionnaire(int nombreQuestion, String theme)
    {
        return getQuestionnaire(nombreQuestion, theme, -1);
    }

    public List<Question> getQuestionnaire(int nombreQuestion, int tempsParQuestion)
    {
        return getQuestionnaire(nombreQuestion, null, tempsParQuestion);
    }

    public List<Question> getQuestionnaire(int nombreQuestion, String theme, int tempsParQuestion)
    {
        String         query         = construireRequete(nombreQuestion, theme);
        Cursor         curseur       = sqlite.rawQuery(query, null);
        List<Question> listeQuestion = new ArrayList<>();
        while(curseur.moveToNext())
        {
            listeQuestion.add(genererQuestion(curseur, tempsParQuestion));
        }
        curseur.close();
        return listeQuestion;
    }
}
