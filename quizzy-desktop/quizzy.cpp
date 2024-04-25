#include "quizzy.h"
#include "participant.h"
#include "question.h"
#include "communicationbluetooth.h"

#include <QDebug>

Quizzy::Quizzy(QObject* parent) :
    QObject(parent), indexQuestionActuelle(INDEX_NON_DEFINI),
    communicationTablette(new CommunicationBluetooth(this))
{
    qDebug() << Q_FUNC_INFO;
    initialiserCommunicationTablette();
}

Quizzy::~Quizzy()
{
    qDebug() << Q_FUNC_INFO;
}

void Quizzy::initialiserCommunicationTablette()
{
    qDebug() << Q_FUNC_INFO;

    communicationTablette->demarrerServeur();
}

void Quizzy::debuter()
{
    participants.clear();
    listeQuestions.clear();
}

void Quizzy::ajouterParticipant(QString pidJoueur, QString nomParticipant)
{
    Participant* participant =
      new Participant(nomParticipant, pidJoueur.toInt());
    participants.push_back(participant);
}

void Quizzy::afficherNouvelleQuestion(QString     libelle,
                                      QStringList propositions,
                                      int         reponseValide,
                                      int         temps)
{
    Question* question = new Question(libelle, propositions);
    listeQuestions.append(question);
}

CommunicationBluetooth* Quizzy::getCommunicationTablette()
{
    return communicationTablette;
}
