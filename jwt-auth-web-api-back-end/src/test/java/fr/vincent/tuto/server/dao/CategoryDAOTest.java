/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : CategoryDAOTest.java
 * Date de création : 29 janv. 2021
 * Heure de création : 19:45:25
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
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistanceContextConfig;
import fr.vincent.tuto.server.model.po.Category;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests nitares des objets de type {@link CategoryDAO}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-common-test.properties", "classpath:back-end-application-test.properties" })
@ContextConfiguration(name = "categoryDAOTest", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistanceContextConfig.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CategoryDAOTest
{
    @Autowired
    private CategoryDAO categoryDAO;

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.categoryDAO = null;
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.CategoryDAO#findOneByName(java.lang.String)}.
     */
    @Test
    void testFindOneByName()
    {
        final Optional<Category> optional = this.categoryDAO.findOneByName(TestsDataUtils.CATEGORY_NAME_TO_SEARCH);

        assertThat(optional).isPresent();
        assertThat(optional.get()).isNotNull();
    }

    @Test
    void testFindOneByName_WithNull()
    {
        final var optional = this.categoryDAO.findOneByName(null);

        assertThat(optional).isNotPresent();
    }

    @Test
    void testFindOneByName_WithEmpty()
    {
        final var optional = this.categoryDAO.findOneByName(StringUtils.EMPTY);

        assertThat(optional).isNotPresent();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.CategoryDAO#findOneByNameIgnoreCase(java.lang.String)}.
     */
    @Test
    void testFindOneByNameIgnoreCase()
    {
        final var optional = this.categoryDAO.findOneByNameIgnoreCase(TestsDataUtils.CATEGORY_NAME_TO_SEARCH_LOWER_CASE);

        assertThat(optional).isPresent();
        assertThat(optional.get()).isNotNull();
    }

    @Test
    void testFindOneByNameIgnoreCase_WithNull()
    {
        final var optional = this.categoryDAO.findOneByNameIgnoreCase(null);

        assertThat(optional).isNotPresent();
    }

    @Test
    void testFindOneByNameIgnoreCase_WithEmpty()
    {
        final var optional = this.categoryDAO.findOneByNameIgnoreCase(StringUtils.EMPTY);

        assertThat(optional).isNotPresent();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.CategoryDAO#existsByName(java.lang.String)}.
     */
    @Test
    void testExistsByName()
    {
        final var exist = this.categoryDAO.existsByName(TestsDataUtils.CATEGORY_NAME_TO_SEARCH);

        assertThat(exist).isTrue();
    }

    @Test
    void testExistsByName_WithLowerCaseName()
    {
        final var exist = this.categoryDAO.existsByName(TestsDataUtils.CATEGORY_NAME_TO_SEARCH_LOWER_CASE);

        assertThat(exist).isFalse();
    }

    @Test
    void testExistsByName_WithNull()
    {
        final var exist = this.categoryDAO.existsByName(null);

        assertThat(exist).isFalse();
    }

    @Test
    void testExistsByName_WithEmpty()
    {
        final var exist = this.categoryDAO.existsByName(StringUtils.EMPTY);

        assertThat(exist).isFalse();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.dao.CategoryDAO#findAllByEnabled(java.lang.Boolean, org.springframework.data.domain.Pageable)}.
     */
    @Test
    void testFindAllByEnabledBooleanPageable()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        final var categories = this.categoryDAO.findAllByEnabled(Boolean.TRUE, paging);

        assertThat(categories).isNotNull();
        assertThat(categories.getContent()).isNotEmpty();
        assertThat(categories.getContent().size()).isPositive();
    }

    @Test
    void testFindAllByEnabledBooleanPageable_WithFalse()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        final var categories = this.categoryDAO.findAllByEnabled(Boolean.FALSE, paging);

        assertThat(categories).isNotNull();
        assertThat(categories.getContent()).isEmpty();
        assertThat(categories.getContent().size()).isNotPositive();
    }

    @Test
    void testFindAllByEnabledBooleanPageable_WithNull()
    {
        final var categories = this.categoryDAO.findAllByEnabled(null, null);

        assertThat(categories).isNotNull();
        assertThat(categories.getContent()).isEmpty();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.CategoryDAO#findAllByEnabled(java.lang.Boolean)}.
     */
    @Test
    void testFindAllByEnabledBoolean()
    {
        final var categories = (List<Category>) this.categoryDAO.findAllByEnabled(Boolean.TRUE);

        assertThat(categories.isEmpty()).isFalse();
        assertThat(categories.size()).isPositive();
    }

    @Test
    void testFindAllByEnabledBoolean_WithFalse()
    {
        final var categories = (List<Category>) this.categoryDAO.findAllByEnabled(Boolean.FALSE);

        assertThat(categories.isEmpty()).isTrue();
        assertThat(categories.size()).isNotPositive();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.CategoryDAO#findAllByEnabledIsTrue()}.
     */
    @Test
    void testFindAllByEnabledIsTrue()
    {
        final var categories = (List<Category>) this.categoryDAO.findAllByEnabledIsTrue();

        assertThat(categories.isEmpty()).isFalse();
        assertThat(categories.size()).isPositive();
    }

    @SuppressWarnings("unused")
    private void initData()
    {
        TestsDataUtils.CATEGORIES()//
        .forEach(category -> this.categoryDAO.saveAndFlush(category));
    }
}
