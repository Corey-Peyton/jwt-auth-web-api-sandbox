/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : AuthTokenProvider.java
 * Date de création : 3 mars 2021
 * Heure de création : 19:43:26
 * Package : fr.vincent.tuto.server.security.jwt
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.security.jwt;

import java.time.Instant;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import fr.vincent.tuto.common.model.mo.TokenModel;
import fr.vincent.tuto.common.service.props.ApplicationPropsService;
import lombok.extern.slf4j.Slf4j;

/**
 * Service de traitement des jetons JWT. Il fournit en autres les fonctions suivantes :
 * <ul>
 * <li>Créer le jeton JWT d'accès pour l'authentification/l'autorisation.</li>
 * <li>Créer le jeton JWT d'actualisation/rafraîchissement pour l'authentification/l'autorisation.</li>
 * <li>Obtenir le jeton pour une demande d'authentification ou pour un principal authentifié à partir du jeton JWT.</li>
 * <li>Valider le jeton d'accès JWT.</li>
 * <li>Obtenir le jeton JWT d'accès : les revendications (claims), le nom d'utilisateur, et la signature.</li>
 * </ul>
 * 
 * @author Vincent Otchoun
 */
@Component
@Slf4j
public class AuthTokenProvider implements InitializingBean
{
    // Retourne la durée en secondes (*1000 = équivalent de getTime() en millisecondes)
    long instant = Instant.now().getEpochSecond();

    private ApplicationPropsService propsService;
    private TokenModel tokenModel;

    @Override
    public void afterPropertiesSet() throws Exception
    {
        log.info("[afterPropertiesSet] - Initialisation des variables.");

    }
}
