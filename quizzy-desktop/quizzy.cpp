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
            initialiserQuiz();
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
        effacerChoix();
        qDebug() << Q_FUNC_INFO << "etat" << etat;
        emit lancementQuiz();
    }
}

// Gestion des participants
bool Quizzy::estParticipantActuel(QString pidJoueur) const
{
    for(Participant* participant: participants)
    {
        if(participant->getIdPupitre() == pidJoueur)
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
        int numeroQuestion  = indexQuestionActuelle + 1;
        qDebug() << Q_FUNC_INFO << "pidJoueur" << participant->getIdPupitre()
                 << "nom" << participant->getNom() << "numeroReponse"
                 << numeroReponse << "reponseCorrecte" << reponseCorrecte
                 << "tempsReponse" << tempsReponse;

        participant->enregistrerReponse(numeroReponse, tempsReponse);

        if(numeroReponse == reponseCorrecte)
        {
            participant->incrementerNombreReponsesCorrectes(numeroQuestion);
        }

        choixParticipants[numeroReponse].append(participant->getNom());
        return true;
    }
    return false;
}

void Quizzy::effacerChoix()
{
    for(int i = 0; i < choixParticipants.size(); ++i)
    {
        choixParticipants[i].clear();
    }
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
    else if(etat == Resultats)
    {
        etat = Initial;
        debuter(true);
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
        Participant* participant = new Participant(nomParticipant, pidJoueur);
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
        // @todo Gérer les participants qui n'ont pas répondu ?
        etat = Quizzy::QuestionTerminee;
        qDebug() << Q_FUNC_INFO << "etat" << etat;
        emit questionTerminee();
    }
}

void Quizzy::passerQuestionSuivante()
{
    if(etat == QuestionDemarree && getQuestion()->getDuree() == 0)
    {
        terminerQuestion();
    }

    if(etat == QuestionTerminee &&
       indexQuestionActuelle < listeQuestions.size() - 1)
    {
        indexQuestionActuelle++;
        qDebug() << Q_FUNC_INFO << "indexQuestionActuelle"
                 << indexQuestionActuelle;
        effacerChoix();
        etat = QuizLance;
        qDebug() << Q_FUNC_INFO << "etat" << etat;
        emit questionSuivantePrete();
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
            if(participant->getIdPupitre() == pidJoueur)
            {
                traiterReponseParticipant(participant,
                                          numeroReponse,
                                          tempsReponse);
            }
        }

        if(getQuestion()->getDuree() == 0)
        {
            emit choixParticipant();
        }
    }
}

void Quizzy::gererFinQuiz()
{
    if(etat == QuestionDemarree &&
       indexQuestionActuelle == listeQuestions.size() - 1)
    {
        terminerQuestion();
    }
    if(etat == QuestionTerminee &&
       indexQuestionActuelle == listeQuestions.size() - 1)
    {
        etat = Resultats;
        qDebug() << Q_FUNC_INFO << "etat" << etat;
        emit quizTermine();
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

QVector<Participant*> Quizzy::getParticipants()
{
    return participants;
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

QMap<int, QStringList> Quizzy::getChoixParticipants() const
{
    return choixParticipants;
}

int Quizzy::getIndexQuestionActuelle() const
{
    return indexQuestionActuelle;
}

QString Quizzy::getNomDuParticipant(QString pidJoueur)
{
    for(Participant* participant: participants)
    {
        if(participant->getIdPupitre() == pidJoueur)
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

void Quizzy::initialiserQuiz()
{
    participants.clear();
    listeQuestions.clear();
    indexQuestionActuelle = INDEX_NON_DEFINI;
}
