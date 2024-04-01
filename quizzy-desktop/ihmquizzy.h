#ifndef IHMQUIZZY_H
#define IHMQUIZZY_H

/**
 * @file ihmquizzy.h
 *
 * @brief Déclaration de la classe IHMQuizzy
 * @author Thomas HNIZDO
 * @version 0.1
 */

#include <QtWidgets>

/**
 * @def NOM_APP
 * @brief Le nom de l'application
 */
#define NOM_APP "Quizzy"

/**
 * @def VERSION_APP
 * @brief La version de l'application
 */
#define VERSION_APP "0.1"

/**
 * @def PLEIN_ECRAN_RASPBERRY_PI
 * @brief Pour le mode plein écran sur la Raspberry Pi
 */
//#define PLEIN_ECRAN_RASPBERRY_PI

class Quizzy;

/**
 * @class IHMQuizzy
 * @brief Déclaration de la classe IHMQuizzy
 * @details Cette classe gère l'affichage du Quiz sur l'écran de la Raspberry Pi
 */
class IHMQuizzy : public QWidget
{
    Q_OBJECT

  private:
    Quizzy*         quizzy; //!< association vers la classe Quizzy
    QStackedWidget* fenetres;
    QWidget*        fenetreAccueil;
    QWidget*        fenetreJeu;
    QWidget*        fenetreResultats;

  private slots:
    void afficherFenetreJeu();
    void afficherFenetreResultats();

  public:
    IHMQuizzy(QWidget* parent = 0);
    ~IHMQuizzy();

    void creerFenetreAccueil();
    void creerFenetreJeu();
    void creerFenetreResultats();
};

#endif // IHMQUIZZY_H
