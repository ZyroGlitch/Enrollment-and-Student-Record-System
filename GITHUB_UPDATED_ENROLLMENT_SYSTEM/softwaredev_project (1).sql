-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Sep 30, 2023 at 04:23 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `softwaredev_project`
--

-- --------------------------------------------------------

--
-- Table structure for table `deleted_records`
--

CREATE TABLE `deleted_records` (
  `studentID` varchar(6) NOT NULL,
  `password` varchar(30) DEFAULT NULL,
  `firstname` varchar(50) DEFAULT NULL,
  `lastname` varchar(50) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `age` int(10) DEFAULT NULL,
  `grade` varchar(20) DEFAULT NULL,
  `tuition` int(11) DEFAULT NULL,
  `date_enrolled` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `deleted_records`
--

INSERT INTO `deleted_records` (`studentID`, `password`, `firstname`, `lastname`, `gender`, `age`, `grade`, `tuition`, `date_enrolled`) VALUES
('427801', '123', 'Kent', 'Baldez', 'Male', 12, 'Grade 2', 12000, '2023-09-26'),
('473782', 'vince', 'Vince', 'Gratituto', 'Male', 12, 'Grade 4', 14000, '2023-09-22'),
('879959', '123', 'Wize', 'Gordon', 'Female', 12, 'Grade 5', 15000, '2023-09-26'),
('970110', 'dave123', 'Dave', 'Evasco', 'Male', 14, 'Grade 5', 15000, '2023-09-19');

-- --------------------------------------------------------

--
-- Table structure for table `student_credentials`
--

CREATE TABLE `student_credentials` (
  `id` int(11) NOT NULL,
  `Student ID` varchar(6) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL,
  `Firstname` varchar(30) DEFAULT NULL,
  `Lastname` varchar(30) DEFAULT NULL,
  `Gender` varchar(8) DEFAULT NULL,
  `Age` int(11) DEFAULT NULL,
  `Grade` varchar(20) DEFAULT NULL,
  `Tuition` int(11) DEFAULT NULL,
  `Date Enrolled` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `student_credentials`
--

INSERT INTO `student_credentials` (`id`, `Student ID`, `Password`, `Firstname`, `Lastname`, `Gender`, `Age`, `Grade`, `Tuition`, `Date Enrolled`) VALUES
(199, '597046', 'john', 'John Ford', 'Buliag', 'Male', 12, 'Grade 1', 10000, '2023-09-30'),
(200, '584820', '123', 'Cris', 'Gordon', 'Female', 14, 'Grade 2', 12000, '2023-09-30');

-- --------------------------------------------------------

--
-- Table structure for table `student_payment`
--

CREATE TABLE `student_payment` (
  `id` int(11) NOT NULL,
  `studentID` varchar(6) DEFAULT NULL,
  `totalPayment` int(11) DEFAULT NULL,
  `totalBalance` int(11) DEFAULT NULL,
  `purpose` varchar(30) DEFAULT NULL,
  `transaction_Datetime` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `student_payment`
--

INSERT INTO `student_payment` (`id`, `studentID`, `totalPayment`, `totalBalance`, `purpose`, `transaction_Datetime`) VALUES
(334, '597046', 1000, 12340, 'Downpayment', '2023-09-30 14:23:09'),
(335, '597046', 1234, 11106, 'Tuition fee', '2023-09-30 14:24:06'),
(336, '584820', 1000, 14340, 'Downpayment', '2023-09-30 14:25:30'),
(337, '584820', 1434, 12906, 'Tuition fee', '2023-09-30 14:27:23');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `deleted_records`
--
ALTER TABLE `deleted_records`
  ADD PRIMARY KEY (`studentID`);

--
-- Indexes for table `student_credentials`
--
ALTER TABLE `student_credentials`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `student_payment`
--
ALTER TABLE `student_payment`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `student_credentials`
--
ALTER TABLE `student_credentials`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=201;

--
-- AUTO_INCREMENT for table `student_payment`
--
ALTER TABLE `student_payment`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=338;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
