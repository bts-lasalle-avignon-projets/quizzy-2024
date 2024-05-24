#include "question.h"

Question::Question(QString     libelle,
                   QStringList propositions,
                   int         reponseCorrecte) :
    libelle(libelle),
    reponseCorrecte(reponseCorrecte)
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

int Question::getDuree() const
{
    return duree;
}
void Question::setDuree(int temps)
{
    duree = temps;
}

int Question::getReponseCorrecte() const
{
    return reponseCorrecte;
}
