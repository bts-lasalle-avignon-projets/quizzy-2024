#ifndef QUIZZY_H
#define QUIZZY_H

#include <QObject>
#include <QVector>

#define INDEX_NON_DEFINI -1

class Participant;
class Question;
class CommunicationBluetooth;

class Quizzy : public QObject
{
    Q_OBJECT

  public:
    // Enumérations
    enum Etat
    {
        Initial,
        QuizDemarre,
        ParticipantsAjoutes,
        QuestionsAjoutees,
        QuizLance,
        QuestionDemarree,
        QuestionTerminee,
        Resultats,
        QuizFini
    };

    // Constructeur et destructeur
    Quizzy(QObject* parent = nullptr);
    ~Quizzy();

    // Gestion du quiz
    void debuter(bool reset = true);
    void lancer();

    // Gestion des participants
    bool ajouterParticipant(QString pidJoueur, QString nomParticipant);
    bool estParticipantActuel(QString pidJoueur) const;

    // Gestion des questions
    void ajouterQuestion(QString     libelle,
                         QStringList propositions,
                         int         reponseValide,
                         int         temps);
    bool demarrerQuestion();
    bool terminerQuestion();

    // Gestion des réponses
    bool traiterReponse(QString pidJoueur, int numeroReponse, int tempsReponse);
    bool traiterReponseParticipant(Participant* participant,
                                   int          numeroReponse,
                                   int          tempsReponse);

    // Getters
    unsigned int            getNbQuestions();
    unsigned int            getNbParticipants();
    Question*               getQuestion();
    Etat                    getEtat() const;
    int                     getIndexQuestionActuelle() const;
    QString                 getNomDuParticipant(QString pidJoueur);
    CommunicationBluetooth* getCommunicationTablette();

  public slots:
    void gererDebutQuiz();

  signals:
    void affichagePremiereQuestion();

  private:
    void initialiserCommunicationTablette();

    // Variables
    QVector<Participant*>   participants;
    QVector<Question*>      listeQuestions;
    int                     indexQuestionActuelle;
    Etat                    etat;
    CommunicationBluetooth* communicationTablette;
};

#endif // QUIZZY_H
