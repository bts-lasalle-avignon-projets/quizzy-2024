#ifndef QUESTION_H
#define QUESTION_H

#include <QString>
#include <QMap>

class Question
{
  private:
    QString             libelle;
    QStringList         participants;
    QMap<char, QString> propositions;
    int                 duree;
    char                reponseCorrecte;

  public:
    Question(QString libelle, QStringList propositions);
    ~Question();

    QString             getLibelle() const;
    QMap<char, QString> getPropositions() const;
    int                 getDuree() const;
};

#endif // QUESTION_H
