#include "participant.h"

Participant::Participant(const QString& nom,
                         int            idPupitre,
                         int            nombreReponsesCorrectes) :
    nom(nom),
    idPupitre(idPupitre), nombreReponsesCorrectes(nombreReponsesCorrectes)
{
}

Participant::~Participant()
{
}

QString Participant::getNom() const
{
    return nom;
}

int Participant::getIdPupitre() const
{
    return idPupitre;
}

int Participant::getNombreReponsesCorrectes() const
{
    return nombreReponsesCorrectes;
}
