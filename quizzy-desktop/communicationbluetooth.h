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

#include <QBluetoothAddress>
#include <QBluetoothServer>
#include <QBluetoothSocket>
#include <QBluetoothLocalDevice>
#include <QBluetoothDeviceDiscoveryAgent>
#include <QBluetoothDeviceInfo>

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
    bool connecte;
    bool verifierChampsTrame(const QString& trame);
    bool verifierTrame(const QString& trame);

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
    // @todo déclarer chaque signal associé à un type de trame reçu avec en
    // paramètres les données de la trame
};

#endif // COMMUNICATIONBLUETOOTH_H
