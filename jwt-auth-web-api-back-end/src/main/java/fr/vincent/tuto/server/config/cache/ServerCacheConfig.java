/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ServerCacheConfig.java
 * Date de création : 29 janv. 2021
 * Heure de création : 11:16:09
 * Package : fr.vincent.tuto.server.config.cache
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.config.cache;

import java.time.Duration;

import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.vincent.tuto.common.service.props.ApplicationPropsService;
import fr.vincent.tuto.common.service.props.ApplicationPropsService.EhcacheProps;
import fr.vincent.tuto.server.constants.ServerConstants;
import fr.vincent.tuto.server.model.po.Category;
import fr.vincent.tuto.server.model.po.User;

/**
 * Configuration pour optimiser les accès aux données avec `EhCache` dans l'API. Elle est inspirée de ce que propose
 * JHipster.
 * 
 * @author Vincent Otchoun
 */
@Configuration
@EnableCaching
public class ServerCacheConfig
{

    /**
     * Configuration en tant que bean afin qu'elle soit utilisée pour personnaliser les caches créés
     * automatiquement. Voir :
     * https://stackoverflow.com/questions/59679175/configuring-caching-for-hibernate-with-spring-boot-2-1
     * 
     * @param propsService les propriétés applicatives.
     * @return la configuration du cache.
     */
    @Bean
    public javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration(@Autowired final ApplicationPropsService propsService)
    {
        final EhcacheProps ehcacheProps = propsService.getEhcacheProps();
        return Eh107Configuration.fromEhcacheCacheConfiguration(CacheConfigurationBuilder.newCacheConfigurationBuilder(Object.class, Object.class,
        ResourcePoolsBuilder.heap(ehcacheProps.getMaxEntries()))//
        .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcacheProps.getTimeToLiveSeconds())))//
        .build());
    }

    /**
     * Customiser les propriétés Hibernate pour utilisation du gestionnaire de cache.
     * 
     * @param cacheManager le gestionnaire de cache.
     * @return les propriétés customisées.
     */
    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager)
    {
        return hibernateProperties -> hibernateProperties.put(ServerConstants.HIBERNATE_CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer(final ApplicationPropsService propsService)
    {
        return cm -> {
            // Création du cache pour optimiser les accès aux données de la table T_USERS.
            createCache(cm, ServerConstants.USERS_BY_USERNAME_CACHE, propsService);
            createCache(cm, ServerConstants.USERS_BY_EMAIL_CACHE, propsService);
            createCache(cm, User.class.getName(), propsService);
            createCache(cm, User.class.getName() + ServerConstants.POINT_ROLES, propsService);

            // Création du cache pour optimiser les accès aux données de la table T_CATEGORIES.
            createCache(cm, Category.class.getName(), propsService);
            createCache(cm, Category.class.getName() + ServerConstants.POINT_PRODUCTS, propsService);
        };
    }

    /**
     * Crééer un nouveau cache.
     * 
     * @param cm        le gestionnaire de cache.
     * @param cacheName le nom du cache dans l'application.
     */
    private void createCache(final javax.cache.CacheManager cm, final String cacheName, final ApplicationPropsService propsService)
    {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null)
        {
            cm.destroyCache(cacheName);
        }
        cm.createCache(cacheName, this.jcacheConfiguration(propsService));
    }
}
