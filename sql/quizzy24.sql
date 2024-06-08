-- phpMyAdmin SQL Dump
-- version 4.9.5deb2
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost:3306
-- Généré le : ven. 07 juin 2024 à 11:11
-- Version du serveur :  8.0.36-0ubuntu0.20.04.1
-- Version de PHP : 7.4.3-4ubuntu2.22

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `quizzy24`
--

-- --------------------------------------------------------

--
-- Structure de la table `evaluations`
--

CREATE TABLE `evaluations` (
  `idEvaluation` int NOT NULL,
  `idTheme` int DEFAULT NULL,
  `horodatage` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `participants`
--

CREATE TABLE `participants` (
  `idParticipant` int NOT NULL,
  `nom` varchar(255) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `questions`
--

CREATE TABLE `questions` (
  `idQuestion` int NOT NULL,
  `idTheme` int DEFAULT NULL,
  `question` text COLLATE utf8mb4_general_ci NOT NULL,
  `proposition1` text COLLATE utf8mb4_general_ci NOT NULL,
  `proposition2` text COLLATE utf8mb4_general_ci NOT NULL,
  `proposition3` text COLLATE utf8mb4_general_ci NOT NULL,
  `proposition4` text COLLATE utf8mb4_general_ci NOT NULL,
  `anecdote` text COLLATE utf8mb4_general_ci,
  `reponse` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `quiz`
--

CREATE TABLE `quiz` (
  `idEvaluation` int NOT NULL,
  `idQuestion` int NOT NULL,
  `points` double DEFAULT '1',
  `temps` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `reponses`
--

CREATE TABLE `reponses` (
  `idEvaluation` int NOT NULL,
  `idParticipant` int NOT NULL,
  `idQuestion` int NOT NULL,
  `temps` int DEFAULT NULL,
  `correct` tinyint(1) NOT NULL
) ;

-- --------------------------------------------------------

--
-- Structure de la table `resultats`
--

CREATE TABLE `resultats` (
  `idEvaluation` int NOT NULL,
  `idParticipant` int NOT NULL,
  `score` int DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Structure de la table `themes`
--

CREATE TABLE `themes` (
  `idTheme` int NOT NULL,
  `theme` varchar(255) COLLATE utf8mb4_general_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Index pour les tables déchargées
--

--
-- Index pour la table `evaluations`
--
ALTER TABLE `evaluations`
  ADD PRIMARY KEY (`idEvaluation`),
  ADD KEY `idTheme` (`idTheme`);

--
-- Index pour la table `participants`
--
ALTER TABLE `participants`
  ADD PRIMARY KEY (`idParticipant`),
  ADD UNIQUE KEY `nom` (`nom`);

--
-- Index pour la table `questions`
--
ALTER TABLE `questions`
  ADD PRIMARY KEY (`idQuestion`),
  ADD KEY `idTheme` (`idTheme`);

--
-- Index pour la table `quiz`
--
ALTER TABLE `quiz`
  ADD PRIMARY KEY (`idQuestion`,`idEvaluation`),
  ADD KEY `idEvaluation` (`idEvaluation`);

--
-- Index pour la table `reponses`
--
ALTER TABLE `reponses`
  ADD PRIMARY KEY (`idEvaluation`,`idParticipant`,`idQuestion`),
  ADD KEY `idQuestion` (`idQuestion`),
  ADD KEY `idParticipant` (`idParticipant`);

--
-- Index pour la table `resultats`
--
ALTER TABLE `resultats`
  ADD PRIMARY KEY (`idEvaluation`,`idParticipant`),
  ADD KEY `idParticipant` (`idParticipant`);

--
-- Index pour la table `themes`
--
ALTER TABLE `themes`
  ADD PRIMARY KEY (`idTheme`),
  ADD UNIQUE KEY `theme` (`theme`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `evaluations`
--
ALTER TABLE `evaluations`
  MODIFY `idEvaluation` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `participants`
--
ALTER TABLE `participants`
  MODIFY `idParticipant` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `questions`
--
ALTER TABLE `questions`
  MODIFY `idQuestion` int NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT pour la table `themes`
--
ALTER TABLE `themes`
  MODIFY `idTheme` int NOT NULL AUTO_INCREMENT;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `evaluations`
--
ALTER TABLE `evaluations`
  ADD CONSTRAINT `evaluations_ibfk_1` FOREIGN KEY (`idTheme`) REFERENCES `themes` (`idTheme`);

--
-- Contraintes pour la table `questions`
--
ALTER TABLE `questions`
  ADD CONSTRAINT `questions_ibfk_1` FOREIGN KEY (`idTheme`) REFERENCES `themes` (`idTheme`) ON DELETE CASCADE;

--
-- Contraintes pour la table `quiz`
--
ALTER TABLE `quiz`
  ADD CONSTRAINT `quiz_ibfk_1` FOREIGN KEY (`idEvaluation`) REFERENCES `evaluations` (`idEvaluation`);

--
-- Contraintes pour la table `reponses`
--
ALTER TABLE `reponses`
  ADD CONSTRAINT `reponses_ibfk_1` FOREIGN KEY (`idEvaluation`) REFERENCES `evaluations` (`idEvaluation`),
  ADD CONSTRAINT `reponses_ibfk_2` FOREIGN KEY (`idQuestion`) REFERENCES `questions` (`idQuestion`),
  ADD CONSTRAINT `reponses_ibfk_3` FOREIGN KEY (`idParticipant`) REFERENCES `participants` (`idParticipant`);

--
-- Contraintes pour la table `resultats`
--
ALTER TABLE `resultats`
  ADD CONSTRAINT `resultats_ibfk_1` FOREIGN KEY (`idEvaluation`) REFERENCES `evaluations` (`idEvaluation`),
  ADD CONSTRAINT `resultats_ibfk_2` FOREIGN KEY (`idParticipant`) REFERENCES `participants` (`idParticipant`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
