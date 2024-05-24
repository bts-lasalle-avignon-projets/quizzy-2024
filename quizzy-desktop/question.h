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
    int                 reponseCorrecte;

  public:
    Question(QString libelle, QStringList propositions, int reponseCorrecte);
    ~Question();

    QString             getLibelle() const;
    QMap<char, QString> getPropositions() const;
    int                 getDuree() const;
    void                setDuree(int temps);
    int                 getReponseCorrecte() const;
};

#endif // QUESTION_H
