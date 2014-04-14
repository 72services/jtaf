-- phpMyAdmin SQL Dump
-- version 4.1.6
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Erstellungszeit: 14. Apr 2014 um 12:36
-- Server Version: 5.6.16
-- PHP-Version: 5.5.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Datenbank: `jtaf`
--

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `athlete`
--

CREATE TABLE IF NOT EXISTS `athlete` (
  `id` bigint(20) NOT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `series_id` bigint(20) DEFAULT NULL,
  `yearOfBirth` int(11) NOT NULL,
  `category_id` bigint(20) DEFAULT NULL,
  `club_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_l67v7hlm0w8m4i7maynveecd` (`category_id`),
  KEY `FK_4n2rh646l89g91vhffa0h77i1` (`club_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `athlete`
--

INSERT INTO `athlete` (`id`, `firstName`, `gender`, `lastName`, `series_id`, `yearOfBirth`, `category_id`, `club_id`) VALUES
(48, 'Kevin', 'm', 'Grimm', 10, 1997, 24, 28),
(49, 'Gian', 'm', 'Ballif', 10, 1998, 24, 29),
(50, 'Alexander', 'm', 'Bennet', 10, 1999, 36, 28),
(51, 'Joël', 'm', 'Grosjean', 10, 1999, 36, 29),
(52, 'Miomon', 'm', 'Schneider-Ludorff', 10, 1999, 36, 28),
(53, 'Fabian', 'm', 'Grimm', 10, 1999, 36, 28),
(54, 'Sédric', 'm', 'Scholder', 10, 2000, 36, 29),
(55, 'Luc', 'm', 'Künzler', 10, 2000, 36, 29),
(56, 'Julian', 'm', 'Christen', 10, 2001, 37, 29),
(57, 'Adirano', 'm', 'Campiotti', 10, 2001, 37, 29),
(58, 'Delano', 'm', 'Winkler', 10, 2002, 37, 29),
(59, 'Robbie', 'm', 'Gilchrist', 10, 2003, 38, 28),
(60, 'Lani', 'm', 'Scholer', 10, 2002, 37, 29),
(61, 'Miguel', 'm', 'Brander', 10, 2003, 38, 28),
(62, 'Rrezart', 'm', 'Kokolari', 10, 2003, 38, 30),
(63, 'Reto', 'm', 'Dubler', 10, 2002, 37, 28),
(64, 'Lucien', 'm', 'Büchli-Köhli', 10, 2002, 37, 29),
(65, 'Nicola', 'm', 'Künzler', 10, 2002, 37, 29),
(66, 'Valérian Luc', 'm', 'Huguelet', 10, 2003, 38, 29),
(67, 'Noah', 'm', 'Christen', 10, 2003, 38, 29),
(68, 'Téo', 'm', 'Emery', 10, 2003, 38, 30),
(69, 'Maxime', 'm', 'Sunier', 10, 2003, 38, 31),
(70, 'Eric', 'm', 'Aeberhardt', 10, 2003, 38, 29),
(71, 'Antoine', 'm', 'Sunier', 10, 2004, 38, 31),
(72, 'Julian', 'm', 'Berner', 10, 2004, 38, 28),
(73, 'Thibault', 'm', 'Biselli', 10, 2004, 38, 30),
(74, 'Félicien', 'm', 'Stauffer', 10, 2004, 38, 31),
(75, 'Jonas', 'm', 'Teutsch', 10, 2004, 38, 29),
(76, 'Jeremias', 'm', 'Teutsch', 10, 2004, 38, 29),
(77, 'Maël', 'm', 'Burkhard', 10, 2005, 39, 31),
(78, 'Mathis', 'm', 'Künzler', 10, 2005, 39, 29),
(79, 'Luc', 'm', 'Perrot', 10, 2005, 39, 29),
(80, 'Matthieu', 'm', 'Rais', 10, 2004, 38, 30),
(81, 'Kaene', 'm', 'Rollier', 10, 2005, 39, 31),
(82, 'Raïn', 'm', 'Schaffter', 10, 2006, 39, 31),
(83, 'Terry', 'm', 'Conrad', 10, 2006, 39, 31),
(84, 'Linus', 'm', 'Moser', 10, 2006, 39, 29),
(85, 'Sami', 'm', 'Simeoni', 10, 2007, 40, 31),
(86, 'Lea', 'f', 'Forti', 10, 1997, 41, 29),
(87, 'Fabienne', 'f', 'Müller', 10, 1997, 41, 29),
(88, 'Mélanie', 'f', 'Grosjean', 10, 1997, 41, 29),
(89, 'Jil', 'f', 'Rüfenacht', 10, 1998, 41, 29),
(90, 'Caroline', 'f', 'Sahli', 10, 1998, 41, 31),
(91, 'Patricia', 'f', 'Reist', 10, 1998, 41, 31),
(92, 'Laila', 'f', 'Huguet', 10, 2000, 42, 31),
(93, 'Michelle', 'f', 'Feuz', 10, 2001, 43, 28),
(94, 'Zoé', 'f', 'Gilchrist', 10, 2000, 42, 28),
(95, 'Anaelle', 'f', 'Giauque', 10, 2001, 43, 31),
(96, 'Lia', 'f', 'Rüfenacht', 10, 2001, 43, 29),
(97, 'Pauline', 'f', 'Aubry', 10, 2001, 43, 31),
(98, 'Joy', 'f', 'Ballif', 10, 2001, 43, 29),
(99, 'Loriane', 'f', 'Sunier', 10, 2001, 43, 31),
(100, 'Elia', 'f', 'Risse', 10, 2003, 44, 30),
(101, 'Muriel', 'f', 'Sprunger', 10, 2002, 43, 31),
(102, 'Julie', 'f', 'Aubry', 10, 2003, 44, 31),
(103, 'Antonia', 'f', 'Anker', 10, 2003, 44, 28),
(104, 'Elodie', 'f', 'Fröhlicher', 10, 2003, 44, 31),
(105, 'Meryl', 'f', 'Sunier', 10, 2002, 43, 31),
(106, 'Ayla', 'f', 'Winkler', 10, 2002, 43, 29),
(107, 'Nora', 'f', 'Michel', 10, 2003, 44, 29),
(108, 'Anja', 'f', 'Dubler', 10, 2005, 45, 28),
(109, 'Jamina', 'f', 'Brander', 10, 2004, 44, 28),
(110, 'Olga', 'f', 'Blanchard', 10, 2005, 45, 30),
(111, 'Léonie', 'f', 'Gfeller', 10, 2004, 44, 29),
(112, 'Zora', 'f', 'Schaffter', 10, 2004, 44, 31),
(113, 'Keira', 'f', 'Pedone', 10, 2004, 44, 30),
(114, 'Melyna', 'f', 'St-Louis', 10, 2004, 44, 30),
(115, 'Zoé', 'f', 'Zurgilgen', 10, 2005, 45, 29),
(116, 'Anouk', 'f', 'Peter', 10, 2005, 45, 29),
(117, 'Sacha', 'f', 'Girard-Pupenko', 10, 2005, 45, 30),
(118, 'Félicia', 'f', 'Conrad', 10, 2006, 45, 31),
(119, 'Manon', 'f', 'Perrinjaquet', 10, 2006, 45, 31),
(120, 'Caroline', 'f', 'Winkelmann', 10, 2006, 45, 31),
(121, 'Leïla', 'f', 'Schaffter', 10, 2006, 45, 31),
(122, 'Lina', 'f', 'Michel', 10, 2006, 45, 29),
(123, 'Muriel', 'f', 'Frésard', 10, 2008, 46, 31),
(124, 'Amélie', 'f', 'Stauffer', 10, 2008, 46, 31);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `category`
--

CREATE TABLE IF NOT EXISTS `category` (
  `id` bigint(20) NOT NULL,
  `abbreviation` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `series_id` bigint(20) DEFAULT NULL,
  `yearFrom` int(11) NOT NULL,
  `yearTo` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `category`
--

INSERT INTO `category` (`id`, `abbreviation`, `gender`, `name`, `series_id`, `yearFrom`, `yearTo`) VALUES
(24, 'A', 'm', 'Knaben', 10, 1900, 1998),
(36, 'B', 'm', 'Knaben', 10, 1999, 2000),
(37, 'C', 'm', 'Knaben', 10, 2001, 2002),
(38, 'D', 'm', 'Knaben', 10, 2003, 2004),
(39, 'E', 'm', 'Knaben', 10, 2005, 2006),
(40, 'F', 'm', 'Knaben', 10, 2007, 2099),
(41, 'G', 'f', 'Mädchen', 10, 1900, 1998),
(42, 'H', 'f', 'Mädchen', 10, 1999, 2000),
(43, 'I', 'f', 'Mädchen', 10, 2001, 2002),
(44, 'K', 'f', 'Mädchen', 10, 2003, 2004),
(45, 'L', 'f', 'Mädchen', 10, 2005, 2006),
(46, 'M', 'f', 'Mädchen', 10, 2007, 2099);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `category_event`
--

CREATE TABLE IF NOT EXISTS `category_event` (
  `Category_id` bigint(20) NOT NULL,
  `events_id` bigint(20) NOT NULL,
  `position` int(11) NOT NULL,
  PRIMARY KEY (`Category_id`,`events_id`),
  UNIQUE KEY `Category_id` (`Category_id`,`events_id`),
  KEY `FK_grfopgithc30iceeqadfeuo9q` (`events_id`),
  KEY `FK_8y01rg6kyjeave3ouubms8hiw` (`Category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `category_event`
--

INSERT INTO `category_event` (`Category_id`, `events_id`, `position`) VALUES
(24, 13, 0),
(24, 14, 3),
(24, 15, 1),
(24, 16, 2),
(36, 13, 0),
(36, 14, 3),
(36, 15, 1),
(36, 16, 2),
(37, 13, 0),
(37, 14, 3),
(37, 15, 1),
(37, 135, 2),
(38, 13, 0),
(38, 14, 3),
(38, 15, 1),
(38, 17, 2),
(39, 12, 0),
(39, 14, 3),
(39, 15, 1),
(39, 17, 2),
(40, 12, 0),
(40, 14, 3),
(40, 15, 1),
(40, 17, 2),
(41, 19, 0),
(41, 20, 3),
(41, 21, 1),
(41, 136, 2),
(42, 19, 0),
(42, 20, 3),
(42, 21, 1),
(42, 136, 2),
(43, 19, 0),
(43, 20, 3),
(43, 21, 1),
(43, 136, 2),
(44, 19, 0),
(44, 20, 3),
(44, 21, 1),
(44, 23, 2),
(45, 18, 0),
(45, 20, 3),
(45, 21, 1),
(45, 23, 2),
(46, 18, 0),
(46, 20, 3),
(46, 21, 1),
(46, 23, 2);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `club`
--

CREATE TABLE IF NOT EXISTS `club` (
  `id` bigint(20) NOT NULL,
  `abbreviation` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `space_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_2al9oeijtauda42fspy13xj9q` (`space_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `club`
--

INSERT INTO `club` (`id`, `abbreviation`, `name`, `space_id`) VALUES
(28, 'TVE', 'Erlach', 7),
(29, 'TVT', 'Twann', 7),
(30, 'TVLL', 'Le Landeron', 7),
(31, 'TVN', 'Nods', 7),
(32, 'TVLN', 'La Neuveville', 7);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `competition`
--

CREATE TABLE IF NOT EXISTS `competition` (
  `id` bigint(20) NOT NULL,
  `competitionDate` date DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `series_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `competition`
--

INSERT INTO `competition` (`id`, `competitionDate`, `name`, `series_id`) VALUES
(11, '2014-05-03', 'CIS 1 Erlach', 10);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `event`
--

CREATE TABLE IF NOT EXISTS `event` (
  `id` bigint(20) NOT NULL,
  `a` double NOT NULL,
  `b` double NOT NULL,
  `c` double NOT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `series_id` bigint(20) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `longName` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `event`
--

INSERT INTO `event` (`id`, `a`, `b`, `c`, `gender`, `name`, `series_id`, `type`, `longName`) VALUES
(12, 17.686955, 1397, 2.1, 'm', '60', 10, 'run', '60 m'),
(13, 10.54596, 1778, 2.1, 'm', '80', 10, 'run', '80 m'),
(14, 0.086375, 18833, 2.1, 'm', '600', 10, 'run_long', '600 m'),
(15, 180.85908, 190, 1, 'm', 'weit', 10, 'jump_throw', 'Weitsprung'),
(16, 82.491673, 178, 0.9, 'm', 'kugel 4', 10, 'jump_throw', 'Kugel 4 kg'),
(17, 18, 800, 0.9, 'm', 'ball', 10, 'jump_throw', 'Ball 80 g'),
(18, 19.742424, 1417, 2.1, 'f', '60', 10, 'run', '60 m'),
(19, 11.754907, 1803, 2.1, 'f', '80', 10, 'run', '80 m'),
(20, 0.089752, 19543, 2.1, 'f', '600', 10, 'run_long', '600 m'),
(21, 220.628792, 180, 1, 'f', 'weit', 10, 'jump_throw', 'Weitsprung'),
(22, 83.435373, 130, 0.9, 'f', 'kugel 4', 10, 'jump_throw', 'Kugel 4 kg'),
(23, 22, 500, 0.9, 'f', 'ball', 10, 'jump_throw', 'Ball 80 g'),
(135, 82.491673, 178, 0.9, 'm', 'kugel 3', 10, 'jump_throw', 'Kugel 3 kg'),
(136, 83.435373, 130, 0.9, 'f', 'kugel 3', 10, 'jump_throw', 'Kugel 3 kg');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `hibernate_sequence`
--

CREATE TABLE IF NOT EXISTS `hibernate_sequence` (
  `next_val` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `hibernate_sequence`
--

INSERT INTO `hibernate_sequence` (`next_val`) VALUES
(137);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `result`
--

CREATE TABLE IF NOT EXISTS `result` (
  `id` bigint(20) NOT NULL,
  `points` int(11) NOT NULL,
  `result` varchar(255) DEFAULT NULL,
  `competition_id` bigint(20) DEFAULT NULL,
  `event_id` bigint(20) DEFAULT NULL,
  `athlete_id` bigint(20) DEFAULT NULL,
  `postition` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_fgsnqjp4kjtcw7pqqprhoe0w5` (`competition_id`),
  KEY `FK_a8ybbjrmfoskysuvhucm3b6n` (`event_id`),
  KEY `FK_ieu66eh4d6imsavwjwtgnekhy` (`athlete_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `securitygroup`
--

CREATE TABLE IF NOT EXISTS `securitygroup` (
  `email` varchar(255) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `securitygroup`
--

INSERT INTO `securitygroup` (`email`, `name`) VALUES
('simon@martinelli.ch', 'user');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `securityuser`
--

CREATE TABLE IF NOT EXISTS `securityuser` (
  `email` varchar(255) NOT NULL,
  `confirmationId` varchar(255) DEFAULT NULL,
  `confirmed` tinyint(1) NOT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `secret` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `securityuser`
--

INSERT INTO `securityuser` (`email`, `confirmationId`, `confirmed`, `firstName`, `lastName`, `secret`) VALUES
('simon@martinelli.ch', NULL, 1, 'Simon', 'Martinelli', '009g5j/5XBby/F8XR9u/Wg==');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `series`
--

CREATE TABLE IF NOT EXISTS `series` (
  `id` bigint(20) NOT NULL,
  `logo` longblob,
  `name` varchar(255) DEFAULT NULL,
  `space_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_l5ok6a2py2dkxp6kwnq9411jm` (`space_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `series`
--

INSERT INTO `series` (`id`, `logo`, `name`, `space_id`) VALUES
(10, NULL, 'CIS', 7);

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `tspace`
--

CREATE TABLE IF NOT EXISTS `tspace` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `tspace`
--

INSERT INTO `tspace` (`id`, `name`) VALUES
(7, 'CIS'),
(9, 'CIS');

-- --------------------------------------------------------

--
-- Tabellenstruktur für Tabelle `userspace`
--

CREATE TABLE IF NOT EXISTS `userspace` (
  `id` bigint(20) NOT NULL,
  `role` int(11) DEFAULT NULL,
  `space_id` bigint(20) DEFAULT NULL,
  `user_email` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_500db1b1ipsocfsyn57mg44x1` (`space_id`),
  KEY `FK_dojpg9l7q2rgfaob1jj1odj14` (`user_email`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Daten für Tabelle `userspace`
--

INSERT INTO `userspace` (`id`, `role`, `space_id`, `user_email`) VALUES
(8, 0, 7, 'simon@martinelli.ch');

--
-- Constraints der exportierten Tabellen
--

--
-- Constraints der Tabelle `athlete`
--
ALTER TABLE `athlete`
  ADD CONSTRAINT `FK_4n2rh646l89g91vhffa0h77i1` FOREIGN KEY (`club_id`) REFERENCES `club` (`id`),
  ADD CONSTRAINT `FK_l67v7hlm0w8m4i7maynveecd` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`);

--
-- Constraints der Tabelle `category_event`
--
ALTER TABLE `category_event`
  ADD CONSTRAINT `FK_8y01rg6kyjeave3ouubms8hiw` FOREIGN KEY (`Category_id`) REFERENCES `category` (`id`),
  ADD CONSTRAINT `FK_grfopgithc30iceeqadfeuo9q` FOREIGN KEY (`events_id`) REFERENCES `event` (`id`);

--
-- Constraints der Tabelle `club`
--
ALTER TABLE `club`
  ADD CONSTRAINT `FK_2al9oeijtauda42fspy13xj9q` FOREIGN KEY (`space_id`) REFERENCES `tspace` (`id`);

--
-- Constraints der Tabelle `result`
--
ALTER TABLE `result`
  ADD CONSTRAINT `FK_a8ybbjrmfoskysuvhucm3b6n` FOREIGN KEY (`event_id`) REFERENCES `event` (`id`),
  ADD CONSTRAINT `FK_fgsnqjp4kjtcw7pqqprhoe0w5` FOREIGN KEY (`competition_id`) REFERENCES `competition` (`id`),
  ADD CONSTRAINT `FK_ieu66eh4d6imsavwjwtgnekhy` FOREIGN KEY (`athlete_id`) REFERENCES `athlete` (`id`);

--
-- Constraints der Tabelle `series`
--
ALTER TABLE `series`
  ADD CONSTRAINT `FK_l5ok6a2py2dkxp6kwnq9411jm` FOREIGN KEY (`space_id`) REFERENCES `tspace` (`id`);

--
-- Constraints der Tabelle `userspace`
--
ALTER TABLE `userspace`
  ADD CONSTRAINT `FK_500db1b1ipsocfsyn57mg44x1` FOREIGN KEY (`space_id`) REFERENCES `tspace` (`id`),
  ADD CONSTRAINT `FK_dojpg9l7q2rgfaob1jj1odj14` FOREIGN KEY (`user_email`) REFERENCES `securityuser` (`email`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
