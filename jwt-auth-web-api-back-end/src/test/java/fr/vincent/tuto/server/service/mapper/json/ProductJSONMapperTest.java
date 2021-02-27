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
package fr.vincent.tuto.server.service.mapper.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.assertj.core.util.Sets;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
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
import fr.vincent.tuto.server.model.po.Product;
import fr.vincent.tuto.server.service.mapper.ProductMapper;
import fr.vincent.tuto.server.util.ServerUtil;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests d'Intégration des composants pour la production de données au format JSON des objet de type
 * {@link Product} et {@link ProductDTO}.
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
// @JsonTest
@TestPropertySource(value = { "classpath:back-end-db-common-test.properties", "classpath:back-end-application-test.properties" })
@ContextConfiguration(name = "productJSONMapperTest", classes = { BackEndServerRootConfig.class, GenericJSONMapper.class,
        StringHttpMessageConverter.class, ProductMapper.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ProductJSONMapperTest
{
    //
    private static final String OBJECT_NULL = "l'objet défini est null";
    private static final String FILE_NAME_COMP = "_List";
    private static final String INMEMORY_FILE = "_InMemory_List";
    private static final String PRETTY_FILE = "_List_Pretty";
    private static final String LIST_PRETTY_FILE = "_With_List_Pretty";
    private static final String SET_PRETTY_FILE = "_With_Set_Pretty";

    private static final String TRIPLE_FORMAT_STR = "%s%s%s";
    private static final String QUATRE_FORMAT_STR = "%s%s%s%s";
    private static final String CINQ_FORMAT_STR = "%s%s%s%s%s";

    private static final String PRODUCT_JSON = "{\"id\":10,\"name\":\"Nom produit de Test L2008902 du DTO\",\"description\":\"Description produit de Test du DTO\",\"quantity\":2,\"unitPrice\":10.00,\"price\":20.00,\"isActive\":true,\"imageUrl\":\"img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg\"}";
    private static final String PRODUCT_JSON_PRETTY_PRINT = "{\"id\" : 10,\r\n" + "  \"name\" : \"Nom produit de Test L2008902 du DTO\",\r\n"
    + "  \"description\" : \"Description produit de Test du DTO\",\r\n" + "  \"quantity\" : 2,\r\n" + "  \"unitPrice\" : 10.00,\r\n"
    + "  \"price\" : 20.00,\r\n" + "  \"isActive\" : true,\r\n"
    + "  \"imageUrl\" : \"img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg\"\r\n" + "}";

    @Autowired
    private GenericJSONMapper genericJSONMapper;

    @Autowired
    private ProductMapper productMapper;

    // @Autowired
    // private JacksonTester<ProductDTO> jsonTester; // pour l'utiliser, il faut : @JsonTest et StringHttpMessageConverter
    // et pas @SpringBootTest

    @Value("${vot.json.file.test.location.product}")
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
        this.productMapper = null;
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.mapper.json.ProductJSONMapper#toStringJSON(java.lang.Object, java.lang.Boolean)}.
     */
    @Test
    void testToStringJSON()
    {
        final String strJson = this.genericJSONMapper.toStringJSON(this.dto, false);

        assertThat(strJson).isNotNull();
        assertThat(strJson.length()).isPositive();
        assertThat(strJson.toString()).contains("Nom produit de Test L2008902");
        assertThat(strJson).startsWith("{");
    }

    @Test
    void testToStringJSON_PrettyPrint()
    {
        final String strJson = this.genericJSONMapper.toStringJSON(this.dto, true);

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

        final String filename = String.format(QUATRE_FORMAT_STR, this.jsonFilePathLocation , ProductDTO.class.getSimpleName() ,FILE_NAME_COMP , AppConstants.JSON_FILE_SUFFIXE);
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

        final String filename = String.format(TRIPLE_FORMAT_STR, this.jsonFilePathLocation , ProductDTO.class.getSimpleName() , FILE_NAME_COMP);
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

        final String filename = String.format(TRIPLE_FORMAT_STR, this.jsonFilePathLocation , ProductDTO.class.getSimpleName() , INMEMORY_FILE);
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

        final String filename = String.format(QUATRE_FORMAT_STR, this.jsonFilePathLocation, ProductDTO.class.getSimpleName(), PRETTY_FILE,
        AppConstants.JSON_FILE_SUFFIXE);
        final Boolean isFileCreated = this.genericJSONMapper.writeJSONFile(filename, false, dtos, true);

        assertThat(isFileCreated).isTrue();
    }

    @Test
    void testWriteJSONFile_WithPrettyPrintTrue_ListAndSet()
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
        final Set<Product> set = ServerUtil.listToSet(products); // Set

        // Création du fichier au format JSON avec List
        final String filename = String.format(QUATRE_FORMAT_STR, this.jsonFilePathLocation, ProductDTO.class.getSimpleName(), LIST_PRETTY_FILE,
        AppConstants.JSON_FILE_SUFFIXE);
        final Boolean isFileCreated = this.genericJSONMapper.writeJSONFile(filename, false, products, true);

        // Création du fichier au format JSON avec Set
        final String filename1 = String.format(QUATRE_FORMAT_STR, this.jsonFilePathLocation, ProductDTO.class.getSimpleName(), SET_PRETTY_FILE,
        AppConstants.JSON_FILE_SUFFIXE);
        final Boolean isFileCreated1 = this.genericJSONMapper.writeJSONFile(filename1, false, set, true);

        assertThat(isFileCreated).isTrue();
        assertThat(isFileCreated1).isTrue();
    }

    @Test
    void testWriteJSONFile_WithPrettyPrintTrue_NoID()
    {

        final List<Product> productsList = TestsDataUtils.PRODUCTS();
        final Set<Product> productsSet = ServerUtil.listToSet(productsList);

        // Création du fichier au format JSON avec List
        final String filename = String.format(CINQ_FORMAT_STR, this.jsonFilePathLocation , Product.class.getSimpleName() , "_NO_ID" , LIST_PRETTY_FILE,AppConstants.JSON_FILE_SUFFIXE);
        final Boolean isFileCreated = this.genericJSONMapper.writeJSONFile(filename, false, productsList, true);

        // Création du fichier au format JSON avec Set
        final String filename1 = String.format(CINQ_FORMAT_STR, this.jsonFilePathLocation , Product.class.getSimpleName() , "_NO_ID" , SET_PRETTY_FILE,AppConstants.JSON_FILE_SUFFIXE);
        final Boolean isFileCreated1 = this.genericJSONMapper.writeJSONFile(filename1, false, productsSet, true);

        assertThat(isFileCreated).isTrue();
        assertThat(isFileCreated1).isTrue();
    }

    @Test
    void testWriteJSONFile_WithPrettyPrintTrue_ID()
    {

        final List<Product> productsList = TestsDataUtils.PRODUCTS_WITH_ID();
        final Set<Product> productsSet = ServerUtil.listToSet(productsList);

        // Création du fichier au format JSON avec List
        final String filename = String.format(CINQ_FORMAT_STR, this.jsonFilePathLocation , Product.class.getSimpleName() , "_ID" , LIST_PRETTY_FILE,AppConstants.JSON_FILE_SUFFIXE);
        final Boolean isFileCreated = this.genericJSONMapper.writeJSONFile(filename, false, productsList, true);

        // Création du fichier au format JSON avec Set
        final String filename1 = String.format(CINQ_FORMAT_STR, this.jsonFilePathLocation , Product.class.getSimpleName() , "_ID" , SET_PRETTY_FILE,AppConstants.JSON_FILE_SUFFIXE);
        final Boolean isFileCreated1 = this.genericJSONMapper.writeJSONFile(filename1, false, productsSet, true);

        assertThat(isFileCreated).isTrue();
        assertThat(isFileCreated1).isTrue();
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
