CREATE TABLE `chikaboom`.`favorite` (
                                        `idfavorite` INT NOT NULL AUTO_INCREMENT,
                                        `idfavorite_owner` INT NULL,
                                        `idfavorite_master` INT NULL,
                                        PRIMARY KEY (`idfavorite`),
                                        INDEX `idfavorite_owner_idx` (`idfavorite_owner` ASC) VISIBLE,
                                        INDEX `idfavorite_master_idx` (`idfavorite_master` ASC) VISIBLE,
                                        CONSTRAINT `idfavorite_owner`
                                            FOREIGN KEY (`idfavorite_owner`)
                                                REFERENCES `chikaboom`.`account` (`idaccount`)
                                                ON DELETE NO ACTION
                                                ON UPDATE NO ACTION,
                                        CONSTRAINT `idfavorite_master`
                                            FOREIGN KEY (`idfavorite_master`)
                                                REFERENCES `chikaboom`.`account` (`idaccount`)
                                                ON DELETE NO ACTION
                                                ON UPDATE NO ACTION);
