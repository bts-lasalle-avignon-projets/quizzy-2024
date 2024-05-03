#include "ihmquizzy.h"
#include "quizzy.h"
#include "question.h"
#include "participant.h"
#include "communicationbluetooth.h"
#include <QDebug>

/**
 * @file ihmquizzy.cpp
 *
 * @brief Définition de la classe IHMQuizzy
 * @author Thomas HNIZDO
 * @version 0.1
 */

/**
 * @brief Constructeur de la classe IHMQuizzy
 *
 * @fn IHMQuizzy::IHMQuizzy
 * @param parent L'adresse de l'objet parent, si nullptr IHMQuizzy sera la
 * fenêtre principale de l'application
 */
IHMQuizzy::IHMQuizzy(QWidget* parent) :
    QWidget(parent), quizzy(new Quizzy(this)), minuteur(new QTimer(this))
{
    qDebug() << Q_FUNC_INFO;
    creerFenetres();
    afficherFenetreAccueil();
    initialiserEvenements();

#ifdef PLEIN_ECRAN_RASPBERRY_PI
    showFullScreen();
// showMaximized();
#endif
}

IHMQuizzy::~IHMQuizzy()
{
    qDebug() << Q_FUNC_INFO;
}

void IHMQuizzy::afficherFenetre(IHMQuizzy::Fenetre fenetre)
{
    qDebug() << Q_FUNC_INFO << "fenetre" << fenetre;
    fenetres->setCurrentIndex(fenetre);
}

void IHMQuizzy::afficherFenetreAccueil()
{
    afficherFenetre(Fenetre::FenetreAccueil);
}

void IHMQuizzy::afficherFenetreParticipants()
{
    afficherFenetre(Fenetre::FenetreParticipants);
}

void IHMQuizzy::afficherFenetreJeu()
{
    afficherFenetre(Fenetre::FenetreJeu);
}

void IHMQuizzy::afficherFenetreResultats()
{
    afficherFenetre(Fenetre::FenetreResultats);
}

void IHMQuizzy::afficherDebutQuiz()
{
    qDebug() << Q_FUNC_INFO;
    messageAttente->setText("En attente des participants...");
    afficherFenetreAccueil();
}

void IHMQuizzy::afficherLancementQuiz()
{
    qDebug() << Q_FUNC_INFO;
    afficherQuestion();
}

void IHMQuizzy::afficherPret()
{
    qDebug() << Q_FUNC_INFO;
    infoQuiz->setText(QString::fromUtf8("\u2139 Prêt à lancer le quiz"));
}

void IHMQuizzy::afficherParticipant(QString pidJoueur, QString nomParticipant)
{
    qDebug() << Q_FUNC_INFO << "pidJoueur" << pidJoueur << "nomParticipant"
             << nomParticipant;
    QWidget*     widgetParticipant = new QWidget(this);
    QVBoxLayout* layoutParticipant = new QVBoxLayout(widgetParticipant);
    QLabel*      labelParticipant  = new QLabel(nomParticipant, this);
    layoutParticipant->setContentsMargins(100, 10, 100, 10);
    layoutParticipant->addWidget(labelParticipant);
    layoutPrincipalParticipants->addWidget(widgetParticipant);

    afficherFenetreParticipants();
}

void IHMQuizzy::demarrerQuestion()
{
    initialiserChronometre();
}

void IHMQuizzy::afficherDecompteQuestion()
{
    if(fenetres->currentIndex() == Fenetre::FenetreJeu)
    {
        changerCouleurChronometre();
        labelChronometre->setText(QString::number(decompteQuestion) + "s");
        decompteQuestion--;
        if(decompteQuestion < 0)
        {
            minuteur->stop();
            labelChronometre->setText(QString::number(0) + "s");
            labelChronometre->setStyleSheet("background-color: #f9e4b7");
            // @fixme Si la question n'a pas de durée, il faudra créer une trame
            // pour mettre fin à cette question ou utilise-t-on la trame $S ?
            quizzy->terminerQuestion();
        }
    }
}

// Méthodes privées

void IHMQuizzy::initialiserFenetres()
{
    fenetres                     = new QStackedWidget(this);
    QVBoxLayout* layoutPrincipal = new QVBoxLayout();
    layoutPrincipal->setSpacing(0);
    layoutPrincipal->setContentsMargins(0, 0, 0, 0);
    layoutPrincipal->addWidget(fenetres);
    setLayout(layoutPrincipal);
    setMinimumSize(QSize(TAILLE_LARGEUR_ECRAN_MIN, TAILLE_HAUTEUR_ECRAN_MIN));
    setWindowTitle(QString(NOM_APP) + QString(" v") + QString(VERSION_APP));
}

