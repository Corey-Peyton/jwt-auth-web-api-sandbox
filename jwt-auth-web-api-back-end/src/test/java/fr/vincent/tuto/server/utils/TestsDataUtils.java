/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : TestsDataUtils.java
 * Date de création : 29 janv. 2021
 * Heure de création : 15:07:23
 * Package : fr.vincent.tuto.server.utils
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.vincent.tuto.server.enumeration.CategoryTypeEnum;
import fr.vincent.tuto.server.enumeration.RoleEnum;
import fr.vincent.tuto.server.model.dto.CategoryDTO;
import fr.vincent.tuto.server.model.dto.ProductDTO;
import fr.vincent.tuto.server.model.dto.UserDTO;
import fr.vincent.tuto.server.model.po.Category;
import fr.vincent.tuto.server.model.po.Product;
import fr.vincent.tuto.server.model.po.User;
import lombok.experimental.UtilityClass;

/**
 * Utilitaire pour la fourniture des données de tests.
 * 
 * @author Vincent Otchoun
 */
@UtilityClass
public final class TestsDataUtils
{
    public static final String USER_ADMIN_USERNAME = "admin";
    public static final String USER_MODERATEUR_USERNAME = "moderateur";
    public static final String ADMIN = "admin";
    public static final String TOKEN = "token";
    public static final String ANONYMOUS = "anonymous";
    public static final String USER_MANAGER = "user_manager";
    public static final String MODERATEUR = "moderateur";
    public static final String MODERATEUR_UPPER = "MODERATEUR";
    public static final String CLIENT = "client";

    public static final String ADMIN_EMAIL = "ADMIN.TEST@LIVE.FR";
    public static final String ADMIN_EMAIL_LOWER = "admin.test@live.fr";
    public static final String INACTIVE_USER_MAIL = "client3.test@live.fr";
    public static final String USER_TWO_EMAIL = "admin2.test@live.fr";

    public static final String IMAGE_URL_TO_SEARCH = "img/lave-vaisselle-pose-libre-electrolux-esf8650row.jpg";
    public static final String CATEGORY_NAME_TO_SEARCH = "MEUBLES-DECO";
    public static final String CATEGORY_NAME_TO_SEARCH_LOWER_CASE = "meubles-deco";

    public static final String PRODUCT_NAME_TO_SEARCH = "TEFAL L2008902";
    public static final String PRODUCT_NAME_TO_SEARCH_LOWER_CASE = "tefal l2008902";

    // Creation des produits : l'active est gérér dans le modèle métier pour la création
    // ECLETROMENAGER
    public final Product PRODUCT_1 = Product.builder().name("TEFAL L2008902")//
    .description("Batterie de cuisine 10 pièces Ingenio Essential - Tous feux sauf induction")//
    .quantity(5L).unitPrice(new BigDecimal("5.539")).price(new BigDecimal("55.39")).imageUrl(
    "img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg").build();

    final Product PRODUCT_7 = Product.builder().name("PHILIPS FC8243/09")//
    .description("Aspirateur traîneau avec sac PowerGo - Suceur Plat Intégré - 750W - 77 dB - A - Rouge Sportif")//
    .quantity(1L).unitPrice(new BigDecimal("5.499")).price(new BigDecimal("54.99")).imageUrl(
    "img/philips-fc8243-09-aspirateur-traineau-avec-sac-pow.jpg").build();

    final Product PRODUCT_4 = Product.builder().name("Lave-vaisselle ELECTROLUX ESF8650ROW")//
    .description("15 couverts - Largeur 60 cm - Classe A+++ - 44 dB - Blanc")//
    .quantity(1L).unitPrice(new BigDecimal("439.99")).price(new BigDecimal("439.99")).imageUrl(
    "img/lave-vaisselle-pose-libre-electrolux-esf8650row.jpg").build();

    // MEUBLE-DECO
    public final Product PRODUCT_2 = Product.builder().name("BARCELONE")//
    .description("Canapé d'angle convertible 4 places + coffre de rangement - Tissu et simili Noir et Gris")//
    .quantity(1L).unitPrice(new BigDecimal("499.99")).price(new BigDecimal("499.99")).imageUrl(
    "img/barcelone-canape-d-angle-convertible-4-places-co.jpg").build();

    final Product PRODUCT_8 = Product.builder().name("TORPE")//
    .description("Bureau informatique contemporain 90x74x50 cm | Taille compacte + support clavier + tiroir | Table ordinateur | Sonoma")//
    .quantity(1L).unitPrice(new BigDecimal("259.99")).price(new BigDecimal("259.99")).imageUrl(
    "img/torpe-bureau-informatique-contemporain-90x74x50.jpg").build();

