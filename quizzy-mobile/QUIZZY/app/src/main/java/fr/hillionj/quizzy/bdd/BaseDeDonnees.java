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
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Vector;

/**
 * @class BaseDeDonnees
 * @brief La classe assurant la gestion de la base de données SQLite "QUIZZY.db"
 */
public class BaseDeDonnees extends SQLiteOpenHelper
{
    /**
     * Constantes
     */
    private static final String TAG                = "_BaseDeDonnees"; //!< TAG pour les logs
    private static final String QUIZZY_BDD         = "QUIZZY.db"; //!< Nom de la base de données
    private static final int    VERSION_QUIZZY_BDD = 1; //!< Version de la base de données

    /**
     * Attributs
     */
    private static BaseDeDonnees baseDeDonnees =
      null;                               //!< Instance unique de BaseDeDonnees (singleton)
    private SQLiteDatabase sqlite = null; //<! Accès à la base de données SQLite

    /**
     * @brief Constructeur de la classe BaseDeDonnees
     */
    private BaseDeDonnees(Context context)
    {
        super(context, QUIZZY_BDD, null, VERSION_QUIZZY_BDD);
        Log.d(TAG, "BaseDeDonnees()");
        if(sqlite == null)
            sqlite = this.getWritableDatabase();
    }

    /**
     * @fn getInstance
     * @brief Retourne l'instance BaseDeDonnees
     */
    public synchronized static BaseDeDonnees getInstance(Context context)
    {
        if(baseDeDonnees == null)
        {
            baseDeDonnees = new BaseDeDonnees(context);
        }
        return baseDeDonnees;
    }

    // Exemples de requêtes SQL

    /**
     * @brief Récupérer une liste d'enregistrements
     */
    public ArrayList<String> getListeEnregistrements()
    {
        ArrayList<String> listeEnregistrements = new ArrayList<String>();
        Cursor            curseur              = sqlite.rawQuery("SELECT * FROM participants", null);

        if(curseur.moveToFirst())
        {
            do
            {
                // int colonne = curseur.getColumnIndex("nomColonne");
                // int data = curseur.getInt(0);
                String enregistrement = curseur.getString(0);
                // ...

                listeEnregistrements.add(enregistrement);
            } while(curseur.moveToNext());
        }
        curseur.close();

        Log.d(TAG, "getListeEnregistrements() " + listeEnregistrements);
        return listeEnregistrements;
    }

    /**
     * @brief Renvoie un vecteur de string contenant le noms des participants enregistrés
     */
    public Vector<String> getNomsParticipants()
    {
        Log.d(TAG, "getNomsJoueursTries()");

        Cursor curseur = sqlite.rawQuery("SELECT nom FROM participants ORDER BY nom ASC", null);

        Vector<String> listeParticipants = new Vector<>();

        while(curseur.moveToNext())
        {
            String nom = curseur.getString(curseur.getColumnIndexOrThrow("nom"));
            listeParticipants.add(nom);
        }
        curseur.close();

        return listeParticipants;
    }

    /**
     * @brief Inserer un enregistrement dans une table
     */
    public void inserer(String data)
    {
        try
        {
            Log.d(TAG, "inserer(" + data + ")");
            sqlite.execSQL("INSERT INTO ... VALUES ('" + data + "')");
        }
        catch(SQLiteConstraintException e)
        {
            Log.e(TAG, "Erreur insertion !");
        }
    }

    /**
     * @brief Actualiser les données d'un enregistrement
     */
    private void actualiser(String id, String data)
    {
        sqlite.execSQL("UPDATE ... SET data = '" + data + "' WHERE id = '" + id + "'");
    }

    /**
     * @brief Supprime un enregistrement
     */
    public void supprimerJoueur(String nom)
    {
        sqlite.execSQL("DELETE FROM participants WHERE nom = ?", new String[] { nom });
    }

