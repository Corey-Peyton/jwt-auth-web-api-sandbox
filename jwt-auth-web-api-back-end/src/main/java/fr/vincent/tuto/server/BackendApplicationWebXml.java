/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : BackendApplicationWebXml.java
 * Date de création : 18 janv. 2021
 * Heure de création : 08:31:21
 * Package : fr.vincent.tuto.server
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Ajout du profile web pour faciliter le déploiement et l'exécution dans un container externe.
 * 
 * @author Vincent Otchoun
 */
public class BackendApplicationWebXml extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder)
    {
        return builder.sources(BackendApplicationStarter.class);
    }

}
