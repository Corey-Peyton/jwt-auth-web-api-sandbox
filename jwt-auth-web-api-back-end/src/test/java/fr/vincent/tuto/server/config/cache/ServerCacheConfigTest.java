/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ServerCacheConfigTest.java
 * Date de création : 29 janv. 2021
 * Heure de création : 13:32:11
 * Package : fr.vincent.tuto.server.config.cache
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.config.cache;

import static org.assertj.core.api.Assertions.assertThat;

import javax.cache.CacheManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import fr.vincent.tuto.common.service.props.ApplicationPropsService;
import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.BackendApplicationStarter;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistanceConfig;

/**
 * Classe des tests unitares des objets de type {@link ServerCacheConfig}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-common-test.properties", "classpath:back-end-application-test.properties" })
@ContextConfiguration(name = "backendApplicationStarter", classes = { BackEndServerRootConfig.class, DatabasePropsService.class,
        PersistanceConfig.class, ServerCacheConfig.class })
@SpringBootTest(classes = BackendApplicationStarter.class)
@ActiveProfiles("test")
class ServerCacheConfigTest
{
    @Autowired
    private ApplicationPropsService propsService;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ServerCacheConfig serverCacheConfig;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.config.cache.ServerCacheConfig#jcacheConfiguration(fr.vincent.tuto.common.service.props.ApplicationPropsService)}.
     */
    @Test
    void testJcacheConfiguration()
    {
        final var jcacheConfiguration = this.serverCacheConfig.jcacheConfiguration(this.propsService);

        assertThat(jcacheConfiguration).isNotNull();
        assertThat(jcacheConfiguration.getKeyType()).isExactlyInstanceOf(Class.class);
        assertThat(jcacheConfiguration.getValueType()).isExactlyInstanceOf(Class.class);
        assertThat(jcacheConfiguration.isStoreByValue()).isFalse();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.config.cache.ServerCacheConfig#hibernatePropertiesCustomizer(javax.cache.CacheManager)}.
     */
    @Test
    void testHibernatePropertiesCustomizer()
    {
        final var hibernatePropertiesCustomizer = this.serverCacheConfig.hibernatePropertiesCustomizer(this.cacheManager);

        assertThat(hibernatePropertiesCustomizer).isNotNull();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.config.cache.ServerCacheConfig#cacheManagerCustomizer(fr.vincent.tuto.common.service.props.ApplicationPropsService)}.
     */
    @Test
    void testCacheManagerCustomizer()
    {
        final var cacheManagerCustomizer = this.serverCacheConfig.cacheManagerCustomizer(this.propsService);

        assertThat(cacheManagerCustomizer).isNotNull();
    }

}
