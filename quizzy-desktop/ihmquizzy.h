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
#include <QVector>

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
    Quizzy*            quizzy;    //!< association vers la classe Quizzy
    QVector<Question*> questions; //!< conteneur de Question pour le quiz
    // Les ressources de la GUI
    QStackedWidget* fenetres;
    // FenetreAccueil
    QWidget* fenetreAccueil;
    QLabel*  titreFenetreAccueil;
    QLabel*  messageAttente;
    // FenetreParticipants
    QWidget*     fenetreParticipants;
    QLabel*      titreFenetreParticipants;
    QVBoxLayout* layoutPrincipalParticipants;
    // FenetreJeu
    QWidget*     fenetreJeu;
    QLabel*      titreFenetreJeu;
    QVBoxLayout* layoutPrincipalJeu;
    QHBoxLayout* layoutLibelle;
    QVBoxLayout* layoutPropositionReponse;
    QHBoxLayout* layoutPropositonAB;
    QHBoxLayout* layoutPropositonCD;
    QHBoxLayout* layoutChronometre;
    QLabel*      labelNombreTotal;
    QLabel*      labelQuestion;
    QLabel*      propositionReponseA;
    QLabel*      propositionReponseB;
    QLabel*      propositionReponseC;
    QLabel*      propositionReponseD;
    QLabel*      labelChronometre;
    // FenetreResultats
    QWidget* fenetreResultats;
    QLabel*  titreFenetreResultats;

    void initialiserFenetres();
    void creerFenetres();
    void creerFenetreAccueil();
    void creerFenetreParticipants();
    void creerLayoutsFenetreParticipants();
    void creerWidgetsFenetreParticipants();
    void placerWidgetsFenetreParticipants();
    void creerFenetreJeu();
    void creerLayoutsFenetreJeu();
    void creerWidgetsFenetreJeu();
    void placerWidgetsFenetreJeu();
    void creerFenetreResultats();
    void initialiserEvenements();

  public:
    IHMQuizzy(QWidget* parent = 0);
    ~IHMQuizzy();

  public slots:
    void afficherFenetre(IHMQuizzy::Fenetre fenetre);
    void afficherFenetreAccueil();
    void afficherFenetreParticipants();
    void afficherFenetreJeu();
    void afficherFenetreResultats();
    void debuterQuiz();
    void ajouterParticipant(QString pidJoueur, QString participant);
    void ajouterLibelleQuestion(const Question& question);
    void afficherNouvelleQuestion(QString     libelle,
                                  QStringList propositions,
                                  int         reponseValide,
                                  int         temps);
};

#endif // IHMQUIZZY_H
