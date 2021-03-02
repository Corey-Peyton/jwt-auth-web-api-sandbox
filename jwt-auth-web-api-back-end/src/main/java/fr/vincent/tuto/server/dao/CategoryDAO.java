/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : CategoryDAO.java
 * Date de création : 29 janv. 2021
 * Heure de création : 14:34:17
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

import fr.vincent.tuto.server.model.po.Category;
import fr.vincent.tuto.server.util.ServerUtil;

/**
 * Dépôt Spring Data JPA pour l'entité {@link Category}.
 * 
 * @author Vincent Otchoun
 */
@Repository
public interface CategoryDAO extends JpaRepository<Category, Long>
{

    Optional<Category> findOneByName(final String pName);

    Optional<Category> findOneByNameIgnoreCase(final String pName);

    @EntityGraph(attributePaths = ServerUtil.CATEGORY_PRODUCTS_ATTRIBUTE_PATHS)
    @Cacheable(cacheNames = ServerUtil.CATEGORY_BY_NAME_CACHE)
    Optional<Category> findOneWithProductsByNameIgnoreCase(final String pName);

    Boolean existsByName(final String pName);

    Page<Category> findAllByEnabled(final Boolean categoryEnable, final Pageable pPageable);

    Collection<Category> findAllByEnabled(final Boolean categoryEnable);

    Collection<Category> findAllByEnabledIsTrue();

}
