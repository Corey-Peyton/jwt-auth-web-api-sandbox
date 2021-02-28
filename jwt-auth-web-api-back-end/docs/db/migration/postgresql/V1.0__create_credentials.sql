/*  ---------------------------------------
	-- DDL : Data Definition Language
	-- BASE DE DONNEES : POSTGRESQL 
	-- SCHEMA OR CATALOG :  
	---------------------------------------
*/

/*  ----------------------------------------
	-- Scripts de Création des Credentials
	----------------------------------------
*/

-- Création de la séquence pour incrément automatique des identifiants des tables
CREATE SEQUENCE IF NOT EXISTS  HIBERNATE_SEQUENCE START 1 INCREMENT 1;

-- Création de la table T_USERS
CREATE TABLE IF NOT EXISTS T_USERS (
	ID INT8 NOT NULL,
	ACCOUNT_EXPIRED BOOLEAN NOT NULL,
	ACCOUNT_LOCKED BOOLEAN NOT NULL,
	CREATED_TIME TIMESTAMP, 
	CREDENTIALS_EXPIRED BOOLEAN NOT NULL,
	EMAIL VARCHAR(254) NOT NULL,
	ENABLED BOOLEAN NOT NULL,
	USER_PASSWORD VARCHAR(60) NOT NULL,
	UPDATED_TIME TIMESTAMP,
	USER_NAME VARCHAR(80) NOT NULL,
	OPTLOCK INTEGER NOT NULL DEFAULT '0',
	PRIMARY KEY (ID)
);

-- Création de la table USERS_ROLES
CREATE TABLE IF NOT EXISTS USER_ROLES (
	USER_ID INT8 NOT NULL,
	ROLES INT4
);

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
		REFERENCES T_USERS;
