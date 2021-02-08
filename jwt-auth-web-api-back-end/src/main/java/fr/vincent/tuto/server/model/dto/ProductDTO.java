/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ProductDTO.java
 * Date de création : 7 févr. 2021
 * Heure de création : 13:56:16
 * Package : fr.vincent.tuto.server.model.dto
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.vincent.tuto.server.constants.ServerConstants;
import fr.vincent.tuto.server.model.po.Product;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Classe d'implementation de l'objet de transfert des données (DTO) de {@link Product}.
 * 
 * @author Vincent Otchoun
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE) //Hides the constructor to force usage of the Builder
@Builder
@JsonInclude(content = JsonInclude.Include.NON_NULL, value = Include.NON_EMPTY)
@JsonPropertyOrder({ "id", "name", "description", "quantity","unitPrice","price","isActive","imageUrl" })
@ApiModel(description = "Objet de transfert des informations des produits", value = "Données Produit")
public class ProductDTO implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -686811623874663501L;

    //
    @ApiModelProperty(name = "id", dataType = "java.lang.Long", value = "Identifiant technique auto-généré.", position = 0)
    private Long id; // identifiant technique auto-généré de l'objet en base.

    @NotNull(message = ServerConstants.PRODUCT_NAME)
    @NotEmpty(message = ServerConstants.PRODUCT_NAME)
    @NotBlank(message = ServerConstants.PRODUCT_NAME)
    @ApiModelProperty(name = "name", dataType = "java.lang.String", value = "Le nom du produit.", required = true, position = 1)
    private String name; // le nom du produit.

    @NotNull(message = ServerConstants.PRODUCT_DESC)
    @NotEmpty(message = ServerConstants.PRODUCT_DESC)
    @NotBlank(message = ServerConstants.PRODUCT_DESC)
    @ApiModelProperty(name = "description", dataType = "java.lang.String", value = "La description du produit.", required = true, position = 2)
    private String description; // la description du produit.

    @NotNull(message = ServerConstants.PRODUCT_QTY)
    @NotEmpty(message = ServerConstants.PRODUCT_QTY)
    @NotBlank(message = ServerConstants.PRODUCT_QTY)
    @ApiModelProperty(name = "quantity", dataType = "java.lang.Long", value = "La quantité du produit.", required = true, position = 3)
    private Long quantity; // la quantité en stock pour le produit.

    @NotNull(message = ServerConstants.PRODUCT_UNIT)
    @NotEmpty(message = ServerConstants.PRODUCT_UNIT)
    @NotBlank(message = ServerConstants.PRODUCT_UNIT)
    @ApiModelProperty(name = "unitPrice", dataType = "java.math.BigDecimal", value = "Le prix unitaire du produit.", required = true, position = 4)
    private BigDecimal unitPrice; // le prix unitaire du produit.

    @NotNull(message = ServerConstants.PRODUCT_PRICE)
    @NotEmpty(message = ServerConstants.PRODUCT_PRICE)
    @NotBlank(message = ServerConstants.PRODUCT_PRICE)
    @ApiModelProperty(name = "price", dataType = "java.math.BigDecimal", value = "Le prix total de la quantité commandée du produit.", required = true, position = 5)
    private BigDecimal price; // le prix du produit.

    @NotNull(message = ServerConstants.PRODUCT_ACTIVE)
    @NotEmpty(message = ServerConstants.PRODUCT_ACTIVE)
    @NotBlank(message = ServerConstants.PRODUCT_ACTIVE)
    @ApiModelProperty(name = "isActive", dataType = "java.lang.Boolean", value = "Indique si le produit est disponible ou non.", required = true, position = 6)
    private Boolean isActive; // indique si le produit est actif/disponible ou non

    @NotNull(message = ServerConstants.PRODUCT_IMG)
    @NotEmpty(message = ServerConstants.PRODUCT_IMG)
    @NotBlank(message = ServerConstants.PRODUCT_IMG)
    @ApiModelProperty(name = "imageUrl", dataType = "java.lang.String", value = "Le lien de l'image du produit.", required = true, position = 7)
    private String imageUrl;

     @Override
     public String toString()
     {
     return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
     }
}
