## 3. Design de l'API

#### 3.1 Structure des liens

Nous avons opté pour une structure assez simple pour nos deux API.

L'API de Management a les URL suivants:

- /users

  - get : Liste de tous les utilisateurs

  - post : Création d'un utilisateur
  - put: Modification d'un utilisateur

- /users/{userId}

  - get : Récupération d'un utilisateur en particulier
  - delete: Suppression d'un utilisateur

- /users/authentication

  - post : Authentification d'un utilisateur

- /users/password/{content}

  - get: Obtention d'un email pour reset le mot de passe
  - post : Reset du mot de passe

Tous les liens sont sécurisés avec un token JWT sauf l'authentification et la création d'un utilisateur.

L'API Calendar a les liens suivants:

- /calendars
  - get: Récupère la liste des calendriers associées à l'utilisateur
  - post: Crée un nouveau calendrier
  - put: Modifie un calendrier
- /calendars/{calendarId}
  - get: Récupère un calendrier 
  - delete: Supprime un calendrier
- /users
  - get: Récupère tous les utilisateurs de la base de données
- /access/{calendarId}
  - get : Récupère tous les accès pour un calendrier
- /access
  - post: Crée un nouvel accès 
  - delete: Supprime un accès
- /access/roles
  - get: Liste de tous les rôles disponible sur un calendrier

Tous les liens sont protégés avec le token JWT.



