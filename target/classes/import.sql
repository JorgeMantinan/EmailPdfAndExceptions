/* Creacion de algunos usersData con sus roles */

-- ************************************************************************
-- Inserción de datos en ROLE
-- ************************************************************************
--por defecto tiene que ir con role_
INSERT INTO ROLE(NAME) VALUES ('ROLE_ADMIN'); -- ROL ID = 1
INSERT INTO ROLE(NAME) VALUES ('ROLE_USER'); -- ROL ID = 2
-- FGS 18/11/19 Introducido el rol RRHH para el crud de datos extendidos
INSERT INTO ROLE(NAME) VALUES ('ROLE_RRHH'); -- ROL ID = 3


-- Insercion en la tabla Permiso.
-- ************************************************************************
-- Inserción de datos en la tabla PERMISSION
-- ************************************************************************
INSERT INTO `PERMISSION` (`id`,`description`,`name`) VALUES (1,'Permiso básico para los usuarios, permiste ver y editar sus datos personales','ROLE_PERMISO BASICO');
INSERT INTO `PERMISSION` (`id`,`description`,`name`) VALUES (2,'Permite ver y filtrar los datos que se encuentran en la tabla de UserData','ROLE_VER USUARIOS');
INSERT INTO `PERMISSION` (`id`,`description`,`name`) VALUES (3,'Permite modificar los datos que se presentan en la tabla de UserData','ROLE_MODIFICAR USUARIOS');
INSERT INTO `PERMISSION` (`id`,`description`,`name`) VALUES (4,'Permite crear usuarios en la tabla de UseraData','ROLE_CREAR USUARIOS');
INSERT INTO `PERMISSION` (`id`,`description`,`name`) VALUES (5,'Permite eliminar los datos que se encuentran en la tabla de UserData','ROLE_ELIMINAR USUARIOS');
INSERT INTO `PERMISSION` (`id`,`description`,`name`) VALUES (6,'Permiter ver y filtrar los datos que se encuentran en la tabla de Role','ROLE_VER ROLES');
INSERT INTO `PERMISSION` (`id`,`description`,`name`) VALUES (7,'Permite modificar los datos que se presentan en la tabla de Role','ROLE_MODIFICAR ROLES');
INSERT INTO `PERMISSION` (`id`,`description`,`name`) VALUES (8,'Permite crear roles en la tabla de Role','ROLE_CREAR ROLES');
INSERT INTO `PERMISSION` (`id`,`description`,`name`) VALUES (9,'Permite eliminar los datos que se encuentran en la tabla de Role','ROLE_ELIMINAR ROLES');
INSERT INTO `PERMISSION` (`id`,`description`,`name`) VALUES (10,'Permite ver y filtrar los datos que se encuentran en la tabla de UserDataExtended','ROLE_VER DATOS EXTENDIDOS');
INSERT INTO `PERMISSION` (`id`,`description`,`name`) VALUES (11,'Permite modificar los datos que se encuentran en la tabla de UserDataExtended','ROLE_MODIFICAR DATOS EXTENDIDOS');
INSERT INTO `PERMISSION` (`id`,`description`,`name`) VALUES (12,'Permite crear datos extendidos de ususarios en la tabla UserDataExtended','ROLE_CREAR DATOS EXTENDIDOS');
INSERT INTO `PERMISSION` (`id`,`description`,`name`) VALUES (13,'Permite eliminar los datos que se encuentran en la tabal de UserDataExtended','ROLE_ELIMINAR DATOS EXTENDIDOS');
-- ***************************************************************************
Inserción de datos en PERMISSION_ROLE. Crea la relación entre roles y permisos
-- ****************************************************************************
INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (2,1);
INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (1,2);
INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (1,3);
INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (1,4);
INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (1,5);
INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (1,6);
INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (1,7);
INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (1,8);
INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (1,9);
-- FGS 18/11/19 Temporalmente le doy a ROLE_ADMIN permisos sobre CRUD de datos extendidos
-- INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (1,10);
-- INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (1,11);
-- INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (1,12);
-- INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (1,13);
-- FGS 18/11/19 Asignación de los permisos correspondientes al ROLE_RRHH
INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (3,10);
INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (3,11);
INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (3,12);
INSERT INTO `PERMISSION_ROLE` (`role_fk`,`permission_fk`) VALUES (3,13);


