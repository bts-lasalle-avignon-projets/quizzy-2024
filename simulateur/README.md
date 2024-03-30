# Simulateur QUIZZY 2024

## Présentation du protocole implanté dans le simulateur ESP'ACE

Ce document présente rapidement le fonctionnement du simulateur ainsi que le protocole implémenté.

> Le protocole complet est disponible dans Google Drive.

## Configuration du simulateur

Valeur par défaut :

```cpp
#define NB_BUZZERS 4
```

## Identification des périphériques

Nom des périphériques Bluetooth :

- \"quizzy-écran\" pour la RPI et,
- \"quizzy-pn\" où \"n\" est le numéro de pupitre

## platform.ini

```ini
[env:lolin32]
platform = espressif32
board = lolin32
framework = arduino
lib_deps =
  thingpulse/ESP8266 and ESP32 OLED driver for SSD1306 displays @ ^4.2.0
upload_port = /dev/ttyUSB0
upload_speed = 115200
monitor_port = /dev/ttyUSB0
monitor_speed = 115200
```

## Protocole

- Auteurs : Mathéo Galesi, Thomas Hnizdo, Jules Hillion
- Version : 0.1
- Trame ASCII
- Délimiteur de début : **\$**
- Délimiteur de fin : **\\n**
- Séparateur de champs : **;**

### Évaluateur -\> Interface de jeu

| **Type**       | **Format**     | **Description**   | **Exemple**    |
|----------------|:--------------:|:-----------------:|:--------------:|
| Lancer un quiz | **\$L\\n**     | Signaler le début d’un quiz    | **\$L\\n** |
| Indiquer un participant au quiz | **\$I;PID;NOM DU JOUEUR\\n**  | PID: string (l’identifiant du pupitre) NOM (ou pseudo) DU JOUEUR: string | **\$I;P1;Robert\\n** |
| Envoyer une question | **\$Q;LIBELLE;R1;R2;R3;R4;NUMERO_REP_VALIDE;TEMPS\\n** | LIBELLE : string (une question) R1, R2, R3 et R4: string (propositions) NUMERO_REP_VALIDE : int (1 à 4) TEMPS : int (en secondes, si 0 alors la question n’a pas de temps limite) | **\$Q;Quelle est le meilleur OS ?;Linux;Windows;Mac;Minitel;4;10\\n** La réponse valide est la n°4, le Minitel | 
| Indiquer la réponse choisie par un joueur | **\$R;PID JOUEUR;NUMÉRO REPONSE;TEMPS REPONSE\\n** | PID: string NUMÉRO REPONSE : 1 à 4 TEMPS RÉPONSE : en ms | **\$R;P1;1;3500\\n** Choix réponse n°1 pour joueur du pupitre 1 en 3500 ms |
| Afficher la réponse | **\$A\\n** | Afficher la réponse à la question actuelle | **\$A\\n** |
| Passer à la question suivante | **\$S\\n** |  Afficher question suivante |**\$S\\n** |
| Revenir à la question précédente | **\$P\\n** | Afficher question précédente | **\$P\\n** |
| Finir un quiz | **\$F\\n** | Signaler la fin d’un quiz | **\$F\\n** |

### Évaluateur -\> Pupitre

| **Type**       | **Format**     | **Description**   | **Exemple**    |
|----------------|:--------------:|:-----------------:|:--------------:|
| Indiquer le numéro de question et le temps alloué pour répondre | **\$Q;NUMERO_QUESTION;TEMPS\\n** | NUMERO_QUESTION : de 1 à n TEMPS QUESTION : en secondes si 0 alors la question n'a pas de temps limite | **\$Q;1;30\\n** Question n°**1** avec **30** secondes pour cette question |
| Désactiver buzzers + chronomètre | **\$D;NUMERO_QUESTION\\n** | Désactiver les buzzers et arrêter le chronomètre si besoin | **\$D;1\\n**   |
| Activer buzzers + lancer chronomètre      | **\$E;NUMERO_QUESTION\\n** | Activer les buzzers pour ce numéro de question et lancer le chronomètre si besoin           | **\$E;1\\n**   |

### Pupitre -\> Évaluateur

| **Type**       | **Format**     | **Description**   | **Exemple**    |
|----------------|:--------------:|:-----------------:|:--------------:|
| Envoi réponse  | **\$R;NUMERO_QUESTION;NUMERO_REPONSE;TEMPS_REPONSE\\n** | NUMERO_QUESTION: de 1 à n NUMERO_REPONSE : **1 à 4** TEMPS_REPONSE : **en ms** si 0 alors le temps n'a pas été mesuré | **\$R;1;2;17000\\n** |
| Acquittement   | **\$A\\n**     | Acquitter toutes les trames de l'évaluateur | **\$A\\n**     |

## Auteur

- Thierry Vaira <<tvaira@free.fr>>
