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
        emit debutQuiz();
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
        emit lancementQuiz();
    }
}

// Gestion des participants
bool Quizzy::estParticipantActuel(QString pidJoueur) const
{
    for(Participant* participant: participants)
    {
        if(participant->getIdPupitre() == pidJoueur.toInt())
        {
            qDebug() << Q_FUNC_INFO << "pidJoueur" << pidJoueur << "nom"
                     << participant->getNom() << "true";
            return true;
        }
    }
    qDebug() << Q_FUNC_INFO << "pidJoueur" << pidJoueur << "false";
    return false;
}

// Gestion des réponses
bool Quizzy::traiterReponseParticipant(Participant* participant,
                                       int          numeroReponse,
                                       int          tempsReponse)
{
    Question* questionActuelle = getQuestion();
    if(questionActuelle)
    {
        int reponseCorrecte = questionActuelle->getReponseCorrecte();
        qDebug() << Q_FUNC_INFO << "pidJoueur" << participant->getIdPupitre()
                 << "nom" << participant->getNom() << "numeroReponse"
                 << numeroReponse << "reponseCorrecte" << reponseCorrecte
                 << "tempsReponse" << tempsReponse;

        participant->enregistrerReponse(numeroReponse, tempsReponse);

        if(numeroReponse == reponseCorrecte)
        {
            participant->incrementerNombreReponsesCorrectes();
        }
        return true;
    }
    return false;
}

// Slot Gestion du quiz
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

// Slot Gestion des participants
void Quizzy::ajouterParticipant(QString pidJoueur, QString nomParticipant)
{
    if(etat == QuizDemarre || etat == ParticipantsAjoutes)
    {
        if(estParticipantActuel(pidJoueur))
            return;

        qDebug() << Q_FUNC_INFO << "pidJoueur" << pidJoueur << "nomParticipant"
                 << nomParticipant;
        Participant* participant =
          new Participant(nomParticipant, pidJoueur.toInt());
        participants.push_back(participant);

        etat = ParticipantsAjoutes;
        qDebug() << Q_FUNC_INFO << "etat" << etat;

        emit participantAjoute(pidJoueur, nomParticipant);
    }
}

// Slot Gestion des questions
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

        etat = QuestionsAjoutees;
        qDebug() << Q_FUNC_INFO << "etat" << etat;

        emit questionAjoutee();
    }
}

void Quizzy::demarrerQuestion()
{
    if(etat == QuizLance && getQuestion() != nullptr)
    {
        etat = QuestionDemarree;
        qDebug() << Q_FUNC_INFO << "etat" << etat;
        emit questionDemarree();
    }
}

void Quizzy::terminerQuestion()
{
    if(etat == QuestionDemarree && getQuestion() != nullptr)
    {
        etat = Quizzy::QuestionTerminee;
        qDebug() << Q_FUNC_INFO << "etat" << etat;
        emit questionTerminee();
    }
}

// Gestion des réponses
void Quizzy::traiterReponse(QString pidJoueur,
                            int     numeroReponse,
                            int     tempsReponse)
{
    if(etat == QuestionDemarree)
    {
        if(!estParticipantActuel(pidJoueur))
        {
            return;
        }

        qDebug() << Q_FUNC_INFO << "pidJoueur" << pidJoueur << "numeroReponse"
                 << numeroReponse << "tempsReponse" << tempsReponse;
        for(Participant* participant: participants)
        {
            if(participant->getIdPupitre() == pidJoueur.toInt())
            {
                traiterReponseParticipant(participant,
                                          numeroReponse,
                                          tempsReponse);
            }
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

// Méthodes privées
void Quizzy::initialiserCommunicationTablette()
{
    qDebug() << Q_FUNC_INFO;
    communicationTablette->demarrerServeur();
}
