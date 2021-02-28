/*  -------------------------------------
	-- DDL : Data Definition Language
	-- BASE DE DONNEES : MARIADB
	-- SCHEMA OR CATALOG : JWTAUTHWEB 
	-------------------------------------
*/

/*  ----------------------------------------
	-- Scripts de Création des Credentials
	----------------------------------------
*/

-- Création de la séquence pour incrément automatique des identifiants des tables
CREATE TABLE IF NOT EXISTS HIBERNATE_SEQUENCE (
	NEXT_VAL BIGINT(20)
) engine=MyISAM;

-- Initialisation de la séquence pour la table T_USERS
INSERT INTO HIBERNATE_SEQUENCE VALUES (1); 

-- Création de la table T_USERS
CREATE TABLE IF NOT EXISTS T_USERS (
	ID BIGINT(20) NOT NULL,
	ACCOUNT_EXPIRED BIT(1) NOT NULL,
	ACCOUNT_LOCKED BIT(1) NOT NULL,
	CREATED_TIME DATETIME,
	CREDENTIALS_EXPIRED BIT(1) NOT NULL,
	EMAIL VARCHAR(254) NOT NULL,
	ENABLED BIT(1) NOT NULL,
	USER_PASSWORD VARCHAR(60) NOT NULL,
	UPDATED_TIME DATETIME,
	USER_NAME VARCHAR(80) NOT NULL,
	OPTLOCK INT(11) NOT NULL DEFAULT '0',
	PRIMARY KEY (ID)
)engine=MyISAM;

-- Création de la table USERS_ROLES
CREATE TABLE IF NOT EXISTS USER_ROLES (
	USER_ID BIGINT(20) NOT NULL,
	ROLES INT(11)
)engine=MyISAM;

-- Contrainte unicité de l'adresse électronique
ALTER TABLE T_USERS 
	ADD CONSTRAINT UK_kbdgs6v1gu1pcoq5u9ohje6ep UNIQUE (EMAIL);
	
	-- Contrainte T_USERS du login
ALTER TABLE T_USERS 
	ADD CONSTRAINT UK_srr913w7behdj71hcwtde381p UNIQUE (USER_NAME);
	
-- Contrainte de la clé étrangère dans la table USERS_ROLES
ALTER TABLE USER_ROLES 
	ADD CONSTRAINT FKs6y4k5lgw4a4ei5lj2u2ibkh5 
		FOREIGN KEY (USER_ID) 
		REFERENCES T_USERS (ID);
