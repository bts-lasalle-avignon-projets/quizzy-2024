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
Participant::Participant(const QString& nom, const QString& idPupitre) :
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
QString Participant::getIdPupitre() const
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
void Participant::incrementerNombreReponsesCorrectes(int numeroQuestion)
{
    ++nombreReponsesCorrectes;
    questionsCorrectes.push_back(numeroQuestion);
}

QVector<int> Participant::getQuestionsCorrectes() const
{
    return questionsCorrectes;
}

QVector<int> Participant::getReponses() const
{
    return reponses;
}

void Participant::enregistrerReponse(int reponse, int tempsReponse)
{
    reponses.push_back(reponse);
    tempsReponses.push_back(tempsReponse);
}

QVector<int> Participant::getTempsReponses() const
{
    return tempsReponses;
}
