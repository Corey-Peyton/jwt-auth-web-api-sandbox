/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : User.java
 * Date de création : 18 janv. 2021
 * Heure de création : 20:17:45
 * Package : fr.vincent.tuto.server.model.po
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.model.po;

import java.io.Serializable;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * Mapping objet des informations en base de données de la table T_USERS. Les informations de mapping sont pour la
 * plupart au sens Spring Security {@link UserDetails}. Hormis : email,createdTime, updatedTime).
 * 
 * @author Vincent Otchoun
 */
public class User implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -7689968200438820488L;

}
