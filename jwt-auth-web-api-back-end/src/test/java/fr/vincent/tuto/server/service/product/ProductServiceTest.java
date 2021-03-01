/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ProductServiceTest.java
 * Date de création : 31 janv. 2021
 * Heure de création : 09:36:26
 * Package : fr.vincent.tuto.server.service.product
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistenceContextConfig;
import fr.vincent.tuto.server.dao.ProductDAO;
import fr.vincent.tuto.server.model.po.Product;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests Unitaires des objets de type {@link ProductService}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-common-test.properties", "classpath:back-end-application-test.properties", "classpath:back-end-tls-test.properties" })
@ContextConfiguration(name = "productServiceTest", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistenceContextConfig.class,
        ProductService.class })
@SpringBootTest
@ActiveProfiles("test")
class ProductServiceTest
{
    private static final String SEARCH_BY_NAME_MSG = "Erreur recherche des informations d'un produit par son nom";
    private static final String SEARCH_BY_ID_MSG = "Erreur recherche des informations d'un produit par identifiant";
    private static final String SAVE_MSG = "Erreur lors de la sauvegarde en base de donnnées des informations d'un produits";

    @MockBean
    private ProductDAO productDAO;

    private ProductService productService;
    private Product product;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        // Instance de ProductService
        this.productService = new ProductService(this.productDAO);

        // Instance de Product
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
        final Product mockProduct = TestsDataUtils.PRODUCT_1;
        final Product productToSaved = TestsDataUtils.PRODUCT_1;