    final Product PRODUCT_9 = Product.builder().name("COREP")//
    .description("Lampadaire en métal - E27 - 40 W - Noir et cuivre")//
    .quantity(1L).unitPrice(new BigDecimal("29.99")).price(new BigDecimal("29.99")).imageUrl("img/corep-lampadaire-en-metal-e27-40-w-noir-et-c.jpg")
    .build();

    // SON
    final Product PRODUCT_3 = Product.builder().name("AUNA DS-2")//
    .description("Chaîne hifi stereo compacte avec platine vinyle , lecteur CD & radio , encodageMP3 , ports USB/SD , haut-parleur 2 voies")//
    .quantity(1L).unitPrice(new BigDecimal("154.99")).price(new BigDecimal("154.99")).imageUrl(
    "img/chaine-stereo-platine-vinyle-enregistrement-mp3.jpg").build();

    final Product PRODUCT_10 = Product.builder().name("FunKey")//
    .description("61 Keyboard noir Set incl. Support de Clavier. Banquette école de clavier inclus")//
    .quantity(1L).unitPrice(new BigDecimal("111.87")).price(new BigDecimal("111.87")).imageUrl(
    "img/funkey-61-keyboard-noir-set-incl-support-de-clav.jpg").build();

    final Product PRODUCT_11 = Product.builder().name("LEGEND ")//
    .description("Pack Guitare Type Stratocaster Black Mat")//
    .quantity(2L).unitPrice(new BigDecimal("137.99")).price(new BigDecimal("137.99")).imageUrl(
    "img/legend-pack-guitare-type-stratocaster-black-mat.jpg").build();

    // INFORMATIQUE
    final Product PRODUCT_5 = Product.builder().name("OMEN HP PC Gamer")//
    .description("15,6\" FHD - AMD Ryzen 7 - RAM 16Go - Stockage 512Go SSD - GTX 1660Ti 6Go - Win 10 - AZERTY")//
    .quantity(1L).unitPrice(new BigDecimal("1219.99")).price(new BigDecimal("1219.99")).imageUrl(
    "img/omen-by-hp-pc-gamer-15-en0002nf-15-6-fhd-amd.jpg").build();

    final Product PRODUCT_12 = Product.builder().name("CORSAIR Clavier Gamer")//
    .description("Membrane K55 RGB - AZERTY (CH-9206015-FR)")//
    .quantity(1L).unitPrice(new BigDecimal("74.99")).price(new BigDecimal("74.99")).imageUrl(
    "img/corsair-clavier-gamer-membrane-k55-rgb-azerty-c.jpg").build();

    final Product PRODUCT_13 = Product.builder().name("HP LaserJet Pro M130a")//
    .description("Imprimante Laser Monochrome Multifonction")//
    .quantity(1L).unitPrice(new BigDecimal("157.94")).price(new BigDecimal("157.94")).imageUrl(
    "img/hp-laserjet-pro-m130a-imprimante-laser-monochrom.jpg").build();

    // TELEPHONIE
    final Product PRODUCT_6 = Product.builder().name("Samsung Galaxy")//
    .description("S20 FE 5G Blanc")//
    .quantity(1L).unitPrice(new BigDecimal("759.00")).price(new BigDecimal("759.00")).imageUrl("img/samsung-galaxy-s20-fe-5g-blanc.jpg").build();

    final Product PRODUCT_14 = Product.builder().name("Ecouteur Bluetooth V5.0 3000mAh")//
    .description("Arbily T22 Antibruit CVC8.0 Hi-FI Son Oreillette Bluetooth Sport Etanche avec Micro Intégré")//
    .quantity(1L).unitPrice(new BigDecimal("30.99")).price(new BigDecimal("30.99")).imageUrl(
    "img/ecouteur-bluetooth-v5-0-3000mah-arbily-t22-antibru.jpg").build();

    // Creation des produits avec ID: l'active est gérér dans le modèle métier pour la création
    // ECLETROMENAGER
    public final Product PRODUCT = Product.builder().id(1L).name("TEFAL L2008902")//
    .description("Batterie de cuisine 10 pièces Ingenio Essential - Tous feux sauf induction")//
    .quantity(5L).unitPrice(new BigDecimal("5.539")).price(new BigDecimal("55.39")).imageUrl(
    "img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg").build();

    final Product PRODUCT2 = Product.builder().id(2L).name("PHILIPS FC8243/09")//
    .description("Aspirateur traîneau avec sac PowerGo - Suceur Plat Intégré - 750W - 77 dB - A - Rouge Sportif")//
    .quantity(1L).unitPrice(new BigDecimal("5.499")).price(new BigDecimal("54.99")).imageUrl(
    "img/philips-fc8243-09-aspirateur-traineau-avec-sac-pow.jpg").build();

    final Product PRODUCT3 = Product.builder().id(3L).name("Lave-vaisselle ELECTROLUX ESF8650ROW")//
    .description("15 couverts - Largeur 60 cm - Classe A+++ - 44 dB - Blanc")//
    .quantity(1L).unitPrice(new BigDecimal("439.99")).price(new BigDecimal("439.99")).imageUrl(
    "img/lave-vaisselle-pose-libre-electrolux-esf8650row.jpg").build();

