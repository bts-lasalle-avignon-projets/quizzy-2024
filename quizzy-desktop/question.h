#ifndef QUESTION_H
#define QUESTION_H

#include <QString>
#include <QMap>

class Question
{
  private:
    QString             libelle;
    QMap<char, QString> propositions;
    int                 duree;
    char                reponseCorrecte;

  public:
    Question();
    ~Question();
};

#endif // QUESTION_H
