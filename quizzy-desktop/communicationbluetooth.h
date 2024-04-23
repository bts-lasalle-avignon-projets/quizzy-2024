#ifndef COMMUNICATIONBLUETOOTH_H
#define COMMUNICATIONBLUETOOTH_H

/**
 * @file communicationbluetooth.h
 *
 * @brief Déclaration de la classe CommunicationBluetooth
 * @author Thomas HNIZDO
 * @version 0.2
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
    bool             connecte;
    QMap<QChar, int> formatTrame;

    bool verifierTrame(const QString& trame);
    bool verifierChampsTrame(QString trame);
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
    void nouvelleQuestion(QString             libelle,
                          QMap<char, QString> propositions,
                          int                 numeroBonneReponse,
                          int                 temps);
    void debutQuestion();
    void choixReponse(QString pidJoueur, int numeroReponse, int tempsReponse);
    void bonneReponse();
    void questionSuivante();
    void questionPrecedente();
    void finQuiz();
    // @todo déclarer chaque signal associé à un type de trame reçu avec en
    // paramètres les données de la trame
};

#endif // COMMUNICATIONBLUETOOTH_H
