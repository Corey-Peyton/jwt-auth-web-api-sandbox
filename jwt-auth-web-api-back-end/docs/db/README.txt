######################################
#### CONFIGURATION VIDE POUR OVERRIDE
######################################
# Configuration Vide pour faciliter l'externalisation selon les profils
# H2
# MARIADB
# POSTGRESQL

# ./docs/db/props/back-end-db-h2.properties  pour H2
# ./docs/db/props/back-end-db-mariadb.properties pour MARIADB
# ./docs/db/props/back-end-db-h2.properties  pour POSTGRESQL

# Argument pour l'exécution pour le profil H2
# Cacher ou supprimer le bloc H2 du fichier : back-end-db.properties
# java -Dlogging.config=$SRVC_HOME/logback-spring.xml -jar $SRVC_HOME/XXX.jar --spring.config.location=$SRVC_HOME/back-end-db.properties.properties,$SRVC_HOME/back-end-db-h2.properties,$SRVC_HOME/back-end-application.properties


# Argument pour l'exécution pour le profil MARIADB
# Cacher ou supprimer le bloc MARIADB du fichier : back-end-db.properties
# java -Dlogging.config=$SRVC_HOME/logback-spring.xml -jar $SRVC_HOME/XXX.jar --spring.config.location=$SRVC_HOME/back-end-db.properties.properties,$SRVC_HOME/back-end-db-mariadb.properties,$SRVC_HOME/back-end-application.properties


# Argument pour l'exécution pour le profil POSTGRESQL
# Cacher ou supprimer le bloc POSTGRESQL du fichier : back-end-db.properties
# java -Dlogging.config=$SRVC_HOME/logback-spring.xml -jar $SRVC_HOME/XXX.jar --spring.config.location=$SRVC_HOME/back-end-db.properties.properties,$SRVC_HOME/back-end-db-postgre.properties,$SRVC_HOME/back-end-application.properties
