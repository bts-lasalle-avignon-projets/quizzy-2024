#include "quizzy.h"
#include "participant.h"
#include "question.h"
#include "communicationbluetooth.h"

#include <QDebug>

// Constructeur et destructeur
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

// Initialisation
void Quizzy::initialiserCommunicationTablette()
{
    qDebug() << Q_FUNC_INFO;
    communicationTablette->demarrerServeur();
}

// Gestion du quiz
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

// Gestion des participants
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

// Gestion des questions
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
        Question* question = new Question(libelle, propositions, reponseValide);
        question->setDuree(temps);
        listeQuestions.append(question);

        qDebug() << Q_FUNC_INFO << "reponseValide:" << reponseValide;

        etat = QuestionsAjoutees;
        qDebug() << Q_FUNC_INFO << "etat" << etat;
    }
}

bool Quizzy::estParticipantActuel(QString pidJoueur)
{
    for(Participant* participant: participants)
    {
        if(participant->getIdPupitre() == pidJoueur.toInt())
        {
            return true;
        }
    }
    return false;
}

void Quizzy::traiterReponse(Participant* participant, int numeroReponse)
{
    Question* questionActuelle = getQuestion();
    if(questionActuelle)
    {
        int reponseCorrecte = questionActuelle->getReponseCorrecte();
        qDebug() << Q_FUNC_INFO << "reponseCorrecte:" << reponseCorrecte;
        qDebug() << Q_FUNC_INFO << "Numero de reponse:" << numeroReponse;

        participant->enregistrerReponse(numeroReponse);

        if(numeroReponse == reponseCorrecte)
        {
            participant->incrementerNombreReponsesCorrectes();
            qDebug() << Q_FUNC_INFO << "pupitre:" << participant->getIdPupitre()
                     << "Bonne reponse:" << numeroReponse;
        }
        else
        {
            qDebug() << Q_FUNC_INFO << "pupitre:" << participant->getIdPupitre()
                     << "Mauvaise reponse:" << numeroReponse;
        }
    }
}

void Quizzy::verifierReponse(QString pidJoueur, int numeroReponse)
{
    if(!estParticipantActuel(pidJoueur))
    {
        qDebug() << Q_FUNC_INFO << "pidJoueur non participant :" << pidJoueur;
        return;
    }

    for(Participant* participant: participants)
    {
        if(participant->getIdPupitre() == pidJoueur.toInt())
        {
            traiterReponse(participant, numeroReponse);
            break;
        }
    }
}

// Getters
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

QString Quizzy::getNomDuParticipant(QString pidJoueur)
{
    for(Participant* participant: participants)
    {
        if(participant->getIdPupitre() == pidJoueur.toInt())
        {
            return participant->getNom();
        }
    }
    return QString();
}

CommunicationBluetooth* Quizzy::getCommunicationTablette()
{
    return communicationTablette;
}
