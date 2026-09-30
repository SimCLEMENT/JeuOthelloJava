# Livrable 2 — Interface graphique JavaFX

Interface graphique JavaFX permettant de jouer au jeu hexagonal développé lors du premier livrable, avec configuration de l'IA et sauvegarde/chargement de partie.

🎥 Démo vidéo : [lien YouTube non répertorié] 

## 📖 Contexte

Développer une interface graphique JavaFX permettant de jouer au jeu développé lors du premier livrable. Cette phase s'est déroulée sur **1 journée**.

## ✨ Fonctionnalités

- Plateau interactif avec trois modes d'interaction : édition libre, jeu classique, retrait de ligne.
- Configuration visuelle du plateau : couleurs des cases, affichage des coordonnées (Cubique/2D), épaisseur des bordures.
- Configuration de l'IA : ajustement des poids de l'heuristique, barre d'avantage en temps réel, suggestion du meilleur coup.
- Sauvegarde et chargement de partie dans un format de fichier personnalisé (`.yns`).
- Affichage de l'état du jeu en cours et pop-up de victoire.

## 🏗️ Architecture

L'interface se décompose en cinq zones, chacune gérée par un contrôleur dédié :

| Zone | Rôle |
|---|---|
| **Centrale** | Visualisation et interaction avec le plateau de jeu |
| **Jeu** | Nouvelle partie, mode édition, couleurs, coordonnées, épaisseur des bordures |
| **IA** | Configuration des scores de l'heuristique, évaluation de la situation, coup conseillé |
| **Supérieure (menu)** | Chargement / sauvegarde de partie, "À propos" |
| **Basse** | Affichage de l'état du jeu en cours (joueur actif, phase, mode d'interaction) |

Chaque sous-contrôleur référence le **`MainController`** via une méthode `setMainController(MainController)`, appelée depuis l'`initialize()` du contrôleur principal.

### Contrôleur principal — `MainController`

Centralise les propriétés observables partagées entre contrôleurs : couleur des hexagones, mode d'affichage des coordonnées, mode d'interaction, victoire, instance du modèle, etc.

**Mode d'interaction** — trois modes, modélisés par une classe abstraite `InteractionMode` (`handleClick`, `entered`, `exited`) :

1. **Édition** — ajout/suppression libre de pions et d'anneaux.
2. **Jeu classique** — déplacement d'un anneau (sélection → case cible).
3. **Retrait de ligne** — sélection d'une ligne puis de l'anneau à retirer.

Le changement de mode est géré dynamiquement via `Bindings.createObjectBinding`, avec priorité : édition > retrait de ligne > jeu classique.

**Victoire** — une `BooleanProperty` déclenche l'affichage d'une pop-up annonçant le joueur gagnant.

### Contrôleur de jeu

- 4 boutons de génération de partie (aléatoire + 3 situations de `Factory`).
- Checkbox mode édition avec sous-zone (choix pion/anneau et équipe via deux `ToggleGroup`).
- `ColorPicker` pour les 3 catégories de case (défaut : gris, gris foncé, gris clair).
- Checkbox d'affichage des coordonnées + `ListView` de sélection du mode (Cubique / 2D).
- `Slider` pour l'épaisseur des bordures (1 à 5).

### Contrôleur central

- **`HexSquare`** : spécialisation de `Polygon` représentant une case hexagonale (couleur, coordonnée, forme affichée, label).
- Construction du plateau à l'initialisation, stocké dans une `HashMap<Coordinate, HexSquare>`.
- Gestion des événements souris déléguée à l'`InteractionMode` courant.

### Contrôleur IA

- 4 `TextField` pour ajuster les poids de l'heuristique (anneaux, victoire, ligne de 4, etc.).
- `ProgressBar` affichant l'avantage courant (noir/blanc), mise à jour à chaque changement d'état.
- Bouton affichant, via une pop-up, le meilleur coup selon l'IA.

### Sauvegarde / chargement

Format de fichier personnalisé (extension `.yns`), sécurisé par un **magic number** `SAE212` (6 premiers octets) permettant de détecter un fichier corrompu ou invalide avant lecture.

Structure du fichier :
1. En-tête de sécurité : `SAE212`
2. État global : joueur courant, phase de jeu
3. Données du terrain : coordonnées occupées, type (pion/anneau) et couleur

Le chargement utilise un `FileChooser` filtré sur l'extension, vérifie le magic number, puis reconstruit l'état et force la mise à jour des contrôleurs.

## 🚀 Lancement du projet

```bash
mvn install
mvn javafx:run
```
*(commandes à exécuter dans le dossier `application`)*

## ✍️ Auteurs

- CHUQUET Anael
- GAUMONT Gabriel
- CLEMENT Simon
- DE MAZZI Leandro
