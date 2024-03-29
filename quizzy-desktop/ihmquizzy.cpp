#include "ihmquizzy.h"
#include "quizzy.h"

IHMQuizzy::IHMQuizzy(QWidget* parent) : QWidget(parent)
{
    quizzy = new Quizzy();
}

IHMQuizzy::~IHMQuizzy()
{
    delete quizzy;
}
