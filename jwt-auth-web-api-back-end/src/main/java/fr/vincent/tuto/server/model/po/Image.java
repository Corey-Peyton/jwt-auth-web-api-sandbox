/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : Image.java
 * Date de création : 25 janv. 2021
 * Heure de création : 09:37:54
 * Package : fr.vincent.tuto.server.model.po
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.model.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
 * Mapping des iformations des images en base de données dans la table T_IMAGES.
 * 
 * @author Vincent Otchoun
 */
@Entity
@Table(name = "T_IMAGES")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Getter 
@Setter 
@NoArgsConstructor 
@EqualsAndHashCode(callSuper = false, of = "id") 
@FieldDefaults(level = AccessLevel.PRIVATE) 
@AllArgsConstructor(access = AccessLevel.PROTECTED) 
@Builder 
public class Image extends AbstractPersistable<Long> implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 8697334648068422890L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", updatable = false, nullable = false)
    private Long id; // identifiant technique auto-généré de l'objet en base.
    
    @Column(name = "IMAGE_URL", nullable = false)
    private String imageUrl;
    
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
