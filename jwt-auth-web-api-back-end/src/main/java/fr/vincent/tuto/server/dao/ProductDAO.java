/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ProductDAO.java
 * Date de création : 29 janv. 2021
 * Heure de création : 14:41:49
 * Package : fr.vincent.tuto.server.dao
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.dao;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.vincent.tuto.server.model.po.Product;

/**
 * Dépôt Spring Data JPA pour l'entité {@link Product}.
 * 
 * @author Vincent Otchoun
 */
@Repository
public interface ProductDAO extends JpaRepository<Product, Long>
{

    Optional<Product> findOneByName(final String pName);

    Optional<Product> findOneByNameIgnoreCase(final String pName);

    Boolean existsByName(final String pName);

    Page<Product> findAllByIsActive(final Boolean productIsActive, final Pageable pPageable);

    Collection<Product> findAllByIsActive(final Boolean categoryEnable);

    Collection<Product> findAllByIsActiveIsTrue();

}
