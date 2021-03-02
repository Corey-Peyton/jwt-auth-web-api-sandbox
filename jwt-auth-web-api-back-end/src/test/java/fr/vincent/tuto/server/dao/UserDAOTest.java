/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : UserDAOTest.java
 * Date de création : 29 janv. 2021
 * Heure de création : 21:29:39
 * Package : fr.vincent.tuto.server.dao
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistenceContextConfig;
import fr.vincent.tuto.server.model.po.User;
import fr.vincent.tuto.server.util.ServerUtil;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests Unitaires des objets de type {@link UserDAO}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-common-test.properties", "classpath:back-end-application-test.properties", "classpath:back-end-tls-test.properties" })
@ContextConfiguration(name = "userDAOTest", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistenceContextConfig.class })
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("test")
class UserDAOTest
{
    @Autowired
    private UserDAO userDAO;

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.userDAO = null;
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#findOneByUsername(java.lang.String)}.
     */
    @Test
    void testFindOneByUsername()
    {
        final var optional = this.userDAO.findOneByUsername(TestsDataUtils.ADMIN);

        assertThat(optional).isPresent();
        assertThat(optional.get()).isNotNull();
    }

    @Test
    void testFindOneByUsername_WithEmpty()
    {
        final var optional = this.userDAO.findOneByUsername(StringUtils.EMPTY);

        assertThat(optional).isNotPresent();
    }

