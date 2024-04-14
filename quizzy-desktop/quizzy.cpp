#include "quizzy.h"
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
    // @todo Faire la connexion signal/slot des signaux émis par l'objet
    // communicationTablette

    // @todo Démarrer le serveur
}

CommunicationBluetooth* Quizzy::getCommunicationTablette()
{
    return communicationTablette;
}
