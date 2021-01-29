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

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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
    private Long id; // identifiant technique auto-généré de l'objet en base.

    @Column(name = "NAME", nullable = false)
    private String name; // le nom du produit.

    @Column(name = "DESCRIPTION", nullable = false)
    private String description; // la description du produit.

    @Column(name = "QUANTITY", nullable = false)
    private Long quantity; // la quantité en stock pour le produit.

    @Column(name = "UNIT_PRICE", nullable = false)
    private BigDecimal unitPrice; // le prix unitaire du produit.

    @Column(name = "PRICE", nullable = false)
    private BigDecimal price; // le prix du produit.

    @Column(name = "IS_ACTIVE", nullable = false)
    private Boolean isActive; // indique si le prOduit est actif/disponible ou non

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "IMAGE_ID", nullable = false)
    private Image image; // URL de l'image du produit.

    @JsonIgnore
    @Version
    @Column(name = "OPTLOCK", nullable = false)
    private Integer version; // Gestion de l'optimistic lock (lock optimiste).

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
