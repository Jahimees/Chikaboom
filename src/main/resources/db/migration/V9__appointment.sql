CREATE TABLE `chikaboom`.`appointment` (
                                           `idappointment` INT NOT NULL AUTO_INCREMENT,
                                           `idaccount_master` INT NOT NULL,
                                           `idaccount_client` INT NOT NULL,
                                           `idUserService` INT NOT NULL,
                                           `appointment_date` VARCHAR(45) NOT NULL,
                                           `appointment_time` VARCHAR(45) NOT NULL,
                                           PRIMARY KEY (`idappointment`));
