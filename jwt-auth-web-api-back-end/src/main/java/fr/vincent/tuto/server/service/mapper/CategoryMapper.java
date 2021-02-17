/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : CategoryMapper.java
 * Date de création : 8 févr. 2021
 * Heure de création : 13:34:20
 * Package : fr.vincent.tuto.server.mapper
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.mapper;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.vincent.tuto.common.mapper.GenericObjectMapper;
import fr.vincent.tuto.server.constants.ServerConstants;
import fr.vincent.tuto.server.enumeration.CategoryTypeEnum;
import fr.vincent.tuto.server.model.dto.CategoryDTO;
import fr.vincent.tuto.server.model.dto.ProductDTO;
import fr.vincent.tuto.server.model.po.Category;
import fr.vincent.tuto.server.model.po.Product;
import lombok.extern.slf4j.Slf4j;

/**
 * Service de conversion/transformation d'un objet de type {@link Category} en son objet de tranfert de ses données
 * {@link CategoryDTO} et vice versa.
 * 
 * @author Vincent Otchoun
 */
@Service
@Slf4j
public class CategoryMapper extends GenericObjectMapper<Category, CategoryDTO>
{
    private final ProductMapper productMapper;

    /**
     * Constructeur avec paramètres pour injection des beans en dépendances.
     * 
     * @param pProductMapper le bean de conversion des objets relatifs aux produits.
     * @param pModelMapper   le bean de conversion des modèles selon le type.
     */
    @Autowired
    public CategoryMapper(final ProductMapper pProductMapper, final ModelMapper pModelMapper)
    {
        super(pModelMapper);
        this.productMapper = pProductMapper;
    }

    @Override
    public Category toSourceObject(CategoryDTO pCategoryDTO)
    {
        log.info("[toSourceObject] - Obtenir les données de la catégorie de produits avec son DTO.");

        if (pCategoryDTO == null)
        {
            return null;
        }

        // Traitement des produits rattachés à la catégorie à persister en base de données.
        final Set<ProductDTO> productDTOs = pCategoryDTO.getProducts();
        final List<Product> productsList = (List<Product>) this.productMapper.toProducts(productDTOs);
        final Set<Product> productsSet = ServerConstants.listToSet(productsList);

        // On retourne l'instance
        return this.modelMapper.map(CategoryDTO.class, Category.CategoryBuilder.class)//
        .id(pCategoryDTO.getId())//
        .name(pCategoryDTO.getName())//
        .description(pCategoryDTO.getDescription())//
        .enabled(pCategoryDTO.getEnabled())//
        .products(productsSet)//
        .categoryType(CategoryTypeEnum.valueOf(pCategoryDTO.getType()))//
        .build();
    }

    /**
     * Construire la liste de produits {@link Category} à partir de la liste des objets de transfert des données
     * {@link CategoryDTO}.
     * 
     * @param categoryDTOs la liste des objets de transfert des informations des catégories de produits.
     * @return la liste des données des catégories de produits en base de de données.
     */
    public Collection<Category> toCategories(final Collection<CategoryDTO> categoryDTOs)
    {
        return super.toSourceObjectList(categoryDTOs);
    }

    @Override
    public CategoryDTO toDestObject(Category pCategory)
    {
        log.info("[toDestObject] - Obtenir les données de l'objet de transfert des catégories (DTO).");

        if (pCategory == null)
        {
            return null;
        }

        // Traitement des produits rattachés à la catégorie en provenance de la base de données.
        final Set<Product> productsSet = pCategory.getProducts();
        final List<ProductDTO> listDtos = (List<ProductDTO>) this.productMapper.toProductsDtos(productsSet);
        final Set<ProductDTO> productDTOs = ServerConstants.listToSet(listDtos);

        // On retourne l'instance
        return this.modelMapper.map(Category.class, CategoryDTO.CategoryDTOBuilder.class)//
        .id(pCategory.getId())//
        .name(pCategory.getName())//
        .description(pCategory.getDescription())//
        .enabled(pCategory.getEnabled())//
        .products(productDTOs)//
        .type(pCategory.getCategoryType().name())//
        .build();
    }

    /**
     * Construire la liste des objets de transfert des données de {@link CategoryDTO} à partir de la liste des
     * {@link Category}.
     * 
     * @param categories la liste des données des catégories de produits en base de de données.
     * @return la liste des objets de transfert des informations des catégories de produits.
     */
    public Collection<CategoryDTO> toCategoryDtos(final Collection<Category> categories)
    {
        return super.toDestObjectList(categories);
    }

}
