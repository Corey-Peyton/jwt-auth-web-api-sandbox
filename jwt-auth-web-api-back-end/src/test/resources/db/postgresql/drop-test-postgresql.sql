/*  ---------------------------------------
	-- DDL : Data Definition Language
	-- BASE DE DONNEES : POSTGRESQL 
	-- SCHEMA OR CATALOG :  
	---------------------------------------
*/

/* 	---------------------------
	-- Scripts de Suppressions 
	---------------------------
*/ 
-- Suppression des tables : T_CATEGORIES, T_CATEGORIES_T_PRODUCTS, T_PRODUCTS
DROP TABLE IF EXISTS T_CATEGORIES CASCADE; 
DROP TABLE IF EXISTS T_CATEGORIES_T_PRODUCTS CASCADE;
DROP TABLE IF EXISTS T_PRODUCTS CASCADE; 

 -- Suppression de la table T_USERS et éléments rattachés
DROP TABLE IF EXISTS T_USERS CASCADE; 
DROP TABLE IF EXISTS USER_ROLES CASCADE; 

-- Suprression de la séquence hibernate
DROP SEQUENCE IF EXISTS HIBERNATE_SEQUENCE;
