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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.domain.AbstractPersistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fr.vincent.tuto.server.enumeration.CategoryTypeEnum;
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
@AllArgsConstructor(access = AccessLevel.PROTECTED)
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
    private Long id; // identifiant technique auto-généré de l'objet en base.

    @Column(name = "NAME", nullable = false)
    private String name; // le nom de la catégorie de produit.
    
    @Column(name = "DESCRIPTION", nullable = false)
    private String description; // la description de la catégorie de produit.
    
    @Column(name = "ENABLED", nullable = false)
    private Boolean enabled; // indique si la catégorie est active ou non.
    
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Product> products; // la liste des produits de la catégorie.
    
    @Enumerated(EnumType.STRING)
    @Column(name = "CATEGORY_TYPE", nullable = false)
    private CategoryTypeEnum categoryType;
    
    @JsonIgnore
    @Version
    @Column(name = "OPTLOCK", nullable = false)
    private Integer version; // Gestion de l'optimistic lock (lock optimiste).
    
    
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
