## 6. JMeter

### 6.1 Introduction

Dans ce chapitre, nous allons tester les performances de la pagination de notre API REST. Pour ce faire, nous aller tester plusieurs liens de notre application et plusieurs variations des paramètres offset et limit. 

### 6.2 Résultats 

Les tests ci-dessous sont effectués avec 10 threads (10 utilisateurs) qui effectueront chacun 10 requêtes avec 1 millions de données dans la base de données.

#### 6.2.1 Résultats avec les paramètres de base

/api/app/access/16: 

| Label        | Samples | Average | Min  | Max  | Std. Dev | Through. | Received KB/sec | Sent KB/s | Avg. Bytes |
| ------------ | ------- | ------- | ---- | ---- | -------- | -------- | --------------- | --------- | ---------- |
| HTTP Request | 100     | 16      | 5    | 31   | 6.04     | 17.9/sec | 25.60           | 5.95      | 1463       |

/api/app/calendars: 

| Label        | Samples | Average | Min  | Max  | Std. Dev | Through. | Received KB/sec | Sent KB/s | Avg. Bytes |
| ------------ | ------- | ------- | ---- | ---- | -------- | -------- | --------------- | --------- | ---------- |
| HTTP Request | 100     | 43      | 14   | 113  | 25.36    | 17.0     | 14.02           | 5.66      | 843        |



#### 5.2.2 Résultats avec un offset de 10 et une limite de 50

/api/app/access/16: 

| Label        | Samples | Average | Min  | Max  | Std. Dev | Through. | Received KB/sec | Sent KB/s | Avg. Bytes |
| ------------ | ------- | ------- | ---- | ---- | -------- | -------- | --------------- | --------- | ---------- |
| HTTP Request | 100     | 19      | 5    | 31   | 5.33     | 17.9     | 25.52           | 6.26      | 1463       |

/api/app/calendar: 

| Label        | Samples | Average | Min  | Max  | Std. Dev | Through. | Received KB/sec | Sent KB/s | Avg. Bytes |
| ------------ | ------- | ------- | ---- | ---- | -------- | -------- | --------------- | --------- | ---------- |
| HTTP Request | 100     | 99      | 19   | 244  | 59.22    | 15.2     | 28.06           | 5.32      | 1893       |



### 6.3 Conclusions 

Nous pouvons remarquer que le changement des paramètres offset et limit ont peu d'incidence pour l'URL `/api/app/access/16` qui ne dépend pas de l'utilisateur. En revanche, récupérer les calendriers d'un utilisateur demande plus temps. Néanmoins, le temps moyen de réponse reste en dessous de la seconde pour un volume de données conséquent avec un nombre de 10 utilisateurs en parallèle.  

### 6.4 Reproduire les tests

 Afin de reproduire les tests, il vous faut **Jmeter** et lancer aussi l'infrastructure de production, pour ce faire il suffit exécuter le script `./start_topology.sh prod`. 

Après avoir lancé l'infrastructure, il faut charger les tests dans Jmeter. Ces derniers se trouvent dans le dossier `jmeter_tests`.
