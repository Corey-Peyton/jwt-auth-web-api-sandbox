/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : Category.java
 * Date de création : 25 janv. 2021
 * Heure de création : 09:39:22
 * Package : fr.vincent.tuto.server.model.po
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.model.po;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
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

import fr.vincent.tuto.server.enumeration.CategoryTypeEnum;
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
 * Mapping des informations des catagories de produits en base de données dans la table T_CATEGORIES.
 * 
 * @author Vincent Otchoun
 */
@Entity
@Table(name = "T_CATEGORIES")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Category extends AbstractPersistable<Long> implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = -1610556115218749258L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    Long id; // identifiant technique auto-généré de l'objet en base.

    @NotNull(message = ServerUtil.CATEGORY_NAME)
    @Column(name = "NAME", nullable = false)
    String name; // le nom de la catégorie de produit.

    @NotNull(message = ServerUtil.CATEGORY_DESC)
    @Column(name = "DESCRIPTION", nullable = false)
    String description; // la description de la catégorie de produit.

    @NotNull(message = ServerUtil.CATEGORY_ACTIVE)
    @Column(name = "ENABLED", nullable = false)
    Boolean enabled; // indique si la catégorie est active ou non.

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> products; // la liste des produits de la catégorie.

    @NotNull(message = ServerUtil.CATEGORY_TYPE)
    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY_TYPE", nullable = false)
    CategoryTypeEnum categoryType;

    @JsonIgnore
    @Version
    @Column(name = "OPTLOCK", nullable = false)
    Integer version; // Gestion de l'optimistic lock (lock optimiste).

    @PrePersist
    protected void onCreate()
    {
        this.enabled = Boolean.TRUE;
        this.version = Integer.valueOf(0);
    }

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
