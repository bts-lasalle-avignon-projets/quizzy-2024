#ifndef QUIZZY_H
#define QUIZZY_H

#include <QObject>
#include <QVector>

#define INDEX_NON_DEFINI -1

class Question;

class Quizzy : public QObject
{
    Q_OBJECT
  private:
    QVector<Question*> listeQuestions;
    int                indexQuestionActuelle;

  public:
    Quizzy(QObject* parent = nullptr);
    ~Quizzy();
};

#endif // QUIZZY_H
