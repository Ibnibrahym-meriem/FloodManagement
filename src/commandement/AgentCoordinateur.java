package commandement;

import madkit.kernel.Agent;
import madkit.kernel.Message;
import messages.NiveauEauMessage;
import messages.MeteoMessage;
import messages.OrdreMessage;
import messages.AlerteMessage;

/**
 * Agent Coordinateur - Groupe : Commandement - Role : Decideur
 * Recoit les donnees des capteurs et coordonne les interventions.
 */
public class AgentCoordinateur extends Agent {

    public static final String COMMUNAUTE = "GestionBarrage";
    public static final String GROUPE     = "Commandement";
    public static final String ROLE       = "Decideur";

    private double dernierNiveau    = 0.0;
    private String dernierStatut    = "NORMAL";

    @Override
    protected void activate() {
        getLogger().info("=== AgentCoordinateur active ===");
        createGroupIfAbsent(COMMUNAUTE, GROUPE);
        requestRole(COMMUNAUTE, GROUPE, ROLE);
        // Creer aussi le groupe Intervention pour que les agents s'y inscrivent
        createGroupIfAbsent(COMMUNAUTE, "Intervention");
        getLogger().info("Role acquis : " + ROLE + " dans " + GROUPE);
    }

    @Override
    protected void live() {
        while (isAlive()) {
            Message m = waitNextMessage(3000);

            if (m instanceof NiveauEauMessage) {
                traiterNiveauEau((NiveauEauMessage) m);
            } else if (m instanceof MeteoMessage) {
                traiterMeteo((MeteoMessage) m);
            } else if (m instanceof OrdreMessage) {
                OrdreMessage rapport = (OrdreMessage) m;
                if (rapport.getTypeOrdre() == OrdreMessage.TypeOrdre.RAPPORT_TERRAIN) {
                    getLogger().info("[Rapport terrain] " + rapport.getDetails());
                }
            }
        }
    }

    private void traiterNiveauEau(NiveauEauMessage msg) {
        dernierNiveau = msg.getNiveauActuel();
        String statut = msg.getStatut();

        getLogger().info("Niveau eau recu : " + msg);

        if (!statut.equals(dernierStatut)) {
            dernierStatut = statut;

            switch (statut) {
                case "CRITIQUE":
                    getLogger().severe("!!! SITUATION CRITIQUE - DECLENCHEMENT DES MESURES D'URGENCE !!!");
                    // Ouvrir les vannes en urgence
                    sendMessage(COMMUNAUTE, "Intervention", "Regulateur",
                        new OrdreMessage(OrdreMessage.TypeOrdre.OUVRIR_VANNES, "Urgence crue", 5));
                    // Evacuer les zones
                    sendMessage(COMMUNAUTE, "Intervention", "Organisateur",
                        new OrdreMessage(OrdreMessage.TypeOrdre.EVACUER_ZONE, "Zones A1,A2,B1,B2", 5));
                    // Declencher les secours
                    sendMessage(COMMUNAUTE, "Intervention", "Sauveteur",
                        new OrdreMessage(OrdreMessage.TypeOrdre.DECLENCHER_SECOURS, "Crue niveau critique", 5));
                    // Alerter la population
                    sendMessage(COMMUNAUTE, GROUPE, "Notificateur",
                        new AlerteMessage(AlerteMessage.NiveauAlerte.CRITIQUE,
                            "Crue critique - evacuation immediate", dernierNiveau));
                    break;

                case "ALERTE":
                    getLogger().warning("ALERTE - Niveau eau en hausse");
                    sendMessage(COMMUNAUTE, "Intervention", "Regulateur",
                        new OrdreMessage(OrdreMessage.TypeOrdre.OUVRIR_VANNES, "Alerte crue", 3));
                    sendMessage(COMMUNAUTE, GROUPE, "Notificateur",
                        new AlerteMessage(AlerteMessage.NiveauAlerte.ALERTE,
                            "Niveau d'alerte atteint", dernierNiveau));
                    break;

                case "NORMAL":
                    getLogger().info("Situation normale.");
                    sendMessage(COMMUNAUTE, "Intervention", "Regulateur",
                        new OrdreMessage(OrdreMessage.TypeOrdre.STANDBY, "Retour normal", 1));
                    sendMessage(COMMUNAUTE, GROUPE, "Notificateur",
                        new AlerteMessage(AlerteMessage.NiveauAlerte.NORMAL,
                            "Situation normalisee", dernierNiveau));
                    break;
            }
        }
    }

    private void traiterMeteo(MeteoMessage msg) {
        getLogger().info("Meteo recue : " + msg);
        if (msg.getRisqueCrue() >= 0.7) {
            getLogger().warning("Risque meteo eleve : " + msg.getPrevision());
        }
    }

    @Override
    protected void end() {
        getLogger().info("AgentCoordinateur arrete.");
    }
}
