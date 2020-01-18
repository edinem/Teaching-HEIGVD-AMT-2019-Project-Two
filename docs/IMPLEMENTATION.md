## 4. Implémentation

#### 4.1 Génération Token JWT

Nous nous sommes inspirés du site Web suivant pour la génération et la vérification du token JWT : https://dzone.com/articles/spring-boot-security-json-web-tokenjwt-hello-world.  

La valeur du secret partagé entre les deux API se trouvent dans le fichier `application.properties` de chaque projet respectif. Le champ contenant la valeur se nomme `jwt.token`. Il est alors possible de l'injecter dans le code avec l'annotation `@Value("${jwt.secret}")`. Il est ainsi facile de modifier la clé sans devoir modifier le code. 

Il est ensuite possible de choisir une validité. Dans notre cas, pour la sécurité de nos applications, nous avons décidé de générer des tokens d'un validité d'un jour.

Nous insérons dans le champ `sub` (subject) du token l'email de l'utilisateur connecté. En effet, l'email est l'identifiant unique des utilisateurs en base de données pour les deux API. 

Pour finir, le payload du Token JWT est le suivant:

- sub : Email de l'utilisateur
- exp: Date d'expiration
- iat : Date d'émission

![Token_Body](./images/Token_Body.PNG)



#### 4.2 Vérification Token JWT

Basé sur l'exemple dont nous nous sommes inspirés, nous avons décidé d'utiliser Spring Security, plus précisément d'étendre les classes `WebSecurityConfigurerAdapter` afin de spécifier les règles qui nous convenaient.

##### 4.2.1 WebSecurityConfig

Cette classe permet de définir les règles de sécurité de nos API. Chaque élément pertinent de la classe va être expliqué.

Cette ligne présente dans la méthode `configureGlobal` permet de définir un service permettant de vérifier et générer les utilisateurs pour les Controllers de notre API. 

```java
auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
```

La méthode `configure` de la classe permet de définir les règles autorisation pour l'API. Pour l'API de Management, nous avons ouvert les URL permettant l'authentification et l'inscription d'un utilisateur ainsi que la documentation de Swagger. Dans l'API Calendar, nous avons uniquement ouvert la documentation Swagger. De plus, nous ajoutons un filtre `JwtRequestFilter` qui s'applique avant le check de l'authentification, appliqué de base par la classe étendue. Si aucun utilisateur n'est authentifié après ce filtre, la classe `JwtAuthenticationEntryPoint` va être appelée et renvoyer une erreur 401.

![WebSecurityConfig](./images/WebSecurityConfig.PNG)

##### 4.2.2 JwtUserDetailsService

Il est nécessaire d'implémenter un `userDetailService` pour pouvoir utiliser la classe `WebSecurityConfigurerAdapter`. Nous avons donc créé une classe `JwtUserDetailsService` satisfaisant nos besoins. Nous avons deux implémentations différentes de ce service pour chaque API. 

L'API Calendar ne gérant pas les utilisateurs et se confiant totalement au token JWT, le service `JwtUserDetailsService` vérifie si l'utilisateur existe en base de données et si ce n'est pas le cas le crée. En effet, lors de la première utilisation de l'API, l'utilisateur est crée en base de données via le service avant que les Controllers effectuent toute requête en base de données.

L'API Management vérifie si l'utilisateur existe en base de données et throw une erreur si ce n'est pas le cas. En effet, l'API Management peut vérifier la véracité des données, ce que l'autre API ne peut pas faire. La base de la confiance repose sur le token. Celui-ci doit avoir un secret fort afin qu'aucun token ne puisse être forgé. 

##### 4.2.3 JwtRequestFilter

Dans cette classe, nous obtenons la valeur de l'entête HTTP `Authorization`. Nous vérifions que la valeur de l'entête soit dans le bon format ("Bearer token") et nous vérifions que le token soit correct et ne soit pas expiré. Si toutes les conditions sont réunies, nous récupérons l'email qui se trouve dans le sujet du token. À partir de l'email nous appelons la classe `JwtUserDetailsService` pour récupérer l'utilisateur et nous authentifions l'utilisateur.

##### 4.2.4 JwtAuthenticationEntryPoint

Cette classe permet de renvoyer une erreur 401 lors d'un mauvais token JWT. 

#### 4.3 Table intermédiaire

Dans l'implémentation de notre API Calendar, il est favorable de travailler avec la table intermédiaire pour récupérer les calendriers associés à l'utilisateur et les accès à ceux-ci.

Pour se faire, nous avons créé une entité `AccessEntity` qui représente notre table intermédiaire `user_has_calendar`. La table ayant une clé primaire composite, nous avons dû créé une classe `AccessIdentity` afin de pouvoir l'utiliser comme Id dans notre entité.

```java
@EmbeddedId
private AccessIdentity id;
```

Il est alors possible de créer un repository `AccessRepository ` en étendant la classe `PagingAndSortingRepository<AccessEntity, AccessIdentity>` et de mettre la classe `AccessIdentity` comme type de clé primaire du repository.

#### 4.4 Pagination

Nous avons défini la pagination pour toutes les listes de données retournées par notre API. Pour ce faire, les repository étendent la classe ``PagingAndSortingRepository<>`.  

Nous passons les paramètres de la pagination dans l'URL `?offset=x&limit=y`. Les paramètres ne sont pas obligatoires. En effet, ils ont une valeur par défaut de 0 pour l'offset et de 20 pour la limite. 

Dans les Controllers, nous avons implémenter la pagination comme ceci : 

```java
Pageable pageable = PageRequest.of(offset, limit);
Page<UserEntity> users = userRepository.findAll(pageable);
```

Il nous suffit ensuite de travailler avec la liste d'objets récupérée de la base de données comme on le souhaite.
#### 4.5 Réinitialisation du mot de passe par e-mails

Nous avons implémenté l'option de réinitialisation par e-mails. En effet, un utilisateur peut demander une réinitialisation en envoyant une requête GET sur le lien `/users/password/edin.mujkanovic@heig-vd.ch` (dans cet exemple nous demandons une réinitialisation du compte ayant l'adresse mail edin.mujkanovic@heig-vd.ch). 

Après avoir effectué la requête, l'email de réinitialisation est envoyé. Pour faciliter l'usage de l'application pour la correction, nous avons ajouté un serveur MockMock qui receptionne tous les mails envoyés. Afin de voir le mail de réinitilisation demandé pour le compte edin.mujkanovic@heig-vd.ch, nous allons donc sur : http://localhost:1111/  et nous voyons donc le mail demandé : 

![Inbox](./images/inbox.png)

Avec son contenu : 

![Content](./images/content.png)

En utilisant le token, reçu par mail, nous pouvons effectuer une requête POST sur le lien `/users/password/VBLvv70Y75at` avec l'email et le nouveau mot de passe afin de réinitialiser le mot de passe: 

![Content](./images/swagger_mail_token.png)