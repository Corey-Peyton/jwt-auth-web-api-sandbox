/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ProductDAOTest.java
 * Date de création : 29 janv. 2021
 * Heure de création : 20:45:11
 * Package : fr.vincent.tuto.server.dao
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.BackendApplicationStarter;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistanceConfig;
import fr.vincent.tuto.server.model.po.Product;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests Unitaires des objets de type {@link ProductDAO}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-test.properties", "classpath:back-end-application-test.properties" })
@ContextConfiguration(name = "productDAOTest", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistanceConfig.class })
@SpringBootTest(classes = BackendApplicationStarter.class)
@ActiveProfiles("test")
@Sql(scripts = { "classpath:db/h2/schema-test-h2.sql", "classpath:db/h2/data-test-h2.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ProductDAOIT
{
    @Autowired
    private ProductDAO productDAO;

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
        this.productDAO = null;
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.ProductDAO#findOneByName(java.lang.String)}.
     */
    @Test
    void testFindOneByName()
    {
        final Optional<Product> optional = this.productDAO.findOneByName(TestsDataUtils.PRODUCT_NAME_TO_SEARCH);

        assertThat(optional).isPresent();
        assertThat(optional.get()).isNotNull();
    }

    @Test
    void testFindOneByName_WithNull()
    {
        final Optional<Product> optional = this.productDAO.findOneByName(null);

        assertThat(optional).isNotPresent();
    }

    @Test
    void testFindOneByName_WithEmpty()
    {
        final Optional<Product> optional = this.productDAO.findOneByName(null);

        assertThat(optional).isNotPresent();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.ProductDAO#findOneByNameIgnoreCase(java.lang.String)}.
     */
    @Test
    void testFindOneByNameIgnoreCase()
    {
        final Optional<Product> optional = this.productDAO.findOneByNameIgnoreCase(TestsDataUtils.PRODUCT_NAME_TO_SEARCH_LOWER_CASE);

        assertThat(optional).isPresent();
        assertThat(optional.get()).isNotNull();
    }

    @Test
    void testFindOneByNameIgnoreCasee_WithNull()
    {
        final Optional<Product> optional = this.productDAO.findOneByNameIgnoreCase(null);

        assertThat(optional).isNotPresent();
    }

    @Test
    void testFindOneByNameIgnoreCase_WithEmpty()
    {
        final Optional<Product> optional = this.productDAO.findOneByNameIgnoreCase(StringUtils.EMPTY);

        assertThat(optional).isNotPresent();
    }
    
    /**
     * Test method for {@link fr.vincent.tuto.server.dao.ProductDAO#existsByName(java.lang.String)}.
     */
    @Test
    void testExistsByName()
    {
        final Boolean exist = this.productDAO.existsByName(TestsDataUtils.PRODUCT_NAME_TO_SEARCH);

        assertThat(exist).isTrue(); 
    }
    
    @Test
    void testExistsByName_WithLowerCaseName()
    {
        final Boolean exist = this.productDAO.existsByName(TestsDataUtils.PRODUCT_NAME_TO_SEARCH_LOWER_CASE);

        assertThat(exist).isFalse(); 
    }
    
    @Test
    void testExistsByName_WithEmpty()
    {
        final Boolean exist = this.productDAO.existsByName(StringUtils.EMPTY);

        assertThat(exist).isFalse(); 
    }
    
    @Test
    void testExistsByName_WithNull()
    {
        final Boolean exist = this.productDAO.existsByName(null);

        assertThat(exist).isFalse(); 
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.dao.ProductDAO#findAllByIsActive(java.lang.Boolean, org.springframework.data.domain.Pageable)}.
     */
    @Test
    void testFindAllByIsActiveBooleanPageable()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        Page<Product> products = this.productDAO.findAllByIsActive(Boolean.TRUE, paging);

        assertThat(products).isNotNull();
        assertThat(products.getContent()).isNotEmpty();
        assertThat(products.getContent().size()).isPositive();
    }

    @Test
    void testFindAllByIsActiveBooleanPageable_WithFalse()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        Page<Product> products = this.productDAO.findAllByIsActive(Boolean.FALSE, paging);

        assertThat(products).isNotNull();
        assertThat(products.getContent()).isEmpty();
        assertThat(products.getContent().size()).isNotPositive();
    }

    @Test
    void testFindAllByIsActiveBooleanPageable_WithNull()
    {
        Page<Product> products = this.productDAO.findAllByIsActive(null, null);

        assertThat(products).isNotNull();
        assertThat(products.getContent()).isEmpty();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.ProductDAO#findAllByIsActive(java.lang.Boolean)}.
     */
    @Test
    void testFindAllByIsActiveBoolean()
    {
        final List<Product> products = (List<Product>) this.productDAO.findAllByIsActive(Boolean.TRUE);
        
        assertThat(products.isEmpty()).isFalse();
        assertThat(products.size()).isPositive();
    }
    
    @Test
    void testFindAllByIsActiveBoolean_WithFalse()
    {
        final List<Product> products = (List<Product>) this.productDAO.findAllByIsActive(Boolean.FALSE);
        
        assertThat(products.isEmpty()).isTrue();
        assertThat(products.size()).isNotPositive();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.ProductDAO#findAllByIsActiveIsTrue()}.
     */
    @Test
    void testFindAllByIsActiveIsTrue()
    {
        final List<Product> products = (List<Product>) this.productDAO.findAllByIsActiveIsTrue();
        
        assertThat(products.isEmpty()).isFalse();
        assertThat(products.size()).isPositive();
    }
}
