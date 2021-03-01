/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : PersistenceContextConfig.java
 * Date de création : 26 janv. 2021
 * Heure de création : 09:23:15
 * Package : fr.vincent.tuto.server.config.db
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.config.db;

import java.util.Arrays;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.SharedCacheMode;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceInitializationMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.SharedEntityManagerBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.google.common.annotations.VisibleForTesting;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;

import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.common.service.props.DatabasePropsService;

/**
 * Configuration de beans de la couche abstraite d'accès aux informations en abse de données.
 * <ul>
 * <li>La source de données mutualisée HikariCP: {@link HikariDataSource}</li>
 * <li>Connexion à la source de données mutualisée HikariCP: {@link HikariPool}</li>
 * <li>Bootstrap du conteneur JPA {@link EntityManagerFactory}</li>
 * <li>Gestionnaire d'infrastructure de transaction impérative de Spring : {@link JpaTransactionManager}</li>
 * <li>Référence JPA {@link EntityManager} partagée pour {@link EntityManagerFactory}</li>
 * <li>Fournisseur de création du gestion d'entités : {@link HibernateJpaVendorAdapter}</li>
 * <li>Dialecte JPA : {@link HibernateJpaDialect}</li>
 * <li>Traduction des exceptions de persistance : {@link PersistenceExceptionTranslationPostProcessor}</li>
 * <li>Analyse des codes d'erreur spécifiques au fournisseur : {@link SQLErrorCodeSQLExceptionTranslator}</li>
 * </ul>
 * 
 * @author Vincent Otchoun
 */
@Configuration
@EnableTransactionManagement
public class PersistenceContextConfig
{
    private static final String DATASOURCE_ERR_MSG = "[dataSource] - Erreur lors de la création du bean DataSource du pool Hikari";

    @Autowired
    private DatabasePropsService databasePropsService;

    @Bean
    @Primary
    @ConfigurationProperties("vot.datasource-props")
    public DataSourceProperties dataSourceProperties()
    {
        //
        try {
            // les propréités de la datasourece à construire
            final var sourceProperties = new DataSourceProperties();

            //
            sourceProperties.setUrl(this.databasePropsService.getDataSourceProps().getJdbcUrl().trim());
            sourceProperties.setUsername(this.databasePropsService.getDataSourceProps().getUserName().trim());
            sourceProperties.setPassword(this.databasePropsService.getDataSourceProps().getPassword().trim());
            sourceProperties.setPlatform(this.databasePropsService.getDataSourceProps().getPlatform().trim());

            /*
             * XXX : Propriétés pour le chargement des données initiales au démarrage de l'application : exécuter des
             * scripts DDL et DML.
             * 1°) - Mode d'initialisation: TOUJOURS, INTÉGRÉ, JAMAIS (ALWAYS, EMBEDDED, NEVER)
             * 2°) - Chargeur de schéma (éxécution de scripts DDL)
             * 3°) - Chargeur de données (exécution de scripts DML)
             */

            // intialization-mode
            final String intialMode = this.databasePropsService.getDataSourceProps().getInitializationMode().trim();
            final String ddl = this.databasePropsService.getDataSourceProps().getInitSchema().trim();
            final String dml = this.databasePropsService.getDataSourceProps().getInitData().trim();
            final Boolean continueOnError = this.databasePropsService.getDataSourceProps().getInitContinueOnError();

            if (StringUtils.isNotEmpty(intialMode)) {
                sourceProperties.setInitializationMode(DataSourceInitializationMode.valueOf(intialMode.toUpperCase()));
            }

            if (StringUtils.isNotEmpty(ddl)) {
                sourceProperties.setSchema(Arrays.asList(ddl));
            }

            if (StringUtils.isNotEmpty(dml)) {
                sourceProperties.setData(Arrays.asList(dml));
            }

            if (null != continueOnError) {
                sourceProperties.setContinueOnError(continueOnError);
            }

            sourceProperties.afterPropertiesSet();
            return sourceProperties;
        }
        catch (Exception e) {
            throw new CustomAppException(DATASOURCE_ERR_MSG, e);
        }
    }

