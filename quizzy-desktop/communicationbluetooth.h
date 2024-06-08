#ifndef COMMUNICATIONBLUETOOTH_H
#define COMMUNICATIONBLUETOOTH_H

/**
 * @file communicationbluetooth.h
 *
 * @brief Déclaration de la classe CommunicationBluetooth
 * @author Thomas HNIZDO
 * @version 1.0
 */

#include <QObject>
#include <QMap>

#include <QBluetoothAddress>
#include <QBluetoothServer>
#include <QBluetoothSocket>
#include <QBluetoothLocalDevice>
#include <QBluetoothDeviceDiscoveryAgent>
#include <QBluetoothDeviceInfo>

#define SEPARATEUR_DE_CHAMPS ';'

// Numéro des champs des trames reçues
#define TYPE_TRAME 0
// Un participant au quiz : $I;PID;NOM DU JOUEUR\n
#define PID_JOUEUR 1
#define NOM_JOUEUR 2
// Une question : $G;LIBELLE;R1;R2;R3;R4;NUMERO_REP_VALIDE;TEMPS\n
#define LIBELLE               1
#define PROPOSITION_A         2
#define PROPOSITION_B         3
#define PROPOSITION_C         4
#define PROPOSITION_D         5
#define NUMERO_REPONSE_VALIDE 6
#define TEMPS                 7
// Réponse d'un participant : $U;PID_JOUEUR;NUMÉRO_REPONSE;TEMPS_REPONSE\n
#define NUMERO_REPONSE_PARTICIPANT 2
#define TEMPS_REPONSE_PARTICIPANT  3

// Liasion série via Bluetooth
static const QString serviceUuid(
  QStringLiteral("00001101-0000-1000-8000-00805F9B34FB"));
static const QString serviceNom(QStringLiteral("quizzy-desktop"));

/**
 * @class CommunicationBluetooth
 * @brief Déclaration de la classe CommunicationBluetooth
 * @details Cette classe gère la connexion bluetooth
 */
class CommunicationBluetooth : public QObject
{
    Q_OBJECT

  private:
    QBluetoothLocalDevice appareilLocal; //!< Le périphérique Bluetooth
    QString nomAppareilLocal;            //!< Le nom du périphérique Bluetooth
    QBluetoothAddress
      adresseAppareilLocal; //!< L'adresse MAC du périphérique Bluetooth
    QBluetoothServer* serveurBluetooth; //!< Le serveur Bluetooth
    QBluetoothSocket* socketTablette; //!< La socket de communication Bluetooth
    QBluetoothServiceInfo
                     serviceInfo; //!< Les informations sur le service bluetooth
    QMap<QChar, int> formatTrame; //!< Le format des trames du protocole Quizzy
    bool             connecte;    //!< L'état de connexion avec la tablette

    void separerTrame(QString trameRecue);
    bool verifierTrame(const QString& trame) const;
    bool verifierChampsTrame(QString trame) const;
    void initialiserFormatTrame();
    void decoderTrame(QString trame);

  public:
    CommunicationBluetooth(QObject* parent = nullptr);
    ~CommunicationBluetooth();

    void verifierBluetooth();
    void activerBluetooth();
    void lireNomAppareil();
    void rendreAppareilVisible();
    void demarrerServeur();
    void arreterServeur();

  private slots:
    void connecterTablette();
    void deconnecterTablette();
    void recevoirTrame();

  signals:
    void tabletteConnectee();
    void tabletteDeconnectee();
    void debutQuiz();
    void nouveauParticipant(QString pidJoueur, QString nomJoueur);
    void nouvelleQuestion(QString     libelle,
                          QStringList propositions,
                          int         numeroReponse,
                          int         temps);
    void debutQuestion();
    void choixReponse(QString pidJoueur, int numeroReponse, int tempsReponse);
    void bonneReponse();
    void questionSuivante();
    void questionPrecedente();
    void finQuiz();
};

#endif // COMMUNICATIONBLUETOOTH_H
