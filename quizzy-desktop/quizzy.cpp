#include "quizzy.h"
#include "question.h"
#include <QDebug>

Quizzy::Quizzy(QObject* parent) :
    QObject(parent), indexQuestionActuelle(INDEX_NON_DEFINI)
{
    qDebug() << Q_FUNC_INFO;
}

Quizzy::~Quizzy()
{
    qDebug() << Q_FUNC_INFO;
}
