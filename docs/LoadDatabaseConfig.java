/*
 * ----------------------------------------------
 * Projet ou Module : jwt-auth-web-api-back-end
 * Nom de la classe : BackendApplicationStarter.java
 * Date de création : 18 janv. 2021
 * Heure de création : 07:33:40
 * Package : fr.vincent.tuto.server
 * Auteur : Vincent Otchoun
 * Copyright © 2021 - All rights reserved.
 * ----------------------------------------------
 */
package fr.vincent.tuto.server.utils;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import fr.vincent.tuto.server.config.BackEndServerRootConfig;
import fr.vincent.tuto.server.dao.CategoryDAO;
import fr.vincent.tuto.server.dao.ProductDAO;
import fr.vincent.tuto.server.dao.UserDAO;
import fr.vincent.tuto.server.enumeration.CategoryTypeEnum;
import fr.vincent.tuto.server.enumeration.RoleEnum;
import fr.vincent.tuto.server.model.po.Category;
import fr.vincent.tuto.server.model.po.Product;
import fr.vincent.tuto.server.model.po.User;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration d'alimentation des différentes tables de la base de données de l'application au démarrage.
 * 
 * @author Vincent Otchoun
 */
@Configuration
@Import(value = { BackEndServerRootConfig.class })
@Slf4j
public class LoadDatabaseConfig
{
    @Autowired
    private BCryptPasswordEncoder pwdEncoder;

    /**
     * Initialisation au démarrage de la table T_USERS avec les données.
     * 
     * @return
     */
    @Bean
    CommandLineRunner initUsers(@Autowired final UserDAO userDAO)
    {
        log.info("[initUsers] - Initilisation de la table T_USERS "); 
        
        return args -> this.creerUsers()//
        .stream()//
        .filter(Objects::nonNull)//
        .forEach(user -> userDAO.saveAndFlush(user));
    }
    
    // @Bean
    // CommandLineRunner initUsers()
    // {
    // return args -> this.creerUsers().forEach(user -> this.userDAO.saveAndFlush(user));
    // }

    /**
     * Initialisation au démarrage de la table T_CATEGORIES avec les données.
     * 
     * @return
     */
    @Bean
    CommandLineRunner initCategories(@Autowired final CategoryDAO categoryDAO)
    {
        log.info("[initCategories] - Initilisation de la table T_CATEGORIES "); 
        
        return args -> this.categories()//
        .stream()//
        .filter(Objects::nonNull)//
        .forEach(categ -> categoryDAO.saveAndFlush(categ));
    }
    
    // @Bean
    // CommandLineRunner initCategories()
    // {
    // return args -> this.categories().forEach(cat -> this.categoryDAO.saveAndFlush(cat));
    // }

    /**
     * Initialisation au démarrage de la table T_PRODUCTS avec les données.
     * 
     * @return
     */
    @Bean
    CommandLineRunner initProducts(final ProductDAO productDAO)
    {
        log.info("[initProducts] - Initilisation de la table T_PRODUCTS "); 
        
        return args -> this.products()//
        .stream()//
        .filter(Objects::nonNull)//
        .forEach(prod -> productDAO.saveAndFlush(prod));
    }
    
    // @Bean
    // CommandLineRunner initProducts()
    // {
    // return args -> this.products().forEach(prod -> this.productDAO.saveAndFlush(prod));
    // }

    // Liste des produits
    private final List<Category> categories()
    {
        final List<Category> categories = Lists.newArrayList();
        categories.add(category);
        categories.add(category2);
        categories.add(category3);
        categories.add(category4);
        categories.add(category5);
        return categories;
    }

    // Liste des produits
    private List<Product> products()
    {
        final List<Product> products = Lists.newArrayList();
        products.add(product);
        products.add(product4);
        products.add(product7);
        products.add(product3);
        products.add(product10);
        products.add(product13);
        products.add(product2);
        products.add(product5);
        products.add(product6);
        products.add(product8);
        products.add(product9);
        products.add(product11);
        products.add(product12);
        products.add(product14);
        return products;
    }

    // CREDENTIALS
    /**
     * Fournir le jeu de données pour intialisser la base de données pour les tests unitaires et d'intégration de façon
     * programmatique.
     * 
     * @return le jeu de données.
     */
    private final List<User> creerUsers()
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

