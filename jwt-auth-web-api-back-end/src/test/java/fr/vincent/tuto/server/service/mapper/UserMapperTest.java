/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : UserMapperTest.java
 * Date de création : 11 févr. 2021
 * Heure de création : 08:32:38
 * Package : fr.vincent.tuto.server.service.mapper
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistenceContextConfig;
import fr.vincent.tuto.server.enumeration.RoleEnum;
import fr.vincent.tuto.server.model.dto.UserDTO;
import fr.vincent.tuto.server.model.po.User;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests Unitaires des objets de type {@link UserMapper}.
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-common-test.properties", "classpath:back-end-application-test.properties", "classpath:back-end-tls-test.properties" })
@ContextConfiguration(name = "userMapperTest", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistenceContextConfig.class,
        UserMapper.class })
@SpringBootTest(webEnvironment=WebEnvironment.NONE) 
@ActiveProfiles("test")
class UserMapperTest
{
    @Autowired
    private UserMapper userMapper;
    
    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        this.userMapper.afterPropertiesSet();
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.userMapper = null;
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.mapper.UserMapper#toSourceObject(fr.vincent.tuto.server.model.dto.UserDTO)}.
     */
    @Test
    void testToSourceObjectUserDTO()
    {
        //
        final UserDTO adminDto = TestsDataUtils.createUserDTOFull(RoleEnum.ROLE_ADMIN.getAuthority(), "admin", "admin_19511982#", "admin.test@live.fr");
        final User adminUser = this.userMapper.toSourceObject(adminDto);
        
        //
        assertThat(adminUser).isNotNull();
        assertThat(adminUser.getUsername()).isEqualTo(TestsDataUtils.ADMIN);
        assertThat(adminUser.getEmail()).isEqualTo(TestsDataUtils.ADMIN_EMAIL_LOWER);
        assertThat(adminUser.getRoles().size()).isEqualTo(1);
        assertThat(adminUser.getPassword()).contains("$2a$12$");
    }
    
    @Test
    void testToSourceObjectUserDTO_ShouldReturnNull()
    {
        final User adminUser = this.userMapper.toSourceObject(null);

        assertThat(adminUser).isNull();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.mapper.UserMapper#toUsers(java.util.Collection)}.
     */
    @Test
    void testTotoUsers()
    {
        final List<UserDTO> dtos = TestsDataUtils.creerJeuDeDonneesDTO();
        final List<User> users = (List<User>) this.userMapper.toUsers(dtos);
        
        assertThat(users).isNotEmpty();
        assertThat(users.size()).isPositive();
    }
    
    @Test
    void testTotoUsers_WithEmptyList()
    {
        final List<User> users = (List<User>) this.userMapper.toUsers(Collections.emptyList());
        
        assertThat(users).isEmpty();
        assertThat(users.size()).isNotPositive();
    }
    
    @Test
    void testTotoUsers_WithNull()
    {
        final List<User> users = (List<User>) this.userMapper.toUsers(null);
        
        assertThat(users).isEmpty();
        assertThat(users.size()).isNotPositive();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.mapper.UserMapper#toDestObject(fr.vincent.tuto.server.model.po.User)}.
     */
    @Test
    void testToDestObjectUser()
    {
       final User user = TestsDataUtils.createUser(RoleEnum.ROLE_ADMIN.getAuthority(), "admin", "admin_19511982#", "admin.test@live.fr");
       final UserDTO dto = this.userMapper.toDestObject(user);
       
       //
       assertThat(dto).isNotNull();
       assertThat(dto.getUsername()).isEqualTo(TestsDataUtils.ADMIN);
       assertThat(dto.getEmail()).isEqualTo(TestsDataUtils.ADMIN_EMAIL_LOWER);
       assertThat(dto.getRoles().size()).isEqualTo(1);
       assertThat(dto.getPassword()).contains("$2a$12$");
    }
    
    @Test
    void testToDestObjectUser_ShouldReturnNull()
    {
        final UserDTO dto = this.userMapper.toDestObject(null);

        assertThat(dto).isNull();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.mapper.UserMapper#toUserDtos(java.util.Collection)}.
     */
    @Test
    void testToUserDtos()
    {
        final List<User> users = TestsDataUtils.creerJeuDeDonnees();
        final List<UserDTO> dtos = (List<UserDTO>) this.userMapper.toUserDtos(users);
        
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isPositive();
    }
    
    @Test
    void testToUserDtos_WithEmptyList()
    {
        final List<UserDTO> dtos = (List<UserDTO>) this.userMapper.toUserDtos(Collections.emptyList());
        
        assertThat(dtos).isEmpty();
        assertThat(dtos.size()).isNotPositive();
    }
    
    @Test
    void testToUserDtos_WithNull()
    {
        final List<UserDTO> dtos = (List<UserDTO>) this.userMapper.toUserDtos(null);
        
        assertThat(dtos).isEmpty();
        assertThat(dtos.size()).isNotPositive();
    }

}
