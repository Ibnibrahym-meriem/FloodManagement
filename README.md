# 🌊 FloodManagement 

> Simulation d'un système de gestion de crue basé sur une architecture multi-agents avec MadKit 5.

---

## 📋 Table des matières

- [Aperçu](#aperçu)
- [Architecture des agents](#architecture-des-agents)
- [Structure du projet](#structure-du-projet)
- [Prérequis](#prérequis)
- [Installation & Configuration](#installation--configuration)
- [Compilation](#compilation)
- [Exécution](#exécution)
- [Résultat attendu](#résultat-attendu)
- [Erreurs fréquentes](#erreurs-fréquentes)

---

## Aperçu

Ce projet simule un **système de gestion de crue** autour d'un barrage. Des agents autonomes communiquent entre eux via la plateforme MadKit 5 pour surveiller le niveau de l'eau, coordonner les décisions et déclencher les interventions d'urgence.

**3 groupes d'agents :**

| Groupe | Rôle |
|--------|------|
| `Surveillance` | Mesure du niveau d'eau, météo, inspection structurelle |
| `Commandement` | Prise de décision, diffusion des alertes |
| `Intervention` | Contrôle des vannes, évacuation, secours |

---

## Architecture des agents

```
[AgentCapteur]      ──── mesure eau ──────────┐
[AgentMeteo]        ──── prévision météo ─────┤──▶ [AgentCoordinateur]
[AgentInspecteur]   ──── état structure ──────┘           │
                                                           │
                          ┌────────────────────────────────┼────────────────────────┐
                          ▼                                ▼                        ▼
                   [AgentVanne]                  [AgentEvacuateur]         [AgentSecouriste]
                   (Regulateur)                  (Organisateur)            (Sauveteur)

                   [AgentAlerte] ◀──── notifie citoyens ──[AgentCoordinateur]
```

### Groupes et rôles MadKit

| Agent | Communauté | Groupe | Rôle |
|-------|------------|--------|------|
| `AgentCapteur` | GestionBarrage | Surveillance | Mesureur |
| `AgentMeteo` | GestionBarrage | Surveillance | Predicteur |
| `AgentInspecteur` | GestionBarrage | Surveillance | Observateur |
| `AgentCoordinateur` | GestionBarrage | Commandement | Decideur |
| `AgentAlerte` | GestionBarrage | Commandement | Notificateur |
| `AgentVanne` | GestionBarrage | Intervention | Regulateur |
| `AgentEvacuateur` | GestionBarrage | Intervention | Organisateur |
| `AgentSecouriste` | GestionBarrage | Intervention | Sauveteur |

---

## Structure du projet

```
GestionBarrage/
├── .vscode/
│   └── settings.json          # Config VSCode — classpath JAR
├── lib/
│   └── madkit-5.3.2.jar       # Bibliothèque MadKit (ne pas modifier)
├── bin/                       # Fichiers .class compilés (généré auto)
├── src/
│   ├── launcher/
│   │   └── Launcher.java      # Point d'entrée principal
│   ├── messages/
│   │   ├── NiveauEauMessage.java
│   │   ├── MeteoMessage.java
│   │   ├── AlerteMessage.java
│   │   └── OrdreMessage.java
│   ├── surveillance/
│   │   ├── AgentCapteur.java
│   │   ├── AgentMeteo.java
│   │   └── AgentInspecteur.java
│   ├── commandement/
│   │   ├── AgentCoordinateur.java
│   │   └── AgentAlerte.java
│   └── intervention/
│       ├── AgentVanne.java
│       ├── AgentEvacuateur.java
│       └── AgentSecouriste.java
└── README.md
```

---

## Prérequis

| Outil | Version minimale | Lien |
|-------|-----------------|------|
| Java JDK | 11+ (JDK 21 LTS recommandé) | [adoptium.net](https://adoptium.net/) |
| Visual Studio Code | Dernière version | [code.visualstudio.com](https://code.visualstudio.com/) |
| Extension Pack for Java | (Microsoft) | Via VSCode Extensions |

**Vérifier l'installation Java :**
```bash
java -version
javac -version
# Les deux doivent afficher une version ≥ 11
```

---

## Installation & Configuration

### 1. Cloner le dépôt

```bash
git clone https://github.com/votre-utilisateur/GestionBarrage.git
cd GestionBarrage
```

### 2. Ouvrir dans VSCode

```
File → Open Folder → Sélectionner GestionBarrage/
```

### 3. Vérifier `.vscode/settings.json`

Ce fichier doit contenir :

```json
{
    "java.project.referencedLibraries": [
        "lib/**/*.jar"
    ],
    "java.project.sourcePaths": [
        "src"
    ],
    "java.compile.nullAnalysis.mode": "disabled"
}
```

> ⚠️ Ne jamais utiliser de chemin absolu (ex: `C:\Users\...`) dans ce fichier.

### 4. Attendre l'indexation

VSCode indexe automatiquement le projet. Attendre que la barre de statut affiche :
```
✓ Java Language Server Ready
```

### 5. Vérifier que le JAR est reconnu

Dans l'explorateur VSCode → icône Java → **JAVA PROJECTS** → `GestionBarrage` → **Referenced Libraries** → `madkit-5.3.2.jar` doit apparaître.

**Si le JAR n'apparaît pas :**
```
Ctrl+Shift+P → "Java: Clean Java Language Server Workspace" → Restart and Delete
```

---

## Compilation

### Option A — Automatique (VSCode)

VSCode compile à chaque sauvegarde. Vérifier l'onglet **PROBLEMS** `Ctrl+Shift+M` — il doit être vide.

### Option B — Ligne de commande

**Windows :**
```cmd
javac -cp lib\madkit-5.3.2.jar -d bin -sourcepath src ^
  src\messages\*.java ^
  src\surveillance\*.java ^
  src\commandement\*.java ^
  src\intervention\*.java ^
  src\launcher\Launcher.java
```

**Linux / macOS :**
```bash
javac -cp lib/madkit-5.3.2.jar -d bin -sourcepath src \
  src/messages/*.java \
  src/surveillance/*.java \
  src/commandement/*.java \
  src/intervention/*.java \
  src/launcher/Launcher.java
```

---

## Exécution

### Option A — Via VSCode

1. Ouvrir `src/launcher/Launcher.java`
2. Cliquer sur **Run** au-dessus de la méthode `main` (ou appuyer sur `F5`)

### Option B — Ligne de commande

**Windows :**
```cmd
java -cp "bin;lib\madkit-5.3.2.jar" launcher.Launcher
```

**Linux / macOS :**
```bash
java -cp "bin:lib/madkit-5.3.2.jar" launcher.Launcher
```

---

## Résultat attendu

Au démarrage, la console affiche l'activation de tous les agents :

```
=== Simulation - Gestion de crue (MadKit 5) ===
[AgentCapteur]      === AgentCapteur active ===
[AgentMeteo]        === AgentMeteo active ===
[AgentInspecteur]   === AgentInspecteur active ===
[AgentCoordinateur] === AgentCoordinateur active ===
[AgentAlerte]       === AgentAlerte active ===
[AgentVanne]        === AgentVanne active ===
[AgentEvacuateur]   === AgentEvacuateur active ===
[AgentSecouriste]   === AgentSecouriste active ===
```

Puis toutes les **5 secondes**, les mesures des capteurs s'affichent. Après quelques cycles, les niveaux d'alerte montent progressivement et les agents d'intervention se déclenchent automatiquement :

```
[AgentCoordinateur] ALERTE - Niveau eau en hausse
[AgentVanne]        >>> VANNES : OUVERTURE PARTIELLE (50%) | Debit : 225.0 m3/s
[AgentAlerte]       ALERTE DIFFUSEE AUX CITOYENS
...
[AgentCoordinateur] !!! SITUATION CRITIQUE !!!
[AgentVanne]        >>> VANNES : OUVERTURE MAXIMALE | Debit : 450.0 m3/s
[AgentEvacuateur]   >>> EVACUATION TOTALE DECLENCHEE ! — 3500 personnes
[AgentSecouriste]   >>> EQUIPES DE SECOURS DEPLOYEES !
```

---

## Erreurs fréquentes

| Erreur | Cause | Solution |
|--------|-------|----------|
| `import net cannot be resolved` | Import `net.madkit` incorrect | Utiliser `import madkit.kernel.*` |
| `Agent cannot be resolved to a type` | JAR absent du classpath | Vérifier `settings.json` → `lib/**/*.jar` |
| `live() must override supertype method` | Mauvaise classe parente | Tous les agents doivent étendre `Agent` (pas `AbstractAgent`) |
| `createGroupIfAbsent` undefined | Classe parente incorrecte | Utiliser `madkit.kernel.Agent` |
| `pause()` undefined | Classe parente incorrecte | Utiliser `Agent` (qui fournit `pause()`) |
| Aucune sortie au lancement | Classpath incorrect | Windows: `;` comme séparateur / Linux: `:` |

---

## Technologies

![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk)
![MadKit](https://img.shields.io/badge/MadKit-5.3.2-blue)
![VSCode](https://img.shields.io/badge/VSCode-compatible-007ACC?logo=visualstudiocode)

---

