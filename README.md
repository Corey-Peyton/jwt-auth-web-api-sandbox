# Products Management Secure Web REST API    ![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg?branch=develop)

`My Products` est une application Web des gestion de produits avec leur catégorie (service Web RESTFull) écrit en **Java** et embarque **Spring** et d'autres technologies non seulement pour l'intégration des différents composants applicatifs
mais également la sécurisation des ressources de l'application.  Il fournit principalement :
- un **Back-End**, permettant entre autres de :
	- _Gérer les utilisateurs : 
		- _Se Connecter/Déconnecter à l'application_
		- _Ajouter de nouveaux utilisateurs_ dans le SI avec leur rôle 
		- _Mettre à jour les informations_ d'un utilisateur existant.
		- _Supprimer les informations_ de l'utilisateur du SI.
		- _Rechercher les information_s d'un utilisateur dans le SI selon son identifiant. 
		- _Obtenir la liste_ des utilisateurs du système. 
	- _Gérer les produits et leur catégorie : 
		- _Ajouter de nouveaux produits avec leur catégorie_ dans le SI 
		- _Mettre à jour les informations_ d'un produit existant avec sa catégorie.
		- _Supprimer les informations_ d'un produit avec sa catégorie du SI.
		- _Rechercher les information_s d'un produit dans le SI selon son identifiant. 
		- _Obtenir la liste_ des produits avec leur catégorie du système. 		
	- _Sécuriser l'application en proposant : 
		- _La Gestion de l'Authentification_ : qui permet de confirmer ou valider l'identité du client/l’utilisateur qui tente d’accéder au système d'informations. 
		- _La Gestion de l'Autorisation (protection des ressources)_ : permet d’octroyer l’accès au système d’informations (donc aux ressources) au client/l’utilisateur.
	- _Gérer la migration des scripts de base de données_ (création de schémas, insertion, mise à jour de tables ou de données ...) avec **Flyway**.
- un **Front-End** (Client Web) pour interagir avec le Back-End par le biais d'une interface utilisateur. Le front fournit les interfaces pour :
	- Se Connecter à l'application/ Se Déconnecter de l'application.
	- Ajouter/Inscrire un nouvel utilisateur dans le SI.
	- Visualiser les informations des utilisateurs. 
	- Modifier les informations d'un utilisateur.
	- Supprimer les informations d'un utilisateur. 
	
## Spécifications 
Dans cette section, quelques éléments sont fournis pour faciliter la compréhension du besoin et des réalisations techniques à venir.
Les élements des processus de gestion des autorisation, authentication et sécurisation des ressources (de l'application), seront mis en place à partir des spécifications
**JWT** avec **Spring Security**. Les échanges se feront principalement entre le client (front-end) et le serveur (back-end). 
Les éléments ci-dessous sont fournis dans le cadre cette spécification :
- une brève présentation de **_JWT_** 
- le diagramme d'architecture applicative et technique
- les diagrammes de séquences de fonctionnement global des points suivants :
	- Accès aux resources protégées : demande été génration des jetons d'accès et d'actualisation
	- Ajout ou enregistrement de nouveaux utilisateurs dans l'application.
	- La gestion de la connexion des utilisateurs à l'application
- les schémas et modèles de données pour la gestion de la persistance des informations dans l'application.


### Brève Présentation JWT  
JWT (**J**SON **W**eb **T**oken), est une spécification pour la représentation des revendications (claims) à transférer entre deux parties. Les revendications sont codées en tant qu'objet JSON utilisé comme charge
 utile d'une structure chiffrée, permettant aux revendications d'être signées ou chiffrées numériquement. La structure peut être :
- **J**SON **W**eb **S**ignature (JWS) ou 
- **J**SON **W**eb **E**ncryption (JWE).

JWT peut être choisi comme format pour les jetons d'accès et d'actualisation utilisés dans le protocole OAuth2.


### Architecture Applicative et Technique Globale 
Le diagramme ci-dessous fournit une vision globale des flux d'échanges entre l'application et les acteurs du système et(ou) briques/composants applicatifs.
Elle comporte les éléments suivants :
- le **Back-End** qui embarque :
	- _Le serveur d'autorisation_ : intégrant le processus de validation des informations d'identification de l'utilisateur, production des jetons (jeton d'accès + jeton d'actualisation).
	- _Le Serveur de ressources_ : intégrant les points de terminaison de l'API REST à sécuriser et les éléments de gestion associés.
	- _Les différentes configurations_ de sécurisation des élements applicatifs.
- le **Front-End** : fournissant l'interface utilisateur permettant d'échanger.
- le **SGBD** : poour le stockage et la persistance des informations métiers de l'application.

![DAAT](.docs/images/architecture-applicative-technique-globale.png "Diagrammme Architecture Applicatif et Technique")

### Le Fonctionnement Global de l'ajout d'un nouvel utilisateur
Le principe de fonctionnement de l'enrgistrement des informations d'un nouvel utilisateur dans le SI, est présenté par le diagramme de séquences ci-dessous :
![DS](./docs/images/fonct-global-enregistrer.png "Diagramme de séquences Ajout nouvel utilisateur")

### Le Fonctionnement Global de la Connexion/Déconnexion
Le principe de fonctionnement de la connexion d'un utilisateur du SI avec ses informations, est présenté par le diagramme de séquences ci-dessous :
![DS](./docs/images/fonct-global-se-connecter.png "Diagramme de séquences Connexion Utilisateur")

### Le Fonctionnement Global de des accès aux ressources de l'application
Une vue macroscopique du fonctionnement global de l'application est fournie par le diagramme de séquences ci-dessous. Il est composé de deux principales phases:
- La demande et obtention des jetons d'accès après s'être authentifié
- L'accès proprement dit aux ressources de l'application avec le jeton d'accès.

![DS](./docs/images/fonct-global-acces_resources-protegees.png "Diagramme de séquences du fonctionnement global Accès aux ressources")


### Modèles et Schémas de données
Les modèles fournis sont relatifs au *_*métier_**. Le diagramme de classes ci-dessous présente les relations entre les entités de gestion de la partie métier de l'application.
![DC](./docs/images/modele-donnees-metier.png "Diagramme de Classes des objets de gestion de l'identification des utilisateurs")


## Stack Technique
Une liste non exhaustive des technos embarquées pour le développment de cette application :