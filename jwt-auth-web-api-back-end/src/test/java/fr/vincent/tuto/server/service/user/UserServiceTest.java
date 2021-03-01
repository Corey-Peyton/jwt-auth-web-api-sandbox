/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : UserServiceTest.java
 * Date de création : 6 févr. 2021
 * Heure de création : 03:52:02
 * Package : fr.vincent.tuto.server.service.user
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistenceContextConfig;
import fr.vincent.tuto.server.dao.UserDAO;
import fr.vincent.tuto.server.enumeration.RoleEnum;
import fr.vincent.tuto.server.model.po.User;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests Unitaires des objets de type {;{@link UserService}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-common-test.properties", "classpath:back-end-application-test.properties", "classpath:back-end-tls-test.properties" })
@ContextConfiguration(name = "userServiceTest", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistenceContextConfig.class,
        UserService.class })
@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest
{
    // Les constantes
    private static final String SAVE_MSG = "Erreur lors de la sauvegarde en base de donnnées des informations";
    private static final String FIND_BY_USERNAME_MSG = "Erreur lors de la recherche des informations d'un utilisateur par son login";
    private static final String FIND_BY_EMAIL_MSG = "Erreur lors de la recherche des informations d'un utilisteur par son email";
    private static final String SEARCH_BY_ID_MSG = "Erreur lors de la recherche des informations d'un utilisteur et ses rôles";

    @MockBean
    private UserDAO userDAO;
    private UserService userService;
    private User user;
    private Set<RoleEnum> roles;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        // Instance du service utilisateur
        this.userService = new UserService(this.userDAO);

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
        this.userDAO = null;
        this.userService = null;
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.user.UserService#createUser(fr.vincent.tuto.server.model.po.User)}.
     */
    @Test
    void testCreateUser()
    {
        final User mockUser = this.user;
        final User userToSaved = this.user;

        BDDMockito.given(this.userDAO.save(Mockito.any(User.class))).willReturn(mockUser);
        final var savedUser = this.userService.createUser(userToSaved);

        assertThat(savedUser).isNotNull();
        TestsDataUtils.assertAllUser(mockUser, savedUser);
        verify(this.userDAO, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_ShouldThrowException()
    {
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userService.createUser(null);
        });

        final var expectedMessage = SAVE_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#getByUsername(java.lang.String)}.
     */
    @Test
    void testGetByUsername()
    {
        final var optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final User user = optional.get();
        user.setId(1L);
        final var username = user.getUsername();

        BDDMockito.given(this.userDAO.findOneByUsername(Mockito.any(String.class))).willReturn(optional);
        final var userFromDB = this.userService.getByUsername(username);

        assertThat(userFromDB).isPresent();
        TestsDataUtils.assertAllUser(user, userFromDB.get());

        verify(this.userDAO, times(1)).findOneByUsername(any(String.class));
    }

    @Test
    void testGetByUsername_WithEmpty()
    {
        final var username = "test";
        when(this.userDAO.findOneByUsername(username)).thenReturn(Optional.empty());
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getByUsername(username);
        });

        final var expectedMessage = FIND_BY_USERNAME_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetByUsername_WithNull()
    {
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getByUsername(null);
        });

        final var expectedMessage = FIND_BY_USERNAME_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#getByEmailIgnoreCase(java.lang.String)}.
     */
    @Test
    void testGetByEmailIgnoreCase()
    {
        final var optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final User user = optional.get();
        user.setId(1L);
        final var email = user.getEmail();

        BDDMockito.given(this.userDAO.findOneByEmailIgnoreCase(Mockito.any(String.class))).willReturn(optional);
        final var userFromDB = this.userService.getByEmailIgnoreCase(email);

        assertThat(userFromDB).isPresent();
        TestsDataUtils.assertAllUser(user, userFromDB.get());

        verify(this.userDAO, times(1)).findOneByEmailIgnoreCase(any(String.class));
    }

    @Test
    void testGetByEmailIgnoreCase_UpperCase()
    {
        final var optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final var user = optional.get();
        user.setId(1L);
        final var email = user.getEmail();
        final var upper = email.toUpperCase();

        BDDMockito.given(this.userDAO.findOneByEmailIgnoreCase(Mockito.any(String.class))).willReturn(optional);
        final var userFromDB = this.userService.getByEmailIgnoreCase(upper);

        assertThat(userFromDB).isPresent();
        TestsDataUtils.assertAllUser(user, userFromDB.get());

        verify(this.userDAO, times(1)).findOneByEmailIgnoreCase(any(String.class));
    }

    @Test
    void testGetByEmailIgnoreCase_WithEmpty()
    {
        final var email = "test";
        BDDMockito.given(this.userDAO.findOneByEmailIgnoreCase(Mockito.any(String.class))).willReturn(Optional.empty());
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getByEmailIgnoreCase(email);
        });

        final var expectedMessage = FIND_BY_EMAIL_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetByEmailIgnoreCase_WithNull()
    {
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getByEmailIgnoreCase(null);
        });

        final var expectedMessage = FIND_BY_EMAIL_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#getExistsByUsername(java.lang.String)}.
     */
    @Test
    void testGetExistsByUsername()
    {
        final var username = this.user.getUsername();
        BDDMockito.given(this.userDAO.existsByUsername(Mockito.any(String.class))).willReturn(Boolean.TRUE);
        final var exist = this.userService.getExistsByUsername(username);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isTrue();

        verify(this.userDAO, times(1)).existsByUsername(any(String.class));
    }

    @Test
    void testGetExistsByUsername_WithFalse()
    {
        final var username = this.user.getUsername();

        BDDMockito.given(this.userDAO.existsByUsername(Mockito.any(String.class))).willReturn(Boolean.FALSE);
        final var exist = this.userService.getExistsByUsername(username);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();

        verify(this.userDAO, times(1)).existsByUsername(any(String.class));
    }

    @Test
    void testGetExistsByUsername_WithNull()
    {
        final var exist = this.userService.getExistsByUsername(null);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#getExistsByEmail(java.lang.String)}.
     */
    @Test
    void testGetExistsByEmail()
    {
        final var email = this.user.getEmail();
        BDDMockito.given(this.userDAO.existsByEmail(Mockito.any(String.class))).willReturn(Boolean.TRUE);
        final var exist = this.userService.getExistsByEmail(email);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isTrue();

        verify(this.userDAO, times(1)).existsByEmail(any(String.class));
    }

    @Test
    void testGetExistsByEmail_WithFalse()
    {
        final var email = this.user.getEmail();
        BDDMockito.given(this.userDAO.existsByEmail(Mockito.any(String.class))).willReturn(Boolean.FALSE);
        final var exist = this.userService.getExistsByEmail(email);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();

        verify(this.userDAO, times(1)).existsByEmail(any(String.class));
    }

    @Test
    void testGetExistsByEmail_WithNull()
    {
        final var exist = this.userService.getExistsByEmail(null);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#getWithRolesById(java.lang.Long)}.
     */
    @Test
    void testGetWithRolesById()
    {
        final var optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final var user = optional.get();
        user.setId(1L);
        final var id = user.getId();
        BDDMockito.given(this.userDAO.findOneWithRolesById(Mockito.any(Long.class))).willReturn(optional);
        final Optional<User> userFromDB = this.userService.getWithRolesById(id);

        assertThat(userFromDB).isPresent();
        TestsDataUtils.assertAllUser(user, userFromDB.get());

        verify(this.userDAO, times(1)).findOneWithRolesById(any(Long.class));

    }

    @Test
    void testGetWithRolesById_ShouldThrowException()
    {
        final var optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final var user = optional.get();
        user.setId(1L);
        final var id = user.getId();
        BDDMockito.given(this.userDAO.findOneWithRolesById(Mockito.any(Long.class))).willReturn(Optional.empty());
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getWithRolesById(id);
        });

        final var expectedMessage = SEARCH_BY_ID_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.userDAO, times(1)).findOneWithRolesById(any(Long.class));
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.user.UserService#getWithRolesByUsernameIgnoreCase(java.lang.String)}.
     */
    @Test
    void testGetWithRolesByUsernameIgnoreCase()
    {
        final var optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final var user = optional.get();
        user.setId(1L);
        final var username = user.getUsername();
        BDDMockito.given(this.userDAO.findOneWithRolesByUsernameIgnoreCase(Mockito.any(String.class))).willReturn(optional);
        final var userFromDB = this.userService.getWithRolesByUsernameIgnoreCase(username);

        assertThat(userFromDB).isPresent();
        TestsDataUtils.assertAllUser(user, userFromDB.get());

        verify(this.userDAO, times(1)).findOneWithRolesByUsernameIgnoreCase(any(String.class));
    }

    @Test
    void testGetWithRolesByUsernameIgnoreCase_UpperCase()
    {
        final var optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final var user = optional.get();
        user.setId(1L);
        final var username = user.getUsername();

        BDDMockito.given(this.userDAO.findOneWithRolesByUsernameIgnoreCase(Mockito.any(String.class))).willReturn(optional);
        final var userFromDB = this.userService.getWithRolesByUsernameIgnoreCase(username.toUpperCase());

        assertThat(userFromDB).isPresent();
        TestsDataUtils.assertAllUser(user, userFromDB.get());

        verify(this.userDAO, times(1)).findOneWithRolesByUsernameIgnoreCase(any(String.class));
    }

    @Test
    void testGetWithRolesByUsernameIgnoreCase_WithEmpty()
    {
        final var username = "test";
        BDDMockito.given(this.userDAO.findOneWithRolesByUsernameIgnoreCase(Mockito.any(String.class))).willReturn(Optional.empty());
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getWithRolesByUsernameIgnoreCase(username);
        });

        final var expectedMessage = FIND_BY_USERNAME_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
        verify(this.userDAO, times(1)).findOneWithRolesByUsernameIgnoreCase(any(String.class));
    }

    @Test
    void testGetWithRolesByUsernameIgnoreCase_WithNull()
    {
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getWithRolesByUsernameIgnoreCase(null);
        });

        final var expectedMessage = FIND_BY_USERNAME_MSG;
        final var actualMessage = exception.getMessage();

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
        final var optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final var user = optional.get();
        user.setId(1L);
        final var email = user.getEmail();

        BDDMockito.given(this.userDAO.findOneWithRolesByEmailIgnoreCase(Mockito.any(String.class))).willReturn(optional);
        final var userFromDB = this.userService.getWithRolesByEmailIgnoreCase(email);

        assertThat(userFromDB).isPresent();
        TestsDataUtils.assertAllUser(user, userFromDB.get());

        verify(this.userDAO, times(1)).findOneWithRolesByEmailIgnoreCase(any(String.class));
    }

    @Test
    void testGetWithRolesByEmailIgnoreCase_UpperCase()
    {
        final var optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final User user = optional.get();
        user.setId(1L);
        final var email = user.getEmail();
        final var upper = email.toUpperCase();

        BDDMockito.given(this.userDAO.findOneWithRolesByEmailIgnoreCase(Mockito.any(String.class))).willReturn(optional);
        final var userFromDB = this.userService.getWithRolesByEmailIgnoreCase(upper);

        assertThat(userFromDB).isPresent();
        TestsDataUtils.assertAllUser(user, userFromDB.get());

        verify(this.userDAO, times(1)).findOneWithRolesByEmailIgnoreCase(any(String.class));
    }

    @Test
    void testGetWithRolesByEmailIgnoreCase_WithEmpty()
    {
        final var email = "test";
        BDDMockito.given(this.userDAO.findOneWithRolesByEmailIgnoreCase(Mockito.any(String.class))).willReturn(Optional.empty());
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getWithRolesByEmailIgnoreCase(email);
        });

        final var expectedMessage = FIND_BY_EMAIL_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
        verify(this.userDAO, times(1)).findOneWithRolesByEmailIgnoreCase(any(String.class));
    }

    @Test
    void testGetWithRolesByEmailIgnoreCase_WithNull()
    {
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userService.getWithRolesByEmailIgnoreCase(null);
        });

        final var expectedMessage = FIND_BY_EMAIL_MSG;
        final var actualMessage = exception.getMessage();

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
        final var page = new PageImpl<User>(TestsDataUtils.creerJeuDeDonnees());

        final var username = TestsDataUtils.USER_ADMIN_USERNAME;
        BDDMockito.given(this.userDAO.findByUsernameContains(any(String.class), any(Pageable.class))).willReturn(page);
        final var result = this.userService.getAllByUsernameContains(username, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isEqualTo(6); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(6); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(1); // Le nombre total de pages
        assertThat(result.getContent().size()).isEqualTo(6); // La taille du contenu

        verify(this.userDAO, times(1)).findByUsernameContains(any(String.class), any(Pageable.class));
    }

    @Test
    void testGetAllByUsernameContains_UpperCase()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        final var username = TestsDataUtils.USER_ADMIN_USERNAME;
        BDDMockito.given(this.userDAO.findByUsernameContains(any(String.class), any(Pageable.class))).willReturn(null);
        final var result = this.userService.getAllByUsernameContains(username, paging);

        assertThat(result).isNull();

        verify(this.userDAO, times(1)).findByUsernameContains(any(String.class), any(Pageable.class));
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
        final var page = new PageImpl<User>(TestsDataUtils.creerJeuDeDonnees());

        final var username = TestsDataUtils.USER_ADMIN_USERNAME;
        BDDMockito.given(this.userDAO.findAllByUsername(any(String.class), any(Pageable.class))).willReturn(page);
        final var result = this.userService.getAllByUsername(username, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isEqualTo(6); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(6); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(1); // Le nombre total de pages
        assertThat(result.getContent().size()).isEqualTo(6); // La taille du contenu

        verify(this.userDAO, times(1)).findAllByUsername(any(String.class), any(Pageable.class));
    }

    @Test
    void testGetAllByUsername_UpperCase()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        final var username = TestsDataUtils.USER_ADMIN_USERNAME;
        final var upper = username.toUpperCase();
        BDDMockito.given(this.userDAO.findAllByUsername(any(String.class), any(Pageable.class))).willReturn(null);
        final var result = this.userService.getAllByUsername(upper, paging);

        assertThat(result).isNull();

        verify(this.userDAO, times(1)).findAllByUsername(any(String.class), any(Pageable.class));
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#getUsers()}.
     */
    @Test
    void testGetUsers()
    {
        final var users = TestsDataUtils.creerJeuDeDonnees();
        when(this.userDAO.findAll()).thenReturn(users);

        final var result = (List<User>) this.userService.getUsers();

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(6);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#getAllByEnabled(java.lang.Boolean)}.
     */
    @Test
    void testGetAllByEnabled()
    {
        final var users = TestsDataUtils.creerJeuDeDonnees();
        BDDMockito.given(this.userDAO.findAllByEnabled(any(Boolean.class))).willReturn(users);
        final var result = (List<User>) this.userService.getAllByEnabled(Boolean.TRUE);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(6);

        verify(this.userDAO, times(1)).findAllByEnabled(any(Boolean.class));
    }

    @Test
    void testGetAllByEnabled_WithFalse()
    {
        BDDMockito.given(this.userDAO.findAllByEnabled(any(Boolean.class))).willReturn(Collections.emptyList());
        final var result = (List<User>) this.userService.getAllByEnabled(Boolean.FALSE);

        assertThat(result).isEmpty();
        assertThat(result.size()).isNotPositive();

        verify(this.userDAO, times(1)).findAllByEnabled(any(Boolean.class));
    }

    @Test
    void testGetAllByEnabled_WithNull()
    {
        final var result = (List<User>) this.userService.getAllByEnabled(null);

        assertThat(result).isEmpty();
        assertThat(result.size()).isNotPositive();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.user.UserService#deleteUser(java.lang.Long)}.
     */
    @Test
    void testDeleteUser()
    {
        final var optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final var user = optional.get();
        user.setId(1L);
        final var id = user.getId();
        BDDMockito.given(this.userDAO.findOneWithRolesById(any(Long.class))).willReturn(optional);
        final var userFromDB = this.userService.getWithRolesById(id);

        assertThat(userFromDB).isPresent();
        TestsDataUtils.assertAllUser(user, userFromDB.get());

        this.userService.deleteUser(id);

        verify(this.userDAO, times(2)).findOneWithRolesById(any(Long.class));
        verify(this.userDAO, times(1)).delete(any(User.class));
    }

    @Test
    void testDeleteUser_ShouldThrowException()
    {
        final var optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final var user = optional.get();
        user.setId(1L);
        final var id = user.getId();
        BDDMockito.given(this.userDAO.findOneWithRolesById(any(Long.class))).willReturn(Optional.empty());
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userService.deleteUser(id);
        });

        final var expectedMessage = SEARCH_BY_ID_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.userDAO, times(1)).findOneWithRolesById(any(Long.class));
    }

    @Test
    void testDeleteUser_ShouldThrowExceptionWithNull()
    {
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userService.deleteUser(null);
        });

        final var expectedMessage = SEARCH_BY_ID_MSG;
        final var actualMessage = exception.getMessage();

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
        final var optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final var userToUpdated = optional.get();
        userToUpdated.setId(1L);
        userToUpdated.setEmail("update.test@test.com");
        userToUpdated.setEnabled(Boolean.FALSE);

        final var id = user.getId();
        BDDMockito.given(this.userDAO.save(any(User.class))).willReturn(userToUpdated);
        BDDMockito.given(this.userDAO.findOneWithRolesById(any(Long.class))).willReturn(Optional.of(userToUpdated));

        final var userFromDB = this.userService.getWithRolesById(id);
        assertThat(userFromDB).isPresent();

        this.userService.updateUser(userFromDB.get().getId(), userToUpdated);

        assertThat(userToUpdated.getId()).isEqualTo(1L);
        assertThat(userToUpdated.getEmail()).isEqualTo("update.test@test.com");
        assertThat(userToUpdated.getEnabled()).isFalse();

        verify(this.userDAO, times(2)).findOneWithRolesById(any(Long.class));
    }

    @Test
    void testUpdateUser_ShouldThrowException()
    {
        final var optional = Optional.ofNullable(this.user);

        assertThat(optional).isPresent();
        final var userToUpdated = optional.get();
        userToUpdated.setId(1L);
        final var id = user.getId();

        BDDMockito.given(this.userDAO.save(any(User.class))).willReturn(userToUpdated);
        BDDMockito.given(this.userDAO.findOneWithRolesById(any(Long.class))).willReturn(Optional.empty());
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userService.updateUser(id, userToUpdated);
        });

        final var expectedMessage = SEARCH_BY_ID_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.userDAO, times(1)).findOneWithRolesById(any(Long.class));
    }

    @Test
    void testUpdateUser_ShouldThrowExceptionWuithNull()
    {
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userService.updateUser(null, null);
        });

        final var expectedMessage = SEARCH_BY_ID_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }
}
