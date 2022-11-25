# Utilisation de la VM:
1. importer la machine virtuelle avec virtual-box

2. lancer la machine virtuelle et ouvrir une session (vagrant/vagrant)

3. Mettre à jour l'API sur la VM.<br>
Pour mettre à jour l'API REST, connexion par sftp vagrant/vagrant au port 2202 à l'adresse 127.0.0.1 (localhost)<br>
Fonctionne aussi avec la commande suivante si ssh est installé sur votre machine ( voir : https://docs.microsoft.com/fr-ca/windows-server/administration/openssh/openssh_install_firstuse)

````shell
scp -P2202 justineapi-0.0.8-SNAPSHOT.jar vagrant@localhost:~/run/
````

5. lancer l'exécution de l'API rest: 
```shell
java -jar run/justineapi-0.0.8-SNAPSHOT.jar
```

6. les comptes de profs: <br>
email: prof1@test.com mdp: secret<br>
email: prof2@test.com mdp: secret<br>
email: prof3@test.com mdp: secret<br>
email: prof4@test.com mdp: secret


problèmes de connexion:
si vous avez des problèmes de connexion ajoutez cette ligne dans le fichier manifest de l'application:

dans la balise 
```xml
<application
android:usesCleartextTraffic="true"
```


connexion ssh à la vm
si vous voulez vous connecter par ssh à votre vm vous pouvez le faire avec la commande suivante:
```shell
ssh vagrant@localhost -p2202
```

7. Enum pour les types de comptes:

```java
public enum TypeCompte {
ADMINISTRATEUR,
PROFESSEUR,
ETUDIANT
}
```

# Documentation de l'API

## I. Authentification

### a. Connexion :
JustineAPI utilise le couple courriel mot de passe pour gérer l'authentification :

#### Endpoint : 
<b>POST</b> /auth/connexion

````java
//Java:
@POST("/auth/connexion")
Call<ComptePOJO> connecter(@Body LoginData loginData);
````

##### Body : 
```json
{
  "email":"prof1@test.com",
  "mot_de_passe":"secret"
}
```
##### Réponse :
code: 200 (OK)

body:
```json
{
"id": "e5797f8b-565e-40db-bbd9-6be2c0911a59",
"nom": "Prades",
"prenom": "Pierre",
"email": "prof1@test.com",
"type_compte": "PROFESSEUR",
"access_token": "ChsvHGglAKaFsLEtBopYPXN0prs0VtPy0bWxzACEwQmjLCdDOp",
"expires_at": "2023-11-24T20:06:59.588065900"
}
```
### b. Déconnexion :

Par la suite utiliser le token d'authentification reçu en réponse de la connexion pour vous authentifier.

#### Endpoint :
<b>POST</b> /auth/deconnexion

````java
    //Java:
    @POST("/auth/deconnexion")
    Call<ResponseBody> deconnecter(@Header("Authorization") String token);
````


## II.Comptes d'étudiants

### a. Créer un compte étudiant
#### Endpoint :
<b>POST</b> /inscription

#### Body :
````json
{
  "nom": "Jean",
  "prenom": "Dujardin",
  "email": "j.dujardin@test.com",
  "mot_de_passe": "motPasseTest",
  "mot_de_passe_confirmation": "motPasseTest"
}
````

#### Réponse
code: 201 (Created)
````json
{
  "id": "162e66ae-6753-4ea7-a830-927d7955e91e",
  "nom": "Jean",
  "prenom": "Dujardin",
  "email": "j.dujardin@test.com",
  "typeCompte": "ETUDIANT",
  "entreprises": null
}
````

### b. Lire les comptes
L'API permet de récupérer les comptes d'étudiants:

#### Endpoint :
<b>GET</b> /compte/getcomptesetudiantsactifs
````java
    // Java :
    @GET("/entreprise")
    Call<ResponseBody> getEntreprises(@Header("Authorization") String token);
````

##### Header :
```json
{
  "Authorization":"<token>"
}
```
##### Réponse :
code: 200 (OK)

```json
[
  {
    "id": "3310460e-b1e7-4ec8-a68b-1745fa1f2b11",
    "nom": "Leblanc",
    "prenom": "Kevin",
    "email": "kevin.leblanc@test.com",
    "typeCompte": "ETUDIANT",
    "entreprises": []
  },
  {
    "id": "e00b7a6b-d48d-4aa2-a8fc-9bcdc972d282",
    "nom": "Masson",
    "prenom": "Cédric",
    "email": "cedric.masson@test.com",
    "typeCompte": "ETUDIANT",
    "entreprises": [
      {
        "id": "22a04081-3c72-45d1-9603-0fdb3ddd4a91",
        "nom": "Boucherie Marien",
        "contact": "Toto",
        "email": "toto@test.com",
        "telephone": "514-555-5555",
        "siteWeb": "https://www.google.com/",
        "adresse": "1499-1415 Rue Jarry E",
        "ville": "Montreal",
        "province": "QC",
        "codePostal": "H2E 3B4",
        "dateContact": null,
        "estFavorite": false
      },
      {
        "id": "e862113f-3e32-4fb0-bda4-6705cc3dc571",
        "nom": "Épicerie les Jardinières",
        "contact": "Toto",
        "email": "toto@test.com",
        "telephone": "514-555-5555",
        "siteWeb": "https://www.google.com/",
        "adresse": "10345 Ave Christophe-Colomb",
        "ville": "Montreal",
        "province": "QC",
        "codePostal": "H2C 2V1",
        "dateContact": null,
        "estFavorite": false
      }
    ]
  }
]
```
### a. Créer une entreprise
L'API permet de créer une entreprise:

#### Endpoint :
<b>POST</b> /entreprise

##### Header :
```json
{
  "Authorization":"<token>"
}
```
##### Body :
```json
{
  "nom": "Pharmaprix",
  "contact": "Toto",
  "email": "toto@test.com",
  "telephone": "514-555-5555",
  "siteWeb": "https://www.google.com/",
  "adresse": "3611 Rue Jarry E",
  "ville": "Montreal",
  "province": "QC",
  "codePostal": "H1Z 2G1",
  "dateContact": null,
  "estFavorite": false
}
```
##### Réponse :
code: 200 (OK)

```json
{
  "id": "8541e9d7-dd43-417d-91aa-aaf8deb382a9",
  "nom": "Pharmaprix",
  "contact": "Toto",
  "email": "toto@test.com",
  "telephone": "514-555-5555",
  "siteWeb": "https://www.pharmaprix.ca/",
  "adresse": "3611 Rue Jarry E",
  "ville": "Montreal",
  "province": "QC",
  "codePostal": "H1Z 2G1",
  "dateContact": null,
  "estFavorite": false
}
```


### b. Lire les entreprises
L'API permet de récupérer les entreprises des étudiants:

#### Endpoint :
<b>GET</b> /entreprise

##### Header :
```json
{
  "Authorization":"<token>"
}
```
##### Réponse :
code: 200 (OK)

```json
[
  {
    "id": "22a04081-3c72-45d1-9603-0fdb3ddd4a91",
    "nom": "Boucherie Marien",
    "contact": "Toto",
    "email": "toto@test.com",
    "telephone": "514-555-5555",
    "siteWeb": "https://www.google.com/",
    "adresse": "1499-1415 Rue Jarry E",
    "ville": "Montreal",
    "province": "QC",
    "codePostal": "H2E 3B4",
    "dateContact": null,
    "estFavorite": false
  },
  {
    "id": "3a3d68b8-1ccb-4b34-877e-aeea6ea64a95",
    "nom": "Pharmaprix",
    "contact": "Toto",
    "email": "toto@test.com",
    "telephone": "514-555-5555",
    "siteWeb": "https://www.google.com/",
    "adresse": "3611 Rue Jarry E",
    "ville": "Montreal",
    "province": "QC",
    "codePostal": "H1Z 2G1",
    "dateContact": null,
    "estFavorite": false
  }
]
```
