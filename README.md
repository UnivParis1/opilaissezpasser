### OPI Laissez-passer permet l'insertion des laissez-passer

## Fonctionnement général

L'application se base sur les web services Apogee livrés par l'AMUE .

## Configuration

# Web Service Serveur

Vous devez en préalable installer le Web Service Serveur de l'AMUE pour utiliser l'application.

Vous trouverez la documentation sur le site de l'AMUE : Documentation -> Fonctionnelle -> Connecteurs -> WS Laissez-passer .

Le web service utilisé: "LaissezPasserMetier".

# Web Service

Le fichier "/src/main/resources/configUrlServices.properties" doit contenir l'url de connexion au web service.

les propriétés user et password ont été rajouté pour la connexion au web service avec un user.

Veuillez vous assurer du bon fonctionnement du web service avant de faire une importation.

Une exception sera levée si le web service n'est pas correctement configuré / indisponibles ou droits insuffisants.

# Compilation 

Commande: "mvn clean install" compile et génère le war dans le dossier "target/".

# Fichier d'import

Un exemple de fichier d'import est donné : "opilaissezpasser/src/main/resources/example.csv".

Format du fichier csv :

* typLpa     -> type de laissez-passer ("IAPRIMO/REINS")
* codAnu     -> année IA du laissez-passer
* numOPI     -> numéro d'OPI de l'étudiant
* codEtp     -> code étape
* codVrsVet  -> code version étape
* codDip     -> code diplôme
* codVrsVdi  -> code version diplôme
* libCmtLpa  -> commentaire du laissez-passer