    // Creation des produits : l'active est gérér dans le modèle métier pour la création
    // ECLETROMENAGER
    private final Product product = Product.builder().name("TEFAL L2008902")//
    .description("Batterie de cuisine 10 pièces Ingenio Essential - Tous feux sauf induction")//
    .quantity(5L).unitPrice(new BigDecimal("5.539")).price(new BigDecimal("55.39")).imageUrl(
    "img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg").build();

    private final Product product2 = Product.builder().name("PHILIPS FC8243/09 ")//
    .description("Aspirateur traîneau avec sac PowerGo - Suceur Plat Intégré - 750W - 77 dB - A - Rouge Sportif")//
    .quantity(1L).unitPrice(new BigDecimal("5.499")).price(new BigDecimal("54.99")).imageUrl(
    "img/philips-fc8243-09-aspirateur-traineau-avec-sac-pow.jpg").build();

    private final Product product3 = Product.builder().name("Lave-vaisselle ELECTROLUX ESF8650ROW")//
    .description("15 couverts - Largeur 60 cm - Classe A+++ - 44 dB - Blanc")//
    .quantity(1L).unitPrice(new BigDecimal("439.99")).price(new BigDecimal("439.99")).imageUrl(
    "img/lave-vaisselle-pose-libre-electrolux-esf8650row.jpg").build();

    // MEUBLE-DECO
    private final Product product4 = Product.builder().name("BARCELONE")//
    .description("Canapé d'angle convertible 4 places + coffre de rangement - Tissu et simili Noir et Gris")//
    .quantity(1L).unitPrice(new BigDecimal("499.99")).price(new BigDecimal("499.99")).imageUrl(
    "img/barcelone-canape-d-angle-convertible-4-places-co.jpg").build();

    private final Product product5 = Product.builder().name("TORPE")//
    .description("Bureau informatique contemporain 90x74x50 cm | Taille compacte + support clavier + tiroir | Table ordinateur | Sonoma")//
    .quantity(1L).unitPrice(new BigDecimal("259.99")).price(new BigDecimal("259.99")).imageUrl(
    "img/torpe-bureau-informatique-contemporain-90x74x50.jpg").build();

    private final Product product6 = Product.builder().name("COREP")//
    .description("Lampadaire en métal - E27 - 40 W - Noir et cuivre")//
    .quantity(1L).unitPrice(new BigDecimal("29.99")).price(new BigDecimal("29.99")).imageUrl("img/corep-lampadaire-en-metal-e27-40-w-noir-et-c.jpg")
    .build();

    // SON
    private final Product product7 = Product.builder().name("AUNA DS-2")//
    .description("Chaîne hifi stereo compacte avec platine vinyle , lecteur CD & radio , encodageMP3 , ports USB/SD , haut-parleur 2 voies")//
    .quantity(1L).unitPrice(new BigDecimal("154.99")).price(new BigDecimal("154.99")).imageUrl(
    "img/chaine-stereo-platine-vinyle-enregistrement-mp3.jpg").build();

    private final Product product8 = Product.builder().name("FunKey")//
    .description("61 Keyboard noir Set incl. Support de Clavier. Banquette école de clavier inclus")//
    .quantity(1L).unitPrice(new BigDecimal("111.87")).price(new BigDecimal("111.87")).imageUrl(
    "img/funkey-61-keyboard-noir-set-incl-support-de-clav.jpg").build();

    private final Product product9 = Product.builder().name("LEGEND ")//
    .description("Pack Guitare Type Stratocaster Black Mat")//
    .quantity(2L).unitPrice(new BigDecimal("137.99")).price(new BigDecimal("137.99")).imageUrl(
    "img/legend-pack-guitare-type-stratocaster-black-mat.jpg").build();

    // INFORMATIQUE
    private final Product product10 = Product.builder().name("OMEN HP PC Gamer")//
    .description("15,6\" FHD - AMD Ryzen 7 - RAM 16Go - Stockage 512Go SSD - GTX 1660Ti 6Go - Win 10 - AZERTY")//
    .quantity(1L).unitPrice(new BigDecimal("1219.99")).price(new BigDecimal("1219.99")).imageUrl(
    "img/omen-by-hp-pc-gamer-15-en0002nf-15-6-fhd-amd.jpg").build();

