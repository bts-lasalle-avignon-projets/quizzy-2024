/**
 * @file participant.cpp
 *
 * @brief Définition de la classe Participant
 * @author Thomas HNIZDO
 * @version 0.2
 */

#include "participant.h"

/**
 * @brief Constructeur de la classe Participant
 *
 * @param nom le nom du paricipant
 * @param idPupitre l'id du pupitre
 */
Participant::Participant(const QString& nom, int idPupitre) :
    nom(nom), idPupitre(idPupitre), nombreReponsesCorrectes(0)
{
}

/**
 * @brief Destructeur de la classe Participant
 */
Participant::~Participant()
{
}

/**
 * @brief Retourne le nom du paricipant
 *
 * @return QString le nom du paricipant
 */
QString Participant::getNom() const
{
    return nom;
}

/**
 * @brief Accesseur de l'attribut idPupitre
 *
 * @return int l'id du pupitre
 */
int Participant::getIdPupitre() const
{
    return idPupitre;
}

/**
 * @brief Constructeur de la classe IHMQuizzy
 *
 * @return int le nombre de bonnes réponses
 */
unsigned int Participant::getNombreReponsesCorrectes() const
{
    return nombreReponsesCorrectes;
}

/**
 * @brief Incrémente le nombre de bonnes réponses
 */
void Participant::incrementerNombreReponsesCorrectes()
{
    ++nombreReponsesCorrectes;
}

QVector<int> Participant::getReponses() const
{
    return reponses;
}

void Participant::enregistrerReponse(int reponse)
{
    reponses.push_back(reponse);
}
