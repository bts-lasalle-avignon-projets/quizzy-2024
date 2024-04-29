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
        EtatInitial,
        EtatParticipantsAjoutes,
        EtatQuestionsAjoutees,
        EtatQuizLance
    };

    Quizzy(QObject* parent = nullptr);
    ~Quizzy();

    void         debuter();
    void         lancer();
    void         ajouterParticipant(QString pidJoueur, QString nomParticipant);
    void         ajouterQuestion(QString     libelle,
                                 QStringList propositions,
                                 int         reponseValide,
                                 int         temps);
    unsigned int getNbQuestions();
    unsigned int getNbParticipants();
    Question*    getQuestion();
    bool         estEncours() const;
    CommunicationBluetooth* getCommunicationTablette();

  private:
    QVector<Participant*> participants;
    QVector<Question*>    listeQuestions;
    int                   indexQuestionActuelle;
    bool                  enCours;
    Etat                  etat;
    CommunicationBluetooth*
      communicationTablette; //!< association vers la classe
                             //!< CommunicationBluetooth

    void initialiserCommunicationTablette();
};

#endif // QUIZZY_H
