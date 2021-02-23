/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : UserMapper.java
 * Date de création : 11 févr. 2021
 * Heure de création : 07:23:30
 * Package : fr.vincent.tuto.server.mapper
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.mapper;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import fr.vincent.tuto.common.mapper.GenericObjectMapper;
import fr.vincent.tuto.server.enumeration.RoleEnum;
import fr.vincent.tuto.server.model.dto.UserDTO;
import fr.vincent.tuto.server.model.po.User;

/**
 * Service de conversion/transformation d'un objet de type {@link User} en son objet de tranfert de ses données
 * {@link UserDTO} et vice versa.
 * 
 * @author Vincent Otchoun
 */
@Service
public class UserMapper extends GenericObjectMapper<User, UserDTO>
{
    //
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * Constructeur avec paramètres pour injection du beans en dépendances.
     * 
     * @param pModelMapper le bean de conversion des modèles selon le type.
     */
    @Autowired
    protected UserMapper(final BCryptPasswordEncoder pPasswordEncoder, final ModelMapper pModelMapper)
    {
        super(pModelMapper);
        this.passwordEncoder = pPasswordEncoder;
    }

    @Override
    public User toSourceObject(UserDTO pUserDTO)
    {
        if (pUserDTO == null)
        {
            return null;
        }

        // Chiffer le mot de passe avant la persistance
        final String userPassword = pUserDTO.getPassword();
        final String encryptedPassword = this.passwordEncoder.encode(userPassword);

        // Récupérer les roles de l'utilisateur à partir de la chaîne de caractères pour les convertir en enumération.
        final Set<RoleEnum> roles = Optional.ofNullable(pUserDTO.getRoles())//
        .orElseGet(Collections::emptySet)//
        .stream()//
        .filter(Objects::nonNull)//
        .map(RoleEnum::valueOf)//
        .collect(Collectors.toSet())//
        ;

        return this.modelMapper.map(UserDTO.class, User.UserBuilder.class)//
        .id(pUserDTO.getId())//
        .username(pUserDTO.getUsername())//
        .password(encryptedPassword) //
        .email(pUserDTO.getEmail())//
        .accountExpired(pUserDTO.getAccountExpired()) //
        .accountLocked(pUserDTO.getAccountLocked()) //
        .credentialsExpired(pUserDTO.getCredentialsExpired())//
        .enabled(pUserDTO.getEnabled()) //
        .roles(roles)//
        .createdTime(pUserDTO.getCreatedTime()) //
        .updatedTime(pUserDTO.getUpdatedTime()) //
        .build();
    }

    /**
     * Construire la liste de {@link User} à partir de la liste des objets de transfert des données
     * {@link UserDTO}.
     * 
     * @param userDTOs la liste des objets de transfert des informations des utilisateurs.
     * @return la liste des données des utilisateurs en base de de données.
     */
    public Collection<User> toUsers(final Collection<UserDTO> userDTOs)
    {
        return super.toSourceObjectList(userDTOs);
    }

    @Override
    public UserDTO toDestObject(User pUser)
    {
        if (pUser == null)
        {
            return null;
        }

        // Récupérer les rôles de l'utilisateur sous forme de chaîne de caractères
        final Set<String> roles = Optional.ofNullable(pUser.getRoles())//
        .orElseGet(Collections::emptySet)//
        .stream()//
        .filter(Objects::nonNull)//
        .map(RoleEnum::getAuthority)//
        .collect(Collectors.toSet())//
        ;

        return this.modelMapper.map(User.class, UserDTO.UserDTOBuilder.class)//
        .id(pUser.getId())//
        .username(pUser.getUsername())//
        .password(pUser.getPassword()) //
        .email(pUser.getEmail())//
        .accountExpired(pUser.getAccountExpired()) //
        .accountLocked(pUser.getAccountLocked()) //
        .credentialsExpired(pUser.getCredentialsExpired())//
        .enabled(pUser.getEnabled()) //
        .roles(roles)//
        .createdTime(pUser.getCreatedTime()) //
        .updatedTime(pUser.getUpdatedTime()) //
        .build();
    }

    /**
     * Construire la liste des objets de transfert des données de {@link UserDTO} à partir de la liste des
     * {@link User}.
     * 
     * @param users la liste des données des utilisateurs en base de de données.
     * @return la liste des objets de transfert des informations des utilisateurs.
     */
    public Collection<UserDTO> toUserDtos(final Collection<User> users)
    {
        return super.toDestObjectList(users);
    }
}
