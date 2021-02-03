/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ServerConstantsTest.java
 * Date de création : 3 févr. 2021
 * Heure de création : 17:10:31
 * Package : fr.vincent.tuto.server.constants
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.constants;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.server.enumeration.RoleEnum;
import fr.vincent.tuto.server.model.po.User;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests Unitaires des fonctions fournies par l'utilitaire {@link ServerConstants}
 * 
 * @author Vincent Otchoun
 */
class ServerConstantsTest
{
    
    private static final String USER_MSG_NOT_ACTIVATED = " est désactivé il ne peut être authentifié.";

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.constants.ServerConstants#createSpringSecurityUser(java.lang.String, fr.vincent.tuto.server.model.po.User)}.
     */
    @Test
    void testCreateSpringSecurityUser()
    {
        final Set<RoleEnum> moderateurSet = new HashSet<>();
        moderateurSet.add(RoleEnum.valueOf(RoleEnum.ROLE_USER.getAuthority()));
        moderateurSet.add(RoleEnum.valueOf(RoleEnum.ROLE_MODERATOR.getAuthority()));
        
        final User moderateur =TestsDataUtils.createUserWithSet(moderateurSet, "moderateur", "moderateur_19511982#", "moderateur.test@live.fr");
        moderateur.setEnabled(Boolean.TRUE); 
        
        final org.springframework.security.core.userdetails.User user = ServerConstants.createSpringSecurityUser("moderateur", moderateur);
        
        assertThat(user).isNotNull();
        assertThat(user.getPassword()).contains("$2a$12$"); 
        assertThat(user.getUsername()).contains("moderateur"); 
        assertThat(user.isEnabled()).isTrue(); 
    }
    
    @Test
    void testCreateSpringSecurityUser_Inactive()
    {
        final Set<RoleEnum> moderateurSet = new HashSet<>();
        moderateurSet.add(RoleEnum.valueOf(RoleEnum.ROLE_USER.getAuthority()));
        moderateurSet.add(RoleEnum.valueOf(RoleEnum.ROLE_MODERATOR.getAuthority()));
        
        final User moderateur =TestsDataUtils.createUserWithSet(moderateurSet, "moderateur", "moderateur_19511982#", "moderateur.test@live.fr");
        moderateur.setEnabled(Boolean.FALSE); 

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            ServerConstants.createSpringSecurityUser("moderateur", moderateur);
        });

        final String expectedMessage = USER_MSG_NOT_ACTIVATED;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.constants.ServerConstants#isQueryMatch(java.lang.String, java.lang.String)}.
     */
    @Test
    void testIsQueryMatch()
    {
        final String SOURCE = "PHILIPS L1478ZERER";
        final String QUERY = "philips";
        final boolean isMatch = ServerConstants.isQueryMatch(SOURCE, QUERY);
        
        assertThat(isMatch).isTrue();
    }
    
    @Test
    void testIsQueryMatch_LowerCase()
    {
        final String SOURCE = "PHILIPS L1478ZERER";
        final String QUERY = "PHILIPS";
        final boolean isMatch = ServerConstants.isQueryMatch(SOURCE, QUERY);
        
        assertThat(isMatch).isTrue();
    }
    
    @Test
    void testIsQueryMatch_WithNull()
    {
        final boolean isMatch = ServerConstants.isQueryMatch(null, null);
        
        assertThat(isMatch).isFalse();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.constants.ServerConstants#queryIgnorecase(java.lang.String, java.lang.String)}.
     */
    @Test
    void testQueryIgnorecase()
    {
        final String SOURCE = "PHILIPS L1478ZERER";
        final String QUERY = "philips";
        final boolean isMatch = ServerConstants.queryIgnorecase(SOURCE, QUERY);
        
        assertThat(isMatch).isTrue();
    }
    
    @Test
    void testQueryIgnorecase_WithNull()
    {
        final boolean isMatch = ServerConstants.queryIgnorecase(null, null);
        
        assertThat(isMatch).isFalse();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.constants.ServerConstants#strCaseInsentitive(java.lang.String, java.lang.String)}.
     */
    @Test
    void testStrCaseInsentitive()
    {
        final String SOURCE = "PHILIPS L1478ZERER";
        final String QUERY = "philips";
        final boolean isMatch = ServerConstants.strCaseInsentitive(SOURCE, QUERY);
        
        assertThat(isMatch).isTrue();
    }
    
    @Test
    void testStrCaseInsentitive_WithNull()
    {
        final boolean isMatch = ServerConstants.strCaseInsentitive(null, null);
        
        assertThat(isMatch).isFalse();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.constants.ServerConstants#convertSetToList(java.util.Set)}.
     */
    @Test
    void testConvertSetToList()
    {
        final Set<String> moderateurSet = new HashSet<>();
        moderateurSet.add(RoleEnum.ROLE_ADMIN.getAuthority());
        moderateurSet.add(RoleEnum.ROLE_MODERATOR.getAuthority());
        moderateurSet.add(RoleEnum.ROLE_USER.getAuthority());
        
        final List<String> roles = ServerConstants.convertSetToList(moderateurSet);
        
        assertThat(roles).isNotEmpty(); 
        assertThat(roles.size()).isEqualTo(3);  
    }
    
    @Test
    void testConvertSetToList_()
    {
        final List<String> roles = ServerConstants.convertSetToList(null);
        
        assertThat(roles).isEmpty(); 
    }

}
