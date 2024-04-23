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
  private:
    QVector<Participant*> participants;
    QVector<Question*>    listeQuestions;
    int                   indexQuestionActuelle;
    CommunicationBluetooth*
      communicationTablette; //!< association vers la classe
                             //!< CommunicationBluetooth

    void initialiserCommunicationTablette();

  public:
    Quizzy(QObject* parent = nullptr);
    ~Quizzy();

    void debuter();
    void ajouterParticipant(QString pidJoueur, QString nomParticipant);
    CommunicationBluetooth* getCommunicationTablette();
};

#endif // QUIZZY_H
