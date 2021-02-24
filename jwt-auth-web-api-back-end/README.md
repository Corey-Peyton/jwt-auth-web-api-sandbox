# Secure Web REST API (Back-End)

![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg?branch=develop)

## Détails Génération du `jeton JWT` avec `Keytool et OpenSSL`
TODO


## Détails Génération du `jeton JWT` avec `OpenSS`L
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
     String rsaPublicKey = "-----BEGIN PUBLIC KEY-----" +
         "MIIBIjANBgkq......B/V73QUxKI4/rQIDAQAB" +
         "-----END PUBLIC KEY-----";
    rsaPublicKey = rsaPublicKey.replace("-----BEGIN PUBLIC KEY-----", "");
    rsaPublicKey = rsaPublicKey.replace("-----END PUBLIC KEY-----", "");
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(rsaPublicKey));
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PublicKey publicKey = kf.generatePublic(keySpec);
    return publicKey;
}

// Exploitation de la clé privée générée pour obtenir PrivateKey
public static PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
    String rsaPrivateKey = "-----BEGIN PRIVATE KEY-----" +
            "MIIEvwIBADANBgkq...... h8TPOvfHATdiwIm7Qu76gHhpzQ==" +
            "-----END PRIVATE KEY-----";

    rsaPrivateKey = rsaPrivateKey.replace("-----BEGIN PRIVATE KEY-----", "");
    rsaPrivateKey = rsaPrivateKey.replace("-----END PRIVATE KEY-----", "");

    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(rsaPrivateKey));
    KeyFactory kf = KeyFactory.getInstance("RSA");
    PrivateKey privKey = kf.generatePrivate(keySpec);
    return privKey;
}
```