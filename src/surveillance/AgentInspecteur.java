package surveillance;

import madkit.kernel.Agent;
import madkit.kernel.Message;
import messages.OrdreMessage;

/**
 * Agent Inspecteur - Groupe : Surveillance - Role : Observateur
 * Inspecte la structure du barrage toutes les 8 secondes.
 */
public class AgentInspecteur extends Agent {

    public static final String COMMUNAUTE = "GestionBarrage";
    public static final String GROUPE     = "Surveillance";
    public static final String ROLE       = "Observateur";

    private int    cycle         = 0;
    private double pressionEau   = 1.0;
    private String etatStructure = "BON";

    @Override
    protected void activate() {
        getLogger().info("=== AgentInspecteur active ===");
        createGroupIfAbsent(COMMUNAUTE, GROUPE);
        requestRole(COMMUNAUTE, GROUPE, ROLE);
        getLogger().info("Role acquis : " + ROLE + " dans " + GROUPE);
    }

    @Override
    protected void live() {
        while (isAlive()) {
            // Lire les messages entrants
            Message msg = nextMessage();
            if (msg instanceof OrdreMessage) {
                getLogger().info("Ordre recu : " + msg);
            }

            inspecterStructure();
            pause(8000);
        }
    }

    private void inspecterStructure() {
        cycle++;
        pressionEau = 1.0 + cycle * 0.3;

        if (pressionEau < 3.0) {
            etatStructure = "BON";
        } else if (pressionEau < 5.0) {
            etatStructure = "ATTENTION - pression elevee";
        } else {
            etatStructure = "CRITIQUE - risque de rupture!";
        }

        getLogger().info("[Inspection] Pression=" + String.format("%.1f", pressionEau)
                + " bars | Structure : " + etatStructure);

        if (etatStructure.startsWith("CRITIQUE")) {
            OrdreMessage rapport = new OrdreMessage(
                OrdreMessage.TypeOrdre.RAPPORT_TERRAIN,
                "ALERTE STRUCTURELLE : " + etatStructure,
                5
            );
            sendMessage(COMMUNAUTE, "Commandement", "Decideur", rapport);
            getLogger().warning(">>> Rapport critique envoye au Coordinateur!");
        }
    }

    @Override
    protected void end() {
        getLogger().info("AgentInspecteur arrete. Dernier etat : " + etatStructure);
    }
}
