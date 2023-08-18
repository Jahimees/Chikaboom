ALTER TABLE `chikaboom`.`appointment`
    ADD COLUMN `appointment_date_time` TIMESTAMP NULL AFTER `appointment_time`;
ALTER TABLE `chikaboom`.`appointment`
DROP COLUMN `appointment_time`,
DROP COLUMN `appointment_date`;
