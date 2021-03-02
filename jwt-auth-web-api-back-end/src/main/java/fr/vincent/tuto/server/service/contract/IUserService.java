/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : IUserService.java
 * Date de création : 23 févr. 2021
 * Heure de création : 03:43:13
 * Package : fr.vincent.tuto.server.service.user
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.contract;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.vincent.tuto.server.model.po.User;

/**
 * Contrat des services des fonctionnalités de gestion des utilisateurs du SI.
 * 
 * @author Vincent Otchoun
 */
public interface IUserService
{
    User createUser(final User pUser);

    Optional<User> getByUsername(String pUsername);

    Optional<User> getByEmailIgnoreCase(String pEmail);

    Boolean getExistsByUsername(String pUsername);

    Boolean getExistsByEmail(String pEmail);

    Optional<User> getWithRolesById(Long pId);

    Optional<User> getWithRolesByUsernameIgnoreCase(String pUsername);

    Optional<User> getWithRolesByEmailIgnoreCase(String pEmail);

    Page<User> getAllByUsername(String pUsername, Pageable pPageable);

    Page<User> getAllByUsernameContains(String pUsername, Pageable pPageable);

    Collection<User> getUsers();

    Collection<User> getAllByEnabled(Boolean pEnabled);

    void deleteUser(final Long pUserId);

    void updateUser(final Long pUserId, final User pUser);

}
