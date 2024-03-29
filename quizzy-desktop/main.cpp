#include "ihmquizzy.h"
#include <QApplication>

int main(int argc, char* argv[])
{
    QApplication a(argc, argv);
    IHMQuizzy    w;
    w.show();

    return a.exec();
}
