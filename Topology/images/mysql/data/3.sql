-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: db
-- Generation Time: Jan 10, 2020 at 01:34 PM
-- Server version: 5.7.15
-- PHP Version: 7.4.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `AMT_MANAGEMENT`
--
CREATE DATABASE IF NOT EXISTS `AMT_MANAGEMENT` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `AMT_MANAGEMENT`;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `email` varchar(255) NOT NULL,
  `firstName` varchar(255) NOT NULL,
  `lastName` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `token` varchar(512) NOT NULL,
  `ttl_token` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`email`, `firstName`, `lastName`, `password`, `token`, `ttl_token`) VALUES
('daniel.oliveirapaiva@heig-vd.ch', 'Daniel', 'Oliveira Paiva', '64f1579fdcb35c1e6ee07c0381b124e570883218c758bbc7c490c42ec8475f8d', '', '0000-00-00'),
('edin.mujkanovic@heig-vd.ch', 'Edin', 'Mujkanovic', '64f1579fdcb35c1e6ee07c0381b124e570883218c758bbc7c490c42ec8475f8d', '', '0000-00-00');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`email`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
