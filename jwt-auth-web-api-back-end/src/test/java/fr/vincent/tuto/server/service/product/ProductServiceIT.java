/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ProductServiceIT.java
 * Date de création : 31 janv. 2021
 * Heure de création : 17:28:39
 * Package : fr.vincent.tuto.server.service.product
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;

import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistanceContextConfig;
import fr.vincent.tuto.server.model.po.Product;

/**
 * Classe des Tests d'Intégration (composants et système) des objets de type {@link ProductService}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-common-test.properties", "classpath:back-end-application-test.properties" })
@ContextConfiguration(name = "productServiceIT", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistanceContextConfig.class,
        ProductService.class })
@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = {"classpath:db/h2/drop-test-h2.sql", "classpath:db/h2/create-test-h2.sql", "classpath:db/h2/data-test-h2.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ProductServiceIT
{
    //
    private static final String SEARCH_BY_NAME_MSG = "Erreur recherche des informations d'un produit par son nom";
    private static final String SEARCH_BY_ID_MSG = "Erreur recherche des informations d'un produit par identifiant";
    private static final String SAVE_MSG = "Erreur lors de la sauvegarde en base de donnnées des informations d'un produits";
    private static final String TRANSACTION_MSG = "Could not commit JPA transaction;";
    private static final String INVALID_DATA_ACCES_MSG = "The given id must not be null!";

    @Autowired
    private IProductService productService;

    private Product product;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        // Instance de nouveau produit à rajouter à l'existant
        this.product = Product.builder()//
        .name("Nom produit de Test L2008902")//
        .description("Description produit de Test") //
        .quantity(1L)//
        .unitPrice(new BigDecimal("10.00"))//
        .price(new BigDecimal("10.00"))//
        .imageUrl("img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg") //
        .build();
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.productService = null;
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.ProductService#createProduct(fr.vincent.tuto.server.model.po.Product)}.
     */
    @Test
    void testCreateProduct()
    {
        final Product savedProduct = this.productService.createProduct(this.product);

        System.err.println("Inditifnat du produit est :" + savedProduct.getId());

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isPositive();

        final List<Product> products = (List<Product>) this.productService.getProducts();

        assertThat(products).isNotEmpty();
        assertThat(products.size()).isEqualTo(15); // Le fichier data-test-h2.sql d'insertion d'enregistrements dans la table T_PRODUCTS contient déjà
                                                   // 14 enregistrements.
    }

    @Test
    void testCreateProduct_WithNotNullFiled_IsNull()
    {
        final Product productInternal = Product.builder()//
        .name(null)//
        .description(null) //
        .quantity(1L)//
        .unitPrice(new BigDecimal("10.00"))//
        .price(new BigDecimal("10.00"))//
        .imageUrl("img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg") //
        .build();

        final Exception exception = assertThrows(TransactionSystemException.class, () -> {
            this.productService.createProduct(productInternal);
        });

        final String expectedMessage = TRANSACTION_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testCreateProduct_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.createProduct(null);
        });

        final String expectedMessage = SAVE_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.ProductService#getProductById(java.lang.Long)}.
     */
    @Test
    void testGetProductById()
    {
        final Long EXIST_ID = 8L;
        final Optional<Product> optional = this.productService.getProductById(EXIST_ID);

        assertThat(optional).isPresent();
        final Product product = optional.get();

        assertThat(product).isNotNull();
        assertThat(product.getName()).contains("L2008902");
        assertThat(product.getDescription()).contains("Batterie de cuisine 10 pièces");
        assertThat(product.getQuantity()).isExactlyInstanceOf(Long.class);
        assertThat(product.getUnitPrice()).isExactlyInstanceOf(BigDecimal.class);
        assertThat(product.getPrice()).isExactlyInstanceOf(BigDecimal.class);
        assertThat(product.getImageUrl()).contains("img/");
        assertThat(product.getQuantity()).isEqualTo(5L);
        assertThat(product.getUnitPrice()).isEqualTo(new BigDecimal("5.54"));
    }

    @Test
    void testGetProductById_WithNotExistId()
    {
        final Long NOT_EXIST_ID = Long.MAX_VALUE;

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.getProductById(NOT_EXIST_ID);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetProductById_WithNull()
    {
        final Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.productService.getProductById(null);
        });

        final String expectedMessage = INVALID_DATA_ACCES_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.ProductService#getProductByName(java.lang.String)}.
     */
    @Test
    void testGetProductByName()
    {
        final String EXIST_PRODUCT_NAME = "BARCELONE";

        final Optional<Product> optional = this.productService.getProductByName(EXIST_PRODUCT_NAME);

        assertThat(optional).isPresent();
        final Product product = optional.get();

        assertThat(product).isNotNull();
        assertThat(product.getName()).contains("BARCELONE");
        assertThat(product.getDescription()).contains("Canapé d\'angle convertible 4 places +");
        assertThat(product.getQuantity()).isExactlyInstanceOf(Long.class);
        assertThat(product.getUnitPrice()).isExactlyInstanceOf(BigDecimal.class);
        assertThat(product.getPrice()).isExactlyInstanceOf(BigDecimal.class);
        assertThat(product.getImageUrl()).contains("img/");
        assertThat(product.getQuantity()).isEqualTo(1L);
        assertThat(product.getUnitPrice()).isEqualTo(new BigDecimal("499.99"));
    }

    @Test
    void testGetProductByName_WithNotExistName()
    {
        final String NOT_EXIST_PRODUCT_NAME = "NOT_EXIST_PRODUCT_NAME";

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.getProductByName(NOT_EXIST_PRODUCT_NAME);
        });

        final String expectedMessage = SEARCH_BY_NAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetProductByName_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.getProductByName(null);
        });

        final String expectedMessage = SEARCH_BY_NAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.ProductService#getProductByNameIgnoreCase(java.lang.String)}.
     */
    @Test
    void testGetProductByNameIgnoreCase()
    {
        final String EXIST_PRODUCT_NAME = "barcelone";

        final Optional<Product> optional = this.productService.getProductByNameIgnoreCase(EXIST_PRODUCT_NAME);

        assertThat(optional).isPresent();
        final Product product = optional.get();

        assertThat(product).isNotNull();
        assertThat(product.getName()).contains("BARCELONE");
        assertThat(product.getDescription()).contains("Canapé d\'angle convertible 4 places +");
        assertThat(product.getQuantity()).isExactlyInstanceOf(Long.class);
        assertThat(product.getUnitPrice()).isExactlyInstanceOf(BigDecimal.class);
        assertThat(product.getPrice()).isExactlyInstanceOf(BigDecimal.class);
        assertThat(product.getImageUrl()).contains("img/");
        assertThat(product.getQuantity()).isEqualTo(1L);
        assertThat(product.getUnitPrice()).isEqualTo(new BigDecimal("499.99"));
    }

    @Test
    void testGetProductByNameIgnoreCase_WithNotExistName()
    {
        final String NOT_EXIST_PRODUCT_NAME = "not_exist_product_name";

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.getProductByNameIgnoreCase(NOT_EXIST_PRODUCT_NAME);
        });

        final String expectedMessage = SEARCH_BY_NAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetProductByNameIgnoreCase_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.getProductByNameIgnoreCase(null);
        });

        final String expectedMessage = SEARCH_BY_NAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.ProductService#existsProductByName(java.lang.String)}.
     */
    @Test
    void testExistsProductByName()
    {
        final String EXIST_PRODUCT_NAME = "AUNA DS-2";

        final Boolean existProduct = this.productService.existsProductByName(EXIST_PRODUCT_NAME);

        assertThat(existProduct).isNotNull();
        assertThat(existProduct.booleanValue()).isTrue();
    }

    @Test
    void testExistsProductByName_WithNotExistName()
    {
        final String NOT_EXIST_PRODUCT_NAME = "NOT_EXIST_PRODUCT_NAME";

        final Boolean existProduct = this.productService.existsProductByName(NOT_EXIST_PRODUCT_NAME);

        assertThat(existProduct).isNotNull();
        assertThat(existProduct.booleanValue()).isFalse();
    }

    @Test
    void testExistsProductByName_WithNull()
    {
        final Boolean existProduct = this.productService.existsProductByName(null);

        assertThat(existProduct).isNotNull();
        assertThat(existProduct.booleanValue()).isFalse();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.ProductService#getProductsByIsActive(java.lang.Boolean, org.springframework.data.domain.Pageable)}.
     */
    @Test
    void testGetProductsByIsActiveBooleanPageable()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        final Page<Product> result = this.productService.getProductsByIsActive(Boolean.TRUE, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isEqualTo(5); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(5); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(3); // Le nombre total de pages
        assertThat(result.getContent().size()).isEqualTo(5); // La taille du contenu
    }

    @Test
    void testGetProductsByIsActiveBooleanPageable_WithFalse()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        final Page<Product> result = this.productService.getProductsByIsActive(Boolean.FALSE, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isNotPositive(); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(5); // La taille de la page
        assertThat(result.getTotalPages()).isNotPositive(); // Le nombre total de pages
        assertThat(result.getContent().size()).isNotPositive(); // La taille du contenu
    }

    @Test
    void testGetProductsByIsActiveBooleanPageable_WithNull()
    {
        final Page<Product> result = this.productService.getProductsByIsActive(null, null);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isNotPositive(); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isNotPositive(); // La taille de la page
        assertThat(result.getTotalPages()).isPositive(); // Le nombre total de pages
        assertThat(result.getContent().size()).isNotPositive(); // La taille du contenu
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.ProductService#getProductsByIsActive(java.lang.Boolean)}.
     */
    @Test
    void testGetProductsByIsActiveBoolean()
    {
        final List<Product> result = (List<Product>) this.productService.getProductsByIsActive(Boolean.TRUE);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(14); // Le fichier data-test-h2.sql d'insertion dans la table T_PRODUCTS contient déjà 14 enregistrements
                                                 // tous actifs
    }

    @Test
    void testGetProductsByIsActiveBoolean_WithFalse()
    {
        final List<Product> result = (List<Product>) this.productService.getProductsByIsActive(Boolean.FALSE);

        assertThat(result).isNotNull();
        assertThat(result.size()).isNotPositive(); // Le fichier data-test-h2.sql d'insertion dans la table T_PRODUCTS contient déjà 14
                                                   // enregistrements tous actifs
    }

    @Test
    void testGetProductsByIsActiveBoolean_WithNull()
    {
        final List<Product> result = (List<Product>) this.productService.getProductsByIsActive(null);

        assertThat(result).isNotNull();
        assertThat(result.size()).isNotPositive(); // Le fichier data-test-h2.sql d'insertion dans la table T_PRODUCTS contient déjà 14
                                                   // enregistrements tous actifs.
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.ProductService#getProducts()}.
     */
    @Test
    void testGetProducts()
    {
        final List<Product> result = (List<Product>) this.productService.getProducts();

        assertThat(result).isNotNull();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(14);// Le fichier data-test-h2.sql d'insertion dans la table T_PRODUCTS contient déjà 14 enregistrements.
                                                // tous actifs.
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.ProductService#getFilteredProducts(java.lang.String)}.
     */
    @Test
    void testGetFilteredProducts()
    {
        final String QUERY = "PHILIPS";
        final List<Product> result = (List<Product>) this.productService.getFilteredProducts(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testGetFilteredProducts_Mixte()
    {
        final String QUERY = "PHILIPS";
        final List<Product> result = (List<Product>) this.productService.getFilteredProducts(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testGetFilteredProducts_WithLowerCase()
    {
        final String QUERY = "philips";
        final List<Product> result = (List<Product>) this.productService.getFilteredProducts(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testGetFilteredProducts_WithChar()
    {
        final String QUERY = "L";

        final List<Product> result = (List<Product>) this.productService.getFilteredProducts(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(9);
    }

    @Test
    void testGetFilteredProducts_WithNull()
    {
        final List<Product> result = (List<Product>) this.productService.getFilteredProducts(null);

        assertThat(result).isNotNull();
        assertThat(result.size()).isNotPositive();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.ProductService#deleteProduct(java.lang.Long)}.
     */
    @Test
    void testDeleteProduct()
    {
        final Product savedProduct = this.productService.createProduct(this.product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isPositive();

        final Long id = savedProduct.getId();

        // Suppression du produit
        this.productService.deleteProduct(id);

        final List<Product> products = (List<Product>) this.productService.getProducts();

        assertThat(products).isNotEmpty();
        assertThat(products.size()).isEqualTo(14); // Le fichier data-test-h2.sql d'insertion d'enregistrements dans la table T_PRODUCTS contient déjà
                                                   // 14 enregistrements.
    }

    @Test
    void testDeleteProduct_WithNotExistId()
    {
        final Long NOT_EXIST_ID = Long.MAX_VALUE;

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.deleteProduct(NOT_EXIST_ID);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testDeleteProduct_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            // Suppression
            this.productService.deleteProduct(null);
        });

        final String expectedMessage = INVALID_DATA_ACCES_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.ProductService#updateProduct(java.lang.Long, fr.vincent.tuto.server.model.po.Product)}.
     */
    @Test
    void testUpdateProduct()
    {
        final Long EXIST_ID = 8L;
        final Optional<Product> optional = this.productService.getProductById(EXIST_ID);

        assertThat(optional).isPresent();
        final Product product = optional.get();
        product.setName("L2008902 Test");
        product.setDescription("Batterie de cuisine 10 pièces L2008902");

        final Long id = product.getId();
        this.productService.updateProduct(id, product);

        assertThat(product).isNotNull();
        assertThat(product.getName()).contains("L2008902 Test");
        assertThat(product.getDescription()).contains("Batterie de cuisine 10 pièces L2008902");
        assertThat(product.getQuantity()).isExactlyInstanceOf(Long.class);
        assertThat(product.getUnitPrice()).isExactlyInstanceOf(BigDecimal.class);
        assertThat(product.getPrice()).isExactlyInstanceOf(BigDecimal.class);
        assertThat(product.getImageUrl()).contains("img/");
        assertThat(product.getQuantity()).isEqualTo(5L);
        assertThat(product.getUnitPrice()).isEqualTo(new BigDecimal("5.54"));
    }

    @Test
    void testUpdateProduct_WithNotExistId()
    {
        final Long NOT_EXIST_ID = Long.MAX_VALUE;

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.updateProduct(NOT_EXIST_ID, this.product);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testUpdateProduct_WithNullId()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.updateProduct(null, this.product);
        });

        final String expectedMessage = INVALID_DATA_ACCES_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testUpdateProduct_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.updateProduct(null, null);
        });

        final String expectedMessage = INVALID_DATA_ACCES_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }
}
