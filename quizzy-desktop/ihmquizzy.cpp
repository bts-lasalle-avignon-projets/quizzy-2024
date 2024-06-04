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
 * @version 0.2
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
    titreFenetreParticipants->setAlignment(Qt::AlignCenter);
    reinitialiserAffichage();
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
    QWidget* widgetParticipant = new QWidget(this);
    layoutParticipant          = new QVBoxLayout(widgetParticipant);
    labelParticipant           = new QLabel(nomParticipant, this);
    labelParticipant->setAlignment(Qt::AlignCenter);

    layoutParticipant->setContentsMargins(100, 10, 100, 10);
    layoutParticipant->addWidget(labelParticipant);
    layoutPrincipalParticipants->addWidget(widgetParticipant);
    auto effetLabelParticipant =
      new QGraphicsDropShadowEffect(labelParticipant);
    effetLabelParticipant->setColor(Qt::black);
    effetLabelParticipant->setBlurRadius(20);
    effetLabelParticipant->setOffset(0);
    labelParticipant->setGraphicsEffect(effetLabelParticipant);
    afficherFenetreParticipants();
}

void IHMQuizzy::afficherQuestionSuivante()
{
    qDebug() << Q_FUNC_INFO;
    effacerChoixParticipants();
    afficherQuestion();
}

void IHMQuizzy::afficherResultats()
{
    QVector<Participant*> participants = quizzy->getParticipants();
    unsigned int          nbQuestions  = quizzy->getNbQuestions();
    qDebug() << Q_FUNC_INFO << "nbParticipants" << participants.size()
             << "nbQuestions" << nbQuestions;

    for(Participant* participant: participants)
    {
        qDebug() << Q_FUNC_INFO << "nom" << participant->getNom();

        layoutParticipantResultat = new QHBoxLayout;

        afficherNombreBonnesReponses(participant, nbQuestions);
        afficherNumerosQuestionsCorrectes(participant);

        layoutPrincipalResultat->addLayout(layoutParticipantResultat);
    }

    afficherFenetreResultats();
}

void IHMQuizzy::afficherNombreBonnesReponses(Participant* participant,
                                             unsigned int nbQuestions)
{
    nomParticipant = new QLabel(this);
    nomParticipant->setText(participant->getNom());

    resultatParticipant            = new QLabel(this);
    unsigned int reponsesCorrectes = participant->getNombreReponsesCorrectes();
    QString resultat = "Score : " + QString::number(reponsesCorrectes) + "/" +
                       QString::number(nbQuestions);
    resultatParticipant->setText(resultat);

    layoutParticipantResultat->addWidget(nomParticipant);
    layoutParticipantResultat->addWidget(resultatParticipant);
}

void IHMQuizzy::afficherNumerosQuestionsCorrectes(Participant* participant)
{
    QVector<int> questionsCorrectes      = participant->getQuestionsCorrectes();
    QString      texteQuestionsCorrectes = "Numéro questions correctes : ";
    for(int numeroQuestion: questionsCorrectes)
    {
        texteQuestionsCorrectes += QString::number(numeroQuestion) + " ";
    }
    QLabel* labelQuestionsCorrectes = new QLabel(this);
    labelQuestionsCorrectes->setText(texteQuestionsCorrectes);

    layoutParticipantResultat->addWidget(labelQuestionsCorrectes);
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
        compteARebours->setValue(decompteQuestion);
        decompteQuestion--;
        if(decompteQuestion < 0)
        {
            minuteur->stop();
            compteARebours->setValue(0);
            quizzy->terminerQuestion();
        }
    }
}

