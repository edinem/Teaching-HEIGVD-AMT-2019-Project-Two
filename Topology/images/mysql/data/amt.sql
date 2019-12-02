-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema amt
-- -----------------------------------------------------

DROP SCHEMA IF EXISTS `AMT_CALENDAR`;


-- -----------------------------------------------------
-- Schema amt
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `AMT_CALENDAR` DEFAULT CHARACTER SET latin1 ;

USE `AMT_CALENDAR` ;

-- -----------------------------------------------------
-- Table `amt`.`calendar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AMT_CALENDAR`.`calendar` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id` (`id` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `amt`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AMT_CALENDAR`.`user` (
  `email` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`email`),
  INDEX `email` (`email` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `amt`.`user_has_calendar`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `AMT_CALENDAR`.`user_has_calendar` (
  `user_id` VARCHAR(255) NOT NULL,
  `calendar_id` INT(11) NOT NULL,
  `role` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`user_id`, `calendar_id`),
  INDEX `fk_user_has_calendar_calendar1_idx` (`calendar_id` ASC) ,
  INDEX `fk_user_has_calendar_user1_idx` (`user_id` ASC) ,
  CONSTRAINT `fk_user_has_calendar_user1`
    FOREIGN KEY (`user_id`)
    REFERENCES `AMT_CALENDAR`.`user` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_calendar_calendar1`
    FOREIGN KEY (`calendar_id`)
    REFERENCES `AMT_CALENDAR`.`calendar` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
