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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.hillionj.quizzy.parametres.Parametres;
import fr.hillionj.quizzy.parametres.receveur.speciales.Participant;
import fr.hillionj.quizzy.session.contenu.Question;
import fr.hillionj.quizzy.session.Session;
import fr.hillionj.quizzy.session.contenu.Theme;

@SuppressWarnings({ "SpellCheckingInspection", "unused", "SdCardPath" })
public class BaseDeDonnees extends SQLiteOpenHelper
{
    private static final String nomFichier = "quizzy.db";

    private final SQLiteDatabase sqlite;

    public BaseDeDonnees(Context context)
    {
        super(context, nomFichier, null, 1);
        new CopieurBDD(nomFichier, context).verifier();
        this.sqlite = this.getWritableDatabase();
    }

    @NonNull
    private Question genererQuestion(Cursor curseur)
    {
        int idQuestion = curseur.getInt(curseur.getColumnIndexOrThrow("idQuestion"));
        String question = curseur.getString(curseur.getColumnIndexOrThrow("question"));
        String prop1    = curseur.getString(curseur.getColumnIndexOrThrow("proposition1"));
        String prop2    = curseur.getString(curseur.getColumnIndexOrThrow("proposition2"));
        String prop3    = curseur.getString(curseur.getColumnIndexOrThrow("proposition3"));
        String prop4    = curseur.getString(curseur.getColumnIndexOrThrow("proposition4"));

        List<String> propositions = Arrays.asList(prop1, prop2, prop3, prop4);

        int nombreCarateres = (question + prop1 + prop2 + prop3 + prop4).length();
        int tempsReponse = Parametres.getParametres().estCalulAutomatiqueDuTempsDeReponse() ? getTempsReponse(-1, nombreCarateres) : Parametres.getParametres().getTempsReponse();

        return new Question(idQuestion, question, propositions, tempsReponse);
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
            query += " WHERE themes.theme = \"" + parametres.getTheme().getNom() + "\"";
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
        Cursor curseur = sqlite.rawQuery("SELECT * FROM participants ORDER BY nom ASC", null);
        List<Participant> listeParticipants = new ArrayList<>();
        while(curseur.moveToNext())
        {
            int idParticipant = curseur.getInt(curseur.getColumnIndexOrThrow("idParticipant"));
            String nom = curseur.getString(curseur.getColumnIndexOrThrow("nom"));
            listeParticipants.add(new Participant(idParticipant, nom));
        }
        curseur.close();
        return listeParticipants;
    }

