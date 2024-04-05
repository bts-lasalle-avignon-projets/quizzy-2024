/**
 * @file src/main.cpp
 * @brief Programme principal QUIZZY 2024
 * @author Thierry Vaira
 * @version 1.0
 */
#include <Arduino.h>
#include <BluetoothSerial.h>
#include <afficheur.h>
#include "esp_bt_main.h"
#include "esp_bt_device.h"

#define DEBUG

// Brochages
#define GPIO_LED_ROUGE   5  //!<
#define GPIO_LED_ORANGE  17 //!<
#define GPIO_LED_VERTE   16 //!<
#define GPIO_SW1         12 //!<
#define GPIO_SW2         14 //!<
#define GPIO_ENCODEUR_A  2
#define GPIO_ENCODEUR_B  4
#define GPIO_ENCODEUR_E  13
#define ADRESSE_I2C_OLED 0x3c //!< Adresse I2C de l'OLED
#define BROCHE_I2C_SDA   21   //!< Broche SDA
#define BROCHE_I2C_SCL   22   //!< Broche SCL

// Protocole (cf. Google Drive)
#define EN_TETE_TRAME    "$"
#define DELIMITEUR_CHAMP ";"
#define DELIMITEURS_FIN  "\n"
#define DELIMITEUR_DATAS ';'
#define DELIMITEUR_FIN   '\n'

#define NB_BUZZERS 4

#define BLUETOOTH
#ifdef BLUETOOTH
#define BLUETOOTH_SLAVE //!< esclave
// #define BLUETOOTH_MASTER //!< maître
BluetoothSerial ESPBluetooth;
#endif

// Évaluateur -> Pupitre
/**
 * @enum TypeTrameRecue
 * @brief Les differents types de trame reçue
 */
enum TypeTrameRecue
{
    Inconnu = -1,
    DEBUT_QUESTION, // Indiquer le numéro de question et le temps alloué pour
                    // répondre
    DESACTIVE,      // Désactiver buzzers + chronomètre
    ACTIVE,         // Activer buzzers + lancer chronomètre
    NB_TRAMES_RECUES
};

/**
 * @enum ChampTrame
 * @brief Les differents champs d'une trame
 */
enum ChampTrame
{
    ENTETE = 0,
    NUMERO_QUESTION, // Indiquer le numéro de question
    TEMPS,           // Indiquer le temps alloué pour répondre
    NB_CHAMPS
};

// Pupitre -> Évaluateur
/**
 * @enum TypeTrameEnvoyee
 * @brief Les differents types de trame envoyée
 */
enum TypeTrameEnvoyee
{
    REPONSE,      // Envoi réponse
    ACQUITTEMENT, // Acquitter
    NB_TRAMES_ENVOYEES
};

/**
 * @enum EtatBuzzer
 * @brief Les differents états
 */
enum EtatBuzzer
{
    Active = 0,
    Desactive
};

/**
 * @enum CouleurBuzzer
 * @brief Les couleurs des trous
 */
enum CouleurBuzzer
{
    Aucune = 0,
    Vert   = 1,
    Rouge  = 2,
    Bleu   = 3,
    Jaune  = 4,
    NbCouleurs
};

const String nomsTrameRecue[TypeTrameRecue::NB_TRAMES_RECUES] = {
    "Q",
    "D",
    "E"
}; //!< nom des trames recues dans le protocole

const String nomsTrameEnvoyee[TypeTrameEnvoyee::NB_TRAMES_ENVOYEES] = {
    "R",
    "A"
}; //!< nom des trames envoyées dans le protocole

EtatBuzzer    etatBuzzers     = EtatBuzzer::Desactive; //!< l'état des buzzers
unsigned long tempsMaxReponse = 0;
unsigned long tempsDebutQuestion = 0;
unsigned long tempsPrecedent     = 0;
int           reponse            = 0;
bool          repondu            = false;
bool          tirEncours         = false;      //!<
bool          bouton             = false;      //!<
bool          encodeur           = false;      //!<
bool          encodeurA          = false;      //!<
bool          encodeurB          = false;      //!<
int           numeroQuestion     = 0;          //!< de 1 à n
int           numeroBuzzer       = 0;          //!< de 1 à NB_BUZZERS
int           nbBuzzers          = NB_BUZZERS; //!< le nombre de bumpers
bool          refresh    = false; //!< demande rafraichissement de l'écran OLED
bool          antiRebond = false; //!< anti-rebond
Afficheur     afficheur(ADRESSE_I2C_OLED,
                    BROCHE_I2C_SDA,
                    BROCHE_I2C_SCL);             //!< afficheur OLED SSD1306
String        entete     = String(EN_TETE_TRAME);    // caractère séparateur
String        separateur = String(DELIMITEUR_CHAMP); // caractère séparateur
String        delimiteurFin = String(DELIMITEURS_FIN); // fin de trame

