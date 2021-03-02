/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : Product.java
 * Date de création : 18 janv. 2021
 * Heure de création : 20:22:52
 * Package : fr.vincent.tuto.server.model.po
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.model.po;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.vincent.tuto.server.util.ServerUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Mapping des informations des produits en base de données dans la table T_PRODUCTS.
 * 
 * @author Vincent Otchoun
 */
@Entity
@Table(name = "T_PRODUCTS")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Hides the constructor to force useage of the Builder
@Builder
public class Product extends AbstractPersistable<Long> implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 7219178330020255201L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    Long id; // identifiant technique auto-généré de l'objet en base.

    @NotNull(message = ServerUtil.PRODUCT_NAME)
    @Column(name = "NAME", nullable = false)
    String name; // le nom du produit.

    @NotNull(message = ServerUtil.PRODUCT_DESC)
    @Column(name = "DESCRIPTION", nullable = false)
    String description; // la description du produit.

    @NotNull(message = ServerUtil.PRODUCT_QTY)
    @Column(name = "QUANTITY", nullable = false)
    Long quantity; // la quantité en stock pour le produit.

    @NotNull(message = ServerUtil.PRODUCT_UNIT)
    @Column(name = "UNIT_PRICE", nullable = false)
    BigDecimal unitPrice; // le prix unitaire du produit.

    @NotNull(message = ServerUtil.PRODUCT_PRICE)
    @Column(name = "PRICE", nullable = false)
    BigDecimal price; // le prix du produit.

    @NotNull(message = ServerUtil.PRODUCT_ACTIVE)
    @Column(name = "IS_ACTIVE", nullable = false)
    Boolean isActive; // indique si le produit est actif/disponible ou non

    @NotNull(message = ServerUtil.PRODUCT_IMG)
    @Column(name = "IMAGE_URL", nullable = false)
    String imageUrl;

    @JsonIgnore
    @Version
    @Column(name = "OPTLOCK", nullable = false)
    Integer version; // Gestion de l'optimistic lock (lock optimiste).

    @PrePersist
    protected void onCreate()
    {
        this.isActive = Boolean.TRUE;
        this.version = Integer.valueOf(0);
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