    public List<Theme> getThemes() {
        Cursor       curseur     = sqlite.rawQuery("SELECT * FROM themes", null);
        List<Theme> listeThemes = new ArrayList<>();
        while(curseur.moveToNext())
        {
            int idTheme = curseur.getInt(curseur.getColumnIndexOrThrow("idTheme"));
            String nom = curseur.getString(curseur.getColumnIndexOrThrow("theme"));
            listeThemes.add(new Theme(idTheme, nom));
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

    public void sauvegarder(@NonNull Session session) {
        int idEvaluation = getIdNouvelleEvaluation(session.getTheme().getIdTheme());
        for (Question question : session.getQuestions()) {
            sqlite.execSQL("INSERT INTO quiz (idEvaluation, idQuestion, points, temps) VALUES (" + idEvaluation + ", " + question.getIdQuestion() + ", 1, " + question.getTempsReponse() + ")");
            for (Participant participant : session.getParticipants()) {
                sqlite.execSQL("INSERT INTO reponses (idEvaluation, idParticipant, idQuestion, temps, correct) VALUES (" + idEvaluation + ", " + participant.getIdParticipant() + ", " + question.getIdQuestion() + ", " + question.getIdQuestion() + ", " + question.estPropositionValide(participant) + ")");
            }
        }
        for (Participant participant : session.getParticipants()) {
            sqlite.execSQL("INSERT INTO resultats (idEvaluation, idParticipant, score) VALUES (" + idEvaluation + ", " + participant.getIdParticipant() + ", " + session.getScore(participant) + ")");
        }
    }

    private int getIdNouvelleEvaluation(int idTheme) {
        String horodatage = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Calendar.getInstance().getTime());

        sqlite.execSQL("INSERT INTO evaluations (idTheme, horodatage) VALUES (" + idTheme + ", '" + horodatage + "')");

        Cursor       curseur     = sqlite.rawQuery("SELECT idEvaluation FROM evaluations WHERE horodatage = '" + horodatage + "'", null);
        List<Theme> listeThemes = new ArrayList<>();
        curseur.moveToNext();
        return curseur.getInt(curseur.getColumnIndexOrThrow("idEvaluation"));
    }

    public List<Session> getHistorique() {
        List<Session> sessions = new ArrayList<>();
        for(Object[] evaluation : getEvaluations("")) {
            int idEvaluation = (int) evaluation[0];
            int idTheme = (int) evaluation[1];
            String horodatage = (String) evaluation[2];
            sessions.add(getSession(idEvaluation, idTheme, horodatage));
        }
        return sessions;
    }

    private List<Object[]> getEvaluations(String argumentSupplementaire) {
        List<Object[]> evaluations = new ArrayList<>();
        Cursor       curseur     = sqlite.rawQuery("SELECT * FROM evaluations" + argumentSupplementaire, null);
        while(curseur.moveToNext())
        {
            int idEvaluation = curseur.getInt(curseur.getColumnIndexOrThrow("idEvaluation"));
            int idTheme = curseur.getInt(curseur.getColumnIndexOrThrow("idTheme"));
            String horodatage = curseur.getString(curseur.getColumnIndexOrThrow("horodatage"));
            evaluations.add(new Object[] { idEvaluation, idTheme, horodatage });
        }
        curseur.close();
        return evaluations;
    }

    public Session getSession(int idEvaluation) {
        Object[] evaluation = getEvaluations(" WHERE idEvaluation = '" + idEvaluation + "'").get(0);
        return getSession(idEvaluation, (int) evaluation[1], (String) evaluation[2]);
    }

    private Session getSession(int idEvaluation, int idTheme, String horodatage) {
        List<Participant> particpants = getParticipants(idEvaluation);
        List<Question> questions = getQuestions(idEvaluation);
        for (Question question : questions) {
            Cursor curseur = sqlite.rawQuery("SELECT * FROM reponses WHERE reponses.idEvaluation = " + idEvaluation + " AND reponses.idQuestion = " + question.getIdQuestion(), null);
            while(curseur.moveToNext()) {
                int idParticipant = curseur.getInt(curseur.getColumnIndexOrThrow("idParticipant"));
                int temps = curseur.getInt(curseur.getColumnIndexOrThrow("temps"));
                boolean correct = curseur.getInt(curseur.getColumnIndexOrThrow("correct")) == 1;
                Participant participant = getParticipantDepuisID(idParticipant, particpants);
                if (correct) {
                    question.ajouterSelection(participant, 0);
                }
            }
        }
        return new Session(idEvaluation, horodatage, questions, particpants);
    }

    private List<Participant> getParticipants(int idEvaluation) {
        List<Participant> participants = new ArrayList<>();
        Cursor curseur = sqlite.rawQuery("SELECT resultats.idParticipant, participants.nom FROM resultats INNER JOIN participants ON resultats.idParticipant = participants.idParticipant WHERE resultats.idEvaluation = " + idEvaluation, null);
        while(curseur.moveToNext()) {
            int idParticipant = curseur.getInt(curseur.getColumnIndexOrThrow("idParticipant"));
            String nom = curseur.getString(curseur.getColumnIndexOrThrow("nom"));
            participants.add(new Participant(idParticipant, nom));
        }

        return participants;
    }

    private Participant getParticipantDepuisID(int idParticipant, List<Participant> participants) {
        for(Participant participant : participants) {
            if(participant.getIdParticipant() == idParticipant) {
                return participant;
            }
        }
        return null;
    }

    private List<Question> getQuestions(int idEvaluation) {
        List<Question> questions = new ArrayList<>();
        Cursor curseur = sqlite.rawQuery("SELECT questions.question, quiz.* FROM quiz INNER JOIN questions ON questions.idQuestion = quiz.idQuestion WHERE quiz.idEvaluation = " + idEvaluation, null);
        while(curseur.moveToNext()) {
            int idQuestion = curseur.getInt(curseur.getColumnIndexOrThrow("idQuestion"));
            String question = curseur.getString(curseur.getColumnIndexOrThrow("question"));
            int temps = curseur.getInt(curseur.getColumnIndexOrThrow("temps"));
            questions.add(new Question(idQuestion, question, null, temps));
        }
        return questions;
    }

    public void supprimerSession(Session session) {
        sqlite.execSQL("DELETE FROM resultats WHERE idEvaluation = " + session.getIdEvaluation());
        sqlite.execSQL("DELETE FROM reponses WHERE idEvaluation = " + session.getIdEvaluation());
        sqlite.execSQL("DELETE FROM quiz WHERE idEvaluation = " + session.getIdEvaluation());
        sqlite.execSQL("DELETE FROM evaluations WHERE idEvaluation = " + session.getIdEvaluation());
    }

    public Participant creerParticipant(String nom) {
        sqlite.execSQL("INSERT INTO participants (nom) VALUES ('" + nom + "')");

        Cursor       curseur     = sqlite.rawQuery("SELECT idParticipant FROM participants WHERE nom = '" + nom + "'", null);
        curseur.moveToNext();
        int idParticipant = curseur.getInt(curseur.getColumnIndexOrThrow("idParticipant"));
        return new Participant(idParticipant, nom);
    }
}
