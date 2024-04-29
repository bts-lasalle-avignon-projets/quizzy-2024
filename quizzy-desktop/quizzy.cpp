#include "quizzy.h"
#include "participant.h"
#include "question.h"
#include "communicationbluetooth.h"

#include <QDebug>

Quizzy::Quizzy(QObject* parent) :
    QObject(parent), indexQuestionActuelle(INDEX_NON_DEFINI), enCours(false),
    communicationTablette(new CommunicationBluetooth(this)), etat(EtatInitial)
{
    qDebug() << Q_FUNC_INFO;
    initialiserCommunicationTablette();
}

Quizzy::~Quizzy()
{
    qDebug() << Q_FUNC_INFO;
}

void Quizzy::initialiserCommunicationTablette()
{
    qDebug() << Q_FUNC_INFO;

    communicationTablette->demarrerServeur();
}

void Quizzy::debuter()
{
    qDebug() << Q_FUNC_INFO << "Etat avant : " << etat;
    participants.clear();
    listeQuestions.clear();
    enCours = false;
    etat    = EtatInitial;
    qDebug() << "Etat après : " << etat;
}

void Quizzy::lancer()
{
    qDebug() << Q_FUNC_INFO << "Etat avant : " << etat;
    if(etat == EtatQuestionsAjoutees)
    {
        enCours = true;
        etat    = EtatQuizLance;
    }
    qDebug() << "Etat après : " << etat;
}

void Quizzy::ajouterParticipant(QString pidJoueur, QString nomParticipant)
{
    qDebug() << Q_FUNC_INFO << "Etat avant : " << etat;
    Participant* participant =
      new Participant(nomParticipant, pidJoueur.toInt());
    participants.push_back(participant);
    if(etat == EtatInitial)
    {
        etat = EtatParticipantsAjoutes;
    }
    qDebug() << "Etat après : " << etat;
}

void Quizzy::ajouterQuestion(QString     libelle,
                             QStringList propositions,
                             int         reponseValide,
                             int         temps)
{
    qDebug() << Q_FUNC_INFO << "Etat avant : " << etat;
    Question* question = new Question(libelle, propositions);
    question->setDuree(temps);
    listeQuestions.append(question);
    if(etat == EtatParticipantsAjoutes)
    {
        etat = EtatQuestionsAjoutees;
    }
    qDebug() << "Etat après : " << etat;
}

unsigned int Quizzy::getNbQuestions()
{
    return listeQuestions.size();
}

unsigned int Quizzy::getNbParticipants()
{
    return participants.size();
}

Question* Quizzy::getQuestion()
{
    if(listeQuestions.isEmpty())
        return nullptr;
    return listeQuestions.last();
}

bool Quizzy::estEncours() const
{
    return enCours;
}

CommunicationBluetooth* Quizzy::getCommunicationTablette()
{
    return communicationTablette;
}