    /**
     * Obtenir le pool Hikari pour la source de données..
     * 
     * @return la source de données.
     */
    @Primary
    @Bean(name = "dataSource", destroyMethod = "close")
    public HikariDataSource dataSource()
    {
        final String driverClassName = this.databasePropsService.getDataSourceProps().getDriverClassName().trim();

        final HikariDataSource hikariDataSource = this.dataSourceProperties()//
        .initializeDataSourceBuilder()//
        .type(HikariDataSource.class)//
        .driverClassName(driverClassName)//
        .build();

        hikariDataSource.setJdbcUrl(this.databasePropsService.getDataSourceProps().getJdbcUrl().trim());
        hikariDataSource.setUsername(this.databasePropsService.getDataSourceProps().getUserName().trim());
        hikariDataSource.setPassword(this.databasePropsService.getDataSourceProps().getPassword().trim());

        hikariDataSource.setPoolName(this.databasePropsService.getHikariProps().getPoolName().trim());
        hikariDataSource.setMinimumIdle(this.databasePropsService.getHikariProps().getMinimumIdle());
        hikariDataSource.setMaximumPoolSize(this.databasePropsService.getHikariProps().getMaximumPoolSize());

        hikariDataSource.setIdleTimeout(this.databasePropsService.getHikariProps().getIdleTimeout());
        hikariDataSource.setMaxLifetime(this.databasePropsService.getHikariProps().getMaxLifetime());
        hikariDataSource.setConnectionTimeout(this.databasePropsService.getHikariProps().getConnectionTimeout());

        // DatasourceProperties props/value
        final String prepareStmtsCache = this.databasePropsService.getDataSourceProps().getCachePrepareStatements().trim();
        final String prepareStmtsCacheSize = this.databasePropsService.getDataSourceProps().getPrepareStatementCacheSize().trim();
        final String cacheSqlLimit = this.databasePropsService.getDataSourceProps().getPrepareStatementCacheSqlLimit().trim();
        final String useServerPreStmts = this.databasePropsService.getDataSourceProps().getUseServerPrepareStatements().trim();

        hikariDataSource.addDataSourceProperty(prepareStmtsCache, this.databasePropsService.getHikariProps().getCachePrepareStatements());
        hikariDataSource.addDataSourceProperty(prepareStmtsCacheSize, this.databasePropsService.getHikariProps().getPrepareStatementCacheSize());
        hikariDataSource.addDataSourceProperty(cacheSqlLimit, this.databasePropsService.getHikariProps().getPrepareStatementCacheSqlLimit());
        hikariDataSource.addDataSourceProperty(useServerPreStmts, this.databasePropsService.getHikariProps().getUseServerPrepareStatements());

        return hikariDataSource;
    }

