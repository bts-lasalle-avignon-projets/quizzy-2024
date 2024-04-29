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
        Resultats,
        QuizFini
    };

  private:
    QVector<Participant*> participants;
    QVector<Question*>    listeQuestions;
    int                   indexQuestionActuelle;
    Etat                  etat;
    CommunicationBluetooth*
      communicationTablette; //!< association vers la classe
                             //!< CommunicationBluetooth

    void initialiserCommunicationTablette();

  public:
    Quizzy(QObject* parent = nullptr);
    ~Quizzy();

    void         debuter(bool reset = true);
    void         lancer();
    bool         ajouterParticipant(QString pidJoueur, QString nomParticipant);
    void         ajouterQuestion(QString     libelle,
                                 QStringList propositions,
                                 int         reponseValide,
                                 int         temps);
    unsigned int getNbQuestions();
    unsigned int getNbParticipants();
    Question*    getQuestion();
    Etat         getEtat() const;
    int          getIndexQuestionActuelle() const;
    CommunicationBluetooth* getCommunicationTablette();

  signals:
    void affichagePremiereQuestion();

  public slots:
    void gererDebutQuiz();
};

#endif // QUIZZY_H
