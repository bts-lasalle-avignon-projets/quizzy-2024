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

  private:
    QVector<Participant*>   participants;
    QVector<Question*>      listeQuestions;
    int                     indexQuestionActuelle;
    Etat                    etat;
    CommunicationBluetooth* communicationTablette;

    void initialiserCommunicationTablette();

  public:
    Quizzy(QObject* parent = nullptr);
    ~Quizzy();

    // Gestion du quiz
    void debuter(bool reset = true);
    void lancer();

    // Gestion des participants
    bool estParticipantActuel(QString pidJoueur) const;

    // Gestion des questions

    // Gestion des réponses
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
    // Gestion du quiz
    void gererDebutQuiz();
    // Gestion des participants
    void ajouterParticipant(QString pidJoueur, QString participant);
    void ajouterQuestion(QString     libelle,
                         QStringList propositions,
                         int         reponseValide,
                         int         temps);
    void demarrerQuestion();
    void terminerQuestion();
    void traiterReponse(QString pidJoueur, int numeroReponse, int tempsReponse);

  signals:
    void debutQuiz();
    void lancementQuiz();
    void participantAjoute(QString pidJoueur, QString nomParticipant);
    void questionAjoutee();
    void questionDemarree();
    void questionTerminee();
};

#endif // QUIZZY_H