    /**
     * @brief Crée les différentes tables de la base de données
     */
    @Override
    public void onCreate(SQLiteDatabase sqlite)
    {
        Log.d(TAG, "onCreate()");
        sqlite.execSQL(
          "CREATE TABLE IF NOT EXISTS evaluateurs (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT UNIQUE NOT NULL);");
        sqlite.execSQL(
          "CREATE TABLE IF NOT EXISTS participants (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT UNIQUE NOT NULL);");
        sqlite.execSQL(
          "CREATE TABLE IF NOT EXISTS niveaux (id INTEGER PRIMARY KEY AUTOINCREMENT, difficulte TEXT UNIQUE NOT NULL);");
        // --- le nom et la difficulté d'une évaluation réalisée par un évaluateur
        sqlite.execSQL(
          "CREATE TABLE IF NOT EXISTS evaluations (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT NOT NULL, idDifficulte INTEGER, idEvaluateur INTEGER, horodatage DATETIME NOT NULL, FOREIGN KEY (idDifficulte) REFERENCES niveaux(id), FOREIGN KEY (idEvaluateur) REFERENCES evaluateurs(id) ON DELETE CASCADE);");
        // --- le libellé d'une question avec le nombre de points attribués et le temps pour
        // répondre
        sqlite.execSQL(
          "CREATE TABLE IF NOT EXISTS questions (id INTEGER PRIMARY KEY AUTOINCREMENT, libelle TEXT NOT NULL, points DOUBLE, temps INTEGER);");
        // --- le libellé d'une proposition
        sqlite.execSQL(
          "CREATE TABLE IF NOT EXISTS propositions (id INTEGER PRIMARY KEY AUTOINCREMENT, libelle TEXT NOT NULL);");
        // --- réponse d'une proposition d'un participant à une question d'un questionnaire (avec le
        // temps mis pour répondre et s'il a bien répondu)
        sqlite.execSQL(
          "CREATE TABLE IF NOT EXISTS reponses (idEvaluation INTEGER, idParticipant INTEGER, idQuestion INTEGER, idProposition INTEGER, temps INTEGER, correct BOOLEAN NOT NULL CHECK (correct IN (0, 1)), FOREIGN KEY (idEvaluation) REFERENCES evaluations(id) ON DELETE CASCADE, FOREIGN KEY (idParticipant) REFERENCES participants(id) ON DELETE CASCADE, FOREIGN KEY (idProposition) REFERENCES propositions(id));");
        // --- une question d'un questionnaire d'une évaluation avec les propositions ainsi que la
        // bonne réponse à fournir
        sqlite.execSQL(
          "CREATE TABLE IF NOT EXISTS questionnaires (idEvaluation INTEGER, idQuestion INTEGER, idProposition INTEGER, correct BOOLEAN NOT NULL CHECK (correct IN (0, 1)), FOREIGN KEY (idEvaluation) REFERENCES evaluations(id) ON DELETE CASCADE, FOREIGN KEY (idProposition) REFERENCES propositions(id));");
        // --- les résultats des participants à une évaluation
        sqlite.execSQL(
          "CREATE TABLE IF NOT EXISTS resultats (id INTEGER PRIMARY KEY AUTOINCREMENT, horodatage DATETIME NOT NULL, idEvaluation INTEGER, idParticipant INTEGER, idPupitre INTEGER, score INTEGER, FOREIGN KEY (idEvaluation) REFERENCES evaluations(id) ON DELETE CASCADE, FOREIGN KEY (idParticipant) REFERENCES participants(id) ON DELETE CASCADE);");

        initialiserBaseDeDonnees(sqlite);
    }

    /**
     * @brief Ajoute des données initiales dans la base de données
     */
    private void initialiserBaseDeDonnees(SQLiteDatabase sqlite)
    {
        Log.d(TAG, "initialiserBaseDeDonnees()");
        sqlite.execSQL("INSERT INTO niveaux(difficulte) VALUES ('facile');");
        sqlite.execSQL("INSERT INTO niveaux(difficulte) VALUES ('moyen');");
        sqlite.execSQL("INSERT INTO niveaux(difficulte) VALUES ('difficile');");

        // pour les tests seulement
        initialiserTestBaseDeDonnees(sqlite);
    }

