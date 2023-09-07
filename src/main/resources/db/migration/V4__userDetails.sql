ALTER TABLE `chikaboom`.`account`
DROP FOREIGN KEY `idphonecode`,
DROP FOREIGN KEY `idabout`;
ALTER TABLE `chikaboom`.`account`
DROP COLUMN `idsocial_network`,
DROP COLUMN `idabout`,
DROP COLUMN `phone`,
DROP COLUMN `idphonecode`,
DROP INDEX `idabout_idx` ,
DROP INDEX `idphonecode_idx`;


CREATE TABLE `chikaboom`.`user_details`
(
    `iduser_details`   INT NOT NULL AUTO_INCREMENT,
    `idphonecode`      INT NULL,
    `phone`            VARCHAR(30) NULL,
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

ALTER TABLE `chikaboom`.`appointment`
    ADD COLUMN `iduser_details_client` INT NOT NULL AFTER `idaccount_master`;

ALTER TABLE `chikaboom`.`appointment`
    ADD CONSTRAINT `iduser_details_client_1`
        FOREIGN KEY (`iduser_details_client`)
            REFERENCES `chikaboom`.`user_details` (`iduser_details`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

ALTER TABLE `chikaboom`.`appointment`
DROP
FOREIGN KEY `idaccount_client`;
ALTER TABLE `chikaboom`.`appointment`
DROP
COLUMN `idaccount_client`,
DROP INDEX `idaccount_client_idx`;

ALTER TABLE `chikaboom`.`account`
    ADD INDEX `iduser_details_idx` (`iduser_details` ASC) VISIBLE;

ALTER TABLE `chikaboom`.`account`
    ADD CONSTRAINT `iduser_details`
        FOREIGN KEY (`iduser_details`)
            REFERENCES `chikaboom`.`user_details` (`iduser_details`)
            ON DELETE CASCADE
            ON UPDATE NO ACTION;

ALTER TABLE `chikaboom`.`user_details`
    ADD UNIQUE INDEX `phone_UNIQUE` (`phone` ASC) VISIBLE;

ALTER TABLE `chikaboom`.`appointment`
DROP
FOREIGN KEY `iduser_details_client_1`;
ALTER TABLE `chikaboom`.`appointment`
    ADD CONSTRAINT `iduser_details_client_1`
        FOREIGN KEY (`iduser_details_client`)
            REFERENCES `chikaboom`.`user_details` (`iduser_details`)
            ON DELETE CASCADE;

DROP TRIGGER IF EXISTS `chikaboom`.`user_details_AFTER_DELETE`;

DELIMITER $$
USE `chikaboom`$$
CREATE
DEFINER = CURRENT_USER TRIGGER `chikaboom`.`user_details_AFTER_DELETE` AFTER DELETE
ON `user_details` FOR EACH ROW
BEGIN
DELETE
FROM about
WHERE `about`.`idabout` = `idabout`;
END$$
DELIMITER ;

ALTER TABLE `chikaboom`.`user_details`
    ADD COLUMN `displayed_phone` VARCHAR(30) NULL AFTER `phone`;

UPDATE user_details set displayed_phone = phone where 1=1;

