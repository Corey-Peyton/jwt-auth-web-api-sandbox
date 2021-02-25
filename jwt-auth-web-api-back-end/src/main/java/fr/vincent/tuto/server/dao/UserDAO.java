/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : UserDAO.java
 * Date de création : 29 janv. 2021
 * Heure de création : 14:33:06
 * Package : fr.vincent.tuto.server.dao
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.dao;

import java.util.Collection;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.vincent.tuto.server.model.po.User;
import fr.vincent.tuto.server.util.ServerUtil;

/**
 * Dépôt Spring Data JPA pour l'entité {@link User}.
 * 
 * @author Vincent Otchoun
 */
@Repository
public interface UserDAO extends JpaRepository<User, Long>
{
    /**
     * Obtenir les informations de l'utilisateur dans la base de données à partir de son login.
     * 
     * @param pUsername le critère de recherche (login de l'utilisateur recherché).
     * @return informations de l'utilisateur recherché si existe, sinon vide.
     */
    Optional<User> findOneByUsername(final String pUsername);

    /**
     * Obtenir les informations de l'utilisateur dans la base de données à partir de adresse son email en ignorant la
     * casse.
     * 
     * @param pEmail le critère de recherche(adresse email de l'utilisateur recherché).
     * @return informations de l'utilisateur recherché si existe, sinon vide.
     */
    Optional<User> findOneByEmailIgnoreCase(final String pEmail);

    /**
     * Rechecher l'existence des informations de l'utilisateur dans la base de données à partir de son login.
     * 
     * @param pUsername le critère de recherche (le login de l'utilisateur recherché).
     * @return true si l'utilisateur existe, false sinon.
     */
    Boolean existsByUsername(final String pUsername);

    /**
     * Rechecher l'existence des informations de l'utilisateur dans la base de données à partir de son adresse mail.
     * 
     * @param pEmail le critère de recherche (adresse email de l'utilisateur recherché).
     * @return true si l'utilisateur existe, false sinon.
     */
    Boolean existsByEmail(final String pEmail);

    /**
     * Rechecher les informations de l'utilisateur dans la base de données par grappes sur les rôles à partir de son
     * identifiant technique.
     * 
     * @param pId le critère de recherche (identifiant technique de l'utilisateur recherché).
     * @return informations de l'utilisateur recherché si existe, sinon vide.
     */
    @EntityGraph(attributePaths = ServerUtil.ATTRIBUTE_PATHS)
    Optional<User> findOneWithRolesById(final Long pId);

    /**
     * Rechecher les informations de l'utilisateur dans la base de données par grappes sur les rôles à partir de son
     * login avec mise en cache du résultat.
     * 
     * @param pUsername le critère de recherche (le login de l'utilisateur recherché).
     * @return informations de l'utilisateur recherché si existe, sinon vide.
     */
    @EntityGraph(attributePaths = ServerUtil.ATTRIBUTE_PATHS)
    @Cacheable(cacheNames = ServerUtil.USERS_BY_USERNAME_CACHE)
    Optional<User> findOneWithRolesByUsernameIgnoreCase(final String pUsername);

    /**
     * Rechecher les informations de l'utilisateur dans la base de données par grappes sur les rôles à partir de son
     * adresse mail avec mise en cache du résultat.
     * 
     * @param pEmail adresse mail de l'utilisateur recherché.
     * @return informations de l'utilisateur recherché si existe, sinon vide.
     */
    @EntityGraph(attributePaths = ServerUtil.ATTRIBUTE_PATHS)
    @Cacheable(cacheNames = ServerUtil.USERS_BY_EMAIL_CACHE)
    Optional<User> findOneWithRolesByEmailIgnoreCase(final String pEmail);

    /**
     * Rechercher la liste paginée des informations dans la base de données des utilisateurs à partir du login.
     * 
     * @param pUsername le critère de recherche (le login de l'utilisateur).
     * @param pPageable condition de pagination de la liste (index de la page, nombre d'éléments dans la page à
     *                  retourner).
     * @return la liste paginée des informations recherchées.
     */
    Page<User> findAllByUsername(final String pUsername, final Pageable pPageable);

    /**
     * Rechercher la liste paginée des informations dans la base de données des utilisateurs ayant le login spécifié.
     * 
     * @param pUsername le critère de recherche (le login de l'utilisateur).
     * @param pPageable condition de pagination de la liste (index de la page, nombre d'éléments dans la page à
     *                  retourner).
     * @return la liste paginée des informations recherchées.
     */
    Page<User> findByUsernameContains(final String pUsername, Pageable pPageable);

    /**
     * Obtenir la liste des utilisateurs par login.
     * 
     * @param pUsername le login de l'utilisateur.
     * @return la liste des utilisateurs.
     */
    Collection<User> findAllByUsername(final String pUsername);

    /**
     * Obtenir la liste des utilisateurs selon leur état dans le sytème d'informations.
     * 
     * @return la liste des utilisateurs selon l'état spécifié.
     */
    Collection<User> findAllByEnabled(final Boolean pEnabled);
}
