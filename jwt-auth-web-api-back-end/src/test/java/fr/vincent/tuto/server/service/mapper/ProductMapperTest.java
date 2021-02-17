/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ProductMapperTest.java
 * Date de création : 7 févr. 2021
 * Heure de création : 15:58:48
 * Package : fr.vincent.tuto.server.mapper
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.assertj.core.util.Sets;
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

import com.google.common.collect.Lists;

import fr.vincent.tuto.common.service.props.DatabasePropsService;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.config.db.PersistanceConfig;
import fr.vincent.tuto.server.constants.ServerConstants;
import fr.vincent.tuto.server.enumeration.CategoryTypeEnum;
import fr.vincent.tuto.server.model.dto.ProductDTO;
import fr.vincent.tuto.server.model.po.Product;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests Unitaires des objets de type {@link ProductMapper}
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-test.properties", "classpath:back-end-application-test.properties" })
@ContextConfiguration(name = "productMapperTest", classes = { BackEndServerRootConfig.class, DatabasePropsService.class, PersistanceConfig.class,
        ProductMapper.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProductMapperTest
{
    @Autowired
    private ProductMapper productMapper;

    private Product product;
    private ProductDTO dto;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        this.productMapper.afterPropertiesSet();
        
        // Instance de Product
        this.product = Product.builder()//
        .id(10L)//
        .name("Nom produit de Test L2008902")//
        .description("Description produit de Test") //
        .quantity(1L)//
        .unitPrice(new BigDecimal("10.00"))//
        .price(new BigDecimal("10.00"))//
        .imageUrl("img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg") //
        .isActive(Boolean.TRUE)//
        .build();

        //
        this.dto = ProductDTO.builder()//
        .id(10L)//
        .name("Nom produit de Test L2008902 du DTO")//
        .description("Description produit de Test du DTO") //
        .quantity(2L)//
        .unitPrice(new BigDecimal("10.00"))//
        .price(new BigDecimal("20.00"))//
        .imageUrl("img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg") //
        .isActive(Boolean.TRUE)//
        .build();
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.productMapper = null;
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.mapper.ProductMapper#toDestObject(fr.vincent.tuto.server.model.po.Product)}.
     */
    @Test
    void testToDestObjectProduct()
    {
        final Long quantite = this.product.getQuantity();
        final BigDecimal prixUnitaire = this.product.getUnitPrice();
        final BigDecimal quantiteBig = new BigDecimal(quantite.longValue());

        final BigDecimal total = quantiteBig.multiply(prixUnitaire);

        assertThat(total).isNotNull();

        final ProductDTO dto = this.productMapper.toDestObject(this.product);

        assertThat(dto).isNotNull();
        TestsDataUtils.assertProductAndProductDTO(this.product, dto);
    }

    @Test
    void testToDestObjectProduct_WithNullField()
    {
        final Product productInternal = Product.builder()//
        .id(null)//
        .name(null)//
        .description("Description produit de Test") //
        .quantity(1L)//
        .unitPrice(new BigDecimal("10.00"))//
        .price(new BigDecimal("10.00"))//
        .imageUrl("img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg") //
        .isActive(Boolean.TRUE)//
        .build();

        final ProductDTO dto = this.productMapper.toDestObject(productInternal);

        assertThat(dto).isNotNull();
        TestsDataUtils.assertProductAndProductDTO(productInternal, dto);
    }

    @Test
    void testToDestObjectProduct_WithNull()
    {
        final ProductDTO dto = this.productMapper.toDestObject(null);

        assertThat(dto).isNull();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.service.mapper.ProductMapper#toSourceObject(fr.vincent.tuto.server.model.dto.ProductDTO)}.
     */
    @Test
    void testToSourceObjectProductDTO()
    {
        final Product product = this.productMapper.toSourceObject(this.dto);

        assertThat(product).isNotNull();
        TestsDataUtils.assertProductAndProductDTO(product, this.dto);
    }

    @Test
    void testToSourceObjectProductDTO_WithNullField()
    {
        final ProductDTO dtoInternal = ProductDTO.builder()//
        .id(null)//
        .name(null)//
        .description("Description produit de Test du DTO") //
        .quantity(2L)//
        .unitPrice(new BigDecimal("10.00"))//
        .price(new BigDecimal("20.00"))//
        .imageUrl("img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg") //
        .isActive(Boolean.TRUE)//
        .build();

        final Product product = this.productMapper.toSourceObject(dtoInternal);

        assertThat(product).isNotNull();
        TestsDataUtils.assertProductAndProductDTO(product, dtoInternal);
    }

    @Test
    void testToSourceObjectProductDTO_WithNull()
    {
        final Product product = this.productMapper.toSourceObject(null);

        assertThat(product).isNull();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.mapper.ProductMapper#toProductsDtos(java.util.Collection)}.
     */
    @Test
    void testToDtos()
    {
        final List<Product> products = Lists.newArrayList();
        products.add(this.product);
        products.add(this.product);
        products.add(this.product);
        products.add(this.product);
        products.add(null);

        final List<ProductDTO> dtos = (List<ProductDTO>) this.productMapper.toProductsDtos(products);

        assertThat(dtos).isNotEmpty();
        assertThat(dtos.get(0).toString()).isNotEmpty();
        assertThat(dtos.size()).isEqualTo(4);
    }

    @Test
    void testToDtos_WithNull()
    {
        final List<ProductDTO> dtos = (List<ProductDTO>) this.productMapper.toProductsDtos(null);

        assertThat(dtos).isEmpty();
        assertThat(dtos.size()).isNotPositive();
    }

    /**
     * Test method for {@link fr.vincent.tuto.server.service.mapper.ProductMapper#toProducts(java.util.Collection)}.
     */
    @Test
    void testToProducts()
    {
        final List<ProductDTO> dtos = Lists.newArrayList();
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(null);

        final List<Product> products = (List<Product>) this.productMapper.toProducts(dtos);

        assertThat(products).isNotEmpty();
        assertThat(products.get(0).toString()).isNotEmpty();
        assertThat(products.size()).isEqualTo(4);
    }

    @Test
    void testToProducts_WithSet()
    {
        //
        final ProductDTO dto1 = ProductDTO.builder()//
        .id(11L)//
        .name("Nom produit de Test L2008902 du DTO 1")//
        .description("Description produit de Test du DTO 1") //
        .quantity(2L)//
        .unitPrice(new BigDecimal("10.00"))//
        .price(new BigDecimal("20.00"))//
        .imageUrl("img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg") //
        .isActive(Boolean.TRUE)//
        .build();

        final ProductDTO dto2 = ProductDTO.builder()//
        .id(12L)//
        .name("Nom produit de Test L2008902 du DTO 2")//
        .description("Description produit de Test du DTO 2") //
        .quantity(2L)//
        .unitPrice(new BigDecimal("10.00"))//
        .price(new BigDecimal("20.00"))//
        .imageUrl("img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg") //
        .isActive(Boolean.TRUE)//
        .build();

        final ProductDTO dto3 = ProductDTO.builder()//
        .id(13L)//
        .name("Nom produit de Test L2008902 du DTO 3")//
        .description("Description produit de Test du DTO 3") //
        .quantity(2L)//
        .unitPrice(new BigDecimal("10.00"))//
        .price(new BigDecimal("20.00"))//
        .imageUrl("img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg") //
        .isActive(Boolean.TRUE)//
        .build();

        final Set<ProductDTO> dtos = Sets.newHashSet();
        dtos.add(this.dto);
        dtos.add(dto1);
        dtos.add(dto2);
        dtos.add(dto3);
        dtos.add(null);

        final List<Product> products = (List<Product>) this.productMapper.toProducts(dtos); // List
        final Set<Product> set = ServerConstants.listToSet(products); // Set

        assertThat(products).isNotEmpty();
        assertThat(products.size()).isEqualTo(4);
        assertThat(set.size()).isEqualTo(4);
    }

    @Test
    void testToProducts_WithNull()
    {
        // System.err.println(">>>>>>> Le Contenu : \n" + CategoryTypeEnum.valueOf("JEUX-VIDEO"));
        System.err.println(">>>>>>> Le Contenant : \n" + CategoryTypeEnum.valueOf("JEUX_VIDEO").name());

        final List<Product> products = (List<Product>) this.productMapper.toProducts(null);

        assertThat(products).isEmpty();
        assertThat(products.size()).isNotPositive();
    }

}
