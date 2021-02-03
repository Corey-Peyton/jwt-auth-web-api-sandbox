/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : RoleEnum.java
 * Date de création : 25 janv. 2021
 * Heure de création : 09:53:39
 * Package : fr.vincent.tuto.server.enumeration
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.enumeration;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enumeration définissant les différents rôles (Authority) dans l'application.
 * 
 * @author Vincent Otchoun
 */
public enum RoleEnum implements GrantedAuthority
{
    ROLE_USER, // Utilisateur authentifié et disposant des autorisations complètes sur les données possédées, indice=0.
    ROLE_ADMIN, // dispose des autorisations complètes sur l'ensemble des données, indice=1.
    ROLE_MODERATOR, // Gestionnaire des produits et des utilisateurs dispose d'autorisations sur les données utilisateur et des produits, indice=2.
    ROLE_ANONYMOUS // Utilisateur non authentifié, indice=3.
    ;

    @Override
    public String getAuthority()
    {
        return name();
    }
}
