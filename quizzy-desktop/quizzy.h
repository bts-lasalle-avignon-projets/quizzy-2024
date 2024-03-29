#ifndef QUIZZY_H
#define QUIZZY_H

#include <QVector>
#include "question.h"

class Quizzy
{
  private:
    QVector<Question*> listeQuestions;
    int                indexQuestionActuelle;

  public:
    Quizzy();
    ~Quizzy();
};

#endif // QUIZZY_H
