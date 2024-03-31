#include "communicationbluetooth.h"
#include <QDebug>

CommunicationBluetooth::CommunicationBluetooth(QObject* parent) :
    QObject(parent)
{
    qDebug() << Q_FUNC_INFO;
}

CommunicationBluetooth::~CommunicationBluetooth()
{
    qDebug() << Q_FUNC_INFO;
}
