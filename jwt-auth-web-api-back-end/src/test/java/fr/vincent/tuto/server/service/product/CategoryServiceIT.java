/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : CategoryServiceIT.java
 * Date de création : 3 févr. 2021
 * Heure de création : 18:10:55
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
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
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
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Sets;

import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistenceContextConfig;
import fr.vincent.tuto.server.enumeration.CategoryTypeEnum;
import fr.vincent.tuto.server.model.po.Category;
import fr.vincent.tuto.server.model.po.Product;
import fr.vincent.tuto.server.service.contract.ICategoryService;
import fr.vincent.tuto.server.util.ServerUtil;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests d'Intégration (composants et système) des objets de type {@link CategoryService}.
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-common-test.properties", "classpath:back-end-application-test.properties", "classpath:back-end-tls-test.properties" })
@ContextConfiguration(name = "categoryServiceIT", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistenceContextConfig.class, ProductService.class,
        CategoryService.class })
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("test")
@Sql(scripts = { "classpath:db/h2/drop-test-h2.sql", "classpath:db/h2/create-test-h2.sql", "classpath:db/h2/data-test-h2.sql" }, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@Transactional
class CategoryServiceIT
{
    private static final String SAVE_MSG = "Erreur lors de la sauvegarde en base de donnnées des informations d'une catégorie";
    private static final String SEARCH_BY_ID_MSG = "Erreur recherche des informations d'une catégorie de produits par identifiant";
    private static final String INVALID_DATA_ACCES_MSG = "The given id must not be null!";
    private static final String SEARCH_BY_NAME_MSG = "Erreur recherche des informations d'une catégorie de produits par son nom";
    private static final String SEARCH_PRODUCT_BY_ID_MSG = "Erreur recherche des informations d'un produit par identifiant";

    @Autowired
    private ICategoryService categoryService;
    private Category category;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        // instance de catégorie
        this.category = Category.builder()//
        .name("Nom de la catégorie de Test L2008902")//
        .description("Description catégorie de Test")//
        .enabled(Boolean.TRUE)//
        .categoryType(CategoryTypeEnum.ELCETROMENAGER)//
        .products(TestsDataUtils.ELECTROS_SAVED())//
        .build();
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.categoryService = null;
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#createCategory(fr.vincent.tuto.server.model.po.Category)}.
     */
    @Test
    void testCreateCategory()
    {
        final Category savedCategory = this.categoryService.createCategory(this.category);

        // System.err.println(">>>>> Identifiant catégorie insérée : \n" +savedCategory.getId());

        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isPositive();

        final List<Category> categories = (List<Category>) this.categoryService.getCategories();

        assertThat(categories).isNotEmpty();
        assertThat(categories.size()).isEqualTo(6); // Le fichier data-test-h2.sql d'insertion d'enregistrements dans la table T_CATEGORIES contient
                                                    // déjà 14 enregistrements.
        assertThat(savedCategory.getProducts().size()).isEqualTo(3);
    }

    @Test
    void testCreateCategory_ShouldThrowException()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.createCategory(null);
        });

        final String expectedMessage = SAVE_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.CategoryService#getCategoryById(java.lang.Long)}.
     */
    @Test
    void testGetCategoryById()
    {
        final Long EXIST_ID = 9L;
        final Optional<Category> optional = this.categoryService.getCategoryById(EXIST_ID);

        assertThat(optional).isPresent();
        final Category category = optional.get();

        assertThat(category).isNotNull();
        assertThat(category.getName()).contains("MEUBLES-DECO");
        assertThat(category.getDescription()).contains("Catégorie des produits MEUBLES-");
        assertThat(category.getEnabled()).isTrue();
        assertThat(category.getProducts().size()).isEqualTo(3);
    }

    @Test
    void testGetCategoryById_ShouldThrowException()
    {
        final Long NOT_EXIST_ID = Long.MAX_VALUE;

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.getCategoryById(NOT_EXIST_ID);
        });
        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetCategoryById_ShouldThrowExceptionWithNullId()
    {
        final Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.categoryService.getCategoryById(null);
        });
        final String expectedMessage = INVALID_DATA_ACCES_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.CategoryService#getCategoryByName(java.lang.String)}.
     */
    @Test
    void testGetCategoryByName()
    {
        final String EXIST_CATEGORY_NAME = "INFORMATIQUE";
        final Optional<Category> optional = this.categoryService.getCategoryByName(EXIST_CATEGORY_NAME);

        assertThat(optional).isPresent();
        final Category category = optional.get();

        assertThat(category).isNotNull();
        assertThat(category.getName()).contains("INFORMATIQUE");
        assertThat(category.getDescription()).contains("produits INFORMATIQUE");
        assertThat(category.getEnabled()).isTrue();
        assertThat(category.getProducts().size()).isEqualTo(3);
    }

    @Test
    void testGetCategoryByName_WithNotExistName()
    {
        final String NOT_EXIST_CATEGORY_NAME = "NOT_EXIST_CATEGORY_NAME";

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.getCategoryByName(NOT_EXIST_CATEGORY_NAME);
        });
        final String expectedMessage = SEARCH_BY_NAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetCategoryByName_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.getCategoryByName(null);
        });
        final String expectedMessage = SEARCH_BY_NAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#getCategoryByNameIgnoreCase(java.lang.String)}.
     */
    @Test
    void testGetCategoryByNameIgnoreCase()
    {
        final String EXIST_CATEGORY_NAME = "informatique";
        final Optional<Category> optional = this.categoryService.getCategoryByNameIgnoreCase(EXIST_CATEGORY_NAME);

        assertThat(optional).isPresent();
        final Category category = optional.get();

        assertThat(category).isNotNull();
        assertThat(category.getName()).contains("INFORMATIQUE");
        assertThat(category.getDescription()).contains("produits INFORMATIQUE");
        assertThat(category.getEnabled()).isTrue();
        assertThat(category.getProducts().size()).isEqualTo(3);
    }

    @Test
    void testGetCategoryByNameIgnoreCase_UpperCase()
    {
        final String EXIST_CATEGORY_NAME = "informatique";
        final String upperCase = ServerUtil.UPPER_CASE.apply(EXIST_CATEGORY_NAME);
        final Optional<Category> optional = this.categoryService.getCategoryByNameIgnoreCase(upperCase);

        assertThat(optional).isPresent();
        final Category category = optional.get();

        assertThat(category).isNotNull();
        assertThat(category.getName()).contains("INFORMATIQUE");
        assertThat(category.getDescription()).contains("produits INFORMATIQUE");
        assertThat(category.getEnabled()).isTrue();
        assertThat(category.getProducts().size()).isEqualTo(3);
    }

    @Test
    void testGetCategoryByNameIgnoreCase_WithNotExistName()
    {
        final String NOT_EXIST_CATEGORY_NAME = "not_exist_category_name";

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.getCategoryByNameIgnoreCase(NOT_EXIST_CATEGORY_NAME);
        });
        final String expectedMessage = SEARCH_BY_NAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetCategoryByNameIgnoreCase_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.getCategoryByNameIgnoreCase(null);
        });
        final String expectedMessage = SEARCH_BY_NAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#getCategoryWithProductsByNameIgnoreCase(java.lang.String)}.
     */
    @Test
    void testGetCategoryWithProductsByNameIgnoreCase()
    {
        final var EXIST_CATEGORY_NAME = "informatique";
        final var optional = this.categoryService.getCategoryWithProductsByNameIgnoreCase(EXIST_CATEGORY_NAME);

        assertThat(optional).isPresent();
        final var category = optional.get();

        assertThat(category).isNotNull();
        assertThat(category.getName()).contains("INFORMATIQUE");
        assertThat(category.getDescription()).contains("produits INFORMATIQUE");
        assertThat(category.getEnabled()).isTrue();
        assertThat(category.getProducts().size()).isEqualTo(3);
    }

    @Test
    void testGetCategoryWithProductsByNameIgnoreCase_UpperCase()
    {
        final var EXIST_CATEGORY_NAME = "informatique";
        final var upperCase = ServerUtil.UPPER_CASE.apply(EXIST_CATEGORY_NAME);
        final var optional = this.categoryService.getCategoryWithProductsByNameIgnoreCase(upperCase);

        assertThat(optional).isPresent();
        final var category = optional.get();

        assertThat(category).isNotNull();
        assertThat(category.getName()).contains("INFORMATIQUE");
        assertThat(category.getDescription()).contains("produits INFORMATIQUE");
        assertThat(category.getEnabled()).isTrue();
        assertThat(category.getProducts().size()).isEqualTo(3);
    }

    @Test
    void testGetCategoryWithProductsByNameIgnoreCase_WithNotExistName()
    {
        final var NOT_EXIST_CATEGORY_NAME = "not_exist_category_name";

        final var exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.getCategoryWithProductsByNameIgnoreCase(NOT_EXIST_CATEGORY_NAME);
        });
        final var expectedMessage = SEARCH_BY_NAME_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testGetCategoryWithProductsByNameIgnoreCase_WithNull()
    {
        final var exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.getCategoryByNameIgnoreCase(null);
        });
        final var expectedMessage = SEARCH_BY_NAME_MSG;
        final var actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#existsCategoryByName(java.lang.String)}.
     */
    @Test
    void testExistsCategoryByName()
    {
        final String EXIST_CATEGORY_NAME = "INFORMATIQUE";
        final Boolean exist = this.categoryService.existsCategoryByName(EXIST_CATEGORY_NAME);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isTrue();
    }

    @Test
    void testExistsCategoryByName_WithNotExistName()
    {
        final String NOT_EXIST_CATEGORY_NAME = "informatique";
        final Boolean exist = this.categoryService.existsCategoryByName(NOT_EXIST_CATEGORY_NAME);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();
    }

    @Test
    void testExistsCategoryByName_WithNull()
    {
        final Boolean exist = this.categoryService.existsCategoryByName(null);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#getCategoriesByEnabled(java.lang.Boolean, org.springframework.data.domain.Pageable)}.
     */
    @Test
    void testGetCategoriesByEnabledBooleanPageable()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        final Page<Category> result = this.categoryService.getCategoriesByEnabled(Boolean.TRUE, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isEqualTo(5); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(5); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(1); // Le nombre total de pages
        assertThat(result.getContent().size()).isEqualTo(5); // La taille du contenu
    }

    @Test
    void testGetCategoriesByEnabledBooleanPageable_False()
    {
        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);
        final Page<Category> result = this.categoryService.getCategoriesByEnabled(Boolean.FALSE, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isNotPositive(); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(5); // La taille de la page
        assertThat(result.getTotalPages()).isNotPositive(); // Le nombre total de pages
        assertThat(result.getContent().size()).isNotPositive(); // La taille du contenu
    }

    @Test
    void testGetCategoriesByEnabledBooleanPageable_WithNull()
    {
        final Page<Category> result = this.categoryService.getCategoriesByEnabled(null, null);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isNotPositive(); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isNotPositive(); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(1); // Le nombre total de pages
        assertThat(result.getContent().size()).isNotPositive(); // La taille du contenu
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#getCategoriesByEnabled(java.lang.Boolean)}.
     */
    @Test
    void testGetCategoriesByEnabledBoolean()
    {
        final List<Category> result = (List<Category>) this.categoryService.getCategoriesByEnabled(Boolean.TRUE);

        assertThat(result).isNotNull();
        assertThat(result.size()).isPositive();
    }

    @Test
    void testGetCategoriesByEnabledBoolean_False()
    {
        final List<Category> result = (List<Category>) this.categoryService.getCategoriesByEnabled(Boolean.FALSE);

        assertThat(result).isNotNull();
        assertThat(result.size()).isNotPositive();
    }

    @Test
    void testGetCategoriesByEnabledBoolean_WithNull()
    {
        final List<Category> result = (List<Category>) this.categoryService.getCategoriesByEnabled(null);

        assertThat(result).isNotNull();
        assertThat(result.size()).isNotPositive();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.CategoryService#getCategories()}.
     */
    @Test
    void testGetCategories()
    {
        final List<Category> result = (List<Category>) this.categoryService.getCategories();

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(5);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#getFilteredCategoriesByProductName(java.lang.String)}.
     */
    @Test
    void testGetFilteredCategoriesByProductName()
    {
        final List<Category> result = (List<Category>) this.categoryService.getFilteredCategoriesByProductName("AUNA DS-2");

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testGetFilteredCategoriesByProductName_WithLowerCase()
    {
        final String QUERY = "auna ds-2";
        final List<Category> result = (List<Category>) this.categoryService.getFilteredCategoriesByProductName(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testGetFilteredCategoriesByProductName_NotMatchQuery()
    {
        final String QUERY = "CP";
        final List<Category> result = (List<Category>) this.categoryService.getFilteredCategoriesByProductName(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isNotPositive();
    }

    @Test
    void testGetFilteredCategoriesByProductName_WithChar()
    {
        final String QUERY = "L";
        final List<Category> result = (List<Category>) this.categoryService.getFilteredCategoriesByProductName(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(5);
    }

    @Test
    void testGetFilteredCategoriesByProductName_WithNull()
    {
        final String QUERY = null;
        final List<Category> result = (List<Category>) this.categoryService.getFilteredCategoriesByProductName(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isNotPositive();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.CategoryService#deleteCategory(java.lang.Long)}.
     */
    @Test
    void testDeleteCategory()
    {
        final Set<Product> products = Sets.newHashSet();

        final Product product = Product.builder().id(8L).name("TEFAL L2008902")//
        .description("Batterie de cuisine 10 pièces Ingenio Essential - Tous feux sauf induction")//
        .quantity(5L).unitPrice(new BigDecimal("5.539")).price(new BigDecimal("55.39")).imageUrl("img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg").build();

        products.add(product);

        final Category categoryInternal = Category.builder()//
        .name("Catégorie de Test L2008902")//
        .description("Description Test")//
        .enabled(Boolean.TRUE)//
        .categoryType(CategoryTypeEnum.ELCETROMENAGER)//
        .products(products)//
        .build();

        final Category savedCategory = this.categoryService.createCategory(categoryInternal);

        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isPositive();

        this.categoryService.deleteCategory(savedCategory.getId());

        final List<Category> categories = (List<Category>) this.categoryService.getCategories();

        assertThat(categories.size()).isEqualTo(5); // Le fichier data-test-h2.sql d'insertion d'enregistrements dans la
                                                    // table T_CATEGORIES contient déjà 14 enregistrements.
    }

    @Test
    void testDeleteCategory_ShouldThrowException()
    {
        final Long NOT_EXIST_ID = Long.MAX_VALUE;
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.deleteCategory(NOT_EXIST_ID);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testDeleteCategory_ShouldThrowExceptionWithNullId()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.deleteCategory(null);
        });

        final String expectedMessage = INVALID_DATA_ACCES_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#updateCategory(java.lang.Long, fr.vincent.tuto.server.model.po.Category)}.
     */
    @Test
    void testUpdateCategory()
    {
        final Set<Product> products = Sets.newHashSet();

        final Product product = Product.builder().id(8L).name("TEFAL L2008902")//
        .description("Batterie de cuisine 10 pièces Ingenio Essential - Tous feux sauf induction")//
        .quantity(5L).unitPrice(new BigDecimal("5.539")).price(new BigDecimal("55.39")).imageUrl("img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg").build();

        products.add(product);

        final Category categoryInternal = Category.builder()//
        .name("Catégorie L2008902")//
        .description("Description L2008902")//
        .enabled(Boolean.TRUE)//
        .categoryType(CategoryTypeEnum.ELCETROMENAGER)//
        .products(products)//
        .build();

        final Category savedCategory = this.categoryService.createCategory(categoryInternal);
        savedCategory.setName("Maj Catégorie L2008902");
        savedCategory.setDescription("Maj description L2008902");
        savedCategory.setEnabled(Boolean.FALSE);

        this.categoryService.updateCategory(savedCategory.getId(), savedCategory);

        assertThat(savedCategory.getId()).isPositive();
        assertThat(savedCategory.getName()).isEqualTo("Maj Catégorie L2008902");
        assertThat(savedCategory.getDescription()).contains("Maj description L2008902");
    }

    @Test
    void testUpdateCategory_ShouldThrowExceptionWithNullId()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.updateCategory(null, this.category);
        });

        final String expectedMessage = INVALID_DATA_ACCES_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testUpdateCategory_ShouldThrowExceptionWithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.updateCategory(null, null);
        });

        final String expectedMessage = INVALID_DATA_ACCES_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#addProduct(java.lang.Long, java.lang.Long)}.
     */
    @Test
    void testAddProduct()
    {
        final Long categoryId = 7L;
        final Long productId = 23L;

        final List<Product> products = (List<Product>) this.categoryService.addProduct(categoryId, productId);

        assertThat(products).isNotEmpty();
        assertThat(products.size()).isEqualTo(4);
    }

    @Test
    void testAddProduct_WithNotExistCategoryId()
    {
        final Long categoryId = Long.MAX_VALUE;
        final Long productId = 7L;

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.addProduct(categoryId, productId);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testAddProduct_WithNotExistProductId()
    {
        final Long categoryId = 7L;
        final Long productId = Long.MAX_VALUE;

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.addProduct(categoryId, productId);
        });

        final String expectedMessage = SEARCH_PRODUCT_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testAddProduct_WithNullCategoryId()
    {
        final Long productId = 7L;
        final Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.categoryService.addProduct(null, productId);
        });

        final String expectedMessage = INVALID_DATA_ACCES_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testAddProduct_WithNullProductId()
    {
        final Long categoryId = 7L;
        final Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.categoryService.addProduct(categoryId, null);
        });

        final String expectedMessage = INVALID_DATA_ACCES_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testAddProduct_WithNull()
    {
        final Exception exception = assertThrows(InvalidDataAccessApiUsageException.class, () -> {
            this.categoryService.addProduct(null, null);
        });

        final String expectedMessage = INVALID_DATA_ACCES_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }
}
