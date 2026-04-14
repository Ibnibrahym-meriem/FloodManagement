# GestionBarrage — SMA MadKit 5
## Guide de configuration de A à Z

---

## STRUCTURE DU PROJET

```
GestionBarrage/
├── .vscode/
│   └── settings.json          ← Config VSCode (classpath JAR)
├── lib/
│   └── madkit-5.3.2.jar       ← Bibliothèque MadKit (NE PAS TOUCHER)
├── bin/                       ← Fichiers .class compilés (auto-généré)
├── src/
│   ├── launcher/
│   │   └── Launcher.java      ← Point d'entrée principal
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

## ÉTAPE 1 — PRÉREQUIS

### 1.1 Installer Java JDK 11 ou supérieur
- Télécharger : https://adoptium.net/
- Choisir **JDK 21 LTS** (recommandé)
- Installer et noter le chemin d'installation

Vérifier l'installation :
```
java -version
javac -version
```
Les deux doivent afficher une version ≥ 11.

### 1.2 Installer Visual Studio Code
- Télécharger : https://code.visualstudio.com/
- Installer normalement

### 1.3 Installer l'extension Java dans VSCode
Ouvrir VSCode → Extensions (Ctrl+Shift+X) → Chercher et installer :
- **Extension Pack for Java** (Microsoft) — installe les 6 extensions Java en une fois

---

## ÉTAPE 2 — CRÉATION DU PROJET

### 2.1 Créer le dossier du projet
```
Créer le dossier : GestionBarrage/
```

### 2.2 Copier tous les fichiers sources
Copier exactement la structure indiquée ci-dessus dans votre dossier `GestionBarrage/`.

### 2.3 Vérifier la présence du JAR
S'assurer que le fichier `lib/madkit-5.3.2.jar` est bien présent.

---

## ÉTAPE 3 — CONFIGURATION VSCODE

### 3.1 Ouvrir le projet
```
VSCode → File → Open Folder → Sélectionner GestionBarrage/
```

### 3.2 Vérifier le fichier .vscode/settings.json
Ce fichier DOIT contenir exactement :
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

> ⚠️ Ne jamais mettre de chemin absolu (ex: C:\\Users\\...) dans ce fichier,
> car le projet ne fonctionnera que sur votre machine.

### 3.3 Attendre l'indexation Java
Après ouverture du projet, VSCode va indexer les fichiers Java.
Attendre que la barre de statut en bas affiche : ✓ Java Language Server Ready

### 3.4 Vérifier que le JAR est reconnu
- Dans l'explorateur VSCode, cliquer sur l'icône Java (menu gauche)
- Sous "JAVA PROJECTS", ouvrir GestionBarrage
- Vérifier que `madkit-5.3.2.jar` apparaît sous "Referenced Libraries"

Si le JAR n'apparaît pas :
1. Ctrl+Shift+P → "Java: Clean Java Language Server Workspace"
2. Cliquer "Restart and Delete"
3. Attendre le rechargement complet

---

## ÉTAPE 4 — COMPILATION

### Option A — Compilation automatique VSCode
VSCode compile automatiquement à chaque sauvegarde si le projet est bien configuré.
Vérifier l'onglet "PROBLEMS" (Ctrl+Shift+M) — il doit être vide.

### Option B — Compilation manuelle en ligne de commande
Ouvrir un terminal dans VSCode (Ctrl+`) :

**Windows :**
```cmd
cd GestionBarrage
javac -cp lib\madkit-5.3.2.jar -d bin -sourcepath src src\launcher\Launcher.java src\messages\*.java src\surveillance\*.java src\commandement\*.java src\intervention\*.java
```

**Linux / macOS :**
```bash
cd GestionBarrage
javac -cp lib/madkit-5.3.2.jar -d bin -sourcepath src \
  src/messages/*.java \
  src/surveillance/*.java \
  src/commandement/*.java \
  src/intervention/*.java \
  src/launcher/Launcher.java
```

---

## ÉTAPE 5 — EXÉCUTION

### Option A — Via VSCode
1. Ouvrir `src/launcher/Launcher.java`
2. Cliquer sur **Run** (lien au-dessus de la méthode `main`)
   ou appuyer sur **F5**

### Option B — En ligne de commande

**Windows :**
```cmd
java -cp "bin;lib\madkit-5.3.2.jar" launcher.Launcher
```

**Linux / macOS :**
```bash
java -cp "bin:lib/madkit-5.3.2.jar" launcher.Launcher
```

---

## ÉTAPE 6 — RÉSULTAT ATTENDU

Au lancement, vous devez voir dans la console :
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

Puis toutes les 5 secondes, les mesures des capteurs s'affichent,
et après quelques cycles, les alertes et interventions se déclenchent automatiquement.

---

## ERREURS FRÉQUENTES ET SOLUTIONS

| Erreur | Cause | Solution |
|--------|-------|----------|
| `import net cannot be resolved` | Mauvais import (`net.madkit` au lieu de `madkit`) | Utiliser `import madkit.kernel.*` |
| `Agent cannot be resolved` | JAR absent du classpath | Vérifier `settings.json` et Referenced Libraries |
| `live() must override` | Mauvaise classe parente | Tous les agents doivent étendre `Agent` (pas `AbstractAgent`) |
| `createGroupIfAbsent` undefined | Classe parente incorrecte | Utiliser `Agent` au lieu de `AbstractAgent` |
| `pause()` undefined | Classe parente incorrecte | Utiliser `Agent` (qui fournit `pause()`) |
| Aucune sortie au lancement | Classpath incorrect | Vérifier le séparateur `;` (Windows) ou `:` (Linux) |

---

## ARCHITECTURE DES AGENTS

```
[AgentCapteur]      → mesure eau          → [AgentCoordinateur]
[AgentMeteo]        → prevision meteo     → [AgentCoordinateur]
[AgentInspecteur]   → etat structure      → [AgentCoordinateur]
                                                    ↓
                              ┌─────────────────────┼─────────────────────┐
                              ↓                     ↓                     ↓
                        [AgentVanne]      [AgentEvacuateur]    [AgentSecouriste]
                        [AgentAlerte] ← notifie citoyens
```

### Groupes et Rôles MadKit

| Agent | Communauté | Groupe | Rôle |
|-------|-----------|--------|------|
| AgentCapteur | GestionBarrage | Surveillance | Mesureur |
| AgentMeteo | GestionBarrage | Surveillance | Predicteur |
| AgentInspecteur | GestionBarrage | Surveillance | Observateur |
| AgentCoordinateur | GestionBarrage | Commandement | Decideur |
| AgentAlerte | GestionBarrage | Commandement | Notificateur |
| AgentVanne | GestionBarrage | Intervention | Regulateur |
| AgentEvacuateur | GestionBarrage | Intervention | Organisateur |
| AgentSecouriste | GestionBarrage | Intervention | Sauveteur |
