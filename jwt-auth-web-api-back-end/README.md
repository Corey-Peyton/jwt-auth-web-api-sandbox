# Secure Web REST API (Back-End)

![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg?branch=develop)
![Gitlab code coverage](https://img.shields.io/gitlab/coverage/oviok-group/jwt-auth-web-api-sandbox/jwt-auth-web-api-back-end) 

## Sécuriser les ressources applicatives 
Le point abordé est l'utilisation des outils `Keytool` et `OpenSSL` pour la production des éléments nécessaires à exploiter pour **signer/valider** 
les `jetons JWT` avec des **clés privées/publiques RSA**. La démarche à suivre est l'une des options présentées ci-dessous :
- **`Keytool`** et **`OpenSSL`** : utilisation combinée des commandes de _Keytool_ et _OpenSSL_, puis exploiter l'API Java dédiée pour obtenir les éléments attendus.
	- Générer le magasin des clés privées/publiques RSA
	- Exporter la clé publique et le certificat X509 dans un fichier
	- Exporter le certificat X509 dans un fichier 
	- Exporter au format PKCS12 (_Convertir le keystore JKS en PKCS12_)
- **`OpenSSL`** : puis exploiter l'API Java pour obtenir les éléments attendus.
	- Générer la clé privée RSA 
	- Extraire la clé publique de la paire de clés (peut être utilisée dans un certificat)

**NB** : 
- L'option choisie dans le cadre de cette réalisation est `la première`, la seconde étant fournie à titre de documentation.
- Par la suite les arguments fournis sont pour l'environnement de `dev` (pour les autres environnements, il faudra bien **`cacher les mots de passe`**)

### Générer le certificat auto-signé
- _Générer le magasin des clés privées/publiques RSA au format JKS_
```bash
# Lgth commande : nécessite la saisie de données supplémentaires (fournies par le paramètre -dname dans la commande full ci-dessous présentée)
$ keytool -genkeypair -alias my-app-recette -keyalg RSA -keysize 4096 -keystore my-app-recette-keystore.jks -validity 3650

# Full commande
$ keytool -genkeypair -dname "CN=server.tuto.vincent.fr,OU=IT,O=OVIOK Group,L=ANTIBES,S=ALPES MARITIMES,C=FR" -alias my-app-recette -keyalg RSA -keysize 4096 -keypass <valeur_alias> -validity 3650 -storetype JKS -keystore my-app-recette-keystore.jks -storepass <valeur_alias> -file my-app-recette-keystore.jks

# Vérifier le contenu du magasin des clés
$ keytool -list -v -keystore my-app-recette-keystore.jks -storetype JKS -storepass <valeur_storepass> 
```
Le tableau ci-dessous fourni quelques détails sur certains paramètres de la commande
|Paramètre|Description succincte du paramètre|
|---|---|
|`genkeypair`|_génère une paire de clés_|
|`alias`|_le nom d'alias de l'élément généré_|
|`keyalg`|_l'algorithme de cryptographie choisie pour générer la paire de clés_|
|`keysize`|_la taille de la clé. Ici 4096 qui est un meilleur choix pour la production_|
|`storetype`|_le type du magasin des clés privées/publiques (le keystore)_|
|`keystore`|_le nom du du magasin des clés privées/publiques (le keystore)|
|`validity`|_nombre de jours de validité (ici 10 ans=3650 jours)_|
|`storepass`|le mot de passe pour le keystore : (_mot de passe utilisé pour accéder au magasin de clés_)|


- _Exporter la clé publique et le certificat X509 dans un fichier_
```bash
# L'export de la clé publique et du certificat dans un fichier à partir du JKS est effectué par la commande suivante :

$ keytool -list -rfc --keystore my-app-recette-keystore.jks | openssl x509 -inform pem -pubkey -out my-app-recette.txt
Enter keystore password:  <valeur_storepass>
```

- _Exporter le certificat X509 dans un fichier_

Pour ce faire, on peut soit :
- exploiter le fichier contenant la clé publique et le certificat généré ci-dessus
- faire un export à partir du magasin des clés (Keystore) généré ci-dessus
```bash
# Exploitation du fichier : il faut copier le bloc 'CERTIFICATE' dans un fichier et l'enregistrer sous le nom : my-app-recette.crt 

# Utilisation du magasin des clés
$ keytool -exportcert -alias my-app-recette -file  my-app-recette.crt -keystore my-app-recette-keystore.jks 
```

- _Exporter au format PKCS12_

Il est recommandé d'utiliser le format PKCS12 qui est un format standard de l'industrie. Ainis, pour obtenir les informations au format PKCS12, on peut :
- Créer le magasin .p12 par import des informations du JKS (Convertir le keystore JKS en PKCS12)
- Générer directement le magasin .p12
```bash
# 1°) - Import des informations du JKS
# Avec le mot de passe de la source --> Confirmer avec le mot de passe de la clé privée : <valeur_keypass>
$ keytool -v -importkeystore -srckeystore my-app-recette-keystore.jks -srcstoretype JKS -srcalias my-app-recette -srckeypass <valeur_keypass> -destkeystore my-app-recette.p12 -deststoretype PKCS12 -destalias my-app-recette -deststorepass <valeur_storepass>

# Sans le mot de passe de la source --> Confirmer avec le mot de passe de la clé privéé : <valeur_keypass>
$ keytool -v -importkeystore -srckeystore my-app-recette-keystore.jks -srcstoretype JKS -srcalias my-app-recette -destkeystore my-app-recette.p12 -deststoretype PKCS12 -destalias my-app-recette -deststorepass <valeur_storepass>

# 2°) - Générer directement le magasin .p12
$ keytool -genkeypair -alias my-app-recette -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore my-app-recette.p12 -validity 3650

# Vérifier le contenu du magasin .p12
$ keytool -list -v -keystore  my-app-recette.p12 -storetype PKCS12 -storepass <valeur_storepass> 
```

### Générer les clés et fichiers avec OpenSSL
- _Générer une clé privée RSA, export dans un fichier_
```bash
$ openssl genrsa -out key.pem 4096
```

- _Extraire la clé publique de la paire de clés_
```bash
$ openssl rsa -in key.pem -outform PEM -pubout -out public.pem
```

- _Exploitation de l'API Java pour obtenir les éléméents attendus à partir des clés générées_
```java 
// Exploitation de la clé publique générée pour obtenir PublicKey
public static PublicKey getPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
    final String rsaPublicKey = "-----BEGIN PUBLIC KEY-----" +
         "MIIBIjANBgkq......B/V73QUxKI4/rQIDAQAB" +
         "-----END PUBLIC KEY-----";
    rsaPublicKey = rsaPublicKey.replace("-----BEGIN PUBLIC KEY-----", "");
    rsaPublicKey = rsaPublicKey.replace("-----END PUBLIC KEY-----", "");
    final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPublicKey));
    final KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePublic(keySpec);;
}

// Exploitation de la clé privée générée pour obtenir PrivateKey
public static PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
    final String rsaPrivateKey = "-----BEGIN PRIVATE KEY-----" +
            "MIIEvwIBADANBgkq...... h8TPOvfHATdiwIm7Qu76gHhpzQ==" +
            "-----END PRIVATE KEY-----";

    rsaPrivateKey = rsaPrivateKey.replace("-----BEGIN PRIVATE KEY-----", "");
    rsaPrivateKey = rsaPrivateKey.replace("-----END PRIVATE KEY-----", "");

    final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rsaPrivateKey));
    final KeyFactory kf = KeyFactory.getInstance("RSA");
    return kf.generatePrivate(keySpec);
}
```

## Sécuriser les échanges
La sécurisation des échanges entre l'application et d'autres SI consiste donc à mettre le mécansime permettant de :
- Activer le support `TLS`
- Exiger  que des requêtes `HTTPS`
	- Soit par fichier de propriétés
	- Soit de façon programmatique
- Rediriger les requêtes `HTTP` vers `HTTPS`
- Distribuer le certificat `TLS` aux clients (le Trust).

### Activer le support TLS
Pour ce faire, il faut à minima fournir les propriétés ci-dessous définies:

```properties
server.ssl.enabled=true # Activer le support TLS pour sécuriser les échanges										
server.port=8443 # Activer le port d'écoute pour les accès sécurisé du serveu										r

server.ssl.key-store-type=PKCS12  # Le format ou type de stockage de clés : JKS ou PKCS12
server.ssl.key-store=classpath:crypto/my-app-recette.p12 # Le chemin d'accès au magasin de clés contenant le certificat
server.ssl.key-store-password=<valeur_storepass> # Le mot de passe utilisé pour accéder au magasin de clés
server.ssl.key-alias=<valeur_alias> # L'alias qui identifie la clé dans le magasin de clés.

# Jusqu'ici la configuration suppose que seul le client vérifie le certificat du serveur (One-Way), pour basculer en 
# Two-Way : forcer également l'authentification par certificat du client côté serveur, il faut définir la propriété : server.ssl.client-auth

# Configuration Security (authentification basique)
#spring.security.user.name=user
#spring.security.user.password=user
```

### Exiger que des requêtes HTTPS
Pour exiger que des requêtes HTTPS, il faut configurer des éléments de Spring Security à cet effet.
- Par fichier de propriétés

```properties
# Spring Security
security.require-ssl=true # Activer Spring Security, configurons-la pour n'accepter que les requêtes HTTPS
```

- Exiger que des requêtes HTTPS de façon programmatique
Voici un exempele de code qui exige que des requêtes HTTPS
```java 
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .requiresChannel()
            .anyRequest()
            .requiresSecure()
            ;
/* 
* Pour les besoins de la démo, assurons-nous que Spring Security autorise toutes les demandes entrantes en ajoutant le bloc de code ci-dessous
.and() 
.authorizeRequests()
.antMatchers("/**")
.permitAll()
;
*/
  }
}
```

### Rediriger les requêtes HTTP vers HTTPS
TODO

### Appel d'une URL HTTPS : Le Trust
Maintenant que le `protocole HTTPS est activé dans notre application`, passer au client et explorer comment invoquer un `point de terminaison HTTPS` avec le `certificat auto-signé`.
Tout d'abord, nous devons **créer un magasin de confiance**. Comme nous avons généré un fichier PKCS12, nous pouvons utiliser le même que le trust store. 
Définir de nouvelles propriétés pour les `détails du trust store` :

```properties
server.ssl.trust-store=classpath:crypto/my-app-recette.p12	# emplacement du magasin de confiance
server.ssl.trust-store-password=<valeur_storepass> # mot de passe du magasin de confiance
server.ssl.client-auth=need # Bascule en Two-way authentification : le client vérifie le certificat du serveur et le certificat du client est vérifié côté serveur également
```

Dans ce cas de figure pour les besoins de la démo, assurons-nous que Spring Security autorise toutes les demandes entrantes en ajoutant la configuration suivante :
```java 
@EnableWebSecurity
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/**")
                .permitAll();
    }
}
```

