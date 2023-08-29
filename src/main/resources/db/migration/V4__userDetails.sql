ALTER TABLE `chikaboom`.`account`
DROP
FOREIGN KEY `idphonecode`,
DROP
FOREIGN KEY `idabout`;
ALTER TABLE `chikaboom`.`account`
DROP
COLUMN `idsocial_network`,
DROP
COLUMN `idabout`,
DROP
COLUMN `phone`,
DROP
COLUMN `idphonecode`,
DROP INDEX `idabout_idx` ,
DROP INDEX `idphonecode_idx`;
;

CREATE TABLE `chikaboom`.`user_details`
(
    `iduser_details`   INT NOT NULL AUTO_INCREMENT,
    `idphonecode`      INT NULL,
    `phone`            VARCHAR(15) NULL,
    `idsocial_network` INT NULL,
    `idabout`          INT NULL,
    `idaccount_owner`  INT NULL,
    `first_name`       VARCHAR(45) NULL,
    `last_name`        VARCHAR(45) NULL,
    PRIMARY KEY (`iduser_details`),
    INDEX              `idphonecode_idx` (`idphonecode` ASC) VISIBLE,
    CONSTRAINT `idphonecode`
        FOREIGN KEY (`idphonecode`)
            REFERENCES `chikaboom`.`phone_code` (`idphonecode`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
);

ALTER TABLE `chikaboom`.`account`
    ADD COLUMN `iduser_details` INT NULL AFTER `is_phone_visible`;
