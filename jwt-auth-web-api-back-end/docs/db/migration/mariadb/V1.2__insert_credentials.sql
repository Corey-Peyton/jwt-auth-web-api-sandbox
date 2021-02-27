/*  --------------------------------------
	-- DML : Data Manipulation Language
	-- BASE DE DONNEES : MARIADB
	-- SCHEMA OR CATALOG :  
	-------------------------------------
*/

/* Insertion dans la table T_USERS */
INSERT INTO `T_USERS` (`ID`, `ACCOUNT_EXPIRED`, `ACCOUNT_LOCKED`, `CREATED_TIME`, `CREDENTIALS_EXPIRED`, `EMAIL`, `ENABLED`, `USER_PASSWORD`, `UPDATED_TIME`, `USER_NAME`, `OPTLOCK`) 
VALUES
	(1, b'0', b'0', '2021-01-30 07:45:05', b'0', 'admin.test@live.fr', b'1', '$2a$12$CIPU5xUUyhaipN0RL6M50OHwsT60.Pboat.mOQJOu/rKQksl5ihIe', NULL, 'admin', 0), -- mot de passe non chiffré: admin_19511982#
	(2, b'0', b'0', '2021-01-30 07:45:06', b'0', 'client.test@live.fr', b'1', '$2a$12$rx8JKgq0FrEaXkn0/nAoy.FTip9iHB47wb5swt9vfws6TuqcgPz2u', NULL, 'client', 0), -- mot de passe non chiffré: client_19511982#
	(3, b'0', b'0', '2021-01-30 07:45:06', b'0', 'client1.test@live.fr', b'1', '$2a$12$qoa6fQU9AxSYn9VNAnf72OCCmKWFRny2emdbY8F878L3lOBNiR2ja', NULL, 'client1', 0), -- mot de passe non chiffré: client1_19511982#
	(4, b'0', b'0', '2021-01-30 07:45:06', b'0', 'client2.test@live.fr', b'1', '$2a$12$5/f7q/d/K/tZtG494/DHDOMl4BBNOEKMT19xOD1gAbXikUZfOy2oa', NULL, 'client2', 0), -- mot de passe non chiffré: client2_19511982#
	(5, b'0', b'0', '2021-01-30 07:45:06', b'0', 'client3.test@live.fr', b'1', '$2a$12$Iq.lT0KuQ.wrmzSELaHmEu16PWTYfyqTEzLxC4BaU8IOZlar6EQH6', NULL, 'client3', 0), -- mot de passe non chiffré: client3_19511982#
	(6, b'0', b'0', '2021-01-30 07:45:06', b'0', 'moderateur.test@live.fr', b'1', '$2a$12$CpVH0m57F8icGafRWOL0Du2C4IZ9iZ7qn83WgU4qrNZf67YhFcmmW', NULL, 'moderateur', 0); -- mot de passe non chiffré: moderateur_19511982#

/* Insertion dans la table USER_ROLES */
INSERT INTO `USER_ROLES` (`USER_ID`, `ROLES`) 
VALUES
	(1, 1), -- role admin
	(2, 0), -- role user
	(3, 0),
	(4, 0),
	(5, 0),
	(6, 0),
	(6, 2); -- role moderateur