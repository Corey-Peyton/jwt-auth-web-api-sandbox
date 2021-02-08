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
package fr.vincent.tuto.server.mapper;

import java.util.Collection;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import fr.vincent.tuto.common.mapper.GenericObjectMapper;
import fr.vincent.tuto.server.model.dto.ProductDTO;
import fr.vincent.tuto.server.model.po.Product;
import lombok.extern.slf4j.Slf4j;

/**
 * Service de conversion/transformation d'un objet de type {@link Product} en son objet de tranfert de ses données
 * {@link ProductDTO} et vice versa.
 * 
 * @author Vincent Otchoun
 */
@Component
@Slf4j
public class ProductMapper extends GenericObjectMapper<Product, ProductDTO> implements InitializingBean
{

    @Override
    public ProductDTO toDestObject(Product pSourceObject)
    {
        log.info("[toDestObject] - Obtenir l'objet de transfert des données des produits (DTO).");

        if (pSourceObject == null)
        {
            return null;
        }

        // Spécificité lors de l'utilisation de Lombok avec le Builder
        return this.modelMapper.map(Product.class, ProductDTO.ProductDTOBuilder.class)//
        .id(pSourceObject.getId())//
        .name(pSourceObject.getName())//
        .description(pSourceObject.getDescription())//
        .quantity(pSourceObject.getQuantity())//
        .unitPrice(pSourceObject.getUnitPrice())//
        .price(pSourceObject.getPrice())//
        .imageUrl(pSourceObject.getImageUrl())//
        .isActive(pSourceObject.getIsActive())//
        .build();
    }


    @Override
    public Product toSourceObject(ProductDTO pDestObject)
    {
        log.info("[toSourceObject] - Obtenir les informations du produit à partir de l'objet de transfert des données (DTO).");
        
        if (pDestObject == null)
        {
            return null;
        }

        return this.modelMapper.map(ProductDTO.class, Product.ProductBuilder.class)//
        .id(pDestObject.getId())//
        .name(pDestObject.getName())//
        .description(pDestObject.getDescription())//
        .quantity(pDestObject.getQuantity())//
        .unitPrice(pDestObject.getUnitPrice())//
        .price(pDestObject.getPrice())//
        .imageUrl(pDestObject.getImageUrl())//
        .isActive(pDestObject.getIsActive())//
        .build();
    }

    /**
     * Construire la liste des objets de transfert des données de {@link ProductDTO} à partir de la liste des
     * {@link Product}.
     * 
     * @param products la liste de produits.
     * @return la liste des objets de treansfert des informations des produits.
     */
    public Collection<ProductDTO> toProductsDtos(final Collection<Product> products)
    {
        return super.toDestObjectList(products);
    }

    /**
     * Construire la liste de produits {@link Product} à partir de la liste des objets de transfert des données
     * {@link ProductDTO}.
     * 
     * @param productDTOs
     * @return
     */
    public Collection<Product> toProducts(final Collection<ProductDTO> productDTOs)
    {
        return super.toSourceObjectList(productDTOs);
    }
}
