use
chikaboom;

ALTER TABLE working_days
    MODIFY COLUMN `working_day_start` TIME,
    MODIFY COLUMN `working_day_end` TIME;

ALTER TABLE working_days
    ADD COLUMN `idaccount` INT NOT NULL DEFAULT 1 AFTER `idworking_days`,
    ADD COLUMN `date` TIMESTAMP AFTER `idAccount`;

ALTER TABLE working_days
    ALTER COLUMN `working_day_start` SET DEFAULT '09:00:00',
ALTER
COLUMN `working_day_end` SET DEFAULT '18:00:00';

ALTER TABLE `chikaboom`.`working_days`
    RENAME TO `chikaboom`.`working_day`;

ALTER TABLE `chikaboom`.`working_day`
    CHANGE COLUMN `idworking_days` `idworking_day` INT NOT NULL AUTO_INCREMENT;

ALTER TABLE `chikaboom`.`account`
    CHANGE COLUMN `idworking_days` `idworking_day` INT NULL DEFAULT NULL;

ALTER TABLE `chikaboom`.`working_day`
DROP
COLUMN `working_days`;

ALTER TABLE `chikaboom`.`account`
DROP
COLUMN `idworking_day`;

ALTER TABLE `chikaboom`.`working_day`
    CHANGE COLUMN `working_day_start` `working_day_start` TIMESTAMP NULL DEFAULT '2000-01-01 09:00:00',
    CHANGE COLUMN `working_day_end` `working_day_end` TIMESTAMP NULL DEFAULT '2000-01-01 18:00:00';
