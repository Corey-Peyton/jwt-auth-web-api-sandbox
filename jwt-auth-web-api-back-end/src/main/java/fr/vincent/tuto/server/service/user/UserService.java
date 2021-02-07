/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : UserService.java
 * Date de création : 6 févr. 2021
 * Heure de création : 02:22:52
 * Package : fr.vincent.tuto.server.service.user
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.user;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.server.dao.UserDAO;
import fr.vincent.tuto.server.model.po.User;
import lombok.extern.slf4j.Slf4j;

/**
 * Service des fonctionnalités de gestion des utilisateurs du SI.
 * 
 * @author Vincent Otchoun
 */
@Service(value = "userService")
@Transactional
@Slf4j
public class UserService
{
    // Les constantes
    private static final String SAVE_MSG = "Erreur lors de la sauvegarde en base de donnnées des informations d'un nouvel utilisateur.";
    private static final String FIND_BY_USERNAME_MSG = "Erreur lors de la recherche des informations d'un utilisateur par son login.";
    private static final String FIND_BY_EMAIL_MSG = "Erreur lors de la recherche des informations d'un utilisteur par son email.";
    private static final String FIND_BY_ID_MSG = "Erreur lors de la recherche des informations d'un utilisteur et ses rôles avec son identifiant.";

    private final UserDAO userDAO;

    /**
     * Construteur avec paramètre pour l'injetion du bean des DAO dans le service.
     * 
     * @param pUserDAO le dépôt Spring Data JPA pour l'entité {@link User}
     */
    @Autowired
    public UserService(final UserDAO pUserDAO)
    {
        this.userDAO = pUserDAO;
    }

