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
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.BackendApplicationStarter;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistanceConfig;
import fr.vincent.tuto.server.constants.ServerConstants;
import fr.vincent.tuto.server.model.po.User;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests Unitaires des objets de type {@link UserDAO}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-test.properties", "classpath:back-end-application-test.properties" })
@ContextConfiguration(name = "userDAOTest", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistanceConfig.class })
@SpringBootTest(classes = BackendApplicationStarter.class)
@ActiveProfiles("test")
class UserDAOTest
{
    @Autowired
    private UserDAO userDAO;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        //this.initData();
    }

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
        final Optional<User> optional = this.userDAO.findOneByUsername(TestsDataUtils.ADMIN);

        assertThat(optional).isPresent();
        assertThat(optional.get()).isNotNull();
    }

    @Test
    void testFindOneByUsername_WithEmpty()
    {
        final Optional<User> optional = this.userDAO.findOneByUsername(StringUtils.EMPTY);

        assertThat(optional).isNotPresent();
    }

    @Test
    void testFindOneByUsername_WithNull()
    {
        final Optional<User> optional = this.userDAO.findOneByUsername(null);

        assertThat(optional).isNotPresent();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#findOneByEmailIgnoreCase(java.lang.String)}.
     */
    @Test
    void testFindOneByEmailIgnoreCase()
    {
        final Optional<User> optional = this.userDAO.findOneByEmailIgnoreCase(TestsDataUtils.ADMIN_EMAIL);

        assertThat(optional).isPresent();
    }

    @Test
    void testFindOneByEmailIgnoreCase_WithEmpty()
    {
        final Optional<User> optional = this.userDAO.findOneByEmailIgnoreCase(StringUtils.EMPTY);

        assertThat(optional).isEmpty();
    }

    @Test
    void testFindOneByEmailIgnoreCase_WithNull()
    {
        final Optional<User> optional = this.userDAO.findOneByEmailIgnoreCase(null);

        assertThat(optional).isNotPresent();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#existsByUsername(java.lang.String)}.
     */
    @Test
    void testExistsByUsername()
    {
        final Boolean isExist = this.userDAO.existsByUsername(TestsDataUtils.MODERATEUR);

        assertThat(isExist).isTrue();
    }

    @Test
    void testExistsByUsername_WithEmpty()
    {
        final Boolean isExist = this.userDAO.existsByUsername(StringUtils.EMPTY);

        assertThat(isExist).isFalse();
    }

    @Test
    void testExistsByUsername_WithNull()
    {
        final Boolean isExist = this.userDAO.existsByUsername(null);

        assertThat(isExist).isFalse();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#existsByEmail(java.lang.String)}.
     */
    @Test
    void testExistsByEmail()
    {
        final Boolean isExist = this.userDAO.existsByEmail(TestsDataUtils.ADMIN_EMAIL_LOWER);

        assertThat(isExist).isTrue();
    }

    @Test
    void testExistsByEmail_WithEmpty()
    {
        final Boolean isExist = this.userDAO.existsByEmail(StringUtils.EMPTY);

        assertThat(isExist).isFalse();
    }

    @Test
    void testExistsByEmail_WithNull()
    {
        final Boolean isExist = this.userDAO.existsByEmail(null);

        assertThat(isExist).isFalse();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#findOneWithRolesById(java.lang.Long)}.
     */
    @Test
    void testFindOneWithRolesById()
    {
        final Optional<User> optional = this.userDAO.findOneWithRolesById(30L);

        assertThat(optional).isPresent();
    }

    @Test
    void testFindOneWithRolesById_ShouldBeEmpty()
    {
        final Optional<User> optional = this.userDAO.findOneWithRolesById(null);

        assertThat(optional).isEmpty();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#findOneWithRolesByUsernameIgnoreCase(java.lang.String)}.
     */
    @Test
    void testFindOneWithRolesByUsernameIgnoreCase()
    {
        final Optional<User> optional = this.userDAO.findOneWithRolesByUsernameIgnoreCase(TestsDataUtils.MODERATEUR);

        assertThat(optional).isPresent();
    }

    @Test
    void testFindOneWithRolesByUsernameIgnoreCase_WithUpperCase()
    {
        final Optional<User> optional = this.userDAO.findOneWithRolesByUsernameIgnoreCase(TestsDataUtils.MODERATEUR_UPPER);

        assertThat(optional).isPresent();
    }

    @Test
    void testFindOneWithRolesByUsernameIgnoreCase_WithEmpty()
    {
        final Optional<User> optional = this.userDAO.findOneWithRolesByUsernameIgnoreCase(StringUtils.EMPTY);

        assertThat(optional).isEmpty();
    }

    @Test
    void testFindOneWithRolesByUsernameIgnoreCase_ShouldBeEmpty()
    {
        final Optional<User> optional = this.userDAO.findOneWithRolesByUsernameIgnoreCase(null);

        assertThat(optional).isEmpty();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#findOneWithRolesByEmailIgnoreCase(java.lang.String)}.
     */
    @Test
    void testFindOneWithRolesByEmailIgnoreCase()
    {
        final Optional<User> optional = this.userDAO.findOneWithRolesByEmailIgnoreCase(TestsDataUtils.ADMIN_EMAIL);

        assertThat(optional).isPresent();
    }

    @Test
    void testFindOneWithRolesByEmailIgnoreCase_WithEmpty()
    {
        final Optional<User> optional = this.userDAO.findOneWithRolesByEmailIgnoreCase(StringUtils.EMPTY);

        assertThat(optional).isEmpty();
    }

    @Test
    void testFindOneWithRolesByEmailIgnoreCase_ShouldBeEmpty()
    {
        final Optional<User> optional = this.userDAO.findOneWithRolesByEmailIgnoreCase(null);

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

        final Page<User> users = this.userDAO.findAllByUsername(TestsDataUtils.MODERATEUR, paging);

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

        final Page<User> users = this.userDAO.findAllByUsername(StringUtils.EMPTY, paging);

        assertThat(users).isNotNull();
        assertThat(users.getContent()).isEmpty();
        assertThat(users.getContent().size()).isNotPositive();
    }

    @Test
    void testFindAllByUsernameStringPageable_ShouldBeEmpty()
    {
        final Page<User> users = this.userDAO.findAllByUsername(null, null);

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

        final Page<User> user = this.userDAO.findByUsernameContains(TestsDataUtils.USER_ADMIN_USERNAME, paging);

        assertThat(user).isNotNull();
        assertThat(user.getContent()).isNotEmpty();
        assertThat(user.getContent().size()).isEqualTo(1);
    }

    @Test
    void testFindByUsernameContains_ShouldThrowException()
    {

        final Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.userDAO.findByUsernameContains(null, null);
        });

        String expectedMessage = "Value must not be null!;";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#findAllByUsername(java.lang.String)}.
     */
    @Test
    void testFindAllByUsernameString()
    {
        final List<User> users = (List<User>) this.userDAO.findAllByUsername(TestsDataUtils.ADMIN);

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(1);
    }

    @Test
    void testFindAllByUsernameString_ShouldThrowException()
    {
        final List<User> users = (List<User>) this.userDAO.findAllByUsername(null);

        assertThat(users).isEmpty();
        assertThat(users.size()).isNotPositive();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.dao.UserDAO#findAllByEnabled(java.lang.Boolean)}.
     */
    @Test
    void testFindAllByEnabled()
    {
        final List<User> users = (List<User>) this.userDAO.findAllByEnabled(Boolean.TRUE);

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(6);
        assertThat(ServerConstants.EMAI_PATTERN.matcher(users.get(0).getEmail()).matches()).isTrue();
        assertThat(ServerConstants.LOGIN_PATTERN.matcher(users.get(0).getUsername()).matches()).isTrue();
        assertThat(ServerConstants.PASSWORD_PATTERN.matcher(users.get(0).getPassword()).matches()).isTrue();
    }

    @Test
    void testFindAllByEnabled_WithFalse()
    {
        final List<User> users = (List<User>) this.userDAO.findAllByEnabled(Boolean.FALSE);

        assertThat(users).isEmpty();
    }

    @SuppressWarnings("unused")
    private void initData()
    {
        TestsDataUtils.creerJeuDeDonnees()//
        .forEach(user -> this.userDAO.saveAndFlush(user));
    }
}
