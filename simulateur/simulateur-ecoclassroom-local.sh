#!/bin/bash

VERSION="0.1"
MOSQUITTO_PUB="mosquitto_pub"
BROKER_MQTT="192.168.1.20" # pc
#BROKER_MQTT="127.0.0.1" # localhost
HEURE_DEBUT=$(date +%k)
PERIODE=${1-"10"}
ATTENTE="1"
PING_BROKER_MQTT="ping -c 1 ${BROKER_MQTT}"

echo -ne "\x1B[93m" # jaune
echo "================================="
echo "Simulateur EcoClassroom 2024 v$VERSION"
echo "================================="
echo -ne "\033[0m"

function afficherMessageMosquittoPub
{
    echo "La commande « $MOSQUITTO_PUB » n'a pas été trouvée, vous pouvez l'installer :"
    echo "sudo apt-get install mosquitto-clients"
    echo -ne "\033[0m"
    #exit 1
}

function afficherMessageBrokerMQQTT
{
    echo "Le broker MQTT « $BROKER_MQTT » n'a pas été trouvée, vous pouvez l'installer localement :"
    echo "sudo apt-get install mosquitto"
    echo -ne "\033[0m"
    #exit 2
}

echo -ne "\x1B[93m" # jaune
echo "Vérification"
echo -ne "\033[0m"

# Test la présence de la commande $MOSQUITTO_PUB
echo -ne "\x1B[93m" # jaune
echo -n "Commande $MOSQUITTO_PUB"
echo -ne "\033[0m"
if which $MOSQUITTO_PUB >/dev/null; then
    echo -ne "\x1B[32m\t" # vert
    echo " [Ok] "
else
    echo -ne "\x1B[31m\t" # rouge
    echo " [Erreur] "
    afficherMessageMosquittoPub
fi
echo -ne "\033[0m"

# Test la présence du broker MQTT $BROKER_MQTT
echo -ne "\x1B[93m" # jaune
echo -n "Broker MQTT $BROKER_MQTT"
echo -ne "\033[0m"
${PING_BROKER_MQTT} > /dev/null 2>&1
res=$?
if [ $res -eq 0 ]
then
    echo -ne "\x1B[32m" # vert
    echo " [Ok] "
else
    echo -ne "\x1B[31m" # rouge
    echo " [Erreur] "
    afficherMessageBrokerMQQTT
fi
echo -ne "\033[0m"
echo ""

# MQTT
# {racine}/{salle}/{module}/{donnee}
# {racine} = salles
# {module} = sonde | detection
# {donnee} = temperature | humidite | co2 | lumiere | presence | fenetre

