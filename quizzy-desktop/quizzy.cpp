#include "quizzy.h"
#include "question.h"
#include "communicationbluetooth.h"

#include <QDebug>

Quizzy::Quizzy(QObject* parent) :
    QObject(parent), indexQuestionActuelle(INDEX_NON_DEFINI),
    bluetooth(new CommunicationBluetooth(this))
{
    qDebug() << Q_FUNC_INFO;
}

Quizzy::~Quizzy()
{
    qDebug() << Q_FUNC_INFO;
}
