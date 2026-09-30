# SAÉ 2.1 & 2.2 — Jeu hexagonal

Implémentation d'un jeu de stratégie hexagonal, avec moteur de jeu, IA Minimax et interface graphique JavaFX.

🎥 Démo vidéo : [lien YouTube non répertorié] 

## 📖 Contexte

Ce dépôt contient le rendu de la SAÉ 2.1 & 2.2 (BUT Informatique, Université de Caen Normandie, 2025-2026), réalisée en **3 jours** répartis en **2 livrables** :

1. **Livrable 1 — Modèle du jeu et IA** *(2 jours)* : conception du moteur de jeu (règles, coordonnées hexagonales, actions) et d'une intelligence artificielle basée sur l'algorithme **Minimax** avec élagage alpha-bêta.
2. **Livrable 2 — Interface graphique** *(1 jour)* : développement d'une interface JavaFX complète permettant de jouer au jeu, de configurer l'IA et de sauvegarder/charger une partie.

Cette branche `master` ne contient pas de code : elle sert uniquement à présenter le projet dans son ensemble. Le code de chaque phase est disponible sur des branches dédiées (voir Architecture ci-dessous).

## 🎮 Le jeu

Le jeu se déroule sur un plateau **hexagonal**. Chaque joueur (Noir / Blanc) débute avec 5 anneaux placés librement sur le terrain.

- Un anneau se déplace selon 6 axes (Nord-Ouest, Nord-Est, Est, Sud-Est, Sud-Ouest, Ouest).
- En se déplaçant, un anneau **crée ou retourne des pions** sur son passage.
- L'objectif est de former des **lignes de 5 pions** de sa couleur : quand une ligne est formée, le joueur retire un de ses anneaux ainsi que la ligne.
- Le **premier joueur à retirer 3 anneaux** remporte la partie.
- Si un joueur ne peut plus déplacer aucun anneau, la partie est déclarée **nulle**.

## ✨ Fonctionnalités

- Moteur de jeu complet respectant les règles du hexagonal ring game.
- Système de coordonnées hexagonales réutilisable (modes Cube et Doubled).
- IA basée sur l'algorithme Minimax avec élagage alpha-bêta et fonction d'évaluation pondérée (victoire/défaite, anneaux, lignes de 4, mobilité).
- Mode de jeu en ligne de commande (CUIMain).
- Interface graphique JavaFX : plateau interactif, gestion des modes d'interaction (édition, jeu, retrait de ligne), configuration de l'IA, sauvegarde/chargement de partie au format `.yns` sécurisé par un magic number.

## 🏗️ Architecture

Le projet est un projet **Maven** multi-module :

- **`HexagonalCoordinate`** : module indépendant gérant les systèmes de coordonnées hexagonales (`CoordinateCube`, `CoordinateDoubled`), les directions et la conversion en coordonnées 2D. Déclaré comme dépendance du projet principal pour permettre sa réutilisation.
- **Projet principal** : contient le modèle du jeu (`State`, `Model`, `Factory`), les actions (`Move`, `RemoveLine`), l'IA (`Node`, `MinimaxAI`) et, à partir du deuxième livrable, l'interface graphique JavaFX (contrôleurs, vues FXML).

**Organisation Git :**
- Branche `master` : présentation générale du projet (ce README), sans code.
- Branche `Jour1Et2` : code du premier livrable — modèle, coordonnées hexagonales et IA Minimax.
- Branche `Jour3` : code du second livrable — interface graphique JavaFX.

Chaque phase de développement a été réalisée sur des branches secondaires dédiées aux fonctionnalités, fusionnées ensuite dans les branches ci-dessus.

## 🛠️ Langages et technologies utilisés

- Java (records, tests unitaires JUnit)
- Maven (gestion multi-module)
- JavaFX (interface graphique, FXML, propriétés observables)

## ✍️ Auteurs

**Livrable 1 — Jour 1 & 2 :**
- MARECHAL Nils
- BROUILLARD Leopold
- CLEMENT Simon
- COLOGON Etienne
- DUMENIL Gaelig

**Livrable 2 — Jour 3 :**
- CHUQUET Anael
- GAUMONT Gabriel
- CLEMENT Simon
- DE MAZZI Leandro