TOPIC_RACINE="salles"
SALLES=("B11" "B20" "B21" "B22")
NB_SALLES=${#SALLES[@]}
MODULES=("sonde" "detection")
MODULE_SONDE="0"
MODULE_DETECTION="1"
NB_MODULES=${#MODULES[@]}
CAPTEURS=("temperature" "humidite" "co2")
NB_CAPTEURS=${#CAPTEURS[@]}
DETECTEURS=("lumiere" "presence" "fenetre")
NB_DETECTEURS=${#DETECTEURS[@]}
NB_DONNEES=3
CHAMP_SALLE_TOPIC="2"
CHAMP_MODULE_TOPIC="3"
CHAMP_DONNEE_TOPIC="4"
TEMPERATURE_MIN="10"
TEMPERATURE_MAX="35"
HUMIDITE_MIN="0"
HUMIDITE_MAX="100"
CO2_MIN="100"
CO2_MAX="1700"

declare -A modules
modules[0,0]="temperature"
modules[0,1]="humidite"
modules[0,2]="co2"
modules[1,0]="lumiere"
modules[1,1]="presence"
modules[1,2]="fenetre"

NB_MESURES=30

declare -A mesuresTemperature=(
    ["B11"]="24 24 24.2 24.1 24.2 24.4 24.5 24.4 24.6 24.7 24.5 24.5 24.6 24.7 24.7 24.8 24.9 25.1 25.2 25.4 24.1 24 24.2 24.3 24.3 24.5 24.6 24.8 24.8 24.9"
    ["B20"]="24.1 24 24.2 24.3 24.3 24.5 24.6 24.8 24.8 24.9 24.3 24.4 24.4 24.5 24.5 24.7 24.8 24.9 25 25.2 24.3 24.4 24.4 24.5 24.5 24.7 24.8 24.9 25 25.2"
    ["B21"]="24.3 24.4 24.4 24.5 24.5 24.7 24.8 24.9 25 25.2 24 24 24.2 24.1 24.2 24.4 24.5 24.4 24.6 24.7 24 24 24.2 24.1 24.2 24.4 24.5 24.4 24.6 24.7"
    ["B22"]="24.5 24.5 24.6 24.7 24.7 24.8 24.9 25.1 25.2 25.4 24.5 24.5 24.6 24.7 24.7 24.8 24.9 25.1 25.2 25.4 24.5 24.5 24.6 24.7 24.7 24.8 24.9 25.1 25.2 25.4"
)

declare -A mesuresHumidite=(
    ["B11"]="54 54 55 54 55 56 57 58 59 60 59 58 57 56 55 54 55 54 54 44 44 45 44 45 46 47 48 49 50 51"
    ["B20"]="44 44 45 44 45 46 47 48 49 48 47 46 45 44 43 42 41 40 39 38 39 40 41 42 43 44 45 46 47 48"
    ["B21"]="45 44 45 46 47 48 49 51 51 52 54 54 55 54 55 56 57 58 59 60 60 61 62 63 64 65 66 67 68 70"
    ["B22"]="56 57 58 59 60 61 62 63 64 65 65 66 67 68 65 66 67 68 65 66 67 68 65 66 67 68 65 66 67 66"
)

declare -A mesuresCO2=(
    ["B11"]="312 455 380 658 889 905 1100 1540 1422 1669 1702 1698 1805 1850 1630 1496 1355 1255 1104 1040 922 669 702 698 512 555 480 404 489 458"
    ["B20"]="312 455 380 658 889 905 999 997 822 969 1002 698 1005 1050 1130 896 855 755 804 840 622 669 702 698 512 555 480 458 489 999"
    ["B21"]="312 455 380 658 889 905 900 940 922 969 702 698 805 850 630 496 355 255 504 540 922 669 702 698 512 555 480 458 489 540"
    ["B22"]="1312 1455 1380 1658 1589 1905 1701 1740 1722 1769 1702 1798 1805 1850 1730 1896 1355 1755 1704 1940 1922 1869 1702 1698 1712 1755 1780 1758 1789 1705"
)

declare -A etatsFenetre=(
    ["B11"]="0 0 0 0 0 1 1 1 1 1 1 1 1 1 0 0 0 0 0 0 0 0 0 0 0 1 1 1 1 1"
    ["B20"]="1 1 1 1 0 0 0 0 0 0 1 1 1 1 0 0 0 0 0 0 1 1 1 1 0 0 0 0 0 0"
    ["B21"]="0 0 1 1 1 1 0 0 0 0 0 0 1 1 1 1 0 0 0 0 0 0 1 1 1 1 0 0 0 0"
    ["B22"]="0 0 0 0 0 0 1 1 1 1 0 0 0 0 0 0 1 1 1 1 0 0 0 0 0 0 1 1 1 1"
)

declare -A etatsPresence=(
    ["B11"]="0 0 0 0 0 1 1 1 1 1 1 1 1 1 0 0 0 0 0 0 0 0 0 0 0 1 1 1 1 1"
    ["B20"]="1 1 1 1 0 0 0 0 0 0 1 1 1 1 0 0 0 0 0 0 1 1 1 1 0 0 0 0 0 0"
    ["B21"]="0 1 1 1 1 1 0 0 0 0 0 1 1 1 1 1 0 0 0 0 0 1 1 1 1 1 0 0 0 0"
    ["B22"]="0 0 0 0 0 0 1 1 1 1 0 0 0 0 0 0 1 1 1 1 0 0 0 0 0 0 1 1 1 1"
)

declare -A etatsLumiere=(
    ["B11"]="0 0 0 0 0 1 1 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 1 1 1"
    ["B20"]="0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0"
    ["B21"]="0 1 1 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 1 0"
    ["B22"]="1 1 1 1 1 1 0 0 0 0 1 1 1 1 1 1 0 0 0 0 1 1 1 1 1 1 0 0 0 0"
)

function getMesureTemperature()
{
    # $1 : salle
    # $2 : periode mesure
    index=$2
    set ${mesuresTemperature[$1]}
    echo ${!index}
}

function getMesureHumidite()
{
    # $1 : salle
    # $2 : periode mesure
    index=$2
    set ${mesuresHumidite[$1]}
    echo ${!index}
}

function getMesureCO2()
{
    # $1 : salle
    # $2 : periode mesure
    index=$2
    set ${mesuresCO2[$1]}
    echo ${!index}
}

function getEtatFenetre()
{
    # $1 : salle
    # $2 : periode mesure
    index=$2
    set ${etatsFenetre[$1]}
    echo ${!index}
}

function getEtatPresence()
{
    # $1 : salle
    # $2 : periode mesure
    index=$2
    set ${etatsPresence[$1]}
    echo ${!index}
}

function getEtatLumiere()
{
    # $1 : salle
    # $2 : periode mesure
    index=$2
    set ${etatsLumiere[$1]}
    echo ${!index}
}

function getDonnee()
{
    # $1 : topic
    # $2 : periode mesure
    salle=$(echo $1 | cut -d'/' -f${CHAMP_SALLE_TOPIC})
    donnee=$(echo $1 | cut -d'/' -f${CHAMP_DONNEE_TOPIC})
    index=$2
    case $donnee in
	"fenetre") etat=$(getEtatFenetre ${salle} ${index});echo "$etat";;
    "presence") etat=$(getEtatPresence ${salle} ${index});echo "$etat";;
    "lumiere") etat=$(getEtatLumiere ${salle} ${index});echo "$etat";;
	"temperature") mesure=$(getMesureTemperature ${salle} ${index});echo "$mesure";;
    "humidite") mesure=$(getMesureHumidite ${salle} ${index});echo "$mesure";;
    "co2") mesure=$(getMesureCO2 ${salle} ${index});echo "$mesure";;
	#*) echo "donnée inconnue !";;
    esac
}

