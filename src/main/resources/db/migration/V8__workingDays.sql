CREATE TABLE `chikaboom`.`working_days` (
                                            `idworking_days` INT NOT NULL AUTO_INCREMENT,
                                            `working_days` TEXT NULL,
                                            PRIMARY KEY (`idworking_days`));

ALTER TABLE `chikaboom`.`account`
    ADD COLUMN `idworking_days` INT NULL AFTER `idsocial_network`;

ALTER TABLE `chikaboom`.`working_days`
    ADD COLUMN `working_day_start` INT NULL DEFAULT 900 AFTER `working_days`,
    ADD COLUMN `working_day_end` INT NULL DEFAULT 1800 AFTER `working_day_start`;
