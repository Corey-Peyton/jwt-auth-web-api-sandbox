/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ServerConstants.java
 * Date de création : 25 janv. 2021
 * Heure de création : 09:50:24
 * Package : fr.vincent.tuto.server.constants
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.constants;

import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import com.google.common.collect.Sets;

import fr.vincent.tuto.common.constants.AppConstants;
import fr.vincent.tuto.server.enumeration.RoleEnum;
import lombok.experimental.UtilityClass;

/**
 * Utilitaire de défintion des constantes pour le module applicatif.
 * 
 * @author Vincent Otchoun
 */
@UtilityClass
public final class ServerConstants
{
    // Cross-Origin - CORS constants
    public static final String ALOW_ORIGIN = HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN;
    public static final String ORIGIN = "*";
    public static final String ALLOW_CREDENTIALS = HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS;
    public static final String ALLOW_METHODS = HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS;

    // DELETE, GET, HEAD, OPTIONS, PATCH, POST, PUT
    public static final String ALLOW_AGE = HttpHeaders.ACCESS_CONTROL_MAX_AGE;
    public static final String ALLOW_HEADERS = HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS;

    public static final String REQUEST_METHOD = HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD;
    public static final String REQUEST_HEADER = HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS;
    public static final String OPTIONS_METHODS = "OPTIONS";

    // DAO
    public static final String USERS_BY_USERNAME_CACHE = "usersByUsername";
    public static final String USERS_BY_EMAIL_CACHE = "usersByEmail";
    public static final String ATTRIBUTE_PATHS = "roles";

    // REGEX ET PATTERNS
    public static final Pattern UUID_PATTERN = AppConstants.UUID_PATTERN;
    public static final String PASSWORD_REGEX = AppConstants.PASSWORD_PATTERN;
    public static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);
    public static final String EMAIL_REGEX = AppConstants.EMAIL_PATTERN;
    public static final Pattern EMAI_PATTERN = Pattern.compile(EMAIL_REGEX);
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";
    public static final Pattern LOGIN_PATTERN = Pattern.compile(LOGIN_REGEX);

    // USER Model Validation et messages
    public static final String USERNAME_VALIDATION_MSG = "Veuillez fournir un nom d'utilisateur entre 3 et 80 caractères";
    public static final String PWD_VALIDATION_MSG = "Veuillez fournir un mot de passe de 8 caractères ou plus avec au moins 1 chiffre et 1 lettre";
    public static final String EMAIL_VALIDATION_MSG = "Veuillez fournir un email valide";

    public static final String ACCOUNT_MSG_DEF = "Indique si le compte de l'utilisateur a expiré.";
    public static final String ID_MSG_DEF = "Identifiant technique auto-généré de l'objet en base.";
    public static final String LOCKED_MSG_DEF = "Indique si l'utilisateur est verrouillé ou déverrouillé.";
    public static final String CREDENTIALS_MSG_DEF = "Indique si les informations d'identification de l'utilisateur (mot de passe)ont expiré.";
    public static final String ENABLED_MSG_DEF = "Indique si l'utilisateur est activé ou désactivé.";
    public static final String OPTLOCK_MSG_DEF = "Lock Optimiste, 0 par défaut.";

    // SWAGGER
    private static final String SWAGGER_UI_URL = "/swagger-ui/index.html";
    private static final String SWAGGER_RESOURCES_URL = "/swagger-resources/**";
    private static final String SWAGGER_WEBJARS_URL = "/webjars/**";
    public final String[] SWAGGER_URL_PATHS = new String[] { SWAGGER_UI_URL, SWAGGER_RESOURCES_URL, "/v3/api-docs**", SWAGGER_WEBJARS_URL };
    public static final String V2_DOCS_SWAGGER = "/v2/api-docs";
    public static final String V3_DOCS_SWAGGER = "/v3/api-docs";
    public static final String INDEX_SWAGGER = "/swagger-ui/index.html";
    public static final String RES_SWAGGER = "/swagger-resources/**";
    public static final String UI_SWAGGER = "/swagger-ui.html";
    public static final String CONFIG_SWAGGER = "/configuration/**";
    public static final String WEB_JARS = "/webjars/**";
    public static final String PUBLIC_SWAGGER = "/configuration/**";

    public static final String H2_CONSOLE_URL = "/h2-console/**/**";
    public static final String ACTUATOR_URL = "/actuator/**/**";

    public static final String HEADER = "Header";
    public static final String AUTH_BAERER_TOKEN = "Bearer %token";

    static Set<String> mediaTypeSet = Sets.newHashSet();
    static
    {
        mediaTypeSet.add(MediaType.APPLICATION_JSON_VALUE);
    }

    public static final String USER_MSG = "L'utilisateur : ";
    public static final String USER_MSG_NOT_ACTIVATED = " est désactivé il ne peut être authentifié.";
    public static final String MAIL_MSG = "L'utilisateur avec l'email : ";
    public static final String DB_MSG = " n'est pas trouvé en base de données.";

    // Fonctions utilitaires
    /**
     * Creation de Spring Security User.
     * 
     * @param pUsername login de l'utilisateur.
     * @param pUser     les informations de l'utilisateur en base de données.
     * @return le user de Spring Security.
     */
    // public static User createSpringSecurityUser(final String pUsername, final fr.vincent.tuto.server.model.po.User pUser)
    // {
    // //
    // final Boolean active = pUser.getEnabled();
    // if (BooleanUtils.isFalse(active))
    // {
    // throw new CustomAppException(USER_MSG + pUsername + USER_MSG_NOT_ACTIVATED);
    // }
    //
    // Set<SimpleGrantedAuthority> grantedAuthorities = pUser.getRoles()//
    // .stream()//
    // .map(role -> new SimpleGrantedAuthority(role.getAuthority()))//
    // .collect(Collectors.toSet());
    // return new User(pUser.getUsername(), pUser.getPassword(), grantedAuthorities);
    // }

    /**
     * Convertit les autorités en une liste d'objets GrantedAuthority.
     */
    public static final java.util.List<GrantedAuthority> ROLES = AuthorityUtils.createAuthorityList(RoleEnum.ROLE_ANONYMOUS.getAuthority(),
    RoleEnum.ROLE_ADMIN.getAuthority(), RoleEnum.ROLE_USER.getAuthority(), RoleEnum.ROLE_MODERATOR.getAuthority());
}