    /**
     * Enrregistrer les informations d'un nouvel utilisateur dans le système d'informations.
     * 
     * @param pUser les informations de l'utilisateur à créer.
     * @return les information de l'utilisateur créé.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public User createUser(final User pUser)
    {
        log.info("[createUser] - Engeristrer les informations d'un nouvel utilisateur dans le SI.");

        // Tentative d'enregistrement des informations d'un nouvel utilisateur.
        try
        {
            final User user = this.userDAO.save(pUser);
            Assert.notNull(user, SAVE_MSG);
            return user;
        }
        catch (Exception e)
        {
            throw new CustomAppException(e);
        }
    }

    /**
     * Obtenir les informations de l'utilisateur dans la base de données à partir de son login.
     * 
     * @param pUsername le critère de recherche (login de l'utilisateur recherché).
     * @return informations de l'utilisateur recherché si existe, sinon vide.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public Optional<User> getByUsername(String pUsername)
    {
        log.info("[getByUsername] - Obtenir les informations d'un utilisateur par son login. Login [{}]", pUsername);

        return Optional.ofNullable(this.userDAO.findOneByUsername(pUsername))//
        .filter(Optional::isPresent)//
        .orElseThrow(() -> new CustomAppException(FIND_BY_USERNAME_MSG));
    }

    /**
     * Obtenir les informations de l'utilisateur dans la base de données à partir de adresse son email en ignorant la
     * casse.
     * 
     * @param pEmail le critère de recherche(adresse email de l'utilisateur recherché).
     * @return informations de l'utilisateur recherché si existe, sinon vide.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public Optional<User> getByEmailIgnoreCase(String pEmail)
    {
        log.info("[getByEmailIgnoreCase] - Obtenir les informations d'un utilisateur par son email ignorant la casse. Email [{}]", pEmail);

        return Optional.ofNullable(this.userDAO.findOneByEmailIgnoreCase(pEmail))//
        .filter(Optional::isPresent)//
        .orElseThrow(() -> new CustomAppException(FIND_BY_EMAIL_MSG));
    }

    /**
     * Rechecher l'existence des informations de l'utilisateur dans la base de données à partir de son login.
     * 
     * @param pUsername le critère de recherche (le login de l'utilisateur recherché).
     * @return true si l'utilisateur existe, false sinon.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public Boolean getExistsByUsername(String pUsername)
    {
        log.info("[getExistsByUsername] - Indiquer l'existence d'un utilisateur par son login. Login : [{}]", pUsername);

        return this.userDAO.existsByUsername(pUsername);
    }

    /**
     * Rechecher l'existence des informations de l'utilisateur dans la base de données à partir de son adresse mail.
     * 
     * @param pEmail le critère de recherche (adresse email de l'utilisateur recherché).
     * @return true si l'utilisateur existe, false sinon.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public Boolean getExistsByEmail(String pEmail)
    {
        log.info("[getExistsByEmail] - Indiquer l'existence d'un utilisateur par son email. Email : [{}]", pEmail);

        return this.userDAO.existsByEmail(pEmail);
    }

    /**
     * Rechecher les informations de l'utilisateur dans la base de données par grappes sur les rôles à partir de son
     * identifiant technique.
     * 
     * @param pId le critère de recherche (identifiant technique de l'utilisateur recherché).
     * @return informations de l'utilisateur recherché si existe, sinon vide.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public Optional<User> getWithRolesById(Long pId)
    {
        log.info("[getWithRolesById] - Obtenir les informations d'un utilisateur avec ses rôles. Identifiant : [{}]", pId);

        return Optional.ofNullable(this.userDAO.findOneWithRolesById(pId))//
        .filter(Optional::isPresent)//
        .orElseThrow(() -> new CustomAppException(FIND_BY_ID_MSG));
    }

    /**
     * Rechecher les informations de l'utilisateur dans la base de données par grappes sur les rôles à partir de son
     * login avec mise en cache du résultat.
     * 
     * @param pUsername le critère de recherche (le login de l'utilisateur recherché).
     * @return informations de l'utilisateur recherché si existe, sinon vide.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public Optional<User> getWithRolesByUsernameIgnoreCase(String pUsername)
    {
        log.info("[getWithRolesByUsernameIgnoreCase] - Obtenir les informations d'un utilisateur avec ses rôles ingorant la casse. Login : [{}]",
        pUsername);

        return Optional.ofNullable(this.userDAO.findOneWithRolesByUsernameIgnoreCase(pUsername))//
        .filter(Optional::isPresent)//
        .orElseThrow(() -> new CustomAppException(FIND_BY_USERNAME_MSG));
    }

    /**
     * Rechecher les informations de l'utilisateur dans la base de données par grappes sur les rôles à partir de son
     * adresse mail avec mise en cache du résultat.
     * 
     * @param pEmail adresse mail de l'utilisateur recherché.
     * @return informations de l'utilisateur recherché si existe, sinon vide.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public Optional<User> getWithRolesByEmailIgnoreCase(String pEmail)
    {
        log.info("[getWithRolesByEmailIgnoreCase] - Obtenir les informations d'un utilisateur avec ses rôles ingorant la casse. Email : [{}]",
        pEmail);

        return Optional.ofNullable(this.userDAO.findOneWithRolesByEmailIgnoreCase(pEmail))//
        .filter(Optional::isPresent)//
        .orElseThrow(() -> new CustomAppException(FIND_BY_EMAIL_MSG));
    }

    /**
     * Rechercher la liste paginée des informations dans la base de données des utilisateurs à partir du login.
     * 
     * @param pUsername le critère de recherche (le login de l'utilisateur).
     * @param pPageable condition de pagination de la liste (index de la page, nombre d'éléments dans la page à
     *                  retourner).
     * @return la liste paginée des informations recherchées.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public Page<User> getAllByUsername(String pUsername, Pageable pPageable)
    {
        log.info("[getAllByUsername] - Liste paginée des informations des utilisateurs du SI ayant le login fourni. Login : [{}]", pUsername);

        return this.userDAO.findAllByUsername(pUsername, pPageable);
    }

    /**
     * Rechercher la liste paginée des informations dans la base de données des utilisateurs ayant le login spécifié.
     * 
     * @param pUsername le critère de recherche (le login de l'utilisateur).
     * @param pPageable condition de pagination de la liste (index de la page, nombre d'éléments dans la page à
     *                  retourner).
     * @return la liste paginée des informations recherchées.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public Page<User> getAllByUsernameContains(String pUsername, Pageable pPageable)
    {
        log.info("[getAllByUsernameContains] - Liste paginée des utilisateurs dont le login contient le pattern fourni. Query :[{}]", pUsername);

        return this.userDAO.findByUsernameContains(pUsername, pPageable);
    }

    /**
     * Obtenir la liste de tous les utilisateurs du système d'informations.
     * 
     * @return la liste de tous les utilisateurs enregistrés dans le système d'informations.
     */
    public Collection<User> getUsers()
    {
        log.info("[getUsers] - Obtenir la liste de tous les utilisateurs enregistrés dans le SI.");

        return this.userDAO.findAll();
    }

    /**
     * Obtenir la liste des utilisateurs selon leur état dans le sytème d'informations.
     * 
     * @return la liste des utilisateurs selon l'état spécifié.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    public Collection<User> getAllByEnabled(Boolean pEnabled)
    {
        log.info("[getAllByEnabled] - Obtenir la liste des utilisateurs selon leur état dans le SI.");

        return this.userDAO.findAllByEnabled(pEnabled);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void deleteUser(final Long pUserId)
    {
        log.info("[deleteUser] - Supprimer les informations d'un utilisateur du SI. Identifiant [{}]", pUserId);

        // Tentative de suppression des informations d'un utilisateur du SI.
        try
        {
            this.getWithRolesById(pUserId)//
            .ifPresent(this.userDAO::delete);
        }
        catch (Exception e)
        {
            throw new CustomAppException(e);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public void updateUser(final Long pUserId, final User pUser)
    {
        log.info("[updateUser] - Mise à jour des informations d'untilisateur existant. Identifiant : [{}].", pUserId);

        // Tentative des mise à jour des informations d'un utilisateur existant dans le SI.
        try
        {
            this.getWithRolesById(pUserId)//
            .ifPresent(user -> {
                final Long id = user.getId();
                pUser.setId(id);
                this.createUser(pUser);
            });
        }
        catch (Exception e)
        {
            throw new CustomAppException(e);
        }
    }
}
