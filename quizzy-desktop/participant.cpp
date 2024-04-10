#include "participant.h"

Participant::Participant(const QString& nom) : nom(nom)
{
}

Participant::~Participant()
{
}

QString Participant::getNom() const
{
    return nom;
}