    @Test
    void testFindOneByUsername_WithNull()
    {
        final var optional = this.userDAO.findOneByUsername(null);

        assertThat(optional).isNotPresent();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#findOneByEmailIgnoreCase(java.lang.String)}.
     */
    @Test
    void testFindOneByEmailIgnoreCase()
    {
        final var optional = this.userDAO.findOneByEmailIgnoreCase(TestsDataUtils.ADMIN_EMAIL);

        assertThat(optional).isPresent();
    }

    @Test
    void testFindOneByEmailIgnoreCase_WithEmpty()
    {
        final var optional = this.userDAO.findOneByEmailIgnoreCase(StringUtils.EMPTY);

        assertThat(optional).isEmpty();
    }

    @Test
    void testFindOneByEmailIgnoreCase_WithNull()
    {
        final var optional = this.userDAO.findOneByEmailIgnoreCase(null);

        assertThat(optional).isNotPresent();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#existsByUsername(java.lang.String)}.
     */
    @Test
    void testExistsByUsername()
    {
        final var isExist = this.userDAO.existsByUsername(TestsDataUtils.MODERATEUR);

        assertThat(isExist).isTrue();
    }

    @Test
    void testExistsByUsername_WithEmpty()
    {
        final var isExist = this.userDAO.existsByUsername(StringUtils.EMPTY);

        assertThat(isExist).isFalse();
    }

    @Test
    void testExistsByUsername_WithNull()
    {
        final var isExist = this.userDAO.existsByUsername(null);

        assertThat(isExist).isFalse();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#existsByEmail(java.lang.String)}.
     */
    @Test
    void testExistsByEmail()
    {
        final var isExist = this.userDAO.existsByEmail(TestsDataUtils.ADMIN_EMAIL_LOWER);

        assertThat(isExist).isTrue();
    }

    @Test
    void testExistsByEmail_WithEmpty()
    {
        final var isExist = this.userDAO.existsByEmail(StringUtils.EMPTY);

        assertThat(isExist).isFalse();
    }

    @Test
    void testExistsByEmail_WithNull()
    {
        final var isExist = this.userDAO.existsByEmail(null);

        assertThat(isExist).isFalse();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#findOneWithRolesById(java.lang.Long)}.
     */
    @Test
    void testFindOneWithRolesById()
    {
        final var optional = this.userDAO.findOneWithRolesById(30L);

        assertThat(optional).isPresent();
    }

    @Test
    void testFindOneWithRolesById_ShouldBeEmpty()
    {
        final var optional = this.userDAO.findOneWithRolesById(null);

        assertThat(optional).isEmpty();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#findOneWithRolesByUsernameIgnoreCase(java.lang.String)}.
     */
    @Test
    void testFindOneWithRolesByUsernameIgnoreCase()
    {
        final var optional = this.userDAO.findOneWithRolesByUsernameIgnoreCase(TestsDataUtils.MODERATEUR);

        assertThat(optional).isPresent();
    }

    @Test
    void testFindOneWithRolesByUsernameIgnoreCase_WithUpperCase()
    {
        final var optional = this.userDAO.findOneWithRolesByUsernameIgnoreCase(TestsDataUtils.MODERATEUR_UPPER);

        assertThat(optional).isPresent();
    }

    @Test
    void testFindOneWithRolesByUsernameIgnoreCase_WithEmpty()
    {
        final var optional = this.userDAO.findOneWithRolesByUsernameIgnoreCase(StringUtils.EMPTY);

        assertThat(optional).isEmpty();
    }

    @Test
    void testFindOneWithRolesByUsernameIgnoreCase_ShouldBeEmpty()
    {
        final var optional = this.userDAO.findOneWithRolesByUsernameIgnoreCase(null);

        assertThat(optional).isEmpty();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#findOneWithRolesByEmailIgnoreCase(java.lang.String)}.
     */
    @Test
    void testFindOneWithRolesByEmailIgnoreCase()
    {
        final var optional = this.userDAO.findOneWithRolesByEmailIgnoreCase(TestsDataUtils.ADMIN_EMAIL);

        assertThat(optional).isPresent();
    }

    @Test
    void testFindOneWithRolesByEmailIgnoreCase_WithEmpty()
    {
        final var optional = this.userDAO.findOneWithRolesByEmailIgnoreCase(StringUtils.EMPTY);

        assertThat(optional).isEmpty();
    }

    @Test
    void testFindOneWithRolesByEmailIgnoreCase_ShouldBeEmpty()
    {
        final var optional = this.userDAO.findOneWithRolesByEmailIgnoreCase(null);

        assertThat(optional).isEmpty();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.dao.UserDAO#findAllByUsername(java.lang.String, org.springframework.data.domain.Pageable)}.
     */
    @Test
    void testFindAllByUsernameStringPageable()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        final var users = this.userDAO.findAllByUsername(TestsDataUtils.MODERATEUR, paging);

        assertThat(users).isNotNull();
        assertThat(users.getContent()).isNotEmpty();
        assertThat(users.getContent().size()).isEqualTo(1);
    }

    @Test
    void testFindAllByUsernameStringPageable_WithEmpty()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        final var users = this.userDAO.findAllByUsername(StringUtils.EMPTY, paging);

        assertThat(users).isNotNull();
        assertThat(users.getContent()).isEmpty();
        assertThat(users.getContent().size()).isNotPositive();
    }

    @Test
    void testFindAllByUsernameStringPageable_ShouldBeEmpty()
    {
        final var users = this.userDAO.findAllByUsername(null, null);

        assertThat(users).isNotNull();
        assertThat(users.getContent()).isEmpty();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.dao.UserDAO#findByUsernameContains(java.lang.String, org.springframework.data.domain.Pageable)}.
     */
    @Test
    void testFindByUsernameContains()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        final var user = this.userDAO.findByUsernameContains(TestsDataUtils.USER_ADMIN_USERNAME, paging);

        assertThat(user).isNotNull();
        assertThat(user.getContent()).isNotEmpty();
        assertThat(user.getContent().size()).isEqualTo(1);
    }

    @Test
    void testFindByUsernameContains_ShouldThrowException()
    {

        final var exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.userDAO.findByUsernameContains(null, null);
        });

        var expectedMessage = "Value must not be null!;";
        var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#findAllByUsername(java.lang.String)}.
     */
    @Test
    void testFindAllByUsernameString()
    {
        final var users = (List<User>) this.userDAO.findAllByUsername(TestsDataUtils.ADMIN);

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    void testFindAllByUsernameString_ShouldThrowException()
    {
        final var users = (List<User>) this.userDAO.findAllByUsername(null);

        assertThat(users).isEmpty();
        assertThat(users.size()).isNotPositive();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#findAllByEnabled(java.lang.Boolean)}.
     */
    @Test
    void testFindAllByEnabled()
    {
        final var users = (List<User>) this.userDAO.findAllByEnabled(Boolean.TRUE);

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(6);
        assertThat(ServerUtil.EMAI_PATTERN.matcher(users.get(0).getEmail()).matches()).isTrue();
        assertThat(ServerUtil.LOGIN_PATTERN.matcher(users.get(0).getUsername()).matches()).isTrue();
        assertThat(ServerUtil.PASSWORD_PATTERN.matcher(users.get(0).getPassword()).matches()).isTrue();
    }

    @Test
    void testFindAllByEnabled_WithFalse()
    {
        final var users = (List<User>) this.userDAO.findAllByEnabled(Boolean.FALSE);

        assertThat(users).isEmpty();
    }

    @SuppressWarnings("unused")
    private void initData()
    {
        TestsDataUtils.creerJeuDeDonnees()//
        .forEach(user -> this.userDAO.saveAndFlush(user));
    }
}