    /**
     * @brief Ajoute des données initiales dans la base de données pour les tests
     */
    private void initialiserTestBaseDeDonnees(SQLiteDatabase sqlite)
    {
        Log.d(TAG, "initialiserTestBaseDeDonnees()");
        // Pour les tests
        sqlite.execSQL("INSERT INTO evaluateurs(nom) VALUES ('Thierry VAIRA');");
        sqlite.execSQL("INSERT INTO evaluateurs(nom) VALUES ('Jérôme BEAUMONT');");

        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('ARMANDO Célian');");
        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('AVRIL Gabain');");
        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('BROUSSE Antoine');");
        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('GUALEZZI Mattéo');");
        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('HILLION Jules');");
        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('HNIZDO Thomas');");
        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('LATYAOUI Othman');");
        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('LERENARD Axelle');");
        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('MATARISE Alexis');");
        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('MDOIOUHOMA Nakib');");
        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('MOUTTE Corentin');");
        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('OUTFLATE Hamza');");
        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('SAUGET Jean-Max');");
        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('VALOBRA Enzo');");
        sqlite.execSQL("INSERT INTO participants(nom) VALUES ('VIGNAL Thomas');");

        sqlite.execSQL(
          "INSERT INTO evaluations (nom, idDifficulte, idEvaluateur, horodatage) VALUES ('Les bases en C/C++', 2, 1, DATETIME('now'));");
        sqlite.execSQL(
          "INSERT INTO evaluations (nom, idDifficulte, idEvaluateur, horodatage) VALUES ('Révisions C/C++', 1, 1, DATETIME('now'));");

        sqlite.execSQL(
          "INSERT INTO questions (libelle, points, temps) VALUES ('Quel opérateur utilise-t-on pour tester l''égalité entre deux variables en C++ ?', 1, 20);");
        sqlite.execSQL(
          "INSERT INTO questions (libelle, points, temps) VALUES ('Lequel de ces types de données permet de stocker le nombre 76.8 ?', 0.5, 20);");
        sqlite.execSQL(
          "INSERT INTO questions (libelle, points, temps) VALUES ('Lequel de ces types de données peut stocker le nombre -1000 ?', 0.5, 20);");
        sqlite.execSQL(
          "INSERT INTO questions (libelle, points, temps) VALUES ('Lequel de ces types de données peut stocker la chaîne de caractères \"titi\" ?', 1, 20);");
        sqlite.execSQL(
          "INSERT INTO questions (libelle, points, temps) VALUES ('Combien de nombres au total peut-on coder avec un short int (16 bits) ?', 2, 20);");
        sqlite.execSQL(
          "INSERT INTO questions (libelle, points, temps) VALUES ('Qu''affiche l''instruction suivante ?<br>i = 2; j = 3; cout << i++ << \" \" << ++j;', 5, 20);");
        sqlite.execSQL(
          "INSERT INTO questions (libelle, points, temps) VALUES ('Qu''affiche l''instruction suivante ?<br>cout << \"j''aime le C++ \" << 30/20 << \" fois\";', 5, 20);");

        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('<>');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('!=');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('==');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('=');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('char');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('double');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('long');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('unsigned int');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('unsigned double');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('string');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('int');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('255');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('32768');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('65535');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('65536');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('2 3');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('3 4');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('2 4');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('3 3');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('j''aime le C++ 0 fois');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('j''aime le C++ 1 fois');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('j''aime le C++ 1.5 fois');");
        sqlite.execSQL("INSERT INTO propositions (libelle) VALUES('j''aime le C++ 2 fois');");

        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 1, 1, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 1, 2, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 1, 3, 1);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 1, 4, 0);");

        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 2, 5, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 2, 6, 1);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 2, 7, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 2, 8, 0);");

        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 3, 5, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 3, 9, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 3, 7, 1);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 3, 8, 0);");

        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 4, 5, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 4, 6, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 4, 10, 1);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 4, 11, 0);");

        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 5, 12, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 5, 13, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 5, 14, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 5, 15, 1);");

        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 1, 16, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 1, 17, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 1, 18, 1);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 1, 19, 0);");

        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 2, 20, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 2, 21, 1);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 2, 22, 0);");
        sqlite.execSQL(
          "INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 2, 23, 0);");

        sqlite.execSQL(
          "INSERT INTO reponses (idEvaluation, idParticipant, idQuestion, idProposition, temps, correct) VALUES(2, 1, 4, 1, 19, 0);");
        sqlite.execSQL(
          "INSERT INTO reponses (idEvaluation, idParticipant, idQuestion, idProposition, temps, correct) VALUES(2, 1, 2, 1, 2, 0);");
        sqlite.execSQL(
          "INSERT INTO reponses (idEvaluation, idParticipant, idQuestion, idProposition, temps, correct) VALUES(2, 5, 4, 3, 5, 1);");
        sqlite.execSQL(
          "INSERT INTO reponses (idEvaluation, idParticipant, idQuestion, idProposition, temps, correct) VALUES(2, 5, 2, 2, 3, 1);");

        sqlite.execSQL(
          "INSERT INTO resultats (horodatage, idEvaluation, idParticipant, idPupitre, score) VALUES ('2024-04-08 08:00:00', 2, 1, 1, 0);");
        sqlite.execSQL(
          "INSERT INTO resultats (horodatage, idEvaluation, idParticipant, idPupitre, score) VALUES ('2024-04-08 08:00:00', 2, 5, 2, 10);");
    }

    /**
     * @brief Supprime les tables existantes pour en recréer des vierges
     */
    public void effacer()
    {
        Log.d(TAG, "effacer()");
        onUpgrade(sqlite, sqlite.getVersion(), sqlite.getVersion() + 1);
    }

    /**
     * @brief Supprimer les tables existantes pour en recréer des vierges
     * @warning le plus simple est de supprimer l'application puis de la réinstaller !
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqlite, int oldVersion, int newVersion)
    {
        Log.d(TAG, "onUpgrade()");
        // sqlite.execSQL("DROP TABLE IF EXISTS ...");
        sqlite.execSQL("DROP TABLE IF EXISTS resultats;");
        sqlite.execSQL("DROP TABLE IF EXISTS questions;");
        sqlite.execSQL("DROP TABLE IF EXISTS questionnaires;");
        sqlite.execSQL("DROP TABLE IF EXISTS evaluations;");
        sqlite.execSQL("DROP TABLE IF EXISTS propositions;");
        sqlite.execSQL("DROP TABLE IF EXISTS reponses;");
        sqlite.execSQL("DROP TABLE IF EXISTS participants;");
        sqlite.execSQL("DROP TABLE IF EXISTS niveaux;");
        sqlite.execSQL("DROP TABLE IF EXISTS evaluateurs;");
        sqlite.setVersion(newVersion);
        onCreate(sqlite);
    }
}
