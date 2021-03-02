/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : CustomUserDetailsServiceITest.java
 * Date de création : 2 mars 2021
 * Heure de création : 10:38:56
 * Package : fr.vincent.tuto.server.security
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Sets;

import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistenceContextConfig;
import fr.vincent.tuto.server.enumeration.RoleEnum;
import fr.vincent.tuto.server.model.po.User;
import fr.vincent.tuto.server.service.contract.IUserService;
import fr.vincent.tuto.server.service.user.UserService;
import fr.vincent.tuto.server.util.ServerUtil;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests d'Intégration (composant et système) des objets de type {@link CustomUserDetailsService}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-common-test.properties", "classpath:back-end-application-test.properties", "classpath:back-end-tls-test.properties" })
@ContextConfiguration(name = "customUserDetailsServiceTest", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistenceContextConfig.class,
        UserService.class, CustomUserDetailsService.class })
@SpringBootTest
@ActiveProfiles("test")
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@Sql(scripts = { "classpath:db/h2/drop-test-h2.sql", "classpath:db/h2/create-test-h2.sql", "classpath:db/h2/data-test-h2.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class CustomUserDetailsServiceITest
{
    private static final String USER_MAIL_SEARCH_MSG = "Erreur lors de la recherche des informations";

    @Autowired
    private IUserService userService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    private User user;
    private Set<RoleEnum> roles = Sets.newHashSet();

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        // Création des droits de l'utilisateur
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
        this.userDetailsService = null;
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.security.CustomUserDetailsService#loadUserByUsername(java.lang.String)}.
     */
    @Order(3)
    @Test
    void testLoadUserByUsername()
    {
        final var username = TestsDataUtils.ADMIN;

        final var userDetails = this.userDetailsService.loadUserByUsername(username);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).contains("$2a$12$");
    }

    @Order(4)
    @Test
    void testLoadUserByUsername_UpperCase()
    {
        final var username = TestsDataUtils.ADMIN;
        final var upper = ServerUtil.UPPER_CASE.apply(username);
        final var userDetails = this.userDetailsService.loadUserByUsername(upper);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).contains("$2a$12$");
    }

    @Order(5)
    @Test
    void testLoadUserByUsername_ShouldThrowException()
    {
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userDetailsService.loadUserByUsername(null);
        });

        final var expectedMessage = USER_MAIL_SEARCH_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);;
    }

    @Order(2)
    @Test
    void testLoadUserByUsername_New_User()
    {
        final var savedUser = this.userService.createUser(this.user);

        assertThat(savedUser).isNotNull();

        final var username = savedUser.getUsername();
        final var userDetails = this.userDetailsService.loadUserByUsername(username);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).contains("$2a$12$");
    }

    @Order(6)
    @Test
    void testLoadUserByUsername_WithValidUsernameEmail()
    {
        final var email = TestsDataUtils.ADMIN_EMAIL_LOWER;

        final var userDetails = this.userDetailsService.loadUserByUsername(email);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(TestsDataUtils.ADMIN);
        assertThat(userDetails.getPassword()).contains("$2a$12$");
    }

    @Order(7)
    @Test
    void testLoadUserByUsername_WithValidUsernameEmail_Uppercase()
    {
        final var email = TestsDataUtils.ADMIN_EMAIL_LOWER;
        final var upper = ServerUtil.UPPER_CASE.apply(email);
        final var userDetails = this.userDetailsService.loadUserByUsername(upper);

        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(TestsDataUtils.ADMIN);
        assertThat(userDetails.getPassword()).contains("$2a$12$");
    }

    @Order(8)
    @Test
    void testLoadUserByUsername_WithNotValidUsernameEmail()
    {
        final var email = "admin2.test.live.fr";

        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userDetailsService.loadUserByUsername(email);
        });

        final var expectedMessage = USER_MAIL_SEARCH_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Order(9)
    @Test
    void testLoadUserByUsername_WithNotExistEmail()
    {
        final var email = TestsDataUtils.USER_TWO_EMAIL;

        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userDetailsService.loadUserByUsername(email);
        });

        final var expectedMessage = USER_MAIL_SEARCH_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Order(1)
    @Test
    void testIsNotResource()
    {
        assertThat(this.userDetailsService).isNotNull();
    }
}
