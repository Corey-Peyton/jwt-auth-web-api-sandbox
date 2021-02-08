/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : ProductJSONMapperTest.java
 * Date de création : 8 févr. 2021
 * Heure de création : 07:48:56
 * Package : fr.vincent.tuto.server.mapper.json
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.mapper.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

import fr.vincent.tuto.common.constants.AppConstants;
import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.common.mapper.GenericJSONMapper;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.model.dto.ProductDTO;

/**
 * Classe des Tests unitaires pour la production de données au format JSON.
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
// @JsonTest
@TestPropertySource(value = { "classpath:back-end-db-test.properties", "classpath:back-end-application-test.properties" })
@ContextConfiguration(name = "serverExceptionHandlerTest", classes = { BackEndServerRootConfig.class, GenericJSONMapper.class,
        StringHttpMessageConverter.class })
@SpringBootTest
@ActiveProfiles("test")
class ProductJSONMapperTest
{
    //
    private static final String OBJECT_NULL = "l'objet défini est null";
    private static final String FILE_NAME_COMP = "_List";
    private static final String INMEMORY_FILE = "_InMemory_List";
    private static final String PRETTY_FILE = "_List_Pretty";

    private static final String PRODUCT_JSON = "{\"id\":10,\"name\":\"Nom produit de Test L2008902 du DTO\",\"description\":\"Description produit de Test du DTO\",\"quantity\":2,\"unitPrice\":10.00,\"price\":20.00,\"isActive\":true,\"imageUrl\":\"img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg\"}";
    private static final String PRODUCT_JSON_PRETTY_PRINT = "{\"id\" : 10,\r\n" + "  \"name\" : \"Nom produit de Test L2008902 du DTO\",\r\n"
    + "  \"description\" : \"Description produit de Test du DTO\",\r\n" + "  \"quantity\" : 2,\r\n" + "  \"unitPrice\" : 10.00,\r\n"
    + "  \"price\" : 20.00,\r\n" + "  \"isActive\" : true,\r\n"
    + "  \"imageUrl\" : \"img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg\"\r\n" + "}";

    @Autowired
    private GenericJSONMapper genericJSONMapper;

    // @Autowired
    // private JacksonTester<ProductDTO> jsonTester; // pour l'utiliser, il faut : @JsonTest et StringHttpMessageConverter
    // et pas @SpringBootTest

    @Value("${vot.json.file.test.location}")
    private String jsonFilePathLocation;

    private ProductDTO dto;

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
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
        this.genericJSONMapper = null;
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.mapper.json.ProductJSONMapper#toStringJSON(java.lang.Object, java.lang.Boolean)}.
     */
    @Test
    void testToStringJSON()
    {
        final String strJson = this.genericJSONMapper.toStringJSON(this.dto, false);

        // System.err.println(">>>>>> Le flux JSON formaté de l'articles est :\n" + strJson);

        assertThat(strJson).isNotNull();
        assertThat(strJson.length()).isPositive();
        assertThat(strJson.toString()).contains("Nom produit de Test L2008902");
        assertThat(strJson).startsWith("{");
    }

    @Test
    void testToStringJSON_PrettyPrint()
    {
        final String strJson = this.genericJSONMapper.toStringJSON(this.dto, true);

        // System.err.println(">>>>>> Le flux JSON formaté de l'articles est :\n" + strJson);

        assertThat(strJson).isNotNull();
        assertThat(strJson.length()).isPositive();
        assertThat(strJson.toString()).contains("Nom produit de Test L2008902");
        assertThat(strJson).startsWith("{");
    }

    @Test
    void testToStringJSON_WithNullPrettyPrint()
    {
        final String strJson = this.genericJSONMapper.toStringJSON(this.dto, null);

        assertThat(strJson).isNotNull();
        assertThat(strJson.length()).isPositive();
        assertThat(strJson.toString()).contains("Nom produit de Test L2008902");
        assertThat(strJson).startsWith("{");
    }

    @Test
    void testToStringJSON_ToMap()
    {
        final String strJson = this.genericJSONMapper.toStringJSON(this.dto, null);
        assertThat(strJson).isNotNull();

        @SuppressWarnings("unchecked")
        final Map<String, Object> map = this.genericJSONMapper.toJSONObject(strJson, Map.class);

        assertThat(map).isNotNull();
        assertThat(map.size()).isPositive();
        assertThat(map).containsEntry("name", this.dto.getName());
    }

    @Test
    void testToStringJSON_ShouldCatchException_WithNullObject()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.genericJSONMapper.toStringJSON(null, true);
        });

        final String expectedMessage = OBJECT_NULL;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testToStringJSON_ShouldCatchException_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.genericJSONMapper.toStringJSON(null, null);
        });

        final String expectedMessage = OBJECT_NULL;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.mapper.json.ProductJSONMapper#toStringJSONList(java.util.Collection, java.lang.Boolean)}.
     */
    @Test
    void testToStringJSONList()
    {
        final List<ProductDTO> dtos = Lists.newArrayList();
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);

        final List<String> strings = this.genericJSONMapper.toStringJSONList(dtos, false);

        assertThat(strings).isNotNull();
        assertThat(strings.size()).isEqualTo(3);
    }

    @Test
    void testToStringJSONList_WithPrettyPrint()
    {
        final List<ProductDTO> dtos = Lists.newArrayList();
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);

        final List<String> strings = this.genericJSONMapper.toStringJSONList(dtos, true);

        assertThat(strings).isNotNull();
        assertThat(strings.size()).isEqualTo(3);
    }

    @Test
    void testToStringJSONList_WithPartialNull()
    {
        final List<ProductDTO> dtos = Lists.newArrayList();
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(null);

        final List<String> strings = this.genericJSONMapper.toStringJSONList(dtos, true);

        assertThat(strings).isNotNull();
        assertThat(strings.size()).isEqualTo(2);
    }

    @Test
    void testToStringJSONList_WithFullElementNull()
    {
        final List<ProductDTO> dtos = Lists.newArrayList();
        dtos.add(null);
        dtos.add(null);
        dtos.add(null);

        final List<String> strings = this.genericJSONMapper.toStringJSONList(dtos, true);

        assertThat(strings).isEmpty();
        assertThat(strings.size()).isNotPositive();
    }

    @Test
    void testToStringJSONList_NullList()
    {
        final List<String> strings = this.genericJSONMapper.toStringJSONList(null, false);

        assertThat(strings).isNotNull();
        assertThat(strings.size()).isNotPositive();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.mapper.json.ProductJSONMapper#toJSONObject(java.lang.String, java.lang.Class)}.
     */
    @Test
    void testToJSONObject()
    {
        final ProductDTO dto = this.genericJSONMapper.toJSONObject(PRODUCT_JSON, ProductDTO.class);

        assertThat(dto).isExactlyInstanceOf(ProductDTO.class);
        assertThat(dto.getName()).contains("L2008902 du DTO");
        assertThat(dto.toString()).isNotNull();
    }

    @Test
    void testToJSONObject_WithPrettyPrint()
    {
        final ProductDTO dto = this.genericJSONMapper.toJSONObject(PRODUCT_JSON_PRETTY_PRINT, ProductDTO.class);

        assertThat(dto).isExactlyInstanceOf(ProductDTO.class);
        assertThat(dto.getName()).contains("L2008902 du DTO");
        assertThat(dto.toString()).isNotNull();
    }

    @Test
    void testToJSONObject_ShouldThrowException_WithNullObject()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.genericJSONMapper.toStringJSON(null, true);
        });

        final String expectedMessage = OBJECT_NULL;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testToJSONObject_ShouldThrowException_WithNull()
    {
        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.genericJSONMapper.toStringJSON(null, null);
        });

        final String expectedMessage = OBJECT_NULL;
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.mapper.json.ProductJSONMapper#toJSONObjectList(java.util.Collection, java.lang.Class)}.
     */
    @Test
    void testToJSONObjectList()
    {
        final List<String> productsStrList = Lists.newArrayList();
        productsStrList.add(PRODUCT_JSON);
        productsStrList.add(PRODUCT_JSON);
        productsStrList.add(PRODUCT_JSON);
        productsStrList.add(PRODUCT_JSON);

        final List<ProductDTO> dtos = (List<ProductDTO>) this.genericJSONMapper.toJSONObjectList(productsStrList, ProductDTO.class);

        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isPositive();
        assertThat(dtos.get(0).getName()).contains("L2008902 du DTO");
    }

    @Test
    void testToJSONObjectList_WithMixte()
    {
        final List<String> productsStrList = Lists.newArrayList();
        productsStrList.add(PRODUCT_JSON);
        productsStrList.add(PRODUCT_JSON);
        productsStrList.add(PRODUCT_JSON_PRETTY_PRINT);
        productsStrList.add(PRODUCT_JSON_PRETTY_PRINT);

        final List<ProductDTO> dtos = (List<ProductDTO>) this.genericJSONMapper.toJSONObjectList(productsStrList, ProductDTO.class);

        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isPositive();
        assertThat(dtos.size()).isEqualTo(4);
        assertThat(dtos.get(0).getName()).contains("L2008902 du DTO");
    }

    @Test
    void testToJSONObjectList_WithPartialNull()
    {
        final List<String> productsStrList = Lists.newArrayList();
        productsStrList.add(PRODUCT_JSON);
        productsStrList.add(PRODUCT_JSON);
        productsStrList.add(PRODUCT_JSON_PRETTY_PRINT);
        productsStrList.add(PRODUCT_JSON_PRETTY_PRINT);
        productsStrList.add(null);

        final List<ProductDTO> dtos = (List<ProductDTO>) this.genericJSONMapper.toJSONObjectList(productsStrList, ProductDTO.class);

        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isPositive();
        assertThat(dtos.size()).isEqualTo(4);
        assertThat(dtos.get(0).getName()).contains("L2008902 du DTO");
    }

    @Test
    void testToJSONObjectList_WithFullElementNull()
    {
        final List<String> productsStrList = Lists.newArrayList();
        productsStrList.add(null);
        productsStrList.add(null);

        final List<ProductDTO> dtos = (List<ProductDTO>) this.genericJSONMapper.toJSONObjectList(productsStrList, ProductDTO.class);

        assertThat(dtos).isEmpty();
        assertThat(dtos.size()).isNotPositive();
    }

    @Test
    void testToJSONObjectList_WithNullList()
    {
        final List<String> productsStrList = Lists.newArrayList();
        productsStrList.add(null);
        productsStrList.add(null);

        final List<ProductDTO> dtos = (List<ProductDTO>) this.genericJSONMapper.toJSONObjectList(null, ProductDTO.class);

        assertThat(dtos).isEmpty();
        assertThat(dtos.size()).isNotPositive();
    }

    @Test
    void testToJSONObjectList_WithFullNull()
    {
        final List<String> productsStrList = Lists.newArrayList();
        productsStrList.add(null);
        productsStrList.add(null);

        final List<?> dtos = (List<?>) this.genericJSONMapper.toJSONObjectList(null, null);

        assertThat(dtos).isEmpty();
        assertThat(dtos.size()).isNotPositive();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.mapper.json.ProductJSONMapper#writeJSONFile(java.lang.String, java.lang.Boolean, java.util.Collection, java.lang.Boolean)}.
     */
    @Test
    void testWriteJSONFile()
    {
        final List<ProductDTO> dtos = Lists.newArrayList();
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);

        final String filename = this.jsonFilePathLocation + ProductDTO.class.getSimpleName() + FILE_NAME_COMP + AppConstants.JSON_FILE_SUFFIXE;
        final Boolean isFileCreated = this.genericJSONMapper.writeJSONFile(filename, false, dtos, false);

        assertThat(isFileCreated).isTrue();
    }

    @Test
    void testWriteJSONFile_WithoutFileSuffixe()
    {
        final List<ProductDTO> dtos = Lists.newArrayList();
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);

        final String filename = this.jsonFilePathLocation + ProductDTO.class.getSimpleName() + FILE_NAME_COMP;

        final Boolean isFileCreated = this.genericJSONMapper.writeJSONFile(filename, false, dtos, false);

        assertThat(isFileCreated).isTrue();
    }

    @Test
    void testWriteJSONFile_WithFileInMemory()
    {
        final List<ProductDTO> dtos = Lists.newArrayList();
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);

        final String filename = this.jsonFilePathLocation + ProductDTO.class.getSimpleName() + INMEMORY_FILE;
        final Boolean isFileCreated = this.genericJSONMapper.writeJSONFile(filename, false, dtos, false);

        assertThat(isFileCreated).isTrue();
    }

    @Test
    void testWriteJSONFile_WithPrettyPrintTrue()
    {
        final List<ProductDTO> dtos = Lists.newArrayList();
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);

        final String filename = this.jsonFilePathLocation + ProductDTO.class.getSimpleName() + PRETTY_FILE + AppConstants.JSON_FILE_SUFFIXE;
        final Boolean isFileCreated = this.genericJSONMapper.writeJSONFile(filename, false, dtos, true);

        assertThat(isFileCreated).isTrue();
    }

    @Test
    void testWriteJSONFile_ShouldThrowException()
    {
        final List<ProductDTO> dtos = Lists.newArrayList();
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);
        dtos.add(this.dto);

        final Exception exception = assertThrows(CustomAppException.class, () -> {
            this.genericJSONMapper.writeJSONFile(null, false, dtos, true);
        });

        final String expectedMessage = "java.lang.NullPointerException";
        final String actualMessage = exception.getMessage();

        assertThat(actualMessage.length()).isPositive();
        assertThat(actualMessage).contains(expectedMessage);
    }

    @Test
    void testResourceNotNull()
    {
        assertThat(this.jsonFilePathLocation).isNotNull();
        assertThat(this.genericJSONMapper).isNotNull();
    }

}
