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
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import fr.vincent.tuto.common.constants.AppConstants;
import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.common.exception.GenericGlobalExceptionHandler;
import fr.vincent.tuto.common.model.error.ApiResponseError;
import fr.vincent.tuto.common.model.payload.GenericApiResponse;
import fr.vincent.tuto.common.utils.rest.RestUtils;
import fr.vincent.tuto.server.util.ServerUtil;

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
        final var error = new ApiResponseError()//
        .status(ex.getStatusCode())//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(AppConstants.HTTP_CLIENT_ERROR)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null);
        return RestUtils.buildResponseErrorEntity(error);
    }

    @Override
    @ExceptionHandler(value = { CustomAppException.class, AuthenticationException.class })
    public ResponseEntity<GenericApiResponse<T>> handleHttpCustomAppException(Exception ex)
    {
        HttpStatus status = null;
        String detailsMessage = null;

        if (AuthenticationException.class.isAssignableFrom(ex.getClass()))
        {
            status = HttpStatus.UNAUTHORIZED;
            detailsMessage = ServerUtil.ACCESS_DENIED;
        }
        else if (CustomAppException.class.isAssignableFrom(ex.getClass()))
        {
            status = HttpStatus.SERVICE_UNAVAILABLE;
            detailsMessage = ServerUtil.SERVER_UNAVAILABLE_MSG;
        }

        final var error = new ApiResponseError()//
        .status(status)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(detailsMessage)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null);
        return RestUtils.buildResponseErrorEntity(error);
    }

    @Override
    public ResponseEntity<GenericApiResponse<T>> handleNotReadableException(Exception ex)
    {
        final var error = new ApiResponseError()//
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
        final var error = new ApiResponseError()//
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
        final var error = new ApiResponseError()//
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
        final var error = new ApiResponseError()//
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
        final var error = new ApiResponseError()//
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
        final String details = String.format(AppConstants.METHOD_ERROR, ex.getName(), ex.getValue(), ex.getRequiredType() + StringUtils.EMPTY);
        final var error = new ApiResponseError()//
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
        final var error = new ApiResponseError()//
        .status(HttpStatus.UNAUTHORIZED)//
        .timestamp(LocalDateTime.now(ZoneId.systemDefault()))//
        .details(AppConstants.ACCESS_DENIED)//
        .debugMessage(ex.getMessage())//
        .validationErrors(null);
        return RestUtils.buildResponseErrorEntity(error);
    }
}
