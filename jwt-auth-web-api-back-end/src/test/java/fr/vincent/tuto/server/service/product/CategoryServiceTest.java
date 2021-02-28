/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : CategoryServiceTest.java
 * Date de création : 2 févr. 2021
 * Heure de création : 21:18:33
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
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
import fr.vincent.tuto.server.config.db.PersistanceContextConfig;
import fr.vincent.tuto.server.dao.CategoryDAO;
import fr.vincent.tuto.server.enumeration.CategoryTypeEnum;
import fr.vincent.tuto.server.model.po.Category;
import fr.vincent.tuto.server.model.po.Product;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests Unitaires des objets de ype {@link CategoryService}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-common-test.properties", "classpath:back-end-application-test.properties" })
@ContextConfiguration(name = "categoryServiceTest", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistanceContextConfig.class,
        ProductService.class, CategoryService.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CategoryServiceTest
{
    //
    private static final String SAVE_MSG = "Erreur lors de la sauvegarde en base de donnnées des informations d'une catégorie";
    private static final String SEARCH_BY_ID_MSG = "Erreur recherche des informations d'une catégorie de produits par identifiant";
    private static final String SEARCH_BY_NAME_MSG = "Erreur recherche des informations d'une catégorie de produits par son nom";

    @MockBean
    private CategoryDAO categoryDAO;
    @MockBean
    private ProductService productService;

    private CategoryService categoryService;
    private Category category;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        // instance du service avec les mock
        this.categoryService = new CategoryService(this.categoryDAO, this.productService);

        // instance de catégorie
        this.category = Category.builder()//
        .name("Nom de la catégorie de Test L2008902")//
        .description("Description catégorie de Test")//
        .enabled(Boolean.TRUE)//
        .categoryType(CategoryTypeEnum.ELCETROMENAGER)//
        .products(TestsDataUtils.ELECTROS())//
        .build();
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.categoryService = null;
        this.categoryDAO = null;
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#createCategory(fr.vincent.tuto.server.model.po.Category)}.
     */
    @Test
    void testCreateCategory()
    {
        final Category mockCategory = TestsDataUtils.CATEGORY_1;
        final Category categoryToSaved = TestsDataUtils.CATEGORY_1;

        //
        when(this.categoryDAO.save(categoryToSaved)).thenReturn(mockCategory);
        final Category savedCategory = this.categoryService.createCategory(categoryToSaved);

        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getId()).isNotNull();
        TestsDataUtils.assertAllCategories(mockCategory, savedCategory);
        verify(this.categoryDAO, times(1)).save(any(Category.class));
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
        final Optional<Category> optional = Optional.ofNullable(TestsDataUtils.CATEGORY_1);

        assertThat(optional).isPresent();
        final Category category = optional.get();
        category.setId(1L);

        final Long id = category.getId();
        when(this.categoryDAO.findById(id)).thenReturn(Optional.of(category));

        final Optional<Category> categoryFromDB = this.categoryService.getCategoryById(id);

        assertThat(categoryFromDB).isPresent();
        TestsDataUtils.assertAllCategories(category, categoryFromDB.get());

        verify(this.categoryDAO, times(1)).findById(any(Long.class));
    }

    @Test
    void testGetCategoryById_ShouldThrowException()
    {
        final Optional<Category> optional = Optional.ofNullable(TestsDataUtils.CATEGORY_1);

        assertThat(optional).isPresent();
        final Category category = optional.get();
        category.setId(1L);

        final Long id = category.getId();
        when(this.categoryDAO.findById(id)).thenReturn(Optional.empty());

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.getCategoryById(id);
        });
        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.categoryDAO, times(1)).findById(any(Long.class));
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.CategoryService#getCategoryByName(java.lang.String)}.
     */
    @Test
    void testGetCategoryByName()
    {
        final Optional<Category> optional = Optional.ofNullable(TestsDataUtils.CATEGORY_1);

        assertThat(optional).isPresent();
        final Category category = optional.get();
        category.setId(1L);

        final String name = category.getName();
        when(this.categoryDAO.findOneByName(name)).thenReturn(Optional.of(category));

        final Optional<Category> categoryFromDB = this.categoryService.getCategoryByName(name);

        assertThat(categoryFromDB).isPresent();
        TestsDataUtils.assertAllCategories(category, categoryFromDB.get());

        verify(this.categoryDAO, times(1)).findOneByName(any(String.class));
    }

    @Test
    void testGetCategoryByName_ShouldThrowException()
    {
        final Optional<Category> optional = Optional.ofNullable(TestsDataUtils.CATEGORY_1);

        assertThat(optional).isPresent();
        final Category category = optional.get();
        category.setId(1L);

        final String name = category.getName();
        when(this.categoryDAO.findOneByName(name)).thenReturn(Optional.empty());

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.getCategoryByName(name);
        });
        final String expectedMessage = SEARCH_BY_NAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.categoryDAO, times(1)).findOneByName(any(String.class));
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#getCategoryByNameIgnoreCase(java.lang.String)}.
     */
    @Test
    void testGetCategoryByNameIgnoreCase()
    {
        final Optional<Category> optional = Optional.ofNullable(TestsDataUtils.CATEGORY_1);

        assertThat(optional).isPresent();
        final Category category = optional.get();
        category.setId(1L);

        final String name = category.getName();
        when(this.categoryDAO.findOneByNameIgnoreCase(name)).thenReturn(Optional.of(category));

        final Optional<Category> categoryFromDB = this.categoryService.getCategoryByNameIgnoreCase(name);

        assertThat(categoryFromDB).isPresent();
        TestsDataUtils.assertAllCategories(category, categoryFromDB.get());

        verify(this.categoryDAO, times(1)).findOneByNameIgnoreCase(any(String.class));
    }

    @Test
    void testGetCategoryByNameIgnoreCase_ShouldThrowException()
    {
        final Optional<Category> optional = Optional.ofNullable(TestsDataUtils.CATEGORY_1);

        assertThat(optional).isPresent();
        final Category category = optional.get();
        category.setId(1L);

        final String name = category.getName();
        final String upperName = name.toUpperCase();
        when(this.categoryDAO.findOneByNameIgnoreCase(name)).thenReturn(Optional.empty());

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.getCategoryByNameIgnoreCase(upperName);
        });
        final String expectedMessage = SEARCH_BY_NAME_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.categoryDAO, times(1)).findOneByNameIgnoreCase(any(String.class));
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#existsCategoryByName(java.lang.String)}.
     */
    @Test
    void testExistsCategoryByName()
    {
        final Optional<Category> optional = Optional.ofNullable(TestsDataUtils.CATEGORY_1);

        assertThat(optional).isPresent();
        final Category category = optional.get();
        category.setId(1L);

        final String name = category.getName();
        when(this.categoryDAO.existsByName(name)).thenReturn(Boolean.TRUE);
        final Boolean exist = this.categoryService.existsCategoryByName(name);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isTrue();

        verify(this.categoryDAO, times(1)).existsByName(any(String.class));
    }

    @Test
    void testExistsCategoryByName_ShouldReturnFalse()
    {
        final Optional<Category> optional = Optional.ofNullable(TestsDataUtils.CATEGORY_1);

        assertThat(optional).isPresent();
        final Category category = optional.get();
        category.setId(1L);

        final String name = category.getName();
        when(this.categoryDAO.existsByName(name)).thenReturn(Boolean.FALSE);
        final Boolean exist = this.categoryService.existsCategoryByName(name);

        assertThat(exist).isNotNull();
        assertThat(exist.booleanValue()).isFalse();

        verify(this.categoryDAO, times(1)).existsByName(any(String.class));
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
        final List<Category> categories = TestsDataUtils.CATEGORIES();
        final Page<Category> page = new PageImpl<Category>(categories);

        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        when(this.categoryDAO.findAllByEnabled(Boolean.TRUE, paging)).thenReturn(page);
        final Page<Category> result = this.categoryService.getCategoriesByEnabled(Boolean.TRUE, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isEqualTo(5); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isEqualTo(5); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(1); // Le nombre total de pages
        assertThat(result.getContent().size()).isEqualTo(5); // La taille du contenu

        verify(this.categoryDAO, times(1)).findAllByEnabled(any(Boolean.class), any(Pageable.class));
    }

    @Test
    void testGetCategoriesByEnabledBooleanPageable_WithEmptyList()
    {
        final List<Category> categories = Collections.emptyList();
        final Page<Category> page = new PageImpl<Category>(categories);

        int pageNumber = 0; // zero-based page index, must NOT be negative.
        int pageSize = 5; // number of items in a page to be returned, must be greater than 0.
        Pageable paging = PageRequest.of(pageNumber, pageSize);

        when(this.categoryDAO.findAllByEnabled(Boolean.TRUE, paging)).thenReturn(page);
        final Page<Category> result = this.categoryService.getCategoriesByEnabled(Boolean.TRUE, paging);

        assertThat(result).isNotNull();
        assertThat(result.getNumber()).isNotPositive(); // Le monmbre
        assertThat(result.getNumberOfElements()).isNotPositive(); // Le nombre d'éléments de la page
        assertThat(result.getSize()).isNotPositive(); // La taille de la page
        assertThat(result.getTotalPages()).isEqualTo(1); // Le nombre total de pages
        assertThat(result.getContent().size()).isNotPositive(); // La taille du contenu

        verify(this.categoryDAO, times(1)).findAllByEnabled(any(Boolean.class), any(Pageable.class));
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#getCategoriesByEnabled(java.lang.Boolean)}.
     */
    @Test
    void testGetCategoriesByEnabledBoolean()
    {
        final List<Category> categories = TestsDataUtils.CATEGORIES();

        when(this.categoryDAO.findAllByEnabled(Boolean.TRUE)).thenReturn(categories);
        final List<Category> result = (List<Category>) this.categoryService.getCategoriesByEnabled(Boolean.TRUE);

        assertThat(result).isNotNull();
        assertThat(result.size()).isPositive();

        verify(this.categoryDAO, times(1)).findAllByEnabled(any(Boolean.class));
    }

    @Test
    void testGetCategoriesByEnabledBoolean_WithEmptyLis()
    {
        final List<Category> categories = Collections.emptyList();

        when(this.categoryDAO.findAllByEnabled(Boolean.TRUE)).thenReturn(categories);
        final List<Category> result = (List<Category>) this.categoryService.getCategoriesByEnabled(Boolean.TRUE);

        assertThat(result).isNotNull();
        assertThat(result.size()).isNotPositive();

        verify(this.categoryDAO, times(1)).findAllByEnabled(any(Boolean.class));
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.product.CategoryService#getCategories()}.
     */
    @Test
    void testGetCategories()
    {
        final List<Category> categories = TestsDataUtils.CATEGORIES();
        when(this.categoryDAO.findAll()).thenReturn(categories);
        final List<Category> result = (List<Category>) this.categoryService.getCategories();

        assertThat(result).isNotNull();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(5);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#getFilteredCategoriesByProductName(java.lang.String)}.
     */
    @Test
    void testGetFilteredCategoriesByProductName()
    {
        final List<Category> categories = TestsDataUtils.CATEGORIES();
        final String QUERY = "AUNA DS-2"; // AUNA DS-2, CORSAIR

        when(this.categoryDAO.findAll()).thenReturn(categories);
        when(this.categoryService.getCategories()).thenReturn(categories);

        final List<Category> result = (List<Category>) this.categoryService.getFilteredCategoriesByProductName(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testGetFilteredCategoriesByProductName_WithLowerCase()
    {
        final List<Category> categories = TestsDataUtils.CATEGORIES();
        final String QUERY = "auna ds-2"; // AUNA DS-2

        when(this.categoryDAO.findAll()).thenReturn(categories);
        when(this.categoryService.getCategories()).thenReturn(categories);

        final List<Category> result = (List<Category>) this.categoryService.getFilteredCategoriesByProductName(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void testGetFilteredCategoriesByProductName_NotMatchQuery()
    {
        final List<Category> categories = TestsDataUtils.CATEGORIES();
        final String QUERY = "PHILIPS";

        when(this.categoryDAO.findAll()).thenReturn(categories);
        when(this.categoryService.getCategories()).thenReturn(categories);

        final List<Category> result = (List<Category>) this.categoryService.getFilteredCategoriesByProductName(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isNotPositive();
    }

    @Test
    void testGetFilteredCategories_WithChar()
    {
        final List<Category> categories = TestsDataUtils.CATEGORIES();
        final String QUERY = "L";

        when(this.categoryDAO.findAll()).thenReturn(categories);
        when(this.categoryService.getCategories()).thenReturn(categories);

        final List<Category> result = (List<Category>) this.categoryService.getFilteredCategoriesByProductName(QUERY);

        assertThat(result).isNotNull();
        assertThat(result.size()).isPositive();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void testGetFilteredCategories_WithNull()
    {
        final List<Category> categories = TestsDataUtils.CATEGORIES();
        final String QUERY = null;

        when(this.categoryDAO.findAll()).thenReturn(categories);
        when(this.categoryService.getCategories()).thenReturn(categories);

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
        final Optional<Category> optional = Optional.ofNullable(TestsDataUtils.CATEGORY_1);

        assertThat(optional).isPresent();
        final Category category = optional.get();
        category.setId(1L);
        final Long id = category.getId();

        when(this.categoryDAO.findById(id)).thenReturn(Optional.of(category));
        final Optional<Category> categoryFromDB = this.categoryService.getCategoryById(id);

        assertThat(categoryFromDB).isPresent();
        TestsDataUtils.assertAllCategories(category, categoryFromDB.get());

        this.categoryService.deleteCategory(id);

        //
        verify(this.categoryDAO, times(2)).findById(any(Long.class));
        verify(this.categoryDAO, times(1)).delete(any(Category.class));
    }

    @Test
    void testDeleteCategory_ShouldThrowException()
    {
        final Optional<Category> optional = Optional.ofNullable(TestsDataUtils.CATEGORY_1);

        assertThat(optional).isPresent();
        final Category category = optional.get();
        category.setId(1L);
        final Long id = category.getId();
        when(this.categoryDAO.findById(id)).thenReturn(Optional.empty());

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.deleteCategory(id);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.categoryDAO, times(1)).findById(any(Long.class));
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.product.CategoryService#updateCategory(java.lang.Long, fr.vincent.tuto.server.model.po.Category)}.
     */
    @Test
    void testUpdateCategory()
    {
        final Optional<Category> optional = Optional.ofNullable(this.category);

        assertThat(optional).isPresent();
        final Category categoryToUpdated = optional.get();
        categoryToUpdated.setId(1L);
        categoryToUpdated.setName("Maj du Nom");
        categoryToUpdated.setDescription("Maj de la description");

        final Long id = categoryToUpdated.getId();

        when(this.categoryDAO.save(categoryToUpdated)).thenReturn(categoryToUpdated);
        when(this.categoryDAO.findById(id)).thenReturn(Optional.of(categoryToUpdated));

        final Optional<Category> categoryFromDB = this.categoryService.getCategoryById(id);

        assertThat(categoryFromDB).isPresent();

        this.categoryService.updateCategory(categoryFromDB.get().getId(), categoryToUpdated);

        assertThat(categoryToUpdated.getId()).isEqualTo(1L);
        assertThat(categoryToUpdated.getName()).isEqualTo("Maj du Nom");
        assertThat(categoryToUpdated.getDescription()).contains("Maj de la description");

        verify(this.categoryDAO, times(1)).save(any(Category.class));
        verify(this.categoryDAO, times(2)).findById(any(Long.class));
    }

    @Test
    void testUpdateCategory_ShouldThrowException()
    {
        final Optional<Category> optional = Optional.ofNullable(this.category);

        assertThat(optional).isPresent();
        final Category categoryToUpdated = optional.get();
        categoryToUpdated.setId(1L);
        categoryToUpdated.setName("Maj du Nom");
        categoryToUpdated.setDescription("Maj de la description");

        final Long id = categoryToUpdated.getId();
        when(this.categoryDAO.save(categoryToUpdated)).thenReturn(categoryToUpdated);
        when(this.categoryDAO.findById(id)).thenReturn(Optional.empty());

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.updateCategory(id, categoryToUpdated);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
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
        // produit : 1,7,4
        final Optional<Category> caOptional = Optional.ofNullable(this.category);
        final Optional<Product> proOptional = Optional.ofNullable(TestsDataUtils.PRODUCT_2);

        assertThat(caOptional).isPresent();
        assertThat(proOptional).isPresent();

        final Category category = caOptional.get();
        category.setId(1L);
        final Long categoryId = category.getId();

        final Product product = proOptional.get();
        product.setId(1L);
        final Long productId = product.getId();

        when(this.categoryDAO.findById(category.getId())).thenReturn(Optional.of(category));
        when(this.productService.getProductById(product.getId())).thenReturn(Optional.of(product));

        final List<Product> products = (List<Product>) this.categoryService.addProduct(categoryId, productId);

        assertThat(products).isNotNull();
        assertThat(products.size()).isEqualTo(2);

        verify(this.categoryDAO, times(1)).findById(any(Long.class));
    }

    @Test
    void testAddProduct_WithEmptyCategory()
    {
        final Optional<Category> caOptional = Optional.ofNullable(this.category);
        final Optional<Product> proOptional = Optional.ofNullable(TestsDataUtils.PRODUCT_2);

        assertThat(caOptional).isPresent();
        assertThat(proOptional).isPresent();

        final Category category = caOptional.get();
        category.setId(1L);
        final Long categoryId = category.getId();

        final Product product = proOptional.get();
        product.setId(1L);
        final Long productId = product.getId();

        when(this.categoryDAO.findById(category.getId())).thenReturn(Optional.empty());
        when(this.productService.getProductById(product.getId())).thenReturn(Optional.of(product));

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.categoryService.addProduct(categoryId, productId);
        });

        final String expectedMessage = SEARCH_BY_ID_MSG;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);

        verify(this.categoryDAO, times(1)).findById(any(Long.class));
    }

    @Test
    void testAddProduct_WithEmptyProduct()
    {
        final Optional<Category> caOptional = Optional.ofNullable(this.category);
        final Optional<Product> proOptional = Optional.ofNullable(TestsDataUtils.PRODUCT_2);

        assertThat(caOptional).isPresent();
        assertThat(proOptional).isPresent();

        final Category category = caOptional.get();
        category.setId(1L);
        final Long categoryId = category.getId();

        final Product product = proOptional.get();
        product.setId(1L);
        final Long productId = product.getId();

        when(this.categoryDAO.findById(category.getId())).thenReturn(Optional.of(category));
        when(this.productService.getProductById(product.getId())).thenReturn(Optional.empty());

        final List<Product> products = (List<Product>) this.categoryService.addProduct(categoryId, productId);

        assertThat(products).isNotNull();
        assertThat(products.size()).isNotPositive();

        verify(this.categoryDAO, times(1)).findById(any(Long.class));
    }
}
