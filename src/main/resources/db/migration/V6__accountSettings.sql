CREATE TABLE `chikaboom`.`account_settings` (
                                                `idaccount_settings` INT NOT NULL AUTO_INCREMENT,
                                                `is_phone_visible` TINYINT NOT NULL DEFAULT 0,
                                                `default_working_day_start` TIME NOT NULL DEFAULT '06:00:00',
                                                `default_working_day_end` TIME NOT NULL DEFAULT '15:00:00',
                                                PRIMARY KEY (`idaccount_settings`));

ALTER TABLE `chikaboom`.`account`
    ADD COLUMN `idaccount_settings` INT NULL AFTER `iduser_details`,
ADD INDEX `idaccount_settings_idx` (`idaccount_settings` ASC) VISIBLE;
;
ALTER TABLE `chikaboom`.`account`
    ADD CONSTRAINT `idaccount_settings`
        FOREIGN KEY (`idaccount_settings`)
            REFERENCES `chikaboom`.`account_settings` (`idaccount_settings`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION;

