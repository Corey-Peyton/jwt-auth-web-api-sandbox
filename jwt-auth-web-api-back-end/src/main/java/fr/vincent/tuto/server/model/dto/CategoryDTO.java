/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : CategoryDTO.java
 * Date de création : 8 févr. 2021
 * Heure de création : 12:21:31
 * Package : fr.vincent.tuto.server.model.dto
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.model.dto;

import java.io.Serializable;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import fr.vincent.tuto.server.model.po.Category;
import fr.vincent.tuto.server.util.ServerUtil;
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
 * Classe d'implementation de l'objet de transfert des données (DTO) de {@link Category}.
 * 
 * @author Vincent Otchoun
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Hides the constructor to force usage of the Builder
@Builder
@JsonInclude(content = JsonInclude.Include.NON_NULL, value = Include.NON_EMPTY)
@JsonPropertyOrder({ "id", "name", "description", "enabled", "type", "products" })
@ApiModel(description = "Objet de transfert des informations des catégories de produits", value = "Données Catégorie de produits")
public class CategoryDTO implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -3548966061537228635L;

    @ApiModelProperty(name = "id", dataType = "java.lang.Long", value = "Identifiant technique auto-généré.", position = 0)
    private Long id; // identifiant technique auto-généré de l'objet en base.

    @NotNull(message = ServerUtil.CATEGORY_NAME)
    @NotEmpty(message = ServerUtil.CATEGORY_NAME)
    @NotBlank(message = ServerUtil.CATEGORY_NAME)
    @ApiModelProperty(name = "name", dataType = "java.lang.String", value = "Le nom dde la catégorie de prosuits..", required = true, position = 1)
    private String name; // le nom de la catégorie de produit.

    @NotNull(message = ServerUtil.CATEGORY_DESC)
    @NotEmpty(message = ServerUtil.CATEGORY_DESC)
    @NotBlank(message = ServerUtil.CATEGORY_DESC)
    @ApiModelProperty(name = "description", dataType = "java.lang.String", value = "La description de la catégorie de produits.", required = true, position = 2)
    private String description; // la description de la catégorie de produit.

    @NotNull(message = ServerUtil.CATEGORY_ACTIVE)
    @NotEmpty(message = ServerUtil.CATEGORY_ACTIVE)
    @NotBlank(message = ServerUtil.CATEGORY_ACTIVE)
    @ApiModelProperty(name = "enabled", dataType = "java.lang.Boolean", value = "Indique si la catégorie de produits est disponible ou non.", required = true, position = 3)
    private Boolean enabled; // indique si la catégorie est active ou non.

    @NotNull(message = ServerUtil.CATEGORY_PRODUCTS)
    @NotBlank(message = ServerUtil.CATEGORY_PRODUCTS)
    @NotEmpty(message = ServerUtil.CATEGORY_PRODUCTS)
    @ApiModelProperty(name = "products", dataType = "java.util.Set<ProductDTO>", value = "La liste des produits rattachés à la catégorie.", required = true, position = 4)
    private Set<ProductDTO> products; // la liste des produits de la catégorie.

    @NotNull(message = ServerUtil.CATEGORY_TYPE)
    @NotEmpty(message = ServerUtil.CATEGORY_TYPE)
    @NotBlank(message = ServerUtil.CATEGORY_TYPE)
    @Pattern(regexp = "^(TELEPHONIE|TV|SON|INFORMATIQUE|PHOTO|JEUX_VIDEO|JOUETS|ELCETROMENAGER|MEUBLES_DECO|LITERIE)$", message = ServerUtil.CATEGORY_TYPE_REGEX)
    @ApiModelProperty(name = "products", dataType = "java.lang.String", value = "Le type de produits rattachés à la catégorie.", required = true, position = 5)
    private String type;

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
