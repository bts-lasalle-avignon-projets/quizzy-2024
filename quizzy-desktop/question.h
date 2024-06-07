#ifndef QUESTION_H
#define QUESTION_H

/**
 * @file question.h
 *
 * @brief Déclaration de la classe Question
 * @author Thomas HNIZDO
 * @version 1.0
 */

#include <QString>
#include <QMap>

/**
 * @class Question
 * @brief Déclaration de la classe Question
 * @details Cette classe définit une question d'un quiz
 */
class Question
{
  private:
    QString     libelle;      //!< le libéllé d'une question
    QStringList participants; //!< la liste des participants
    QMap<char, QString>
        propositions;    //!< les propositions de réponses pour une question
    int duree;           //!< la durée pour répondre
    int reponseCorrecte; //!< le numéro de réponse correcte

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