void IHMQuizzy::creerFenetres()
{
    initialiserFenetres();
    creerFenetreAccueil();
    creerFenetreParticipants();
    creerFenetreJeu();
    creerFenetreResultats();
}

void IHMQuizzy::creerFenetreAccueil()
{
    fenetreAccueil             = new QWidget(this);
    QVBoxLayout* layoutAccueil = new QVBoxLayout(fenetreAccueil);
    titreFenetreAccueil        = new QLabel("QUIZZY", this);
    titreFenetreAccueil->setObjectName("titreAccueil");
    titreFenetreAccueil->setAlignment(Qt::AlignCenter);
    messageAttente = new QLabel("", this);
    messageAttente->setObjectName("messageAttente");
    messageAttente->setAlignment(Qt::AlignCenter);
    layoutAccueil->addWidget(titreFenetreAccueil);
    layoutAccueil->addWidget(messageAttente);
    fenetres->addWidget(fenetreAccueil);
}

void IHMQuizzy::creerFenetreParticipants()
{
    fenetreParticipants = new QWidget(this);
    creerLayoutsFenetreParticipants();
    creerWidgetsFenetreParticipants();
    placerWidgetsFenetreParticipants();

    fenetres->addWidget(fenetreParticipants);
}

void IHMQuizzy::creerLayoutsFenetreParticipants()
{
    layoutPrincipalParticipants = new QVBoxLayout(fenetreParticipants);
}

void IHMQuizzy::creerWidgetsFenetreParticipants()
{
    titreFenetreParticipants = new QLabel("Liste des participants", this);
    titreFenetreParticipants->setAlignment(Qt::AlignCenter);
    titreFenetreParticipants->setObjectName("titreParticipants");

    infoQuiz = new QLabel(this);
    infoQuiz->setFixedSize(500, 100);
    infoQuiz->setAlignment(Qt::AlignCenter);
    infoQuiz->setObjectName("infoQuiz");
}

void IHMQuizzy::placerWidgetsFenetreParticipants()
{
    layoutPrincipalParticipants->addWidget(titreFenetreParticipants);
    layoutInfoQuiz = new QHBoxLayout;
    layoutInfoQuiz->addStretch();
    layoutInfoQuiz->addWidget(infoQuiz);
    layoutInfoQuiz->addStretch();
    layoutPrincipalParticipants->addLayout(layoutInfoQuiz);
}

void IHMQuizzy::creerFenetreJeu()
{
    fenetreJeu = new QWidget(this);
    creerLayoutsFenetreJeu();
    creerWidgetsFenetreJeu();
    placerWidgetsFenetreJeu();
    fenetres->addWidget(fenetreJeu);
}

void IHMQuizzy::creerLayoutsFenetreJeu()
{
    layoutPrincipalJeu       = new QVBoxLayout(fenetreJeu);
    layoutLibelle            = new QHBoxLayout();
    layoutPropositionReponse = new QVBoxLayout();
    layoutPropositonAB       = new QHBoxLayout();
    layoutPropositonCD       = new QHBoxLayout();
    layoutPropositionChoixA  = new QHBoxLayout(); // Nouveau layout
    layoutPropositionChoixB  = new QHBoxLayout(); // Nouveau layout
    layoutPropositionChoixC  = new QHBoxLayout(); // Nouveau layout
    layoutPropositionChoixD  = new QHBoxLayout(); // Nouveau layout
    layoutChronometre        = new QHBoxLayout();
}

void IHMQuizzy::creerWidgetsFenetreJeu()
{
    labelNombreTotal    = new QLabel("0/0", this);
    labelQuestion       = new QLabel("", this);
    propositionReponseA = new QLabel("A", this);
    choixPropositionA   = new QLabel("Choix A", this);
    propositionReponseB = new QLabel("B", this);
    choixPropositionB   = new QLabel("Choix B", this);
    propositionReponseC = new QLabel("C", this);
    choixPropositionC   = new QLabel("Choix C", this);
    propositionReponseD = new QLabel("D", this);
    choixPropositionD   = new QLabel("Choix D", this);
    labelChronometre    = new QLabel("00:00", this);
    styleFenetreJeu();
}

void IHMQuizzy::styleFenetreJeu()
{
    propositionReponseA->setObjectName("propositionReponseA");
    propositionReponseB->setObjectName("propositionReponseB");
    propositionReponseC->setObjectName("propositionReponseC");
    propositionReponseD->setObjectName("propositionReponseD");
    choixPropositionA->setObjectName("choixPropositionA");
    choixPropositionB->setObjectName("choixPropositionB");
    choixPropositionC->setObjectName("choixPropositionC");
    choixPropositionD->setObjectName("choixPropositionD");
}

