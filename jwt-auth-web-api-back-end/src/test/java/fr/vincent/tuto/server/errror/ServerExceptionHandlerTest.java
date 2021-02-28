/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ServerExceptionHandlerTest.java
 * Date de création : 30 janv. 2021
 * Heure de création : 19:14:03
 * Package : fr.vincent.tuto.server.errror
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.errror;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;
import java.util.Set;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.http.MockHttpInputMessage;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.core.JsonParseException;
import com.google.common.collect.Sets;

import fr.vincent.tuto.common.constants.AppConstants;
import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.common.model.error.ApiResponseError;
import fr.vincent.tuto.common.model.payload.GenericApiResponse;
import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.cache.ServerCacheConfig;
import fr.vincent.tuto.server.config.db.PersistanceContextConfig;
import fr.vincent.tuto.server.model.po.Product;
import fr.vincent.tuto.server.model.po.User;
import fr.vincent.tuto.server.util.ServerUtil;
import io.jsonwebtoken.JwtException;

/**
 * Classe des Tests Unitaires des objets de type {@link ServerExceptionHandler}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-common-test.properties", "classpath:back-end-application-test.properties" })
@ContextConfiguration(name = "serverExceptionHandlerTest", classes = { BackEndServerRootConfig.class, DatabasePropsService.class,
        PersistanceContextConfig.class, ServerCacheConfig.class })
@SpringBootTest
@ActiveProfiles("test")
class ServerExceptionHandlerTest
{
    @Autowired
    private ServerExceptionHandler<Product> exceptionHandler;

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.exceptionHandler = null;
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.errror.ServerExceptionHandler#handleHttpClientErrorException(org.springframework.web.client.HttpClientErrorException)}.
     */
    @Test
    void testHandleHttpClientErrorException()
    {
        final HttpClientErrorException clientErrorException = new HttpClientErrorException(HttpStatus.UNAUTHORIZED, AppConstants.ACCESS_DENIED);
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleHttpClientErrorException(clientErrorException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(AppConstants.HTTP_CLIENT_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(clientErrorException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleHttpClientErrorException_BadRequest()
    {
        final HttpClientErrorException clientErrorException = new HttpClientErrorException(HttpStatus.BAD_REQUEST, AppConstants.ACCESS_DENIED);
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleHttpClientErrorException(clientErrorException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(AppConstants.HTTP_CLIENT_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(clientErrorException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleHttpClientErrorException_ShouldThrowException()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            new HttpClientErrorException(null, null);
        });

        String actualMessage = exception.getMessage();

        // System.err.println(">>>Le message d'erreur est : \n"+actualMessage);

        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.errror.ServerExceptionHandler#handleHttpCustomAppException(fr.vincent.tuto.common.exception.CustomAppException)}.
     */
    @Test
    void testHandleHttpCustomAppException()
    {
        final CustomAppException customAppException = new CustomAppException("Erreur customisée");
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleHttpCustomAppException(customAppException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(ServerUtil.SERVER_UNAVAILABLE_MSG);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(customAppException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }


    @Test
    void testHandleHttpCustomAppException_WithNull()
    {
        final CustomAppException customAppException = new CustomAppException(null, null);
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleHttpCustomAppException(customAppException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(ServerUtil.SERVER_UNAVAILABLE_MSG);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(customAppException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleHttpCustomAppException_ShouldTrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleHttpCustomAppException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }
    

    @Test
    void testHandleHttpCustomAppException_AuthenticationException()
    {
        final var authenticationException = new AuthenticationExceptionCustom("AuthenticationException customisée");
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleHttpCustomAppException(authenticationException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(ServerUtil.ACCESS_DENIED);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(authenticationException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }
    
    @Test
    void testHandleHttpCustomAppException_WithNull_AuthenticationException()
    {
        final var customAppException = new AuthenticationExceptionCustom(null, null); 
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleHttpCustomAppException(customAppException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(ServerUtil.ACCESS_DENIED);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(customAppException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }
    
    @Test
    void testHandleHttpCustomAppException_ShouldTrowNPE_AuthenticationException()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleHttpCustomAppException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.errror.ServerExceptionHandler#handleNotReadableException(java.lang.Exception)}.
     */
    @Test
    void testHandleNotReadableException()
    {
        MockHttpInputMessage inputMessage = new MockHttpInputMessage("mockInput".getBytes());
        final HttpMessageNotReadableException notReadableException = new HttpMessageNotReadableException("HttpMessageNotReadableExceptione", null,
        inputMessage);
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleNotReadableException(notReadableException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(AppConstants.FORMAT_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(notReadableException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleNotReadableException_Json()
    {
        final JsonParseException jsonParseException = new JsonParseException(null, "JSON");
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleNotReadableException(jsonParseException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(AppConstants.FORMAT_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(jsonParseException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleNotReadableException_WithNull()
    {
        final HttpMessageNotReadableException notReadableException = new HttpMessageNotReadableException(null, null, null);
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleNotReadableException(notReadableException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(AppConstants.FORMAT_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(notReadableException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleNotReadableException_ShouldTrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleNotReadableException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.errror.ServerExceptionHandler#handleNoHandlerFoundException(org.springframework.web.servlet.NoHandlerFoundException)}.
     */
    @Test
    void testHandleNoHandlerFoundException()
    {
        ServletServerHttpRequest req = new ServletServerHttpRequest(new MockHttpServletRequest("GET", "/resource"));
        final NoHandlerFoundException noHandlerFoundException = new NoHandlerFoundException(req.getMethod().toString(), req.getServletRequest()
        .getRequestURI(), req.getHeaders());
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleNoHandlerFoundException(noHandlerFoundException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().getDetails()).isNotEqualTo(AppConstants.URL_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(noHandlerFoundException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleNoHandlerFoundException_WithNull()
    {
        final NoHandlerFoundException noHandlerFoundException = new NoHandlerFoundException(null, null, null);
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleNoHandlerFoundException(noHandlerFoundException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().getDetails()).isNotEqualTo(AppConstants.URL_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(noHandlerFoundException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleNoHandlerFoundException_ShouldThrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleNoHandlerFoundException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.errror.ServerExceptionHandler#handleConstraintViolationException(javax.validation.ConstraintViolationException)}.
     */
    @Test
    void testHandleConstraintViolationException()
    {
        Set<ConstraintViolation<User>> constraintViolations = Sets.newHashSet();
        final ConstraintViolationException violationException = new ConstraintViolationException("Violation de contraintes", constraintViolations);
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleConstraintViolationException(violationException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(AppConstants.CONTRAINST_VALDATION_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(violationException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isNull();
    }

    @Test
    void testHandleConstraintViolationException_WithNull()
    {
        final ConstraintViolationException violationException = new ConstraintViolationException(null, null);
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleConstraintViolationException(violationException);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    @Test
    void testHandleConstraintViolationException_ShouldThrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleConstraintViolationException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.errror.ServerExceptionHandler#handleNotFoundException(java.lang.Exception)}.
     */
    @Test
    void testHandleNotFoundException()
    {
        final EntityNotFoundException entityNotFoundException = new EntityNotFoundException("Entité non trouvée.");
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleNotFoundException(entityNotFoundException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(AppConstants.NOT_FOUND_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(entityNotFoundException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleNotFoundException_WithNull()
    {
        final EntityNotFoundException entityNotFoundException = new EntityNotFoundException(null);
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleNotFoundException(entityNotFoundException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(AppConstants.NOT_FOUND_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(entityNotFoundException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleNotFoundException_ShouldThrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleNotFoundException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.errror.ServerExceptionHandler#handleDataIntegrityException(org.springframework.dao.DataIntegrityViolationException)}.
     */
    @Test
    void testHandleDataIntegrityException()
    {
        final DataIntegrityViolationException dataIntegrityViolationException = new DataIntegrityViolationException(
        "Violation intégrité des données.");
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleDataIntegrityException(
        dataIntegrityViolationException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(AppConstants.INTEGRITY_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(dataIntegrityViolationException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleDataIntegrityException_WithNull()
    {
        final DataIntegrityViolationException dataIntegrityViolationException = new DataIntegrityViolationException(null, null);
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleDataIntegrityException(
        dataIntegrityViolationException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody().getErrors().getDetails()).isEqualTo(AppConstants.INTEGRITY_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(dataIntegrityViolationException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleDataIntegrityException_ShouldThrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleDataIntegrityException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.errror.ServerExceptionHandler#handleMethodArgumentTypException(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException)}.
     * 
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    @Test
    void testHandleMethodArgumentTypException() throws NoSuchMethodException, SecurityException
    {
        Method method = Product.class.getMethod("setDescription", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentTypeMismatchException mismatchException = new MethodArgumentTypeMismatchException(null, Product.class, "name", methodParameter,
        null);
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleMethodArgumentTypException(mismatchException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().getDetails()).isNotEqualTo(AppConstants.METHOD_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(mismatchException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleMethodArgumentTypException_WithNull()
    {
        MethodArgumentTypeMismatchException mismatchException = new MethodArgumentTypeMismatchException(null, null, null, null, null);
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleMethodArgumentTypException(mismatchException);
        // final Exception exception = assertThrows(NullPointerException.class, () -> {
        // this.exceptionHandler.handleMethodArgumentTypException(mismatchException);
        // });

        // String actualMessage = exception.getMessage();
        // assertThat(actualMessage).isNull();

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getErrors().getDetails()).isNotEqualTo(AppConstants.METHOD_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(mismatchException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleMethodArgumentTypException_ShouldThrowNPE()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleMethodArgumentTypException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.errror.ServerExceptionHandler#handleAccessDeniedException(java.lang.Exception)}.
     */
    @Test
    void testHandleAccessDeniedException()
    {
        AccessDeniedException accessDeniedException = new AccessDeniedException("Accès non autorisé");
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleAccessDeniedException(accessDeniedException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getErrors().getDetails()).isNotEqualTo(AppConstants.METHOD_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(accessDeniedException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleAccessDeniedException_Jwt()
    {
        JwtException jwtException = new JwtException("Accès non autorisé Jwt");
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleAccessDeniedException(jwtException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getErrors().getDetails()).isNotEqualTo(AppConstants.METHOD_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(jwtException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleAccessDeniedException_WithNull()
    {
        AccessDeniedException accessDeniedException = new AccessDeniedException(null, null);
        final ResponseEntity<GenericApiResponse<Product>> response = this.exceptionHandler.handleAccessDeniedException(accessDeniedException);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getData()).isNull();
        assertThat(response.getBody().getErrors()).isNotNull();
        assertThat(response.getBody().getErrors()).isExactlyInstanceOf(ApiResponseError.class);
        assertThat(response.getBody().getErrors().getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody().getErrors().getDetails()).isNotEqualTo(AppConstants.METHOD_ERROR);
        assertThat(response.getBody().getErrors().getDebugMessage()).isEqualTo(accessDeniedException.getMessage());
        assertThat(response.getBody().getErrors().getValidationErrors()).isEmpty();
    }

    @Test
    void testHandleAccessDeniedException_ShouldThrowException()
    {
        final Exception exception = assertThrows(NullPointerException.class, () -> {
            this.exceptionHandler.handleAccessDeniedException(null);
        });

        String actualMessage = exception.getMessage();
        assertThat(actualMessage).isNull();
    }

    class AuthenticationExceptionCustom extends AuthenticationException
    {
        /**
         * 
         */
        private static final long serialVersionUID = -8733741822686331697L;

        /**
         * @param msg
         * @param t
         */
        public AuthenticationExceptionCustom(String msg, Throwable t)
        {
            super(msg, t);
        }

        /**
         * @param msg
         */
        public AuthenticationExceptionCustom(String msg)
        {
            super(msg);
        }

    }
}
