# PLD_AGILE


## Description
Notre projet vise à développer une application pour optimiser des tournées de livraison pour plusieurs livreurs.


L'application permet de :
- Choisir le nombre de livreurs.
- Charger un fichier XML décrivant une carte de la ville (intersections, segments de route, entrepôt).
- Charger un fichier de demande avec un entrepôt et des points de livraison
OU/ET
- Entrer manuellement un entrepôt et des points de livraison
- Changer la carte.
- Calculer et afficher les tournées les plus optimales de livraison sur la carte.
- Gérer plusieurs livreurs à la fois en regroupant les points par clustering.
- Ajouter ou supprimer un point de livraison après le calcul des tournées.
- Annuler ou refaire des opérations (plusieurs fois de suite);
- Afficher les durées de chaque tournée et les heures d'arrivée au point de livraison
- Exporter le fichier XML de la demande en cours pour le réutiliser ultérieurement
- Exporter une feuille de route avec toutes les informations pour les livreurs


# Projet Java avec Maven et JavaFX


Ce projet est une application Java développée avec **Maven** et utilisant **JavaFX** pour l'interface graphique. Ce guide vous expliquera comment configurer l'environnement, installer les dépendances, et lancer le projet.


---


## 🚀 Prérequis


### Outils nécessaires
- **Java 17** ou version supérieure ([JDK 17+](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) recommandé)
- **Maven** ([guide d'installation](https://maven.apache.org/install.html))
- Un IDE comme **Visual Studio Code**, **IntelliJ IDEA**, ou **Eclipse**.


---


## 🛠️ Installation et configuration


### 1. Installer Maven
#### Sur Windows
1. Téléchargez [Maven](https://maven.apache.org/download.cgi).
2. Dézippez le fichier téléchargé.
3. Ajoutez le chemin du dossier `bin` de Maven à la variable d'environnement `PATH`.
4. Vérifiez l'installation avec la commande :
   ```bash
   mvn -v


### 2. Installer JavaFx
#### Sur Windows
1. Téléchargez [JavaFx]( https://gluonhq.com/products/javafx/).
2. Dézippez le fichier téléchargé.
3. Ajoutez le chemin du dossier `bin` de Maven à la variable d'environnement `PATH`.


### 3. Mettre à jour les fichiers settings.json et pom.xml
Ajoutez les bonnes dépendances pour Maven et JavaFx


## Lancer le projet !




A bientôt!

