#ifndef PARTICIPANT_H
#define PARTICIPANT_H

#include <QString>

class Participant
{
  private:
    QString nom;

  public:
    Participant(const QString& nom);
    ~Participant();
    QString getNom() const;
};
#endif // PARTICIPANT_H