void IHMQuizzy::afficherChoixParticipants()
{
    QMap<int, QStringList> choixParticipants = quizzy->getChoixParticipants();
    for(auto it = choixParticipants.begin(); it != choixParticipants.end();
        ++it)
    {
        int         numeroReponse     = it.key();
        QStringList listeParticipants = it.value();
        qDebug() << Q_FUNC_INFO << "numeroReponse" << numeroReponse
                 << "listeParticipants" << listeParticipants;

        if(listeParticipants.size() < 1)
            continue;

        QString texte = "<small>";
        for(const QString& nomParticipant: listeParticipants)
        {
            texte += nomParticipant + " ";
        }
        texte += "</small>";
        mettreAJourProposition(numeroReponse, texte);
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
    configurerResponsiveLabels();
    definirNomsObjets();
}

void IHMQuizzy::creerFenetreAccueil()
{
    fenetreAccueil             = new QWidget(this);
    QVBoxLayout* layoutAccueil = new QVBoxLayout(fenetreAccueil);
    titreFenetreAccueil        = new QLabel(this);
    QPixmap pixmap(CHEMIN_LOGO);
    pixmap =
      pixmap.scaled(QSize(LARGEUR_LOGO, HAUTEUR_LOGO), Qt::KeepAspectRatio);
    titreFenetreAccueil->setPixmap(pixmap);
    titreFenetreAccueil->setAlignment(Qt::AlignCenter);

    messageAttente = new QLabel("", this);
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
    infoQuiz = new QLabel(this);
    infoQuiz->setFixedSize(500, 100);
    infoQuiz->setAlignment(Qt::AlignCenter);
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
    layoutPropositionChoixA  = new QHBoxLayout();
    layoutPropositionChoixB  = new QHBoxLayout();
    layoutPropositionChoixC  = new QHBoxLayout();
    layoutPropositionChoixD  = new QHBoxLayout();
    layoutChronometre        = new QHBoxLayout();
}

void IHMQuizzy::creerWidgetsFenetreJeu()
{
    labelNombreTotal      = new QLabel("0/0", this);
    labelQuestion         = new QLabel("", this);
    idPropositionReponseA = new QLabel("A.", this);
    propositionReponseA   = new QLabel("", this);
    choixPropositionA     = new QLabel("", this);
    idPropositionReponseB = new QLabel("B.", this);
    propositionReponseB   = new QLabel("", this);
    choixPropositionB     = new QLabel("", this);
    idPropositionReponseC = new QLabel("C.", this);
    propositionReponseC   = new QLabel("", this);
    choixPropositionC     = new QLabel("", this);
    idPropositionReponseD = new QLabel("D.", this);
    propositionReponseD   = new QLabel("", this);
    choixPropositionD     = new QLabel("", this);
    compteARebours        = new QProgressBar(this);
}

void IHMQuizzy::configurerResponsiveLabels()
{
    // Fenêtre d'accueil
    auto effetAccueil = new QGraphicsDropShadowEffect(messageAttente);
    effetAccueil->setColor(Qt::black);
    effetAccueil->setBlurRadius(20);
    effetAccueil->setOffset(0);
    messageAttente->setGraphicsEffect(effetAccueil);

    // Fenêtre Participant
    auto effetTitreParticipant =
      new QGraphicsDropShadowEffect(titreFenetreParticipants);
    effetTitreParticipant->setColor(Qt::black);
    effetTitreParticipant->setBlurRadius(20);
    effetTitreParticipant->setOffset(0);
    titreFenetreParticipants->setGraphicsEffect(effetTitreParticipant);

    // Fenêtre Jeu
    labelQuestion->setWordWrap(true);
    propositionReponseA->setWordWrap(true);
    propositionReponseB->setWordWrap(true);
    propositionReponseC->setWordWrap(true);
    propositionReponseD->setWordWrap(true);

    choixPropositionA->setWordWrap(true);
    choixPropositionB->setWordWrap(true);
    choixPropositionC->setWordWrap(true);
    choixPropositionD->setWordWrap(true);

    labelNombreTotal->setSizePolicy(QSizePolicy::Fixed, QSizePolicy::Ignored);
    labelNombreTotal->setFixedHeight(HAUTEUR_LABEL_LIBELLE);

    labelQuestion->setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Fixed);
    labelQuestion->setFixedHeight(HAUTEUR_LABEL_LIBELLE);

    propositionReponseA->setSizePolicy(QSizePolicy::Expanding,
                                       QSizePolicy::Fixed);
    propositionReponseB->setSizePolicy(QSizePolicy::Expanding,
                                       QSizePolicy::Fixed);
    propositionReponseC->setSizePolicy(QSizePolicy::Expanding,
                                       QSizePolicy::Fixed);
    propositionReponseD->setSizePolicy(QSizePolicy::Expanding,
                                       QSizePolicy::Fixed);

    propositionReponseA->setFixedHeight(HAUTEUR_PROPOSITION);
    propositionReponseB->setFixedHeight(HAUTEUR_PROPOSITION);
    propositionReponseC->setFixedHeight(HAUTEUR_PROPOSITION);
    propositionReponseD->setFixedHeight(HAUTEUR_PROPOSITION);

    choixPropositionA->setFixedSize(LARGEUR_PROPOSITION, HAUTEUR_PROPOSITION);
    choixPropositionB->setFixedSize(LARGEUR_PROPOSITION, HAUTEUR_PROPOSITION);
    choixPropositionC->setFixedSize(LARGEUR_PROPOSITION, HAUTEUR_PROPOSITION);
    choixPropositionD->setFixedSize(LARGEUR_PROPOSITION, HAUTEUR_PROPOSITION);
    layoutPropositionReponse->setContentsMargins(MARGE_LAYOUT_PROPOSITION,
                                                 0,
                                                 MARGE_LAYOUT_PROPOSITION,
                                                 0);

    compteARebours->setAlignment(Qt::AlignCenter);
}