function choisirTopic()
{
    nbSalles=$((NB_SALLES-1))
    nbModules=$((NB_MODULES-1))
    nbDonnees=$((NB_DONNEES-1))
    numeroSalle=$(shuf -i 0-${nbSalles} -n 1)
    typeModule=$(shuf -i 0-${nbModules} -n 1)
    typeDonnee=$(shuf -i 0-${nbDonnees} -n 1)
    echo "${TOPIC_RACINE}/${SALLES[$numeroSalle]}/${MODULES[$typeModule]}/${modules[$typeModule,$typeDonnee]}"
}

function choisirTopicSalle()
{
    numeroSalle=$1
    nbModules=$((NB_MODULES-1))
    nbDonnees=$((NB_DONNEES-1))
    typeModule=$(shuf -i 0-${nbModules} -n 1)
    typeDonnee=$(shuf -i 0-${nbDonnees} -n 1)
    echo "${TOPIC_RACINE}/${SALLES[$numeroSalle]}/${MODULES[$typeModule]}/${modules[$typeModule,$typeDonnee]}"
}

function choisirTopicSalleSonde()
{
    numeroSalle=$1
    typeModule=$MODULE_SONDE
    typeDonnee=$2
    echo "${TOPIC_RACINE}/${SALLES[$numeroSalle]}/${MODULES[$typeModule]}/${modules[$typeModule,$typeDonnee]}"
}

function choisirTopicSalleDetecteur()
{
    numeroSalle=$1
    typeModule=$MODULE_DETECTION
    typeDonnee=$2
    echo "${TOPIC_RACINE}/${SALLES[$numeroSalle]}/${MODULES[$typeModule]}/${modules[$typeModule,$typeDonnee]}"
}

function choisirDonnee()
{
    donnee=$(echo $1 | cut -d'/' -f${CHAMP_DONNEE_TOPIC})
    case $donnee in
	"lumiere"|"presence"|"fenetre") etat=$(shuf -i 0-1 -n 1);echo "$etat";;
	"temperature") mesure=$(shuf -i ${TEMPERATURE_MIN}-${TEMPERATURE_MAX} -n 1);echo "$mesure";;
    "humidite") mesure=$(shuf -i ${HUMIDITE_MIN}-${HUMIDITE_MAX} -n 1);echo "$mesure";;
    "co2") mesure=$(shuf -i ${CO2_MIN}-${CO2_MAX} -n 1);echo "$mesure";;
	#*) echo "donnée inconnue !";;
    esac
}

echo -ne "\x1B[93m" # jaune
echo "Configuration"
echo "Nb salles     : ${NB_SALLES}"
echo -n "Salles        : "
for((i=0;i<${NB_SALLES};i++))
do
    echo -n "${SALLES[$i]} "
done
echo ""
echo "Nb modules    : ${NB_MODULES}"
echo -n "Modules       : "
for((i=0;i<${NB_MODULES};i++))
do
    echo -n "${MODULES[$i]} "
done
echo ""
echo "Nb capteurs   : ${NB_CAPTEURS}"
echo -n "Capteurs      : "
for((i=0;i<${NB_CAPTEURS};i++))
do
    echo -n "${CAPTEURS[$i]} "
