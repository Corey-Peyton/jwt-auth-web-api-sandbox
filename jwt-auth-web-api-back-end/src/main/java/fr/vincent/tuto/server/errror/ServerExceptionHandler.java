/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ServerExceptionHandler.java
 * Date de création : 30 janv. 2021
 * Heure de création : 18:46:42
 * Package : fr.vincent.tuto.server.errror
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.errror;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import fr.vincent.tuto.common.constants.AppConstants;
import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.common.exception.GenericGlobalExceptionHandler;
import fr.vincent.tuto.common.model.error.ApiResponseError;
import fr.vincent.tuto.common.model.payload.GenericApiResponse;
import fr.vincent.tuto.common.utils.rest.RestUtils;

/**
 * Composant de gestion des erreurs au niveau de l'application.
 * 
 * @author Vincent Otchoun
 */
@ControllerAdvice
public class ServerExceptionHandler<T> extends GenericGlobalExceptionHandler<T>
{

    @Override
    public ResponseEntity<GenericApiResponse<T>> handleHttpClientErrorException(HttpClientErrorException ex)
    {
        LOGGER.info("[handleHttpClientErrorException] - Interception des erreurs de type 4xx.");
        
        final ApiResponseError error = new ApiResponseError()//
        .status(ex.getStatusCode())//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(AppConstants.HTTP_CLIENT_ERROR)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null);
        return RestUtils.buildResponseErrorEntity(error);
    }

    @Override
    public ResponseEntity<GenericApiResponse<T>> handleHttpCustomAppException(CustomAppException ex)
    {
        LOGGER.info("[handleHttpCustomAppException] - Interception des erreurs internes à l'application.");

        final ApiResponseError error = new ApiResponseError()//
        .status(HttpStatus.INTERNAL_SERVER_ERROR)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(AppConstants.SERVER_INTERNAL_ERROR)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null);
        return RestUtils.buildResponseErrorEntity(error);
    }

    @Override
    public ResponseEntity<GenericApiResponse<T>> handleNotReadableException(Exception ex)
    {
        LOGGER.info("[handleNotReadableException] - Interception des erreurs de structure mal formatée.");

        final ApiResponseError error = new ApiResponseError()//
        .status(HttpStatus.UNPROCESSABLE_ENTITY)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(AppConstants.FORMAT_ERROR)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null);
        return RestUtils.buildResponseErrorEntity(error);
    }

    @Override
    public ResponseEntity<GenericApiResponse<T>> handleNoHandlerFoundException(NoHandlerFoundException ex)
    {
        LOGGER.info("[handleNoHandlerFoundException] - Interception des erreurs d'URL non valide.");

        final ApiResponseError error = new ApiResponseError()//
        .status(HttpStatus.BAD_REQUEST)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(String.format(AppConstants.URL_ERROR, ex.getHttpMethod(), ex.getRequestURL()))//
        .debugMessage(ex.getMessage())//
        .validationErrors(null);
        return RestUtils.buildResponseErrorEntity(error);
    }

    @Override
    public ResponseEntity<GenericApiResponse<T>> handleConstraintViolationException(ConstraintViolationException ex)
    {
        LOGGER.info("[handleConstraintViolationException] - Interception des erreurs de violation d contraintes.");

        final ApiResponseError error = new ApiResponseError()//
        .status(HttpStatus.BAD_REQUEST)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(AppConstants.CONTRAINST_VALDATION_ERROR)//
        .debugMessage(ex.getMessage());

        error.addValidationErrorsCV(ex.getConstraintViolations());
        return RestUtils.buildResponseErrorEntity(error);
    }

    @Override
    public ResponseEntity<GenericApiResponse<T>> handleNotFoundException(Exception ex)
    {
        LOGGER.info("[handleNotFoundException] - Interception des erreurs pour recherche infructueuse.");

        final ApiResponseError error = new ApiResponseError()//
        .status(HttpStatus.NOT_FOUND)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(AppConstants.NOT_FOUND_ERROR)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null);
        return RestUtils.buildResponseErrorEntity(error);
    }

    @Override
    public ResponseEntity<GenericApiResponse<T>> handleDataIntegrityException(DataIntegrityViolationException ex)
    {
        LOGGER.info("[handleDataIntegrityException] - Interception des erreurs pour violation d'intégrité des données.");

        final ApiResponseError error = new ApiResponseError()//
        .status(HttpStatus.CONFLICT)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(AppConstants.INTEGRITY_ERROR)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null);
        return RestUtils.buildResponseErrorEntity(error);
    }

    @Override
    public ResponseEntity<GenericApiResponse<T>> handleMethodArgumentTypException(MethodArgumentTypeMismatchException ex)
    {
        LOGGER.info("[handleMethodArgumentTypException] - Interception des erreurs de non-concordance de type d'argument de  méthode.");

        final String details = String.format(AppConstants.METHOD_ERROR, ex.getName(), ex.getValue(), ex.getRequiredType()+StringUtils.EMPTY);
        final ApiResponseError error = new ApiResponseError()//
        .status(HttpStatus.BAD_REQUEST)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(details)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null);
        return RestUtils.buildResponseErrorEntity(error);
    }
    

    @Override
    public ResponseEntity<GenericApiResponse<T>> handleAccessDeniedException(Exception ex)
    {
        LOGGER.info("[handleAccessDeniedException] - Interception des erreurs d'accès non autorisé.");

        final ApiResponseError error = new ApiResponseError()//
        .status(HttpStatus.UNAUTHORIZED)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(AppConstants.ACCESS_DENIED)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null);
        return RestUtils.buildResponseErrorEntity(error);
    }

}
