/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : IProductService.java
 * Date de création : 23 févr. 2021
 * Heure de création : 03:53:20
 * Package : fr.vincent.tuto.server.service.product
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.product;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.vincent.tuto.server.model.po.Product;

/**
 * Contrat de services des fonctionnalités de gestion des informations des produits du SI.
 * 
 * @author Vincent Otchoun
 */
public interface IProductService
{
    Product createProduct(Product pProduct);

    Optional<Product> getProductById(Long pProductId);

    Optional<Product> getProductByName(String pName);

    Optional<Product> getProductByNameIgnoreCase(String pName);

    Boolean existsProductByName(String pName);

    Page<Product> getProductsByIsActive(Boolean productIsActive, Pageable pPageable);

    Collection<Product> getProductsByIsActive(Boolean productIsActive);

    Collection<Product> getProducts();

    Collection<Product> getFilteredProducts(String pQuery);

    void deleteProduct(Long pProductId);

    void updateProduct(Long pProductId, Product pProduct);
}
