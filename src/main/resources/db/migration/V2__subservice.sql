ALTER TABLE `chikaboom`.`service`
CHANGE COLUMN `idServiceType` `idSubserviceType` VARCHAR(36) NOT NULL;

CREATE TABLE `chikaboom`.`subservicetype` (
  `idSubserviceType` VARCHAR(36) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `idServiceType` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idSubserviceType`),
  UNIQUE INDEX `idSubserviceType_UNIQUE` (`idSubserviceType` ASC) VISIBLE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;

INSERT INTO chikaboom.servicetype values (eb88bb10-90c0-44b0-82d7-3e81ea5e8cc4, 'Парикмахерские услуги');
INSERT INTO chikaboom.servicetype values (6d935e6a-79f6-4981-bd19-ef8b0579f978, 'Ногтевой сервис');
INSERT INTO chikaboom.servicetype values (9c11c7fc-7be1-463a-a14f-abe60f23c9b4, 'Ресницы');
INSERT INTO chikaboom.servicetype values (ff9fbd06-5b75-49d8-8beb-cbcbbcafd366, 'Депиляция/эпиляция');
INSERT INTO chikaboom.servicetype values (7e8f4cfc-2009-42e6-be82-5b82748f9b77, 'Визаж/брови');
INSERT INTO chikaboom.servicetype values (f3f254f7-91bb-45f2-aa5a-df15a87ce121, 'Барбершоп/усы и борода');
INSERT INTO chikaboom.servicetype values (a3f5f7c8-cf5b-4467-b4f3-1b12e4a72cbf, 'Косметология/уход за телом');
INSERT INTO chikaboom.servicetype values (16deeb1e-ba24-47a6-a6f3-1b2b9a9d59cf, 'Татуаж/тату');
INSERT INTO chikaboom.servicetype values (94082b8f-aec8-4175-8877-aeeb4eb4d392, 'Массаж');

INSERT INTO chikaboom.subservicetype values (d9c31db7-6740-4b29-936d-76011dff4d1e, 'Стрижки', eb88bb10-90c0-44b0-82d7-3e81ea5e8cc4);
INSERT INTO chikaboom.subservicetype values (482b294f-a393-4b91-b05c-d66e129a29b7, 'Окрашивание', eb88bb10-90c0-44b0-82d7-3e81ea5e8cc4);
INSERT INTO chikaboom.subservicetype values (2e288b62-9f59-45bd-a986-84e89d2cf1f0, 'Наращивание', eb88bb10-90c0-44b0-82d7-3e81ea5e8cc4);
INSERT INTO chikaboom.subservicetype values (88baf382-fa4e-4207-89e0-19881eec0b3d, 'Укладка и прически', eb88bb10-90c0-44b0-82d7-3e81ea5e8cc4);
INSERT INTO chikaboom.subservicetype values (2c017661-55bb-4244-a319-2bf807e00324, 'Завивка', eb88bb10-90c0-44b0-82d7-3e81ea5e8cc4);
INSERT INTO chikaboom.subservicetype values (ca1eedba-af32-4d20-a65f-db45608149b6, 'Выпрямление', eb88bb10-90c0-44b0-82d7-3e81ea5e8cc4);
INSERT INTO chikaboom.subservicetype values (202fb66e-a28c-4fac-8f50-582e97d3f8b1, 'Долговременная укладка', eb88bb10-90c0-44b0-82d7-3e81ea5e8cc4);
INSERT INTO chikaboom.subservicetype values (8b25e20d-d32e-4517-b40f-59c464f09cd1, 'Плетение косичек', eb88bb10-90c0-44b0-82d7-3e81ea5e8cc4);
INSERT INTO chikaboom.subservicetype values (59683d96-c7a1-43dd-953a-1b1c08ca22cc, 'Лечение', eb88bb10-90c0-44b0-82d7-3e81ea5e8cc4);
INSERT INTO chikaboom.subservicetype values (4de3f75b-c73a-4407-b6b2-d1b1ac6fa015, 'Ламинирование', eb88bb10-90c0-44b0-82d7-3e81ea5e8cc4);
INSERT INTO chikaboom.subservicetype values (dd59ef73-3682-47f2-addb-8713ebad9c42, 'Ботекс', eb88bb10-90c0-44b0-82d7-3e81ea5e8cc4);
INSERT INTO chikaboom.subservicetype values (926a0692-3c09-4dc9-830a-e4129c238ab8, 'BoostUp', eb88bb10-90c0-44b0-82d7-3e81ea5e8cc4);
INSERT INTO chikaboom.subservicetype values (0e257bc4-613a-46a7-a878-671603a938a7, 'Маникюр', 6d935e6a-79f6-4981-bd19-ef8b0579f978);
INSERT INTO chikaboom.subservicetype values (81f6286b-db90-4eb1-90fb-6be34979b38f, 'Педикюр', 6d935e6a-79f6-4981-bd19-ef8b0579f978);
INSERT INTO chikaboom.subservicetype values (6e59f444-c765-4309-97c4-2e9dea8ebfe0, 'Покрытие гель-лаком', 6d935e6a-79f6-4981-bd19-ef8b0579f978);
INSERT INTO chikaboom.subservicetype values (ee9bc24d-473f-41d3-a23f-e53d69394a7a, 'Наращивание', 6d935e6a-79f6-4981-bd19-ef8b0579f978);
INSERT INTO chikaboom.subservicetype values (ff283f73-ed71-4648-8d3e-13478506535c, 'Лечение', 6d935e6a-79f6-4981-bd19-ef8b0579f978);
INSERT INTO chikaboom.subservicetype values (f9b42328-7020-468b-875d-43e5c8289b07, 'Спа-уходы', 6d935e6a-79f6-4981-bd19-ef8b0579f978);
INSERT INTO chikaboom.subservicetype values (294ee5d7-5891-4f48-96a7-037120bb76aa, 'Наращивание', 9c11c7fc-7be1-463a-a14f-abe60f23c9b4);
INSERT INTO chikaboom.subservicetype values (735fd30e-4a6e-4fd2-ba7d-a95427189938, 'Ботекс', 9c11c7fc-7be1-463a-a14f-abe60f23c9b4);
INSERT INTO chikaboom.subservicetype values (e6ab60d2-1a39-40af-80c5-7e1cebaa4bfb, 'Ламинирование', 9c11c7fc-7be1-463a-a14f-abe60f23c9b4);
INSERT INTO chikaboom.subservicetype values (9df43697-7080-47ff-a0ca-67f22a1593e8, 'Коллагенирование', 9c11c7fc-7be1-463a-a14f-abe60f23c9b4);
INSERT INTO chikaboom.subservicetype values (aea25992-ab62-46ac-938c-f12cc23098eb, 'Окраска', 9c11c7fc-7be1-463a-a14f-abe60f23c9b4);
INSERT INTO chikaboom.subservicetype values (b6b3a0d7-590a-419a-8387-296007623c27, 'Шугаринг', ff9fbd06-5b75-49d8-8beb-cbcbbcafd366);
INSERT INTO chikaboom.subservicetype values (631d8a4c-e59c-4bca-8950-05a210a49f5c, 'Депиляция воском', ff9fbd06-5b75-49d8-8beb-cbcbbcafd366);
INSERT INTO chikaboom.subservicetype values (f0fc191b-4f9b-4c5b-926e-4c89369a569c, 'Лазерная эпиляция', ff9fbd06-5b75-49d8-8beb-cbcbbcafd366);
INSERT INTO chikaboom.subservicetype values (29cb4af6-5b5f-4cd9-b375-3ae5be9dbd92, 'Макияж', 7e8f4cfc-2009-42e6-be82-5b82748f9b77);
INSERT INTO chikaboom.subservicetype values (a30bea10-db45-40bb-8877-d56c109cfd2c, 'Архитектура бровей', 7e8f4cfc-2009-42e6-be82-5b82748f9b77);
INSERT INTO chikaboom.subservicetype values (f6d1b392-3f0b-4c7a-8a6c-306c8ce26df9, 'Окраска', 7e8f4cfc-2009-42e6-be82-5b82748f9b77);
INSERT INTO chikaboom.subservicetype values (91ae035c-b380-4055-aee0-4e5317539b56, 'Долговременная укладка', 7e8f4cfc-2009-42e6-be82-5b82748f9b77);
INSERT INTO chikaboom.subservicetype values (f17b3d76-3ef5-4323-9ac4-ce64abd4da30, 'Тридинг', 7e8f4cfc-2009-42e6-be82-5b82748f9b77);
INSERT INTO chikaboom.subservicetype values (ff28a8f6-07fb-4379-958e-c1ad586aca1c, 'Мужская стрижка', f3f254f7-91bb-45f2-aa5a-df15a87ce121);
INSERT INTO chikaboom.subservicetype values (2f973d04-0be1-49cd-8425-67f90a7ea958, 'Окрашивание', f3f254f7-91bb-45f2-aa5a-df15a87ce121);
INSERT INTO chikaboom.subservicetype values (926ba779-618e-4a51-858e-b721a8c62161, 'Оформление усов и бороды/бритье', f3f254f7-91bb-45f2-aa5a-df15a87ce121);
INSERT INTO chikaboom.subservicetype values (c6a03914-1ed7-43d0-a54d-d247560d6012, 'Тонирование бороды', f3f254f7-91bb-45f2-aa5a-df15a87ce121);
INSERT INTO chikaboom.subservicetype values (dd58e2db-e70c-4e33-9447-d59f603eb552, 'Удаление волос воском', f3f254f7-91bb-45f2-aa5a-df15a87ce121);
INSERT INTO chikaboom.subservicetype values (32246873-9e2c-41f9-b5ef-dfa5ea3046fb, 'Пилинг и обертывание', a3f5f7c8-cf5b-4467-b4f3-1b12e4a72cbf);
INSERT INTO chikaboom.subservicetype values (b73ca4c6-e4ca-406b-ab5b-1d24b25c1c16, 'Программы ухода за лицом и телом', a3f5f7c8-cf5b-4467-b4f3-1b12e4a72cbf);
INSERT INTO chikaboom.subservicetype values (6e4173f7-71eb-4af3-922b-08d1d286753c, 'Мезотерапия', a3f5f7c8-cf5b-4467-b4f3-1b12e4a72cbf);
INSERT INTO chikaboom.subservicetype values (e40355d1-1f4b-43ee-a482-b306510be947, 'Аппаратные процедуры', a3f5f7c8-cf5b-4467-b4f3-1b12e4a72cbf);
INSERT INTO chikaboom.subservicetype values (12ca2d51-2c05-4532-9c3e-8d3155aacf6e, 'Тейпирование тела', a3f5f7c8-cf5b-4467-b4f3-1b12e4a72cbf);
INSERT INTO chikaboom.subservicetype values (1acbd17a-a179-4729-a0f3-cccb818285c3, 'Чистка лица', a3f5f7c8-cf5b-4467-b4f3-1b12e4a72cbf);
INSERT INTO chikaboom.subservicetype values (756034a2-0f68-46e8-94e2-c4c642cd9fb5, 'Лимфатический массаж лица', a3f5f7c8-cf5b-4467-b4f3-1b12e4a72cbf);
INSERT INTO chikaboom.subservicetype values (b5915c3c-845a-4dfc-8ea6-297a7152c124, 'Удаление перманентного макияжа', 16deeb1e-ba24-47a6-a6f3-1b2b9a9d59cf);
INSERT INTO chikaboom.subservicetype values (459c3472-7cf6-4287-975f-691c90a80c0b, 'Микроблейдинг', 16deeb1e-ba24-47a6-a6f3-1b2b9a9d59cf);
INSERT INTO chikaboom.subservicetype values (4b4b00f9-67a7-4784-ba03-cc11d8613d2b, 'Волосковая техника татуажа', 16deeb1e-ba24-47a6-a6f3-1b2b9a9d59cf);
INSERT INTO chikaboom.subservicetype values (d65347c0-f860-4113-befd-78e0b4b6298c, 'Татуаж бровей хной', 16deeb1e-ba24-47a6-a6f3-1b2b9a9d59cf);
INSERT INTO chikaboom.subservicetype values (79850743-b30d-444a-b622-0387163997cd, 'Теневой татуаж', 16deeb1e-ba24-47a6-a6f3-1b2b9a9d59cf);
INSERT INTO chikaboom.subservicetype values (54e4382a-1d51-4a3d-828e-23f90cd91e3b, 'Пудровая техника', 16deeb1e-ba24-47a6-a6f3-1b2b9a9d59cf);
INSERT INTO chikaboom.subservicetype values (2848b132-92b1-4ed9-b0d2-c814d83bb72b, 'Татуировки', 16deeb1e-ba24-47a6-a6f3-1b2b9a9d59cf);
INSERT INTO chikaboom.subservicetype values (3bbe2a53-974d-493c-ad34-d1b32e53c727, 'Удаление татуировок', 16deeb1e-ba24-47a6-a6f3-1b2b9a9d59cf);
INSERT INTO chikaboom.subservicetype values (c3e3da88-340d-4be0-b23d-949721e5a570, 'Классический', 94082b8f-aec8-4175-8877-aeeb4eb4d392);
INSERT INTO chikaboom.subservicetype values (5bb20ada-f0e7-4cbd-8147-50c3ec283ef5, 'Лимфодренажный', 94082b8f-aec8-4175-8877-aeeb4eb4d392);
INSERT INTO chikaboom.subservicetype values (ec8c5b31-db42-4420-8783-f0f3aa43282f, 'Антицеллюлитный', 94082b8f-aec8-4175-8877-aeeb4eb4d392);
INSERT INTO chikaboom.subservicetype values (63b98975-12dd-4e3b-ac24-58cfdf9f24df, 'Спортивный', 94082b8f-aec8-4175-8877-aeeb4eb4d392);
INSERT INTO chikaboom.subservicetype values (a7e823fd-b000-4a17-85a1-e87bac679213, 'Оздоровительный', 94082b8f-aec8-4175-8877-aeeb4eb4d392);
INSERT INTO chikaboom.subservicetype values (ed5f099c-95d6-45ff-bc2b-fe73129fd261, 'Релаксирующий', 94082b8f-aec8-4175-8877-aeeb4eb4d392);
INSERT INTO chikaboom.subservicetype values (600a43b1-3492-424a-925e-0f577bafe9b6, 'Аппаратный (коррекция фигуры)', 94082b8f-aec8-4175-8877-aeeb4eb4d392);
