/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : UserServiceIT.java
 * Date de création : 6 févr. 2021
 * Heure de création : 12:11:53
 * Package : fr.vincent.tuto.server.service.user
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistanceConfig;
import fr.vincent.tuto.server.enumeration.RoleEnum;
import fr.vincent.tuto.server.model.po.User;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests d'Intégration (composants et système) des objets de type {@link UserService}.
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-common-test.properties", "classpath:back-end-application-test.properties" })
@ContextConfiguration(name = "userServiceIT", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistanceConfig.class,
        UserService.class })
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
// @SqlGroup({ @Sql(scripts = { "classpath:db/h2/create-test-h2.sql",
// "classpath:db/h2/data-test-h2.sql" }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD),
@Sql(scripts = { "classpath:db/h2/drop-test-h2.sql", "classpath:db/h2/create-test-h2.sql",
        "classpath:db/h2/data-test-h2.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class UserServiceIT
{
    // Les constantes
    private static final String TRANSACTION_MSG = "Could not commit JPA transaction;";
    private static final String INTEGRITY_VIOLATION_MSG = "could not execute statement; SQL";
    private static final String FIND_BY_USERNAME_MSG = "Erreur lors de la recherche des informations d'un utilisateur par son login";
    private static final String FIND_BY_EMAIL_MSG = "Erreur lors de la recherche des informations d'un utilisteur par son email";
    private static final String SEARCH_BY_ID_MSG = "Erreur lors de la recherche des informations d'un utilisteur et ses rôles";
    private static final String INVALID_DATA_ACCES_MSG = "Value must not be null!";

    @Autowired
    private IUserService userService;

    private User user;
    private Set<RoleEnum> roles;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        // Création des droits de l'utilisateur
        this.roles = new HashSet<>();
        this.roles.add(RoleEnum.ROLE_ADMIN);
        this.roles.add(RoleEnum.ROLE_USER);

        // Instance de l'utilisateur pour le test
        this.user = TestsDataUtils.createUserWithSet(this.roles, "test", "test_19511982#", "test.test@live.fr");
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.userService = null;
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.user.UserService#createUser(fr.vincent.tuto.server.model.po.User)}.
     */
    @Test
    void testCreateUser()
    {
        final User savedUser = this.userService.createUser(this.user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isPositive();

        final List<User> users = (List<User>) this.userService.getUsers();

        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(7); // Le fichier data-test-h2.sql d'insertion dans T_USERS contient déjà 6 enregistrements.
    }

    @Order(1)
    @Test
    void testCreateUser_WithNotNullFiled_IsUsernameNull()
    {
        final User internalUser = TestsDataUtils.createUserWithSet(this.roles, "test1", "test1_19511982#", "test1.test@live.fr");
        internalUser.setUsername(null);
        final Exception exception = assertThrows(TransactionSystemException.class, () -> {
            this.userService.createUser(internalUser);
        });

        final String expectedMessage = TRANSACTION_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testCreateUser_WithNotNullFiled_IsEmailNull()
    {
        final User internalUser = TestsDataUtils.createUserWithSetFull(this.roles, "test2", "test2_19511982#", "test2.test@live.fr");
        internalUser.setEmail(null);
        final Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            this.userService.createUser(internalUser);
        });

        final String expectedMessage = INTEGRITY_VIOLATION_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testCreateUser_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.createUser(null);
        });

        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#getByUsername(java.lang.String)}.
     */
    @Test
    void testGetByUsername()
    {
        final String username = TestsDataUtils.USER_ADMIN_USERNAME;
        final Optional<User> userOptional = this.userService.getByUsername(username);

        assertThat(userOptional).isPresent();
        final User user = userOptional.get();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isPositive();
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getRoles().size()).isPositive();
    }

    @Test
    void testGetByUsername_Moderateur()
    {
        final String username = TestsDataUtils.USER_MODERATEUR_USERNAME;
        final Optional<User> userOptional = this.userService.getByUsername(username);

        assertThat(userOptional).isPresent();
        final User user = userOptional.get();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isPositive();
        assertThat(user.getUsername()).isEqualToIgnoringCase(username);
        assertThat(user.getRoles().size()).isEqualTo(2);
    }

    @Test
    void testGetByUsername_UpperCase()
    {
        final String username = TestsDataUtils.USER_MODERATEUR_USERNAME;
        final String upper = username.toUpperCase();
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getByUsername(upper);
        });

        final String expectedMessage = FIND_BY_USERNAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetByUsername_NotExistUsername()
    {
        final String username = TestsDataUtils.USER_ADMIN_USERNAME + "not exist";

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getByUsername(username);
        });

        final String expectedMessage = FIND_BY_USERNAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetByUsername_Empty()
    {
        final String username = StringUtils.EMPTY;
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getByUsername(username);
        });

        final String expectedMessage = FIND_BY_USERNAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

    }

    @Test
    void testGetByUsername_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getByUsername(null);
        });

        final String expectedMessage = FIND_BY_USERNAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#getByEmailIgnoreCase(java.lang.String)}.
     */
    @Test
    void testGetByEmailIgnoreCase()
    {
        final String email = TestsDataUtils.ADMIN_EMAIL_LOWER;
        Optional<User> userOptional = this.userService.getByEmailIgnoreCase(email);
        assertThat(userOptional).isPresent();
        final User user = userOptional.get();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isPositive();
        assertThat(user.getEmail()).isEqualToIgnoringCase(email);
        assertThat(user.getRoles().size()).isPositive();
    }

    @Test
    void testGetByEmailIgnoreCase_UpperCase()
    {
        final String email = TestsDataUtils.ADMIN_EMAIL;
        Optional<User> userOptional = this.userService.getByEmailIgnoreCase(email);
        assertThat(userOptional).isPresent();
        final User user = userOptional.get();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isPositive();
        assertThat(user.getEmail()).isEqualToIgnoringCase(email);
        assertThat(user.getRoles().size()).isPositive();
    }

    @Test
    void testGetByEmailIgnoreCase_NotExistEmail()
    {
        final String email = "patatipatata.test@test.com";
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getByEmailIgnoreCase(email);
        });

        final String expectedMessage = FIND_BY_EMAIL_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetByEmailIgnoreCase_WithEmpty()
    {
        final String email = StringUtils.EMPTY;
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getByEmailIgnoreCase(email);
        });

        final String expectedMessage = FIND_BY_EMAIL_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetByEmailIgnoreCase_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getByEmailIgnoreCase(null);
        });

        final String expectedMessage = FIND_BY_EMAIL_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#getExistsByUsername(java.lang.String)}.
     */
    @Test
    void testGetExistsByUsername()
    {
        final String username = TestsDataUtils.USER_MODERATEUR_USERNAME;
        final Boolean exist = this.userService.getExistsByUsername(username);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isTrue();
    }

    @Test
    void testGetExistsByUsername_WithFalse()
    {
        final String username = TestsDataUtils.USER_MODERATEUR_USERNAME;
        final String upper = username.toUpperCase();
        final Boolean exist = this.userService.getExistsByUsername(upper);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();
    }

    @Test
    void testGetExistsByUsername_WithEmptyUsername()
    {
        final String username = StringUtils.EMPTY;
        final Boolean exist = this.userService.getExistsByUsername(username);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();
    }

    @Test
    void testGetExistsByUsername_WithNull()
    {
        final Boolean exist = this.userService.getExistsByUsername(null);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#getExistsByEmail(java.lang.String)}.
     */
    @Test
    void testGetExistsByEmail()
    {
        final String email = TestsDataUtils.ADMIN_EMAIL_LOWER;
        final Boolean exist = this.userService.getExistsByEmail(email);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isTrue();
    }

    @Test
    void testGetExistsByEmail_UpperCase()
    {
        final String email = TestsDataUtils.ADMIN_EMAIL;
        final Boolean exist = this.userService.getExistsByEmail(email);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();
    }

    @Test
    void testGetExistsByEmail_WithEmpty()
    {
        final String email = StringUtils.EMPTY;
        final Boolean exist = this.userService.getExistsByEmail(email);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();
    }

    @Test
    void testGetExistsByEmail_WithNull()
    {
        final Boolean exist = this.userService.getExistsByEmail(null);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#getWithRolesById(java.lang.Long)}.
     */
    @Test
    void testGetWithRolesById()
    {
        final Long id = 31L;
        Optional<User> userOptional = this.userService.getWithRolesById(id);

        assertThat(userOptional).isPresent();
        final User user = userOptional.get();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isPositive();
        assertThat(user.getId()).isEqualTo(id);
        assertThat(user.getRoles().size()).isPositive();
    }

    @Test
    void testGetWithRolesById_WithNotExistId()
    {
        final Long id = Long.MAX_VALUE;
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getWithRolesById(id);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetWithRolesById_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getWithRolesById(null);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.user.UserService#getWithRolesByUsernameIgnoreCase(java.lang.String)}.
     */
    @Test
    void testGetWithRolesByUsernameIgnoreCase()
    {
        final String username = TestsDataUtils.USER_MODERATEUR_USERNAME;
        Optional<User> userOptional = this.userService.getWithRolesByUsernameIgnoreCase(username);

        assertThat(userOptional).isPresent();
        final User user = userOptional.get();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isPositive();
        assertThat(user.getUsername()).isEqualToIgnoringCase(username);
        assertThat(user.getRoles().size()).isPositive();
    }

    @Test
    void testGetWithRolesByUsernameIgnoreCase_UpperCase()
    {
        final String username = TestsDataUtils.USER_MODERATEUR_USERNAME;
        final String upper = username.toUpperCase();
        Optional<User> userOptional = this.userService.getWithRolesByUsernameIgnoreCase(upper);

        assertThat(userOptional).isPresent();
        final User user = userOptional.get();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isPositive();
        assertThat(user.getUsername()).isEqualToIgnoringCase(username);
        assertThat(user.getRoles().size()).isPositive();
    }

    @Test
    void testGetWithRolesByUsernameIgnoreCase_WithEmpty()
    {
        final String username = StringUtils.EMPTY;
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getWithRolesByUsernameIgnoreCase(username);
        });

        final String expectedMessage = FIND_BY_USERNAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetWithRolesByUsernameIgnoreCase_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getWithRolesByUsernameIgnoreCase(null);
        });

        final String expectedMessage = FIND_BY_USERNAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.user.UserService#getWithRolesByEmailIgnoreCase(java.lang.String)}.
     */
    @Test
    void testGetWithRolesByEmailIgnoreCase()
    {
        final String email = TestsDataUtils.ADMIN_EMAIL_LOWER;
        Optional<User> userOptional = this.userService.getWithRolesByEmailIgnoreCase(email);

        assertThat(userOptional).isPresent();
        final User user = userOptional.get();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isPositive();
        assertThat(user.getEmail()).isEqualToIgnoringCase(email);
        assertThat(user.getRoles().size()).isPositive();
    }

    @Test
    void testGetWithRolesByEmailIgnoreCase_Uppercase()
    {
        final String email = TestsDataUtils.ADMIN_EMAIL;
        Optional<User> userOptional = this.userService.getWithRolesByEmailIgnoreCase(email);

        assertThat(userOptional).isPresent();
        final User user = userOptional.get();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isPositive();
        assertThat(user.getEmail()).isEqualToIgnoringCase(email);
        assertThat(user.getRoles().size()).isPositive();
    }

    @Test
    void testGetWithRolesByEmailIgnoreCase_WithEmpty()
    {
        final String email = StringUtils.EMPTY;
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getWithRolesByEmailIgnoreCase(email);
        });

        final String expectedMessage = FIND_BY_EMAIL_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetWithRolesByEmailIgnoreCase_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getWithRolesByEmailIgnoreCase(null);
        });

        final String expectedMessage = FIND_BY_EMAIL_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.user.UserService#getAllByUsernameContains(java.lang.String, org.springframework.data.domain.Pageable)}.
     */
    @Test
    void testGetAllByUsernameContains()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        final String username = TestsDataUtils.USER_ADMIN_USERNAME;

        final Page<User> result = this.userService.getAllByUsernameContains(username, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isEqualTo(1); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(5); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(1); // Le nombre total de pages
        assertThat(result.getContent().size()).isEqualTo(1); // La taille du contenu
    }

    @Test
    void testGetAllByUsernameContains_WithClient()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        final String username = TestsDataUtils.CLIENT;

        final Page<User> result = this.userService.getAllByUsernameContains(username, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isEqualTo(4); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(5); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(1); // Le nombre total de pages
        assertThat(result.getContent().size()).isEqualTo(4); // La taille du contenu
    }

    @Test
    void testGetAllByUsernameContains_UpperCase()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        final String username = TestsDataUtils.USER_ADMIN_USERNAME;
        final String upper = username.toUpperCase();
        final Page<User> result = this.userService.getAllByUsernameContains(upper, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isNotPositive(); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(5); // La taille de la page
        assertThat(result.getTotalPages()).isNotPositive(); // Le nombre total de pages
        assertThat(result.getContent().size()).isNotPositive(); // La taille du contenu
    }

    @Test
    void testGetAllByUsernameContains_WithEmpty()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        final String username = StringUtils.EMPTY;
        final Page<User> result = this.userService.getAllByUsernameContains(username, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isEqualTo(5); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(5); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(2); // Le nombre total de pages
        assertThat(result.getContent().size()).isEqualTo(5); // La taille du contenu
    }

    @Test
    void testGetAllByUsernameContains_WithNull()
    {
        final Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.userService.getAllByUsernameContains(null, null);
        });

        final String expectedMessage = INVALID_DATA_ACCES_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.user.UserService#getAllByUsername(java.lang.String, org.springframework.data.domain.Pageable)}.
     */
    @Test
    void testGetAllByUsername()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        final String username = TestsDataUtils.CLIENT;

        final Page<User> result = this.userService.getAllByUsername(username, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isEqualTo(1); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(5); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(1); // Le nombre total de pages
        assertThat(result.getContent().size()).isEqualTo(1); // La taille du contenu
    }

    @Test
    void testGetAllByUsername_UpperCase()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        final String username = TestsDataUtils.CLIENT;
        final String upper = username.toUpperCase();

        final Page<User> result = this.userService.getAllByUsername(upper, paging);

        assertThat(result).isNotNull();
    }

    @Test
    void testGetAllByUsername_WithEmpty()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        final String username = StringUtils.EMPTY;
        final Page<User> result = this.userService.getAllByUsername(username, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isNotPositive(); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(5); // La taille de la page
        assertThat(result.getTotalPages()).isNotPositive(); // Le nombre total de pages
        assertThat(result.getContent().size()).isNotPositive(); // La taille du contenu
    }

    @Test
    void testGetAllByUsername_WithNull()
    {
        // final Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
        // this.userService.getAllByUsername(null, null);
        // });
        //
        // final String expectedMessage = INVALID_DATA_ACCES_MSG;
        // final String actualMessage = exception.getMessage();
        //
        // assertThat(actualMessage.length()).isPositive();
        // assertThat(actualMessage).contains(expectedMessage);

        final Page<User> result = this.userService.getAllByUsername(null, null);
        assertThat(result).isNotNull();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#getUsers()}.
     */
    @Test
    void testGetUsers()
    {
        final List<User> result = (List<User>) this.userService.getUsers();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(6); // Le fichier data-test-h2.sql d'insertion dans T_USERS contient déjà 6 enregistrements.
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#getAllByEnabled(java.lang.Boolean)}.
     */
    @Test
    void testGetAllByEnabled()
    {
        final List<User> result = (List<User>) this.userService.getAllByEnabled(Boolean.TRUE);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(6);
    }

    @Test
    void testGetAllByEnabled_WithFlase()
    {
        final List<User> result = (List<User>) this.userService.getAllByEnabled(Boolean.FALSE);

        assertThat(result).isEmpty();
        assertThat(result.size()).isNotPositive();
    }

    @Test
    void testGetAllByEnabled_WithNull()
    {
        final List<User> result = (List<User>) this.userService.getAllByEnabled(null);

        assertThat(result).isEmpty();
        assertThat(result.size()).isNotPositive();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#deleteUser(java.lang.Long)}.
     */
    @Test
    void testDeleteUser()
    {
        final User internalUser = TestsDataUtils.createUserWithSetFull(this.roles, "test2", "test2_19511982#", "test2.test@live.fr");
        final User savedUser = this.userService.createUser(internalUser);
        final Optional<User> userFromDB = this.userService.getWithRolesById(savedUser.getId());

        assertThat(userFromDB).isPresent();
        TestsDataUtils.assertAllUserWithoutTime(savedUser, userFromDB.get());
        final Long idToDeleted = userFromDB.get().getId();

        this.userService.deleteUser(idToDeleted);

        List<User> users = (List<User>) this.userService.getUsers();
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isEqualTo(6); // On supprime l'enregistrement qui vient d'être rajouté.
    }

    @Test
    void testDeleteUser_WithNotExistId()
    {
        final Long id = Long.MAX_VALUE;
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.deleteUser(id);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testDeleteUser_WithNullId()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.deleteUser(null);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.user.UserService#updateUser(java.lang.Long, fr.vincent.tuto.server.model.po.User)}.
     */
    @Test
    void testUpdateUser()
    {
        final User internalUser = TestsDataUtils.createUserWithSetFull(this.roles, "test2", "test2_19511982#", "test2.test@live.fr");
        final User userToUpdated = this.userService.createUser(internalUser);

        assertThat(userToUpdated).isNotNull();
        assertThat(userToUpdated.getId()).isNotNull();

        final Long id = userToUpdated.getId();
        userToUpdated.setUsername("test2_update");
        userToUpdated.setEmail("test2_update.test@live.fr");
        userToUpdated.setPassword(new BCryptPasswordEncoder(12).encode("test2_update_19511982#"));

        this.userService.updateUser(id, userToUpdated);

        assertThat(userToUpdated.getId()).isPositive();
        assertThat(userToUpdated.getEmail()).isEqualToIgnoringCase("test2_update.test@live.fr");
        assertThat(userToUpdated.getEnabled()).isTrue();
        assertThat(userToUpdated.getPassword()).contains("$2a$12$");
    }

    @Test
    void testUpdateUser_WitNotExistId()
    {
        final User internalUser = TestsDataUtils.createUserWithSetFull(this.roles, "test2", "test2_19511982#", "test2.test@live.fr");
        final User userToUpdated = this.userService.createUser(internalUser);

        final Long id = Long.MAX_VALUE;
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.updateUser(id, userToUpdated);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testUpdateUser_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.userService.updateUser(null, null);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }
}
