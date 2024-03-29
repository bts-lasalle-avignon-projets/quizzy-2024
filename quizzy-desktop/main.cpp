#include "ihmquizzy.h"
#include <QApplication>

/**
 * @file main.cpp
 * @brief Programme principal
 * @details Crée et affiche la fenêtre principale de l'application Quizzy
 * @author Thomas HNIZDO
 * @version 0.1
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

    ihmQuizzy.show();

    return a.exec();
}
