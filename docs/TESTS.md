## 4. Tests

### 4.1 Introductions aux tests

Dans ce chapitre, nous allons parcourir et expliquer les différents types de tests effectués durant le projet.

#### 	4.1.1 Tests des models (Junit - Unit Testing)

​		Junit permet de tester le bon fonctionnement de nos modèles

#### 	4.1.2 Tests des DAOs (Arquillian - Test d'intégration)

​		Arquillian est utilisé dans notre projet afin de tester le bon fonctionnement de nos DAOs.

#### 	4.1.3 Tests des vues (Mockito - Test d'intégration)

​		Mockito est utilisé dans notre projet afin de tester le bon fonctionnement de nos vues.

### 4.2 Prérequis

Afin de pouvoir exécuter les tests, il vous faudra installer **maven** sur votre machine ainsi que mettre en place un environnement nécessaire au bon fonctionnement d'Arquillian. Pour ce faire : 

- Exécuter le script `start_topology.sh` en lui spécifiant `test` comme argumant : 

  `./start_topology.sh test`

  Cette ligne de commande va lancé les dockers mysql, phpmyadmin, ainsi que payara **sans** déployer le .war.

- S'assurer que le certificat de payara est bien dans votre keytool

### 4.2 Execution des tests

Après avoir executé l'infrastructure de tests, il faut aller dans le dossier projet et executer la commande  `mvn clean test`



