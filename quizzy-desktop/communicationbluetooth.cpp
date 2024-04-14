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
    QObject(parent), serveurBluetooth(nullptr), socketTablette(nullptr)
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
        // @todo Instancier un objet QBluetoothServer pour le service
        // QBluetoothServiceInfo::RfcommProtocol

        // @todo Faire la connexion signal/slot pour les connexions sur le
        // serveur (newConnection() -> connecterTablette())

        // @todo Placer l'objet QBluetoothServer en écoute de demandes de
        // connexion pour l'uuid QBluetoothUuid::Rfcomm
    }
}

void CommunicationBluetooth::arreterServeur()
{
    if(!appareilLocal.isValid() || serveurBluetooth == nullptr)
        return;

    // serviceInfo.unregisterService();

    if(socketTablette == nullptr)
        return;

    // @todo Fermer la socket socketTablette

    // @todo Libérer l'instance socketTablette

    // @todo Affecter nullptr à socketTablette

    // @todo Libérer l'instance serveurBluetooth

    // @todo Affecter nullptr à serveurBluetooth

    qDebug() << Q_FUNC_INFO;
}

void CommunicationBluetooth::connecterTablette()
{
    qDebug() << Q_FUNC_INFO;
    // @todo Récupérer la socket socketTablette suite à la connexion en attente
    // sur la socket serveurBluetooth

    // @todo Faire la connexion signal/slot pour les signaux readyRead() et
    // disconnected()

    // @todo Emettre le signal tabletteConnectee()
}

void CommunicationBluetooth::deconnecterTablette()
{
    qDebug() << Q_FUNC_INFO;
    // @todo Emettre le signal tabletteDeconnectee()
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
