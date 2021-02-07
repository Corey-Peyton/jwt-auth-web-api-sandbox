/*  --------------------------------------
	-- DML : Data Manipulation Language
	-- BASE DE DONNEES : H2 
	-- SCHEMA OR CATALOG :  
	-------------------------------------
*/

/* Insertion dans la table T_CATEGORIES */
INSERT INTO T_CATEGORIES (ID, CATEGORY_TYPE, DESCRIPTION, ENABLED, NAME, OPTLOCK) 
VALUES
	(7, 'ELCETROMENAGER', 'Catégorie des produits ELCETRO-MENAGER', 'TRUE', 'ELCETROMENAGER', 0),
	(9, 'MEUBLES_DECO', 'Catégorie des produits MEUBLES-DECO', 'TRUE', 'MEUBLES-DECO', 0),
	(11, 'SON', 'Catégorie des produits de SON', 'TRUE', 'SON', 0),
	(13, 'INFORMATIQUE', 'Catégorie des produits INFORMATIQUE', 'TRUE', 'INFORMATIQUE', 0),
	(15, 'TELEPHONIE', 'Catégorie des produits TELEPHONIE', 'TRUE', 'TELEPHONIE', 0);

	
/* Insertion dans la table T_PRODUCTS */
INSERT INTO T_PRODUCTS (ID, DESCRIPTION, IMAGE_URL, IS_ACTIVE, NAME, PRICE, QUANTITY, UNIT_PRICE, OPTLOCK) 
VALUES
	(8, 'Batterie de cuisine 10 pièces Ingenio Essential - Tous feux sauf induction', 'img/tefal-l2008902-batterie-de-cuisine-10-pieces-ingen.jpg', 'TRUE', 'TEFAL L2008902', 55.39, 5, 5.54, 1),
	(10, 'Canapé d''angle convertible 4 places + coffre de rangement - Tissu et simili Noir et Gris', 'img/barcelone-canape-d-angle-convertible-4-places-co.jpg', 'TRUE', 'BARCELONE', 499.99, 1, 499.99, 0),
	(12, 'Chaîne hifi stereo compacte avec platine vinyle , lecteur CD & radio , encodageMP3 , ports USB/SD , haut-parleur 2 voies', 'img/chaine-stereo-platine-vinyle-enregistrement-mp3.jpg', 'TRUE', 'AUNA DS-2', 154.99, 1, 154.99, 0),
	(14, '15,6" FHD - AMD Ryzen 7 - RAM 16Go - Stockage 512Go SSD - GTX 1660Ti 6Go - Win 10 - AZERTY', 'img/omen-by-hp-pc-gamer-15-en0002nf-15-6-fhd-amd.jpg', 'TRUE', 'OMEN HP PC Gamer', 1219.99, 1, 1219.99, 0),
	(16, 'S20 FE 5G Blanc', 'img/samsung-galaxy-s20-fe-5g-blanc.jpg', 'TRUE', 'Samsung Galaxy', 759.00, 1, 759.00, 0),
	(17, '15 couverts - Largeur 60 cm - Classe A+++ - 44 dB - Blanc', 'img/lave-vaisselle-pose-libre-electrolux-esf8650row.jpg', 'TRUE', 'Lave-vaisselle ELECTROLUX ESF8650ROW', 439.99, 1, 439.99, 0),
	(18, 'Aspirateur traîneau avec sac PowerGo - Suceur Plat Intégré - 750W - 77 dB - A - Rouge Sportif', 'img/philips-fc8243-09-aspirateur-traineau-avec-sac-pow.jpg', 'TRUE', 'PHILIPS FC8243/09 ', 54.99, 1, 5.50, 0),
	(19, 'Bureau informatique contemporain 90x74x50 cm | Taille compacte + support clavier + tiroir | Table ordinateur | Sonoma', 'img/torpe-bureau-informatique-contemporain-90x74x50.jpg', 'TRUE', 'TORPE', 259.99, 1, 259.99, 0),
	(20, 'Lampadaire en métal - E27 - 40 W - Noir et cuivre', 'img/corep-lampadaire-en-metal-e27-40-w-noir-et-c.jpg', 'TRUE', 'COREP', 29.99, 1, 29.99, 0),
	(21, '61 Keyboard noir Set incl. Support de Clavier. Banquette école de clavier inclus', 'img/funkey-61-keyboard-noir-set-incl-support-de-clav.jpg', 'TRUE', 'FunKey', 111.87, 1, 111.87, 0),
	(22, 'Pack Guitare Type Stratocaster Black Mat', 'img/legend-pack-guitare-type-stratocaster-black-mat.jpg', 'TRUE', 'LEGEND ', 137.99, 2, 137.99, 0),
	(23, 'Membrane K55 RGB - AZERTY (CH-9206015-FR)', 'img/corsair-clavier-gamer-membrane-k55-rgb-azerty-c.jpg', 'TRUE', 'CORSAIR Clavier Gamer', 74.99, 1, 74.99, 0),
	(24, 'Imprimante Laser Monochrome Multifonction', 'img/hp-laserjet-pro-m130a-imprimante-laser-monochrom.jpg', 'TRUE', 'HP LaserJet Pro M130a', 157.94, 1, 157.94, 0),
	(25, 'Arbily T22 Antibruit CVC8.0 Hi-FI Son Oreillette Bluetooth Sport Etanche avec Micro Intégré', 'img/ecouteur-bluetooth-v5-0-3000mah-arbily-t22-antibru.jpg', 'TRUE', 'Ecouteur Bluetooth V5.0 3000mAh', 30.99, 1, 30.99, 0);

/* Insertion dans la table T_CATEGORIES_T_PRODUCTS */
INSERT INTO T_CATEGORIES_T_PRODUCTS (CATEGORY_ID, PRODUCTS_ID) 
VALUES
	(7, 8),
	(9, 10),
	(11, 12),
	(13, 14),
	(15, 16);