void IHMQuizzy::definirNomsObjets()
{
    // Fenêtre Accueil
    titreFenetreAccueil->setObjectName("titreAccueil");
    messageAttente->setObjectName("messageAttente");

    // Fenêtre Participants
    titreFenetreParticipants->setObjectName("titreParticipants");
    infoQuiz->setObjectName("infoQuiz");

    // Fenêtre jeu
    labelNombreTotal->setObjectName("labelNombreTotal");
    labelQuestion->setObjectName("labelQuestion");
    idPropositionReponseA->setObjectName("idPropositionReponseA");
    idPropositionReponseB->setObjectName("idPropositionReponseB");
    idPropositionReponseC->setObjectName("idPropositionReponseC");
    idPropositionReponseD->setObjectName("idPropositionReponseD");
    propositionReponseA->setObjectName("propositionReponseA");
    propositionReponseB->setObjectName("propositionReponseB");
    propositionReponseC->setObjectName("propositionReponseC");
    propositionReponseD->setObjectName("propositionReponseD");
    choixPropositionA->setObjectName("choixPropositionA");
    choixPropositionB->setObjectName("choixPropositionB");
    choixPropositionC->setObjectName("choixPropositionC");
    choixPropositionD->setObjectName("choixPropositionD");
    compteARebours->setObjectName("chronometre");
    // Fenêtre Résultats
    titreFenetreResultats->setObjectName("titreResultats");
}

void IHMQuizzy::placerWidgetsFenetreJeu()
{
    // En-tête Question
    layoutLibelle->addWidget(labelNombreTotal);
    layoutLibelle->addWidget(labelQuestion);
    layoutPrincipalJeu->addLayout(layoutLibelle);

    // Proposition A et B
    layoutPrincipalJeu->addStretch(1);
    layoutPropositionChoixA->addWidget(idPropositionReponseA);
    layoutPropositionChoixA->addWidget(propositionReponseA);
    layoutPropositionChoixA->addWidget(choixPropositionA);
    layoutPropositonAB->addLayout(layoutPropositionChoixA);
    layoutPropositionChoixB->addWidget(idPropositionReponseB);
    layoutPropositionChoixB->addWidget(propositionReponseB);
    layoutPropositionChoixB->addWidget(choixPropositionB);
    layoutPropositonAB->addLayout(layoutPropositionChoixB);
    layoutPropositionReponse->addLayout(layoutPropositonAB);

    // Proposition C et D
    layoutPropositionChoixC->addWidget(idPropositionReponseC);
    layoutPropositionChoixC->addWidget(propositionReponseC);
    layoutPropositionChoixC->addWidget(choixPropositionC);
    layoutPropositonCD->addLayout(layoutPropositionChoixC);
    layoutPropositionChoixD->addWidget(idPropositionReponseD);
    layoutPropositionChoixD->addWidget(propositionReponseD);
    layoutPropositionChoixD->addWidget(choixPropositionD);
    layoutPropositonCD->addLayout(layoutPropositionChoixD);
    layoutPropositionReponse->addLayout(layoutPropositonCD);

    // Ajout Layout principal
    layoutPrincipalJeu->addLayout(layoutPropositionReponse);

    // Chronomètre
    layoutPrincipalJeu->addStretch(1);
    layoutChronometre->addWidget(compteARebours);
    layoutPrincipalJeu->addLayout(layoutChronometre);
}

void IHMQuizzy::creerFenetreResultats()
{
    fenetreResultats        = new QWidget(this);
    layoutPrincipalResultat = new QVBoxLayout(fenetreResultats);
    titreFenetreResultats   = new QLabel("Résultats", this);
    titreFenetreResultats->setAlignment(Qt::AlignCenter);

    layoutPrincipalResultat->addWidget(titreFenetreResultats);

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
    // Trame $S
    connect(quizzy->getCommunicationTablette(),
            SIGNAL(questionSuivante()),
            quizzy,
            SLOT(passerQuestionSuivante()));
    // Trame $F
    connect(quizzy->getCommunicationTablette(),
            SIGNAL(finQuiz()),
            quizzy,
            SLOT(gererFinQuiz()));
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
            SIGNAL(choixParticipant()),
            this,
            SLOT(afficherChoixParticipants()));
    connect(quizzy,
            SIGNAL(questionTerminee()),
            this,
            SLOT(afficherChoixParticipants()));
    connect(quizzy,
            SIGNAL(questionSuivantePrete()),
            this,
            SLOT(afficherQuestionSuivante()));
    connect(quizzy, SIGNAL(quizTermine()), this, SLOT(afficherResultats()));
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
    afficherFenetreJeu();
}

