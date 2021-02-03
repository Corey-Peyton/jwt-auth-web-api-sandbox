/* 	---------------------------
	-- Scripts de Suppressions 
	---------------------------
*/ 

-- ###############
-- ### MARIADB
-- ###############
--  FLYWAY : Suppression de la table : FLYWAY_SCHEMA_HISTORY
DROP TABLE IF EXISTS FLYWAY_SCHEMA_HISTORY CASCADE; --  flyway_schema_history;

-- Suppression de la séquence hibernate
DROP TABLE IF EXISTS HIBERNATE_SEQUENCE; 

-- Suppression des tables pour les Credentials : T_USERS, USER_ROLES
DROP TABLE IF EXISTS T_USERS CASCADE; 
DROP TABLE IF EXISTS USER_ROLES CASCADE;

-- Suppression des tables 
DROP TABLE IF EXISTS T_CATEGORIES CASCADE; 
DROP TABLE IF EXISTS T_CATEGORIES_T_PRODUCTS CASCADE;
DROP TABLE IF EXISTS T_PRODUCTS CASCADE; 


-- ###############
-- ### POSTGRESQL
-- ###############
--  FLYWAY : Suppression de la table : FLYWAY_SCHEMA_HISTORY
DROP TABLE IF EXISTS FLYWAY_SCHEMA_HISTORY CASCADE; --  flyway_schema_history;

-- Suppression de la séquence hibernate
DROP SEQUENCE IF EXISTS HIBERNATE_SEQUENCE;

-- Suppression des tables pour les Credentials : T_USERS, USER_ROLES
DROP TABLE IF EXISTS T_USERS CASCADE; 
DROP TABLE IF EXISTS USER_ROLES CASCADE;

-- Suppression des tables 
DROP TABLE IF EXISTS T_CATEGORIES CASCADE; 
DROP TABLE IF EXISTS T_CATEGORIES_T_PRODUCTS CASCADE;
DROP TABLE IF EXISTS T_PRODUCTS CASCADE;