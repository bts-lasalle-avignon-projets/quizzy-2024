#ifndef PARTICIPANT_H
#define PARTICIPANT_H

#include <QString>

class Participant
{
  private:
    QString nom;
    int     idPupitre               = 0;
    int     nombreReponsesCorrectes = 0;

  public:
    Participant(const QString& nom, int idPupitre, int nombreReponsesCorrectes);
    ~Participant();
    QString getNom() const;
    int     getIdPupitre() const;
    int     getNombreReponsesCorrectes() const;
};
#endif // PARTICIPANT_H