void IHMQuizzy::placerWidgetsFenetreJeu()
{
    // En-tête Question
    layoutLibelle->addWidget(labelNombreTotal);
    layoutLibelle->addWidget(labelQuestion);
    layoutPrincipalJeu->addLayout(layoutLibelle);
    // Proposition A et B
    layoutPropositionChoixA->addWidget(propositionReponseA);
    layoutPropositionChoixA->addWidget(choixPropositionA);
    layoutPropositonAB->addLayout(layoutPropositionChoixA);
    layoutPropositionChoixB->addWidget(propositionReponseB);
    layoutPropositionChoixB->addWidget(choixPropositionB);
    layoutPropositonAB->addLayout(layoutPropositionChoixB);
    layoutPropositionReponse->addLayout(layoutPropositonAB);

    // Proposition C et D
    layoutPropositionChoixC->addWidget(propositionReponseC);
    layoutPropositionChoixC->addWidget(choixPropositionC);
    layoutPropositonCD->addLayout(layoutPropositionChoixC);
    layoutPropositionChoixD->addWidget(propositionReponseD);
    layoutPropositionChoixD->addWidget(choixPropositionD);
    layoutPropositonCD->addLayout(layoutPropositionChoixD);
    layoutPropositionReponse->addLayout(layoutPropositonCD);
    // Ajout Layout principal
    layoutPrincipalJeu->addLayout(layoutPropositionReponse);
    layoutChronometre->addWidget(labelChronometre);
    layoutPrincipalJeu->addLayout(layoutChronometre);
}

void IHMQuizzy::creerFenetreResultats()
{
    fenetreResultats             = new QWidget(this);
    QVBoxLayout* layoutPrincipal = new QVBoxLayout(fenetreResultats);
    titreFenetreResultats        = new QLabel("Résultats", this);
    layoutPrincipal->addWidget(titreFenetreResultats);

    fenetres->addWidget(fenetreResultats);
}

void IHMQuizzy::initialiserEvenements()
{
    // communicationTablette vers quizzy
    // Trame $L
    connect(quizzy->getCommunicationTablette(),
            SIGNAL(debutQuiz()),
            quizzy,
            SLOT(gererDebutQuiz()));
    // Trame $I
    connect(quizzy->getCommunicationTablette(),
            SIGNAL(nouveauParticipant(QString, QString)),
            quizzy,
            SLOT(ajouterParticipant(QString, QString)));
    // Trame $G
    connect(quizzy->getCommunicationTablette(),
            SIGNAL(nouvelleQuestion(QString, QStringList, int, int)),
            quizzy,
            SLOT(ajouterQuestion(QString, QStringList, int, int)));
    // Trame $T
    connect(quizzy->getCommunicationTablette(),
            SIGNAL(debutQuestion()),
            quizzy,
            SLOT(demarrerQuestion()));
    // Trame $U
    connect(quizzy->getCommunicationTablette(),
            SIGNAL(choixReponse(QString, int, int)),
            quizzy,
            SLOT(traiterReponse(QString, int, int)));

    // quizzy vers ihmQuizzy (this)
    connect(quizzy, SIGNAL(debutQuiz()), this, SLOT(afficherDebutQuiz()));
    connect(quizzy,
            SIGNAL(participantAjoute(QString, QString)),
            this,
            SLOT(afficherParticipant(QString, QString)));
    connect(quizzy,
            SIGNAL(lancementQuiz()),
            this,
            SLOT(afficherLancementQuiz()));
    connect(quizzy, SIGNAL(questionAjoutee()), this, SLOT(afficherPret()));
    connect(quizzy, SIGNAL(questionDemarree()), this, SLOT(demarrerQuestion()));
    connect(quizzy,
            SIGNAL(questionTerminee()),
            this,
            SLOT(afficherChoixReponse()));
}

void IHMQuizzy::afficherQuestion()
{
    qDebug() << Q_FUNC_INFO;
    Question* question = quizzy->getQuestion();
    afficherNbQuestions(quizzy->getIndexQuestionActuelle() + 1,
                        quizzy->getNbQuestions());
    afficherLibelleQuestion(*question);
    afficherPropositionsQuestion(*question);
    afficherTempsQuestion(*question);
    effacerChoix();
    afficherFenetreJeu();
}

void IHMQuizzy::afficherNbQuestions(unsigned int numeroQuestion,
                                    unsigned int nbQuestions)
{
    qDebug() << Q_FUNC_INFO << "numeroQuestion" << numeroQuestion
             << "nbQuestions" << nbQuestions;
    labelNombreTotal->setText(QString("Question n°") +
                              QString::number(numeroQuestion));
}