    /**
     * Obtenir le gestionnaire d'entités pour l'accès aux données en base dans l'application.
     * 
     * @return le gestionnaire d'entités.
     * @throws CustomAppException exception levée lorsque survient une erreur.
     */
    @Primary
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws CustomAppException
    {
        //
        final String packageScan = this.databasePropsService.getDataSourceProps().getPackageToScan().trim();
        final String unitName = this.databasePropsService.getDataSourceProps().getPersistenceUnitName().trim();

        //
        final var emf = new LocalContainerEntityManagerFactoryBean();
        emf.setDataSource(this.dataSource());
        emf.setPersistenceUnitName(unitName);
        emf.setPackagesToScan(packageScan);
        emf.setJpaVendorAdapter(this.hibernatJpaVendorAdapter());
        emf.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        emf.setJpaDialect(this.hibernatJpaDialect());
        emf.setJpaProperties(this.additionalProperties());

        // Spécifie comment le fournisseur doit utiliser un cache de second niveau pour l'unité de persistance.
        emf.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE); // La mise en cache est activée pour toutes les entités pour lesquelles Cacheable
                                                                  // (true) est spécifié.
        
        
        // Le mode de validation à utiliser par le fournisseur de l'unité la persistance
        emf.setValidationMode(javax.persistence.ValidationMode.AUTO); // Si un fournisseur de validation de bean est présent dans l'environnement, le
                                                                      // fournisseur de persistance doit effectuer la validation automatique des
                                                                      // entités.
        //
        emf.afterPropertiesSet();
        return emf;
    }

    /**
     * Obtenir la référence partégée du créateur de gestionnaire d'entités dans l'application.
     * 
     * @return la référence paratégée.
     * @throws CustomAppException exception levée lorsque survient une erreur.
     */
    @Primary
    @Bean(name = "sharedEntityManager")
    public SharedEntityManagerBean sharedEntityManager() throws CustomAppException
    {
        final var sharedEntityManagerBean = new SharedEntityManagerBean();
        sharedEntityManagerBean.setEntityManagerFactory(this.entityManagerFactory().getObject());
        sharedEntityManagerBean.afterPropertiesSet();
        return new SharedEntityManagerBean();
    }

    /**
     * Obtenir le gestionnaire des transactions d'accès aux données en base dans l'application.
     * 
     * @return le gestionnaire de transactions.
     */
    @Primary
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager()
    {
        //
        final var txManager = new JpaTransactionManager();

        txManager.setDataSource(this.dataSource());
        txManager.setEntityManagerFactory(entityManagerFactory().getObject());
        txManager.setJpaDialect(this.hibernatJpaDialect());
        txManager.setDefaultTimeout(TransactionDefinition.TIMEOUT_DEFAULT);
        txManager.afterPropertiesSet();
        return txManager;
    }

    /**
     * Obtenir l'adaptateur du fournisseur Hibernate d'accès aux données.
     * 
     * @return l'adapteur du fournisseur Hibernate.
     */
    @Primary
    @Bean(name = "hibernatJpaVendorAdapter")
    public JpaVendorAdapter hibernatJpaVendorAdapter()
    {
        //
        final String databasePlatform = this.databasePropsService.getJpaHibernateProps().getDatabaseName().trim();
        final var vendorAdapter = new HibernateJpaVendorAdapter();

        vendorAdapter.setDatabasePlatform(databasePlatform);
        vendorAdapter.setGenerateDdl(this.databasePropsService.getJpaHibernateProps().getGenerateDdl());
        vendorAdapter.setShowSql(this.databasePropsService.getJpaHibernateProps().getShowSql());
        vendorAdapter.setDatabase(Database.valueOf(databasePlatform));
        return vendorAdapter;
    }

    /**
     * Obtenir la dialecte JPA pour l'accès aux données en base.
     * 
     * @return la dialecte.
     */
    @Primary
    @Bean(name = "hibernatJpaDialect")
    public JpaDialect hibernatJpaDialect()
    {
        return new HibernateJpaDialect();
    }

    /**
     * Obtenir le traduction d'exceptions de persistance.
     *
     * @return le traduction des exceptions de persistance.
     */
    @Primary
    @Bean(name = "exceptionTranslationPostProcessor")
    public PersistenceExceptionTranslationPostProcessor exceptionTranslationPostProcessor()
    {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    /**
     * Obtenir le traducteur d'exceptions SQL..
     * 
     * @return le traducteur d'exceptions SQL.
     * @throws CustomAppException exception levée lorsque survient une erreur.
     */
    @Primary
    @Bean(name = "sqlExceptionTranslator")
    public SQLErrorCodeSQLExceptionTranslator sqlExceptionTranslator() throws CustomAppException
    {
        return new SQLErrorCodeSQLExceptionTranslator(this.dataSource());
    }

    /**
     * @return
     */
    public DatabasePropsService getDatabasePropsService()
    {
        return this.databasePropsService;
    }

    @VisibleForTesting
    public void setDatabasePropsService(DatabasePropsService databasePropsService)
    {
        this.databasePropsService = databasePropsService;
    }

    /**
     * Charger les propriétés aditionnelles pour Hibernate.
     * 
     * @return les propriétés aditionnelles.
     */
    private Properties additionalProperties()
    {
        // Create object and put retrieve properties
        final var properties = new Properties();

        //
        properties.put(AvailableSettings.HBM2DDL_AUTO, this.databasePropsService.getJpaHibernateProps().getDdlAuto().trim());
        properties.put(AvailableSettings.DIALECT, this.databasePropsService.getJpaHibernateProps().getDialect().trim());
        properties.put(AvailableSettings.SHOW_SQL, this.databasePropsService.getJpaHibernateProps().getShowSql());
        properties.put(AvailableSettings.FORMAT_SQL, this.databasePropsService.getJpaHibernateProps().getFormatSql());
        properties.put(AvailableSettings.USE_SQL_COMMENTS, this.databasePropsService.getJpaHibernateProps().getUseSqlComments());
        properties.put(AvailableSettings.ENABLE_LAZY_LOAD_NO_TRANS, this.databasePropsService.getJpaHibernateProps().getEnableLazy());
        properties.put(AvailableSettings.USE_SECOND_LEVEL_CACHE, this.databasePropsService.getJpaHibernateProps().getUseSecondLevelCache());
        properties.put(AvailableSettings.GENERATE_STATISTICS, this.databasePropsService.getJpaHibernateProps().getGenerateStatistics());
        properties.put(AvailableSettings.USE_REFLECTION_OPTIMIZER, this.databasePropsService.getJpaHibernateProps()
        .getBytecodeUseReflectionOptimizer());

        // Chragement avec Hibernate
        final String immportFiles = this.databasePropsService.getJpaHibernateProps().getHbm2ddlImportFiles().trim();
        if (StringUtils.isNotBlank(immportFiles)) {
            properties.put(AvailableSettings.HBM2DDL_IMPORT_FILES, immportFiles);
        }
        return properties;
    }
}
