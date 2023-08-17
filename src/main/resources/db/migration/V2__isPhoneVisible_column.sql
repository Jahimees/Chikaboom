ALTER TABLE `chikaboom`.`account`
    ADD COLUMN `is_phone_visible` TINYINT NULL DEFAULT 0 AFTER `idworking_days`;
