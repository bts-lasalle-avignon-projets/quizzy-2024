--- LMD (langage de manipulation de données)

--- Contenu des tables (tests)

--- Table evaluateurs

INSERT INTO evaluateurs(nom) VALUES ('Thierry VAIRA');
INSERT INTO evaluateurs(nom) VALUES ('Jérôme BEAUMONT');

--- Table participants

INSERT INTO participants(nom) VALUES ('ARMANDO Célian');
INSERT INTO participants(nom) VALUES ('AVRIL Gabain');
INSERT INTO participants(nom) VALUES ('BROUSSE Antoine');
INSERT INTO participants(nom) VALUES ('GUALEZZI Mattéo');
INSERT INTO participants(nom) VALUES ('HILLION Jules');
INSERT INTO participants(nom) VALUES ('HNIZDO Thomas');
INSERT INTO participants(nom) VALUES ('LATYAOUI Othman');
INSERT INTO participants(nom) VALUES ('LERENARD Axelle');
INSERT INTO participants(nom) VALUES ('MATARISE Alexis');
INSERT INTO participants(nom) VALUES ('MDOIOUHOMA Nakib');
INSERT INTO participants(nom) VALUES ('MOUTTE Corentin');
INSERT INTO participants(nom) VALUES ('OUTFLATE Hamza');
INSERT INTO participants(nom) VALUES ('SAUGET Jean-Max');
INSERT INTO participants(nom) VALUES ('VALOBRA Enzo');
INSERT INTO participants(nom) VALUES ('VIGNAL Thomas');

--- Table niveaux (Facile, Moyen, Difficile)

INSERT INTO niveaux(difficulte) VALUES ('facile');
INSERT INTO niveaux(difficulte) VALUES ('moyen');
INSERT INTO niveaux(difficulte) VALUES ('difficile');

--- Table evaluations
--- le nom et la difficulté d'une évaluation réalisée par un évaluateur

INSERT INTO evaluations (nom, idDifficulte, idEvaluateur, horodatage) VALUES ('Les bases en C/C++', 2, 1, DATETIME('now'));
INSERT INTO evaluations (nom, idDifficulte, idEvaluateur, horodatage) VALUES ('Révisions C/C++', 1, 1, DATETIME('now'));

--- Table questions
--- le libellé d'une question

INSERT INTO questions (libelle, points, temps) VALUES ('Quel opérateur utilise-t-on pour tester l''égalité entre deux variables en C++ ?', 1, 20);
INSERT INTO questions (libelle, points, temps) VALUES ('Lequel de ces types de données permet de stocker le nombre 76.8 ?', 0.5, 20);
INSERT INTO questions (libelle, points, temps) VALUES ('Lequel de ces types de données peut stocker le nombre -1000 ?', 0.5, 20);
INSERT INTO questions (libelle, points, temps) VALUES ('Lequel de ces types de données peut stocker la chaîne de caractères "titi" ?', 1, 20);
INSERT INTO questions (libelle, points, temps) VALUES ('Combien de nombres au total peut-on coder avec un short int (16 bits) ?', 2, 20);
INSERT INTO questions (libelle, points, temps) VALUES ('Qu''affiche l''instruction suivante ?<br>i = 2; j = 3; cout << i++ << \" \" << ++j;', 5, 20);
INSERT INTO questions (libelle, points, temps) VALUES ('Qu''affiche l''instruction suivante ?<br>cout << \"j''aime le C++ \" << 30/20 << \" fois\";', 5, 20);

--- Table propositions
--- le libellé d'une proposition

INSERT INTO propositions (libelle) VALUES('<>');
INSERT INTO propositions (libelle) VALUES('!=');
INSERT INTO propositions (libelle) VALUES('==');
INSERT INTO propositions (libelle) VALUES('=');

INSERT INTO propositions (libelle) VALUES('char');
INSERT INTO propositions (libelle) VALUES('double');
INSERT INTO propositions (libelle) VALUES('long');
INSERT INTO propositions (libelle) VALUES('unsigned int');
INSERT INTO propositions (libelle) VALUES('unsigned double');
INSERT INTO propositions (libelle) VALUES('string');
INSERT INTO propositions (libelle) VALUES('int');
INSERT INTO propositions (libelle) VALUES('255');
INSERT INTO propositions (libelle) VALUES('32768');
INSERT INTO propositions (libelle) VALUES('65535');
INSERT INTO propositions (libelle) VALUES('65536');
INSERT INTO propositions (libelle) VALUES('2 3');
INSERT INTO propositions (libelle) VALUES('3 4');
INSERT INTO propositions (libelle) VALUES('2 4');
INSERT INTO propositions (libelle) VALUES('3 3');
INSERT INTO propositions (libelle) VALUES('j''aime le C++ 0 fois');
INSERT INTO propositions (libelle) VALUES('j''aime le C++ 1 fois');
INSERT INTO propositions (libelle) VALUES('j''aime le C++ 1.5 fois');
INSERT INTO propositions (libelle) VALUES('j''aime le C++ 2 fois');

--- Table questionnaires
--- une question d'un questionnaire d'une évaluation avec les propositions ainsi que la bonne réponse à fournir

INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 1, 1, 0);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 1, 2, 0);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 1, 3, 1);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 1, 4, 0);

INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 2, 5, 0);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 2, 6, 1);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 2, 7, 0);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 2, 8, 0);

INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 3, 5, 0); 
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 3, 9, 0);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 3, 7, 1);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 3, 8, 0);

INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 4, 5, 0);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 4, 6, 0);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 4, 10, 1);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 4, 11, 0);

INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 5, 12, 0);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 5, 13, 0);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 5, 14, 0);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (1, 5, 15, 1);

INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 1, 16, 0);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 1, 17, 0);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 1, 18, 1);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 1, 19, 0);

INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 2, 20, 0);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 2, 21, 1);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 2, 22, 0);
INSERT INTO questionnaires (idEvaluation, idQuestion, idProposition, correct) VALUES (2, 2, 23, 0);

--- Table reponses
--- réponse d'une proposition d'un participant à une question d'un questionnaire (avec le temps mis pour répondre et s'il a bien répondu)

INSERT INTO reponses (idEvaluation, idParticipant, idQuestion, idProposition, temps, correct) VALUES(2, 1, 4, 1, 19, 0);
INSERT INTO reponses (idEvaluation, idParticipant, idQuestion, idProposition, temps, correct) VALUES(2, 1, 2, 1, 2, 0);
INSERT INTO reponses (idEvaluation, idParticipant, idQuestion, idProposition, temps, correct) VALUES(2, 5, 4, 3, 5, 1);
INSERT INTO reponses (idEvaluation, idParticipant, idQuestion, idProposition, temps, correct) VALUES(2, 5, 2, 2, 3, 1);

--- Table resultats
--- les résultats d'un participant à une évaluation

INSERT INTO resultats (horodatage, idEvaluation, idParticipant, idPupitre, score) VALUES ('2024-04-08 08:00:00', 2, 1, 1, 0);
INSERT INTO resultats (horodatage, idEvaluation, idParticipant, idPupitre, score) VALUES ('2024-04-08 08:00:00', 2, 5, 2, 10);
