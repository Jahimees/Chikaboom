ALTER TABLE `chikaboom`.`account`
ADD COLUMN `salt` VARCHAR(45) NOT NULL AFTER `registrationDate`;
ALTER TABLE `chikaboom`.`account`
CHANGE COLUMN `password` `password` VARCHAR(200) NOT NULL;