String extraireChamp(String& trame, unsigned int numeroChamp)
{
    String       champ;
    unsigned int compteurCaractere  = 0;
    unsigned int compteurDelimiteur = 0;
    char         fin                = '\n';

    if(delimiteurFin.length() > 0)
        fin = delimiteurFin[0];

    for(unsigned int i = 0; i < trame.length(); i++)
    {
        if(trame[i] == separateur[0] || trame[i] == fin)
        {
            compteurDelimiteur++;
        }
        else if(compteurDelimiteur == numeroChamp)
        {
            champ += trame[i];
            compteurCaractere++;
        }
    }

    return champ;
}

/**
 * @brief Envoie une trame réponse
 *
 */
void envoyerTrameReponse(int numeroReponse, unsigned long tempsReponse)
{
    if(repondu)
        return;
    if(etatBuzzers == EtatBuzzer::Desactive)
        return;
    char trameEnvoi[64];
    // Format : $R;NUMERO_QUESTION;NUMERO_REPONSE;TEMPS_REPONSE\n
    sprintf((char*)trameEnvoi,
            "%sR;%d;%d;%lu\n",
            entete.c_str(),
            numeroQuestion,
            numeroReponse,
            tempsReponse);
    ESPBluetooth.write((uint8_t*)trameEnvoi, strlen((char*)trameEnvoi));
    repondu = true;
#ifdef DEBUG
    String trame = String(trameEnvoi);
    trame.remove(trame.indexOf("\n"), 1);
    Serial.print("> ");
    Serial.println(trame);
#endif
}

/**
 * @brief Envoie une trame d'acquittement
 *
 */
void envoyerTrameAcquittement()
{
    char trameEnvoi[64];
    // Format : $A\n
    sprintf((char*)trameEnvoi, "%sA\n", entete.c_str());
    ESPBluetooth.write((uint8_t*)trameEnvoi, strlen((char*)trameEnvoi));
#ifdef DEBUG
    String trame = String(trameEnvoi);
    trame.remove(trame.indexOf("\n"), 1);
    Serial.print("> ");
    Serial.println(trame);
#endif
}

void IRAM_ATTR encoderA()
{
    if(antiRebond || encodeurA || etatBuzzers == EtatBuzzer::Desactive)
        return;

    encodeurA  = true;
    antiRebond = true;
}

void IRAM_ATTR encoderB()
{
    if(antiRebond || encodeurB || etatBuzzers == EtatBuzzer::Desactive)
        return;

    encodeurB  = true;
    antiRebond = true;
}

void IRAM_ATTR encoderE()
{
    if(antiRebond || encodeur || etatBuzzers == EtatBuzzer::Desactive)
        return;

    encodeur   = true;
    antiRebond = true;
}

/**
 * @brief Lit une trame via le Bluetooth
 *
 * @fn lireTrame(String &trame)
 * @param trame la trame reçue
 * @return bool true si une trame a été lue, sinon false
 */
bool lireTrame(String& trame)
{
    if(ESPBluetooth.available())
    {
        trame.clear();
        trame = ESPBluetooth.readStringUntil(DELIMITEUR_FIN);
#ifdef DEBUG
        Serial.print("< ");
        Serial.println(trame);
#endif
        trame.concat(DELIMITEUR_FIN); // remet le DELIMITEUR_FIN
        return true;
    }

    return false;
}

/**
 * @brief Vérifie si la trame reçue est valide et retorune le type de la trame
 *
 * @fn verifierTrame(String &trame)
 * @param trame
 * @return TypeTrame le type de la trame
 */
TypeTrameRecue verifierTrame(String& trame)
{
    String format;

    for(int i = 0; i < TypeTrameRecue::NB_TRAMES_RECUES; i++)
    {
        // Format : ${TYPE};...\n
        format = entete + nomsTrameRecue[i];
        //#ifdef DEBUG
        //        Serial.print("Verification trame : ");
        //        Serial.print(format);
        //#endif

        if(trame.indexOf(format) != -1)
        {
            return (TypeTrameRecue)i;
        }
        else
        {
#ifdef DEBUG
            Serial.println("");
#endif
        }
    }
#ifdef DEBUG
    Serial.println("Type de trame : inconnu");
#endif
    return Inconnu;
}

void reinitialiserAffichage()
{
    afficheur.setMessageLigne(Afficheur::Ligne1, ""); // Question
    afficheur.setMessageLigne(Afficheur::Ligne2, ""); // Début/Fin
    afficheur.setMessageLigne(Afficheur::Ligne3, ""); // Réponse
    afficheur.setMessageLigne(Afficheur::Ligne4, ""); // Temps
    refresh = true;
}

/**
 * @brief Retourne true si l'échéance de la durée fixée a été atteinte
 * @param intervalle unsigned long
 * @return bool true si la durée est arrivée à échéance
 */