    // MEUBLE-DECO
    public final Product PRODUCT4 = Product.builder().id(4L).name("BARCELONE")//
    .description("Canapé d'angle convertible 4 places + coffre de rangement - Tissu et simili Noir et Gris")//
    .quantity(1L).unitPrice(new BigDecimal("499.99")).price(new BigDecimal("499.99")).imageUrl(
    "img/barcelone-canape-d-angle-convertible-4-places-co.jpg").build();

    final Product PRODUCT5 = Product.builder().id(5L).name("TORPE")//
    .description("Bureau informatique contemporain 90x74x50 cm | Taille compacte + support clavier + tiroir | Table ordinateur | Sonoma")//
    .quantity(1L).unitPrice(new BigDecimal("259.99")).price(new BigDecimal("259.99")).imageUrl(
    "img/torpe-bureau-informatique-contemporain-90x74x50.jpg").build();

    final Product PRODUCT6 = Product.builder().id(6L).name("COREP")//
    .description("Lampadaire en métal - E27 - 40 W - Noir et cuivre")//
    .quantity(1L).unitPrice(new BigDecimal("29.99")).price(new BigDecimal("29.99")).imageUrl("img/corep-lampadaire-en-metal-e27-40-w-noir-et-c.jpg")
    .build();

    // SON
    final Product PRODUCT7 = Product.builder().id(7L).name("AUNA DS-2")//
    .description("Chaîne hifi stereo compacte avec platine vinyle , lecteur CD & radio , encodageMP3 , ports USB/SD , haut-parleur 2 voies")//
    .quantity(1L).unitPrice(new BigDecimal("154.99")).price(new BigDecimal("154.99")).imageUrl(
    "img/chaine-stereo-platine-vinyle-enregistrement-mp3.jpg").build();

    final Product PRODUCT8 = Product.builder().id(8L).name("FunKey")//
    .description("61 Keyboard noir Set incl. Support de Clavier. Banquette école de clavier inclus")//
    .quantity(1L).unitPrice(new BigDecimal("111.87")).price(new BigDecimal("111.87")).imageUrl(
    "img/funkey-61-keyboard-noir-set-incl-support-de-clav.jpg").build();

    final Product PRODUCT9 = Product.builder().id(9L).name("LEGEND ")//
    .description("Pack Guitare Type Stratocaster Black Mat")//
    .quantity(2L).unitPrice(new BigDecimal("137.99")).price(new BigDecimal("137.99")).imageUrl(
    "img/legend-pack-guitare-type-stratocaster-black-mat.jpg").build();

    // INFORMATIQUE
    final Product PRODUCT10 = Product.builder().id(10L).name("OMEN HP PC Gamer")//
    .description("15,6\" FHD - AMD Ryzen 7 - RAM 16Go - Stockage 512Go SSD - GTX 1660Ti 6Go - Win 10 - AZERTY")//
    .quantity(1L).unitPrice(new BigDecimal("1219.99")).price(new BigDecimal("1219.99")).imageUrl(
    "img/omen-by-hp-pc-gamer-15-en0002nf-15-6-fhd-amd.jpg").build();

    final Product PRODUCT11 = Product.builder().id(11L).name("CORSAIR Clavier Gamer")//
    .description("Membrane K55 RGB - AZERTY (CH-9206015-FR)")//
    .quantity(1L).unitPrice(new BigDecimal("74.99")).price(new BigDecimal("74.99")).imageUrl(
    "img/corsair-clavier-gamer-membrane-k55-rgb-azerty-c.jpg").build();

    final Product PRODUCT12 = Product.builder().id(12L).name("HP LaserJet Pro M130a")//
    .description("Imprimante Laser Monochrome Multifonction")//
    .quantity(1L).unitPrice(new BigDecimal("157.94")).price(new BigDecimal("157.94")).imageUrl(
    "img/hp-laserjet-pro-m130a-imprimante-laser-monochrom.jpg").build();

    // TELEPHONIE
    final Product PRODUCT13 = Product.builder().id(13L).name("Samsung Galaxy")//
    .description("S20 FE 5G Blanc")//
    .quantity(1L).unitPrice(new BigDecimal("759.00")).price(new BigDecimal("759.00")).imageUrl("img/samsung-galaxy-s20-fe-5g-blanc.jpg").build();

    final Product PRODUCT14 = Product.builder().id(14L).name("Ecouteur Bluetooth V5.0 3000mAh")//
    .description("Arbily T22 Antibruit CVC8.0 Hi-FI Son Oreillette Bluetooth Sport Etanche avec Micro Intégré")//
    .quantity(1L).unitPrice(new BigDecimal("30.99")).price(new BigDecimal("30.99")).imageUrl(
    "img/ecouteur-bluetooth-v5-0-3000mah-arbily-t22-antibru.jpg").build();

