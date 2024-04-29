#include "quizzy.h"
#include "participant.h"
#include "question.h"
#include "communicationbluetooth.h"

#include <QDebug>

Quizzy::Quizzy(QObject* parent) :
    QObject(parent), indexQuestionActuelle(INDEX_NON_DEFINI), etat(Initial),
    communicationTablette(new CommunicationBluetooth(this))
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

void Quizzy::debuter(bool reset)
{
    if(etat == Initial)
    {
        if(reset)
        {
            qDebug() << Q_FUNC_INFO << "reset" << reset;
            participants.clear();
            listeQuestions.clear();
            indexQuestionActuelle = INDEX_NON_DEFINI;
        }
        etat = QuizDemarre;
        qDebug() << Q_FUNC_INFO << "etat" << etat;
    }
}

void Quizzy::lancer()
{
    if(etat == QuestionsAjoutees)
    {
        indexQuestionActuelle = 0;
        qDebug() << Q_FUNC_INFO << "indexQuestionActuelle"
                 << indexQuestionActuelle;
        etat = QuizLance;
        qDebug() << Q_FUNC_INFO << "etat" << etat;
        emit affichagePremiereQuestion();
    }
}

bool Quizzy::ajouterParticipant(QString pidJoueur, QString nomParticipant)
{
    if(etat == QuizDemarre || etat == ParticipantsAjoutes)
    {
        // @todo Vérifier l'existence du pidJoueur/nomParticipant
        Participant* participant =
          new Participant(nomParticipant, pidJoueur.toInt());
        participants.push_back(participant);

        etat = ParticipantsAjoutes;
        qDebug() << Q_FUNC_INFO << "etat" << etat;

        return true;
    }

    return false;
}

void Quizzy::ajouterQuestion(QString     libelle,
                             QStringList propositions,
                             int         reponseValide,
                             int         temps)
{
    if(etat == ParticipantsAjoutes || etat == QuestionsAjoutees)
    {
        qDebug() << Q_FUNC_INFO << "libelle" << libelle << "propositions"
                 << propositions << "reponseValide" << reponseValide << temps
                 << temps;
        Question* question = new Question(libelle, propositions);
        question->setDuree(temps);
        listeQuestions.append(question);

        etat = QuestionsAjoutees;
        qDebug() << Q_FUNC_INFO << "etat" << etat;
    }
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
    return listeQuestions[indexQuestionActuelle];
}

Quizzy::Etat Quizzy::getEtat() const
{
    return etat;
}

int Quizzy::getIndexQuestionActuelle() const
{
    return indexQuestionActuelle;
}

CommunicationBluetooth* Quizzy::getCommunicationTablette()
{
    return communicationTablette;
}

void Quizzy::gererDebutQuiz()
{
    if(etat == Initial)
    {
        debuter();
    }
    else if(etat == QuestionsAjoutees)
    {
        lancer();
    }
}
