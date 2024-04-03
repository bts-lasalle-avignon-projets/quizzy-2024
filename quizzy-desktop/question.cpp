#include "question.h"

Question::Question(QString libelle, QStringList propositions) : libelle(libelle)
{
    char indexPropositions[] = { 'A', 'B', 'C', 'D' };
    for(int i = 0; i < propositions.size(); ++i)
    {
        this->propositions[indexPropositions[i]] = propositions.at(i);
    }
}

Question::~Question()
{
}

QString Question::getLibelle() const
{
    return libelle;
}

QMap<char, QString> Question::getPropositions() const
{
    return propositions;
}
