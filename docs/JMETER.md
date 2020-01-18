## 6. JMeter

### 6.1 Introduction

Dans ce chapitre, nous allons tester les performances de deux vues, **showCalendars.xhtml** et **showLazyCalendars.xhtml**. Ces derniers affichent tous les calendriers présents dans la base de données, de façon paginée mais de deux manières différentes. showCalendars.xhtml va charger tous les calendriers en mémoire en une fois alors que le showLazyCalendars.xhtml va effectuer une requête à chaque sélection de page afin d'y charger les calendriers correspondant.

### 6.2 Résultats 

Les tests ci-dessous sont effectués avec 10 threads (10 utilisateurs) qui effectueront chacun 10 requêtes.

#### 6.2.1 Résultats 10'000 données

LazyCalendars : 

| Label        | Samples | Average | Min  | Max  | Std. Dev | Through. | Received KB/sec | Sent KB/s | Avg. Bytes |
| ------------ | ------- | ------- | ---- | ---- | -------- | -------- | --------------- | --------- | ---------- |
| HTTP Request | 100     | 17      | 7    | 45   | 10.26    | 18.0/sec | 118.41          | 2.62      | 6726.0     |

showCalendars: 

| Label        | Samples | Average | Min  | Max  | Std. Dev | Through. | Received KB/sec | Sent KB/s | Avg. Bytes |
| ------------ | ------- | ------- | ---- | ---- | -------- | -------- | --------------- | --------- | ---------- |
| HTTP Request | 100     | 17      | 9    | 45   | 7.81     | 81.8/sec | 387.67          | 12.23     | 4851.0     |



#### 5.2.2 Résultats 1'000'000 données:

LazyCalendars : 

| Label        | Samples | Average | Min  | Max  | Std. Dev | Through. | Received KB/sec | Sent KB/s | Avg. Bytes |
| ------------ | ------- | ------- | ---- | ---- | -------- | -------- | --------------- | --------- | ---------- |
| HTTP Request | 100     | 21      | 7    | 33   | 4.44     | 17.7     | 116.24          | 2.53      | 6719       |

showCalendars: 

| Label        | Samples | Average | Min  | Max  | Std. Dev | Through. | Received KB/sec | Sent KB/s | Avg. Bytes |
| ------------ | ------- | ------- | ---- | ---- | -------- | -------- | --------------- | --------- | ---------- |
| HTTP Request | 100     | 114     | 23   | 188  | 44.70    | 15.2     | 99.89           | 2.1       | 6747       |



### 5.3 Conclusions 

Nous pouvons remarquer que le LazyCalendars est beaucoup plus performant quant au chargement des données. En effet, nous pouvons en conclure que le chargement des données en mémoire est un processus couteux contrairement au chargement "dynamique" que propose le LazyCalendar.

### 5.4 Reproduire les tests

 Afin de reproduire les tests, il vous faut **Jmeter** et lancer aussi l'infrastructure de production, pour ce faire il suffit d'executer le script `./start_topology.sh prod`. 

Après avoir lancé l'infrastructure, il faut charger les tests dans Jmeter. Ces derniers se trouvent dans le dossier `Jmeter_tests`.
