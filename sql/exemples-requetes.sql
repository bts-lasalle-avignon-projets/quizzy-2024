--- Exemples requêtes

--- Liste des participants

SELECT * FROM participants;

SELECT COUNT(*) AS NbParticipants FROM participants;

--- Liste des evaluations

SELECT * FROM evaluations;

--- Liste des questionnaires
--- une question d'un questionnaire d'une évaluation avec les propositions ainsi que la bonne réponse à fournir

SELECT evaluations.nom, questions.libelle, questions.points, questions.temps, propositions.libelle, questionnaires.correct FROM questionnaires
INNER JOIN evaluations ON (questionnaires.idEvaluation = evaluations.id)
INNER JOIN questions ON (questionnaires.idQuestion = questions.id)
INNER JOIN propositions ON (questionnaires.idProposition = propositions.id);

--- Un questionnaire pour une évaluation
--- une question d'un questionnaire d'une évaluation avec les propositions ainsi que la bonne réponse à fournir

SELECT evaluations.nom, questions.libelle, questions.points, questions.temps, propositions.libelle, questionnaires.correct FROM questionnaires
INNER JOIN evaluations ON (questionnaires.idEvaluation = evaluations.id)
INNER JOIN questions ON (questionnaires.idQuestion = questions.id)
INNER JOIN propositions ON (questionnaires.idProposition = propositions.id)
WHERE questionnaires.idEvaluation='1';

--- Liste des reponses pour une évaluation
--- réponse d'une proposition d'un participant à une question d'un questionnaire (avec le temps mis pour répondre et s'il a bien répondu)

SELECT evaluations.nom, questions.libelle, questions.points, questions.temps, propositions.libelle, participants.nom, reponses.correct, reponses.temps FROM reponses
INNER JOIN evaluations ON (reponses.idEvaluation = evaluations.id)
INNER JOIN questions ON (reponses.idQuestion = questions.id)
INNER JOIN propositions ON (reponses.idProposition = propositions.id)
INNER JOIN participants ON (reponses.idParticipant = participants.id)
WHERE reponses.idEvaluation='2';

--- Liste des reponses pour une évaluation d'un participant
--- réponse d'une proposition d'un participant à une question d'un questionnaire (avec le temps mis pour répondre et s'il a bien répondu)

SELECT evaluations.nom, questions.libelle, questions.points, questions.temps, propositions.libelle, participants.nom, reponses.correct, reponses.temps FROM reponses
INNER JOIN evaluations ON (reponses.idEvaluation = evaluations.id)
INNER JOIN questions ON (reponses.idQuestion = questions.id)
INNER JOIN propositions ON (reponses.idProposition = propositions.id)
INNER JOIN participants ON (reponses.idParticipant = participants.id)
WHERE reponses.idEvaluation='2' AND participants.id='1';

--- Liste des resultats
--- les résultats d'un participant à une évaluation

CREATE TABLE IF NOT EXISTS resultats (id INTEGER PRIMARY KEY AUTOINCREMENT, horodatage DATETIME NOT NULL, idEvaluation INTEGER, idParticipant INTEGER, idPupitre INTEGER, score INTEGER, FOREIGN KEY (idEvaluation) REFERENCES evaluations(id) ON DELETE CASCADE, FOREIGN KEY (idParticipant) REFERENCES participants(id) ON DELETE CASCADE);

SELECT resultats.horodatage, participants.nom, resultats.score FROM resultats
INNER JOIN evaluations ON (resultats.idEvaluation = evaluations.id)
INNER JOIN participants ON (resultats.idParticipant = participants.id);

--- Mettre à jour un résultat

UPDATE resultats SET horodatage=DATETIME('now'), score=0 WHERE id='1';
