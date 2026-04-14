package intervention;

import madkit.kernel.Agent;
import madkit.kernel.Message;
import messages.OrdreMessage;

/**
 * Agent Vanne - Groupe Intervention - Role : Regulateur
 * Ouvre/ferme les vannes du barrage selon les ordres du Coordinateur.
 */
public class AgentVanne extends Agent {

    public static final String COMMUNAUTE = "GestionBarrage";
    public static final String GROUPE     = "Intervention";
    public static final String ROLE       = "Regulateur";

    private int    ouvertureVannes = 0;
    private String etatVannes      = "FERMEES";

    @Override
    protected void activate() {
        getLogger().info("=== AgentVanne active ===");
        createGroupIfAbsent(COMMUNAUTE, GROUPE);
        requestRole(COMMUNAUTE, GROUPE, ROLE);
        getLogger().info("Role acquis : " + ROLE + " dans " + GROUPE);
        getLogger().info("Vannes initialisees : fermees (0%)");
    }

    @Override
    protected void live() {
        while (isAlive()) {
            Message msg = waitNextMessage(5000);

            if (msg instanceof OrdreMessage) {
                executerOrdre((OrdreMessage) msg);
            }
        }
    }

    private void executerOrdre(OrdreMessage ordre) {
        getLogger().info("Ordre recu : " + ordre);

        switch (ordre.getTypeOrdre()) {
            case OUVRIR_VANNES:
                if (ordre.getPriorite() >= 5) {
                    ouvertureVannes = 100;
                    etatVannes      = "OUVERTURE MAXIMALE";
                } else {
                    ouvertureVannes = 50;
                    etatVannes      = "OUVERTURE PARTIELLE (50%)";
                }
                getLogger().warning(">>> VANNES : " + etatVannes
                        + " | Debit : " + calculerDebit() + " m3/s");
                envoyerRapport("Vannes ouvertes a " + ouvertureVannes + "%");
                break;

            case FERMER_VANNES:
                ouvertureVannes = 0;
                etatVannes      = "FERMEES";
                getLogger().info(">>> VANNES : " + etatVannes);
                envoyerRapport("Vannes fermees");
                break;

            case STANDBY:
                ouvertureVannes = 10;
                etatVannes      = "POSITION STANDARD (10%)";
                getLogger().info(">>> VANNES : " + etatVannes);
                break;

            default:
                getLogger().info("Ordre non applicable : " + ordre.getTypeOrdre());
        }
    }

    private double calculerDebit() {
        return Math.round((ouvertureVannes / 100.0) * 450.0 * 10.0) / 10.0;
    }

    private void envoyerRapport(String details) {
        sendMessage(COMMUNAUTE, "Commandement", "Decideur",
            new OrdreMessage(OrdreMessage.TypeOrdre.RAPPORT_TERRAIN,
                "AgentVanne : " + details + " | Debit=" + calculerDebit() + "m3/s", 2));
    }

    @Override
    protected void end() {
        getLogger().info("AgentVanne arrete. Etat final : " + etatVannes);
    }
}
