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
#include <QBluetoothSocket>
#include <QBluetoothLocalDevice>
#include <QBluetoothDeviceDiscoveryAgent>
#include <QBluetoothDeviceInfo>

/**
 * @class CommunicationBluetooth
 * @brief Déclaration de la classe CommunicationBluetooth
 * @details Cette classe gère la connexion bluetooth
 */
class CommunicationBluetooth : public QObject
{
    Q_OBJECT

  private:
    QString           nomAppareil;
    QBluetoothAddress adresseAppareil;
    QBluetoothSocket* socket;

    QBluetoothLocalDevice appareilLocal;
    QString               nomAppareilLocal;

  public slots:
    void appareilDecouvert(const QBluetoothDeviceInfo& appareil);

  public:
    CommunicationBluetooth(QObject* parent = nullptr);
    ~CommunicationBluetooth();

    void verifierBluetooth();
    void activerBluetooth();
    void lireNomAppareil();
    void rendreAppareilVisible();
    void obtenirAppareilsConnectes();
    void demarrerScanAppareil();
};

#endif // COMMUNICATIONBLUETOOTH_H
