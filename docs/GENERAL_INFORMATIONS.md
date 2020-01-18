## 2. Informations Générales

### 2.1 Lancer l'infrastructure

Afin de lancer l'infrastructure, il suffit de lancer le script `start_topology.sh` situé à la racine avec comme argument `prod` ou `test` : 

- `./start_topology.sh prod` lancera le serveur MySQL (avec un million d'entrées), phpMyAdmin, MockMock, Traefik et les deux serveurs pour chacune des APIs.
- `./start_topology.sh test` lancera le serveur MySQL, et executera les tests sur les différentes APIs.

### 2.2 Données aléatoires

Sur l'infrastructure de production, un script contenant un million d'entrées est importé dans notre API Business, `API-Calendar`. 

### 2.3 Identifiants

#### 2.3.1 MySQL/phpMyAdmin

Les identifiants sont : 

- Utilisateur : root
- Mot de passe : root

#### 2.3.2 Utilisateurs de l'API-Calendar

Par défaut, les dumps SQL vont insérer 20 utilisateurs (username1, username2, username3,..., username20) dans la base de données avec le même mot de passe (#Welcome123), les identfiants sont donc par exemple: 

- Utilisateur : username1
- Mot de passe : #Welcome123

#### 2.3.3 Utilisateurs de l'API-Management

Par défaut, les utilisateurs déjà inscrits sur l'API-Management sont : 

- Utilisateur #1 : 
  - Email : edin.mujkanovic@heig-vd.ch
  - Mot de passe : #Welcome123
- Utilisateur #2 : 
  - Email : daniel.oliveirapaiva@heig-vd.ch
  - Mot de passe : #Welcome123




### 2.4 Liens utiles

Ci-dessous, les liens utiles lorsque l'infrastructure est montée : 

- phpMyAdmin : http://localhost:6060

- API-Management:  http://localhost/api/management/

  - Swagger-ui: http://localhost/api/management/swagger-ui.html
  
- API-Calendar : http://localhost/api/app/

  - Swagger-ui: http://localhost/api/app/swagger-ui.html
  
- Traefik admin console : http://localhost:1234

- MockMock : http://localhost:1111

  
