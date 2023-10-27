CREATE TABLE `chikaboom`.`comment`
(
    `idcomment`        INT NOT NULL AUTO_INCREMENT,
    `idaccount_client` INT NULL,
    `idaccount_master` INT NULL,
    `isgood`           TINYINT NULL,
    `date`             TIMESTAMP NULL,
    `text`             VARCHAR(1000) NULL,
    PRIMARY KEY (`idcomment`),
    INDEX              `idaccount_client_com_idx` (`idaccount_client` ASC) VISIBLE,
    INDEX              `idaccount_master_com_idx` (`idaccount_master` ASC) VISIBLE,
    CONSTRAINT `idaccount_client_com`
        FOREIGN KEY (`idaccount_client`)
            REFERENCES `chikaboom`.`account` (`idaccount`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `idaccount_master_com`
        FOREIGN KEY (`idaccount_master`)
            REFERENCES `chikaboom`.`account` (`idaccount`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);
