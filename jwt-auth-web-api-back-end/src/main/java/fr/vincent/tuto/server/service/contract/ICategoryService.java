/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ICategoryService.java
 * Date de création : 23 févr. 2021
 * Heure de création : 06:54:00
 * Package : fr.vincent.tuto.server.service.product
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.contract;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.vincent.tuto.server.model.po.Category;
import fr.vincent.tuto.server.model.po.Product;

/**
 * Contrat de services des fonctionnalités de gestion des catégories de produits dans le SI.
 * 
 * @author Vincent Otchoun
 */
public interface ICategoryService
{
    Category createCategory(Category pCategory);

    Optional<Category> getCategoryById(Long pCategoryId);

    Optional<Category> getCategoryByName(String pName);

    Optional<Category> getCategoryByNameIgnoreCase(String pName);

    Optional<Category> getCategoryWithProductsByNameIgnoreCase(String pName);

    Boolean existsCategoryByName(String pName);

    Page<Category> getCategoriesByEnabled(Boolean pCategoryEnable, Pageable pPageable);

    Collection<Category> getCategoriesByEnabled(Boolean pCategoryEnable);

    Collection<Category> getCategories();

    Collection<Category> getFilteredCategoriesByProductName(String pQuery);

    void updateCategory(Long pCategoryId, Category pCategory);

    void deleteCategory(Long pCategoryId);

    Collection<Product> addProduct(Long pCategoryId, Long pProductId);
}
