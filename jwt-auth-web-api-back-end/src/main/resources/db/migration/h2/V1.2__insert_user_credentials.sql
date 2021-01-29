/*  --------------------------------------
	-- DDL : Data Manipulation Language
	-- DATABASE : H2 
	-- SCHEMA OR CATALOG :  
	-------------------------------------
*/

/* Inserion dans la table T_USERS */
INSERT INTO T_USERS (ID, ACCOUNT_EXPIRED, ACCOUNT_LOCKED, CREATED_TIME, CREDENTIALS_EXPIRED, EMAIL, ENABLED, USER_PASSWORD, UPDATED_TIME, USER_NAME, OPTLOCK) 
VALUES 
	(1, 'FALSE', 'FALSE', '2020-12-23 10:11:28', 'FALSE', 'admin.test@live.fr', 'TRUE', '$2a$12$3yOzX6o60.NQfdcGJAnamu0y2tNip6EzNxOvtM10ivD8cmwutrwzq', NULL, 'admin', 0), -- mot de passe non chiffré: admin_19511982#
	(2, 'FALSE', 'FALSE', '2020-12-23 10:11:28', 'FALSE', 'client.test@live.fr', 'TRUE', '$2a$12$uCa/OmS7PrNIgsWTe461yuVLYIjvuf8RWjl78.qJrcVxff5bJwsh6', NULL, 'client', 0), -- mot de passe non chiffré: client_19511982#
	(3, 'FALSE', 'FALSE', '2020-12-23 10:11:28', 'FALSE', 'client1.test@live.fr','TRUE', '$2a$12$xrq6TOr2z7n9uiIwJY4cSu3kQAlW219lrm/Uh8EjZntZ3MzbrvufS', NULL, 'client1', 0), -- mot de passe non chiffré: client1_19511982#
	(4, 'FALSE', 'FALSE', '2020-12-23 10:11:28', 'FALSE', 'client2.test@live.fr', 'TRUE', '$2a$12$CnwL1k1xhSgACqybLN8sveVOlfKZBdA2NYWECjWDXEwZwVgP6rFdq', NULL, 'client2', 0), -- mot de passe non chiffré: client2_19511982#
	(5, 'FALSE', 'FALSE', '2020-12-23 10:11:28', 'FALSE', 'manager.test@live.fr', 'TRUE', '$2a$12$KYWMVz/dxbxeMQAaNNhhb.Xd1/okirBtEO5IlJCrt/CqnlGymHWsm', NULL, 'manager', 0), -- mot de passe non chiffré: manager_19511982#
	(6, 'TRUE', 'TRUE', '2020-12-23 10:11:29', 'TRUE', 'client3.test@live.fr', 'FALSE', '$2a$12$gOx360257du776Uy5NmzGOPqdpdRIO5BsdodQCpMpls3K4.iuE0Re', NULL, 'client3', 0), -- mot de passe non chiffré: client3_19511982#
	(7, 'TRUE', 'TRUE', '2020-12-23 10:11:29', 'TRUE', 'client4.test@live.fr', 'FALSE', '$2a$12$GlBl5S6Xho4gE4FPCC5EG.MJWdfxH8YjAFF7aR3Ob9LS6ZnjkVtJy', NULL, 'client4', 0), -- mot de passe non chiffré: client4_19511982#
	(8, 'TRUE', 'TRUE', '2020-12-23 10:11:29', 'TRUE', 'client5.test@live.fr', 'FALSE', '$2a$12$FjhZopQ/pzTkbcm3gQQqwuw3aAihSbmkiphZJ68tk4wsbal6MkwLS', NULL, 'client5', 0); -- mot de passe non chiffré: client5_19511982#

/* Insertion dans la table USER_ROLES */
INSERT INTO USER_ROLES (USER_ID, ROLES)
VALUES 
	(1, 0), -- role admin
	(1, 3), -- admin est aussi un user
	(2, 3), 
	(3, 3),
	(4, 3),
	(5, 4), -- role manager
	(5, 3), -- manager est aussi un user	
	(6, 3),
	(7, 3),
	(8, 3);