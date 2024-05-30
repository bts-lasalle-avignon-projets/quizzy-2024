#ifndef PARTICIPANT_H
#define PARTICIPANT_H

/**
 * @file participant.h
 *
 * @brief Déclaration de la classe Participant
 * @author Thomas HNIZDO
 * @version 0.2
 */

#include <QString>
#include <QVector>

/**
 * @def ID_PUPITRE_NON_DEFINI
 * @brief Pour définir un id invalide
 */
#define ID_PUPITRE_NON_DEFINI -1

/**
 * @class Participant
 * @brief Déclaration de la classe Participant
 * @details Cette classe gère un participant d'un Quiz
 */
class Participant
{
  private:
    QString      nom;                     //!< le nom du participant
    QString      idPupitre;               //!< l'id du pupitre
    unsigned int nombreReponsesCorrectes; //!< le nombre de bonnes réponses
    QVector<int> reponses;
    QVector<int> tempsReponses; //!< les temps de réponse
    QVector<int> questionsCorrectes;

  public:
    Participant(const QString& nom, const QString& idPupitre);
    ~Participant();

    QString      getNom() const;
    QString      getIdPupitre() const;
    unsigned int getNombreReponsesCorrectes() const;
    QVector<int> getReponses() const;
    QVector<int> getTempsReponses() const;
    QVector<int> getQuestionsCorrectes() const;
    void         enregistrerReponse(int reponse, int tempsReponse);
    void         incrementerNombreReponsesCorrectes(int numeroQuestion);
};

#endif // PARTICIPANT_H
