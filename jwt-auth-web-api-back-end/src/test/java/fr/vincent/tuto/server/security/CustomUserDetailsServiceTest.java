/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : CustomUserDetailsServiceTest.java
 * Date de création : 2 mars 2021
 * Heure de création : 03:15:34
 * Package : fr.vincent.tuto.server.security
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Sets;

import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistenceContextConfig;
import fr.vincent.tuto.server.enumeration.RoleEnum;
import fr.vincent.tuto.server.model.po.User;
import fr.vincent.tuto.server.service.contract.IUserService;
import fr.vincent.tuto.server.util.ServerUtil;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests Unitaires des objets de type {@link CustomUserDetailsService}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-common-test.properties", "classpath:back-end-application-test.properties", "classpath:back-end-tls-test.properties" })
@ContextConfiguration(name = "customUserDetailsServiceTest", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistenceContextConfig.class,
        CustomUserDetailsService.class })
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("test")
class CustomUserDetailsServiceTest
{
    private static final String USER_MAIL_NULL = "Utilisateur avec e-mail";

    @MockBean
    private IUserService userService;

    private CustomUserDetailsService userDetailsService;

    private User user;
    private Set<RoleEnum> roles = Sets.newHashSet();

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        this.userDetailsService = new CustomUserDetailsService(this.userService);

        // Création des droits de l'utilisateur
        this.roles.add(RoleEnum.ROLE_ADMIN);
        this.roles.add(RoleEnum.ROLE_USER);
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
    @Test
    void testLoadUserByUsername()
    {
        final var optionalUser = Optional.ofNullable(this.user);
        final var username = this.user.getUsername();

        BDDMockito.given(this.userService.getWithRolesByUsernameIgnoreCase(Mockito.any(String.class))).willReturn(optionalUser);

        final UserDetails userFromDB = this.userDetailsService.loadUserByUsername(username);

        assertThat(userFromDB).isNotNull();
        assertThat(userFromDB.getUsername()).isEqualTo(this.user.getUsername());
        assertThat(userFromDB.getPassword()).isEqualTo(this.user.getPassword());

        verify(this.userService, times(1)).getWithRolesByUsernameIgnoreCase(any(String.class));
    }

    @Test
    void testLoadUserByUsername_UpperCase()
    {
        final var optionalUser = Optional.ofNullable(this.user);
        final var username = this.user.getUsername();
        final var upper = ServerUtil.UPPER_CASE.apply(username);

        BDDMockito.given(this.userService.getWithRolesByUsernameIgnoreCase(Mockito.any(String.class))).willReturn(optionalUser);

        final UserDetails userFromDB = this.userDetailsService.loadUserByUsername(upper);

        assertThat(userFromDB).isNotNull();
        assertThat(userFromDB.getUsername()).isEqualTo(this.user.getUsername());
        assertThat(userFromDB.getPassword()).isEqualTo(this.user.getPassword());

        verify(this.userService, times(1)).getWithRolesByUsernameIgnoreCase(any(String.class));
    }

    @Test
    void testLoadUserByUsername_WithNull_ShouldThrowException()
    {

        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userDetailsService.loadUserByUsername(null);
        });

        final var expectedMessage = USER_MAIL_NULL;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testLoadUserByUsername_WithNotExistEmail()
    {
        final var email = TestsDataUtils.USER_TWO_EMAIL;

        BDDMockito.given(this.userService.getWithRolesByEmailIgnoreCase(Mockito.any(String.class))).willReturn(Optional.empty());

        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userDetailsService.loadUserByUsername(email);
        });

        final var expectedMessage = USER_MAIL_NULL;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.userService, times(1)).getWithRolesByEmailIgnoreCase(any(String.class));
    }

    @Test
    void testLoadUserByUsername_WithValidUsernameEmail()
    {
        final var email = TestsDataUtils.USER_TWO_EMAIL;
        final var optionalUser = Optional.ofNullable(this.user);
        BDDMockito.given(this.userService.getWithRolesByEmailIgnoreCase(Mockito.any(String.class))).willReturn(optionalUser);

        final UserDetails userFromDB = this.userDetailsService.loadUserByUsername(email);

        assertThat(userFromDB).isNotNull();
        assertThat(userFromDB.getUsername()).isEqualTo(this.user.getUsername());
        assertThat(userFromDB.getPassword()).isEqualTo(this.user.getPassword());

        verify(this.userService, times(1)).getWithRolesByEmailIgnoreCase(any(String.class));
    }

    @Test
    void testLoadUserByUsername_WithNotValidUsernameEmail()
    {
        final var email = "admin2.test.live.fr";
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.userDetailsService.loadUserByUsername(email);
        });

        final var expectedMessage = USER_MAIL_NULL;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.userService, times(1)).getWithRolesByUsernameIgnoreCase(any(String.class));
    }

    @Test
    void testIsNotNullResource()
    {
        assertThat(this.userDetailsService).isNotNull();
    }
}
