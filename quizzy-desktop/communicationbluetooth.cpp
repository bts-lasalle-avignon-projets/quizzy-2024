/**
 * @file communicationbluetooth.cpp
 *
 * @brief Définition de la classe CommunicationBluetooth
 * @author Thomas HNIZDO
 * @version 0.2
 */
#include "communicationbluetooth.h"
#include <QDebug>
#include <unistd.h>

/**
 * @brief Constructeur de la classe CommunicationBluetooth
 */
CommunicationBluetooth::CommunicationBluetooth(QObject* parent) :
    QObject(parent), serveurBluetooth(nullptr), socketTablette(nullptr)
{
    qDebug() << Q_FUNC_INFO;
    verifierBluetooth();
    demarrerServeur();
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
        qDebug() << Q_FUNC_INFO << "Bluetooth disponible";
        activerBluetooth();
        lireNomAppareil();
        rendreAppareilVisible();
    }
    else
    {
        qDebug() << Q_FUNC_INFO << "Bluetooth n'est pas disponible.";
    }
}

void CommunicationBluetooth::activerBluetooth()
{
    appareilLocal.powerOn();
    qDebug() << Q_FUNC_INFO << "Bluetooth activé.";
}

void CommunicationBluetooth::lireNomAppareil()
{
    nomAppareilLocal     = appareilLocal.name();
    adresseAppareilLocal = appareilLocal.address();
    QString adresseMAC   = appareilLocal.address().toString();
    qDebug() << Q_FUNC_INFO << "nomAppareilLocal" << nomAppareilLocal
             << "adresseAppareilLocal" << adresseAppareilLocal.toString();
}

void CommunicationBluetooth::rendreAppareilVisible()
{
    appareilLocal.setHostMode(QBluetoothLocalDevice::HostDiscoverable);
    qDebug() << Q_FUNC_INFO;
}

void CommunicationBluetooth::demarrerServeur()
{
    if(!appareilLocal.isValid())
        return;

    if(serveurBluetooth == nullptr)
    {
        qDebug() << Q_FUNC_INFO;
        serveurBluetooth =
          new QBluetoothServer(QBluetoothServiceInfo::RfcommProtocol, this);

        connect(serveurBluetooth,
                SIGNAL(newConnection()),
                this,
                SLOT(connecterTablette()));

        QBluetoothUuid uuid(QBluetoothUuid::Rfcomm);
        serviceInfo = serveurBluetooth->listen(uuid, serviceNom);
    }
}

void CommunicationBluetooth::arreterServeur()
{
    if(!appareilLocal.isValid() || serveurBluetooth == nullptr)
        return;

    // serviceInfo.unregisterService();

    if(socketTablette == nullptr)
        return;

    socketTablette->close();
    delete socketTablette;
    socketTablette = nullptr;
    delete serveurBluetooth;
    serveurBluetooth = nullptr;
    qDebug() << Q_FUNC_INFO;
}

void CommunicationBluetooth::connecterTablette()
{
    qDebug() << Q_FUNC_INFO;
    socketTablette = serveurBluetooth->nextPendingConnection();

    if(!socketTablette)
        return;
    connect(socketTablette,
            SIGNAL(disconnected()),
            this,
            SLOT(socketDisconnected()));
    connect(socketTablette, SIGNAL(readyRead()), this, SLOT(socketReadyRead()));

    emit tabletteConnectee();
}

void CommunicationBluetooth::deconnecterTablette()
{
    qDebug() << Q_FUNC_INFO;
    emit tabletteDeconnectee();
}

void CommunicationBluetooth::socketDisconnected()
{
    qDebug() << Q_FUNC_INFO;
    connecte = false;
    emit tabletteDeconnectee();
}

void CommunicationBluetooth::socketReadyRead()
{
    qDebug() << Q_FUNC_INFO;
    QByteArray donnees;

    while(socketTablette->bytesAvailable())
    {
        donnees += socketTablette->readAll();
        usleep(150000);
    }
}

void CommunicationBluetooth::recevoirTrame()
{
    QByteArray donneesRecues;

    donneesRecues = socketTablette->readAll();

    donneesRecues += QString(donneesRecues.data());
    qDebug() << Q_FUNC_INFO << "donneesRecues" << donneesRecues;

    // @todo Vérifier la trame

    // @todo Si la trame est valide et complète, alors procéder à son décodage
    // puis émettre les données extraites de la trame dans un signal.
    // Chaque signal est associé à un type de trame.
    // Ces signaux doivent être connectés dans l'objet ihmQuizzy pas dans cette
    // classe. Les slots peuvent être des méthodes de l'objet ihmQuizzy ou de
    // l'objet quizzy
}
