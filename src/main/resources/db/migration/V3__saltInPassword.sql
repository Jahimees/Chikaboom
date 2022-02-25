ALTER TABLE `chikaboom`.`account`
ADD COLUMN `salt` VARCHAR(45) NOT NULL AFTER `registrationDate`;
