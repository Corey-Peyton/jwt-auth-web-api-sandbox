/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : UserDTO.java
 * Date de création : 10 févr. 2021
 * Heure de création : 21:00:32
 * Package : fr.vincent.tuto.server.model.dto
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.model.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import fr.vincent.tuto.server.model.po.User;
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
 * Classe d'implementation de l'objet de transfert des données (DTO) de {@link User}.
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
@JsonPropertyOrder({ "id", "username", "password", "email", "accountExpired", "accountLocked", "credentialsExpired", "enabled", "roles",
        "createdTime", "updatedTime" })
@ApiModel(description = "Objet de transfert des informations des utilisateurs", value = "Données Utilisateur")
public class UserDTO implements Serializable
{
    /**
     * 
     */
    private static final long serialVersionUID = 897952097395010927L;

    @ApiModelProperty(name = "id", dataType = "java.lang.Long", value = "Identifiant technique auto-généré.", position = 0)
    private Long id; // identifiant technique auto-généré de l'objet en base.

    @NotNull(message = ServerUtil.USERNAME_VALIDATION_MSG)
    @NotEmpty(message = ServerUtil.USERNAME_VALIDATION_MSG)
    @NotBlank(message = ServerUtil.USERNAME_VALIDATION_MSG)
    @Size(min = 3, max = 80, message = ServerUtil.USERNAME_VALIDATION_MSG)
    @Pattern(regexp = ServerUtil.LOGIN_REGEX)
    @ApiModelProperty(name = "username", dataType = "java.lang.String", value = "Le login utilisé pour authentifier l'utilisateur (non null et unique).", required = true, position = 1)
    private String username;

    // @JsonIgnore
    @NotNull(message = ServerUtil.PWD_VALIDATION_MSG)
    @NotEmpty(message = ServerUtil.PWD_VALIDATION_MSG)
    @NotBlank(message = ServerUtil.PWD_VALIDATION_MSG)
    @Pattern(regexp = ServerUtil.PASSWORD_REGEX, message = ServerUtil.PWD_VALIDATION_MSG)
    @Size(min = 60, max = 60)
    @ApiModelProperty(name = "password", dataType = "java.lang.String", value = "Le mot de passe utilisé pour authentifier l'utilisateur(non null).", required = true, position = 2)
    private String password;

    @Email(message = ServerUtil.EMAIL_VALIDATION_MSG)
    @Size(min = 8, max = 254, message = ServerUtil.EMAIL_VALIDATION_MSG)
    @ApiModelProperty(name = "email", dataType = "java.lang.String", value = "Adresse mail de l'utilisateur.", required = true, position = 3)
    private String email; // .

    @ApiModelProperty(name = "accountExpired", dataType = "java.lang.Boolean", value = "Indique si le compte de l'utilisateur a expiré.", position = 4)
    private Boolean accountExpired;

    @ApiModelProperty(name = "accountLocked", dataType = "java.lang.Boolean", value = "Indique si l'utilisateur est verrouillé ou déverrouillé.", position = 5)
    private Boolean accountLocked;//

    @ApiModelProperty(name = "credentialsExpired", dataType = "java.lang.Boolean", value = "Indique si les informations d'identification de l'utilisateur (mot de passe)ont expiré.", position = 6)
    private Boolean credentialsExpired; // .

    @ApiModelProperty(name = "enabled", dataType = "java.lang.Boolean", value = "Indique si l'utilisateur est activé ou désactivé.", position = 7)
    private Boolean enabled; // .

    @NotNull(message = ServerUtil.USER_ROLE_MSG)
    @NotBlank(message = ServerUtil.USER_ROLE_MSG)
    @NotEmpty(message = ServerUtil.USER_ROLE_MSG)
    @Pattern(regexp = "^(ROLE_USER|ROLE_ADMIN|ROLE_MODERATOR)$", message = ServerUtil.USER_ROLE_TYPE_REGEX)
    @ApiModelProperty(name = "products", dataType = "java.util.Set<String>", value = "Les rôles de l'utilisateur.", required = true, position = 8)
    private Set<String> roles;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ServerUtil.APP_DATE_TIME_ISO_FORMAT, locale = ServerUtil.FR_LOCALE, timezone = ServerUtil.CET_TIMEZONE)
    @ApiModelProperty(name = "createdTime", dataType = "java.time.LocalDateTime", value = "Horodatage pour la création de l'objet en base de données.", position = 9)
    private LocalDateTime createdTime;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ServerUtil.APP_DATE_TIME_ISO_FORMAT, locale = ServerUtil.FR_LOCALE, timezone = ServerUtil.CET_TIMEZONE)
    @ApiModelProperty(name = "updatedTime", dataType = "java.time.LocalDateTime", value = "Horodatage pour la mise à jour de l'objet en base de données.", position = 10)
    private LocalDateTime updatedTime;

    @Override
    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
