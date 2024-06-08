BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "themes" (
	"idTheme"	INTEGER,
	"theme"	TEXT NOT NULL UNIQUE,
	PRIMARY KEY("idTheme" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "evaluations" (
	"idEvaluation"	INTEGER,
	"idTheme"	INTEGER,
	"horodatage"	DATETIME NOT NULL,
	FOREIGN KEY("idTheme") REFERENCES "themes"("idTheme"),
	PRIMARY KEY("idEvaluation" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "participants" (
	"idParticipant"	INTEGER,
	"nom"	TEXT NOT NULL UNIQUE,
	PRIMARY KEY("idParticipant" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "questions" (
	"idQuestion"	INTEGER,
	"idTheme"	INTEGER,
	"question"	TEXT NOT NULL,
	"proposition1"	TEXT NOT NULL,
	"proposition2"	TEXT NOT NULL,
	"proposition3"	TEXT NOT NULL,
	"proposition4"	TEXT NOT NULL,
	"anecdote"	TEXT DEFAULT NULL,
	"reponse"	INTEGER,
	FOREIGN KEY("idTheme") REFERENCES "themes"("idTheme") ON DELETE CASCADE,
	PRIMARY KEY("idQuestion" AUTOINCREMENT)
);
CREATE TABLE IF NOT EXISTS "quiz" (
	"idEvaluation"	INTEGER,
	"idQuestion"	INTEGER,
	"points"	DOUBLE DEFAULT 1,
	"temps"	INTEGER,
	FOREIGN KEY("idEvaluation") REFERENCES "evaluations"("idEvaluation"),
    FOREIGN KEY("idQuestion") REFERENCES "questions"("idQuestion"),
	PRIMARY KEY("idQuestion","idEvaluation")
);
CREATE TABLE IF NOT EXISTS "reponses" (
	"idEvaluation"	INTEGER,
	"idParticipant"	INTEGER,
	"idQuestion"	INTEGER,
	"temps"	INTEGER,
	"correct"	BOOLEAN NOT NULL CHECK("correct" IN (0, 1)),
	FOREIGN KEY("idParticipant") REFERENCES "participants"("idParticipant"),
	FOREIGN KEY("idQuestion") REFERENCES "questions"("idQuestion"),
	FOREIGN KEY("idEvaluation") REFERENCES "evaluations"("idEvaluation"),
	PRIMARY KEY("idEvaluation","idParticipant","idQuestion")
);
CREATE TABLE IF NOT EXISTS "resultats" (
	"idEvaluation"	INTEGER,
	"idParticipant"	INTEGER,
	"score"	INTEGER,
	PRIMARY KEY("idEvaluation","idParticipant"),
	FOREIGN KEY("idParticipant") REFERENCES "participants"("idParticipant"),
	FOREIGN KEY("idEvaluation") REFERENCES "evaluations"("idEvaluation")
);
COMMIT;
