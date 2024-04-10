#include "ihmquizzy.h"
#include "quizzy.h"
#include "question.h"
#include "participant.h"
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
    QWidget(parent), quizzy(new Quizzy(this))
{
    qDebug() << Q_FUNC_INFO;

    creerFenetres();
    afficherFenetreAccueil();

#ifdef TEST_FENETRE_PARTICIPANTS
    QStringList listeParticipants;
    listeParticipants << "Participant 1"
                      << "Participant 2";
    for(int i = 0; i < listeParticipants.size(); ++i)
    {
        Participant participant(listeParticipants.at(i), i + 1);
        ajouterParticipant(participant.getNom());
    }
    afficherFenetreParticipants();
#endif

#ifdef TEST_FENETRE_JEU
    QStringList propositions;
    propositions << "Linux"
                 << "Windows"
                 << "Mac"
                 << "Minitel";
    questions.push_back(
      new Question("Quel est le meilleur OS ?", propositions));
    ajouterLibelleQuestion(*questions.last());
    // @todo etc ...
    afficherFenetreJeu();
#endif

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

void IHMQuizzy::ajouterParticipant(QString participant)
{
    QWidget*     widgetParticipant = new QWidget(this);
    QVBoxLayout* layoutParticipant = new QVBoxLayout(widgetParticipant);
    QLabel*      labelParticipant  = new QLabel(participant, this);
    layoutParticipant->setContentsMargins(100, 10, 100, 10);
    layoutParticipant->addWidget(labelParticipant);
    layoutPrincipalParticipants->addWidget(widgetParticipant);
}

void IHMQuizzy::ajouterLibelleQuestion(const Question& question)
{
    labelQuestion->setText(question.getLibelle());
}

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
    messageAttente = new QLabel("En attente des participants...", this);
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

    // creerListeParticipants(layoutPrincipal);

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
}

void IHMQuizzy::placerWidgetsFenetreParticipants()
{
    layoutPrincipalParticipants->addWidget(titreFenetreParticipants);
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
    layoutChronometre        = new QHBoxLayout();
}

void IHMQuizzy::creerWidgetsFenetreJeu()
{
    labelNombreTotal    = new QLabel("0/0", this);
    labelQuestion       = new QLabel("", this);
    propositionReponseA = new QLabel("A", this);
    propositionReponseB = new QLabel("B", this);
    propositionReponseC = new QLabel("C", this);
    propositionReponseD = new QLabel("D", this);
    labelChronometre    = new QLabel("00:00", this);
}

void IHMQuizzy::placerWidgetsFenetreJeu()
{
    layoutLibelle->addWidget(labelNombreTotal);
    layoutLibelle->addWidget(labelQuestion);
    layoutPrincipalJeu->addLayout(layoutLibelle);
    layoutPropositonAB->addWidget(propositionReponseA);
    layoutPropositonAB->addWidget(propositionReponseB);
    layoutPropositonCD->addWidget(propositionReponseC);
    layoutPropositonCD->addWidget(propositionReponseD);
    layoutPropositionReponse->addLayout(layoutPropositonAB);
    layoutPropositionReponse->addLayout(layoutPropositonCD);
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
