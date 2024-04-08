--- LDD (langage de définition de données)

--- Supprime les tables

DROP TABLE IF EXISTS resultats;
DROP TABLE IF EXISTS questions;
DROP TABLE IF EXISTS questionnaires;
DROP TABLE IF EXISTS evaluations;
DROP TABLE IF EXISTS propositions;
DROP TABLE IF EXISTS reponses;
DROP TABLE IF EXISTS participants;
DROP TABLE IF EXISTS niveaux;
DROP TABLE IF EXISTS evaluateurs;

--- Table evaluateurs

CREATE TABLE IF NOT EXISTS evaluateurs (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT UNIQUE NOT NULL);

--- Table participants

CREATE TABLE IF NOT EXISTS participants (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT UNIQUE NOT NULL);

--- Table niveaux (Facile, Moyen, Difficile)

CREATE TABLE IF NOT EXISTS niveaux (id INTEGER PRIMARY KEY AUTOINCREMENT, difficulte TEXT UNIQUE NOT NULL);

--- Table evaluations
--- le nom et la difficulté d'une évaluation réalisée par un évaluateur

CREATE TABLE IF NOT EXISTS evaluations (id INTEGER PRIMARY KEY AUTOINCREMENT, nom TEXT NOT NULL, idDifficulte INTEGER, idEvaluateur INTEGER, horodatage DATETIME NOT NULL, FOREIGN KEY (idDifficulte) REFERENCES niveaux(id), FOREIGN KEY (idEvaluateur) REFERENCES evaluateurs(id) ON DELETE CASCADE);

--- Table questions
--- le libellé d'une question avec le nombre de points attribués et le temps pour répondre

CREATE TABLE IF NOT EXISTS questions (id INTEGER PRIMARY KEY AUTOINCREMENT, libelle TEXT NOT NULL, points DOUBLE, temps INTEGER);

--- Table propositions
--- le libellé d'une proposition

CREATE TABLE IF NOT EXISTS propositions (id INTEGER PRIMARY KEY AUTOINCREMENT, libelle TEXT NOT NULL);

--- Table reponses
--- réponse d'une proposition d'un participant à une question d'un questionnaire (avec le temps mis pour répondre et s'il a bien répondu)

CREATE TABLE IF NOT EXISTS reponses (idEvaluation INTEGER, idParticipant INTEGER, idQuestion INTEGER, idProposition INTEGER, temps INTEGER, correct BOOLEAN NOT NULL CHECK (correct IN (0, 1)), FOREIGN KEY (idEvaluation) REFERENCES evaluations(id) ON DELETE CASCADE, FOREIGN KEY (idParticipant) REFERENCES participants(id) ON DELETE CASCADE, FOREIGN KEY (idProposition) REFERENCES propositions(id));

--- Table questionnaires
--- une question d'un questionnaire d'une évaluation avec les propositions ainsi que la bonne réponse à fournir

CREATE TABLE IF NOT EXISTS questionnaires (idEvaluation INTEGER, idQuestion INTEGER, idProposition INTEGER, correct BOOLEAN NOT NULL CHECK (correct IN (0, 1)), FOREIGN KEY (idEvaluation) REFERENCES evaluations(id) ON DELETE CASCADE, FOREIGN KEY (idProposition) REFERENCES propositions(id));

--- Table resultats
--- les résultats d'un participant à une évaluation

CREATE TABLE IF NOT EXISTS resultats (id INTEGER PRIMARY KEY AUTOINCREMENT, horodatage DATETIME NOT NULL, idEvaluation INTEGER, idParticipant INTEGER, idPupitre INTEGER, score INTEGER, FOREIGN KEY (idEvaluation) REFERENCES evaluations(id) ON DELETE CASCADE, FOREIGN KEY (idParticipant) REFERENCES participants(id) ON DELETE CASCADE);
