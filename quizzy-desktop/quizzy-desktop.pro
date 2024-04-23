#-------------------------------------------------
#
# Project created by QtCreator 2024-03-29T20:59:46
#
#-------------------------------------------------

QT       += \
         core gui \
         bluetooth

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = quizzy-desktop
TEMPLATE = app

DEFINES += QT_DEPRECATED_WARNINGS
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

CONFIG += c++11

SOURCES += \
        main.cpp \
        ihmquizzy.cpp \
        participant.cpp \
        quizzy.cpp \
        question.cpp \
        communicationbluetooth.cpp

HEADERS += \
        ihmquizzy.h \
        participant.h \
        quizzy.h \
        question.h \
        communicationbluetooth.h

RESOURCES += \
    quizzy.qrc

DISTFILES += \
    style.qss

# Les defines pour la version release (sans debug)
CONFIG(release, debug|release):DEFINES+=QT_NO_DEBUG_OUTPUT
# Les defines pour la version debug
#CONFIG(debug, debug|release):DEFINES+=TEST_FENETRE_JEU
