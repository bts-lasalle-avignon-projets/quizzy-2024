/**
 * @file communicationbluetooth.cpp
 *
 * @brief Définition de la classe CommunicationBluetooth
 * @author Thomas HNIZDO
 * @version 0.2
 */
#include "communicationbluetooth.h"
#include <QDebug>

/**
 * @brief Constructeur de la classe CommunicationBluetooth
 */
CommunicationBluetooth::CommunicationBluetooth(QObject* parent) :
    QObject(parent)
{
    qDebug() << Q_FUNC_INFO;
    verifierBluetooth();
}

/**
 * @brief Destructeur de la classe Participant
 */
CommunicationBluetooth::~CommunicationBluetooth()
{
    qDebug() << Q_FUNC_INFO;
}

void CommunicationBluetooth::verifierBluetooth()
{
    if(appareilLocal.isValid())
    {
        qDebug() << "Bluetooth disponible";
        activerBluetooth();
        lireNomAppareil();
        rendreAppareilVisible();
        obtenirAppareilsConnectes();
    }
    else
    {
        qDebug() << "Bluetooth n'est pas disponible.";
    }
}

void CommunicationBluetooth::activerBluetooth()
{
    appareilLocal.powerOn();
    qDebug() << "Bluetooth activé.";
}

void CommunicationBluetooth::lireNomAppareil()
{
    nomAppareilLocal = appareilLocal.name();
    qDebug() << "nom de l'appareil :" << nomAppareilLocal;
}

void CommunicationBluetooth::rendreAppareilVisible()
{
    appareilLocal.setHostMode(QBluetoothLocalDevice::HostDiscoverable);
    qDebug() << "Appareil visible";
}

void CommunicationBluetooth::obtenirAppareilsConnectes()
{
    QList<QBluetoothAddress> appareils;
    appareils = appareilLocal.connectedDevices();
    qDebug() << "Appareils connectés :" << appareils;
}

void CommunicationBluetooth::demarrerScanAppareil()
{
    QBluetoothDeviceDiscoveryAgent* agentDecouverte =
      new QBluetoothDeviceDiscoveryAgent(this);
    connect(agentDecouverte,
            SIGNAL(deviceDiscovered(QBluetoothDeviceInfo)),
            this,
            SLOT(appareilDecouvert(QBluetoothDeviceInfo)));

    agentDecouverte->start();
}

void CommunicationBluetooth::appareilDecouvert(
  const QBluetoothDeviceInfo& appareil)
{
    qDebug() << "Nouvel appareil trouvé:" << appareil.name() << '('
             << appareil.address().toString() << ')';
}
