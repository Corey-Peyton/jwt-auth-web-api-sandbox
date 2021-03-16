/*
 * ----------------------------------------------
 * Projet ou Module : oauth-common-app
 * Nom de la classe : JwtAppDbPropsService.java
 * Date de création : 9 mars 2021
 * Heure de création : 10:42:02
 * Package : fr.vincent.tuto.common.service.props
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */	
package fr.vincent.tuto.server.service.db;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Vincent Otchoun
 *
 */
@Component
@ConfigurationProperties(prefix = "vot", ignoreInvalidFields = false, ignoreUnknownFields = true)
public class JwtAppDbPropsService
{
    /*
     * Création des instances de chargement des propriétés définies
     */
    private final DataSourceProps dataSourceProps = new DataSourceProps();
    private final JpaHibernateProps jpaHibernateProps = new JpaHibernateProps();
    private final HikariProps hikariProps = new HikariProps();
    private final FlywayProps flywayProps = new FlywayProps();

    /*
     * Accesseurs
     */
    public DataSourceProps getDataSourceProps()
    {
        return this.dataSourceProps;
    }

    public JpaHibernateProps getJpaHibernateProps()
    {
        return this.jpaHibernateProps;
    }

    public HikariProps getHikariProps()
    {
        return this.hikariProps;
    }

    public FlywayProps getFlywayProps()
    {
        return this.flywayProps;
    }

    /**
     * Chargement des propriétés externalisées et personnalisées pour la migration de scripts de base de données avec Flyway
     * 
     * @author Vincent Otchoun
     */
    public static class FlywayProps
    {
        private Boolean enabled;
        private Boolean group;
        private Boolean baselineOnMigrate;
        private String sqlMigrationPrefix;
        private String sqlMigrationSeparator;
        private String sqlMigrationSuffixes;
        private String locations;
        private String url;
        private String user;
        private String password;

        public Boolean getEnabled()
        {
            return this.enabled;
        }

        public Boolean getGroup()
        {
            return this.group;
        }

        public Boolean getBaselineOnMigrate()
        {
            return this.baselineOnMigrate;
        }

        public String getSqlMigrationPrefix()
        {
            return this.sqlMigrationPrefix;
        }

        public String getSqlMigrationSeparator()
        {
            return this.sqlMigrationSeparator;
        }

        public String getSqlMigrationSuffixes()
        {
            return this.sqlMigrationSuffixes;
        }

        public String getLocations()
        {
            return this.locations;
        }

        public String getUrl()
        {
            return this.url;
        }

        public String getUser()
        {
            return this.user;
        }

        public String getPassword()
        {
            return this.password;
        }

        public void setEnabled(final Boolean pEnabled)
        {
            this.enabled = pEnabled;
        }

        public void setGroup(final Boolean pGroup)
        {
            this.group = pGroup;
        }

        public void setBaselineOnMigrate(final Boolean pBaselineOnMigrate)
        {
            this.baselineOnMigrate = pBaselineOnMigrate;
        }

        public void setSqlMigrationPrefix(final String pSqlMigrationPrefix)
        {
            this.sqlMigrationPrefix = pSqlMigrationPrefix;
        }

        public void setSqlMigrationSeparator(final String pSqlMigrationSeparator)
        {
            this.sqlMigrationSeparator = pSqlMigrationSeparator;
        }

        public void setSqlMigrationSuffixes(final String pSqlMigrationSuffixes)
        {
            this.sqlMigrationSuffixes = pSqlMigrationSuffixes;
        }

        public void setLocations(final String pLocations)
        {
            this.locations = pLocations;
        }

        public void setUrl(final String pUrl)
        {
            this.url = pUrl;
        }

        public void setUser(final String pUser)
        {
            this.user = pUser;
        }

        public void setPassword(final String pPasswword)
        {
            this.password = pPasswword;
        }

