ALTER TABLE `chikaboom`.`role`
    CHANGE COLUMN `role` `name` VARCHAR(45) NOT NULL ;

UPDATE `chikaboom`.`role` SET `name` = 'ROLE_MASTER' WHERE (`idrole` = '1');
UPDATE `chikaboom`.`role` SET `name` = 'ROLE_CLIENT' WHERE (`idrole` = '2');

ALTER TABLE `chikaboom`.`account`
    CHANGE COLUMN `nickname` `nickname` VARCHAR(45) NOT NULL ,
    ADD UNIQUE INDEX `nickname_UNIQUE` (`nickname` ASC) VISIBLE;

CREATE TABLE `chikaboom`.`account_roles` (
                                            `idaccount_roles` INT NOT NULL AUTO_INCREMENT,
                                            `roles_idrole` INT NOT NULL,
                                            `account_idaccount` INT NOT NULL,
                                            PRIMARY KEY (`idaccount_role`),
                                            INDEX `idrole_idx` (`idrole` ASC) VISIBLE,
                                            INDEX `idaccount_idx` (`idaccount` ASC) VISIBLE,
                                            CONSTRAINT `idrole`
                                                FOREIGN KEY (`idrole`)
                                                    REFERENCES `chikaboom`.`role` (`idrole`)
                                                    ON DELETE NO ACTION
                                                    ON UPDATE NO ACTION,
                                            CONSTRAINT `idaccount`
                                                FOREIGN KEY (`idaccount`)
                                                    REFERENCES `chikaboom`.`account` (`idaccount`)
                                                    ON DELETE NO ACTION
                                                    ON UPDATE NO ACTION);

ALTER TABLE `chikaboom`.`account`
DROP FOREIGN KEY `fk_account_role`;
ALTER TABLE `chikaboom`.`account`
DROP COLUMN `idrole`,
DROP INDEX `fk_account_role_idx` ;
;
