/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : PersistanceConfigTest.java
 * Date de création : 26 janv. 2021
 * Heure de création : 09:36:45
 * Package : fr.vincent.tuto.server.config.db
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.config.db;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.zaxxer.hikari.HikariDataSource;

import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.BackendApplicationStarter;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;

/**
 * Classe des tests unitaires des objets de type {@link PersistanceConfig}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-test.properties", "classpath:back-end-application-test.properties" })
@ContextConfiguration(name = "persistanceContextConfigTest", classes = { BackEndServerRootConfig.class, DatabasePropsService.class,
        PersistanceConfig.class })
@SpringBootTest(classes = BackendApplicationStarter.class)
@ActiveProfiles("test")
class PersistanceConfigTest
{

    private static final String DRIVER_CLASS_NAME = "org.h2.Driver";
    private static final String PERSIT_UNIT_NAME = "JwtRestSecureServerPUTest";

    @Autowired
    private PersistanceConfig persistanceConfig;

    @Autowired
    private DatabasePropsService databasePropsService;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        this.persistanceConfig.setDatabasePropsService(this.databasePropsService);
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.persistanceConfig = null;
        this.databasePropsService = null;
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.config.db.PersistanceConfig#dataSourceProperties()}.
     */
    @Test
    void testDataSourceProperties()
    {
        final var dataSourceProperties = this.persistanceConfig.dataSourceProperties();

        assertThat(dataSourceProperties).isNotNull();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.config.db.PersistanceConfig#dataSource()}.
     */
    @Test
    void testDataSource()
    {
        final var dataSource = this.persistanceConfig.dataSource();

        assertThat(dataSource).isNotNull();
        assertThat(dataSource.getClass()).isSameAs(HikariDataSource.class);
        assertThat(dataSource.getConnectionTimeout()).isPositive();
        assertThat(dataSource.getDriverClassName()).isEqualTo(DRIVER_CLASS_NAME);
        assertThat(dataSource.getIdleTimeout()).isPositive();
    }

    @Test
    void testDataSource_ShouldThrowException()
    {
        final var config = new PersistanceConfig();
        config.setDatabasePropsService(null);

        final var exception = assertThrows(NullPointerException.class, () -> {
            config.dataSource();
        });

        var expectedMessage = "null";
        var actualMessage = exception.getMessage();

        // System.err.println(">>>Le message d'erreur est :"+exception.getMessage());

        assertThat(actualMessage).isNotEqualTo(expectedMessage);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.config.db.PersistanceConfig#entityManagerFactory()}.
     */
    @Test
    void testEntityManagerFactory()
    {
        final var bean = this.persistanceConfig.entityManagerFactory();

        assertThat(bean).isNotNull();
        assertThat(bean.getDataSource()).isNotNull();
        assertThat(bean.getDataSource()).isExactlyInstanceOf(HikariDataSource.class);
        assertThat(bean.getJpaDialect()).isExactlyInstanceOf(HibernateJpaDialect.class);
        assertThat(bean.getJpaVendorAdapter()).isExactlyInstanceOf(HibernateJpaVendorAdapter.class);
        assertThat(bean.getPersistenceUnitName()).isEqualTo(PERSIT_UNIT_NAME);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.config.db.PersistanceConfig#sharedEntityManager()}.
     */
    @Test
    void testSharedEntityManager()
    {
        final var sharedEntity = this.persistanceConfig.sharedEntityManager();

        assertThat(sharedEntity).isNotNull();
        assertThat(sharedEntity.getEntityManagerFactory()).isNotNull();
        assertThat(sharedEntity.getPersistenceUnitName()).isNull();
        assertThat(sharedEntity.getObject()).isNotNull();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.config.db.PersistanceConfig#transactionManager()}.
     */
    @Test
    void testTransactionManager()
    {
        final var transactionManager = this.persistanceConfig.transactionManager();

        assertThat(transactionManager).isExactlyInstanceOf(JpaTransactionManager.class);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.config.db.PersistanceConfig#hibernatJpaVendorAdapter()}.
     */
    @Test
    void testHibernatJpaVendorAdapter()
    {
        final var vendorAdapter = this.persistanceConfig.hibernatJpaVendorAdapter();

        assertThat(vendorAdapter).isExactlyInstanceOf(HibernateJpaVendorAdapter.class);
        assertThat(vendorAdapter.getJpaDialect()).isNotNull();
        assertThat(vendorAdapter.getJpaPropertyMap()).isNotEmpty();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.config.db.PersistanceConfig#hibernatJpaDialect()}.
     */
    @Test
    void testHibernatJpaDialect()
    {
        final var dialect = this.persistanceConfig.hibernatJpaDialect();

        assertThat(dialect).isExactlyInstanceOf(HibernateJpaDialect.class);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.config.db.PersistanceConfig#exceptionTranslationPostProcessor()}.
     */
    @Test
    void testExceptionTranslationPostProcessor()
    {
        final var processor = this.persistanceConfig.exceptionTranslationPostProcessor();

        assertThat(processor).isExactlyInstanceOf(PersistenceExceptionTranslationPostProcessor.class);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.config.db.PersistanceConfig#sqlExceptionTranslator()}.
     */
    @Test
    void testSqlExceptionTranslator()
    {
        final var codeSQLExceptionTranslator = this.persistanceConfig.sqlExceptionTranslator();

        assertThat(codeSQLExceptionTranslator).isExactlyInstanceOf(SQLErrorCodeSQLExceptionTranslator.class);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.config.db.PersistanceConfig#getDatabasePropsService()}.
     */
    @Test
    void testGetDatabasePropsService()
    {
        assertThat(this.persistanceConfig).isNotNull();
        assertThat(this.persistanceConfig.getDatabasePropsService()).isNotNull();
    }

}
