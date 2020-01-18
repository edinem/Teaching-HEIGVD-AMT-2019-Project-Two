-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Generation Time: Jan 04, 2020 at 10:47 AM
-- Server version: 5.7.15
-- PHP Version: 7.2.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `AMT_CALENDAR`
--

-- --------------------------------------------------------

--
-- Table structure for table `calendar`
--
CREATE DATABASE IF NOT EXISTS `AMT_CALENDAR` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;

USE AMT_CALENDAR;

CREATE TABLE `calendar` (
  `id` int(11) NOT NULL,
  `name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `calendar`
--

INSERT INTO `calendar` (`id`, `name`) VALUES
(1, 'Test'),
(2, 'test2'),
(3, 'Test'),
(4, 'test2'),
(5, 'sdrt'),
(6, 'abcde'),
(7, 'abcde'),
(8, 'abcdserftze'),
(9, 'abcdserftze'),
(10, 'abcdserfthnze'),
(11, 'abcdserlhnze'),
(12, 'abcdserlhnze'),
(13, 'testtzui'),
(14, 'testtzui'),
(15, 'test1234');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `email` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`email`) VALUES
('abc@abc.com'),
('test@test.com');

-- --------------------------------------------------------

--
-- Table structure for table `user_has_calendar`
--

CREATE TABLE `user_has_calendar` (
  `user_id` varchar(255) NOT NULL,
  `calendar_id` int(11) NOT NULL,
  `role` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_has_calendar`
--

INSERT INTO `user_has_calendar` (`user_id`, `calendar_id`, `role`) VALUES
('test@test.com', 10, 'EDITOR'),
('test@test.com', 11, 'EDITOR'),
('test@test.com', 13, 'EDITOR'),
('test@test.com', 15, 'OWNER');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `calendar`
--
ALTER TABLE `calendar`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `id` (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`email`),
  ADD KEY `email` (`email`);

--
-- Indexes for table `user_has_calendar`
--
ALTER TABLE `user_has_calendar`
  ADD PRIMARY KEY (`user_id`,`calendar_id`),
  ADD KEY `fk_user_has_calendar_calendar1_idx` (`calendar_id`),
  ADD KEY `fk_user_has_calendar_user1_idx` (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `calendar`
--
ALTER TABLE `calendar`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `user_has_calendar`
--
ALTER TABLE `user_has_calendar`
  ADD CONSTRAINT `fk_user_has_calendar_calendar1` FOREIGN KEY (`calendar_id`) REFERENCES `calendar` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `fk_user_has_calendar_user1` FOREIGN KEY (`user_id`) REFERENCES `user` (`email`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
