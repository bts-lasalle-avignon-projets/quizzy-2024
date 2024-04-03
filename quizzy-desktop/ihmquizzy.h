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
 * @def TAILLE_LARGEUR_ECRAN_MIN
 * @brief Pour le mode plein écran sur la Raspberry Pi
 */
#define TAILLE_LARGEUR_ECRAN_MIN 1920

/**
 * @def TAILLE_HAUTEUR_ECRAN_MIN
 * @brief Pour le mode plein écran sur la Raspberry Pi
 */
#define TAILLE_HAUTEUR_ECRAN_MIN 1024

/**
 * @def PLEIN_ECRAN_RASPBERRY_PI
 * @brief Pour le mode plein écran sur la Raspberry Pi
 */
//#define PLEIN_ECRAN_RASPBERRY_PI

class Quizzy;
class Question;

/**
 * @class IHMQuizzy
 * @brief Déclaration de la classe IHMQuizzy
 * @details Cette classe gère l'affichage du Quiz sur l'écran de la Raspberry Pi
 */
class IHMQuizzy : public QWidget
{
    Q_OBJECT

  public:
    /**
     * @enum Fenetre
     * @brief Définit les différentes fenêtres de la GUI
     *
     */
    enum Fenetre
    {
        FenetreAccueil = 0,
        FenetreParticipants,
        FenetreJeu,
        FenetreResultats,
        NbEcrans
    };

  private:
    Quizzy*   quizzy; //!< association vers la classe Quizzy
    Question* question;
    // Les ressources de la GUI
    QStackedWidget* fenetres;
    QWidget*        fenetreAccueil;
    QLabel*         titreFenetreAccueil;
    QWidget*        fenetreParticipants;
    QLabel*         titreFenetreParticipants;
    QWidget*        fenetreJeu;
    QLabel*         titreFenetreJeu;
    QWidget*        fenetreResultats;
    QLabel*         titreFenetreResultats;
    QLabel*         messageAttente;

    QVBoxLayout* layoutPrincipal;
    QHBoxLayout* layoutLibelle;
    QVBoxLayout* layoutPropositionReponse;
    QHBoxLayout* layoutPropositonAB;
    QHBoxLayout* layoutPropositonCD;
    QHBoxLayout* layoutChronometre;

    QLabel* labelNombreTotal;
    QLabel* labelQuestion;
    QLabel* propositionReponseA;
    QLabel* propositionReponseB;
    QLabel* propositionReponseC;
    QLabel* propositionReponseD;
    QLabel* labelChronometre;

    void creerFenetres();
    void creerFenetreAccueil();
    void creerFenetreParticipants();
    void creerFenetreJeu();
    void creerFenetreResultats();
    void initialiserFenetres();
    void creerListeParticipants(QVBoxLayout* layoutPrincipal);

    void creerLayouts();
    void creerLabels();

  public:
    IHMQuizzy(QWidget* parent = 0);
    ~IHMQuizzy();

  public slots:
    void afficherFenetre(IHMQuizzy::Fenetre fenetre);
    void afficherFenetreAccueil();
    void afficherFenetreParticipants();
    void afficherFenetreJeu();
    void afficherFenetreResultats();
};

#endif // IHMQUIZZY_H