        when(this.productDAO.save(productToSaved)).thenReturn(mockProduct);
        final Product savedProduct = this.productService.createProduct(productToSaved);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();
        TestsDataUtils.assertAllProduct(mockProduct, savedProduct);
        verify(this.productDAO, times(1)).save(any(Product.class));
    }

    @Test
    void testCreateProduct_ShouldThrowException()
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
        final Optional<Product> optional = Optional.ofNullable(TestsDataUtils.PRODUCT_1);

        assertThat(optional).isPresent();
        final Product product = optional.get();
        product.setId(1L);

        final Long id = product.getId();
        when(this.productDAO.findById(id)).thenReturn(Optional.of(product));
        final Optional<Product> productFromDB = this.productService.getProductById(id);

        assertThat(productFromDB).isPresent();
        TestsDataUtils.assertAllProduct(product, productFromDB.get());

        verify(this.productDAO, times(1)).findById(any(Long.class));
    }

    @Test
    void testGetProductById_ShouldThrowException()
    {
        final Optional<Product> optional = Optional.ofNullable(TestsDataUtils.PRODUCT_1);

        assertThat(optional).isPresent();
        final Product product = optional.get();
        product.setId(1L);

        final Long id = product.getId();
        when(this.productDAO.findById(id)).thenReturn(Optional.empty());
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.getProductById(id);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.productDAO, times(1)).findById(any(Long.class));
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.ProductService#getProductByName(java.lang.String)}.
     */
    @Test
    void testGetProductByName()
    {
        final Optional<Product> optional = Optional.ofNullable(TestsDataUtils.PRODUCT_1);

        assertThat(optional).isPresent();
        final Product product = optional.get();
        product.setId(1L);

        final String name = product.getName();
        when(this.productDAO.findOneByName(name)).thenReturn(Optional.of(product));
        final Optional<Product> productFromDB = this.productService.getProductByName(name);

        assertThat(productFromDB).isPresent();
        TestsDataUtils.assertAllProduct(product, productFromDB.get());

        verify(this.productDAO, times(1)).findOneByName(any(String.class));
    }

    @Test
    void testGetProductByName_ShouldThrowException()
    {
        final Optional<Product> optional = Optional.ofNullable(TestsDataUtils.PRODUCT_1);

        assertThat(optional).isPresent();
        final Product product = optional.get();
        product.setId(1L);

        final String name = product.getName();
        when(this.productDAO.findOneByName(name)).thenReturn(Optional.empty());
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.getProductByName(name);
        });

        final String expectedMessage = SEARCH_BY_NAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.productDAO, times(1)).findOneByName(any(String.class));
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.ProductService#getProductByNameIgnoreCase(java.lang.String)}.
     */
    @Test
    void testGetProductByNameIgnoreCase()
    {
        final Optional<Product> optional = Optional.ofNullable(TestsDataUtils.PRODUCT_1);

        assertThat(optional).isPresent();
        final Product product = optional.get();
        product.setId(1L);

        final String name = product.getName();
        when(this.productDAO.findOneByNameIgnoreCase(name.toUpperCase())).thenReturn(Optional.of(product));
        final Optional<Product> productFromDB = this.productService.getProductByNameIgnoreCase(name.toUpperCase());

        assertThat(productFromDB).isPresent();
        TestsDataUtils.assertAllProduct(product, productFromDB.get());

        verify(this.productDAO, times(1)).findOneByNameIgnoreCase(any(String.class));
    }

    @Test
    void testGetProductByNameIgnoreCase_ShouldThrowException()
    {
        final Optional<Product> optional = Optional.ofNullable(TestsDataUtils.PRODUCT_1);

        assertThat(optional).isPresent();
        final Product product = optional.get();
        product.setId(1L);

        final String name = product.getName();
        final String upperName = name.toUpperCase();
        when(this.productDAO.findOneByNameIgnoreCase(upperName)).thenReturn(Optional.empty());
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.getProductByNameIgnoreCase(upperName);
        });

        final String expectedMessage = SEARCH_BY_NAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.productDAO, times(1)).findOneByNameIgnoreCase(any(String.class));
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.ProductService#existsProductByName(java.lang.String)}.
     */
    @Test
    void testExistsProductByName()
    {
        final Optional<Product> optional = Optional.ofNullable(TestsDataUtils.PRODUCT_1);

        assertThat(optional).isPresent();
        final Product product = optional.get();
        product.setId(1L);

        final String name = product.getName();
        when(this.productDAO.existsByName(name)).thenReturn(Boolean.TRUE);
        final Boolean exist = this.productService.existsProductByName(name);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isTrue();

        verify(this.productDAO, times(1)).existsByName(any(String.class));
    }

    @Test
    void testExistsProductByName_ShouldReturnFalse()
    {
        final Optional<Product> optional = Optional.ofNullable(TestsDataUtils.PRODUCT_1);

        assertThat(optional).isPresent();
        final Product product = optional.get();
        product.setId(1L);

        final String name = product.getName() + " Test";
        when(this.productDAO.existsByName(name)).thenReturn(Boolean.FALSE);
        final Boolean exist = this.productService.existsProductByName(name);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();

        verify(this.productDAO, times(1)).existsByName(any(String.class));
    }

    @Test
    void testExistsProductByName_WithNull()
    {
        final Boolean exist = this.productService.existsProductByName(null);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.ProductService#getProductsByIsActive(java.lang.Boolean, org.springframework.data.domain.Pageable)}.
     */
    @Test
    void testGetProductsByIsActiveBooleanPageable()
    {
        final List<Product> products = TestsDataUtils.PRODUCTS();
        final Page<Product> page = new PageImpl<Product>(products);

        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        when(this.productDAO.findAllByIsActive(Boolean.TRUE, paging)).thenReturn(page);
        final Page<Product> result = this.productService.getProductsByIsActive(Boolean.TRUE, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isEqualTo(14); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(14); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(1); // Le nombre total de pages
        assertThat(result.getContent().size()).isEqualTo(14); // La taille du contenu

        verify(this.productDAO, times(1)).findAllByIsActive(any(Boolean.class), any(Pageable.class));
    }

    @Test
    void testGetProductsByIsActiveBooleanPageable_WithEmptyList()
    {
        final List<Product> products = Collections.emptyList();
        final Page<Product> page = new PageImpl<Product>(products);

        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        when(this.productDAO.findAllByIsActive(Boolean.TRUE, paging)).thenReturn(page);
        final Page<Product> result = this.productService.getProductsByIsActive(Boolean.TRUE, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isNotPositive(); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isNotPositive(); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(1); // Le nombre total de pages
        assertThat(result.getContent().size()).isNotPositive(); // La taille du contenu

        verify(this.productDAO, times(1)).findAllByIsActive(any(Boolean.class), any(Pageable.class));
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.ProductService#getProductsByIsActive(java.lang.Boolean)}.
     */
    @Test
    void testGetProductsByIsActiveBoolean()
    {
        final List<Product> products = TestsDataUtils.PRODUCTS();

        when(this.productDAO.findAllByIsActive(Boolean.TRUE)).thenReturn(products);
        final List<Product> result = (List<Product>) this.productService.getProductsByIsActive(Boolean.TRUE);

        assertThat(result).isNotNull();
        assertThat(result.size()).isPositive();

        verify(this.productDAO, times(1)).findAllByIsActive(any(Boolean.class));
    }

    @Test
    void testGetProductsByIsActiveBoolean_WithEmptyList()
    {
        final List<Product> products = Collections.emptyList();

        when(this.productDAO.findAllByIsActive(Boolean.TRUE)).thenReturn(products);
        final List<Product> result = (List<Product>) this.productService.getProductsByIsActive(Boolean.TRUE);

        assertThat(result).isNotNull();
        assertThat(result.size()).isNotPositive();

        verify(this.productDAO, times(1)).findAllByIsActive(any(Boolean.class));
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.ProductService#getProducts()}.
     */
    @Test
    void testGetProducts()
    {
        final List<Product> products = TestsDataUtils.PRODUCTS();
        when(this.productDAO.findAll()).thenReturn(products);
        final List<Product> result = (List<Product>) this.productService.getProducts();

        assertThat(result).isNotNull();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(14);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.ProductService#getFilteredProducts(java.lang.String)}.
     */

    @Test
    void testGetFilteredProducts()
    {
        final List<Product> products = TestsDataUtils.PRODUCTS();
        final String QUERY = "PHILIPS";

        when(this.productDAO.findAll()).thenReturn(products);
        when(this.productService.getProducts()).thenReturn(products);
        final List<Product> result = (List<Product>) this.productService.getFilteredProducts(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testGetFilteredProducts_WithChar()
    {
        final List<Product> products = TestsDataUtils.PRODUCTS();
        products.add(product);
        final String QUERY = "L";

        when(this.productDAO.findAll()).thenReturn(products);
        when(this.productService.getProducts()).thenReturn(products);
        final List<Product> result = (List<Product>) this.productService.getFilteredProducts(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void testGetFilteredProducts_WithNull()
    {
        final List<Product> products = TestsDataUtils.PRODUCTS();
        products.add(product);
        final String QUERY = null;

        when(this.productDAO.findAll()).thenReturn(products);
        when(this.productService.getProducts()).thenReturn(products);
        final List<Product> result = (List<Product>) this.productService.getFilteredProducts(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isNotPositive();
    }

    @Test
    void testGetFilteredProducts_Mixte()
    {
        final List<Product> products = TestsDataUtils.PRODUCTS();
        products.add(product);
        final String QUERY = "L2008902";

        when(this.productDAO.findAll()).thenReturn(products);
        when(this.productService.getProducts()).thenReturn(products);
        final List<Product> result = (List<Product>) this.productService.getFilteredProducts(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void testGetFilteredProducts_LowerCase()
    {
        final List<Product> products = TestsDataUtils.PRODUCTS();
        final String QUERY = "philips";

        when(this.productDAO.findAll()).thenReturn(products);
        when(this.productService.getProducts()).thenReturn(products);
        final List<Product> result = (List<Product>) this.productService.getFilteredProducts(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testGetFilteredProducts_NotMatchQuery()
    {
        final List<Product> products = TestsDataUtils.PRODUCTS();
        final String QUERY = "Aspirateur";

        when(this.productDAO.findAll()).thenReturn(products);
        when(this.productService.getProducts()).thenReturn(products);
        final List<Product> result = (List<Product>) this.productService.getFilteredProducts(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isNotPositive();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.ProductService#deleteProduct(java.lang.Long)}.
     */
    @Test
    void testDeleteProduct()
    {
        final Optional<Product> optional = Optional.ofNullable(TestsDataUtils.PRODUCT_1);

        assertThat(optional).isPresent();
        final Product product = optional.get();
        product.setId(1L);

        final Long id = product.getId();
        when(this.productDAO.findById(id)).thenReturn(Optional.of(product));
        final Optional<Product> productFromDB = this.productService.getProductById(id);

        assertThat(productFromDB).isPresent();
        TestsDataUtils.assertAllProduct(product, productFromDB.get());

        this.productService.deleteProduct(id);

        // qsds
        verify(this.productDAO, times(2)).findById(any(Long.class));
        verify(this.productDAO, times(1)).delete(any(Product.class));
    }

    @Test
    void testDeleteProduct_ShouldThrowException()
    {
        final Optional<Product> optional = Optional.ofNullable(TestsDataUtils.PRODUCT_1);

        assertThat(optional).isPresent();
        final Product product = optional.get();
        product.setId(1L);

        final Long id = product.getId();
        when(this.productDAO.findById(id)).thenReturn(Optional.empty());
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.deleteProduct(id);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.productDAO, times(1)).findById(any(Long.class));
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.ProductService#updateProduct(java.lang.Long, fr.vincent.tuto.server.model.po.Product)}.
     */
    @Test
    void testUpdateProduct()
    {
        final Optional<Product> optional = Optional.ofNullable(this.product);

        assertThat(optional).isPresent();
        final Product productToUpdated = optional.get();
        productToUpdated.setId(1L);
        productToUpdated.setName("Maj du Nom");
        productToUpdated.setImageUrl("Image Test");

        final Long id = productToUpdated.getId();

        when(this.productDAO.save(productToUpdated)).thenReturn(productToUpdated);
        when(this.productDAO.findById(id)).thenReturn(Optional.of(productToUpdated));
        final Optional<Product> productFromDB = this.productService.getProductById(id);

        assertThat(productFromDB).isPresent();

        this.productService.updateProduct(productFromDB.get().getId(), productToUpdated);

        assertThat(productToUpdated.getId()).isEqualTo(1L);
        assertThat(productToUpdated.getName()).isEqualTo("Maj du Nom");
        assertThat(productToUpdated.getImageUrl()).contains("Image");

        verify(this.productDAO, times(1)).save(any(Product.class));
        verify(this.productDAO, times(2)).findById(any(Long.class));
    }

    @Test
    void testUpdateProduct_ShouldThrowException()
    {
        final Optional<Product> optional = Optional.ofNullable(TestsDataUtils.PRODUCT_1);

        assertThat(optional).isPresent();
        final Product productToUpdated = optional.get();
        productToUpdated.setId(1L);

        final Long id = product.getId();

        when(this.productDAO.save(productToUpdated)).thenReturn(productToUpdated);
        when(this.productDAO.findById(id)).thenReturn(Optional.empty());
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.productService.updateProduct(id, productToUpdated);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }
}
