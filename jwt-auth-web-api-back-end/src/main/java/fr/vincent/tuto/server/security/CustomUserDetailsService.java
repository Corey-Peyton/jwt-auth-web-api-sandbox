/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : CustomUserDetailsService.java
 * Date de création : 2 mars 2021
 * Heure de création : 02:31:22
 * Package : fr.vincent.tuto.server.security
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.security;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.server.model.po.User;
import fr.vincent.tuto.server.service.contract.IUserService;
import fr.vincent.tuto.server.util.ServerUtil;

/**
 * Service pour charger un {@link User} par nom d'utilisateur et renvoyer un objet {@link UserDetails}
 * que Spring Security utilise pour l'authentification et la validation.
 * 
 * @author Vincent Otchoun
 */
@Component(value = "userDetailsService")
public class CustomUserDetailsService implements UserDetailsService
{
    //
    private static final String MAIL_MSG_1 = "Utilisateur avec e-mail ";
    private static final String USER_MSG_NOT_FOUND = " n'a pas été trouvé dans la base de données";

    private final IUserService userService;

    /**
     * Constructeur avec en paramètre le bean pour injection.
     * 
     * @param userService le service des fonctionnalités de gestion des utilisateurs.
     */
    public CustomUserDetailsService(final IUserService userService)
    {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(final String pUsername) throws UsernameNotFoundException
    {
        // Vérifier si le nom d'utilisateur est un email
        final var isValidEmail = new EmailValidator().isValid(pUsername, null);
        if (isValidEmail)
        {
            return this.userService.getWithRolesByEmailIgnoreCase(pUsername)//
            .map(user -> ServerUtil.createSpringSecurityUser(pUsername, user))//
            .orElseThrow(() -> new CustomAppException(String.format(ServerUtil.THREE_PATTERN, MAIL_MSG_1, pUsername, USER_MSG_NOT_FOUND)));
        }

        //
        final var lowercaseUsername = ServerUtil.LOWER_CASE.apply(pUsername);

        return this.userService.getWithRolesByUsernameIgnoreCase(lowercaseUsername)//
        .map(user -> ServerUtil.createSpringSecurityUser(pUsername, user))//
        .orElseThrow(() -> new CustomAppException(String.format(ServerUtil.THREE_PATTERN, MAIL_MSG_1, lowercaseUsername, USER_MSG_NOT_FOUND)));
    }
}
