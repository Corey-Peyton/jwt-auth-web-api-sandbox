# Secure Web REST API (Back-End)

![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg?branch=develop)


## Sécurité applicative
Le point qui est abordé ici est l'utilisation des outils `Keytool et OpenSSL` pour produire les éléments nécessaires à exploiter pour **signer/valider** 
les `jetons JWT` avec des **clés privées/publiques RSA**. La démarche à suivre est l'une des options présentées ci-dessous :
- **`Keytool et OpenSSL`** : utilisation combinée des commandes de `Keytool et OpenSSL`, puis exploiter l'API Java pour obtenir les éléméents attendus.
	- Génération du magasin `clés privées/publiques RSA` avec `Ketyool`
	- Export de la clé publique et du certificat dans un fichier  avec `Ketyool` et `OpenSSL` combinés
	- Export du certificat dans un fichier
	- Export au format PKCS12  avec `Ketyool`
- **`OpenSSL`** : puis exploiter l'API Java pour obtenir les éléméents attendus.
	- Génération la `clé privée RSA` 
	- Extraction la clé publique de la paire de clés, qui peut être utilisée dans un certificat

**NB** : 
- L'option choisie ici est `la première`, la seconde étant fournie à titre de documentation.
- Par la suite les arguments fournis sont pour l'environnement de développement (pour les autres environnements, il faudra bien **`cacher les mots de passe`**)

### Générer le certificat auto-signé avec `Keytool et OpenSSL`
- Génération du magasin `clés privées/publiques RSA` au format JKS
```bash
# Utilisation de la commande light
$ keytool -genkeypair -alias my-app-recette -keyalg RSA -keysize 4096 -keystore my-app-recette-keystore.jks -validity 3650

# Utilisation de la commande full
$ keytool -genkeypair -dname "CN=server.tuto.vincent.fr,OU=IT,O=OVIOK Group,L=ANTIBES,S=ALPES MARITIMES,C=FR" -alias my-app-recette -keyalg RSA -keysize 4096 -keypass my-app-recette -validity 3650 -storetype JKS -keystore my-app-recette-keystore.jks -storepass my-app-recette -file my-app-recette-keystore.jks
```

- Export de la clé publique et du certificat dans un fichier
```bash
# L'export de la clé publique et du certificat dans un fichier à partir du JKS est effectué par la commande suivante
$ keytool -list -rfc --keystore my-app-recette-keystore.jks | openssl x509 -inform pem -pubkey -out my-app-recette.txt
Enter keystore password:  my-app-recette
```

- Export du certificat dans un fichier
On peut soit :
	- exploiter le fichier contenant la clé publique et du certificat
	- faire un export à partir du magasin des clés (Keystore) généré ci-dessus
```bash
# Exploitation du fichier : il faut copier le bloc 'CERTIFICATE' dans un fichier et l'enregistrer sous le nom : my-app-recette.crt 

# Utilisation du magasin des clés
$ keytool -exportcert -alias my-app-recette -file  my-app-recette.crt -keystore my-app-recette-keystore.jks 
```

- Export au format PKCS12
Il est recommandé d'utiliser le format PKCS12 qui est un format standard de l'industrie. Ainis, pour obtenir les informations au format PKCS12, on peut :
	- Créer le magasin .p12 par import des informations du JKS
	- Générer directement le magasin .p12
```bash
# 1°) - Import des informations du JKS

# Avec le mot de passe de la source --> Confirmer avec le mot de passe de la clé privée 
$ keytool -v -importkeystore -srckeystore my-app-recette-keystore.jks -srcstoretype JKS -srcalias my-app-recette -srckeypass my-app-recette -destkeystore my-app-recette.p12 -deststoretype PKCS12 -destalias my-app-recette -deststorepass my-app-recette

# Sans le mot de passe de la source --> Confirmer avec le mot de passe de la clé privéé
$ keytool -v -importkeystore -srckeystore my-app-recette-keystore.jks -srcstoretype JKS -srcalias my-app-recette -destkeystore my-app-recette.p12 -deststoretype PKCS12 -destalias my-app-recette -deststorepass my-app-recette

# 2°) - Générer directement le magasin .p12
$ keytool -genkeypair -alias my-app-recette -keyalg RSA -keysize 4096 -storetype PKCS12 -keystore my-app-recette.p12 -validity 3650
```

### Générer les clés et fichiers avec `OpenSSL`
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