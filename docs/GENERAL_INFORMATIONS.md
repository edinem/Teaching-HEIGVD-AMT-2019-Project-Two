## 2. Informations Générales

### 2.1 Lancer l'infrastructure

Afin de lancer l'infrastructure, il suffit de lancer le script `start.sh` situé dans le dossier `Topology`. Ce dernier va lancer les containers suivants : 

- Traefik
- MySQL Server
- phpMyadmin
- API `Calendar` : l'API de notre business. 
- API `management` : l'API qui est en charge de la gestion des utilisateurs.
- Serveur MockMock

### 2.2 Données aléatoires de l'API Calendar

Nous avons générés 1'000'000 de données, qui vont être insérées dans la base de données `API Calendar`. 

### 2.3 Identifiants

#### 2.3.1 MySQL/phpMyAdmin

Les identifiants sont : 

- Utilisateur : root
- Mot de passe : root

#### 2.3.2 Utilisateurs de l'application

Par défaut, les dumps SQL vont insérer 20 utilisateurs (username1, username2, username3,..., username20) dans la base de données avec le même mot de passe (#Welcome123), les identfiants sont donc par exemple: 

- Utilisateur : username1

- Mot de passe : #Welcome123

  


### 2.4 Liens utiles

Ci-dessous, les liens utiles lorsque l'infrastructure est montée : 

- phpMyAdmin : http://localhost:6060

- API-Management : http://localhost:8080/ws-calendar

- API-Calendar : 

  

#### 