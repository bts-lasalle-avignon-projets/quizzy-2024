#!/bin/bash

MOSQUITTO_PUB="mosquitto_pub"
BROKER_MQTT="192.168.52.7" # raspberry pi zero
#BROKER_MQTT="127.0.0.1" # localhost
PERIODE="30"
ATTENTE="5"
PING_BROKER_MQTT="ping -c 1 ${BROKER_MQTT}"

echo -ne "\x1B[90m" # gris
echo "============================"
echo "Simulateur EcoClassroom 2024"
echo "============================"
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

# Test la présence de la commande $MOSQUITTO_PUB
echo -n "Commande $MOSQUITTO_PUB"
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
echo -n "Broker MQTT $BROKER_MQTT"
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
HUMIDITE_MIN="40"
HUMIDITE_MAX="60"
CO2_MIN="100"
CO2_MAX="1700"

declare -A modules
modules[0,0]="temperature"
modules[0,1]="humidite"
modules[0,2]="co2"
modules[1,0]="lumiere"
modules[1,1]="presence"
modules[1,2]="fenetre"

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
echo -ne "\033[0m"
while ((1))
do
    # Solution 1 : Une nouvelle donnée par période
    #topic=$(choisirTopic)
    # Solution 2 : Une nouvelle donnée par salle par période
    #for((i=0;i<$NB_SALLES;i++))
    #do
    #    topic=$(choisirTopicSalle $i)
    #    donnee=$(choisirDonnee $topic)
    #    #echo "${topic}/${donnee}"
    #    couleur=$(getCouleurSalle ${topic})
    #    echo -ne $couleur
    #    echo "${MOSQUITTO_PUB} -h ${BROKER_MQTT} -t ${topic} -m ${donnee}"
    #    echo -ne "\033[0m"
    #done
    # Solution 3 : Une nouvelle donnée par sonde par salle par période
    echo -ne "\x1B[90m" # gris
    echo "Modules SONDE"
    echo -ne "\033[0m"
    for((s=0;s<$NB_SALLES;s++))
    do
        for((c=0;c<$NB_CAPTEURS;c++))
        do
            topic=$(choisirTopicSalleSonde $s $c)
            donnee=$(choisirDonnee $topic)
            #echo "${topic}/${donnee}"
            couleur=$(getCouleurSalle ${topic})
            echo -ne $couleur
            echo "${MOSQUITTO_PUB} -h ${BROKER_MQTT} -t ${topic} -m ${donnee}"
            ${MOSQUITTO_PUB} -h ${BROKER_MQTT} -t ${topic} -m ${donnee}
            echo -ne "\033[0m"
        done
    done
    echo -ne "\x1B[31m" # rouge
    echo "*******************************************************************"
    echo -ne "\033[0m"
    sleep $ATTENTE
    echo -ne "\x1B[90m" # gris
    echo "Modules DETECTION"
    echo -ne "\033[0m"
    for((s=0;s<$NB_SALLES;s++))
    do
        for((d=0;d<$NB_DETECTEURS;d++))
        do
            topic=$(choisirTopicSalleDetecteur $s $d)
            donnee=$(choisirDonnee $topic)
            #echo "${topic}/${donnee}"
            couleur=$(getCouleurSalle ${topic})
            echo -ne $couleur
            echo "${MOSQUITTO_PUB} -h ${BROKER_MQTT} -t ${topic} -m ${donnee}"
            ${MOSQUITTO_PUB} -h ${BROKER_MQTT} -t ${topic} -m ${donnee}
            echo -ne "\033[0m"
        done
    done
    echo -ne "\x1B[31m" # rouge
    echo "*******************************************************************"
    echo -ne "\033[0m"
    sleep $PERIODE
done
echo -ne "\033[0m"
exit 0
