/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : BackendApplicationStarter.java
 * Date de création : 18 janv. 2021
 * Heure de création : 07:33:40
 * Package : fr.vincent.tuto.server
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import fr.vincent.tuto.common.config.LogStartConfig;

/**
 * Le Starter du serveur de gestion des accès aux ressources sécurisées.
 * 
 * @author Vincent Otchoun
 */
@SpringBootApplication
public class BackendApplicationStarter
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        final SpringApplication application = new SpringApplication(BackendApplicationStarter.class);
        LogStartConfig.addDefaultProfile(application);

        final Environment environment = application.run(args).getEnvironment();

        LogStartConfig.logStartUp(environment);
    }
}
