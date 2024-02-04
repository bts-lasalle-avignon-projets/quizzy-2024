# Simulateur

Il y a deux simulateurs basés sur la commande `mosquitto_pub` et sur le _broker_ MQTT `mosquitto` (par défaut l'adresse IP est `192.168.52.7`).

## Simulateur n°1 : `simulateur-ecoclassroom.sh`

A chaque période (par défaut à 10s), une nouvelle donnée par sonde et par salle est émise (idem pour les états). Les données sont générées pseudo-aléatoirement dans des plages définies.

## Simulateur n°2 : `simulateur-ecoclassroom-lasalle.sh`

Ce simulateur est réglé pour qu'une seconde soit égale à 1 minute. La période est fixée par défaut à 10s donc 10 minutes simulées.

A chaque période, chaque salle envoie ses mesures (température, humidité et co2) et ses états (fenêtre, présence et lumière).

Pour donner un comportement plus réaliste, il y a 30 mesures et états qui sont définis à l'avance.

Vous pouvez les modifier comme il vous convient dans le script (tableaux `mesuresTemperature`, `mesuresHumidite`, ...). Vous devez juste respecter qu'il y ait bien 30 mesures ou états car il n'y a pas de vérification (le script ne plantera pas mais aucune donnée ne sera émise dans ce cas).

Par exemple, il est possible de simuler qu'à partir d'un certain moment, on ouvre les fenêtres dans une salle et que la température commence à baisser ainsi que le co2 etc.

Le script boucle indéfiniment (il recommence donc les séquences de 30 mesures et états).

Lorsqu'on lance le script, il récupère l'heure actuelle (pas les minutes) et il démarre son cycle.

```sh
$ ./simulateur-ecoclassroom-local.sh
=================================
Simulateur EcoClassroom 2024 v0.1
=================================
Vérification
Commande mosquitto_pub	 [Ok] 
Broker MQTT 192.168.1.20 [Ok] 

Configuration
Nb salles     : 4
Salles        : B11 B20 B21 B22 
Nb modules    : 2
Modules       : sonde detection 
Nb capteurs   : 3
Capteurs      : temperature humidite co2 
Nb détecteurs : 3
Détecteurs    : lumiere presence fenetre 
Période       : 10 secondes
Heure début   : 14 h
Ctrl-C pour quitter ...
*******************************************************************
Attention : 1 seconde = 1 minute
*******************************************************************
Il est 14h00 (mesure n°1)
*******************************************************************
Modules SONDE
mosquitto_pub -h 192.168.1.20 -t salles/B11/sonde/temperature -m 24
mosquitto_pub -h 192.168.1.20 -t salles/B11/sonde/humidite -m 54
mosquitto_pub -h 192.168.1.20 -t salles/B11/sonde/co2 -m 312
mosquitto_pub -h 192.168.1.20 -t salles/B20/sonde/temperature -m 24.1
mosquitto_pub -h 192.168.1.20 -t salles/B20/sonde/humidite -m 44
mosquitto_pub -h 192.168.1.20 -t salles/B20/sonde/co2 -m 312
mosquitto_pub -h 192.168.1.20 -t salles/B21/sonde/temperature -m 24.3
mosquitto_pub -h 192.168.1.20 -t salles/B21/sonde/humidite -m 45
mosquitto_pub -h 192.168.1.20 -t salles/B21/sonde/co2 -m 312
mosquitto_pub -h 192.168.1.20 -t salles/B22/sonde/temperature -m 24.5
mosquitto_pub -h 192.168.1.20 -t salles/B22/sonde/humidite -m 56
mosquitto_pub -h 192.168.1.20 -t salles/B22/sonde/co2 -m 1312
*******************************************************************
Modules DETECTION
mosquitto_pub -h 192.168.1.20 -t salles/B11/detection/lumiere -m 0
mosquitto_pub -h 192.168.1.20 -t salles/B11/detection/presence -m 0
mosquitto_pub -h 192.168.1.20 -t salles/B11/detection/fenetre -m 0
mosquitto_pub -h 192.168.1.20 -t salles/B20/detection/lumiere -m 0
mosquitto_pub -h 192.168.1.20 -t salles/B20/detection/presence -m 1
mosquitto_pub -h 192.168.1.20 -t salles/B20/detection/fenetre -m 1
mosquitto_pub -h 192.168.1.20 -t salles/B21/detection/lumiere -m 0
mosquitto_pub -h 192.168.1.20 -t salles/B21/detection/presence -m 0
mosquitto_pub -h 192.168.1.20 -t salles/B21/detection/fenetre -m 0
mosquitto_pub -h 192.168.1.20 -t salles/B22/detection/lumiere -m 1
mosquitto_pub -h 192.168.1.20 -t salles/B22/detection/presence -m 0
mosquitto_pub -h 192.168.1.20 -t salles/B22/detection/fenetre -m 0
*******************************************************************
Il est 14h10 (mesure n°2)
*******************************************************************
Modules SONDE
mosquitto_pub -h 192.168.1.20 -t salles/B11/sonde/temperature -m 24
mosquitto_pub -h 192.168.1.20 -t salles/B11/sonde/humidite -m 54
mosquitto_pub -h 192.168.1.20 -t salles/B11/sonde/co2 -m 455
mosquitto_pub -h 192.168.1.20 -t salles/B20/sonde/temperature -m 24
mosquitto_pub -h 192.168.1.20 -t salles/B20/sonde/humidite -m 44
mosquitto_pub -h 192.168.1.20 -t salles/B20/sonde/co2 -m 455
mosquitto_pub -h 192.168.1.20 -t salles/B21/sonde/temperature -m 24.4
mosquitto_pub -h 192.168.1.20 -t salles/B21/sonde/humidite -m 44
mosquitto_pub -h 192.168.1.20 -t salles/B21/sonde/co2 -m 455
mosquitto_pub -h 192.168.1.20 -t salles/B22/sonde/temperature -m 24.5
mosquitto_pub -h 192.168.1.20 -t salles/B22/sonde/humidite -m 57
mosquitto_pub -h 192.168.1.20 -t salles/B22/sonde/co2 -m 1455
*******************************************************************
Modules DETECTION
mosquitto_pub -h 192.168.1.20 -t salles/B11/detection/lumiere -m 0
mosquitto_pub -h 192.168.1.20 -t salles/B11/detection/presence -m 0
mosquitto_pub -h 192.168.1.20 -t salles/B11/detection/fenetre -m 0
mosquitto_pub -h 192.168.1.20 -t salles/B20/detection/lumiere -m 0
mosquitto_pub -h 192.168.1.20 -t salles/B20/detection/presence -m 1
mosquitto_pub -h 192.168.1.20 -t salles/B20/detection/fenetre -m 1
mosquitto_pub -h 192.168.1.20 -t salles/B21/detection/lumiere -m 1
mosquitto_pub -h 192.168.1.20 -t salles/B21/detection/presence -m 1
mosquitto_pub -h 192.168.1.20 -t salles/B21/detection/fenetre -m 0
mosquitto_pub -h 192.168.1.20 -t salles/B22/detection/lumiere -m 1
mosquitto_pub -h 192.168.1.20 -t salles/B22/detection/presence -m 0
mosquitto_pub -h 192.168.1.20 -t salles/B22/detection/fenetre -m 0
*******************************************************************
...
```

---
Thierry Vaira : **[thierry(dot)vaira(at)gmail(dot)com](thierry.vaira@gmail.com)**
