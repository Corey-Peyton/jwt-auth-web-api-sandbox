/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : CategoryTypeEnum.java
 * Date de création : 26 janv. 2021
 * Heure de création : 00:27:15
 * Package : fr.vincent.tuto.server.enumeration
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.enumeration;

/**
 * Défintion des différents types de catégorie à gérer dans l'application.
 * 
 * @author Vincent Otchoun
 */
public enum CategoryTypeEnum
{
    TELEPHONIE("TELEPHONIE"), //
    TV("TV"), //
    SON("SON"), //
    INFORMATIQUE("INFORMATIQUE"), //
    PHOTO("PHOTO"), //
    JEUX_VIDEO("JEUX-VIDEO"), //
    JOUETS("JOUETS"), //
    ELCETROMENAGER("ELCETROMENAGER"), //
    MEUBLES_DECO("MEUBLES-DECO"), //
    LITERIE("LITERIE");

    private String value;

    /**
     * Constructeur
     * 
     * @param string
     */
    private CategoryTypeEnum(String pValue)
    {
        this.value = pValue;
    }

    /**
     * Retoruner la lvaleur attendue.
     * 
     * @return the value
     */
    public String getValue()
    {
        return this.value;
    }
}
