/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : CategoryJSONMapperTest.java
 * Date de création : 9 févr. 2021
 * Heure de création : 12:50:35
 * Package : fr.vincent.tuto.server.mapper.json
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.service.mapper.json;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import fr.vincent.tuto.common.constants.AppConstants;
import fr.vincent.tuto.common.exception.CustomAppException;
import fr.vincent.tuto.common.mapper.GenericJSONMapper;
import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.constants.ServerConstants;
import fr.vincent.tuto.server.enumeration.CategoryTypeEnum;
import fr.vincent.tuto.server.model.dto.CategoryDTO;
import fr.vincent.tuto.server.model.dto.ProductDTO;
import fr.vincent.tuto.server.model.po.Category;
import fr.vincent.tuto.server.service.mapper.CategoryMapper;
import fr.vincent.tuto.server.service.mapper.ProductMapper;
import fr.vincent.tuto.server.utils.TestsDataUtils;

/**
 * Classe des Tests d'Intégration des composants pour la production de données au format JSON des objet de type
 * {@link Category} et {@link CategoryDTO}.
 * 
 * @author Vincent Otchoun
 */
@RunWith(SpringRunner.class)
@TestPropertySource(value = { "classpath:back-end-db-test.properties", "classpath:back-end-application-test.properties" })
@ContextConfiguration(name = "categoryJSONMapperTest", classes = { BackEndServerRootConfig.class, GenericJSONMapper.class,
        StringHttpMessageConverter.class, ProductMapper.class, CategoryMapper.class })
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CategoryJSONMapperTest
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

    @Value("${vot.json.file.test.location.category}")
    private String jsonFilePathLocation;

    @Autowired
    private GenericJSONMapper genericJSONMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryDTO dto;

    /**
     * Créer un fichier à partir de sa localisation.
     * 
     * @param filename l'emplacement du fichier.
     * @return le fichier créé.
     */
    private File creerFichier(final String filename)
    {
        final Path fichier = Paths.get(filename);
        final File file = fichier.toFile();
        file.setWritable(true);
        return file;
    }

    /**
     * Lire le contenu fichier non formaté.
     * 
     * @return le flux JSON contenu dans la chaîne de caractères.
     * @throws IOException exception levée lorsque srvient une erreur.
     */
    private String ReadNotPrettyFileContent() throws IOException
    {
        final String filename = String.format(TRIPLE_FORMAT_STR, this.jsonFilePathLocation, "CategoryStr", AppConstants.JSON_FILE_SUFFIXE);
        final File file = creerFichier(filename);
        final String string = FileUtils.readFileToString(file, StandardCharsets.UTF_8.name());
        // Traitement pour récupérer le contenu du fichier
        final String strResult = string.replace("\\", "");
        // Recupérer le flux JSON entre les quotes de la chaîne de caractères
        final String strResultContent = StringUtils.substring(strResult, 1, strResult.length() - 1);
        return strResultContent;
    }

    /**
     * Lire le contenu fichier formaté.
     * 
     * @return le flux JSON contenu dans la chaîne de caractères.
     * @throws IOException exception levée lorsque srvient une erreur.
     */
    private String readPrettyFileContent() throws IOException
    {
        final String filename = String.format(TRIPLE_FORMAT_STR, this.jsonFilePathLocation, "CategoryStrPretty", AppConstants.JSON_FILE_SUFFIXE);
        final File file = creerFichier(filename);
        final String string = FileUtils.readFileToString(file, StandardCharsets.UTF_8.name());
        // Supprimer les retours chariot
        final String strNoEndOfLine = string.replace("\\r\\n", "");
        // Supprimer les slashs
        final String strNoSlash = strNoEndOfLine.replace("\\", "");
        // Supprimer les espaces inutiles
        final String strNoSpace = strNoSlash.replace("  ", "");
        // Recupérer le flux JSON entre les quotes de la chaîne de caractères
        final String strResultContent = StringUtils.substring(strNoSpace, 1, strNoSpace.length() - 1);
        return strResultContent;
    }

    /**
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        this.dto = TestsDataUtils.CATEGORY_DTO;
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterEach
    void tearDown() throws Exception
    {
        this.genericJSONMapper = null;
        this.categoryMapper = null;
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.mapper.json.CategoryJSONMapper#toStringJSON(java.lang.Object, java.lang.Boolean)}.
     * 
     * @throws IOException
     * @throws JsonMappingException
     * @throws JsonGenerationException
     */
    @Test
    @Order(1)
    void testToStringJSON() throws JsonGenerationException, JsonMappingException, IOException
    {
        final String strJson = this.genericJSONMapper.toStringJSON(this.dto, false);

        final String filename = String.format(TRIPLE_FORMAT_STR, this.jsonFilePathLocation, "CategoryStr", AppConstants.JSON_FILE_SUFFIXE);
        final File file = creerFichier(filename);
        this.objectMapper.writeValue(file, strJson);

        assertThat(strJson).isNotNull();
        assertThat(strJson.length()).isPositive();
        assertThat(strJson.toString()).contains(CategoryTypeEnum.ELCETROMENAGER.name());
        assertThat(strJson).startsWith("{");
    }

    @Test
    @Order(2)
    void testToStringJSON_PrettyPrint() throws JsonGenerationException, JsonMappingException, IOException
    {
        final String strJson = this.genericJSONMapper.toStringJSON(this.dto, true);
        final String filename = String.format(TRIPLE_FORMAT_STR, this.jsonFilePathLocation, "CategoryStrPretty", AppConstants.JSON_FILE_SUFFIXE);

        final File file = creerFichier(filename);
        this.objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, strJson);

        final List<String> fileStrings = FileUtils.readLines(file, StandardCharsets.UTF_8.name());
        final String result = fileStrings.stream().collect(Collectors.joining());
        // System.err.println(">>>>>>> La jointure est : \n" + result);

        assertThat(result).startsWith("\"{");
        assertThat(result.length()).isPositive();
        assertThat(result.toString()).contains(CategoryTypeEnum.ELCETROMENAGER.name());

    }

    @Test
    void testToStringJSON_WithNullPrettyPrint()
    {
        final String strJson = this.genericJSONMapper.toStringJSON(this.dto, null);

        assertThat(strJson).isNotNull();
        assertThat(strJson.length()).isPositive();
        assertThat(strJson.toString()).contains(CategoryTypeEnum.ELCETROMENAGER.name());
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
     * {@link fr.vincent.tuto.server.mapper.json.CategoryJSONMapper#toStringJSONList(java.util.Collection, java.lang.Boolean)}.
     */
    @Test
    void testToStringJSONList()
    {
        final List<String> strings = this.genericJSONMapper.toStringJSONList(TestsDataUtils.CATEGORIES_DTO(), false);

        assertThat(strings).isNotNull();
        assertThat(strings.size()).isEqualTo(2);
        assertThat(strings.toString()).contains(CategoryTypeEnum.ELCETROMENAGER.name());
    }

    @Test
    void testToStringJSONList_WithPrettyPrint()
    {
        final List<String> strings = this.genericJSONMapper.toStringJSONList(TestsDataUtils.CATEGORIES_DTO(), true);

        assertThat(strings).isNotNull();
        assertThat(strings.size()).isEqualTo(2);
        assertThat(strings.toString()).contains(CategoryTypeEnum.ELCETROMENAGER.name());
    }

    @Test
    void testToStringJSONList_WithPartialNull()
    {
        final List<CategoryDTO> dtos = TestsDataUtils.CATEGORIES_DTO();
        dtos.add(null);

        final List<String> strings = this.genericJSONMapper.toStringJSONList(dtos, true);

        assertThat(strings).isNotNull();
        assertThat(strings.size()).isEqualTo(2);
        assertThat(strings.toString()).contains(CategoryTypeEnum.ELCETROMENAGER.name());
    }

    @Test
    void testToStringJSONList_WithFullElementNull()
    {
        final List<CategoryDTO> dtos = Lists.newArrayList();
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
     * {@link fr.vincent.tuto.server.mapper.json.CategoryJSONMapper#toJSONObject(java.lang.String, java.lang.Class)}.
     * 
     * @throws IOException
     */
    @Test
    void testToJSONObject() throws IOException
    {
        final String noPrettyContent = this.ReadNotPrettyFileContent();

        final CategoryDTO dto = this.genericJSONMapper.toJSONObject(noPrettyContent, CategoryDTO.class);
        //
        assertThat(dto).isExactlyInstanceOf(CategoryDTO.class);
        assertThat(dto.getName()).contains("ELCETROMENAGER");
        assertThat(dto.getProducts()).isNotEmpty();
        assertThat(dto.getProducts().size()).isPositive();
        assertThat(dto.getProducts().size()).isEqualTo(3);
    }

    @Test
    void testToJSONObject_WithPrettyPrint() throws IOException
    {
        final String prettyContent = this.readPrettyFileContent();
        //
        final CategoryDTO dto = this.genericJSONMapper.toJSONObject(prettyContent, CategoryDTO.class);
        //
        assertThat(dto).isExactlyInstanceOf(CategoryDTO.class);
        assertThat(dto.getName()).contains("ELCETROMENAGER");
        assertThat(dto.getProducts()).isNotEmpty();
        assertThat(dto.getProducts().size()).isPositive();
        assertThat(dto.getProducts().size()).isEqualTo(3);
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
     * {@link fr.vincent.tuto.server.mapper.json.CategoryJSONMapper#toJSONObjectList(java.util.Collection, java.lang.Class)}.
     * 
     * @throws IOException
     */
    @Test
    void testToJSONObjectList() throws IOException
    {
        final String noPrettyContent = this.ReadNotPrettyFileContent();

        final List<String> categoryStrList = Lists.newArrayList();
        categoryStrList.add(noPrettyContent);
        categoryStrList.add(noPrettyContent);
        categoryStrList.add(noPrettyContent);
        categoryStrList.add(noPrettyContent);
        
        final List<CategoryDTO> dtos = (List<CategoryDTO>) this.genericJSONMapper.toJSONObjectList(categoryStrList, CategoryDTO.class);
        
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isPositive();
        assertThat(dtos.size()).isEqualTo(4);
        assertThat(dtos.get(0).getName()).contains("ELCETROMENAGER");
    }

    @Test
    void testToJSONObjectList__WithMixte() throws IOException
    {
        final String noPrettyContent = this.ReadNotPrettyFileContent();
        final String prettyContent = this.readPrettyFileContent();
        
        final List<String> categoryStrList = Lists.newArrayList();
        categoryStrList.add(noPrettyContent);
        categoryStrList.add(noPrettyContent);
        categoryStrList.add(prettyContent);
        categoryStrList.add(prettyContent);
        
        final List<CategoryDTO> dtos = (List<CategoryDTO>) this.genericJSONMapper.toJSONObjectList(categoryStrList, CategoryDTO.class);
        
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isPositive();
        assertThat(dtos.size()).isEqualTo(4);
        assertThat(dtos.get(0).getName()).contains("ELCETROMENAGER");
    }

    @Test
    void testToJSONObjectList_WithPartialNull() throws IOException
    {
        final String noPrettyContent = this.ReadNotPrettyFileContent();
        final String prettyContent = this.readPrettyFileContent();
        
        final List<String> categoryStrList = Lists.newArrayList();
        categoryStrList.add(noPrettyContent);
        categoryStrList.add(noPrettyContent);
        categoryStrList.add(prettyContent);
        categoryStrList.add(prettyContent);
        categoryStrList.add(null);
        
        final List<CategoryDTO> dtos = (List<CategoryDTO>) this.genericJSONMapper.toJSONObjectList(categoryStrList, CategoryDTO.class);
        
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isPositive();
        assertThat(dtos.size()).isEqualTo(4);
        assertThat(dtos.get(0).getName()).contains("ELCETROMENAGER");
    }
    
    @Test
    void testToJSONObjectList_WithFullElementNull()
    {
        final List<String> categoryStrList = Lists.newArrayList();
        categoryStrList.add(null);
        categoryStrList.add(null);

        final List<CategoryDTO> dtos = (List<CategoryDTO>) this.genericJSONMapper.toJSONObjectList(categoryStrList, CategoryDTO.class);

        assertThat(dtos).isEmpty();
        assertThat(dtos.size()).isNotPositive();
    }

    @Test
    void testToJSONObjectList_WithNullList()
    {
        final List<CategoryDTO> dtos = (List<CategoryDTO>) this.genericJSONMapper.toJSONObjectList(null, CategoryDTO.class);

        assertThat(dtos).isEmpty();
        assertThat(dtos.size()).isNotPositive();
    }

    @Test
    void testToJSONObjectList_WithFullNull()
    {
        final List<?> dtos = (List<?>) this.genericJSONMapper.toJSONObjectList(null, null);

        assertThat(dtos).isEmpty();
        assertThat(dtos.size()).isNotPositive();
    }

    /**
     * Test method for
     * {@link fr.vincent.tuto.server.mapper.json.CategoryJSONMapper#writeJSONFile(java.lang.String, java.lang.Boolean, java.util.Collection, java.lang.Boolean)}.
     */
    @Test
    void testWriteJSONFile()
    {
        final String filename = String.format(QUATRE_FORMAT_STR, this.jsonFilePathLocation , CategoryDTO.class.getSimpleName() ,FILE_NAME_COMP , AppConstants.JSON_FILE_SUFFIXE);
        final Boolean isFileCreated = this.genericJSONMapper.writeJSONFile(filename, false, TestsDataUtils.CATEGORIES_DTO_SINGLE(), false);

        assertThat(isFileCreated).isTrue();
    }
    
    @Test
    void testWriteJSONFile_WithFileInMemory()
    {
        final String filename = String.format(TRIPLE_FORMAT_STR, this.jsonFilePathLocation , CategoryDTO.class.getSimpleName() , INMEMORY_FILE);
        final Boolean isFileCreated = this.genericJSONMapper.writeJSONFile(filename, false, TestsDataUtils.CATEGORIES_DTO_SINGLE(), false);

        assertThat(isFileCreated).isTrue();
    }
    
    
    @Test
    void testWriteJSONFile_WithPrettyPrintTrue()
    {
        final String filename = String.format(QUATRE_FORMAT_STR, this.jsonFilePathLocation, CategoryDTO.class.getSimpleName(), PRETTY_FILE,
        AppConstants.JSON_FILE_SUFFIXE);
        final Boolean isFileCreated = this.genericJSONMapper.writeJSONFile(filename, false, TestsDataUtils.CATEGORIES_DTO_SINGLE(), true);

        assertThat(isFileCreated).isTrue();
    }
    
    @Test
    void testWriteJSONFile_WithPrettyPrintTrueFullData()
    {
        final String filename = String.format(QUATRE_FORMAT_STR, this.jsonFilePathLocation, CategoryDTO.class.getSimpleName(), "_List_Pretty_2",
        AppConstants.JSON_FILE_SUFFIXE);
        final Boolean isFileCreated = this.genericJSONMapper.writeJSONFile(filename, false, TestsDataUtils.CATEGORIES_DTO(), true);

        assertThat(isFileCreated).isTrue();
    }
    
    @Test
    void testWriteJSONFile_WithPrettyPrintTrue_ListAndSet()
    {
        final List<Category> categories = (List<Category>) this.categoryMapper.toCategories(TestsDataUtils.CATEGORIES_DTO());
        final Set<Category> categoriesSet = ServerConstants.listToSet(categories); 
        
        // Création du fichier au format JSON avec List
        final String filename = String.format(QUATRE_FORMAT_STR, this.jsonFilePathLocation, ProductDTO.class.getSimpleName(), LIST_PRETTY_FILE,
        AppConstants.JSON_FILE_SUFFIXE);
        final Boolean isFileCreated = this.genericJSONMapper.writeJSONFile(filename, false, categories, true);

        // Création du fichier au format JSON avec Set
        final String filename1 = String.format(QUATRE_FORMAT_STR, this.jsonFilePathLocation, ProductDTO.class.getSimpleName(), SET_PRETTY_FILE,
        AppConstants.JSON_FILE_SUFFIXE);
        final Boolean isFileCreated1 = this.genericJSONMapper.writeJSONFile(filename1, false, categoriesSet, true);

        assertThat(isFileCreated).isTrue();
        assertThat(isFileCreated1).isTrue();
    }
}
