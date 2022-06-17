-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema chikaboom
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema chikaboom
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `chikaboom` DEFAULT CHARACTER SET utf8 ;
USE `chikaboom` ;

-- -----------------------------------------------------
-- Table `chikaboom`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chikaboom`.`role` (
                                                  `idrole` INT NOT NULL AUTO_INCREMENT,
                                                  `role` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`idrole`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chikaboom`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chikaboom`.`account` (
                                                     `idaccount` INT NOT NULL AUTO_INCREMENT,
                                                     `phone` VARCHAR(15) NOT NULL,
    `password` VARCHAR(200) NOT NULL,
    `salt` VARCHAR(100) NOT NULL,
    `nickname` VARCHAR(45) NOT NULL,
    `registration_date` DATE NOT NULL,
    `idrole` INT NOT NULL,
    PRIMARY KEY (`idaccount`),
    INDEX `fk_account_role_idx` (`idrole` ASC) VISIBLE,
    CONSTRAINT `fk_account_role`
    FOREIGN KEY (`idrole`)
    REFERENCES `chikaboom`.`role` (`idrole`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chikaboom`.`address`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chikaboom`.`address` (
                                                     `idaddress` INT NOT NULL AUTO_INCREMENT,
                                                     `address` VARCHAR(145) NOT NULL,
    `idaccount` INT NOT NULL,
    PRIMARY KEY (`idaddress`),
    INDEX `fk_address_account1_idx` (`idaccount` ASC) VISIBLE,
    CONSTRAINT `fk_address_account1`
    FOREIGN KEY (`idaccount`)
    REFERENCES `chikaboom`.`account` (`idaccount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chikaboom`.`social_network`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chikaboom`.`social_network` (
                                                            `idsocial_network` INT NOT NULL AUTO_INCREMENT,
                                                            `link` VARCHAR(145) NOT NULL,
    `idaccount` INT NOT NULL,
    PRIMARY KEY (`idsocial_network`),
    INDEX `fk_social_network_account1_idx` (`idaccount` ASC) VISIBLE,
    CONSTRAINT `fk_social_network_account1`
    FOREIGN KEY (`idaccount`)
    REFERENCES `chikaboom`.`account` (`idaccount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chikaboom`.`about`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chikaboom`.`about` (
                                                   `idabout` INT NOT NULL AUTO_INCREMENT,
                                                   `text` TEXT NULL,
                                                   `tags` TEXT NULL,
                                                   `idaccount` INT NOT NULL,
                                                   PRIMARY KEY (`idabout`),
    INDEX `fk_about_account1_idx` (`idaccount` ASC) VISIBLE,
    CONSTRAINT `fk_about_account1`
    FOREIGN KEY (`idaccount`)
    REFERENCES `chikaboom`.`account` (`idaccount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chikaboom`.`status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chikaboom`.`status` (
                                                    `idstatus` INT NOT NULL AUTO_INCREMENT,
                                                    `status` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`idstatus`))
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `chikaboom`.`account_status`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `chikaboom`.`account_status` (
                                                            `idaccount_status` INT NOT NULL AUTO_INCREMENT,
                                                            `since_date` DATE NOT NULL,
                                                            `to_date` DATE NULL,
                                                            `idaccount` INT NOT NULL,
                                                            `idstatus` INT NOT NULL,
                                                            PRIMARY KEY (`idaccount_status`),
    INDEX `fk_account_status_account1_idx` (`idaccount` ASC) VISIBLE,
    INDEX `fk_account_status_status1_idx` (`idstatus` ASC) VISIBLE,
    CONSTRAINT `fk_account_status_account1`
    FOREIGN KEY (`idaccount`)
    REFERENCES `chikaboom`.`account` (`idaccount`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_account_status_status1`
    FOREIGN KEY (`idstatus`)
    REFERENCES `chikaboom`.`status` (`idstatus`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
