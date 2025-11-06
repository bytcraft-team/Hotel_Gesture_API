**Gestion des Hotels**

##  Description
Projet de gestion d'hôtels développé avec **Spring Boot** et **Kotlin**.  
Ce projet permet de gérer les hôtels, les chambres, les clients, les employés et les réservations via une API REST sécurisée.

##  Technologies Utilisées
- **Langage** : Kotlin
- **Framework** : Spring Boot
- **Base de données** : MySQL
- **Build Tool** : Gradle
- **ORM** : Spring Data JPA

## Diagramme UML
Voici le diagramme UML du projet :
[Diagramme UML] (images/UML.png)

## 🗃 Structure Complète de la Base de Données

# 1️-Employee
- **Description** : Gestion des employés de l'hôtel
- **Colonnes principales** :
    - `employeId` (INT, PK, auto-increment)
    - `nom` (VARCHAR)
    - `poste` (VARCHAR)
    - `salaire` (DECIMAL)

# 2️-Client
- **Description** : Gestion des clients standards
- **Colonnes principales** :
    - `client_id` (INT, PK, auto-increment)
    - `nom` (VARCHAR)
    - `prenom` (VARCHAR)
    - `email` (VARCHAR, unique)
    - `telephone` (VARCHAR)

# 3-ClientVIP : Représente les clients VIP et **hérite de Client**, c'est-à-dire qu'il possède **toutes les colonnes de Client** plus des colonnes spécifiques aux VIP.
- **Description** : Clients VIP avec avantages supplémentaires
- **Colonnes principales** :
    - `client_id` (INT, PK, auto-increment)
    - `remise` (Double)

# 4️-Chambre
- **Description** : Gestion des chambres standards
- **Colonnes principales** :
    - `chambre_id` (INT, PK, auto-increment)
    - `numero` (INT, unique)
    - `prix` (DECIMAL)
    - `type_chambre` (VARCHAR)

# 5️-ChambreSuite : Représente les suites et **hérite de Chambre**, donc elle possède **toutes les colonnes de Chambre** plus des colonnes spécifiques aux suites :
- **Description** : Chambres suite avec équipements supplémentaires
- **Colonnes principales** :
    - `chambre_id` (INT, PK, auto-increment)
    - `jacuzzi` (BOOLEAN)
    - `nombre_pieces` (INT)
    - `suite_nom` (VARCHAR)

# 6️-Reservation
- **Description** : Réservations standard
- **Colonnes principales** :
    - `reservation_id` (INT, PK, auto-increment)
    - `date_debut` (DATE)
    - `date_fin` (DATE)
    - `statut` (VARCHAR)
    - `chambre_id` (INT, FK -> Chambre)
    - `client_id` (INT, FK -> Client)
    - `employe_id`(INT, FK ->Employees

# 7️-ReservationOnline
- **Description** : Représente les réservations effectuées en ligne et **hérite de Reservation**, donc elle possède **toutes les colonnes de Reservation** plus des colonnes spécifiques :
- **Colonnes principales** :
    - `reservation_id` (INT, PK, auto-increment)
      -`remise` (Double)
      -`platforme` (VARCHAR)

---
## 🚀 Installation et Exécution

### Prérequis
- JDK 17+
- Mysql workbench
- Gradle
- Postman (optionnel, pour tester les API)

## 🚀 Étapes d'installation

1. Clonez le repository :
```bash
git clone https://github.com/bytcraft-team/Hotel_Gesture.git
```

2. Créez la base de données
```sql 
   CREATE DATABASE hotel_db;
```
3. Configurez `application.properties`
```properties
spring.application.name=APiRes
spring.datasource.url=jdbc:mysql://localhost:3306/hotel_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=hotel_user
spring.datasource.password=password123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE
server.port=8080
server.error.include-message=always
server.error.include-binding-errors=always
server.error.include-stacktrace=on_param
server.error.include-exception=false
spring.data.web.pageable.default-page-size=10
spring.data.web.pageable.max-page-size=100
spring.jackson.serialization.write-dates-as-timestamps=false
spring.jackson.serialization.indent-output=true
logging.level.org.springframework.web=INFO
logging.level.org.hibernate=INFO
logging.level.APiRes=DEBUG
```
4. Lancez l'application
   ```bash 
   ./mvnw spring-boot:run

## Endpoints Disponibles

-  `GET /api/employees` - Récupérer toutes les employés
- `GET /api/employees/{id}` - Récupérer un employé
- `POST /api/employees` - Créer une nouvelle employé
- `PUT /api/employees{id}` - Mettre à jour un employé
- `DELETE /suite/{id}` - Supprimer un employé


-`GET /api/clients` - Récupérer toutes les client
- `GET /api/clients/{id}` - Récupérer une client par ID
- `POST /api/clients` - Créer une nouvelle client
- `PUT /api/clients/{id}` - Mettre à jour une client
- `DELETE /api/clients/{id}` - Supprimer une client

-`GET /vip` - Récupérer toutes les clients VIP
- `GET /vip/{id}` - Récupérer une client VIP par ID
- `POST /vip` - Créer une nouvelle client VIP
- `PUT /vip/{id}` - Mettre à jour une client VIP
- `DELETE /vip/{id}` - Supprimer une client VIP

-  `GET /api/chambres` - Récupérer toutes les chambres
- `GET /api/chambres/{id}` - Récupérer une chambre par ID
- `POST /api/chambres` - Créer une nouvelle chambre
- `PUT /api/chambres/{id}` - Mettre à jour une chambre
- `DELETE /api/chambres/{id}` - Supprimer une chambre

-  `GET /suite` - Récupérer toutes les chambres suites
- `GET /suite/{id}` - Récupérer une chambre suite par ID
- `POST /suite` - Créer une nouvelle chambre suite
- `PUT /suiteid}` - Mettre à jour une chambre suite
- `DELETE /suite/{id}` - Supprimer une chambre suite

-  `GET /api/reservations` - Récupérer toutes les réservations
- `GET /api/reservations/{id}` - Récupérer une réservation par son ID
- `POST /api/reservations` - Ajouter une nouvelle réservation standart
- `POST/api/reservations/online` - Ajouter une réservation en ligne(avec ReservationOnlineDTO)
- `PUT /api/reservations/{id}/confirmer` - Confirmer une réservation (optionnel:employeId)
- `PUT /api/reservations/{id}/annuler` - Annuler une réservation (optionnel:employeId)
- `GET /api/reservation/statuts/{statut}` - Récupérer toutes les réservations par statut(Confirmée , Annulée,...)
- `GET /api/reservations/client/{clientId}` - Récupérer toutes les réservations d'un client spécifique
- `GET /api/reservations/{id}/montant` - Calculer le montant d’une réservation
- `DELETE / api/reservations/{id}` - Supprimer une réservation par ID
- `GET /api/reservations/chambre/{chambreId}` - Récupérer toutes les réservations pour une chambre spécifique
- `GET /api/reservations/dates?/start=YYYY-MM-DD&end=YYYY-MM-DD` - Récupérer toutes les réservations dans une plage de dates


## Auteur
** [ Aziza Laafar / Hocein Essaif ] ** - Projet Back-End Kotlin/Spring Boot

## Date
[2025-11-04]