## 2. Informations Générales

### 2.1 Lancer l'infrastructure

Afin de lancer l'infrastructure, il suffit de lancer le script `start_topology.sh` situé à la racine avec comme argument `prod` ou `test` : 

- `./start_topology.sh prod` lancera le serveur MySQL, phpMyAdmin et Payara en déployant le .war
- `./start_topology.sh test` lancera le serveur MySQL, phpMyAdmin et Payara **sans** déployer le .war

Après avoir lancé l'infrastructure, il faut attendre quelques temps avant de lancer des tests, utiliser l'application ou accéder à phpMyAdmin. Ce délais est dû au fait de l'importation d'un grand nombre de données.

### 2.2 Données aléatoires

Nous avons générés trois dumps SQL de 10'000, 100'000 et 1'000'000 de données. Ces derniers se trouvent dans le dossier `SQL Dumps` se trouvant à la racine. Par défaut, le système va importer 1'000'000 de données. Si vous souhaitez modifier le dump, il vous faut le copier dans `/topology/test/images/mysql/data` ou `/topology/prod/images/mysql/data` selon l'environnement souhaité.

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

- WS-Calendar : http://localhost:8080/ws-calendar

  

#### 