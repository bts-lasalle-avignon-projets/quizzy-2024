#ifndef QUIZZY_H
#define QUIZZY_H

#include <QObject>
#include <QVector>
#include <QMap>

#define INDEX_NON_DEFINI -1

class Participant;
class Question;
class CommunicationBluetooth;

class Quizzy : public QObject
{
    Q_OBJECT

  public:
    /**
     * @enum Etat
     * @brief Définit les différentes états de déroulement d'un quiz
     *
     */
    enum Etat
    {
        Initial,
        QuizDemarre,
        ParticipantsAjoutes,
        QuestionsAjoutees,
        QuizLance,
        QuestionDemarree,
        QuestionTerminee,
        Resultats,
        QuizFini
    };

  private:
    QVector<Participant*>
                       participants; //!< conteneur de Participant pour le quiz
    QVector<Question*> listeQuestions; //!< conteneur de Question pour le quiz
    QMap<int, QStringList>
      choixParticipants; //!< conteneur des choix des participants pour la
                         //!< question en cours
    int  indexQuestionActuelle; //!< le numéro de question en cours
    Etat etat;                  //!< l'état de déroulement du quiz
    CommunicationBluetooth*
      communicationTablette; //!< association avec CommunicationBluetooth

    void initialiserCommunicationTablette();
    void initialiserQuiz();

  public:
    Quizzy(QObject* parent = nullptr);
    ~Quizzy();

    // Gestion du quiz
    void debuter(bool reset = true);
    void lancer();

    // Gestion des participants
    bool estParticipantActuel(QString pidJoueur) const;

    // Gestion des questions

    // Gestion des réponses
    bool traiterReponseParticipant(Participant* participant,
                                   int          numeroReponse,
                                   int          tempsReponse);
    void effacerChoix();
    // Getters
    unsigned int            getNbQuestions();
    unsigned int            getNbParticipants();
    Question*               getQuestion();
    Etat                    getEtat() const;
    QMap<int, QStringList>  getChoixParticipants() const;
    int                     getIndexQuestionActuelle() const;
    QString                 getNomDuParticipant(QString pidJoueur);
    CommunicationBluetooth* getCommunicationTablette();

  public slots:
    // Gestion du quiz
    void gererDebutQuiz();
    // Gestion des participants
    void ajouterParticipant(QString pidJoueur, QString participant);
    void ajouterQuestion(QString     libelle,
                         QStringList propositions,
                         int         reponseValide,
                         int         temps);
    void demarrerQuestion();
    void terminerQuestion();
    void traiterReponse(QString pidJoueur, int numeroReponse, int tempsReponse);
    void passerQuestionSuivante();

  signals:
    void debutQuiz();
    void lancementQuiz();
    void participantAjoute(QString pidJoueur, QString nomParticipant);
    void questionAjoutee();
    void questionDemarree();
    void questionTerminee();
    void questionSuivantePrete();
};

#endif // QUIZZY_H