void IHMQuizzy::afficherLibelleQuestion(const Question& question)
{
    labelQuestion->setText(question.getLibelle());
}

void IHMQuizzy::afficherPropositionsQuestion(const Question& question)
{
    QMap<char, QString> propositions = question.getPropositions();
    propositionReponseA->setText("A. " + propositions['A']);
    propositionReponseB->setText("B. " + propositions['B']);
    propositionReponseC->setText("C. " + propositions['C']);
    propositionReponseD->setText("D. " + propositions['D']);
}

void IHMQuizzy::afficherTempsQuestion(const Question& question)
{
    if(question.getDuree() > 0)
    {
        labelChronometre->setText(QString::number(question.getDuree()) + "s");
        labelChronometre->setVisible(true);
    }
    else
    {
        labelChronometre->setVisible(false);
    }
}

void IHMQuizzy::initialiserChronometre()
{
    decompteQuestion = quizzy->getQuestion()->getDuree();
    qDebug() << Q_FUNC_INFO << "decompteQuestion" << decompteQuestion;

    disconnect(minuteur,
               SIGNAL(timeout()),
               this,
               SLOT(afficherDecompteQuestion()));

    if(minuteur->isActive())
        minuteur->stop();

    if(decompteQuestion > 0)
    {
        connect(minuteur,
                SIGNAL(timeout()),
                this,
                SLOT(afficherDecompteQuestion()));

        minuteur->start(TOP_SECONDE);
    }
}

void IHMQuizzy::changerCouleurChronometre()
{
    QString couleur;
    if(decompteQuestion > ECHEANCE_CHRONOMETRE)
    {
        couleur = FOND_VERT;
    }
    else
    {
        couleur = FOND_ROUGE;
    }
    labelChronometre->setStyleSheet("background-color: " + couleur);
}

void IHMQuizzy::traiterChoixReponse(QString pidJoueur,
                                    int     numeroReponse,
                                    int     tempsReponse)
{
    qDebug() << Q_FUNC_INFO << "pidJoueur" << pidJoueur << "numeroReponse"
             << numeroReponse << "tempsReponse" << tempsReponse;

    mettreAJourChoix(pidJoueur, numeroReponse, tempsReponse);
}

void IHMQuizzy::afficherChoixReponse()
{
    qDebug() << Q_FUNC_INFO;

    // @todo Déclencher par un signal émis par quizzy
    afficherChoixParticipants();
}

void IHMQuizzy::effacerChoix()
{
    for(int i = 0; i < choixParticipants.size(); ++i)
    {
        choixParticipants[i].clear();
    }
}

void IHMQuizzy::mettreAJourChoix(QString pidJoueur,
                                 int     numeroReponse,
                                 int     tempsReponse)
{
    QString nomParticipant = quizzy->getNomDuParticipant(pidJoueur);
    choixParticipants[numeroReponse].append(nomParticipant);
}

void IHMQuizzy::afficherChoixParticipants()
{
    // @todo A revoir
    for(auto it = choixParticipants.begin(); it != choixParticipants.end();
        ++it)
    {
        int         numeroReponse     = it.key();
        QStringList listeParticipants = it.value();
        qDebug() << Q_FUNC_INFO << "numeroReponse" << numeroReponse
                 << "listeParticipants" << listeParticipants;

        if(listeParticipants.size() < 1)
            continue;

        QString texte = "<br><small>Choisie par : ";
        for(const QString& nomParticipant: listeParticipants)
        {
            texte += nomParticipant + " ";
        }
        texte += "</small>";
        mettreAJourProposition(numeroReponse, texte);
    }
}

void IHMQuizzy::mettreAJourProposition(int numeroReponse, QString texte)
{
    qDebug() << Q_FUNC_INFO << "numeroReponse" << numeroReponse << "texte"
             << texte;

    switch(numeroReponse)
    {
        case 1:
            propositionReponseA->setStyleSheet(
              "background-color: #f9b7b7; border: 3px solid red");
            propositionReponseA->setText(propositionReponseA->text() + texte);
            break;
        case 2:
            propositionReponseB->setStyleSheet(
              "background-color: #b7f9ba; border: 3px solid red");
            propositionReponseB->setText(propositionReponseB->text() + texte);
            break;
        case 3:
            propositionReponseC->setStyleSheet(
              "background-color: #f6f476; border: 3px solid red");
            propositionReponseC->setText(propositionReponseC->text() + texte);
            break;
        case 4:
            propositionReponseD->setStyleSheet(
              "background-color: #b7baf9; border: 3px solid red");
            propositionReponseD->setText(propositionReponseD->text() + texte);
            break;
        default:
            break;
    }
}
