/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : BackEndServerRootConfig.java
 * Date de création : 18 janv. 2021
 * Heure de création : 09:41:53
 * Package : fr.vincent.tuto.server.config
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import fr.vincent.tuto.common.config.CommonBeansConfig;
import fr.vincent.tuto.common.service.props.ApplicationPropsService;
import fr.vincent.tuto.common.service.props.DatabasePropsService;

/**
 * Configuration de base. Elle contient les beans de niveua supérieur et tout configuration requise par des fiultres.
 * 
 * @author Vincent Otchoun
 */
@Configuration
@Import(value = { JavaMailSenderImpl.class, ApplicationPropsService.class, CommonBeansConfig.class, DatabasePropsService.class })
@PropertySources(value = { @PropertySource(value = { "classpath:back-end-db.properties" }, ignoreResourceNotFound = false), @PropertySource(value = {
        "classpath:back-end-application.properties" }, ignoreResourceNotFound = false) })
@ComponentScan(basePackages = { "fr.vincent.tuto.server", "fr.vincent.tuto.common" })
@ConfigurationProperties(prefix = "vot", ignoreUnknownFields = true, ignoreInvalidFields = false)
public class BackEndServerRootConfig
{
    //
}