/* *********************** Creacion de algunos usuarios con sus roles ***********************  */
-- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> locked
-- INSERCION DEL  userData 0.  "ADMINISTRADOR".userData_ID = 1
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000000','AdminCero','Perez', 'Lopez', 'admin0@foo.com',2);
-- PWD = 666666
INSERT INTO LOGIN (DAS_ID,PASSWORD) VALUES ('A000000','$2a$10$IOInakkGq6IvIraEqLknTuWl/G/2kgZNCso6zfzxlV1WLoYdpedSO');

INSERT INTO userData_role (userData_fk, role_fk) VALUES (1,1);
INSERT INTO userData_LOGIN (userData_FK,LOGIN_FK) VALUES (1,1);


-- INSERCION DEL PRIMER userData "ADMIN".userData_ID = 2
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000001','Antonio','Perez', 'Lopez', 'usr01@foo.com',2);
-- PWD = 555888
INSERT INTO LOGIN (DAS_ID,PASSWORD) VALUES ('A000001','$2a$10$4XQMNAVJQmEmfBID0pUtQOQVWuxDtrXV7wOhi1Zv5gGh0.deccTaG');

INSERT INTO userData_role (userData_fk, role_fk) VALUES (2,1);
INSERT INTO userData_LOGIN (userData_FK,LOGIN_FK) VALUES (2,2); 

-- INSERCION DEL SEGUNDO userData "NORMAL".userData_ID = 3
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000002','Ruben','Antunez', 'Lobo', 'usr02@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
  -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000002','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (3,3);


INSERT INTO userData_role (userData_fk, role_fk) VALUES (3,3);

 
-- INSERCION DEL TERCER userData "NORMAL" CON ROL: ROLE_PRUEBA.  userData_ID = 4
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000003','Andres','Lopez', 'Vazquez', 'usr03@foo.com',2);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (4,2);-- Rol ID=2 -> USER
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
  -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000003','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (4,4);

 -- INSERCION DEL CUARTO userData "NORMAL".userData_ID = 5
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000004','Carlos','Zahera', 'Gonzalez', 'usr04@foo.com',2);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (5,2); -- Rol ID=2 -> USER
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000004','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (5,5);

  -- INSERCION DEL QUINTO userData "NORMAL".userData_ID = 6
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000005','Jose','Rubianes', 'Maki', 'usr05@foo.com',2);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (6,2); -- Rol ID=4 -> ROLE_PRUEBA_CREAR -> Cambiar este rol es solo para probar borrado.
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000005','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (6,6);

-- INSERCION DEL SEXTO userData "NORMAL".userData_ID = 7
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000006','Jose Luis','Lopez', 'Cuerda', 'usr06@foo.com',2);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (7,2); -- Rol ID=4 -> ROLE_PRUEBA_CREAR -> Cambiar este rol es solo para probar borrado.
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000006','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (7,7);
 
  -- INSERCION DEL SEPTIMO userData "NORMAL".userData_ID = 8
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000007','Felipe','Gonzalez', 'Marquez', 'usr07@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000007','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (8,8);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (8,2); -- Rol ID=2 -> ROLE_USER

