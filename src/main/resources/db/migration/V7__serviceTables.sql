CREATE TABLE IF NOT EXISTS `chikaboom`.`service`
(
    `idService` INT NOT NULL AUTO_INCREMENT,
    `serviceName` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`idService`),
    UNIQUE INDEX `serviceName_UNIQUE`(`serviceName` ASC) VISIBLE);

CREATE TABLE IF NOT EXISTS `chikaboom`.`subservice`
(
    `idSubservice` INT NOT NULL AUTO_INCREMENT,
    `idService` INT NOT NULL,
    `subserviceName` VARCHAR(45) NOT NULL,
    PRIMARY KEY (`idSubservice`));

CREATE TABLE IF NOT EXISTS `chikaboom`.`user_service`
(
    `idUserService` INT NOT NULL AUTO_INCREMENT,
    `idSubservice` INT NOT NULL,
    `idAccount` INT NOT NULL,
    `userServiceName` VARCHAR(45) NOT NULL,
    `price` FLOAT NOT NULL,
    `time` VARCHAR(45) NULL,
    PRIMARY KEY (`idUserService`));

INSERT INTO service (serviceName)
values ("Ногтевой сервис"),
       ("Парикмахерские услуги"),
       ("Ресницы"),
       ("Брови"),
       ("Визаж"),
       ("Депиляция/Эпиляция"),
       ("Барбершоп"),
       ("Косметология/уход за телом"),
       ("Тату/татуаж"),
       ("Массаж");

INSERT INTO subservice (idService, subserviceName)
VALUES (1, "Маникюр"),
       (1, "Педикюр"),
       (2, "Стрижка"),
       (2, "Окрашивание"),
       (2, "Наращивание"),
       (2, "Укладка прически"),
       (3, "Наращивание ресниц"),
       (3, "Ламинирование ресниц"),
       (3, "Окраска ресниц"),
       (4, "Архитектура бровей"),
       (4, "Окраска бровей"),
       (4, "Тридинг бровей"),
       (4, "Укладка бровей"),
       (5, "Макияж"),
       (6, "Депиляция"),
       (6, "Эпиляция"),
       (7, "Мужские стрижки"),
       (7, "Коррекция и оформление бороды и усов"),
       (7, "Окрашивание"),
       (7, "Укладка"),
       (8, "Пилинг и обёртывание"),
       (8, "Программы ухода за лицом и телом"),
       (8, "Мезотерапия"),
       (8, "Аппаратные процедуры"),
       (8, "Тейпирование тела"),
       (8, "Чистка лица"),
       (8, "Лимфатическая массаж лица"),
       (9, "Тутуировки"),
       (9, "Татуаж"),
       (9, "Микроблейдинг"),
       (9, "Удаление перманентного макияжа"),
       (9, "Удаление татуировок"),
       (10, "Классический массаж"),
       (10, "Косметический массаж"),
       (10, "Гигиенический массаж"),
       (10, "Спортивный массаж"),
       (10, "Медицинский(лечебный) массаж");

ALTER TABLE `chikaboom`.`about`
DROP FOREIGN KEY `fk_about_account1`;
ALTER TABLE `chikaboom`.`about`
DROP COLUMN `idaccount`,
DROP INDEX `fk_about_account1_idx` ;

ALTER TABLE `chikaboom`.`address`
DROP FOREIGN KEY `fk_address_account1`;
ALTER TABLE `chikaboom`.`address`
DROP COLUMN `idaccount`,
DROP INDEX `fk_address_account1_idx` ;

ALTER TABLE `chikaboom`.`social_network`
DROP FOREIGN KEY `fk_social_network_account1`;
ALTER TABLE `chikaboom`.`social_network`
DROP COLUMN `idaccount`,
DROP INDEX `fk_social_network_account1_idx` ;

ALTER TABLE `chikaboom`.`account`
    ADD COLUMN `idAbout` INT NULL AFTER `idrole`,
ADD COLUMN `idAddress` INT NULL AFTER `idAbout`,
ADD COLUMN `idsocial_network` VARCHAR(45) NULL AFTER `idAddress`;

ALTER TABLE `chikaboom`.`account`
    CHANGE COLUMN `idAddress` `address` VARCHAR(45) NULL DEFAULT NULL ;

DROP TABLE `chikaboom`.`address`;

