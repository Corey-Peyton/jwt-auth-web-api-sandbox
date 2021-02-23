/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : CategoryService.java
 * Date de création : 30 janv. 2021
 * Heure de création : 18:17:22
 * Package : fr.vincent.tuto.server.service.product.impl
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.product;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.server.constants.ServerConstants;
import fr.vincent.tuto.server.dao.CategoryDAO;
import fr.vincent.tuto.server.model.po.Category;
import fr.vincent.tuto.server.model.po.Product;

/**
 * Service des fonctionnalités de gestion des catégories de produits dans le SI.
 * 
 * @author Vincent Otchoun
 */
@Service(value = "categoryService")
@Transactional
public class CategoryService implements ICategoryService
{
    //
    private static final String SAVE_MESSAGE = "Erreur lors de la sauvegarde en base de donnnées des informations d'une catégorie de produits.";
    private static final String FIND_BY_ID_MESSAGE = "Erreur recherche des informations d'une catégorie de produits par identifiant.";
    private static final String FIND_BY_NAME_MESSAGE = "Erreur recherche des informations d'une catégorie de produits par son nom.";

    private final CategoryDAO categoryDAO;
    private final ProductService productService;

    /**
     * Constructuer avec injection des beans d'accès à la base de données.
     * 
     * @param pCategoryDAO    le dépôt Spring Data JPA pour l'entité {@link Category}.
     * @param pProductService le service de gestion de l'entité {@link Product}.
     */
    @Autowired
    public CategoryService(final CategoryDAO pCategoryDAO, final ProductService pProductService)
    {
        this.categoryDAO = pCategoryDAO;
        this.productService = pProductService;
    }

    /**
     * Enregistrer une catégorie de produits dans la base de données.
     * 
     * @param pCategory la catégorie de produit à enregistrer.
     * @return la catégorie de produit enregistrée.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    @Override
    public Category createCategory(Category pCategory)
    {
        try
        {
            final Category category = this.categoryDAO.save(pCategory);
            Assert.notNull(category, SAVE_MESSAGE);
            return category;
        }
        catch (Exception e)
        {
            throw new CustomAppException(SAVE_MESSAGE, e);
        }
    }

    /**
     * Obtenir une catégorie de produits par son identifiant.
     * 
     * @param pCategoryId identifiant de la catégorie recherchée.
     * @return la catégorie de produit ayant l'dentifiant recherche.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER')")
    @Override
    public Optional<Category> getCategoryById(Long pCategoryId)
    {
        return Optional.ofNullable(this.categoryDAO.findById(pCategoryId))//
        .filter(Optional::isPresent)//
        .orElseThrow(() -> new CustomAppException(FIND_BY_ID_MESSAGE));//
    }

    /**
     * Obtenir une catégorie de produits par son nom.
     * 
     * @param pName le nom de la catégorie de produit recherchée.
     * @return la catégorie de produit recherchée si trouvé, sinon vide.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER')")
    @Override
    public Optional<Category> getCategoryByName(String pName)
    {
        return Optional.ofNullable(this.categoryDAO.findOneByName(pName))//
        .filter(Optional::isPresent)//
        .orElseThrow(() -> new CustomAppException(FIND_BY_NAME_MESSAGE));
    }

    /**
     * Obtenir une catégorie de produits par son nom en ignorant la casse.
     * 
     * @param pName le nom de la catégorie de produit recherchée.
     * @return la catégorie de produit recherchée si trouvé, sinon vide.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER')")
    @Override
    public Optional<Category> getCategoryByNameIgnoreCase(String pName)
    {
        return Optional.ofNullable(this.categoryDAO.findOneByNameIgnoreCase(pName))//
        .filter(Optional::isPresent)//
        .orElseThrow(() -> new CustomAppException(FIND_BY_NAME_MESSAGE));
    }

    /**
     * Indiquer l'existence de la catégorie de produits par son nom.
     * 
     * @param pName le nom de la catégorie de produits.
     * @return true si la catégorie de produits existe, false sinon.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER')")
    @Override
    public Boolean existsCategoryByName(String pName)
    {
        return this.categoryDAO.existsByName(pName);
    }

    /**
     * Obtenir une liste paginée de catégories de produits selon l'état en base de données (actif ou non).
     * 
     * @param pCategoryEnable état des catégories de produits à remonter.
     * @param pPageable       pagination de la liste (index de la page, nombre d'éléments dans la page à retourner).
     * @return la liste paginée des catégories de produits correspondant.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER')")
    @Override
    public Page<Category> getCategoriesByEnabled(Boolean pCategoryEnable, Pageable pPageable)
    {
        return this.categoryDAO.findAllByEnabled(pCategoryEnable, pPageable);
    }

    /**
     * Obtenir une liste de catégories de produits selon l'état en base de données (actif ou non).
     * 
     * @param pCategoryEnable état des catégories de produits à remonter.
     * @return la liste de catégories de produits correspondant aux critères de recherche.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER')")
    @Override
    public Collection<Category> getCategoriesByEnabled(Boolean pCategoryEnable)
    {
        return this.categoryDAO.findAllByEnabled(pCategoryEnable);
    }

    /**
     * Obtenir l'ensemble de catégories de produits en base de données.
     * 
     * @return la liste des catégories de produits.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER')")
    @Override
    public Collection<Category> getCategories()
    {
        return this.categoryDAO.findAll();
    }

    /**
     * Obtenez une liste de catégories de produits filtrée avec un nom de produit correspondant à la requête donnée.
     * 
     * @param pQuery le modèle de requête donné.
     * @return la liste filtrée de caagories de produits.
     */
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN','ROLE_MODERATOR','ROLE_USER')")
    @Override
    public Collection<Category> getFilteredCategoriesByProductName(String pQuery)
    {
        final List<Category> categories = (List<Category>) this.getCategories();

        // Filtrée la liste des catégories de produits en pure Java.
        return Optional.ofNullable(categories.stream()//
        .filter(categorie -> categorie.getProducts().stream()//
        .map(Product::getName)//
        .filter(Objects::nonNull)//
        .anyMatch(name -> ServerConstants.strCaseInsentitive(name, pQuery))//
        )//
        .distinct()//
        .collect(Collectors.toList())//
        ).orElseGet(Collections::emptyList);
    }

