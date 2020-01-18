## 5. Tests

### 5.1 Introductions aux tests

Dans ce chapitre, nous allons parcourir et expliquer les différents tests effectués durant ce projet.

### 	5.2 Cucumber

Contrairement à la plupart des groupes, nous avons implémenté cucumber directement dans nos deux APIs. 

Vous trouverez les features dans le dossier `test/java/ressources/features/` dans chacune des APIs. Les `steps` se trouvent dans le dossier `test/java/ch/heigvd/calendar.management.cucumber.bdd`.

Nous avons trouvé cette implémentation dans sur ce site : 

https://medium.com/@bcarunmail/set-up-and-run-cucumber-tests-in-spring-boot-application-d0c149d26220

Ainsi que su le repo Github lié :

https://github.com/bcarun/cucumber-samples/tree/master/hello-springboot-cucumber 

### 	5.3 Tests de l'API Management

Ci-dessous, la liste des tests effectués pour l'API Management : 

- Il est possible de s'authentifier avec des identifiants valide
- Il n'est pas possible de s'authentifier avec un email non-valide
- Il n'est pas possible de s'authentifier avec un mot de passe non-valide
- Il n'est pas possible de s'authentifier avec des identifiants non-valide
- Il est possible de s'enregistrer
- Il est pas possible de s'enregistrer avec mail déjà utilisé
- Il ne doit pas etre possible de s'enregistrer avec une valeur manquante
- Il ne doit pas etre possible de récuperer la liste des utilisateurs sans être authentifié
- Il doit etre possible de récuperer la liste des utilisateurs en étant authentifié
- Il ne doit pas être possible de récuperer les informations d'un compte sans être authentifé
- Il doit être possible de récuperer les informations d'un compte en étant authentifé
- Il ne doit pas être possible de mettre à jour les informations d'un autre compte
- Il doit être possible de mettre à jour les informations de son compte
- Il ne doit pas être possible de supprimer un autre compte que le sien
- Il doit être possible de supprimer son propre compte



### 	5.4 Tests de l'API Calendar

Ci-dessous, la liste des tests effectués pour l'API Calendar : 

- Il est possible de récupérer la liste des utilisateurs avec un JWT Token valide
- Il n'est pas possible de récuperer la liste des utilisateurs avec un JWT Token invalide

- Il est possible de récuperer la liste des rôles disponibles
- Il est possible de récuperer la liste des accès pour un calendrier donné
- Il est possible d'ajouter une permission d'un calendrier donné
- Il ne doit pas être possible d'ajouter une permissions à un calendrier pour une personne ayant le droit "Viewer"
- Il est possible de récuperer une liste de tous les calendriers
- Il est possible de créer un calendrier



### 5.5 Execution des tests

Afin de pouvoir exécuter les tests, il vous faut : 

- Exécuter le script `start_topology.sh` en lui spécifiant `test` comme argumant : 

  `./start_topology.sh test`