bool estEcheance(unsigned long intervalle)
{
    if(etatBuzzers == EtatBuzzer::Desactive || intervalle == 0)
        return false;
    unsigned long temps = millis();
    if(temps - tempsPrecedent >= intervalle)
    {
        tempsPrecedent = temps;
        return true;
    }
    return false;
}

/**
 * @brief Initialise les ressources du programme
 *
 * @fn setup
 *
 */
void setup()
{
    Serial.begin(115200);
    while(!Serial)
        ;

    pinMode(GPIO_LED_ROUGE, OUTPUT);
    pinMode(GPIO_LED_ORANGE, OUTPUT);
    pinMode(GPIO_LED_VERTE, OUTPUT);
    // pinMode(GPIO_SW1, INPUT_PULLUP);
    // pinMode(GPIO_SW2, INPUT_PULLUP);
    pinMode(GPIO_ENCODEUR_A, INPUT_PULLUP);
    pinMode(GPIO_ENCODEUR_B, INPUT_PULLUP);
    pinMode(GPIO_ENCODEUR_E, INPUT_PULLUP);

    attachInterrupt(digitalPinToInterrupt(GPIO_ENCODEUR_A), encoderA, FALLING);
    attachInterrupt(digitalPinToInterrupt(GPIO_ENCODEUR_B), encoderB, FALLING);
    attachInterrupt(digitalPinToInterrupt(GPIO_ENCODEUR_E), encoderE, FALLING);

    digitalWrite(GPIO_LED_ROUGE, LOW);
    digitalWrite(GPIO_LED_ORANGE, LOW);
    digitalWrite(GPIO_LED_VERTE, LOW);

    afficheur.initialiser();

    String titre  = "";
    String stitre = "=====================";

#ifdef BLUETOOTH
#ifdef BLUETOOTH_MASTER
    String nomBluetooth = "iot-esp-maitre";
    ESPBluetooth.begin(nomBluetooth, true);
    const uint8_t* adresseESP32 = esp_bt_dev_get_address();
    char           str[18];
    sprintf(str,
            "%02x:%02x:%02x:%02x:%02x:%02x",
            adresseESP32[0],
            adresseESP32[1],
            adresseESP32[2],
            adresseESP32[3],
            adresseESP32[4],
            adresseESP32[5]);
    stitre = String("== ") + String(str) + String(" ==");
    titre  = nomBluetooth;
#else
    String nomBluetooth = "quizzy-p1";
    ESPBluetooth.begin(nomBluetooth);
    const uint8_t* adresseESP32 = esp_bt_dev_get_address();
    char           str[18];
    sprintf(str,
            "%02x:%02x:%02x:%02x:%02x:%02x",
            adresseESP32[0],
            adresseESP32[1],
            adresseESP32[2],
            adresseESP32[3],
            adresseESP32[4],
            adresseESP32[5]);
    stitre = String("== ") + String(str) + String(" ==");
    titre  = nomBluetooth;
#endif
#endif

#ifdef DEBUG
    Serial.println(titre);
    Serial.println(stitre);
#endif

    afficheur.setTitre(titre);
    afficheur.setSTitre(stitre);
    afficheur.afficher();

    // initialise le générateur pseudo-aléatoire
    // Serial.println(randomSeed(analogRead(34)));
    Serial.println(esp_random());
}

/**
 * @brief Boucle infinie d'exécution du programme
 *
 * @fn loop
 *
 */
