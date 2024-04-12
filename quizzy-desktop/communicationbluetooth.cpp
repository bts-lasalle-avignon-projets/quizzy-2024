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
    nomAppareilLocal   = appareilLocal.name();
    QString adresseMAC = appareilLocal.address().toString();
    qDebug() << "nom de l'appareil :" << nomAppareilLocal << "Adresse MAC"
             << adresseMAC;
}

void CommunicationBluetooth::rendreAppareilVisible()
{
    appareilLocal.setHostMode(QBluetoothLocalDevice::HostDiscoverable);
    qDebug() << "Appareil visible";
}
