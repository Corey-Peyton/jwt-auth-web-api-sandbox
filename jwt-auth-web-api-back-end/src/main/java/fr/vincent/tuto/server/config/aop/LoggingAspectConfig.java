/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : LoggingAspectConfig.java
 * Date de création : 23 févr. 2021
 * Heure de création : 03:01:53
 * Package : fr.vincent.tuto.server.aop
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.config.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StopWatch;

import lombok.extern.slf4j.Slf4j;

/**
 * Configuration AOP pour la séparation des préoccupations traversales (journalisation dans les couches applicatives).
 * 
 * @author Vincent Otchoun
 */
@Configuration
@Aspect
@Slf4j
public class LoggingAspectConfig
{
    //
    private static final String AFTER_MSG = "Erreur dans {}.{}() avec la cause = \'{}\' et le message = \'{}\'";
    private static final String ENTER_MSG = "Entrée dans la méthode : {}.{}() argument[s] = {}";
    private static final String EXIT_MSG = "Sortie de la méthode : {}.{}() avec le résultat  = {}";
    private static final String ILLEGAL_MSG = "Illegal argument: {} dans la méthode {}.{}()";
    private static final String PROCESSING_TIME_MSG = "Temps d'execution de la méthode : {}";
    private static final String AROUND_VALUES = "securityLayerExecution() || endpointsLayerExecution() || businessLayerExecution() || errorLayerExecution() || configLayerExecution() || repoLayerExecution()";
    private static final String POINTCUT_VALUES = "securityLayerExecution() || endpointsLayerExecution() || businessLayerExecution() || errorLayerExecution() || configLayerExecution() || repoLayerExecution()";


    /*
     * Défintion des Pointcut : définir quand un appel à une méthode doit être intercepté.
     */

    /**
     * Définiion du pointcu pour la couche des élément de sécurisation de l'application.
     */
    @Pointcut("execution(* fr.vincent.tuto.server.security.*.*(..))")
    public void securityLayerExecution()
    {
        //
    }
    
    /**
     * Définiion du pointcu pour la couche des reférentiels.
     */
    @Pointcut("execution(* fr.vincent.tuto.server.dao.*.*(..))")
    public void repoLayerExecution()
    {
        //
    }

    /**
     * Définiion du pointcu pour la couche des services métiers.
     */
    @Pointcut("execution(* fr.vincent.tuto.server.service.*.*(..))")
    public void businessLayerExecution()
    {
        //
    }

    /**
     * Définiion du pointcu pour la couche des endpoints REST Web.
     */
    @Pointcut("execution(* fr.vincent.tuto.server.api.*.*(..))")
    public void endpointsLayerExecution()
    {
        //
    }

    /**
     * Définiion du pointcu pour la couche des objets ou composants interceptant les erreurs.
     */
    @Pointcut("execution(* fr.vincent.tuto.server.errror.*.*(..))")
    public void errorLayerExecution()
    {
        //
    }

    /**
     * Définiion du pointcu pour la couche des composants de configuration de l'application.
     */
    @Pointcut("execution(* fr.vincent.tuto.server.config.*.*(..))")
    public void configLayerExecution()
    {
        //
    }
    
    /**
     * Advice qui enregistre les méthodes lançant des exceptions.
     * 
     * @param joinPoint le point de jonction.
     * @param e         exception ou erreur survenue lors de l'exécution de l'application
     */
    @AfterThrowing(pointcut = POINTCUT_VALUES, throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable e)
    {
        final String type = joinPoint.getSignature().getDeclaringTypeName();
        final String nomMethode = joinPoint.getSignature().getName();

        log.error(AFTER_MSG, type, nomMethode, e.getCause() != null ? e.getCause() : "NULL", e.getMessage(), e);
    }

    /**
     * Advice qui enregistre lorsqu'une méthode est entrée et sortie avec calcul de sa durée d'exécution.
     * 
     * @param joinPoint le point de jonction.
     * @return le resultat ou l'execption/erreur survenue.
     * @throws Throwable exception/erreur lorsque survient une erreur.
     */
    @Around(AROUND_VALUES)
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable
    {
        final String typeClasse = joinPoint.getSignature().getDeclaringTypeName();
        final String nomMethode = joinPoint.getSignature().getName();
        final Object[] argsMethode = joinPoint.getArgs();

        log.info(ENTER_MSG, typeClasse, nomMethode, Arrays.toString(argsMethode));

        final StopWatch clock = new StopWatch(getClass().getName());

        try
        {
            clock.start(joinPoint.toString());
            final Object result = joinPoint.proceed();

            log.info(EXIT_MSG, typeClasse, nomMethode, result);
            return result;
        }
        catch (IllegalArgumentException e)
        {
            log.error(ILLEGAL_MSG, Arrays.toString(argsMethode), typeClasse, nomMethode);
            throw e;
        }
        finally
        {
            clock.stop();
            log.info(PROCESSING_TIME_MSG, clock.prettyPrint());
        }
    }

}