void loop()
{
    String         trame;
    TypeTrameRecue typeTrame;
    char           strMessageDisplay[24];

    if(refresh)
    {
        afficheur.afficher();
        refresh = false;
    }

    if(antiRebond)
    {
        afficheur.afficher();
        delay(300);
        antiRebond = false;
    }

    if(estEcheance(tempsMaxReponse) && !repondu)
    {
        reponse = 0;
        envoyerTrameReponse(reponse, tempsMaxReponse);
        sprintf(strMessageDisplay, "-> %d", reponse);
        afficheur.setMessageLigne(Afficheur::Ligne3, String(strMessageDisplay));
        sprintf(strMessageDisplay, "-> %lu", tempsMaxReponse);
        afficheur.setMessageLigne(Afficheur::Ligne4, String(strMessageDisplay));
        refresh = true;
    }

    if(encodeur && !repondu)
    {
        // validation
        unsigned long tempsReponse = millis() - tempsDebutQuestion;
        envoyerTrameReponse(reponse, tempsReponse);
        encodeur = false;
        sprintf(strMessageDisplay, "-> %lu", tempsReponse);
        afficheur.setMessageLigne(Afficheur::Ligne4, String(strMessageDisplay));
        refresh = true;
    }

    if(encodeurA && !repondu)
    {
        // gauche
        reponse = (reponse - 1) % (NB_BUZZERS + 1);
        if(reponse == 0)
            reponse = NB_BUZZERS;
        encodeurA = false;
        sprintf(strMessageDisplay, "-> %d", reponse);
        afficheur.setMessageLigne(Afficheur::Ligne3, String(strMessageDisplay));
        refresh = true;
#ifdef DEBUG
        Serial.println(strMessageDisplay);
#endif
    }

    if(encodeurB && !repondu)
    {
        // droite
        reponse = (reponse + 1) % (NB_BUZZERS + 1);
        if(reponse == 0)
            reponse = 1;
        encodeurB = false;
        sprintf(strMessageDisplay, "-> %d", reponse);
        afficheur.setMessageLigne(Afficheur::Ligne3, String(strMessageDisplay));
        refresh = true;
#ifdef DEBUG
        Serial.println(strMessageDisplay);
#endif
    }

    if(lireTrame(trame))
    {
        typeTrame = verifierTrame(trame);
        refresh   = true;
#ifdef DEBUG
        if(typeTrame >= 0)
            Serial.println("\nTrame : " + nomsTrameRecue[typeTrame]);
#endif
        String strNumeroQuestion;
        String strTempsAlloue;
        switch(typeTrame)
        {
            case Inconnu:
                break;
            case TypeTrameRecue::DEBUT_QUESTION:
#ifdef DEBUG
                Serial.println("Nouvelle question ?");
#endif
                etatBuzzers = EtatBuzzer::Desactive;
                strNumeroQuestion =
                  extraireChamp(trame, ChampTrame::NUMERO_QUESTION);
                numeroQuestion = strNumeroQuestion.toInt();
#ifdef DEBUG
                Serial.print("Question : ");
                Serial.println(numeroQuestion);
#endif
                strTempsAlloue  = extraireChamp(trame, ChampTrame::TEMPS);
                tempsMaxReponse = strTempsAlloue.toInt() * 1000;
#ifdef DEBUG
                Serial.print("Temps max : ");
                Serial.println(tempsMaxReponse);
#endif
                repondu = false;
                envoyerTrameAcquittement();

                reinitialiserAffichage();
                digitalWrite(GPIO_LED_ROUGE, LOW);
                digitalWrite(GPIO_LED_ORANGE, HIGH);
                digitalWrite(GPIO_LED_VERTE, LOW);
                afficheur.setMessageLigne(Afficheur::Ligne1,
                                          String("Question ") +
                                            String(strNumeroQuestion));
                afficheur.afficher();
                break;
            case TypeTrameRecue::ACTIVE:
#ifdef DEBUG
                Serial.println("Activation ?");
#endif
                strNumeroQuestion =
                  extraireChamp(trame, ChampTrame::NUMERO_QUESTION);
                if(numeroQuestion == strNumeroQuestion.toInt())
                {
#ifdef DEBUG
                    Serial.print("Question : ");
                    Serial.println(numeroQuestion);
#endif
                    if(etatBuzzers == EtatBuzzer::Desactive)
                    {
                        etatBuzzers = EtatBuzzer::Active;
                        repondu     = false;
                        reponse     = 1;
                        envoyerTrameAcquittement();
                        tempsDebutQuestion = millis();
                        tempsPrecedent     = tempsDebutQuestion;
                    }

                    // reinitialiserAffichage();
                    digitalWrite(GPIO_LED_ROUGE, LOW);
                    digitalWrite(GPIO_LED_ORANGE, LOW);
                    digitalWrite(GPIO_LED_VERTE, HIGH);
                    afficheur.setMessageLigne(Afficheur::Ligne2,
                                              String("Début"));
                    afficheur.afficher();
                }
                break;
            case TypeTrameRecue::DESACTIVE:
#ifdef DEBUG
                Serial.println("Désactivation ?");
#endif
                strNumeroQuestion =
                  extraireChamp(trame, ChampTrame::NUMERO_QUESTION);
                if(numeroQuestion == strNumeroQuestion.toInt())
                {
#ifdef DEBUG
                    Serial.print("Question : ");
                    Serial.println(numeroQuestion);
#endif
                    if(etatBuzzers == EtatBuzzer::Active)
                    {
                        etatBuzzers = EtatBuzzer::Desactive;
                        envoyerTrameAcquittement();
                    }

                    // reinitialiserAffichage();
                    digitalWrite(GPIO_LED_ROUGE, HIGH);
                    digitalWrite(GPIO_LED_ORANGE, LOW);
                    digitalWrite(GPIO_LED_VERTE, LOW);
                    afficheur.setMessageLigne(Afficheur::Ligne2, String("Fin"));
                    afficheur.afficher();
                }
                break;
            default:
#ifdef DEBUG
                Serial.println("Trame invalide !");
#endif
                break;
        }
    }
}