    private final Product product11 = Product.builder().name("CORSAIR Clavier Gamer")//
    .description("Membrane K55 RGB - AZERTY (CH-9206015-FR)")//
    .quantity(1L).unitPrice(new BigDecimal("74.99")).price(new BigDecimal("74.99")).imageUrl(
    "img/corsair-clavier-gamer-membrane-k55-rgb-azerty-c.jpg").build();

    private final Product product12 = Product.builder().name("HP LaserJet Pro M130a")//
    .description("Imprimante Laser Monochrome Multifonction")//
    .quantity(1L).unitPrice(new BigDecimal("157.94")).price(new BigDecimal("157.94")).imageUrl(
    "img/hp-laserjet-pro-m130a-imprimante-laser-monochrom.jpg").build();

    // TELEPHONIE
    private final Product product13 = Product.builder().name("Samsung Galaxy")//
    .description("S20 FE 5G Blanc")//
    .quantity(1L).unitPrice(new BigDecimal("759.00")).price(new BigDecimal("759.00")).imageUrl("img/samsung-galaxy-s20-fe-5g-blanc.jpg").build();

    private final Product product14 = Product.builder().name("Ecouteur Bluetooth V5.0 3000mAh")//
    .description("Arbily T22 Antibruit CVC8.0 Hi-FI Son Oreillette Bluetooth Sport Etanche avec Micro Intégré")//
    .quantity(1L).unitPrice(new BigDecimal("30.99")).price(new BigDecimal("30.99")).imageUrl(
    "img/ecouteur-bluetooth-v5-0-3000mah-arbily-t22-antibru.jpg").build();

    // GESTION DES CATEGORIES DE PRODUITS
    private final Set<Product> electros()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(product);
        products.add(product2);
        products.add(product3);
        return products;
    }

    private final Category category = Category.builder().categoryType(CategoryTypeEnum.ELCETROMENAGER)//
    .name(CategoryTypeEnum.ELCETROMENAGER.getValue())//
    .description("Catégorie des produits ELCETRO-MENAGER").products(electros()).build();

    private final Set<Product> meubles()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(product4);
        products.add(product5);
        products.add(product6);
        return products;
    }

    private final Category category2 = Category.builder().categoryType(CategoryTypeEnum.MEUBLES_DECO)//
    .name(CategoryTypeEnum.MEUBLES_DECO.getValue())//
    .description("Catégorie des produits MEUBLES-DECO").products(meubles()).build();

    private final Set<Product> sons()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(product7);
        products.add(product8);
        products.add(product9);
        return products;
    }

    private final Category category3 = Category.builder().categoryType(CategoryTypeEnum.SON)//
    .name(CategoryTypeEnum.SON.getValue())//
    .description("Catégorie des produits de SON").products(sons()).build();

    private final Set<Product> informatique()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(product10);
        products.add(product11);
        products.add(product12);
        return products;
    }

    private final Category category4 = Category.builder().categoryType(CategoryTypeEnum.INFORMATIQUE)//
    .name(CategoryTypeEnum.INFORMATIQUE.getValue())//
    .description("Catégorie des produits INFORMATIQUE").products(informatique()).build();

    private final Set<Product> telephonie()
    {
        final Set<Product> products = Sets.newHashSet();
        products.add(product13);
        products.add(product14);
        return products;
    }

    private final Category category5 = Category.builder().categoryType(CategoryTypeEnum.TELEPHONIE)//
    .name(CategoryTypeEnum.TELEPHONIE.getValue())//
    .description("Catégorie des produits TELEPHONIE").products(telephonie()).build();

    private User createUser(final String pRole, final String pUsername, final String pPwd, final String pEmail)
    {
        // ACTIVE USER
        final Set<RoleEnum> roleEnums = new HashSet<>();
        roleEnums.add(RoleEnum.valueOf(pRole));
        return User.builder()//
        .username(pUsername)//
        .password(this.pwdEncoder.encode(pPwd))//
        .email(pEmail)//
        .roles(roleEnums)//
        .build();
    }

    private User createUserWithSet(final Set<RoleEnum> pRoleEnums, final String pUsername, final String pPwd, final String pEmail)
    {
        // ACTIVE USER
        return User.builder()//
        .username(pUsername)//
        .password(this.pwdEncoder.encode(pPwd))//
        .email(pEmail)//
        .roles(pRoleEnums)//
        .build();
    }
}
