### 3. Implémentation

#### 3.1 Technologie utilisée

Nous avons décidé d'utiliser JSF (JavaServer Faces) pour la présentation. Un des membres du groupe a déjà développé avec cette technologie. JSF est un framework MVC rendant le développement Web plus facile. À noter qu'il est possible d'utiliser JSP en parallèle de JSF.

![Résultat de recherche d'images pour "JSF mvc"](https://www.ibm.com/support/knowledgecenter/SSRTLW_9.6.1/com.ibm.etools.jsf.doc/images/jsfmvc.gif)



### 3.2 Navigation implicite

Résultat de recherche d'images pour "JSF mvc"JSF ne possède pas de système mappage d'URL de base. Il n'est donc pas possible de mapper une URL à une page XHTML. Néanmoins, JSF recherche la bonne vue directement grâce à son nom dans ses composants.

### 3.3 Fonctionnalités implémentées

Les fonctionnalités suivantes ont été implémentées dans l'application :

- Connexion 
- Déconnexion
- Enregistrement d'un utilisateur
- Affichage des calendriers accessible
- Ajout d'un calendrier
- Supprimer un calendrier
- Éditer un calendrier et les droits des utilisateurs sur le calendrier
- Éditer les informations personnelles
- Modifier son mot de passe

Nous avons également implémenté deux méthodes de pagination afin de pouvoir tester les performances. 

### 3.4 Implémentation des sessions

L’implémentation des sessions est un des points qui nous a demandé le plus de réflexion. En effet, avec JSF, il est possible d'utiliser les servlets @SessionScoped directement dans les vues et dans les autres servlets également. Néanmoins, cela à un coût évident en terme de mémoire. 

Le servlet "LoginView" est notre servlet de session et elle a comme attribut `loggedIn`. Cet attribut permet de savoir si l'utilisateur est connecté lors de l'accès aux ressources sécurisées. Elle est utilisé dans notre classe `SecurityFilter`. Notre filtre vérifie que l'utilisateur est bien connecté lorsqu'il essaie d'accéder à une URL commençant par `mycalendar/`.

Nous avons également un attribut `user` qui permet à tous les autres servlet d'accéder à l'utilisateur sans devoir refaire une requête à la base de données.

###  3.5 Méthode de pagination

La première permet de charger les données en mémoires et de demander dynamiquement les données grâce à de l'AJAX et la seconde effectue une requête en base de données à chaque changement de page (Lazy). 



### 3.6 Gestion des droitsRésultat de recherche d'images pour "JSF mvc"

La gestion des droits dans l'application nous a apporté des plusieurs problèmes. Le premier est le stockage des droits. Nous avons décider de créer un Enum avec tous les droits disponible dans l'application.

Ensuite, il faut autorisé l'utilisateur à éditer ou supprimer un calendrier s'il a un droit plus élevé que `viewer`. Nous avons eu un problème pour l'affichage car la balise `<c:if` ne fonctionnait pas car JSTL est interprété avant JSF (https://stackoverflow.com/questions/3361613/cwhen-and-cif-dont-work).

Et pour finir, il était nécessaire d'assurer plusieurs contraintes au niveau applicatif :

- Un calendrier ne peut avoir qu'un seul owner
- Un utilisateur ne peut avoir qu'un seul role sur le calendrier

Pour afficher la liste des utilisateurs disponible pour un nouvel accès sur le calendrier, nous avons dû:

1. Récupérer tous les utilisateurs
2. Soustraire tous les utilisateurs ayant un droit

Nous avons dû également dû enlever l'accès de l'owner de la liste des droits lors de l'affichage des accès pour qu'il ne puisse pas être supprimer.