    /**
     * Mettre à jour les informations d'une catégorie de produits à partir de son identifiant.
     * 
     * @param pCategoryId identifiant de la catégorie de produits à mettre à jour.
     * @param pCategory   la catégorie de produits à mettre à jour.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    @Override
    public void updateCategory(Long pCategoryId, Category pCategory)
    {
        try
        {
            this.getCategoryById(pCategoryId).ifPresent(categorie -> {
                final Long id = categorie.getId();
                pCategory.setId(id);
                this.createCategory(pCategory);
            });
        }
        catch (Exception e)
        {
            throw new CustomAppException(e);
        }
    }

    /**
     * Supprimer les informations d'une catégorie de produits de la base de données.
     * 
     * @param pCategoryId identifiant de la catégorie de produits à supprimer.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    @Override
    public void deleteCategory(Long pCategoryId)
    {
        try
        {
            this.getCategoryById(pCategoryId)//
            .ifPresent(this.categoryDAO::delete);
        }
        catch (Exception e)
        {
            throw new CustomAppException(e);
        }
    }

    /**
     * Ajouter un nouveau produit à une catégorie.
     * 
     * @param pCategoryId identifiant de la catégorie de produit à mettre à jour.
     * @param pProductId  identifiant du nouveau produit à ajouter.
     * @return la liste de produits mise à jour.
     */
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
    @Override
    public Collection<Product> addProduct(Long pCategoryId, Long pProductId)
    {
        //
        final Optional<Category> categorieOptional = this.getCategoryById(pCategoryId);
        final Optional<Product> productOptional = this.productService.getProductById(pProductId);

        // Si recherche infructueuse
        if (categorieOptional.isEmpty() || productOptional.isEmpty())
        {
            return Collections.emptyList();
        }

        final Category categorie = categorieOptional.get();
        final Product product = productOptional.get();

        final Set<Product> products = categorie.getProducts();
        products.add(product);
        categorie.setProducts(products);
        this.categoryDAO.save(categorie);

        return ServerConstants.setToList(products);
    }
}
