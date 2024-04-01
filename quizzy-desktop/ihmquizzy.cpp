#include "ihmquizzy.h"
#include "quizzy.h"
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
    setWindowTitle(QString(NOM_APP) + QString(" v") + QString(VERSION_APP));

    fenetres = new QStackedWidget(this);
    creerFenetreAccueil();
    creerFenetreJeu();
    creerFenetreResultats();
    QVBoxLayout* layoutPrincipal = new QVBoxLayout(this);
    layoutPrincipal->addWidget(fenetres);

#ifdef PLEIN_ECRAN_RASPBERRY_PI
    showFullScreen();
// showMaximized();
#endif
}

IHMQuizzy::~IHMQuizzy()
{
    qDebug() << Q_FUNC_INFO;
}

void IHMQuizzy::creerFenetreAccueil()
{
    fenetreAccueil             = new QWidget();
    QVBoxLayout* layoutAccueil = new QVBoxLayout(fenetreAccueil);

    fenetres->addWidget(fenetreAccueil);
}

void IHMQuizzy::creerFenetreJeu()
{
    fenetreJeu                   = new QWidget();
    QVBoxLayout* layoutPrincipal = new QVBoxLayout(fenetreJeu);

    fenetres->addWidget(fenetreJeu);
}

void IHMQuizzy::creerFenetreResultats()
{
    fenetreResultats = new QWidget();
    fenetres->addWidget(fenetreResultats);
}

void IHMQuizzy::afficherFenetreJeu()
{
    fenetres->setCurrentWidget(fenetreJeu);
}

void IHMQuizzy::afficherFenetreResultats()
{
    fenetres->setCurrentWidget(fenetreResultats);
}
