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
    int          idPupitre;               //!< l'id du pupitre
    unsigned int nombreReponsesCorrectes; //!< le nombre de bonnes réponses

  public:
    Participant(const QString& nom, int idPupitre);
    ~Participant();

    QString      getNom() const;
    int          getIdPupitre() const;
    unsigned int getNombreReponsesCorrectes() const;
    void         incrementerNombreReponsesCorrectes();
};

#endif // PARTICIPANT_H
