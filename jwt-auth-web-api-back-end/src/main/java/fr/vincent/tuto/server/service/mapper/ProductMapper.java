/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ProductMapper.java
 * Date de création : 7 févr. 2021
 * Heure de création : 15:25:44
 * Package : fr.vincent.tuto.server.mapper
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.mapper;

import java.util.Collection;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import fr.vincent.tuto.common.mapper.GenericObjectMapper;
import fr.vincent.tuto.server.model.dto.ProductDTO;
import fr.vincent.tuto.server.model.po.Product;

/**
 * Service de conversion/transformation d'un objet de type {@link Product} en son objet de tranfert de ses données
 * {@link ProductDTO} et vice versa.
 * 
 * @author Vincent Otchoun
 */
@Service
public class ProductMapper extends GenericObjectMapper<Product, ProductDTO> implements InitializingBean
{

    /**
     * Constructeur avec paramètres pour injection du beans en dépendances.
     * 
     * @param pModelMapper le bean de conversion des modèles selon le type.
     */
    protected ProductMapper(ModelMapper pModelMapper)
    {
        super(pModelMapper);
    }

    @Override
    public ProductDTO toDestObject(Product pProduct)
    {
        if (pProduct == null)
        {
            return null;
        }

        // Spécificité lors de l'utilisation de Lombok avec le Builder
        return this.modelMapper.map(Product.class, ProductDTO.ProductDTOBuilder.class)//
        .id(pProduct.getId())//
        .name(pProduct.getName())//
        .description(pProduct.getDescription())//
        .quantity(pProduct.getQuantity())//
        .unitPrice(pProduct.getUnitPrice())//
        .price(pProduct.getPrice())//
        .imageUrl(pProduct.getImageUrl())//
        .isActive(pProduct.getIsActive())//
        .build();
    }

    @Override
    public Product toSourceObject(ProductDTO pProductDTO)
    {
        if (pProductDTO == null)
        {
            return null;
        }

        return this.modelMapper.map(ProductDTO.class, Product.ProductBuilder.class)//
        .id(pProductDTO.getId())//
        .name(pProductDTO.getName())//
        .description(pProductDTO.getDescription())//
        .quantity(pProductDTO.getQuantity())//
        .unitPrice(pProductDTO.getUnitPrice())//
        .price(pProductDTO.getPrice())//
        .imageUrl(pProductDTO.getImageUrl())//
        .isActive(pProductDTO.getIsActive())//
        .build();
    }

    /**
     * Construire la liste des objets de transfert des données de {@link ProductDTO} à partir de la liste des
     * {@link Product}.
     * 
     * @param products la liste de produits.
     * @return la liste des objets de transfert des informations des produits.
     */
    public Collection<ProductDTO> toProductsDtos(final Collection<Product> products)
    {
        return super.toDestObjectList(products);
    }

    /**
     * Construire la liste de produits {@link Product} à partir de la liste des objets de transfert des données
     * {@link ProductDTO}.
     * 
     * @param productDTOs la liste des données des objets de transfert des produits.
     * @return la liste des données des produits en basede données.
     */
    public Collection<Product> toProducts(final Collection<ProductDTO> productDTOs)
    {
        return super.toSourceObjectList(productDTOs);
    }
}
