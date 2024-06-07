#include "ihmquizzy.h"
#include <QApplication>
#include <QFontDatabase>

/**
 * @file main.cpp
 * @brief Programme principal
 * @details Crée et affiche la fenêtre principale de l'application Quizzy
 * @author Thomas HNIZDO
 * @version 1.0
 *
 * @param argc
 * @param argv[]
 * @return int
 *
 */
int main(int argc, char* argv[])
{
    QApplication a(argc, argv);
    IHMQuizzy    ihmQuizzy;
    QFontDatabase::addApplicationFont(":/fonts/REM-Bold.ttf");
    QFile fichier(":/qss/style.qss");
    if(fichier.open(QFile::ReadOnly))
    {
        QString feuilleStyle = QLatin1String(fichier.readAll());
        a.setStyleSheet(feuilleStyle);
    }

    ihmQuizzy.show();

    return a.exec();
}
