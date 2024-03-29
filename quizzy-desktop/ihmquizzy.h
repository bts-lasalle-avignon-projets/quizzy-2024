#ifndef IHMQUIZZY_H
#define IHMQUIZZY_H

#include "quizzy.h"

#include <QWidget>

class IHMQuizzy : public QWidget
{
    Q_OBJECT

  private:
    Quizzy* quizzy;

  public:
    IHMQuizzy(QWidget* parent = 0);
    ~IHMQuizzy();
};

#endif // IHMQUIZZY_H