void IHMQuizzy::afficherNbQuestions(unsigned int numeroQuestion,
                                    unsigned int nbQuestions)
{
    qDebug() << Q_FUNC_INFO << "numeroQuestion" << numeroQuestion
             << "nbQuestions" << nbQuestions;
    labelNombreTotal->setText(QString("Question ") +
                              QString::number(numeroQuestion) + QString("/") +
                              QString::number(nbQuestions));
}

void IHMQuizzy::afficherLibelleQuestion(const Question& question)
{
    labelQuestion->setText(question.getLibelle());
}

void IHMQuizzy::afficherPropositionsQuestion(const Question& question)
{
    QMap<char, QString> propositions = question.getPropositions();
    propositionReponseA->setText(propositions['A']);
    propositionReponseB->setText(propositions['B']);
    propositionReponseC->setText(propositions['C']);
    propositionReponseD->setText(propositions['D']);
}

void IHMQuizzy::afficherTempsQuestion(const Question& question)
{
    if(question.getDuree() > 0)
    {
        compteARebours->setMaximum(question.getDuree());
        compteARebours->setValue(question.getDuree());
        compteARebours->setFormat("%v s");
        compteARebours->setVisible(true);
    }
    else
    {
        compteARebours->setVisible(false);
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
    compteARebours->setStyleSheet(
      "QProgressBar::chunk#chronometre { background-color: " + couleur + "; }");
}

void IHMQuizzy::mettreAJourProposition(int numeroReponse, QString texte)
{
    qDebug() << Q_FUNC_INFO << "numeroReponse" << numeroReponse << "texte"
             << texte;

    switch(numeroReponse)
    {
        case 1:
            choixPropositionA->setStyleSheet("border: 3px solid red");
            choixPropositionA->setText(texte);
            break;
        case 2:
            choixPropositionB->setStyleSheet("border: 3px solid red");
            choixPropositionB->setText(texte);
            break;
        case 3:
            choixPropositionC->setStyleSheet("border: 3px solid red");
            choixPropositionC->setText(texte);
            break;
        case 4:
            choixPropositionD->setStyleSheet("border: 3px solid red");
            choixPropositionD->setText(texte);
            break;
        default:
            break;
    }
}

void IHMQuizzy::effacerChoixParticipants()
{
    choixPropositionA->setText("");
    choixPropositionA->setStyleSheet("");
    choixPropositionB->setText("");
    choixPropositionB->setStyleSheet("");
    choixPropositionC->setText("");
    choixPropositionC->setStyleSheet("");
    choixPropositionD->setText("");
    choixPropositionD->setStyleSheet("");
}

void IHMQuizzy::reinitialiserAffichage()
{
    qDebug() << Q_FUNC_INFO;
    effacerFenetreParticipants();
    effacerChoixParticipants();
    effacerFenetreResultats();
}

void IHMQuizzy::effacerFenetreParticipants()
{
    qDebug() << Q_FUNC_INFO;
    QLayoutItem* child;
    for(int i = layoutPrincipalParticipants->count() - 1; i >= 0; --i)
    {
        child = layoutPrincipalParticipants->itemAt(i);
        if(child->widget() != titreFenetreParticipants)
        {
            delete child->widget();
        }
    }
    infoQuiz->clear();
}

void IHMQuizzy::effacerFenetreResultats()
{
    qDebug() << Q_FUNC_INFO;
    QLayoutItem* child;
    for(int i = layoutPrincipalResultat->count() - 1; i >= 0; --i)
    {
        child = layoutPrincipalResultat->itemAt(i);
        if(child->widget() != titreFenetreResultats)
        {
            if(child->widget() != nullptr)
            {
                delete child->widget();
            }
            else if(child->layout() != nullptr)
            {
                QLayout*     layout = child->layout();
                QLayoutItem* item;
                while((item = layout->takeAt(0)) != nullptr)
                {
                    delete item->widget();
                    delete item;
                }
            }
            delete child;
        }
    }
}
