/*  --------------------------------------
	-- DML : Data Manipulation Language
	-- BASE DE DONNEES : MARIADB
	-- SCHEMA OR CATALOG : JWTAUTHWEB  
	-------------------------------------
*/

/* Insertion dans la table T_CATEGORIES */
INSERT INTO `T_CATEGORIES` (`ID`, `CATEGORY_TYPE`, `DESCRIPTION`, `ENABLED`, `NAME`, `OPTLOCK`) 
VALUES
	(7, 'ELCETROMENAGER', 'Catégorie des produits ELCETRO-MENAGER', b'1', 'ELCETROMENAGER', 0),
	(9, 'MEUBLES_DECO', 'Catégorie des produits MEUBLES-DECO', b'1', 'MEUBLES-DECO', 0),
	(11, 'SON', 'Catégorie des produits de SON', b'1', 'SON', 0),
	(13, 'INFORMATIQUE', 'Catégorie des produits INFORMATIQUE', b'1', 'INFORMATIQUE', 0),
	(15, 'TELEPHONIE', 'Catégorie des produits TELEPHONIE', b'1', 'TELEPHONIE', 0);

/* Insertion dans la table T_PRODUCTS */
INSERT INTO `T_PRODUCTS` (`ID`, `DESCRIPTION`, `IMAGE_URL`, `IS_ACTIVE`, `NAME`, `PRICE`, `QUANTITY`, `UNIT_PRICE`, `OPTLOCK`) 
VALUES
	(8, 'Batterie de cuisine 10 pièces Ingenio Essential - Tous feux sauf induction', 'img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg', b'1', 'TEFAL L2008902', 55.39, 5, 5.54, 1),
	(10, 'Canapé d\'angle convertible 4 places + coffre de rangement - Tissu et simili Noir et Gris', 'img/barcelone-canape-d-angle-convertible-4-places-co.jpg', b'1', 'BARCELONE', 499.99, 1, 499.99, 0),
	(12, 'Chaîne hifi stereo compacte avec platine vinyle , lecteur CD & radio , encodageMP3 , ports USB/SD , haut-parleur 2 voies', 'img/chaine-stereo-platine-vinyle-enregistrement-mp3.jpg', b'1', 'AUNA DS-2', 154.99, 1, 154.99, 0),
	(14, '15,6" FHD - AMD Ryzen 7 - RAM 16Go - Stockage 512Go SSD - GTX 1660Ti 6Go - Win 10 - AZERTY', 'img/omen-by-hp-pc-gamer-15-en0002nf-15-6-fhd-amd.jpg', b'1', 'OMEN HP PC Gamer', 1219.99, 1, 1219.99, 0),
	(16, 'S20 FE 5G Blanc', 'img/samsung-galaxy-s20-fe-5g-blanc.jpg', b'1', 'Samsung Galaxy', 759.00, 1, 759.00, 0),
	(17, '15 couverts - Largeur 60 cm - Classe A+++ - 44 dB - Blanc', 'img/lave-vaisselle-pose-libre-electrolux-esf8650row.jpg', b'1', 'Lave-vaisselle ELECTROLUX ESF8650ROW', 439.99, 1, 439.99, 0),
	(18, 'Aspirateur traîneau avec sac PowerGo - Suceur Plat Intégré - 750W - 77 dB - A - Rouge Sportif', 'img/philips-fc8243-09-aspirateur-traineau-avec-sac-pow.jpg', b'1', 'PHILIPS FC8243/09 ', 54.99, 1, 5.50, 0),
	(19, 'Bureau informatique contemporain 90x74x50 cm | Taille compacte + support clavier + tiroir | Table ordinateur | Sonoma', 'img/torpe-bureau-informatique-contemporain-90x74x50.jpg', b'1', 'TORPE', 259.99, 1, 259.99, 0),
	(20, 'Lampadaire en métal - E27 - 40 W - Noir et cuivre', 'img/corep-lampadaire-en-metal-e27-40-w-noir-et-c.jpg', b'1', 'COREP', 29.99, 1, 29.99, 0),
	(21, '61 Keyboard noir Set incl. Support de Clavier. Banquette école de clavier inclus', 'img/funkey-61-keyboard-noir-set-incl-support-de-clav.jpg', b'1', 'FunKey', 111.87, 1, 111.87, 0),
	(22, 'Pack Guitare Type Stratocaster Black Mat', 'img/legend-pack-guitare-type-stratocaster-black-mat.jpg', b'1', 'LEGEND ', 137.99, 2, 137.99, 0),
	(23, 'Membrane K55 RGB - AZERTY (CH-9206015-FR)', 'img/corsair-clavier-gamer-membrane-k55-rgb-azerty-c.jpg', b'1', 'CORSAIR Clavier Gamer', 74.99, 1, 74.99, 0),
	(24, 'Imprimante Laser Monochrome Multifonction', 'img/hp-laserjet-pro-m130a-imprimante-laser-monochrom.jpg', b'1', 'HP LaserJet Pro M130a', 157.94, 1, 157.94, 0),
	(25, 'Arbily T22 Antibruit CVC8.0 Hi-FI Son Oreillette Bluetooth Sport Etanche avec Micro Intégré', 'img/ecouteur-bluetooth-v5-0-3000mah-arbily-t22-antibru.jpg', b'1', 'Ecouteur Bluetooth V5.0 3000mAh', 30.99, 1, 30.99, 0);

/* Insertion dans la table T_CATEGORIES_T_PRODUCTS */
INSERT INTO `T_CATEGORIES_T_PRODUCTS` (`CATEGORY_ID`, `PRODUCTS_ID`) 
VALUES
	(7, 8),
	(9, 10),
	(11, 12),
	(13, 14),
	(15, 16);