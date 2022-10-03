ALTER TABLE `chikaboom`.`account`
    ADD COLUMN `email` VARCHAR(45) NULL AFTER `nickname`,
CHANGE COLUMN `nickname` `nickname` VARCHAR(45) NULL ,
ADD UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE;
;