done
echo ""
echo "Nb détecteurs : ${NB_DETECTEURS}"
echo -n "Détecteurs    : "
for((i=0;i<${NB_DETECTEURS};i++))
do
    echo -n "${DETECTEURS[$i]} "
done
echo ""
echo -n "Période       : ${PERIODE} secondes"
echo ""
echo -n "Heure début   : ${HEURE_DEBUT} h"
echo ""
echo "Ctrl-C pour quitter ..."
echo -ne "\033[0m"

declare -A couleursSalle=(
    ["B11"]="\x1B[95m"
    ["B20"]="\x1B[96m"
    ["B21"]="\x1B[92m"
    ["B22"]="\x1B[94m"
)

function getCouleurSalle()
{
    couleur=$(echo $1 | cut -d'/' -f${CHAMP_SALLE_TOPIC})
    echo ${couleursSalle[$couleur]}
}

echo -ne "\x1B[31m" # rouge
echo "*******************************************************************"
echo "Attention : 1 seconde = 1 minute"
echo "*******************************************************************"
echo -ne "\033[0m"

heure=0
MINUTE=0
heureActuelle=$(date +%k)
modulo=$((60/$PERIODE))
while ((1))
do
    for((n=1;n<=$NB_MESURES;n++))
    do
        # changement d'heure ?
        #changement=$(($n%$modulo))
        #echo "heure = $heure"
        #echo "MINUTE = $MINUTE"
        changement=$((($PERIODE*$MINUTE)%60))
        #echo "changement = $changement"
        if [ "$changement" -eq "50" ]
        then
            heureActuelle=$(($heureActuelle+1))
            #echo "->> heureActuelle = $heureActuelle"
            heure=0
        fi
        # changement de minute ?
        #echo "heure = $heure"
        #echo "MINUTE = $MINUTE"
        #echo "n = $n"
        changement=$(($n%$modulo))
        #echo "changement = $changement"
        if [ "$changement" -eq "1" ]
        then
            MINUTE=0
            #echo "-> heureActuelle = $heureActuelle"
            #echo "-> MINUTE = $MINUTE"
        else
            MINUTE=$(($n-1))
        fi
        # changement de journée ?
        changementHeure=$((($heureActuelle+$heure)%24))
        #echo "heure = $(($heureActuelle+$heure))"
        #echo "changementHeure = $changementHeure"
        if [ "$changementHeure" -eq "0" ]
        then
            heureActuelle=0
            heure=0
        fi
        printf "Il est %dh%02d (mesure n°%d)\n" "$(($heureActuelle+$heure))" "$((($PERIODE*$MINUTE)%60))" "$n"

        echo -ne "\x1B[90m" # gris
        echo "*******************************************************************"
        echo -ne "\033[0m"
        #echo -ne "\x1B[90m" # gris
        echo "Modules SONDE"
        #echo -ne "\033[0m"
        for((s=0;s<$NB_SALLES;s++))
        do
            for((c=0;c<$NB_CAPTEURS;c++))
            do
                topic=$(choisirTopicSalleSonde $s $c)
                donnee=$(getDonnee $topic $n)
                couleur=$(getCouleurSalle ${topic})
                echo -ne $couleur
                echo "${MOSQUITTO_PUB} -h ${BROKER_MQTT} -t ${topic} -m ${donnee}"
                ${MOSQUITTO_PUB} -h ${BROKER_MQTT} -t ${topic} -m ${donnee}
                echo -ne "\033[0m"
            done
        done
        echo -ne "\x1B[90m" # gris
        echo "*******************************************************************"
        echo -ne "\033[0m"
        sleep $ATTENTE
        #echo -ne "\x1B[90m" # gris
        echo "Modules DETECTION"
        #echo -ne "\033[0m"
        for((s=0;s<$NB_SALLES;s++))
        do
            for((d=0;d<$NB_DETECTEURS;d++))
            do
                topic=$(choisirTopicSalleDetecteur $s $d)
                donnee=$(getDonnee $topic $n)
                couleur=$(getCouleurSalle ${topic})
                echo -ne $couleur
                echo "${MOSQUITTO_PUB} -h ${BROKER_MQTT} -t ${topic} -m ${donnee}"
                ${MOSQUITTO_PUB} -h ${BROKER_MQTT} -t ${topic} -m ${donnee}
                echo -ne "\033[0m"
            done
        done
        echo -ne "\x1B[90m" # gris
        echo "*******************************************************************"
        echo -ne "\033[0m"
        sleep $PERIODE
    done
    ((heure++))
done
echo -ne "\033[0m"
exit 0
