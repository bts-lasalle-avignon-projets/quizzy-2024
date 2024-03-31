#ifndef COMMUNICATIONBLUETOOTH_H
#define COMMUNICATIONBLUETOOTH_H

#include <QObject>
#include <QBluetoothAddress>
#include <QBluetoothSocket>

class CommunicationBluetooth : public QObject
{
    Q_OBJECT

  private:
    QString           nomAppareil;
    QBluetoothAddress adresseAppareil;
    QBluetoothSocket* socket;

  public:
    CommunicationBluetooth(QObject* parent = nullptr);
    ~CommunicationBluetooth();
};

#endif // COMMUNICATIONBLUETOOTH_H