-- INSERCION DEL OCTAVO userData "NORMAL".userData_ID = 9
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000008','Fernando','Gonzalez', 'Gonzo', 'usr08@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000008','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (9,9);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (9,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL NOVENO userData "NORMAL".userData_ID = 10
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000009','Ricardo','Castella', 'Perez', 'usr09@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000009','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (10,10);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (10,2); -- Rol ID=2 -> ROLE_USER
 
  -- INSERCION DEL DECIMO userData "NORMAL".userData_ID = 11
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000010','David','Broncano', 'Lopez', 'usr10@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000010','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (11,11);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (11,2); -- Rol ID=2 -> ROLE_USER

 -- INSERCION DEL UNDECIMO userData "NORMAL".userData_ID = 12
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000011','Miguel','Gila', 'Gomez', 'usr11@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000011','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (12,12);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (12,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL DUODECIMO userData "NORMAL".userData_ID = 13
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000012','Carlos','Faemino', 'Ros', 'usr12@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000012','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (13,13);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (13,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL DECIMOTERCER userData "NORMAL".userData_ID = 14
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000013','Juan','Cansado', 'Perez', 'usr13@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000013','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (14,14);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (14,2); -- Rol ID=2 -> ROLE_USER
 
  -- INSERCION DEL DECIMOCUARTO userData "NORMAL".userData_ID = 15
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000014','Juan','Gomez', 'Jurado', 'usr14@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000014','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (15,15);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (15,2); -- Rol ID=2 -> ROLE_USER
 
   -- INSERCION DEL DECIMOQUINTO userData "NORMAL".userData_ID = 16
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000015','Arturo','Gonzalez', 'Campos', 'usr15@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000015','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (16,16);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (16,2); -- Rol ID=2 -> ROLE_USER
 
   -- INSERCION DEL DECIMOSEXTO userData "NORMAL".userData_ID = 17
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000016','Rodrigo','Cortes', 'Jurado', 'usr16@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000016','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (17,17);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (17,2); -- Rol ID=2 -> ROLE_USER
 
   -- INSERCION DEL DECIMOSEPTIMO userData "NORMAL".userData_ID = 18
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000017','Arturo','Perez', 'Triste', 'usr17@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000017','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (18,18);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (18,2); -- Rol ID=2 -> ROLE_USER
 
   -- INSERCION DEL DECIMO OCTAVO userData "NORMAL".userData_ID = 19
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000018','Pablo','Martinez', 'Perez', 'usr18@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000018','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (19,19);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (19,2); -- Rol ID=2 -> ROLE_USER
 
    -- INSERCION DEL DECIMO NOVENO userData "NORMAL".userData_ID = 20
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000019','Daniel','Martinez', 'Lobo', 'usr19@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000019','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (20,20);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (20,2); -- Rol ID=2 -> ROLE_USER
 
     -- INSERCION DEL VIGESIMO userData "NORMAL".userData_ID = 21
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000020','Jorge','Mantinan', 'Montero', 'usr20@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000020','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (21,21);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (21,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL VIGESIMO PRIMER userData "NORMAL".userData_ID = 22
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000021','Daniel','Cardesin', 'Seoane', 'usr21@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000021','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (22,22);
  INSERT INTO userData_role (userData_fk, role_fk) VALUES (22,2); -- Rol ID=2 -> ROLE_USER
 
  -- INSERCION DEL VIGESIMO SEGUNDO userData "NORMAL".userData_ID = 23
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000022','Carlos','Rubal', 'Seoane', 'usr22@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000022','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (23,23);
  INSERT INTO userData_role (userData_fk, role_fk) VALUES (23,2); -- Rol ID=2 -> ROLE_USER

 -- INSERCION DEL VIGESIMO TERCER userData "NORMAL".userData_ID = 24
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000023','Fernando','Gomez', 'Souto', 'usr23@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000023','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (24,24);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (24,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL VIGESIMO CUARTO userData "NORMAL".userData_ID = 25
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000024','Daniel','Cornide', 'Seoane', 'usr24@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000024','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (25,25);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (25,2); -- Rol ID=2 -> ROLE_USER
 
  -- INSERCION DEL VIGESIMO QUINTO userData "NORMAL".userData_ID = 26
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000025','Brais','Lopez', 'Seoane', 'usr25@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000025','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (26,26);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (26,2); -- Rol ID=2 -> ROLE_USER
 
  -- INSERCION DEL VIGESIMO SEXTO userData "NORMAL".userData_ID = 27
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000026','Alonso','Contreras', 'Lopez', 'usr26@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000026','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (27,27);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (27,2); -- Rol ID=2 -> ROLE_USER
 
   -- INSERCION DEL VIGESIMO SEPTIMO userData "NORMAL".userData_ID = 28
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000027','Alonso','Contreras', 'Lopez', 'usr27@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000027','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (28,28);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (28,2); -- Rol ID=2 -> ROLE_USER
 
   -- INSERCION DEL VIGESIMO OCTAVO userData "NORMAL".userData_ID = 29
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000028','Elias','Lopez', 'Perez', 'usr28@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000028','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (29,29);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (29,2); -- Rol ID=2 -> ROLE_USER
   
   -- INSERCION DEL VIGESIMO NOVENO userData "NORMAL".userData_ID = 30
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000029','Susana','Riveira', 'Perez', 'usr29@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000029','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (30,30);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (30,2); -- Rol ID=2 -> ROLE_USER
    
   -- INSERCION DEL TRIGESIMO userData "NORMAL".userData_ID = 31
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000030','Antonia','Sanchez', 'Perez', 'usr30@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000030','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (31,31);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (31,2); -- Rol ID=2 -> ROLE_USER
   
-- INSERCION DEL TRIGESIMO PRIMER userData "NORMAL".userData_ID = 32
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000031','Luis','Cedeira', 'Gomez', 'usr31@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000031','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (32,32);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (32,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL TRIGESIMO SEGUNDO userData "NORMAL".userData_ID = 33
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000032','Antonio','Gago', 'Perez', 'usr32@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000032','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (33,33);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (33,2); -- Rol ID=2 -> ROLE_USER

  -- INSERCION DEL TRIGESIMO TERCER userData "NORMAL".userData_ID = 34
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000033','Juan Carlos','Rey', 'Boado', 'usr33@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000033','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (34,34);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (34,2); -- Rol ID=2 -> ROLE_USER

 -- INSERCION DEL TRIGESIMO CUARTO userData "NORMAL".userData_ID = 35
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000034','Sonia','Pan', 'Rubal', 'usr34@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000034','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (35,35);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (35,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL TRIGESIMO QUINTO userData "NORMAL".userData_ID = 36
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000035','Antonia','Miguez', 'Finisterre', 'usr35@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000035','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (36,36);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (36,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL TRIGESIMO SEXTO userData "NORMAL".userData_ID = 37
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000036','Luz','Pozo', 'Garza', 'usr36@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000036','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (37,37);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (37,2); -- Rol ID=2 -> ROLE_USER
  
 -- INSERCION DEL TRIGESIMO SEPTIMO userData "NORMAL".userData_ID = 38
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000037','Juan','Dobarro', 'Garcia', 'usr37@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000037','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (38,38);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (38,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL TRIGESIMO OCTAVO userData "NORMAL".userData_ID = 39
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000038','Andrea','Doria', 'Fernandez', 'usr38@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000038','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (39,39);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (39,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL TRIGESIMO NOVENO userData "NORMAL".userData_ID = 40
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000039','Martin','Gomez', 'Perez', 'usr39@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000039','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (40,40);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (40,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL CUADRAGESIMO userData "NORMAL".userData_ID = 41
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000040','Marcos','Hornos', 'Perez', 'usr40@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000040','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (41,41);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (41,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL CUADRAGESIMO PRIMER userData "NORMAL".userData_ID = 42
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000041','Santiago','Blanco', 'Antunez', 'usr41@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000041','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (42,42);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (42,2); -- Rol ID=2 -> ROLE_USER
 
-- INSERCION DEL CUADRAGESIMO SEGUNDO userData "NORMAL".userData_ID = 43
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000042','Carlos','Alsina', 'Garcia', 'usr42@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000042','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (43,43);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (43,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL CUADRAGESIMO TERCER userData "NORMAL".userData_ID = 44
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000043','Jorge','Mantinan', 'Seoane', 'usr43@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000043','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (44,44);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (44,2); -- Rol ID=2 -> ROLE_USER
 
-- INSERCION DEL CUADRAGESIMO CUARTO userData "NORMAL".userData_ID = 45
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000044','Gustavo','Souto', 'Seoane', 'usr44@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000044','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (45,45);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (45,2); -- Rol ID=2 -> ROLE_USER
 
-- INSERCION DEL CUADRAGESIMO QUINTO userData "NORMAL".userData_ID = 46
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000045','Adolfo','Deza', 'Gomez', 'usr45@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000045','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (46,46);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (46,2); -- Rol ID=2 -> ROLE_USER
 
-- INSERCION DEL CUADRAGESIMO SEXTO userData "NORMAL".userData_ID = 47
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000046','Luisa','Monelos', 'Perez', 'usr46@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000046','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (47,47);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (47,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL CUADRAGESIMO SEPTIMO userData "NORMAL".userData_ID = 48
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000047','Jose','Monelos', 'Deza', 'usr47@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000047','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (48,48);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (48,2); -- Rol ID=2 -> ROLE_USER

 -- INSERCION DEL CUADRAGESIMO OCTAVO userData "NORMAL".userData_ID = 49
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000048','Francisco','Franco', 'Rojo', 'usr48@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000048','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (49,49);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (49,2); -- Rol ID=2 -> ROLE_USER

-- INSERCION DEL CUADRAGESIMO NOVENO userData "NORMAL".userData_ID = 50
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000049','Raquel','Sanchez', 'Silva', 'usr49@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000049','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (50,50);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (50,2); -- Rol ID=2 -> ROLE_USER

 -- INSERCION DEL QUINCUAGESIMO  userData "NORMAL".userData_ID = 51
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000050','Francisco','Blanco', 'Silva', 'usr50@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000050','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (51,51);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (51,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL QUINCUAGESIMO PRIMERO userData "NORMAL".userData_ID = 52
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000051','Carlos','Besteiro', 'Rodriguez', 'usr51@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000051','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (52,52);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (52,2); -- Rol ID=2 -> ROLE_USER
 
  -- INSERCION DEL QUINCUAGESIMO SEGUNDO SEGUNDO "NORMAL".userData_ID = 53
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000052','Rodrigo','Trigo', 'Rato', 'usr52@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000052','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (53,53);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (53,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL QUINCUAGESIMO TERCERO "NORMAL".userData_ID = 54
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000053','Antonio','De la Torre', 'Bajo', 'usr53@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000053','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (54,54);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (54,2); -- Rol ID=2 -> ROLE_USER
 
 -- INSERCION DEL QUINCUAGESIMO CUARTO "NORMAL".userData_ID = 55
INSERT INTO userData (das_id,NAME, surname1, surname2, email, state) VALUES ('A000054','Miguel','Rial', 'Bajo', 'usr54@foo.com',2);
 -- suponiendo state: 1 -> pen. activation, 2 -> active y 3 -> inactive
 -- pwd 12345 cifrado
 INSERT INTO LOGIN (das_id, password) VALUES ('A000054','$2a$10$xLOGCrpSSbZMsM3DOUhAPORMMZMhnFJ.akm7cHyITMHHg1VZTFFNy');
 INSERT INTO userData_login (userData_fk, login_fk) VALUES (55,55);
 INSERT INTO userData_role (userData_fk, role_fk) VALUES (55,2); -- Rol ID=2 -> ROLE_USER
 
 
 
 
 -- Insercion de provincias
 INSERT INTO `community` VALUES (3,'Asturias, Principado de'),(6,'Cantabria'),(12,'Galicia'),(16,'País Vasco');
 
 -- Insercion de provincias 
 INSERT INTO `province` VALUES (1,'Álava',16),(15,'Coruña, A',12),(20,'Gipuzkoa',16),(27,'Lugo',12),(32,'Ourense',12),(33,'Asturias',3),(36,'Pontevedra',12),(39,'Cantabria',6),(48,'Bizkaia',16);
 
 -- Insercion en municipios
 INSERT INTO `township` VALUES (1,1,'Alegría-Dulantzi'),(1,15,'Abegondo'),(1,20,'Abaltzisketa'),(1,27,'Abadín'),(1,32,'Allariz'),(1,33,'Allande'),(1,36,'Arbo'),(1,39,'Alfoz de Lloredo'),(1,48,'Abadiño'),(2,1,'Amurrio'),(2,15,'Ames'),(2,20,'Aduna'),(2,27,'Alfoz'),(2,32,'Amoeiro'),(2,33,'Aller'),(2,36,'Barro'),(2,39,'Ampuero'),(2,48,'Abanto y Ciérvana-Abanto Zierbena'),(3,1,'Aramaio'),(3,15,'Aranga'),(3,20,'Aizarnazabal'),(3,27,'Antas de Ulla'),(3,32,'Arnoia, A'),(3,33,'Amieva'),(3,36,'Baiona'),(3,39,'Anievas'),(3,48,'Amorebieta-Etxano'),(4,1,'Artziniega'),(4,15,'Ares'),(4,20,'Albiztur'),(4,27,'Baleira'),(4,32,'Avión'),(4,33,'Avilés'),(4,36,'Bueu'),(4,39,'Arenas de Iguña'),(4,48,'Amoroto'),(5,15,'Arteixo'),(5,20,'Alegia'),(5,27,'Barreiros'),(5,32,'Baltar'),(5,33,'Belmonte de Miranda'),(5,36,'Caldas de Reis'),(5,39,'Argoños'),(5,48,'Arakaldo'),(6,1,'Armiñón'),(6,15,'Arzúa'),(6,20,'Alkiza'),(6,27,'Becerreá'),(6,32,'Bande'),(6,33,'Bimenes'),(6,36,'Cambados'),(6,39,'Arnuero'),(6,48,'Arantzazu'),(7,15,'Baña, A'),(7,20,'Altzo'),(7,27,'Begonte'),(7,32,'Baños de Molgas'),(7,33,'Boal'),(7,36,'Campo Lameiro'),(7,39,'Arredondo'),(8,1,'Arratzua-Ubarrundia'),(8,15,'Bergondo'),(8,20,'Amezketa'),(8,27,'Bóveda'),(8,32,'Barbadás'),(8,33,'Cabrales'),(8,36,'Cangas'),(8,39,'Astillero, El'),(8,48,'Artzentales'),(9,1,'Asparrena'),(9,15,'Betanzos'),(9,20,'Andoain'),(9,27,'Carballedo'),(9,32,'Barco de Valdeorras, O'),(9,33,'Cabranes'),(9,36,'Cañiza, A'),(9,39,'Bárcena de Cicero'),(9,48,'Arrankudiaga'),(10,1,'Ayala/Aiara'),(10,15,'Boimorto'),(10,20,'Anoeta'),(10,27,'Castro de Rei'),(10,32,'Beade'),(10,33,'Candamo'),(10,36,'Catoira'),(10,39,'Bárcena de Pie de Concha'),(10,48,'Arrieta'),(11,1,'Baños de Ebro/Mañueta'),(11,48,'Arrigorriaga'),(13,1,'Barrundia');
 
 
 -- Insercion de codigos postales
 INSERT INTO `postalcode` VALUES (1240,1,1),(15218,1,15),(20269,1,20),(27730,1,27),(32660,1,32),(33815,1,33),(36430,1,36),(39320,1,39),(48220,1,48),(1450,2,1),(15229,2,15),(20150,2,20),(27773,2,27),(32170,2,32),(33670,2,33),(36191,2,36),(39840,2,39),(48500,2,48),(1160,3,1),(15317,3,15),(20749,3,20),(27570,3,27),(32417,3,32),(33556,3,33),(36300,3,36),(39451,3,39),(48290,3,48),(1474,4,1),(15620,4,15),(20495,4,20),(27130,4,27),(32412,4,32),(33401,4,33),(36912,4,36),(39450,4,39),(48289,4,48),(15008,5,15),(20260,5,20),(27790,5,27),(32632,5,32),(33830,5,33),(36615,5,36),(39197,5,39),(48498,5,48),(1220,6,1),(15810,6,15),(20494,6,20),(27640,6,27),(32840,6,32),(33527,6,33),(36630,6,36),(39195,6,39),(48140,6,48),(15863,7,15),(20268,7,20),(27373,7,27),(32701,7,32),(33720,7,33),(36110,7,36),(39813,7,39),(1013,8,1),(15165,8,15),(20010,8,20),(27340,8,27),(32890,8,32),(33554,8,33),(36940,8,36),(39610,8,39),(48879,8,48),(1208,9,1),(15300,9,15),(20140,9,20),(27528,9,27),(32300,9,32),(33310,9,33),(36491,9,36),(39790,9,39),(48499,9,48),(1408,10,1),(15818,10,15),(20270,10,20),(27250,10,27),(32431,10,32),(33414,10,33),(36612,10,36),(39420,10,39),(48114,10,48),(1307,11,1),(48480,11,48),(1206,13,1);
 
 
 
 
-- ************************************************************************
-- Inserción de datos en la tabla USERDATAEXTENDED para algunos usuarios
-- ************************************************************************
-- INSERCION DEL USUARIO 1 A000000
INSERT INTO userDataExtended (user_data_id, nif, ss_number, iban_number) VALUES (1,'34898333L','396120465841','ES6900868111144269976030');

-- INSERCION DEL USUARIO 2 A000001
INSERT INTO userDataExtended (user_data_id, nie, ss_number, iban_number) VALUES (2,'Z1200053X','148026500190','ES9112511562311242556993');

-- INSERCION DEL USUARIO 3 A000002
INSERT INTO userDataExtended (user_data_id, nie, ss_number, iban_number) VALUES (3,'Y1335887S','381100922183','ES3120731038492295330172');

-- INSERCION DEL USUARIO 4 A000003
INSERT INTO userDataExtended (user_data_id,nif, ss_number, iban_number) VALUES (4,'05258855C','215451200796','ES0902374813690971043359');

-- INSERCION DEL USUARIO 5 A000004
INSERT INTO userDataExtended (user_data_id, passport, ss_number, iban_number) VALUES (5,'PUA468434','276234699204','ES3331881591336604235749');

-- INSERCION DEL USUARIO 6 A000005
INSERT INTO userDataExtended (user_data_id, nif, ss_number, iban_number) VALUES (6,'13466183M','281234567840','ES1014840896701090258816');

-- INSERCION DEL USUARIO 7 A000006
INSERT INTO userDataExtended (user_data_id, nif, ss_number, iban_number) VALUES (7,'62633281A','150112358106','ES9331304847637554728218');

-- INSERCION DEL USUARIO 8 A000007
INSERT INTO userDataExtended (user_data_id, passport, ss_number, iban_number) VALUES (8,'RCQ712130','151235813292','ES1701867176754703880007');

-- INSERCION DEL USUARIO 9 A000008
INSERT INTO userDataExtended (user_data_id, nif, ss_number, iban_number) VALUES (9,'55367041S','151491625332','ES8831440330099702615864');

-- INSERCION DEL USUARIO 10 A000009
INSERT INTO userDataExtended (user_data_id, nie, ss_number, iban_number) VALUES (10,'X6862302E','369876543238','ES4900084944565393712477');

-- INSERCION DEL USUARIO 11 A000010
INSERT INTO userDataExtended (user_data_id, nif, ss_number, iban_number) VALUES (11,'74860070S','385556667707','ES6115131692770490576410');

-- INSERCION DEL USUARIO 12 A000011
INSERT INTO userDataExtended (user_data_id, nif, ss_number, iban_number) VALUES (12,'17207691B','481234448882','ES8330070484748126677746');

-- INSERCION DEL USUARIO 13 A000012
INSERT INTO userDataExtended (user_data_id, passport, ss_number, iban_number) VALUES (13,'SUZ229135','320055667764','ES7414946288364714406332');

-- INSERCION DEL USUARIO 14 A000013
INSERT INTO userDataExtended (user_data_id, nie, ss_number, iban_number) VALUES (14,'X6462605L','270099331146','ES1530676250331366240127');

-- INSERCION DEL USUARIO 15 A000014
INSERT INTO userDataExtended (user_data_id,nie, ss_number, iban_number) VALUES (15,'Y8930186J','151500812343','ES9620720241414667845746');