        /////////////
        // TOSTRING
        /////////////

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString()
        {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

    /**
     * Chargement des propriétés alimentant des composants assurant la connexion à la base de données.
     * 
     * @author Vincent Otchoun
     */
    public static class DataSourceProps
    {
        private String driverClassName;
        private String jdbcUrl;
        private String userName;
        private String password;
        private Boolean testWhileIdle;
        private String validationQuery;
        private Boolean poolPreparedStatements;
        private Integer maxOpenPreparedStatements;
        private String persistenceUnitName;
        private String packageToScan;
        private Boolean generateDdl;
        private String datasourceClassName;
        private String type;
        private String platform;

        /*
         * propriétés Spring JDBC DataSource initializer pour le gestionnaire de pool Hikari.
         */
        private String cachePrepareStatements;
        private String prepareStatementCacheSize;
        private String prepareStatementCacheSqlLimit;
        private String useServerPrepareStatements;

        /*
         * Approche Non-programmatique : utiliser Spring JDBC initializer pour créer le schéma de la base de données
         * et y injecter les données initiales avec les scripts DDL/DML.
         */
        private String initializationMode;
        private String initSchema;
        private String initData;
        private Boolean initContinueOnError;

        public String getDriverClassName()
        {
            return this.driverClassName;
        }

        public String getJdbcUrl()
        {
            return this.jdbcUrl;
        }

        public String getUserName()
        {
            return this.userName;
        }

        public String getPassword()
        {
            return this.password;
        }

        public Boolean getTestWhileIdle()
        {
            return this.testWhileIdle;
        }

        public String getValidationQuery()
        {
            return this.validationQuery;
        }

        public Boolean getPoolPreparedStatements()
        {
            return this.poolPreparedStatements;
        }

        public Integer getMaxOpenPreparedStatements()
        {
            return this.maxOpenPreparedStatements;
        }

        public String getPersistenceUnitName()
        {
            return this.persistenceUnitName;
        }

        public String getPackageToScan()
        {
            return this.packageToScan;
        }

        public Boolean getGenerateDdl()
        {
            return this.generateDdl;
        }

        public String getDatasourceClassName()
        {
            return this.datasourceClassName;
        }

        public String getType()
        {
            return this.type;
        }

        public String getPlatform()
        {
            return this.platform;
        }

        public String getCachePrepareStatements()
        {
            return this.cachePrepareStatements;
        }

        public String getPrepareStatementCacheSize()
        {
            return this.prepareStatementCacheSize;
        }

        public String getPrepareStatementCacheSqlLimit()
        {
            return this.prepareStatementCacheSqlLimit;
        }

        public String getUseServerPrepareStatements()
        {
            return this.useServerPrepareStatements;
        }

        public String getInitializationMode()
        {
            return this.initializationMode;
        }

        public String getInitSchema()
        {
            return this.initSchema;
        }

        public String getInitData()
        {
            return this.initData;
        }

        public Boolean getInitContinueOnError()
        {
            return this.initContinueOnError;
        }

        public void setDriverClassName(final String pDriverClassName)
        {
            this.driverClassName = pDriverClassName;
        }

        public void setJdbcUrl(final String pJdbcUrl)
        {
            this.jdbcUrl = pJdbcUrl;
        }

        public void setUserName(final String pUserName)
        {
            this.userName = pUserName;
        }

        public void setPassword(final String pPassword)
        {
            this.password = pPassword;
        }

        public void setTestWhileIdle(final Boolean pTestWhileIdle)
        {
            this.testWhileIdle = pTestWhileIdle;
        }

        public void setValidationQuery(final String pValidationQuery)
        {
            this.validationQuery = pValidationQuery;
        }

        public void setPoolPreparedStatements(final Boolean pPoolPreparedStatements)
        {
            this.poolPreparedStatements = pPoolPreparedStatements;
        }

        public void setMaxOpenPreparedStatements(final Integer pMaxOpenPreparedStatements)
        {
            this.maxOpenPreparedStatements = pMaxOpenPreparedStatements;
        }

        public void setPersistenceUnitName(final String pPersistenceUnitName)
        {
            this.persistenceUnitName = pPersistenceUnitName;
        }

        public void setPackageToScan(final String pPackageToScan)
        {
            this.packageToScan = pPackageToScan;
        }

        public void setGenerateDdl(final Boolean pGenerateDdl)
        {
            this.generateDdl = pGenerateDdl;
        }

        public void setDatasourceClassName(final String pDatasourceClassName)
        {
            this.datasourceClassName = pDatasourceClassName;
        }

        public void setType(final String pType)
        {
            this.type = pType;
        }

        public void setPlatform(final String pPlatform)
        {
            this.platform = pPlatform;
        }

        public void setCachePrepareStatements(final String pCachePrepareStatements)
        {
            this.cachePrepareStatements = pCachePrepareStatements;
        }

        public void setPrepareStatementCacheSize(final String pPrepareStatementCacheSize)
        {
            this.prepareStatementCacheSize = pPrepareStatementCacheSize;
        }

        public void setPrepareStatementCacheSqlLimit(final String pPrepareStatementCacheSqlLimit)
        {
            this.prepareStatementCacheSqlLimit = pPrepareStatementCacheSqlLimit;
        }

        public void setUseServerPrepareStatements(final String pUseServerPrepareStatements)
        {
            this.useServerPrepareStatements = pUseServerPrepareStatements;
        }

        public void setInitializationMode(final String pInitializationMode)
        {
            this.initializationMode = pInitializationMode;
        }

        public void setInitSchema(final String pInitSchema)
        {
            this.initSchema = pInitSchema;
        }

        public void setInitData(final String pInitData)
        {
            this.initData = pInitData;
        }

        public void setInitContinueOnError(final Boolean pInitContinueOnError)
        {
            this.initContinueOnError = pInitContinueOnError;
        }

        /////////////
        // TOSTRING
        /////////////

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString()
        {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

    /**
     * Chargement des propriétés pour alimenter les composants JPA/Hibernate d'abstraction des accès aux
     * informations de base de données.
     * 
     * @author Vincent Otchoun
     */
    public static class JpaHibernateProps
    {
        private String databaseName;
        private String dialect;
        private Boolean showSql;
        private Boolean formatSql;
        private Boolean useSql;
        private Boolean useSqlComments;
        private Boolean bytecodeUseReflectionOptimizer;
        private String namingStrategy;
        private Boolean useSecondLevelCache;
        private Boolean generateStatistics;
        private Boolean useReflectionOptimizer;
        private String ddlAuto;
        private Boolean enableLazy;
        private String hbm2ddlImportFiles;
        private Boolean generateDdl;

        public String getDatabaseName()
        {
            return this.databaseName;
        }

        public String getDialect()
        {
            return this.dialect;
        }

        public Boolean getShowSql()
        {
            return this.showSql;
        }

        public Boolean getFormatSql()
        {
            return this.formatSql;
        }

        public Boolean getUseSql()
        {
            return this.useSql;
        }

        public Boolean getUseSqlComments()
        {
            return this.useSqlComments;
        }

        public Boolean getBytecodeUseReflectionOptimizer()
        {
            return this.bytecodeUseReflectionOptimizer;
        }

        public String getNamingStrategy()
        {
            return this.namingStrategy;
        }

        public Boolean getUseSecondLevelCache()
        {
            return this.useSecondLevelCache;
        }

        public Boolean getGenerateStatistics()
        {
            return this.generateStatistics;
        }

        public Boolean getUseReflectionOptimizer()
        {
            return this.useReflectionOptimizer;
        }

        public String getDdlAuto()
        {
            return this.ddlAuto;
        }

        public Boolean getEnableLazy()
        {
            return this.enableLazy;
        }

        public String getHbm2ddlImportFiles()
        {
            return this.hbm2ddlImportFiles;
        }

        public Boolean getGenerateDdl()
        {
            return this.generateDdl;
        }

        public void setDatabaseName(final String pDatabaseName)
        {
            this.databaseName = pDatabaseName;
        }

        public void setDialect(final String pDialect)
        {
            this.dialect = pDialect;
        }

        public void setShowSql(final Boolean pShowSql)
        {
            this.showSql = pShowSql;
        }

        public void setFormatSql(final Boolean pFormatSql)
        {
            this.formatSql = pFormatSql;
        }

        public void setUseSql(final Boolean pUseSql)
        {
            this.useSql = pUseSql;
        }

        public void setUseSqlComments(final Boolean pUseSqlComments)
        {
            this.useSqlComments = pUseSqlComments;
        }

        public void setBytecodeUseReflectionOptimizer(final Boolean pBytecodeUseReflectionOptimizer)
        {
            this.bytecodeUseReflectionOptimizer = pBytecodeUseReflectionOptimizer;
        }

        public void setNamingStrategy(final String pNamingStrategy)
        {
            this.namingStrategy = pNamingStrategy;
        }

        public void setUseSecondLevelCache(final Boolean pUseSecondLevelCache)
        {
            this.useSecondLevelCache = pUseSecondLevelCache;
        }

        public void setGenerateStatistics(final Boolean pGenerateStatistics)
        {
            this.generateStatistics = pGenerateStatistics;
        }

        public void setUseReflectionOptimizer(final Boolean pUseReflectionOptimizer)
        {
            this.useReflectionOptimizer = pUseReflectionOptimizer;
        }

        public void setDdlAuto(final String pDdlAuto)
        {
            this.ddlAuto = pDdlAuto;
        }

        public void setEnableLazy(final Boolean pEnableLazy)
        {
            this.enableLazy = pEnableLazy;
        }

        public void setHbm2ddlImportFiles(final String pHbm2ddlImportFiles)
        {
            this.hbm2ddlImportFiles = pHbm2ddlImportFiles;
        }

        public void setGenerateDdl(final Boolean pGenerateDdl)
        {
            this.generateDdl = pGenerateDdl;
        }

        /////////////
        // TOSTRING
        /////////////
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString()
        {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

    /**
     * Chargement des propriétés pour alimenter les composants Hikari (gestionnaire de pool d'accès aux ressources de la
     * base de données).
     * 
     * @author Vincent Otchoun
     */
    public static class HikariProps
    {
        private Integer minimumIdle;
        private Integer maximumPoolSize;
        private Long idleTimeout;
        private String poolName;
        private Long maxLifetime;
        private Long connectionTimeout;

        private Boolean cachePrepareStatements;
        private Integer prepareStatementCacheSize;
        private Integer prepareStatementCacheSqlLimit;
        private Boolean useServerPrepareStatements;

        public Integer getMinimumIdle()
        {
            return this.minimumIdle;
        }

        public Integer getMaximumPoolSize()
        {
            return this.maximumPoolSize;
        }

        public Long getIdleTimeout()
        {
            return this.idleTimeout;
        }

        public String getPoolName()
        {
            return this.poolName;
        }

        public Long getMaxLifetime()
        {
            return this.maxLifetime;
        }

        public Long getConnectionTimeout()
        {
            return this.connectionTimeout;
        }

        public Boolean getCachePrepareStatements()
        {
            return this.cachePrepareStatements;
        }

        public Integer getPrepareStatementCacheSize()
        {
            return this.prepareStatementCacheSize;
        }

        public Integer getPrepareStatementCacheSqlLimit()
        {
            return this.prepareStatementCacheSqlLimit;
        }

        public Boolean getUseServerPrepareStatements()
        {
            return this.useServerPrepareStatements;
        }

        public void setMinimumIdle(final Integer pMinimumIdle)
        {
            this.minimumIdle = pMinimumIdle;
        }

        public void setMaximumPoolSize(final Integer pMaximumPoolSize)
        {
            this.maximumPoolSize = pMaximumPoolSize;
        }

        public void setIdleTimeout(final Long pIdleTimeout)
        {
            this.idleTimeout = pIdleTimeout;
        }

        public void setPoolName(final String pPoolName)
        {
            this.poolName = pPoolName;
        }

        public void setMaxLifetime(final Long pMaxLifetime)
        {
            this.maxLifetime = pMaxLifetime;
        }

        public void setConnectionTimeout(final Long pConnectionTimeout)
        {
            this.connectionTimeout = pConnectionTimeout;
        }

        public void setCachePrepareStatements(final Boolean pCachePrepareStatements)
        {
            this.cachePrepareStatements = pCachePrepareStatements;
        }

        public void setPrepareStatementCacheSize(final Integer pPrepareStatementCacheSize)
        {
            this.prepareStatementCacheSize = pPrepareStatementCacheSize;
        }

        public void setPrepareStatementCacheSqlLimit(final Integer pPrepareStatementCacheSqlLimit)
        {
            this.prepareStatementCacheSqlLimit = pPrepareStatementCacheSqlLimit;
        }

        public void setUseServerPrepareStatements(final Boolean pUseServerPrepareStatements)
        {
            this.useServerPrepareStatements = pUseServerPrepareStatements;
        }

        /////////////
        // TOSTRING
        /////////////
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString()
        {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
        }
    }

}