    // Liste des produits
    public static final List<Product> PRODUCTS()
    {
        final List<Product> products = Lists.newArrayList();
        products.add(PRODUCT_1);
        products.add(PRODUCT_2);
        products.add(PRODUCT_3);
        products.add(PRODUCT_4);
        products.add(PRODUCT_5);
        products.add(PRODUCT_6);
        products.add(PRODUCT_7);
        products.add(PRODUCT_8);
        products.add(PRODUCT_9);
        products.add(PRODUCT_10);
        products.add(PRODUCT_11);
        products.add(PRODUCT_12);
        products.add(PRODUCT_13);
        products.add(PRODUCT_14);
        return products;
    }

    public static final List<Product> PRODUCTS_WITH_ID()
    {
        final List<Product> products = Lists.newArrayList();
        products.add(PRODUCT);
        products.add(PRODUCT2);
        products.add(PRODUCT3);
        products.add(PRODUCT4);
        products.add(PRODUCT5);
        products.add(PRODUCT6);
        products.add(PRODUCT7);
        products.add(PRODUCT8);
        products.add(PRODUCT9);
        products.add(PRODUCT10);
        products.add(PRODUCT11);
        products.add(PRODUCT12);
        products.add(PRODUCT13);
        products.add(PRODUCT14);
        return products;
    }

