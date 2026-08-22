# Livrable 1 — Modèle du jeu & IA Minimax

## Objectif

Développer le moteur de jeu (règles, coordonnées hexagonales, actions) ainsi qu'une intelligence artificielle capable de jouer, à partir d'une situation donnée, le coup qui lui semble le plus pertinent. Cette phase s'est déroulée sur **2 jours**.

Fonctionnalités attendues à l'issue de cette phase :
- Possibilité de jouer au jeu en ligne de commande.
- Une IA renvoyant le coup le plus pertinent pour une situation donnée.

## Architecture

### Module `HexagonalCoordinate`

Module indépendant, déclaré en dépendance du projet principal, gérant la représentation des cases d'un plateau hexagonal :

- **`Coordinate`** : classe abstraite servant d'interface, indépendante du système de coordonnées choisi (`to2DCoordinate`, `toDir`, `getNeighbors`, `between`, directions N/NE/E/SE/S/SO/O).
- **`CoordinateCube`** : représentation en coordonnées cubiques `(q, r, s)` avec la contrainte `q + r + s = 0`.
- **`CoordinateDoubled`** : représentation en coordonnées "doubled" `(y, x)`.
- **`Direction`** (enum) et **`Mode`** (enum `FLAT` / `POINTY`) : gestion de l'orientation du plateau.
- **`Point`** (record) : coordonnée 2D résultant de la conversion.

### Modèle du jeu

- **`State`** : représentation immuable (record) d'une situation de jeu, stockant le plateau via une `HashMap<Coordinate, Token>`.
- **`IState`** : interface définissant les opérations `move` et `removeLine`, ainsi que `availableMoves`, `winner`, `lines`.
- **`Model`** : classe mutable encapsulant l'état courant du jeu et sa mise à jour.
- **`Token`** (`Pawn`, `Ring`) : les pions et anneaux, chacun associé à une équipe (`Team`).
- **`Factory`** (`FactoryCube`, `FactoryDoubled`) : génération de plateaux vierges et de situations de test prédéfinies.

### Actions

- **`Action`** : super-classe abstraite servant de type de référence.
- **`Move`** : déplacement d'un anneau (coordonnée de départ/arrivée).
- **`RemoveLine`** : retrait d'une ligne de 5 pions et d'un anneau associé.

### Intelligence artificielle

- **`Node`** : nœud de l'arbre de recherche, stockant l'état de jeu, le nœud parent et l'action ayant permis d'y accéder.
- **`AI`** (interface) : expose `Action chooseMove(IState state)`.
- **`MinimaxAI`** : implémentation de l'algorithme **Minimax** avec **élagage alpha-bêta**, explorant l'arbre des états jusqu'à une profondeur `d`.
- **Fonction d'évaluation** pondérée sur plusieurs critères : victoire/défaite, différentiel d'anneaux, lignes de 4 pions, mobilité des anneaux.
- **`MainAI`** : classe permettant de jouer une partie contre l'IA.

### Bonus

- **`CUIMain`** : interface en ligne de commande permettant de visualiser le plateau et de jouer une partie (génération aléatoire de 5 anneaux par équipe, affichage ASCII du terrain).

## Tests

Les classes s'y prêtant disposent de tests unitaires dans `src/test/java`, notamment sur les situations de test prédéfinies (`stateForWhiteLineTest`, `stateForBlackLineTest`, `testState`, `doubleLineStateTest`).

## Lancer le projet

```bash
mvn install
```

## Contributors

<!-- Ajouter ici la liste des membres du groupe -->
-
-
-
 
 
 Groupe de la journée 1 et 2 :
- MARECHAL Nils
- BROUILLARD Leopold
- CLEMENT Simon
- COLOGON Etienne
- DUMENIL Gaelig
