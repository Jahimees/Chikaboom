CREATE TABLE `chikaboom`.`chat_message`
(
    `idchat_message`      INT NOT NULL AUTO_INCREMENT,
    `idaccount_sender`    INT NULL,
    `idaccount_recipient` INT NULL,
    `idmessage_status`    INT NULL,
    `message`             VARCHAR(1000) NULL,
    `date_time`           TIMESTAMP NULL,
    PRIMARY KEY (`idchat_message`)
);

CREATE TABLE `chikaboom`.`message_status`
(
    `idmessage_status` INT NOT NULL AUTO_INCREMENT,
    `name`             VARCHAR(45) NULL,
    PRIMARY KEY (`idmessage_status`)
);

ALTER TABLE `chikaboom`.`chat_message`
    ADD INDEX `idaccount_sender_idx` (`idaccount_sender` ASC) VISIBLE,
ADD INDEX `idaccount_recipient_idx` (`idaccount_recipient` ASC) VISIBLE,
ADD INDEX `idmessage_status_idx` (`idmessage_status` ASC) VISIBLE;
;
ALTER TABLE `chikaboom`.`chat_message`
    ADD CONSTRAINT `idaccount_sender`
        FOREIGN KEY (`idaccount_sender`)
            REFERENCES `chikaboom`.`account` (`idaccount`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
ADD CONSTRAINT `idaccount_recipient`
  FOREIGN KEY (`idaccount_recipient`)
  REFERENCES `chikaboom`.`account` (`idaccount`)
  ON
DELETE
NO ACTION
  ON
UPDATE NO ACTION,
    ADD CONSTRAINT `idmessage_status`
    FOREIGN KEY (`idmessage_status`)
    REFERENCES `chikaboom`.`message_status` (`idmessage_status`)
ON
DELETE
NO ACTION
  ON
UPDATE NO ACTION;

INSERT INTO `chikaboom`.`message_status` (`name`)
VALUES ('not_viewed');
INSERT INTO `chikaboom`.`message_status` (`name`)
VALUES ('viewed');