    public static void assertAllProduct(final Product expected, final Product actual)
    {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getUnitPrice()).isEqualTo(expected.getUnitPrice());
        assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl());
    }

    public static void assertProductAndProductDTO(final Product expected, final ProductDTO actual)
    {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getUnitPrice()).isEqualTo(expected.getUnitPrice());
        assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity());
        assertThat(actual.getPrice()).isEqualTo(expected.getPrice());
        assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl());
    }

    public static void assertAllCategories(final Category expected, final Category actual)
    {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getEnabled()).isEqualTo(expected.getEnabled());
        assertThat(actual.getCategoryType()).isEqualTo(expected.getCategoryType());
        assertThat(actual.getProducts()).isEqualTo(expected.getProducts());
        assertThat(actual.getProducts().size()).isEqualTo(expected.getProducts().size());
    }

    public static void assertCategoryAndCategoryDTO(final Category expected, final CategoryDTO actual)
    {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getEnabled()).isEqualTo(expected.getEnabled());
        assertThat(actual.getType()).isEqualTo(expected.getCategoryType().name());
        assertThat(actual.getProducts().size()).isEqualTo(expected.getProducts().size());
        assertThat(actual.getProducts().size()).isEqualTo(expected.getProducts().size());
    }

    public final Product PRODUCT_1_SAVED = Product.builder().id(8L).name("TEFAL L2008902")//
    .description("Batterie de cuisine 10 pièces Ingenio Essential - Tous feux sauf induction")//
    .quantity(5L).unitPrice(new BigDecimal("5.539")).price(new BigDecimal("55.39")).imageUrl(
    "img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg").build();

    final Product PRODUCT_7_SAVED = Product.builder().id(18L).name("PHILIPS FC8243/09")//
    .description("Aspirateur traîneau avec sac PowerGo - Suceur Plat Intégré - 750W - 77 dB - A - Rouge Sportif")//
    .quantity(1L).unitPrice(new BigDecimal("5.499")).price(new BigDecimal("54.99")).imageUrl(
    "img/philips-fc8243-09-aspirateur-traineau-avec-sac-pow.jpg").build();

    final Product PRODUCT_4_SAVED = Product.builder().id(17L).name("Lave-vaisselle ELECTROLUX ESF8650ROW")//
    .description("15 couverts - Largeur 60 cm - Classe A+++ - 44 dB - Blanc")//
    .quantity(1L).unitPrice(new BigDecimal("439.99")).price(new BigDecimal("439.99")).imageUrl(
    "img/lave-vaisselle-pose-libre-electrolux-esf8650row.jpg").build();

    public static final Set<Product> ELECTROS_SAVED()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(PRODUCT_1_SAVED);
        products.add(PRODUCT_7_SAVED);
        products.add(PRODUCT_4_SAVED);
        return products;
    }

    ////////////////////////////////
    ///// FABRICATION DES DTO
    ////////////////////////////////

    public final ProductDTO PRODUCT_DTO = ProductDTO.builder().id(1L).name("TEFAL L2008902")//
    .description("Batterie de cuisine 10 pièces Ingenio Essential - Tous feux sauf induction")//
    .quantity(5L).unitPrice(new BigDecimal("5.539")).price(new BigDecimal("55.39")).imageUrl(
    "img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg").build();

    final ProductDTO PRODUCT_DTO2 = ProductDTO.builder().id(2L).name("PHILIPS FC8243/09")//
    .description("Aspirateur traîneau avec sac PowerGo - Suceur Plat Intégré - 750W - 77 dB - A - Rouge Sportif")//
    .quantity(1L).unitPrice(new BigDecimal("5.499")).price(new BigDecimal("54.99")).imageUrl(
    "img/philips-fc8243-09-aspirateur-traineau-avec-sac-pow.jpg").build();

    final ProductDTO PRODUCT_DTO3 = ProductDTO.builder().id(3L).name("Lave-vaisselle ELECTROLUX ESF8650ROW")//
    .description("15 couverts - Largeur 60 cm - Classe A+++ - 44 dB - Blanc")//
    .quantity(1L).unitPrice(new BigDecimal("439.99")).price(new BigDecimal("439.99")).imageUrl(
    "img/lave-vaisselle-pose-libre-electrolux-esf8650row.jpg").build();

    // MEUBLE-DECO
    public final ProductDTO PRODUCT_DTO4 = ProductDTO.builder().id(4L).name("BARCELONE")//
    .description("Canapé d'angle convertible 4 places + coffre de rangement - Tissu et simili Noir et Gris")//
    .quantity(1L).unitPrice(new BigDecimal("499.99")).price(new BigDecimal("499.99")).imageUrl(
    "img/barcelone-canape-d-angle-convertible-4-places-co.jpg").build();

    final ProductDTO PRODUCT_DTO5 = ProductDTO.builder().id(5L).name("TORPE")//
    .description("Bureau informatique contemporain 90x74x50 cm | Taille compacte + support clavier + tiroir | Table ordinateur | Sonoma")//
    .quantity(1L).unitPrice(new BigDecimal("259.99")).price(new BigDecimal("259.99")).imageUrl(
    "img/torpe-bureau-informatique-contemporain-90x74x50.jpg").build();

    final ProductDTO PRODUCT_DTO6 = ProductDTO.builder().id(6L).name("COREP")//
    .description("Lampadaire en métal - E27 - 40 W - Noir et cuivre")//
    .quantity(1L).unitPrice(new BigDecimal("29.99")).price(new BigDecimal("29.99")).imageUrl("img/corep-lampadaire-en-metal-e27-40-w-noir-et-c.jpg")
    .build();

    private static final Set<ProductDTO> MEUBLES_DTO()
    {
        final Set<ProductDTO> products = Sets.newHashSet();
        products.add(PRODUCT_DTO4);
        products.add(PRODUCT_DTO5);
        products.add(PRODUCT_DTO6);
        return products;
    }

    public static final Set<ProductDTO> ELECTROS_DTO()
    {
        final Set<ProductDTO> products = Sets.newHashSet();
        products.add(PRODUCT_DTO);
        products.add(PRODUCT_DTO2);
        products.add(PRODUCT_DTO3);
        return products;
    }

    public static final CategoryDTO CATEGORY_DTO = CategoryDTO.builder().id(1L)//
    .type(CategoryTypeEnum.ELCETROMENAGER.name())//
    .name(CategoryTypeEnum.ELCETROMENAGER.name())//
    .description("Catégorie des produits ELCETRO-MENAGER").products(ELECTROS_DTO()).build();

    public static final CategoryDTO CATEGORY_DTO2 = CategoryDTO.builder().id(2L)//
    .type(CategoryTypeEnum.MEUBLES_DECO.name())//
    .name(CategoryTypeEnum.MEUBLES_DECO.name())//
    .description("Catégorie des produits MEUBLES-DECO").products(MEUBLES_DTO()).build();

    public static final List<CategoryDTO> CATEGORIES_DTO()
    {
        final List<CategoryDTO> categories = Lists.newArrayList();
        categories.add(CATEGORY_DTO);
        categories.add(CATEGORY_DTO2);
        return categories;
    }

    public static final List<CategoryDTO> CATEGORIES_DTO_SINGLE()
    {
        final List<CategoryDTO> categories = Lists.newArrayList();
        categories.add(CATEGORY_DTO);
        return categories;
    }

    // GESTION DES CATEGORIES DE PRODUITS
    public static final Set<Product> ELECTROS()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(PRODUCT_1);
        products.add(PRODUCT_7);
        products.add(PRODUCT_4);
        return products;
    }

    public static final Set<Product> ELECTROS_WITH_ID()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(PRODUCT);
        products.add(PRODUCT2);
        products.add(PRODUCT3);
        return products;
    }

    public static final Category CATEGORY_1 = Category.builder().categoryType(CategoryTypeEnum.ELCETROMENAGER)//
    .name(CategoryTypeEnum.ELCETROMENAGER.name())//
    .description("Catégorie des produits ELCETRO-MENAGER").products(ELECTROS()).build();

    public static final Category CATEGORY = Category.builder().id(1L).categoryType(CategoryTypeEnum.ELCETROMENAGER)//
    .name(CategoryTypeEnum.ELCETROMENAGER.name())//
    .description("Catégorie des produits ELCETRO-MENAGER").products(ELECTROS_WITH_ID()).build();

    private static final Set<Product> MEUBLES()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(PRODUCT_2);
        products.add(PRODUCT_8);
        products.add(PRODUCT_9);
        return products;
    }

    private static final Set<Product> MEUBLES_WITH_ID()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(PRODUCT4);
        products.add(PRODUCT5);
        products.add(PRODUCT6);
        return products;
    }

    public static final Category CATEGORY_2 = Category.builder().categoryType(CategoryTypeEnum.MEUBLES_DECO)//
    .name(CategoryTypeEnum.MEUBLES_DECO.name())//
    .description("Catégorie des produits MEUBLES-DECO").products(MEUBLES()).build();

    public static final Category CATEGORY2 = Category.builder().id(2L).categoryType(CategoryTypeEnum.MEUBLES_DECO)//
    .name(CategoryTypeEnum.MEUBLES_DECO.name())//
    .description("Catégorie des produits MEUBLES-DECO").products(MEUBLES_WITH_ID()).build();

    private static final Set<Product> SONS()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(PRODUCT_3);
        products.add(PRODUCT_10);
        products.add(PRODUCT_11);
        return products;
    }

    private static final Set<Product> SONS_WITH_ID()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(PRODUCT7);
        products.add(PRODUCT8);
        products.add(PRODUCT9);
        return products;
    }

    public static final Category CATEGORY_3 = Category.builder().categoryType(CategoryTypeEnum.SON)//
    .name(CategoryTypeEnum.SON.name())//
    .description("Catégorie des produits de SON").products(SONS()).build();

    public static final Category CATEGORY3 = Category.builder().id(3L).categoryType(CategoryTypeEnum.SON)//
    .name(CategoryTypeEnum.SON.name())//
    .description("Catégorie des produits de SON").products(SONS_WITH_ID()).build();

    private static final Set<Product> INFORMATIQUE()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(PRODUCT_5);
        products.add(PRODUCT_12);
        products.add(PRODUCT_13);
        return products;
    }

    private static final Set<Product> INFORMATIQUE_WITH_ID()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(PRODUCT10);
        products.add(PRODUCT11);
        products.add(PRODUCT12);
        return products;
    }

    public static final Category CATEGORY_4 = Category.builder().categoryType(CategoryTypeEnum.INFORMATIQUE)//
    .name(CategoryTypeEnum.INFORMATIQUE.name())//
    .description("Catégorie des produits INFORMATIQUE").products(INFORMATIQUE()).build();

    public static final Category CATEGORY4 = Category.builder().id(4L).categoryType(CategoryTypeEnum.INFORMATIQUE)//
    .name(CategoryTypeEnum.INFORMATIQUE.name())//
    .description("Catégorie des produits INFORMATIQUE").products(INFORMATIQUE_WITH_ID()).build();

    private static final Set<Product> TELEPHONIE()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(PRODUCT_6);
        products.add(PRODUCT_14);
        return products;
    }

    private static final Set<Product> TELEPHONIEE_WITH_ID()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(PRODUCT13);
        products.add(PRODUCT14);
        return products;
    }

    public static final Category CATEGORY_5 = Category.builder().categoryType(CategoryTypeEnum.TELEPHONIE)//
    .name(CategoryTypeEnum.TELEPHONIE.name())//
    .description("Catégorie des produits TELEPHONIE").products(TELEPHONIE()).build();

    public static final Category CATEGORY5 = Category.builder().id(5L).categoryType(CategoryTypeEnum.TELEPHONIE)//
    .name(CategoryTypeEnum.TELEPHONIE.name())//
    .description("Catégorie des produits TELEPHONIE").products(TELEPHONIEE_WITH_ID()).build();

    // Liste des produits
    public static final List<Category> CATEGORIES()
    {
        final List<Category> categories = Lists.newArrayList();
        categories.add(CATEGORY_1);
        categories.add(CATEGORY_2);
        categories.add(CATEGORY_3);
        categories.add(CATEGORY_4);
        categories.add(CATEGORY_5);
        return categories;
    }

    public static final List<Category> CATEGORIES_WITH_ID()
    {
        final List<Category> categories = Lists.newArrayList();
        categories.add(CATEGORY);
        categories.add(CATEGORY2);
        categories.add(CATEGORY3);
        categories.add(CATEGORY4);
        categories.add(CATEGORY5);
        return categories;
    }

    // CREDENTIALS
    /**
     * Fournir le jeu de données pour intialisser la base de données pour les tests unitaires et d'intégration de façon
     * programmatique.
     * 
     * @return le jeu de données.
     */
    public static final List<User> creerJeuDeDonnees()
    {
        final List<User> users = Lists.newArrayList();
        // ADMIN
        final User admin = createUser(RoleEnum.ROLE_ADMIN.getAuthority(), "admin", "admin_19511982#", "admin.test@live.fr");

        // CLIENT
        final User client = createUser(RoleEnum.ROLE_USER.getAuthority(), "client", "client_19511982#", "client.test@live.fr");
        final User client1 = createUser(RoleEnum.ROLE_USER.getAuthority(), "client1", "client31_19511982#", "client1.test@live.fr");
        final User client2 = createUser(RoleEnum.ROLE_USER.getAuthority(), "client2", "client2_19511982#", "client2.test@live.fr");
        final User client3 = createUser(RoleEnum.ROLE_USER.getAuthority(), "client3", "client3_19511982#", "client3.test@live.fr");

        // MODERATEUR
        final Set<RoleEnum> moderateurSet = new HashSet<>();
        moderateurSet.add(RoleEnum.valueOf(RoleEnum.ROLE_USER.getAuthority()));
        moderateurSet.add(RoleEnum.valueOf(RoleEnum.ROLE_MODERATOR.getAuthority()));
        final User moderateur = createUserWithSet(moderateurSet, "moderateur", "moderateur_19511982#", "moderateur.test@live.fr");

        users.add(admin);
        users.add(client);
        users.add(client1);
        users.add(client2);
        users.add(client3);
        users.add(moderateur);
        return users;
    }

    /**
     * @param pRole
     * @param pUsername
     * @param pPwd
     * @param pEmail
     * @return
     */
    public static User createUser(final String pRole, final String pUsername, final String pPwd, final String pEmail)
    {
        // ACTIVE USER
        final Set<RoleEnum> roleEnums = new HashSet<>();
        roleEnums.add(RoleEnum.valueOf(pRole));
        final User user = User.builder()//
        .username(pUsername)//
        .password(pwdEncoder().encode(pPwd))//
        .email(pEmail)//
        .roles(roleEnums)//
        .build();
        return user;
    }

    /**
     * @param pRoleEnums
     * @param pUsername
     * @param pPwd
     * @param pEmail
     * @return
     */
    public static User createUserWithSet(final Set<RoleEnum> pRoleEnums, final String pUsername, final String pPwd, final String pEmail)
    {
        // ACTIVE USER
        final User user = User.builder()//
        .username(pUsername)//
        .password(pwdEncoder().encode(pPwd))//
        .email(pEmail)//
        .roles(pRoleEnums)//
        .build();
        return user;
    }

    /**
     * @param pRoleEnums
     * @param pUsername
     * @param pPwd
     * @param pEmail
     * @return
     */
    public static User createUserWithSetFull(final Set<RoleEnum> pRoleEnums, final String pUsername, final String pPwd, final String pEmail)
    {
        // ACTIVE USER
        final User user = User.builder()//
        .username(pUsername)//
        .password(pwdEncoder().encode(pPwd))//
        .email(pEmail)//
        .accountExpired(Boolean.FALSE)//
        .accountLocked(Boolean.FALSE)//
        .credentialsExpired(Boolean.FALSE)//
        .enabled(Boolean.TRUE)//
        .roles(pRoleEnums)//
        .createdTime(LocalDateTime.now(ZoneId.systemDefault()))//
        .updatedTime(LocalDateTime.now(ZoneId.systemDefault()))//
        .build();
        return user;
    }

    /**
     * @param expected
     * @param actual
     */
    public static void assertAllUser(final User expected, final User actual)
    {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getAccountExpired()).isEqualTo(expected.getAccountExpired());
        assertThat(actual.getAccountLocked()).isEqualTo(expected.getAccountLocked());
        assertThat(actual.getCredentialsExpired()).isEqualTo(expected.getCredentialsExpired());
        assertThat(actual.getEnabled()).isEqualTo(expected.getEnabled());
        assertThat(actual.getRoles()).isEqualTo(expected.getRoles());
        assertThat(actual.getCreatedTime()).isEqualTo(expected.getCreatedTime());
        assertThat(actual.getUpdatedTime()).isEqualTo(expected.getUpdatedTime());
    }

    /**
     * @param expected
     * @param actual
     */
    public static void assertAllUserWithoutTime(final User expected, final User actual)
    {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getAccountExpired()).isEqualTo(expected.getAccountExpired());
        assertThat(actual.getAccountLocked()).isEqualTo(expected.getAccountLocked());
        assertThat(actual.getCredentialsExpired()).isEqualTo(expected.getCredentialsExpired());
        assertThat(actual.getEnabled()).isEqualTo(expected.getEnabled());
        assertThat(actual.getRoles()).isEqualTo(expected.getRoles());
    }

    /**
     * 
     * @param pRole
     * @param pUsername
     * @param pPwd
     * @param pEmail
     * @return
     */
    public static UserDTO createUserDTOFull(final String pRole, final String pUsername, final String pPwd, final String pEmail)
    {
        final Set<String> strRoles = new HashSet<>();
        strRoles.add(pRole);

        // ACTIVE USER
        final UserDTO dto = UserDTO.builder()//
        .username(pUsername)//
        // .password(pwdEncoder().encode(pPwd))//
        .password(pPwd)//
        .email(pEmail)//
        .accountExpired(Boolean.FALSE)//
        .accountLocked(Boolean.FALSE)//
        .credentialsExpired(Boolean.FALSE)//
        .enabled(Boolean.TRUE)//
        .roles(strRoles)//
        .createdTime(LocalDateTime.now(ZoneId.systemDefault()))//
        .updatedTime(LocalDateTime.now(ZoneId.systemDefault()))//
        .build();
        return dto;
    }
    
    /**
     * 
     * @return
     */
    public static final List<UserDTO> creerJeuDeDonneesDTO()
    {
        final List<UserDTO> users = Lists.newArrayList();
        // ADMIN
        final UserDTO admin = createUserDTOFull(RoleEnum.ROLE_ADMIN.getAuthority(), "admin", "admin_19511982#", "admin.test@live.fr");

        // CLIENT
        final UserDTO client = createUserDTOFull(RoleEnum.ROLE_USER.getAuthority(), "client", "client_19511982#", "client.test@live.fr");
        final UserDTO client1 = createUserDTOFull(RoleEnum.ROLE_USER.getAuthority(), "client1", "client31_19511982#", "client1.test@live.fr");
        final UserDTO client2 = createUserDTOFull(RoleEnum.ROLE_USER.getAuthority(), "client2", "client2_19511982#", "client2.test@live.fr");
        final UserDTO client3 = createUserDTOFull(RoleEnum.ROLE_USER.getAuthority(), "client3", "client3_19511982#", "client3.test@live.fr");

        // MODERATEUR
        final Set<String> moderateurSet = new HashSet<>();
        moderateurSet.add(RoleEnum.ROLE_USER.getAuthority());
        moderateurSet.add(RoleEnum.ROLE_MODERATOR.getAuthority());
        final UserDTO moderateur = createUserDTOWithSetFull(moderateurSet, "moderateur", "moderateur_19511982#", "moderateur.test@live.fr");

        users.add(admin);
        users.add(client);
        users.add(client1);
        users.add(client2);
        users.add(client3);
        users.add(moderateur);
        return users;
    }

    /**
     * @param pRoleEnums
     * @param pUsername
     * @param pPwd
     * @param pEmail
     * @return
     */
    public static UserDTO createUserDTOWithSetFull(final Set<String> pRoleEnums, final String pUsername, final String pPwd, final String pEmail)
    {
        // ACTIVE USER
        final UserDTO dto = UserDTO.builder()//
        .username(pUsername)//
        // .password(pwdEncoder().encode(pPwd))//
        .password(pPwd)//
        .email(pEmail)//
        .accountExpired(Boolean.FALSE)//
        .accountLocked(Boolean.FALSE)//
        .credentialsExpired(Boolean.FALSE)//
        .enabled(Boolean.TRUE)//
        .roles(pRoleEnums)//
        .createdTime(LocalDateTime.now(ZoneId.systemDefault()))//
        .updatedTime(LocalDateTime.now(ZoneId.systemDefault()))//
        .build();
        return dto;
    }

    /**
     * @param expected
     * @param actual
     */
    public static void assertAllUserDTO(final UserDTO expected, final UserDTO actual)
    {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getAccountExpired()).isEqualTo(expected.getAccountExpired());
        assertThat(actual.getAccountLocked()).isEqualTo(expected.getAccountLocked());
        assertThat(actual.getCredentialsExpired()).isEqualTo(expected.getCredentialsExpired());
        assertThat(actual.getEnabled()).isEqualTo(expected.getEnabled());
        assertThat(actual.getRoles()).isEqualTo(expected.getRoles());
        assertThat(actual.getCreatedTime()).isEqualTo(expected.getCreatedTime());
        assertThat(actual.getUpdatedTime()).isEqualTo(expected.getUpdatedTime());
    }

    /**
     * @param expected
     * @param actual
     */
    public static void assertAllUserDTOWithoutTime(final UserDTO expected, final UserDTO actual)
    {
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getUsername()).isEqualTo(expected.getUsername());
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
        assertThat(actual.getAccountExpired()).isEqualTo(expected.getAccountExpired());
        assertThat(actual.getAccountLocked()).isEqualTo(expected.getAccountLocked());
        assertThat(actual.getCredentialsExpired()).isEqualTo(expected.getCredentialsExpired());
        assertThat(actual.getEnabled()).isEqualTo(expected.getEnabled());
        assertThat(actual.getRoles()).isEqualTo(expected.getRoles());
    }

    /**
     * @return
     */
    private static BCryptPasswordEncoder pwdEncoder()
    {
        return new BCryptPasswordEncoder(12);
    }
}
