/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : CategoryMapperTest.java
 * Date de création : 8 févr. 2021
 * Heure de création : 21:35:44
 * Package : fr.vincent.tuto.server.mapper
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistanceConfig;
import fr.vincent.tuto.server.model.dto.CategoryDTO;
import fr.vincent.tuto.server.model.po.Category;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe ds Tests d'intégration composants des objets de type {@link CategoryMapper}.
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-test.properties", "classpath:back-end-application-test.properties" })
@ContextConfiguration(name = "categoryMapperTest", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistanceConfig.class,
        ProductMapper.class, CategoryMapper.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CategoryMapperTest
{
    @Autowired
    private CategoryMapper categoryMapper;

    private CategoryDTO categoryDTO;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        this.categoryMapper.afterPropertiesSet();
        //
        this.categoryDTO = TestsDataUtils.CATEGORY_DTO;
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.categoryMapper = null;
        this.categoryDTO = null;
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.mapper.CategoryMapper#toSourceObject(fr.vincent.tuto.server.model.dto.CategoryDTO)}.
     */
    @Test
    void testToSourceObjectCategoryDTO()
    {
        final Category category = this.categoryMapper.toSourceObject(this.categoryDTO);

        assertThat(category).isNotNull();
        assertThat(category.getProducts().size()).isEqualTo(3);
        TestsDataUtils.assertAllCategories(TestsDataUtils.CATEGORY, category);
    }

    @Test
    void testToSourceObjectCategoryDTO_ShouldReturnNull()
    {
        final Category category = this.categoryMapper.toSourceObject(null);

        assertThat(category).isNull();
    }
    

    /**
     * Test method for {@link fr.vincent.tuto.server.service.mapper.CategoryMapper#toCategories(java.util.Collection)}.
     */
    @Test
    void testToCategories()
    {
        final List<CategoryDTO> dtos = TestsDataUtils.CATEGORIES_DTO();
        final List<Category> categories = (List<Category>) this.categoryMapper.toCategories(dtos);
        
        assertThat(categories).isNotEmpty();
        assertThat(categories.size()).isPositive();
        assertThat(categories.size()).isEqualTo(2);
        assertThat(categories).contains(TestsDataUtils.CATEGORY);
        TestsDataUtils.assertCategoryAndCategoryDTO(categories.get(0), dtos.get(0));
    }

    @Test
    void testToCategories_WithNull()
    {
        final List<Category> categories = (List<Category>) this.categoryMapper.toCategories(null);
        
        assertThat(categories).isEmpty();
        assertThat(categories.size()).isNotPositive();
    }
    
    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.mapper.CategoryMapper#toDestObject(fr.vincent.tuto.server.model.po.Category)}.
     */
    @Test
    void testToDestObjectCategory()
    {
        final CategoryDTO dto = this.categoryMapper.toDestObject(TestsDataUtils.CATEGORY);

        assertThat(dto).isNotNull();
        assertThat(dto.getProducts().size()).isPositive();
        TestsDataUtils.assertCategoryAndCategoryDTO(TestsDataUtils.CATEGORY, dto);
    }

    @Test
    void testToDestObjectCategory_WithNull()
    {
        final CategoryDTO dto = this.categoryMapper.toDestObject(null);

        assertThat(dto).isNull();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.mapper.CategoryMapper#toCategoryDtos(java.util.Collection)}.
     */
    @Test
    void testToCategoryDtos()
    {
        final List<Category> categories = TestsDataUtils.CATEGORIES_WITH_ID();
        final List<CategoryDTO> dtos = (List<CategoryDTO>) this.categoryMapper.toCategoryDtos(categories);

        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isPositive();
        assertThat(dtos.size()).isEqualTo(5);
        assertThat(dtos).contains(TestsDataUtils.CATEGORY_DTO);
        TestsDataUtils.assertCategoryAndCategoryDTO(categories.get(0), dtos.get(0));
    }

    @Test
    void testToCategoryDtos_WithNull()
    {
        final List<CategoryDTO> dtos = (List<CategoryDTO>) this.categoryMapper.toCategoryDtos(null);

        assertThat(dtos).isEmpty();
        assertThat(dtos.size()).isNotPositive();
    }



}
