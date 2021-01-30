/* 	---------------------------
	-- Scripts de Suppressions 
	---------------------------
*/ 

-- ###############
-- ### MARIADB
-- ###############

--  FLYWAY : Suppression de la table : FLYWAY_SCHEMA_HISTORY
DROP TABLE IF EXISTS FLYWAY_SCHEMA_HISTORY CASCADE; --  flyway_schema_history;

-- Suppression de la séquence et des différentes  tables 
DROP TABLE IF EXISTS HIBERNATE_SEQUENCE; 

DROP TABLE IF EXISTS T_USERS CASCADE; 
DROP TABLE IF EXISTS USER_ROLES CASCADE;

DROP TABLE IF EXISTS T_CATEGORIES CASCADE; 
DROP TABLE IF EXISTS T_CATEGORIES_T_PRODUCTS CASCADE;
DROP TABLE IF EXISTS T_PRODUCTS CASCADE; 



-- ###############
-- ### POSTGRESQL
-- ###############
--  FLYWAY : Suppression de la table : FLYWAY_SCHEMA_HISTORY
DROP TABLE IF EXISTS FLYWAY_SCHEMA_HISTORY CASCADE; --  flyway_schema_history;

-- Suppression de la séquence et des différentes  tables 
DROP SEQUENCE IF EXISTS HIBERNATE_SEQUENCE;
DROP TABLE IF EXISTS T_USERS CASCADE; 
DROP TABLE IF EXISTS USER_ROLES CASCADE;

DROP TABLE IF EXISTS T_CATEGORIES CASCADE; 
DROP TABLE IF EXISTS T_CATEGORIES_T_PRODUCTS CASCADE;
DROP TABLE IF EXISTS T_PRODUCTS CASCADE;